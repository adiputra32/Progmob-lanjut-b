package com.example.adiputra.sewainbali;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LandingSuccessActivity extends AppCompatActivity {
    private Button btnGoToHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_success);

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

    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(LandingSuccessActivity.this, SearchMotorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("JENIS","ALL");
        startActivity(intent);
    }

}
