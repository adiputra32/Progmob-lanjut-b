package com.example.adiputra.sewainbali;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchMotorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText edtSearch;
    private TextView tvSearch;
    private SearchMotorAdapter adapter;
    private ArrayList<SearchMotor> searchMotorArrayList;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_motor);

        loading = ProgressDialog.show(this, null, "Please wait...", true, false);
        recyclerView = (RecyclerView) findViewById(R.id.rev_search_motor);
        edtSearch = (EditText) findViewById(R.id.edt_search);
        tvSearch = (TextView) findViewById(R.id.tv_srch);
        String jenis = getIntent().getStringExtra("JENIS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchMotorArrayList = new ArrayList<>();
        if (jenis.equals("Matic")) {
            maticData();
            tvSearch.setText("Showing Matic");
        } else if (jenis.equals("Standard")) {
            standardData();
            tvSearch.setText("Showing Standard");
        } else if (jenis.equals("Sport")) {
            sportData();
            tvSearch.setText("Showing Sport");
        } else if (jenis.equals("Trail")) {
            trailData();
            tvSearch.setText("Showing Trail");
        } else {
            addData();
        }

        adapter = new SearchMotorAdapter(searchMotorArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        loading.dismiss();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable);
            }
        });

        Toast.makeText(this, "Showing " + jenis, Toast.LENGTH_SHORT).show();
    }

    void addData(){
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Putu Balik Motor"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor2, "Honda Scoopy","Matic","Rp 75.000/day", "Putu Balik Motor"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor3, "Yamaha Nmax","Matic","Rp 120.000/day", "Adi Putra"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Adi Putra"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Adi Putra"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor2, "Honda Scoopy","Matic","Rp 75.000/day", "Komang Bagus"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor3, "Yamaha Nmax","Matic","Rp 120.000/day", "Adi Putra"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Adi Putra"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Adi Putra"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor2, "Honda Scoopy","Matic","Rp 75.000/day", "Komang Bagus"));
    }

    void maticData(){
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Matic","Rp 125.000/day", "Putu Balik Motor"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor2, "Honda Scoopy","Matic","Rp 75.000/day", "Putu Balik Motor"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor3, "Yamaha Nmax","Matic","Rp 120.000/day", "Adi Putra"));
    }

    void standardData(){
    }

    void sportData(){
    }

    void trailData(){
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Trail","Rp 125.000/day", "Putu Balik Motor"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor2, "Honda Scoopy","Trail","Rp 75.000/day", "Putu Balik Motor"));
        searchMotorArrayList.add(new SearchMotor(R.drawable.motor3, "Yamaha Nmax","Trail","Rp 120.000/day", "Adi Putra"));
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
