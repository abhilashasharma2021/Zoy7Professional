package com.zoyo7professional.activity;


import static com.zoyo7professional.ApiData.API.login;
import static com.zoyo7professional.ApiData.API.otpVerify;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zoyo7professional.ApiData.API;
import com.zoyo7professional.databinding.ActivityLoginBinding;
import com.zoyo7professional.databinding.ActivityOtpVerifyBinding;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectionInterface;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectivity;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OtpVerifyActivity extends AppCompatActivity {
ActivityOtpVerifyBinding binding;
    RequestQueue queue;
    String mobile_No="";
    String strOne = "", strTwo = "", strthree = "", strFour = "",strcombine="",get_otp="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        binding= ActivityOtpVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mobile_No = SharedHelper.getKey(getApplicationContext(), AppConstats.MOBILE_NO);
        get_otp = SharedHelper.getKey(getApplicationContext(), AppConstats.GET_OTP);


        binding.tvEnterOtp.setText("Enter OTP Send to"+ mobile_No);

        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();

       binding.txResend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               reSendOtp(mobile_No);
           }
       });

        binding.etOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                binding.etTwo.requestFocus();

            }
        });


        binding.etTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                binding.etThree.requestFocus();

            }
        });

        binding.etThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                binding.etFour.requestFocus();

            }
        });


        binding.etFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                strOne = binding.etOne.getText().toString().trim();
                Log.e("fgfgfg", strOne);
                strTwo = binding.etTwo.getText().toString().trim();
                Log.e("fgfgfg", strTwo);
                strthree = binding.etThree.getText().toString().trim();
                Log.e("fgfgfg", strthree);
                strFour = binding.etFour.getText().toString().trim();
                Log.e("fgfgfg", strFour);

                strcombine = strOne + strTwo + strthree + strFour;

                Log.e("dkjfdfdljd", strcombine);


            }
        });

        binding.btVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                InternetConnectionInterface connectivity = new InternetConnectivity();
                if (connectivity.isConnected(getApplicationContext())) {
                    if (get_otp.equals(strcombine)){

                        verify_Otp();
                    }else {
                        Toast.makeText(OtpVerifyActivity.this, "You are enter wrong Otp", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OtpVerifyActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                }




            }
        });

    }

    public void verify_Otp() {

        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("djfkdj", response);
                try {
                    binding.progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {

                            String data = jsonObject.getString("data");

                            JSONObject jsonData = new JSONObject(data);
                            startActivity(new Intent(OtpVerifyActivity.this,AddDetailsActivity.class));
                            Toast.makeText(OtpVerifyActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                        }
                        else {
                            Toast.makeText(OtpVerifyActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                        }

                    }


                } catch (Exception ex) {
                    Log.e("jgvkdfj", ex.getMessage());

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("thrtfghfg",error.getMessage());
                binding.progressBar.setVisibility(View.GONE);
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action",otpVerify);
                map.put("mobile_no",mobile_No);
                map.put("otp",strcombine);

                return map;
            }
        };
        queue.add(request);
    }



    public void reSendOtp(String stMobile) {

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
                            startActivity(new Intent(OtpVerifyActivity.this, OtpVerifyActivity.class));
                            Toast.makeText(OtpVerifyActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(OtpVerifyActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(request);


    }
}

