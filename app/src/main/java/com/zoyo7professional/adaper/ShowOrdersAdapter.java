package com.zoyo7professional.adaper;


import static com.zoyo7professional.ApiData.API.completeOrder;
import static com.zoyo7professional.ApiData.API.help;
import static com.zoyo7professional.ApiData.API.ordersDetails;
import static com.zoyo7professional.ApiData.API.startOrder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.zoyo7professional.ApiData.API;
import com.zoyo7professional.MainActivity;
import com.zoyo7professional.R;
import com.zoyo7professional.activity.AddDetailsActivity;
import com.zoyo7professional.activity.HelpActivity;
import com.zoyo7professional.activity.RequestCancelActivity;
import com.zoyo7professional.databinding.RowshoworderlayoutBinding;

import com.zoyo7professional.model.ShowOrderData;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowOrdersAdapter extends RecyclerView.Adapter<ShowOrdersAdapter.ViewHolder> {
    private Context mcontext;
    private ArrayList<ShowOrderData> orderList;
    TextView txRating, tvTime, tvNumber, tvAddress, tvDate, txDesp, txName,txService;
    ProgressBar progressBar;
    RequestQueue queue;
    RatingBar ratingStar;
    String user_Id="";
    ImageView  ivService;
    public ShowOrdersAdapter(Context mcontext, ArrayList<ShowOrderData> orderList) {
        this.mcontext = mcontext;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ShowOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowshoworderlayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShowOrdersAdapter.ViewHolder holder, int position) {
        ShowOrderData modelObject = orderList.get(position);
        queue = SingletonRequestQueue.getInstance(mcontext).getRequestQueue();
        user_Id = SharedHelper.getKey(mcontext, AppConstats.USER_ID);


        if (modelObject != null) {

            holder.rowshoworderlayoutBinding.txName.setText(modelObject.getServiceTittle());
            holder.rowshoworderlayoutBinding.txServiceName.setText(modelObject.getServiceName());
            holder.rowshoworderlayoutBinding.ratingStar.setRating(Float.parseFloat(modelObject.getRating()));

            holder.rowshoworderlayoutBinding.txDate.setText(modelObject.getOrderDate());
            holder.rowshoworderlayoutBinding.txTime.setText(modelObject.getOrderTime());

            holder.rowshoworderlayoutBinding.txRating.setText(modelObject.getRating() + "Rating");

        //    holder.rowshoworderlayoutBinding.txDesp.setText(Html.fromHtml(modelObject.getServiceDescription()) + ".......");
            holder.rowshoworderlayoutBinding.txDespAll.setText(Html.fromHtml(modelObject.getServiceDescription()));

          /*  holder.rowshoworderlayoutBinding.txDespAll.setVisibility(View.VISIBLE);
            holder.rowshoworderlayoutBinding.txDespAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.rowshoworderlayoutBinding.txDespAll.setText(Html.fromHtml(modelObject.getServiceDescription()));
                    holder.rowshoworderlayoutBinding.txDesp.setVisibility(View.GONE);

                }
            });*/

            try {
                Picasso.with(mcontext).load(modelObject.getServicePath() + modelObject.getServiceImage()).into(holder.rowshoworderlayoutBinding.ivService);
            } catch (Exception e) {

                e.printStackTrace();
            }


            String status = modelObject.getStatus();
            /*3 pending  2 cancel, 5 complete ,4 ongoing */
            if (status.equals("5")) {
                holder.rowshoworderlayoutBinding.btCompleted.setVisibility(View.VISIBLE);
                holder.rowshoworderlayoutBinding.btOngoing.setVisibility(View.GONE);
                holder.rowshoworderlayoutBinding.btaccept.setVisibility(View.GONE);
            } else if (status.equals("4")) {
                holder.rowshoworderlayoutBinding.btOngoing.setVisibility(View.VISIBLE);
                holder.rowshoworderlayoutBinding.btCompleted.setVisibility(View.GONE);
                holder.rowshoworderlayoutBinding.btaccept.setVisibility(View.GONE);

                holder.rowshoworderlayoutBinding.btOngoing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(mcontext);
                        dialog.setContentView(R.layout.dialog_completed_layout);
                        dialog.setCancelable(true);
                        Button btOk = dialog.findViewById(R.id.btOk);
                        Button btStart = dialog.findViewById(R.id.btStart);
                        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
                        txName = dialog.findViewById(R.id.txName);
                        txRating = dialog.findViewById(R.id.txRating);
                        txDesp = dialog.findViewById(R.id.txDesp);
                        tvAddress = dialog.findViewById(R.id.tvAddress);
                        txService = dialog.findViewById(R.id.txService);
                        tvNumber = dialog.findViewById(R.id.tvNumber);
                        tvDate = dialog.findViewById(R.id.tvDate);
                        ivService = dialog.findViewById(R.id.ivService);
                        tvTime = dialog.findViewById(R.id.tvTime);
                        ratingStar = dialog.findViewById(R.id.ratingStar);
                        progressBar = dialog.findViewById(R.id.progressBar);
                        myOrder_Details(modelObject.getServiceId());

                        btOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mcontext.startActivity(new Intent(mcontext, MainActivity.class));
                                dialog.dismiss();
                            }
                        });


                        btStart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                completedOrder(modelObject.getServiceId());

                                dialog.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });


                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        dialog.show();
                    }
                });



            } else if (status.equals("3")) {
                holder.rowshoworderlayoutBinding.btaccept.setVisibility(View.VISIBLE);
                holder.rowshoworderlayoutBinding.btOngoing.setVisibility(View.GONE);
                holder.rowshoworderlayoutBinding.btCompleted.setVisibility(View.GONE);

                holder.rowshoworderlayoutBinding.btaccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(mcontext);
                        dialog.setContentView(R.layout.dialog_ongoing_completed_layout);
                        dialog.setCancelable(true);
                        Button btOk = dialog.findViewById(R.id.btOk);
                        Button btStart = dialog.findViewById(R.id.btStart);
                        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
                        txName = dialog.findViewById(R.id.txName);
                        txRating = dialog.findViewById(R.id.txRating);
                        txDesp = dialog.findViewById(R.id.txDesp);
                        tvAddress = dialog.findViewById(R.id.tvAddress);
                        txService = dialog.findViewById(R.id.txService);
                        tvNumber = dialog.findViewById(R.id.tvNumber);
                        tvDate = dialog.findViewById(R.id.tvDate);
                        ivService = dialog.findViewById(R.id.ivService);
                        tvTime = dialog.findViewById(R.id.tvTime);
                        ratingStar = dialog.findViewById(R.id.ratingStar);
                        progressBar = dialog.findViewById(R.id.progressBar);
                        myOrder_Details(modelObject.getServiceId());

                        btOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mcontext.startActivity(new Intent(mcontext, RequestCancelActivity.class));
                                SharedHelper.putKey(mcontext, AppConstats.ORDER_ID,modelObject.getServiceId());
                                dialog.dismiss();
                            }
                        });


                        btStart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startOrder(modelObject.getServiceId());

                                dialog.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });


                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        dialog.show();
                    }
                });

            }
        }
    }


    public void myOrder_Details(String id) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("hfghfgh", response);


                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {

                            String data = jsonObject.getString("data");

                            if (!data.equals("")){
                                JSONArray jsonArray = new JSONArray(data);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonData = jsonArray.getJSONObject(i);

                                    txName.setText(jsonData.getString("service_name"));
                                    txService.setText(jsonData.getString("services_name"));
                                    tvAddress.setText(jsonData.getString("address")+(jsonData.getString("landmark")));
                                    tvDate.setText(jsonData.getString("scheduled_date"));
                                    tvNumber.setText(jsonData.getString("contact_no"));
                                    ratingStar.setRating(Float.parseFloat(jsonData.getString("avrage_rate")));
                                    txRating.setText(jsonData.getString("avrage_rate")+"Rating");
                                    tvTime.setText(jsonData.getString("scheduled_time"));

                                    try {
                                        Picasso.with(mcontext).load(jsonObject.getString("path") + jsonData.getString("services_image")).into(ivService);
                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }


                                    JSONArray jsonArray1 = new JSONArray(jsonData.getString("services"));


                                    Log.e("jndsjkkc", jsonArray1.toString());
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject object = jsonArray1.getJSONObject(j);
                                        txDesp.setText(Html.fromHtml(object.getString("description")));


                                    }

                                }


                            }


                        }


                    }


                } catch (Exception ex) {
                    Log.e("jgvkdfj", ex.getMessage());
                    progressBar.setVisibility(View.GONE);

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e("hxgsgxhj",error.getMessage() );
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", ordersDetails);
                map.put("user_id", user_Id);
                map.put("id", id);/*order id*/

                return map;
            }
        };
        queue.add(request);

    }




    public void startOrder(String serviceId){

       progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("jfdkjgf", response);


                try {

                  progressBar.setVisibility(View.GONE);

                    JSONObject object = new JSONObject(response);

                    if (object.has("result")) {

                        String result = object.getString("result");


                        if (result.equals("true")) {
                            String data = object.getString("data");

                            mcontext.startActivity(new Intent(mcontext,MainActivity.class));
                            Toast.makeText(mcontext, object.getString("message"), Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(mcontext, object.getString("message"), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("csvcxbcbx", error.getMessage() + "");
                progressBar.setVisibility(View.GONE);

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
                map.put("action", startOrder);
                map.put("user_id", user_Id);
                map.put("id", serviceId);

                return map;
            }
        };


        queue.add(request);




    }

    public void completedOrder(String serviceId){

        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("jdkjdklj", response);


                try {

                    progressBar.setVisibility(View.GONE);

                    JSONObject object = new JSONObject(response);

                    if (object.has("result")) {

                        String result = object.getString("result");


                        if (result.equals("true")) {
                            String data = object.getString("data");

                            mcontext.startActivity(new Intent(mcontext,MainActivity.class));
                            Toast.makeText(mcontext, object.getString("message"), Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(mcontext, object.getString("message"), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("jddjdjkj", error.getMessage() + "");
                progressBar.setVisibility(View.GONE);

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
                map.put("action", completeOrder);
                map.put("user_id", user_Id);
                map.put("id", serviceId);

                return map;
            }
        };


        queue.add(request);




    }


    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowshoworderlayoutBinding rowshoworderlayoutBinding;

        public ViewHolder(RowshoworderlayoutBinding rowshoworderlayoutBinding) {
            super(rowshoworderlayoutBinding.getRoot());
            this.rowshoworderlayoutBinding = rowshoworderlayoutBinding;
        }
    }
}
