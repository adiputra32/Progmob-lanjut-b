package com.example.adiputra.sewainbali;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adiputra.sewainbali.apiHelper.BaseApiService;
import com.example.adiputra.sewainbali.apiHelper.UtilsApi;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailMotorActivity extends AppCompatActivity {
    CarouselView carouselView;
    private Button btnCheckout;
    private ProgressDialog loading;
    //    int[] motor = {R.drawable.motor1, R.drawable.motor2, R.drawable.motor3};
    private BaseApiService mApiService;
    private String gbrMotor, fine, insurance,hargaMotor,emailPemilik,tokenPemilik;
    private TextView tvHarga,tvNama,tvPemilik,tvMerk,tvYear,tvColor,tvCC,tvFuel,tvHelmet,tvDetail,tvHarga2,tvType,gambar;
    private Drawable jenis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_motor);

        gambar = (TextView) findViewById(R.id.gambarnya);
        tvType = (TextView) findViewById(R.id.tv_jenis);
        tvHarga = (TextView) findViewById(R.id.tv_harga);
        tvNama = (TextView) findViewById(R.id.tv_nama_motor);
        tvPemilik = (TextView) findViewById(R.id.tv_pemilik);
        tvMerk = (TextView) findViewById(R.id.tv_merk);
        tvYear = (TextView) findViewById(R.id.tv_year);
        tvColor = (TextView) findViewById(R.id.tv_color);
        tvCC = (TextView) findViewById(R.id.tv_cc);
        tvFuel = (TextView) findViewById(R.id.tv_fuel);
        tvHelmet = (TextView) findViewById(R.id.tv_helmet);
        tvDetail = (TextView) findViewById(R.id.tv_detail);
        tvHarga2 = (TextView) findViewById(R.id.tv_harga_2);

        mApiService = UtilsApi.getAPIService();

        final String idMotor = getIntent().getStringExtra("IDMOTOR");
        gbrMotor = getIntent().getStringExtra("GBRMOTOR");

        loading = ProgressDialog.show(DetailMotorActivity.this, null, "Please wait...", true, false);
        getMotorDetail(idMotor);

        carouselView = (CarouselView) findViewById(R.id.img_motor);
        carouselView.setPageCount(3);
        carouselView.setImageListener(imageListener);
        btnCheckout = (Button) findViewById(R.id.btn_checkout);

        loading.dismiss();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailMotorActivity.this, CheckOutActivity.class);
                intent.putExtra("HARGA",hargaMotor);
                intent.putExtra("MERK",tvMerk.getText().toString());
                intent.putExtra("JENISMOTOR",tvType.getText().toString());
                intent.putExtra("PEMILIK",tvPemilik.getText().toString());
                intent.putExtra("DENDA",fine);
                intent.putExtra("URLGAMBAR",gbrMotor);
                intent.putExtra("ASURANSI",insurance);
                intent.putExtra("EMAIL",emailPemilik);
                intent.putExtra("IDMOTOR",idMotor);
                startActivity(intent);
                finish();
            }
        });

        getSupportActionBar().setTitle("Detail Motor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
//            imageView.setImageResource(motor[position]);
//            Log.D("motorku",gambarMotor);
            switch (tvType.getText().toString()) {
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

            Glide.with(DetailMotorActivity.this)
                    .load("https://kelompok23.000webhostapp.com/images/"+gbrMotor)
                    .placeholder(jenis)
                    .into(imageView);

        }
    };

    private void getMotorDetail(String idMotor){
        mApiService.motorDetailRequest(idMotor)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        loading.dismiss();
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
//                                    id_motor = jsonRESULTS.getJSONObject("motor").getString("id_motor");
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("harga")){
                                        double harga = Double.parseDouble(jsonRESULTS.getJSONObject("motor").getString("harga"));

                                        Locale localeID = new Locale("in", "ID");
                                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                                        Log.d("harga","harganya " + formatRupiah.format((double)harga));
                                        String price = formatRupiah.format((double)harga);
                                        String price2 = formatRupiah.format((double)harga) + "/day";
                                        hargaMotor = jsonRESULTS.getJSONObject("motor").getString("harga");
                                        tvHarga.setText(price);
                                        tvHarga2.setText(price2);
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("merk")){
                                        tvNama.setText(jsonRESULTS.getJSONObject("motor").getString("merk"));
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("name")){
                                        tvPemilik.setText(jsonRESULTS.getJSONObject("motor").getString("name"));
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("merk")){
                                        tvMerk.setText(jsonRESULTS.getJSONObject("motor").getString("merk"));
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("jenis_motor")){
                                        tvType.setText(jsonRESULTS.getJSONObject("motor").getString("jenis_motor"));
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("tahun")){
                                        tvYear.setText(jsonRESULTS.getJSONObject("motor").getString("tahun"));
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("warna")){
                                        tvColor.setText(jsonRESULTS.getJSONObject("motor").getString("warna"));
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("CC")){
                                        tvCC.setText(jsonRESULTS.getJSONObject("motor").getString("CC"));
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("bahan_bakar")){
                                        tvFuel.setText(jsonRESULTS.getJSONObject("motor").getString("bahan_bakar"));
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("helm")){
                                        tvHelmet.setText(jsonRESULTS.getJSONObject("motor").getString("helm") + " unit");
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("detail")){
                                        tvDetail.setText(jsonRESULTS.getJSONObject("motor").getString("detail"));
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("denda")){
                                        fine = jsonRESULTS.getJSONObject("motor").getString("denda");
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("asuransi")){
                                        insurance = jsonRESULTS.getJSONObject("motor").getString("asuransi");
                                    }

                                    emailPemilik = jsonRESULTS.getJSONObject("motor").getString("email");
                                    tokenPemilik = jsonRESULTS.getJSONObject("motor").getString("token");

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
                            Log.i("debug", "onResponse: GA BERHASIL");
//                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        loading.dismiss();
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(DetailMotorActivity.this, "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
                    }
                });
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
}
