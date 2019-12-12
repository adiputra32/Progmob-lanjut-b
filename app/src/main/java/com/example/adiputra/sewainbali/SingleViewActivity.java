package com.example.adiputra.sewainbali;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SingleViewActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_view);

        ImageView imageView = (ImageView) findViewById(R.id.SingleView);

        Glide.with(SingleViewActivity.this)
                .load("https://kelompok23.000webhostapp.com/images/"+getIntent().getStringExtra("URL"))
                .into(imageView);
    }
}