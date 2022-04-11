package com.zoyo7professional.activity;

import static com.zoyo7professional.ApiData.API.addAmountToWallet;
import static com.zoyo7professional.ApiData.API.myAmount;
import static com.zoyo7professional.ApiData.API.walletHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.zoyo7professional.MainActivity;
import com.zoyo7professional.R;
import com.zoyo7professional.adaper.MyBookingHistoryAdapter;
import com.zoyo7professional.adaper.MyWalletHistoryAdapter;
import com.zoyo7professional.databinding.ActivityAddMoneyBinding;
import com.zoyo7professional.databinding.ActivityMyWalletBinding;
import com.zoyo7professional.model.ShowOrderData;
import com.zoyo7professional.model.WalletHistoryData;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyWalletActivity extends AppCompatActivity {
    ActivityMyWalletBinding binding;
    RequestQueue queue;
    String userId="";
    private MyWalletHistoryAdapter adapter;
    private ArrayList<WalletHistoryData>historyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        binding= ActivityMyWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyWalletActivity.this,AddMoneyActivity.class));
            }
        });


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyWalletActivity.this, MainActivity.class));
                finish();
            }
        });
        userId = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MyWalletActivity.this, RecyclerView.VERTICAL, false);
        binding.rvHistory.setLayoutManager(mLayoutManager);

        showMoney(userId);
        wallet_History(userId);




    }


    public void showMoney(String userId) {



        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("hjkjk", response);


                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("result")) {
                        String result = object.getString("result");
                        if (result.equals("true")) {
                            String data = object.getString("data");
                            JSONObject jsonObject = new JSONObject(data);

                            binding.txAmount.setText(jsonObject.getString("wallet_amount"));


                        } else {

                            Toast.makeText(MyWalletActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();

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
                map.put("action", myAmount);
                map.put("user_id", userId);


                return map;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(request);


    }

    public void wallet_History(String userId) {



        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("hjkjk", response);


                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("result")) {
                        String result = object.getString("result");
                        if (result.equals("true")) {
                            String data = object.getString("data");
                            JSONArray jsonArray=new JSONArray(data);
                            historyList=new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj=jsonArray.getJSONObject(i);
                                WalletHistoryData historyData=new WalletHistoryData();
                                historyData.setName(obj.getString("description"));
                                historyData.setAmount(obj.getString("amount"));
                                historyData.setTrnTime(obj.getString("wdatetime"));
                                historyData.setTrnDate(obj.getString("wdatedate"));
                                historyList.add(historyData);



                            }

                            adapter = new MyWalletHistoryAdapter(MyWalletActivity.this, historyList);
                            binding.rvHistory.setAdapter(adapter);

                        } else {

                            Toast.makeText(MyWalletActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();

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
                map.put("action", walletHistory);
                map.put("user_id", userId);


                return map;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(request);


    }
}