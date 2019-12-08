package com.example.adiputra.sewainbali;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adiputra.sewainbali.apiHelper.BaseApiService;
import com.example.adiputra.sewainbali.apiHelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 100;

    private TextView tvNama, tvAlamat, tvStatusActive, tvStatus;
    private Button btnKeluar, btnEdit;
    private Cursor cursor;
    private DatabaseHandler dbHandler;
    private ProgressDialog loading;
    private BaseApiService mApiService;
    private String email;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        setHasOptionsMenu(true);

        mApiService = UtilsApi.getAPIService();

        btnKeluar = (Button) view.findViewById(R.id.btn_keluar);
        btnEdit = (Button) view.findViewById(R.id.btn_edit);
        tvNama = (TextView) view.findViewById(R.id.tv_account_name);
        tvAlamat = (TextView) view.findViewById(R.id.tv_account_alamat);
        tvStatusActive = (TextView) view.findViewById(R.id.tv_status_acc);
        tvStatus = (TextView) view.findViewById(R.id.tv_acc);

        email = Preferences.getLoggedInUser(requireActivity().getBaseContext());
        tvNama.setText(Preferences.getKeyNameTeregister(requireActivity().getBaseContext()));

        loading = ProgressDialog.show(requireContext(), null, "Please wait...", true, false);

        requestStatus();

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences.clearLoggedInUser(requireContext());
                startActivity(new Intent(requireActivity().getApplicationContext(),LoginActivity.class));
                requireActivity().finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(),EditAccountActivity.class);
                startActivity(intent);
            }
        });

//        getExternalStorageText();
        return view;
    }

    private void setStatus(){
        tvStatus.setText("Your Account is");
        tvStatusActive.setText("Active");
        tvStatusActive.setTextColor(Color.parseColor("#3bbc44"));
    }

    private void requestStatus(){
        mApiService.statusRequest(email)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    tvAlamat.setText(jsonRESULTS.getString("address"));
                                    setStatus();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    tvAlamat.setText(jsonRESULTS.getString("address"));
                                    Log.d("errorAPI", "errornya : " + error_message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                            getStatusSqlite();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
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

    private void getExternalStorageText(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    File sdcard = Environment.getExternalStorageDirectory();
                    File dir = new File(sdcard.getAbsolutePath() + "/text/");
                    if(dir.exists()) {
                        File file = new File(dir, "sample.txt");

                        FileOutputStream os = null;
                        StringBuilder text = new StringBuilder();

                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;

                            while ((line = br.readLine()) != null) {
                                text.append(line);
                                text.append('\n');
                            }
                            br.close();
                        } catch (IOException e) {
                            //You'll need to add proper error handling here
                        }
                        tvNama.setText(text);
                    }
                } else {
                    requestPermission(); // Code for permission
                }
            } else {

                File sdcard = Environment.getExternalStorageDirectory();
                File dir = new File(sdcard.getAbsolutePath() + "/text/");
                if(dir.exists()) {
                    File file = new File(dir, "sample.txt");

                    FileOutputStream os = null;
                    StringBuilder text = new StringBuilder();

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;

                        while ((line = br.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }
                        br.close();
                    } catch (IOException e) {
                        //You'll need to add proper error handling here
                    }
                    tvNama.setText(text);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(), "Write External Storage permission allows us to read files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

}
