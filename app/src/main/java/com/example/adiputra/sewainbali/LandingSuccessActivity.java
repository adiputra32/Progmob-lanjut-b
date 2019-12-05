package com.example.adiputra.sewainbali;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingSuccessActivity extends AppCompatActivity {
    private Button btnGoToHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_success);

        btnGoToHistory = (Button) findViewById(R.id.btn_go_to_history);

        btnGoToHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingSuccessActivity.this, DetailHistoryActivity.class);
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
