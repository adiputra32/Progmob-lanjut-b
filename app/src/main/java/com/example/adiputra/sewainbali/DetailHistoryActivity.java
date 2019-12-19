package com.example.adiputra.sewainbali;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.adiputra.sewainbali.apiHelper.BaseApiService;
import com.example.adiputra.sewainbali.apiHelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import static androidx.annotation.InspectableProperty.ValueType.COLOR;
import static com.example.adiputra.sewainbali.EditAccountActivity.GET_FROM_GALLERY;

public class DetailHistoryActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private TextView tvHargaMotor,tvNamaMotor,tvJenisMotor,tvPemilik,tvPaymentStatus,tvPayment,
            tvLokasi,tvPaymentProof,tvDays,tvHargaDetail,tvDenda, tvAsuransi,tvHargaTotal,tvDate,
            tvPaymentLimit;
    private ImageView imgMotor;
    private ProgressDialog loading;
    private LinearLayout lyPaymentProof,lyPaymentStts;
    private Button btnCancel,btnUpload;
    private boolean paymentStts = false;
    private Bitmap bitmap, tmpImgPP;
    private String idSewa;
    private String buktiPembayaran = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        tvHargaMotor = (TextView) findViewById(R.id.tv_harga_motor);
        tvNamaMotor = (TextView) findViewById(R.id.tv_nama_motor);
        tvJenisMotor = (TextView) findViewById(R.id.tv_jenis_motor);
        tvPemilik = (TextView) findViewById(R.id.tv_pemilik);
        tvPaymentStatus = (TextView) findViewById(R.id.tv_payment_status);
        tvPaymentLimit = (TextView) findViewById(R.id.tv_payment_limit);
        tvLokasi = (TextView) findViewById(R.id.tv_lokasi);
        tvPaymentProof = (TextView) findViewById(R.id.tv_payment_proof);
        tvDays = (TextView) findViewById(R.id.tv_days);
        tvHargaDetail = (TextView) findViewById(R.id.tv_harga_detail);
        tvDenda = (TextView) findViewById(R.id.tv_denda);
        tvAsuransi = (TextView) findViewById(R.id.tv_asuransi);
        tvHargaTotal = (TextView) findViewById(R.id.tv_harga_total);
        tvPayment = (TextView) findViewById(R.id.tv_payment);
        tvDate = (TextView) findViewById(R.id.tv_date);
        imgMotor = (ImageView) findViewById(R.id.img_motor);
        lyPaymentProof = (LinearLayout) findViewById(R.id.ly_payment_proof);
        lyPaymentStts = (LinearLayout) findViewById(R.id.ly_payment_stts);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnUpload = (Button) findViewById(R.id.btn_send_pp);

        loading = ProgressDialog.show(DetailHistoryActivity.this, null, "Please wait...", true, false);

        mApiService = UtilsApi.getAPIService();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idSewa = getIntent().getStringExtra("IDSEWA2");
        getDetailSewa(idSewa);

