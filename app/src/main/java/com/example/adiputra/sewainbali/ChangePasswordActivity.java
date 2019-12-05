package com.example.adiputra.sewainbali;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adiputra.sewainbali.apiHelper.BaseApiService;
import com.example.adiputra.sewainbali.apiHelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edt_old_password, edt_new_password, edt_re_password;
    private ProgressDialog loading;
    private BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mApiService = UtilsApi.getAPIService();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edt_old_password = (EditText) findViewById(R.id.et_old_password);
        edt_new_password = (EditText) findViewById(R.id.et_new_password);
        edt_re_password = (EditText) findViewById(R.id.et_re_password);

    }

    public void savePassword(View v){
        edt_old_password.setError(null);
        edt_new_password.setError(null);
        edt_re_password.setError(null);
        View fokus = null;
        boolean cancel = false;

        String new_password = edt_new_password.getText().toString();
        String old_password = edt_old_password.getText().toString();
        String re_password = edt_re_password.getText().toString();
        String user = getIntent().getStringExtra("USER");

        if (TextUtils.isEmpty(old_password)){
            edt_old_password.setError("This field is required");
            fokus = edt_old_password;
            cancel = true;
        }

        if (TextUtils.isEmpty(new_password)){
            edt_new_password.setError("This field is required");
            fokus = edt_new_password;
            cancel = true;
        }else if (!cekRePassword(new_password, re_password)){
            edt_re_password.getText().clear();
            edt_new_password.setError("This password is incorrect");
            fokus = edt_new_password;
            cancel = true;
        }

        loading = ProgressDialog.show(ChangePasswordActivity.this, null, "Please wait...", true, false);
        if (cancel){
            loading.dismiss();
            if (fokus != null) {
                fokus.requestFocus();
            }
        } else {
            cekPassword(old_password,user,new_password);
        }
    }

    private void cekPassword(String oldPassword, String user, String newPassword){
        mApiService.changePasswordRequest(user, oldPassword, newPassword)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    Toast.makeText(ChangePasswordActivity.this, "Password has been changed!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(ChangePasswordActivity.this, error_message, Toast.LENGTH_SHORT).show();
                                    Log.d("errorAPI", "errornya : " + error_message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(ChangePasswordActivity.this, "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

    private boolean cekRePassword(String newPassword, String rePassword){
        return newPassword.equals(rePassword);
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
