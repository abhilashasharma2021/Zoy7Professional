package com.zoyo7professional.activity;

import static com.zoyo7professional.ApiData.API.addAmountToWallet;
import static com.zoyo7professional.ApiData.API.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.zoyo7professional.R;
import com.zoyo7professional.databinding.ActivityAddMoneyBinding;
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

public class AddMoneyActivity extends AppCompatActivity {
    ActivityAddMoneyBinding binding;
    RequestQueue queue;
    String userId="",stAmount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        binding= ActivityAddMoneyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMoneyActivity.this,MyWalletActivity.class));
                finish();
            }
        });
        binding.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stAmount = binding.etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(stAmount)) {
                    binding.etAmount.setError("Amount Number Must Required !");
                    binding.etAmount.requestFocus();
                }
                else {
                    InternetConnectionInterface connectivity = new InternetConnectivity();
                    if (connectivity.isConnected(getApplicationContext())) {
                        addMoney(userId,stAmount);
                    } else {
                        Toast.makeText(AddMoneyActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });




    }

    public void addMoney(String userId,String stAmount) {

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
                            binding.etAmount.setText("");
                            startActivity(new Intent(AddMoneyActivity.this,MyWalletActivity.class));
                            Toast.makeText(AddMoneyActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(AddMoneyActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
                Map<String, String> map = new HashMap<>();
                map.put("action", addAmountToWallet);
                map.put("user_id", userId);
                map.put("amount", stAmount);

                return map;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(request);


    }
}