//        if (buktiPembayaran != null){
//            btnUpload.setText("Show");
//            tvPaymentProof.setText("Uploaded");
//            tvPaymentProof.setTextColor(Color.parseColor("#000000"));
//        }
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

    public void ChatOwner(View v){
        Intent intent = new Intent(DetailHistoryActivity.this, MainActivity.class);
        intent.putExtra("FRAGMENT","chat");
        startActivity(intent);
    }
    
    public void uploadPP(View v){
        if (paymentStts){
            Intent intent = new Intent(DetailHistoryActivity.this, SingleViewActivity.class);
            intent.putExtra("URL",buktiPembayaran);
            startActivity(intent);
        } else {
            String up = btnUpload.getText().toString();
            Toast.makeText(this, up, Toast.LENGTH_SHORT).show();
            if (up.equals("Send")){
                uploadPaymentProof(tmpImgPP, idSewa);
                lyPaymentProof.setVisibility(View.GONE);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, GET_FROM_GALLERY);

            }
        }
    }

    private void uploadPaymentProof(Bitmap tmpImgPP, String idSewa) {
        File ppFile = createTempFile(tmpImgPP);
        RequestBody ppName = RequestBody.create(MediaType.parse("image/*"), ppFile);
        MultipartBody.Part ppMultipart = MultipartBody.Part.createFormData("ppimage", ppFile.getName(), ppName);
        mApiService.uploadPPRequest(idSewa,ppMultipart,ppName)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.dismiss();
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    Toast.makeText(DetailHistoryActivity.this, "Payment Proof is successfully uploaded!", Toast.LENGTH_SHORT).show();
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        recreate();
                                    } else {
                                        Intent intent = getIntent();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        finish();
                                        overridePendingTransition(0, 0);

                                        startActivity(intent);
                                        overridePendingTransition(0, 0);
                                    }
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
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        loading.dismiss();
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(DetailHistoryActivity.this, "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RequestOptions myOptions = new RequestOptions()
                .fitCenter() // or centerCrop
                .override(200, 200);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Glide.with(this)
                    .asBitmap()
                    .apply(myOptions)
                    .load(bitmap)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            tmpImgPP = resource;
                        }
                    });

            if (bitmap != null){
                btnUpload.setText("Send");
                tvPaymentProof.setText("Payment proof ready to send!");
                tvPaymentProof.setTextColor(Color.parseColor("#8BC34A"));
            }
        }
    }

    private void getDetailSewa(String idSewa){
        mApiService.historyDetailRequest(idSewa)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.dismiss();
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
//                                    id_motor = jsonRESULTS.getJSONObject("motor").getString("id_motor");
                                    String hargaMotor = jsonRESULTS.getJSONObject("motor").getString("harga");
                                    String hargaMotorTotal = jsonRESULTS.getJSONObject("motor").getString("total_harga");

                                    String hargaMotorRes = getRupiah(hargaMotor);
                                    String hargaMotorTotalRes = getRupiah(hargaMotorTotal);

                                    tvHargaMotor.setText(hargaMotorRes);
                                    tvHargaDetail.setText(hargaMotorTotalRes);
                                    tvHargaTotal.setText(hargaMotorTotalRes);

                                    Log.d("Tag Harga",hargaMotor+hargaMotorTotal);
                                    tvNamaMotor.setText(jsonRESULTS.getJSONObject("motor").getString("merk"));
                                    tvJenisMotor.setText(jsonRESULTS.getJSONObject("motor").getString("jenis_motor"));
                                    tvPemilik.setText(jsonRESULTS.getJSONObject("motor").getString("name"));
                                    String payment = jsonRESULTS.getJSONObject("motor").getString("pembayaran");
                                    tvPayment.setText("Payment Status (" + payment + ")");
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("denda")){
                                        String denda = jsonRESULTS.getJSONObject("motor").getString("denda");
                                        tvDenda.setText("Rp" + denda + "/Hour");
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("asuransi")){
                                        String asuransi = jsonRESULTS.getJSONObject("motor").getString("asuransi");
                                        tvAsuransi.setText("Rp" + asuransi);
                                    }
                                    if (!jsonRESULTS.getJSONObject("motor").isNull("gambar_motor")){
                                        Drawable jenis;
                                        switch (tvJenisMotor.getText().toString()) {
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
                                        String gambarMotor = jsonRESULTS.getJSONObject("motor").getString("gambar_motor");
                                        Glide.with(DetailHistoryActivity.this)
                                                .load("https://kelompok23.000webhostapp.com/images/"+gambarMotor)
                                                .placeholder(jenis)
                                                .into(imgMotor);
                                    }
//                                    String sttsPembayaran = jsonRESULTS.getJSONObject("motor").getString("status_pembayaran");
//                                    if (sttsPembayaran.equals("1")){
//                                        tvPaymentStatus.setText("Success");
//                                        tvPaymentStatus.setTextColor(Color.parseColor("#8BC34A"));
//                                    } else {
//                                        tvPaymentStatus.setText("Waiting for Payment");
////                                        tvPaymentLimit.setText(jsonRESULTS.getJSONObject("motor").getString("batas_pembayaran") + " hours left");
//                                    }
                                    String hari = jsonRESULTS.getJSONObject("motor").getString("jumlah_hari");
                                    tvDays.setText(hari + " Day(s)");
                                    tvDate.setText(jsonRESULTS.getJSONObject("motor").getString("tgl"));
                                    tvLokasi.setText(jsonRESULTS.getJSONObject("motor").getString("lokasi"));
                                    Log.d("lokasii",jsonRESULTS.getJSONObject("motor").getString("lokasi"));

                                    String sttsSewa = jsonRESULTS.getJSONObject("motor").getString("status_sewa");
                                    Log.d("status_sewa",jsonRESULTS.getJSONObject("motor").getString("status_sewa"));

                                    switch (sttsSewa) {
                                        case "Belum Lunas":
//                                            btnCancel.setVisibility(View.INVISIBLE);
                                            if (payment.equals("Cash")){
                                                lyPaymentProof.setVisibility(View.GONE);
                                            } else {
                                                lyPaymentProof.setVisibility(View.VISIBLE);
                                                if (!jsonRESULTS.getJSONObject("motor").isNull("bukti_pembayaran")){
                                                    paymentStts = true;
                                                    tvPaymentProof.setText("Payment proof has been uploaded");
                                                    tvPaymentProof.setTextColor(Color.parseColor("#8BC34A"));
                                                    btnUpload.setText("Show");
                                                    tvPaymentStatus.setText("Waiting for approvement");
                                                    btnCancel.setVisibility(View.GONE);
                                                    buktiPembayaran = jsonRESULTS.getJSONObject("motor").getString("bukti_pembayaran");
                                                } else {
                                                    tvPaymentStatus.setText("Waiting for payment");
                                                }
                                            }
                                            break;
                                        case "Lunas":
                                            tvPaymentStatus.setText("Success");
                                            tvPaymentStatus.setTextColor(Color.parseColor("#8BC34A"));
                                            tvPaymentLimit.setText("");
                                            btnCancel.setVisibility(View.GONE);
                                            if (payment.equals("CASH")){
                                                lyPaymentProof.setVisibility(View.GONE);
                                            } else{
                                                lyPaymentProof.setVisibility(View.VISIBLE);
                                            }
                                            paymentStts = true;
                                            break;
                                        case "Batal":
                                            tvPaymentStatus.setText("Cancel");
                                            tvPaymentLimit.setText("");
                                            lyPaymentProof.setVisibility(View.GONE);
                                            btnCancel.setVisibility(View.GONE);
                                            break;
                                    }


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
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        loading.dismiss();
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(DetailHistoryActivity.this, "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_image.png");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,0, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void cancelOrder(View v){
        cancelOrderRequest(idSewa);
    }

    private void cancelOrderRequest(String idSewa) {
        mApiService.cancelOrderRequest(idSewa)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.dismiss();
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    Toast.makeText(DetailHistoryActivity.this, "Your order has been canceled", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(DetailHistoryActivity.this, SearchMotorActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("JENIS","ALL");
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
                            Log.i("debug", "onResponse: GA BERHASIL");
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        loading.dismiss();
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(DetailHistoryActivity.this, "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String getRupiah(String price){
        double harga = Double.parseDouble(price);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Log.d("harga","harganya " + formatRupiah.format((double)harga));
        String priceRes = formatRupiah.format((double)harga);

        return priceRes;
    }
}
