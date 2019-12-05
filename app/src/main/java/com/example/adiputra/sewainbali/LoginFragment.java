package com.example.adiputra.sewainbali;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText edt_email, edt_password;
    private Button btn_login;
    private Cursor cursor;
    private DatabaseHandler dbHandler;
    private ProgressDialog loading;
    private BaseApiService mApiService;
    private String name;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mApiService = UtilsApi.getAPIService();

        btn_login = (Button) view.findViewById(R.id.btn_login);
        edt_email = (EditText) view.findViewById(R.id.et_email);
        edt_password = (EditText) view.findViewById(R.id.et_password);

        edt_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    validatorLogin();
                    return true;
                }
                return false;
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(requireContext(), null, "Please wait...", true, false);
                validatorLogin();
            }
        });

        return view;
    }

    private void requestLogin(String email, String password){
        mApiService.loginRequest(email, password)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
//                                    Toast.makeText(requireContext(), "Login success", Toast.LENGTH_SHORT).show();
                                    name = jsonRESULTS.getJSONObject("user").getString("nama");
                                    masuk();
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
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(requireContext(), "Please, check your connection and try again!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
        }

    private void validatorLogin(){
        edt_email.setError(null);
        edt_password.setError(null);
        View fokus = null;
        boolean cancel = false;

        String email = edt_email.getText().toString();
        String password = edt_password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            loading.dismiss();
            edt_email.setError("This field is required");
            fokus = edt_email;
            cancel = true;
        } else if (TextUtils.isEmpty(password)){
            loading.dismiss();
            edt_password.setError("This field is required");
            fokus = edt_password;
            cancel = true;
        }

        if (cancel) fokus.requestFocus();
        else requestLogin(email,password);
    }

    private void masuk(){
        Preferences.setKeyNameTeregister(requireActivity().getBaseContext(), name);
        Preferences.setLoggedInUser(requireActivity().getBaseContext(), edt_email.getText().toString());
        Preferences.setLoggedInStatus(requireActivity().getBaseContext(),true);
        startActivity(new Intent(requireActivity().getBaseContext(),MainActivity.class));
        String login_username = Preferences.getKeyNameTeregister(requireActivity().getBaseContext());
        Toast.makeText(requireContext(), "Hai, " + login_username, Toast.LENGTH_LONG).show();
        requireActivity().finish();
    }


}
