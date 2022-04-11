package com.zoyo7professional.activity;

import static com.zoyo7professional.ApiData.API.cancelOrdersRequest;
import static com.zoyo7professional.ApiData.API.cancel_policy;
import static com.zoyo7professional.ApiData.API.otpVerify;
import static com.zoyo7professional.ApiData.API.reasonForCancel;
import static com.zoyo7professional.ApiData.API.selectstates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
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
import com.zoyo7professional.adaper.CancelPolicyAdapter;
import com.zoyo7professional.databinding.ActivityRequestBinding;
import com.zoyo7professional.model.CancelPolicyData;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectionInterface;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectivity;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestCancelActivity extends AppCompatActivity {
ActivityRequestBinding binding;
    RequestQueue queue;
    ArrayList<String> arrayListReasonID;
    ArrayList<String> arrayListReason;
    ArrayAdapter<String> adapterReason;
    String strReasonId="",strComments="";

    private CancelPolicyAdapter adapter;
    String user_Id="";
    private ArrayList<CancelPolicyData> policyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        binding = ActivityRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        user_Id = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);


        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();




        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestCancelActivity.this,MainActivity.class));
                finish();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RequestCancelActivity.this, RecyclerView.VERTICAL, false);
        binding.rvPolicy.setLayoutManager(mLayoutManager);

        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strComments=binding.etComments.getText().toString().trim();
                InternetConnectionInterface connectivity = new InternetConnectivity();
                if (connectivity.isConnected(getApplicationContext())) {
                    submitCancelPolicy();
                } else {
                    Toast.makeText(RequestCancelActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                }



            }
        });

        binding.spReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strReasonId = arrayListReasonID.get(i);

                ((TextView) adapterView.getChildAt(0)).setTextSize(15);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        reason_CancelPolicy();
        reason_Cancel();
        //order_details();

    }

    public void cancelRequest() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_request_cancel_layout);
        dialog.setCancelable(true);
        Button btDone = dialog.findViewById(R.id.btDone);

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequestCancelActivity.this,MainActivity.class));
            }
        });



        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


    public void reason_Cancel() {


        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("djfkdj", response);


                arrayListReasonID = new ArrayList<>();
                arrayListReason = new ArrayList<>();
                arrayListReasonID.add("");
                arrayListReason.add("Reason for Cancel");
                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {

                            String data = jsonObject.getString("data");

                            JSONArray jsonArray = new JSONArray(data);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                if (!object.getString("reason").equals("null")) {
                                    arrayListReasonID.add(object.getString("id"));
                                    arrayListReason.add(object.getString("reason"));

                                }
                            }

                            adapterReason = new ArrayAdapter<>(RequestCancelActivity.this, android.R.layout.simple_list_item_1, arrayListReason);
                            adapterReason.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            binding.spReason.setAdapter(adapterReason);
                        }

                    }


                } catch (Exception ex) {
                    Log.e("jgvkdfj", ex.getMessage());

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("thrtfghfg", error.getMessage());

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", reasonForCancel);


                return map;
            }
        };
        queue.add(request);
    }


    public void reason_CancelPolicy() {


        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("djfkdj", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {

                            String data = jsonObject.getString("data");


                            Log.e("chdeck", data );
                           JSONArray jsonArray=new JSONArray(data);
                            policyList=new ArrayList<>();

                            for (int i = 0; i <jsonArray.length() ; i++) {
                                CancelPolicyData policyData=new CancelPolicyData();
                                JSONObject object=jsonArray.getJSONObject(i);
                                policyData.setPolicy(object.getString("tnc"));
                                policyList.add(policyData);





                            }

                            adapter = new CancelPolicyAdapter(RequestCancelActivity.this, policyList);
                            binding.rvPolicy.setAdapter(adapter);
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

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action",cancel_policy);


                return map;
            }
        };
        queue.add(request);
    }

    public void submitCancelPolicy(){

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
                            cancelRequest();
                          //  Toast.makeText(RequestCancelActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                        }
                        else {
                            Toast.makeText(RequestCancelActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                        }

                    }


                } catch (Exception ex) {
                    Log.e("kdkkk", ex.getMessage());

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("jdjfdjj",error.getMessage());
                binding.progressBar.setVisibility(View.GONE);
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action",cancelOrdersRequest);
                map.put("id","2");/* service Id*/
                map.put("cancelled_by",user_Id);
                map.put("cancellation_reason_id",strReasonId);
                map.put("cancellation_reason",strComments);

                return map;
            }
        };
        queue.add(request);

    }
}