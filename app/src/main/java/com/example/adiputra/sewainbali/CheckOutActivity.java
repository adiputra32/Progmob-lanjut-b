package com.example.adiputra.sewainbali;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adiputra.sewainbali.apiHelper.BaseApiService;
import com.example.adiputra.sewainbali.apiHelper.UtilsApi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {
    private Button btnRent;
    private ImageButton btnAdd, btnSub, btnDatePicker;
    private EditText edtDays;
    private int days, hargaTotal;
    private long hargaRes;
    private String idMotor;
    private TextView tvHarga,tvMerk,tvJenis,tvPemilik,tvHargaDetail,tvDenda,tvAsuransi,tvHargaTotal,tvHarga2,tvDate;
    private EditText edtLokasi;
    private RadioButton rbPayment, rbCash;
    private RadioGroup rgPayment;
    private BaseApiService mApiService;
    private ProgressDialog loading;
    private ImageView imgMotor;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        mApiService = UtilsApi.getAPIService();

        btnRent = (Button) findViewById(R.id.btn_rent);
        btnAdd = (ImageButton) findViewById(R.id.btn_add_book);
        btnSub = (ImageButton) findViewById(R.id.btn_sub_book);
        btnDatePicker = (ImageButton) findViewById(R.id.btn_date_picker);
        edtDays = (EditText) findViewById(R.id.edt_days);
        tvHarga = (TextView) findViewById(R.id.tv_harga_motor);
        tvJenis = (TextView) findViewById(R.id.tv_jenis_motor);
        tvMerk = (TextView) findViewById(R.id.tv_nama_motor);
        tvPemilik = (TextView) findViewById(R.id.tv_pemilik);
        edtLokasi = (EditText) findViewById(R.id.edt_lokasi);
        tvHargaDetail = (TextView) findViewById(R.id.tv_harga_detail);
        tvDenda = (TextView) findViewById(R.id.tv_denda);
        tvAsuransi = (TextView) findViewById(R.id.tv_asuransi);
        tvHargaTotal = (TextView) findViewById(R.id.tv_harga_total);
        tvHarga2 = (TextView) findViewById(R.id.tv_harga2);
        rgPayment = (RadioGroup) findViewById(R.id.rgbtn_payment);
        tvDate = (TextView) findViewById(R.id.tv_date);
        imgMotor = (ImageView) findViewById(R.id.img_motor);
        rbCash = (RadioButton) findViewById(R.id.rbtn_cash);

        rbCash.setSelected(true);

        String harga = getIntent().getStringExtra("HARGA");
        String denda = getIntent().getStringExtra("DENDA");
        String asuransi = getIntent().getStringExtra("ASURANSI");
        String gbrMotor = getIntent().getStringExtra("URLGAMBAR");
        final String emailPemilik = getIntent().getStringExtra("EMAIL");

        idMotor = getIntent().getStringExtra("IDMOTOR");
        tvHarga.setText(getRupiah(harga) + "/day");
        tvMerk.setText(getIntent().getStringExtra("MERK"));
        tvPemilik.setText(getIntent().getStringExtra("PEMILIK"));
        tvHargaDetail.setText(getRupiah(harga));
        if (denda.equals("null")){
            tvDenda.setText("Rp-/Hour");
        } else {
            tvDenda.setText(denda + "/Hour");
        }
        if (asuransi.equals("null")){
            tvDenda.setText("Rp-");
        } else {
            tvAsuransi.setText(asuransi);
        }
        tvHargaTotal.setText(getRupiah(harga));
        tvHarga2.setText(getRupiah(harga));
        tvJenis.setText(getIntent().getStringExtra("JENISMOTOR"));

        Drawable jenis;
        switch (tvJenis.getText().toString()) {
            case "Matic":
                jenis = getResources().getDrawable(R.drawable.jenis_matic_2);
                break;
            case "Standard":
                jenis = getResources().getDrawable(R.drawable.jenis_standard);
                break;
            case "Sport":
                jenis = getResources().getDrawable(R.drawable.jenis_sport);
                break;
            case "Trail":
                jenis = getResources().getDrawable(R.drawable.jenis_trail);
                break;
            default:
                jenis = getResources().getDrawable(R.drawable.jenis_matic);
                break;
        }
        Glide.with(this)
                .load("https://kelompok23.000webhostapp.com/images/"+gbrMotor)
                .placeholder(jenis)
                .into(imgMotor);

        days = Integer.parseInt(edtDays.getText().toString());
        hargaTotal = Integer.parseInt(harga);

        edtDays.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                days = Integer.parseInt(edtDays.getText().toString());
                if (days > 30){
                    days = 30;
                    edtDays.setText(String.valueOf(days));
                    Toast.makeText(CheckOutActivity.this, "Maximum to rent is 30 days", Toast.LENGTH_SHORT).show();
                } else if (days < 1) {
                    days = 1;
                    edtDays.setText(String.valueOf(days));
                    Toast.makeText(CheckOutActivity.this, "Minimum to rent is 1 days", Toast.LENGTH_SHORT).show();
                } else {
                    days = 1;
                    edtDays.setText(String.valueOf(days));
                }
                return false;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (days < 30){
                    days += 1;
                    hargaRes = hargaTotal*days;
                    edtDays.setText(String.valueOf(days));
                    String harga = getRupiah(String.valueOf(hargaRes));
                    tvHargaTotal.setText(harga);
                    tvHarga2.setText(harga);
                }
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (days > 1){
                    days -= 1;
                    hargaRes = hargaTotal*days;
                    edtDays.setText(String.valueOf(days));
                    String harga = getRupiah(String.valueOf(hargaRes));
                    tvHargaTotal.setText(harga);
                    tvHarga2.setText(harga);
                }
            }
        });

        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sttsAccount = Preferences.getKeyStatusAktif(CheckOutActivity.this);
                Log.d("TAGKUAS",sttsAccount);
                if (sttsAccount.equals("Active")){
                    loading = ProgressDialog.show(CheckOutActivity.this, null, "Please wait...", true, false);
                    int selectedId = rgPayment.getCheckedRadioButtonId();
                    rbPayment = (RadioButton) findViewById(selectedId);
                    String payment = rbPayment.getText().toString();
                    if (payment.equals("Bank Payment")) payment = "Bank";
                    String email = Preferences.getLoggedInUser(CheckOutActivity.this);
                    String lokasi = edtLokasi.getText().toString();
                    String hari = String.valueOf(days);
                    String tgl = tvDate.getText().toString();

                    if (!TextUtils.isEmpty(lokasi) || !tgl.equals("-")){
                        sendCheckout(email,idMotor,lokasi,hari,payment,tgl,emailPemilik);
                    } else {
                        Toast.makeText(CheckOutActivity.this, "You can't go to next step, maybe you forgot something in location, date, or payment method", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CheckOutActivity.this, "Activate your account first!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CheckOutActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            myCalendar.set(Calendar.YEAR,year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();
        }
    };


    private void updateLabel(){
        String myCalFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myCalFormat, Locale.ROOT);

        tvDate.setText(sdf.format(myCalendar.getTime()));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                if (getParentActivityIntent() == null){
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void DetailMotor(View view){
        Intent intent = new Intent(CheckOutActivity.this, DetailMotorActivity.class);
        startActivity(intent);
    }

    public String getRupiah(String price){
        double harga = Double.parseDouble(price);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format((double)harga);
    }

    private void sendCheckout(String email, String id, String lokasi, String hari, String pembayaran, String tanggal, final String emailPemilik){
        mApiService.tambahSewaRequest(email,id,lokasi,hari,pembayaran,tanggal)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.dismiss();
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    String idSewa = jsonRESULTS.getJSONObject("motor").getString("id_sewa");

                                    Intent intent = new Intent(CheckOutActivity.this, LandingSuccessActivity.class);
                                    intent.putExtra("IDSEWA",idSewa);
                                    intent.putExtra("EMAIL2",emailPemilik);
                                    finish();
                                    startActivity(intent);
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Log.d("errorAPI", "errornya : " + error_message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                            Log.i("debug", "onResponse: GA BERHASIL");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(CheckOutActivity.this, "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
