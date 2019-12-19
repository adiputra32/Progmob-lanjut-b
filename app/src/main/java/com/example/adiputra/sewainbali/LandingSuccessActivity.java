package com.example.adiputra.sewainbali;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adiputra.sewainbali.apiHelper.BaseApiService;
import com.example.adiputra.sewainbali.apiHelper.RetrofitClient;
import com.example.adiputra.sewainbali.apiHelper.UtilsApi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LandingSuccessActivity extends AppCompatActivity {
    private Button btnGoToHistory;
    private BaseApiService mApiService;
    private RetrofitClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_success);

        mApiService = UtilsApi.getAPIServiceNotif();

        btnGoToHistory = (Button) findViewById(R.id.btn_go_to_history);
        final String id_sewa = getIntent().getStringExtra("IDSEWA");
        String emailPemilik = getIntent().getStringExtra("EMAIL2");

        DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
        Map<String,Object> map = new HashMap<String,Object>();
        String email_asal = Preferences.getLoggedInUser(this);
        String email_tujuan = emailPemilik;

        email_asal = email_asal.replace("@","");
        email_asal = email_asal.replace(".","");
        email_tujuan = email_tujuan.replace("@","");
        email_tujuan = email_tujuan.replace(".","");
        map.put(email_tujuan+email_asal,"");
        root.updateChildren(map);

        btnGoToHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingSuccessActivity.this, DetailHistoryActivity.class);
                intent.putExtra("IDSEWA2",id_sewa);
                startActivity(intent);
            }
        });

        sendNotificationToPatner();
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(LandingSuccessActivity.this, SearchMotorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("JENIS","ALL");
        startActivity(intent);
    }

    private void sendNotificationToPatner() {
        SendNotificationModel sendNotificationModel = new SendNotificationModel("Ada yang nyewa motor kamu nih", "Sewain");
        RequestNotification requestNotification = new RequestNotification();
        requestNotification.setSendNotificationModel(sendNotificationModel);
        //token is id , whom you want to send notification ,
//        String token = Preferences.getKeyFcm(this);
        String token = "f5kSYJPyDPI:APA91bEJWljgJTg4t9_0vqyaJqFk71hUVuOZbOAS0jY1vW75lzFDyJ16c4gAn2B383HJcxR-VqcU-pX0yDI_qGgzwrQrLKSTUlIRonM4BGFniWruNENKw3uBGZUJUYTGDwwnwAtbXJti";
        requestNotification.setToken(token);

        mApiService =  apiClient.getClientNotif().create(BaseApiService.class);
        retrofit2.Call<ResponseBody> responseBodyCall = mApiService.sendChatNotification(requestNotification);


        responseBodyCall
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.d("HORE","BERHASIL");
                        } else {
                            Log.i("debug", "onResponse: GA BERHASIL");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

}
