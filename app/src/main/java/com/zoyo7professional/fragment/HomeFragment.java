package com.zoyo7professional.fragment;


import static com.zoyo7professional.ApiData.API.myOrders;
import static com.zoyo7professional.ApiData.API.selectecity;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.zoyo7professional.activity.RequestCancelActivity;
import com.zoyo7professional.adaper.ShowOrdersAdapter;
import com.zoyo7professional.databinding.FragmentHomeBinding;
import com.zoyo7professional.model.ShowOrderData;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
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
    String user_Id = "";
    private ArrayList<ShowOrderData> orderList;
    ArrayList<String> arrayListCityID;
    ArrayList<String> arrayListCity;
    ArrayAdapter<String> adapterCity;
    String strCityId = "", st_CityName = "", stStatus = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        view = binding.getRoot();
        //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        queue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();
        user_Id = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
        st_CityName = SharedHelper.getKey(getActivity(), AppConstats.CITY_NAME);
        strCityId = SharedHelper.getKey(getActivity(), AppConstats.CITY_ID);

        Log.e("cityNAme", st_CityName);
        Log.e("cityid", strCityId);
        Log.e("user_Id", user_Id);


        if (!st_CityName.equals("") || !st_CityName.equals(null)) {
            binding.tvCity.setText(st_CityName);
        }
        binding.cardCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.cardCity.setVisibility(View.GONE);
                binding.cardSpin.setVisibility(View.VISIBLE);


            }
        });
        binding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strCityId = arrayListCityID.get(i);
                Toast.makeText(getActivity(), strCityId, Toast.LENGTH_SHORT).show();

                Log.e("checkid", strCityId);
                ((TextView) adapterView.getChildAt(0)).setTextSize(15);
                SharedHelper.putKey(getActivity(), AppConstats.CITY_NAME, arrayListCity.get(i));
                binding.tvCity.setText(arrayListCity.get(i));

                accept_Request(stStatus, strCityId);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        choose_city();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.rvOrders.setLayoutManager(mLayoutManager);
        Log.e("ncmnxc", st_CityName);
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.rlPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stStatus = "3";
                accept_Request(stStatus, strCityId);


            }
        });


        binding.rlOngoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stStatus = "4";
                accept_Request(stStatus, strCityId);
            }
        });

        binding.rlCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stStatus = "5";
                accept_Request(stStatus, strCityId);
            }
        });


        binding.swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
                accept_Request(stStatus, strCityId);
            }
        });

        accept_Request(stStatus, strCityId);


        return view;

    }


    public void accept_Request(String stStatus, String strCity) {

        Log.e("sjxhsjh", strCity);


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
                            binding.txPending.setText(jsonObject.getString("scheduled"));
                            String data = jsonObject.getString("data");

                            if (!data.equals("")) {
                                JSONArray jsonArray = new JSONArray(data);
                                orderList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ShowOrderData orderData = new ShowOrderData();
                                    JSONObject jsonData = jsonArray.getJSONObject(i);
                                    orderData.setServiceId(jsonData.getString("id"));
                                    orderData.setOrderDate(jsonData.getString("scheduled_date"));
                                    orderData.setOrderTime(jsonData.getString("scheduled_time"));
                                    orderData.setServiceTittle(jsonData.getString("service_name"));
                                    orderData.setServiceName(jsonData.getString("services_name"));
                                    orderData.setStatus(jsonData.getString("payment_status"));
                                    orderData.setServiceImage(jsonData.getString("services_image"));
                                    orderData.setServicePath(jsonObject.getString("path"));
                                    orderData.setRating(jsonData.getString("avrage_rate"));
                                    JSONArray jsonArray1 = new JSONArray(jsonData.getString("services"));

                                    Log.e("jndsjkkc", jsonArray1.toString());
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject object = jsonArray1.getJSONObject(j);
                                        orderData.setServiceDescription(object.getString("description"));


                                    }
                                    orderList.add(orderData);
                                }

                                if (jsonArray.length() == 0) {
                                    binding.rlAnim.setVisibility(View.VISIBLE);
                                    binding.progressBar.setVisibility(View.GONE);

                                } else {
                                    binding.rlAnim.setVisibility(View.GONE);

                                    adapter = new ShowOrdersAdapter(getActivity(), orderList);
                                    binding.rvOrders.setAdapter(adapter);
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
                binding.progressBar.setVisibility(View.GONE);


            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", myOrders);
                map.put("user_id", user_Id);
                //     map.put("city_id", strCity);
                map.put("payment_status", stStatus);
                return map;
            }
        };
        queue.add(request);
    }


    public void choose_city() {


        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("hgedhgefhdfj", response);


                arrayListCityID = new ArrayList<>();
                arrayListCity = new ArrayList<>();
                arrayListCityID.add("");
                arrayListCity.add("Select City");
                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {

                            String data = jsonObject.getString("data");

                            JSONArray jsonArray = new JSONArray(data);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                if (!object.getString("city").equals("null")) {
                                    arrayListCityID.add(object.getString("id"));
                                    arrayListCity.add(object.getString("city"));

                                }
                            }

                            adapterCity = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayListCity);
                            adapterCity.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            binding.spCity.setAdapter(adapterCity);
                        }

                    }


                } catch (Exception ex) {
                    Log.e("fvfdv", ex.getMessage());

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", selectecity);


                return map;
            }
        };
        queue.add(request);
    }

}