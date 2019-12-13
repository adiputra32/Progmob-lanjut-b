package com.example.adiputra.sewainbali;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.adiputra.sewainbali.apiHelper.BaseApiService;
import com.example.adiputra.sewainbali.apiHelper.UtilsApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private Button btnRegister;
    private EditText edtNama, edtPhone, edtPassword, edtRePassword, edtEmail;
    private ProgressDialog loading;
    private BaseApiService mApiService;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mApiService = UtilsApi.getAPIService();

        btnRegister = (Button) view.findViewById(R.id.btn_register);
        edtNama = (EditText) view.findViewById(R.id.et_name);
        edtEmail = (EditText) view.findViewById(R.id.et_email);
        edtPhone = (EditText) view.findViewById(R.id.et_phone);
        edtPassword = (EditText) view.findViewById(R.id.et_password);
        edtRePassword = (EditText) view.findViewById(R.id.et_repassword);

        edtRePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    validatorRegister();
                    return true;
                }
                return false;
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(requireContext(), null, "Please wait...", true, false);
                validatorRegister();
            }
        });

        return view;
    }

    private void requestRegister() {
        final String[] msg = new String[1];
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        String token = task.getResult().getToken();
                        msg[0] = getString(R.string.fcm_token, token);
                        Preferences.setKeyFcm(requireContext(), msg[0]);
                        Log.d("TAGTAGTAG", msg[0]);

                    }
                });
        mApiService.registerRequest(edtNama.getText().toString(),
                edtEmail.getText().toString(),
                edtPhone.getText().toString(),
                edtPassword.getText().toString(),
                msg[0])
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL");
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                String error = jsonRESULTS.getString("error");
                                if (error.equals("false")){
                                    Toast.makeText(requireContext(), "Your account has been registered successfully!", Toast.LENGTH_SHORT).show();

//                                    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
//                                    Map<String,Object> map = new HashMap<String,Object>();
//                                    String email_tujuan = edtEmail.getText().toString();
//                                    email_tujuan = email_tujuan.replace("@","");
//                                    email_tujuan = email_tujuan.replace(".","");
//                                    map.put(email_tujuan,"");
//                                    root.updateChildren(map);

                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(requireContext(), error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("debug", "onResponse: GA BERHASIL");
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(requireContext(), "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

    private void validatorRegister() {
        edtNama.setError(null);
        edtEmail.setError(null);
        edtPhone.setError(null);
        edtPassword.setError(null);
        edtRePassword.setError(null);
        View fokus = null;
        boolean cancel = false;

        String repassword = edtRePassword.getText().toString();
        String email = edtEmail.getText().toString();
        String phone = edtPhone.getText().toString();
        String password = edtPassword.getText().toString();
        String name = edtNama.getText().toString();

        if (TextUtils.isEmpty(name)) {
            loading.dismiss();
            edtNama.setError("This field is required");
            fokus = edtNama;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            loading.dismiss();
            edtEmail.setError("This field is required");
            fokus = edtEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            loading.dismiss();
            edtPhone.setError("This field is required");
            fokus = edtPhone;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            loading.dismiss();
            edtPassword.setError("This field is required");
            fokus = edtPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(repassword)) {
            loading.dismiss();
            edtRePassword.setError("This field is required");
            fokus = edtRePassword;
            cancel = true;
        } else if (!password.equals(repassword)) {
            loading.dismiss();
            edtPassword.setError("This password is incorrect");
            fokus = edtPassword;
            cancel = true;
        }

        if (cancel) fokus.requestFocus();
        else requestRegister();
    }
}


