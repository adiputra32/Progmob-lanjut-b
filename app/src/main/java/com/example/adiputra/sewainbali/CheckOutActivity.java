package com.example.adiputra.sewainbali;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CheckOutActivity extends AppCompatActivity {
    private Button btnRent;
    private ImageButton btnAdd, btnSub;
    private EditText edtDays;
    private int days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        btnRent = (Button) findViewById(R.id.btn_rent);
        btnAdd = (ImageButton) findViewById(R.id.btn_add_book);
        btnSub = (ImageButton) findViewById(R.id.btn_sub_book);
        edtDays = (EditText) findViewById(R.id.edt_days);

        days = Integer.parseInt(edtDays.getText().toString());

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

        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckOutActivity.this, LandingSuccessActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (days < 30){
                    days += 1;
                    edtDays.setText(String.valueOf(days));
                }
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (days > 1){
                    days -= 1;
                    edtDays.setText(String.valueOf(days));
                }
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

    public void DetailMotor(View view){
        Intent intent = new Intent(CheckOutActivity.this, DetailMotorActivity.class);
        startActivity(intent);
    }
}
