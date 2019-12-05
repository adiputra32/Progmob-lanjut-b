package com.example.adiputra.sewainbali;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adiputra.sewainbali.apiHelper.BaseApiService;
import com.example.adiputra.sewainbali.apiHelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {

    public static final int GET_FROM_GALLERY = 3;
    CircleImageView imgPP;
    ImageView imgIC;
    Bitmap bitmap;
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


        edt_name.setText(getIntent().getStringExtra("NAME"));
        String bd = getIntent().getStringExtra("BIRTHDATE");
        String adr = getIntent().getStringExtra("ADDRESS");
        String ph = getIntent().getStringExtra("PHONE");
        if (!bd.equals("kosong")) edt_birthdate.setText(bd);
        if (!adr.equals("kosong")) edt_address.setText(adr);
        if (!adr.equals("kosong")) edt_phone.setText(ph);

        edt_email.setText(Preferences.getLoggedInUser(this));

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
        String birthdate = edt_birthdate.getText().toString();
        String email = edt_email.getText().toString();

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
            update(name,phone,address,birthdate,email);
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

    public void save(View v){
        loading = ProgressDialog.show(this, null, "Please wait...", true, false);
        validatorSave();
//        update();
    }

    private void update(final String name, final String phone, final String address, final String birthdate, final String email) {
        mApiService.uptRequest(name,email,birthdate,phone,address)
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
                imgPP.setImageBitmap(bitmap);
                iTag = null;
            } else if (iTag.equals("ic")) {
                imgIC.setImageBitmap(bitmap);
                imgIC.setVisibility(View.VISIBLE);
                iTag = null;
            }
        }

    }

}
