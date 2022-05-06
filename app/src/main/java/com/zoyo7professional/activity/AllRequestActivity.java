package com.zoyo7professional.activity;


import static com.zoyo7professional.ApiData.API.showrequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baoyz.widget.PullRefreshLayout;
import com.zoyo7professional.ApiData.API;
import com.zoyo7professional.MainActivity;
import com.zoyo7professional.R;
import com.zoyo7professional.adaper.MyBookingHistoryAdapter;
import com.zoyo7professional.adaper.ShowRequestAdapter;
import com.zoyo7professional.databinding.ActivityAllRequestBinding;
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

public class AllRequestActivity extends AppCompatActivity {
    ActivityAllRequestBinding binding;
    private ShowRequestAdapter adapter;
    private ArrayList<ShowOrderData> requestList;
    RequestQueue queue;

    String user_Id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        binding=ActivityAllRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        queue = SingletonRequestQueue.getInstance(AllRequestActivity.this).getRequestQueue();
        user_Id = SharedHelper.getKey(AllRequestActivity.this, AppConstats.USER_ID);
        Log.e("dnjcnjd", user_Id);

       // Toast.makeText(this, user_Id, Toast.LENGTH_SHORT).show();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllRequestActivity.this,MainActivity.class));
                finish();
            }
        });


        binding.swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
                showAllRequest();
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AllRequestActivity.this, RecyclerView.VERTICAL, false);
        binding.rvRequest.setLayoutManager(mLayoutManager);
        showAllRequest();
    }

    public void showAllRequest(){
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
                                JSONArray jsonArray = new JSONArray(data);
                                requestList=new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    ShowOrderData requestData=new ShowOrderData();
                                    requestData.setServiceTittle(jsonObject1.getString("service_name"));
                                    requestData.setServiceId(jsonObject1.getString("id"));
                                    requestData.setServiceName(jsonObject1.getString("services_name"));
                                    requestData.setOrderDate(jsonObject1.getString("scheduled_date"));
                                    requestData.setOrderTime(jsonObject1.getString("scheduled_time"));
                                    JSONArray array=new JSONArray(jsonObject1.getString("services"));

                                    for (int j = 0; j <array.length() ; j++) {
                                        JSONObject obj=array.getJSONObject(j);
                                        requestData.setServiceImage(obj.getString("image"));
                                    }

                                    requestData.setServicePath(jsonObject.getString("path"));
                                    requestList.add(requestData);


                                }



                                if (jsonArray.length() ==0) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.rlAnim.setVisibility(View.VISIBLE);
                                  //  Toast.makeText(AllRequestActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    adapter = new ShowRequestAdapter(AllRequestActivity.this, requestList);
                                    binding.rvRequest.setAdapter(adapter);
                                }




                            }
                        }
                    }


                } catch (Exception ex) {
                    Log.e("jgvkdfj", ex.getMessage());
                    binding.progressBar.setVisibility(View.GONE);
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.progressBar.setVisibility(View.GONE);
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", showrequest);
                map.put("user_id", user_Id);
                return map;
            }
        };
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AllRequestActivity.this,MainActivity.class));
        finish();
    }
}