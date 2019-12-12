package com.example.adiputra.sewainbali;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adiputra.sewainbali.apiHelper.BaseApiService;
import com.example.adiputra.sewainbali.apiHelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SearchMotorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText edtSearch;
    private TextView tvSearch;
    private SearchMotorAdapter adapter;
    private ArrayList<SearchMotor> searchMotorArrayList;
    private ProgressDialog loading;
    private String searchJenisMotor;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loadingRecyclerView = true;
    private BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_motor);

        mApiService = UtilsApi.getAPIService();

        loading = ProgressDialog.show(this, null, "Please wait...", true, false);
        recyclerView = (RecyclerView) findViewById(R.id.rev_search_motor);
        edtSearch = (EditText) findViewById(R.id.edt_search);
        tvSearch = (TextView) findViewById(R.id.tv_srch);
        String jenis = getIntent().getStringExtra("JENIS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        switch (jenis) {
            case "Matic":
                searchJenisMotor = "Matic";
                tvSearch.setText("Showing Matic");
                break;
            case "Standard":
                searchJenisMotor = "Standard";
                tvSearch.setText("Showing Standard");
                break;
            case "Sport":
                searchJenisMotor = "Sport";
                tvSearch.setText("Showing Sport");
                break;
            case "Trail":
                searchJenisMotor = "Trail";
                tvSearch.setText("Showing Trail");
                break;
            default:
                searchJenisMotor = "All";
                break;
        }

        searchMotorArrayList = new ArrayList<>();
        final String email = Preferences.getLoggedInUser(this);
        getMotorAwal(0,5, searchJenisMotor,email);

        adapter = new SearchMotorAdapter(searchMotorArrayList);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                    if (loadingRecyclerView)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loadingRecyclerView = false;
                            Log.v("...", "Last Item Wow !");
                            getMotor(totalItemCount,5, searchJenisMotor,email);
                        }
                    }
                }
            }
        });

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

    private void getMotor(int total, int tambah, String kategori, String email){
        mApiService.motorRequest(total,tambah,kategori,email)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    int jml = jsonRESULTS.getJSONArray("motor").length();
                                    for (int i = 0; i <= jml ; i++){
                                        String idMotor = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("id_motor");
                                        String gambarMotor = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("gambar_motor");
                                        String merk = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("merk");
                                        String name = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("name");
                                        String jenis_motor = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("jenis_motor");
                                        String harga = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("harga");
                                        Log.d("lengthnya","i : " + i);
                                        addData(idMotor,gambarMotor,merk,jenis_motor,harga,name);
                                    }
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Log.d("errorAPI", "errornya : " + error_message);
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
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(SearchMotorActivity.this, "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

    private void getMotorAwal(int total, int tambah, String kategori, String email){
        mApiService.motorRequest(total,tambah,kategori,email)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    int jml = jsonRESULTS.getJSONArray("motor").length();
                                    for (int i = 0; i <= jml ; i++){
                                        String idMotor = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("id_motor");
                                        String gambarMotor = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("gambar_motor");
                                        String merk = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("merk");
                                        String name = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("name");
                                        String jenis_motor = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("jenis_motor");
                                        String harga = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("harga");
                                        Log.d("lengthnya","i : " + i);
                                        addData(idMotor,gambarMotor,merk,jenis_motor,harga,name);
                                    }
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Log.d("errorAPI", "errornya : " + error_message);
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
                        loading.dismiss();
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(SearchMotorActivity.this, "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    void addData(String id_motor, String gambar_motor, String merk, String jenis_motor, String harga, String name){
        searchMotorArrayList.add(new SearchMotor(id_motor, gambar_motor, merk, jenis_motor, harga, name));
        adapter.notifyDataSetChanged();
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Putu Balik Motor"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor2, "Honda Scoopy","Matic","Rp 75.000/day", "Putu Balik Motor"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor3, "Yamaha Nmax","Matic","Rp 120.000/day", "Adi Putra"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Adi Putra"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Adi Putra"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor2, "Honda Scoopy","Matic","Rp 75.000/day", "Komang Bagus"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor3, "Yamaha Nmax","Matic","Rp 120.000/day", "Adi Putra"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Adi Putra"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Adi Putra"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor2, "Honda Scoopy","Matic","Rp 75.000/day", "Komang Bagus"));
    }

//    void maticData(){
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Matic","Rp 125.000/day", "Putu Balik Motor"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor2, "Honda Scoopy","Matic","Rp 75.000/day", "Putu Balik Motor"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor3, "Yamaha Nmax","Matic","Rp 120.000/day", "Adi Putra"));
//    }
//
//    void standardData(){
//    }
//
//    void sportData(){
//    }
//
//    void trailData(){
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor1, "Honda New CBR","Trail","Rp 125.000/day", "Putu Balik Motor"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor2, "Honda Scoopy","Trail","Rp 75.000/day", "Putu Balik Motor"));
//        searchMotorArrayList.add(new SearchMotor(R.drawable.motor3, "Yamaha Nmax","Trail","Rp 120.000/day", "Adi Putra"));
//    }

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
