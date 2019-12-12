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
public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private ChatLobbyAdapter adapter;
    private ArrayList<ChatLobby> chatLobbyArrayList;
    private BaseApiService mApiService;
    private ProgressDialog loading;

    public ChatFragment() {
        // recycle view chat fragment
    }

    void addData(String email_pemilik, String photo_profil, String name){
        chatLobbyArrayList.add(new ChatLobby(email_pemilik, photo_profil, name));
        adapter.notifyDataSetChanged();
//        chatLobbyArrayList.add(new ChatLobby(R.drawable.profile, "Adi Putra","Halo, posisi dimana ya?"));
//        chatLobbyArrayList.add(new ChatLobby(R.drawable.profile2, "Putu Balik","Mas, saya tungguin di depan rumah ya, pas di pertigaan pertama belok kanan aja langsung"));
//        chatLobbyArrayList.add(new ChatLobby(R.drawable.profile3, "Komang Bagus","Motornya sudah dicuci belum pak?"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mApiService = UtilsApi.getAPIService();

//        chatLobbyArrayList = new ArrayList<>();
//        addData();
        loading = ProgressDialog.show(requireContext(), null, "Please wait...", true, false);


        chatLobbyArrayList = new ArrayList<>();
//        addData();
        getChat(Preferences.getLoggedInUser(requireContext()));

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new ChatLobbyAdapter(chatLobbyArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getChat(String email) {
        mApiService.chatRequest(email)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.dismiss();
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    int jml = jsonRESULTS.getJSONArray("chat").length();
                                    for (int i = 0; i <= jml ; i++){
                                        String email_pemilik = jsonRESULTS.getJSONArray("chat").getJSONObject(i).getString("email_pemilik");
                                        String photo_profil = jsonRESULTS.getJSONArray("chat").getJSONObject(i).getString("photo_profile");
                                        String name = jsonRESULTS.getJSONArray("chat").getJSONObject(i).getString("name");
                                        Log.d("lengthnya","i : " + i);
                                        addData(email_pemilik,photo_profil,name);
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
                        } else {
                            Toast.makeText(requireContext(), "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
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

}
