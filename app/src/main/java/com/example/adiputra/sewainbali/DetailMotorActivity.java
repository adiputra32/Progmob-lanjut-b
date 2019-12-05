package com.example.adiputra.sewainbali;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class DetailMotorActivity extends AppCompatActivity {
    CarouselView carouselView;
    private Button btnCheckout;
    int[] motor = {R.drawable.motor1, R.drawable.motor2, R.drawable.motor3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_motor);

        carouselView = (CarouselView) findViewById(R.id.img_motor);
        carouselView.setPageCount(motor.length);
        carouselView.setImageListener(imageListener);
        btnCheckout = (Button) findViewById(R.id.btn_checkout);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailMotorActivity.this, CheckOutActivity.class);
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
            imageView.setImageResource(motor[position]);
        }
    };

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
