package com.example.adiputra.sewainbali;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adiputra.sewainbali.apiHelper.BaseApiService;
import com.example.adiputra.sewainbali.apiHelper.UtilsApi;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TextView tvNama, tvStatusActive, tvStatus;
    private Cursor cursor;
    private DatabaseHandler dbHandler;
    private ProgressDialog loading;
    private BaseApiService mApiService;
    private RecyclerView recyclerView;
    private MostSearchedAdapter adapter;
    private ArrayList<MostSearched> mostSearchedArrayList;
    private LinearLayout btnAllBike, btnMatic, btnStandard, btnSport, btnTrail;
    private ImageView imgAllBike, imgMatic, imgStandard, imgSport, imgTrail;
    private ScrollView scrlHome;

    public HomeFragment() {
        // Required empty public constructor
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mApiService = UtilsApi.getAPIService();

        tvNama = (TextView) view.findViewById(R.id.tv_name);
        tvStatusActive = (TextView) view.findViewById(R.id.tv_status_acc);
        tvStatus = (TextView) view.findViewById(R.id.tv_acc);
        recyclerView = (RecyclerView) view.findViewById(R.id.rev_most_searched);
        btnAllBike = (LinearLayout) view.findViewById(R.id.btn_all_bike);
        btnMatic = (LinearLayout) view.findViewById(R.id.btn_matic_bike);
        btnStandard = (LinearLayout) view.findViewById(R.id.btn_standard_bike);
        btnSport = (LinearLayout) view.findViewById(R.id.btn_sport_bike);
        btnTrail = (LinearLayout) view.findViewById(R.id.btn_trail_bike);
        imgAllBike = (ImageView) view.findViewById(R.id.img_all_bike);
        imgMatic = (ImageView) view.findViewById(R.id.img_matic);
        imgStandard = (ImageView) view.findViewById(R.id.img_standard);
        imgSport = (ImageView) view.findViewById(R.id.img_sport);
        imgTrail = (ImageView) view.findViewById(R.id.img_trail);
        scrlHome = (ScrollView) view.findViewById(R.id.scrl_home);

        scrlHome.setFocusableInTouchMode(true);
        scrlHome.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        loading = ProgressDialog.show(requireContext(), null, "Please wait...", true, false);

        Glide.with(view.getContext()).load(getResources().getIdentifier("jenis_all","drawable",view.getContext().getPackageName())).into(imgAllBike);
        Glide.with(view.getContext()).load(getResources().getIdentifier("jenis_matic_2","drawable",view.getContext().getPackageName())).into(imgMatic);
        Glide.with(view.getContext()).load(getResources().getIdentifier("jenis_standard","drawable",view.getContext().getPackageName())).into(imgStandard);
        Glide.with(view.getContext()).load(getResources().getIdentifier("jenis_sport","drawable",view.getContext().getPackageName())).into(imgSport);
        Glide.with(view.getContext()).load(getResources().getIdentifier("jenis_trail","drawable",view.getContext().getPackageName())).into(imgTrail);

        tvNama.setText(Preferences.getKeyNameTeregister(requireActivity().getBaseContext()));

        dbHandler = new DatabaseHandler(requireContext());

        requestStatus();
        addData();

        adapter = new MostSearchedAdapter(mostSearchedArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        final Intent intent = new Intent(requireContext(),SearchMotorActivity.class);

        /*onClicks*/
        btnAllBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("JENIS","ALL");
                startActivity(intent);
            }
        });

        btnMatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("JENIS","Matic");
                startActivity(intent);
            }
        });

        btnSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("JENIS","Sport");
                startActivity(intent);
            }
        });

        btnStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("JENIS","Standard");
                startActivity(intent);
            }
        });

        btnTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("JENIS","Trail");
                startActivity(intent);
            }
        });

        return view;
    }

    private void setStatus(){
        tvStatus.setText("Your Account is");
        tvStatusActive.setText("Active");
        tvStatusActive.setTextColor(Color.parseColor("#3bbc44"));
    }

    private void requestStatus(){
        mApiService.statusRequest(Preferences.getLoggedInUser(requireActivity().getBaseContext()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.dismiss();
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    setStatus();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Log.d("errorAPI", "errornya : " + error_message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            getStatusSqlite();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(requireContext(), "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
                        getStatusSqlite();
                        loading.dismiss();
                    }
                });
    }

    private void getStatusSqlite(){
        SQLiteDatabase dbRead = dbHandler.getReadableDatabase();
        cursor = dbRead.rawQuery("SELECT phone FROM users WHERE email = '" +
                Preferences.getRegisteredUser(requireContext()) + "'",null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            cursor.moveToPosition(0);
            if (cursor.getString(0) != null){
                setStatus();
            }
        }
    }

    private void addData(){
        mostSearchedArrayList = new ArrayList<>();
        mostSearchedArrayList.add(new MostSearched(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Putu Balik Motor"));
        mostSearchedArrayList.add(new MostSearched(R.drawable.motor2, "Honda Scoopy","Matic","Rp 75.000/day", "Putu Balik Motor"));
        mostSearchedArrayList.add(new MostSearched(R.drawable.motor3, "Yamaha Nmax","Matic","Rp 120.000/day", "Adi Putra"));
        mostSearchedArrayList.add(new MostSearched(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Putu Balik Motor"));
        mostSearchedArrayList.add(new MostSearched(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Putu Balik Motor"));
        mostSearchedArrayList.add(new MostSearched(R.drawable.motor2, "Honda Scoopy","Matic","Rp 75.000/day", "Putu Balik Motor"));
        mostSearchedArrayList.add(new MostSearched(R.drawable.motor3, "Yamaha Nmax","Matic","Rp 120.000/day", "Adi Putra"));
        mostSearchedArrayList.add(new MostSearched(R.drawable.motor1, "Honda New CBR","Sport","Rp 125.000/day", "Putu Balik Motor"));
    }

}
