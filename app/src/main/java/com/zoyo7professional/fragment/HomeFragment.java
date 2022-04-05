package com.zoyo7professional.fragment;


import static com.zoyo7professional.ApiData.API.servicesStatus;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.zoyo7professional.activity.AddDetailsActivity;
import com.zoyo7professional.activity.OtpVerifyActivity;
import com.zoyo7professional.activity.RequestCancelActivity;
import com.zoyo7professional.adaper.CancelPolicyAdapter;
import com.zoyo7professional.adaper.ShowOrdersAdapter;
import com.zoyo7professional.databinding.FragmentHomeBinding;
import com.zoyo7professional.model.ShowOrderData;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    RequestQueue queue;
    private FragmentHomeBinding binding;
    private View view;
    private ShowOrdersAdapter adapter;
    private ArrayList<ShowOrderData>orderList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        view = binding.getRoot();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        queue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();

      /*  binding.btOngoing1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
            }
        });

*/
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.drawerLayout.openDrawer(GravityCompat.START);
            }
        });



        accept_Request();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.rvOrders.setLayoutManager(mLayoutManager);

        return view;

    }


    public void request() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_accept_layout);
        dialog.setCancelable(true);
        Button btaccept = dialog.findViewById(R.id.btaccept);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);

        btaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ongoing();
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), RequestCancelActivity.class));
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }



    public void accept_Request() {

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

                            binding.txOngoing.setText(jsonObject.getString("Ongoing"));
                            binding.txCompleted.setText(jsonObject.getString("completed"));
                            String data = jsonObject.getString("data");
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
                                    orderData.setServiceDescription(object.getString("description"));
                                    orderData.setServiceImage(object.getString("image"));
                                    orderData.setServicePath(jsonObject.getString("path"));
                                    orderData.setRating(object.getString("avrage_rate"));
                                    orderList.add(orderData);

                                }


                            }


                        }
                        adapter = new ShowOrdersAdapter(getActivity(), orderList);
                        binding.rvOrders.setAdapter(adapter);

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
                map.put("action",servicesStatus);
                return map;
            }
        };
        queue.add(request);
    }


    public void ongoing() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_ongoing_layout);
        dialog.setCancelable(true);
        Button btOk = dialog.findViewById(R.id.btOk);
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);


        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

       ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }
}