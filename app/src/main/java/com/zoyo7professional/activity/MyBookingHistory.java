package com.zoyo7professional.activity;

import static com.zoyo7professional.ApiData.API.myBookingHistory;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zoyo7professional.ApiData.API;
import com.zoyo7professional.MainActivity;
import com.zoyo7professional.R;
import com.zoyo7professional.adaper.MyBookingHistoryAdapter;
import com.zoyo7professional.adaper.ShowOrdersAdapter;
import com.zoyo7professional.databinding.ActivityMyBookingHistoryBinding;
import com.zoyo7professional.model.ShowOrderData;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyBookingHistory extends AppCompatActivity {
    RequestQueue queue;
    ActivityMyBookingHistoryBinding binding;
    private MyBookingHistoryAdapter adapter;
    private ArrayList<ShowOrderData>orderList;
    private Context mcontext;
    String user_Id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        binding=ActivityMyBookingHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = SingletonRequestQueue.getInstance(MyBookingHistory.this).getRequestQueue();
        user_Id = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mcontext, RecyclerView.VERTICAL, false);
        binding.rvHistory.setLayoutManager(mLayoutManager);
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyBookingHistory.this, MainActivity.class));
                finish();
            }
        });
        showHistory();

    }
    public void showHistory(){

        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("hfghfgh", response);
                try {
                    binding.progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {
                            String data = jsonObject.getString("data");
                            if (!data.equals("")){
                                JSONArray jsonArray=new JSONArray(data);
                                orderList=new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ShowOrderData orderData=new ShowOrderData();

                                    JSONObject jsonData =jsonArray.getJSONObject(i);
                                    orderData.setOrderDate(jsonData.getString("scheduled_date"));
                                    orderData.setOrderTime(jsonData.getString("scheduled_time"));
                                    JSONArray jsonArray1=new JSONArray(jsonData.getString("services"));


                                    Log.e("jndsjkkc", jsonArray1.toString());
                                    for (int j = 0; j <jsonArray1.length() ; j++) {
                                        JSONObject object=jsonArray1.getJSONObject(j);
                                        orderData.setServiceName(object.getString("service_name"));
                                        orderData.setServiceImage(object.getString("image"));
                                        orderData.setServicePath(jsonObject.getString("path"));
                                        orderData.setRating(object.getString("avrage_rate"));


                                    }
                                orderList.add(orderData);

                                }

                                if (jsonArray.length()==0){
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.rlAnim.setVisibility(View.VISIBLE);
                                }
                                else{
                                    adapter = new MyBookingHistoryAdapter(mcontext, orderList);
                                    binding.rvHistory.setAdapter(adapter);
                                }

                            }



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
                map.put("action",myBookingHistory);
                map.put("user_id",user_Id);
                map.put("payment_status ","5");
                return map;
            }
        };
        queue.add(request);
    }
}