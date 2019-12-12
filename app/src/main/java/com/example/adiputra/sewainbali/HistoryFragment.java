package com.example.adiputra.sewainbali;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressDialog loading;
    private HistoryAdapter adapter;
    private ArrayList<History> historyArrayList;
    private BaseApiService mApiService;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mApiService = UtilsApi.getAPIService();

//        addData();
        historyArrayList = new ArrayList<>();
        loading = ProgressDialog.show(requireContext(), null, "Please wait...", true, false);
        getHistory();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new HistoryAdapter(historyArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getHistory(){
        mApiService.historyLobbyRequest(Preferences.getLoggedInUser(requireContext()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    int jml = jsonRESULTS.getJSONArray("motor").length();
                                    for (int i = 0; i <= jml ; i++){
                                        String idSewa = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("id_sewa");
                                        String gambarMotor = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("gambar_motor");
                                        String merk = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("merk");
                                        String name = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("name");
                                        String jenis_motor = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("jenis_motor");
                                        String harga = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("harga");
                                        String rentStatus = jsonRESULTS.getJSONArray("motor").getJSONObject(i).getString("status_sewa");

                                        switch (rentStatus) {
                                            case "Lunas":
                                                rentStatus = "Success";
                                                break;
                                            case "Belum Lunas":
                                                rentStatus = "Waiting for payment";
                                                break;
                                            case "Batal":
                                                rentStatus = "Cancel";
                                                break;
                                        }

                                        Log.d("lengthnya","i : " + i);
                                        addData(idSewa,gambarMotor,merk,jenis_motor,harga,name,rentStatus);
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
                        Toast.makeText(requireContext(), "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }


    void addData(String idSewa,String gambarMotor,String merk,String jenis_motor,String harga,String name,String paymentStatus){
        historyArrayList.add(new History(idSewa, gambarMotor, merk, name, jenis_motor, paymentStatus));
        adapter.notifyDataSetChanged();
//        historyArrayList.add(new History(R.drawable.motor1, "Honda New CBR","Adi Putra Motor", "Sport","Sukses"));
//        historyArrayList.add(new History(R.drawable.motor2, "Honda Scoopy","Adi Putra Motor", "Matic","Sukses"));
//        historyArrayList.add(new History(R.drawable.motor3, "Yamaha Nmax","Putu Balik", "Matic","Waiting for Payment"));
    }
}
