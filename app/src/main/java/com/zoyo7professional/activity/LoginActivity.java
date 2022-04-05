package com.zoyo7professional.activity;

import static com.zoyo7professional.ApiData.API.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zoyo7professional.ApiData.API;
import com.zoyo7professional.ApiData.APIClient;
import com.zoyo7professional.RetrofitModel.LoginData;
import com.zoyo7professional.databinding.ActivityLoginBinding;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.ReturnErrorToast;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectionInterface;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectivity;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    String stMobile = "", stCheck = "";
    ProgressDialog dialog;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog = new ProgressDialog(this);
        binding.llTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, TermAndConditionActivity.class));
            }
        });
        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();
        binding.btGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stMobile = binding.etMobile.getText().toString().trim();

/*
                if (binding.cAgree.isChecked()) {
                    stCheck = "yes";
                } else {
                    stCheck="stCheck";
                }
*/

                if (TextUtils.isEmpty(stMobile)) {
                    binding.etMobile.setError("Mobile Number Must Required !");
                    binding.etMobile.requestFocus();
                } else if (stMobile.length() < 10) {
                    binding.etMobile.setError("Please enter atleast 10 digit mobile number");
                    binding.etMobile.requestFocus();
                }

                else if (!binding.cAgree.isChecked()){
                    Toast.makeText(LoginActivity.this, "Please accept term and condition", Toast.LENGTH_SHORT).show();

                }else {
                    InternetConnectionInterface connectivity = new InternetConnectivity();
                    if (connectivity.isConnected(getApplicationContext())) {
                        sendOtp();
                    } else {
                        Toast.makeText(LoginActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

/*
    public void sendOtp(String stMobile) {

        dialog.setTitle("Sending");
        dialog.setMessage("please wait....");
        dialog.setCancelable(false);

        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            Log.e("dskjdsjd", e.getMessage(), e);
        }


        Map<String, String> param = new HashMap<>();

        Log.e("hsxghs", stMobile);
        param.put("action", login);
        param.put("mobile_no", stMobile);
        Call<LoginData> call = APIClient.getAPIClient().login(param);

        Log.e("hjhjh", call.toString());

        call.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(@NonNull Call<LoginData> call, @NonNull Response<LoginData> response) {
                if (!response.isSuccessful()) {
                    dialog.hide();
                    ReturnErrorToast.showToast(LoginActivity.this);
                }

                LoginData loginData = response.body();

                Log.e("bhbhnmn", loginData.getMessage());

                if (loginData != null) {

                    if (loginData.getResult()) {

                        LoginData.Data userData = loginData.getData();

                        Log.e("smkckm", userData.toString());

                        //  SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ID, userData.getUserID());


                        startActivity(new Intent(getApplicationContext(), OtpVerifyActivity.class));
                        finishAffinity();
                        dialog.hide();

                    } else {
                        dialog.hide();
                        Toast.makeText(LoginActivity.this, loginData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginData> call, @NonNull Throwable t) {

                Log.e("snlkcxlsk", t.getMessage());
                dialog.hide();
            }
        });
    }
*/


    public void sendOtp() {

        binding.progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("jfdkjgf", response);


                try {

                    binding.progressBar.setVisibility(View.GONE);

                    JSONObject object = new JSONObject(response);

                    if (object.has("result")) {

                        String result = object.getString("result");


                        if (result.equals("true")) {
                            String data = object.getString("data");

                            JSONObject jsonObject = new JSONObject(data);

                            SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ID, jsonObject.getString("id"));
                            SharedHelper.putKey(getApplicationContext(), AppConstats.MOBILE_NO, jsonObject.getString("mobile_no"));
                            SharedHelper.putKey(getApplicationContext(), AppConstats.GET_OTP, jsonObject.getString("otp"));
                            startActivity(new Intent(LoginActivity.this, OtpVerifyActivity.class));
                            Toast.makeText(LoginActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(LoginActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("kgjfdkjg", error.getMessage() + "");
                binding.progressBar.setVisibility(View.GONE);

                NetworkResponse response = error.networkResponse;
                String errorMsg = "";
                if (response != null && response.data != null) {
                    String errorString = new String(response.data);
                    Log.i("log error", errorString);
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Log.e("osdifopsif", stMobile);

                Map<String, String> map = new HashMap<>();
                map.put("action", login);
                map.put("mobile_no", stMobile);

                return map;
            }
        };


        queue.add(request);


    }
}