package com.example.adiputra.sewainbali;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class EditAccountActivity extends AppCompatActivity {

    public static final int GET_FROM_GALLERY = 3;
    CircleImageView imgPP;
    ImageView imgIC;
    Bitmap bitmap,tmpImgPP,tmpImgIC;
    EditText edt_birthdate, edt_name, edt_email, edt_phone, edt_address;
    Calendar myCalendar = Calendar.getInstance();
    Cursor cursor;
    DatabaseHandler dbHandler;
    String iTag;
    ProgressDialog loading;
    BaseApiService mApiService;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        imgPP = (CircleImageView) findViewById(R.id.img_profile);
        imgIC = (ImageView) findViewById(R.id.img_id_card);
        edt_birthdate = (EditText) findViewById(R.id.et_birthdate);
        edt_name = (EditText) findViewById(R.id.et_name);
        edt_email = (EditText) findViewById(R.id.et_phone);
        edt_phone = (EditText) findViewById(R.id.et_password);
        edt_address = (EditText) findViewById(R.id.et_address);

        mApiService = UtilsApi.getAPIService();
        getWindow().setStatusBarColor(Color.parseColor("#262ca6"));

        loading = ProgressDialog.show(this, null, "Please wait...", true, false);
        edit();

//        dbHandler = new DatabaseHandler(this);

//        SQLiteDatabase dbRead = dbHandler.getReadableDatabase();
//        cursor = dbRead.rawQuery("SELECT * FROM users WHERE email = '" +
//                 edt_email.getText().toString() + "'",null);
//        cursor.moveToFirst();
//        if(cursor.getCount()>0){
//            cursor.moveToPosition(0);
//            edt_name.setText(cursor.getString(1));
//            edt_email.setText(cursor.getString(2));
//            edt_phone.setText(cursor.getString(6));
//            String bd = cursor.getString(4);
//            edt_address.setText(cursor.getString(5));
//            if (bd != null){
//                edt_birthdate.setText(bd);
//            }
//
//        } else{
//            Toast.makeText(this, "Account not found!", Toast.LENGTH_LONG).show();
//        }
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

    public void birthdate(View v){
        new DatePickerDialog(EditAccountActivity.this, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel(){
        String myCalFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myCalFormat, Locale.ROOT);

        edt_birthdate.setText(sdf.format(myCalendar.getTime()));
    }

    public void validatorSave(){
        edt_name.setError(null);
        View fokus = null;
        boolean cancel = false;

        String name = edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        String address = edt_address.getText().toString();
//        String birthdate = edt_birthdate.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateBD = null;
        try {
            dateBD = sdf.parse(edt_birthdate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String birthdate = df.format(dateBD);
        String email = edt_email.getText().toString();
        File ppFile = null,icFile = null;
        MultipartBody.Part ppMultipart = null, icMultipart = null;
        RequestBody ppName = null, icName = null;

        if (tmpImgPP != null) {
            ppFile = createTempFile(tmpImgPP);
            ppName = RequestBody.create(MediaType.parse("image/*"), ppFile);
            ppMultipart = MultipartBody.Part.createFormData("userimage", ppFile.getName(), ppName);
        }

        if (tmpImgIC != null) {
            icFile = createTempFile(tmpImgIC);
            icName = RequestBody.create(MediaType.parse("image/*"), icFile);
            icMultipart = MultipartBody.Part.createFormData("idcardimage", icFile.getName(), icName);
        }

        if (TextUtils.isEmpty(name)){
            edt_name.setError("This field is required");
            fokus = edt_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)){
            phone = null;
        }

        if (TextUtils.isEmpty(address)){
            address = null;
        }

        if (TextUtils.isEmpty(birthdate)){
            birthdate = null;
        }

        if (cancel){
            fokus.requestFocus();
        }else {
            update(name,phone,address,birthdate,email,ppMultipart,ppName,icMultipart,icName);
        }
    }

//    public void saveSQLite(String name, String phone, String address, String birthdate, String email){
//        SQLiteDatabase dbRead = dbHandler.getReadableDatabase();
//        cursor = dbRead.rawQuery("SELECT * FROM users WHERE email = '" +
//                email + "'",null);
//        cursor.moveToFirst();
//        if(cursor.getCount()>0) {
//            SQLiteDatabase dbWrite = dbHandler.getWritableDatabase();
//            dbWrite.execSQL("UPDATE users SET " +
//                    "name = '" + name + "', " +
//                    "phone = '" + phone + "', " +
//                    "birthdate = '" + birthdate + "', " +
//                    "address = '" + address + "'" + " WHERE email = '" + email + "'");
//
//            Preferences.setKeyNameTeregister(this.getBaseContext(), name);
//        }else{
//            Toast.makeText(this, "Error, please try again later!", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void edit() {
        mApiService.editRequest(Preferences.getLoggedInUser(this))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL");
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                String error = jsonRESULTS.getString("error");
                                if (error.equals("false")){
//                                    Toast.makeText(requireContext(), "BERHASIL REGISTRASI", Toast.LENGTH_SHORT).show();
                                    String name = jsonRESULTS.getJSONObject("user").getString("nama");
                                    String birthdate = "kosong";
                                    String phone = "kosong";
                                    String address = "kosong";
                                    String photoProfile = "kosong";
                                    String idCard = "kosong";

                                    if (!jsonRESULTS.getJSONObject("user").isNull("birthdate")){
                                        birthdate = jsonRESULTS.getJSONObject("user").getString("birthdate");
                                    }

                                    if (!jsonRESULTS.getJSONObject("user").isNull("phone")){
                                        phone = jsonRESULTS.getJSONObject("user").getString("phone");
                                    }

                                    if (!jsonRESULTS.getJSONObject("user").isNull("address")){
                                        address = jsonRESULTS.getJSONObject("user").getString("address");
                                    }

                                    if (!jsonRESULTS.getJSONObject("user").isNull("photo_profile")){
                                        photoProfile = jsonRESULTS.getJSONObject("user").getString("photo_profile");
                                    }

                                    if (!jsonRESULTS.getJSONObject("user").isNull("id_card")){
                                        idCard = jsonRESULTS.getJSONObject("user").getString("id_card");
                                    }

                                    edt_name.setText(name);
                                    if (!birthdate.equals("")) edt_birthdate.setText(birthdate);
                                    if (!address.equals("")) edt_address.setText(address);
                                    if (!phone.equals("")) edt_phone.setText(phone);
                                    if (!photoProfile.equals("")){
                                        Glide.with(EditAccountActivity.this).load("https://sewainbali.000webhostapp.com/sewain/images/"+photoProfile).into(imgPP);
                                    }
                                    if (!idCard.equals("")){
                                        Glide.with(EditAccountActivity.this).load("https://sewainbali.000webhostapp.com/sewain/images/"+idCard).into(imgIC);
                                    }
                                    edt_email.setText(Preferences.getLoggedInUser(EditAccountActivity.this));
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(EditAccountActivity.this, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            loading.dismiss();
                        } else {
                            Log.i("debug", "onResponse: GA BERHASIL");
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(EditAccountActivity.this, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

    public void save(View v){
        loading = ProgressDialog.show(this, null, "Please wait...", true, false);
        validatorSave();
//        update();
    }

    private void update(final String name, final String phone, final String address, final String birthdate, final String email, final MultipartBody.Part pp, final RequestBody ppName, final MultipartBody.Part ic, final RequestBody icName) {
        Log.d("birthdate","is : " + birthdate);
        mApiService.uptRequest(name,email,birthdate,phone,address,pp,ppName,ic,icName)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL");
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                String error = jsonRESULTS.getString("error");
                                if (error.equals("false")){
                                    Toast.makeText(EditAccountActivity.this, "Updated Succesfully!", Toast.LENGTH_SHORT).show();
                                    Preferences.setKeyNameTeregister(EditAccountActivity.this,edt_name.getText().toString());
                                    finish();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(EditAccountActivity.this, error_message, Toast.LENGTH_SHORT).show();
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
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(EditAccountActivity.this, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

    public void insertPhotoProfile(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        iTag = "pp";
        startActivityForResult(intent, GET_FROM_GALLERY);
//        if (bitmap != null){
//            imgPP.setImageBitmap(bitmap);
//            bitmap = null;
//        }
    }

    public void insertIdCard(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        iTag = "ic";
        startActivityForResult(intent, GET_FROM_GALLERY);
//        if (bitmap != null){
//            imgIC.setImageBitmap(bitmap);
//            imgIC.setVisibility(View.VISIBLE);
//            bitmap = null;
//        }
    }

    public void changePassword(View v){
        Intent intent = new Intent(this,ChangePasswordActivity.class);
        intent.putExtra("USER", edt_email.getText().toString());
        startActivity(intent);
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
            if (iTag.equals("pp")) {
                Glide.with(this)
                        .asBitmap()
                        .apply(myOptions)
                        .load(bitmap)
                        .into(imgPP);
//                imgPP.setImageBitmap(bitmap);

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

                iTag = null;
            } else if (iTag.equals("ic")) {
                Glide.with(this)
                        .asBitmap()
                        .apply(myOptions)
                        .load(bitmap)
                        .into(imgIC);
//                imgIC.setImageBitmap(bitmap);

                Glide.with(this)
                        .asBitmap()
                        .apply(myOptions)
                        .load(bitmap)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                tmpImgIC = resource;
                            }
                        });
                imgIC.setVisibility(View.VISIBLE);
                iTag = null;
            }
        }

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
}
