package com.zoyo7professional.adaper;


import static com.zoyo7professional.ApiData.API.acceptOrder;
import static com.zoyo7professional.ApiData.API.ordersDetails;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.zoyo7professional.ApiData.API;
import com.zoyo7professional.MainActivity;
import com.zoyo7professional.R;
import com.zoyo7professional.activity.AllRequestActivity;
import com.zoyo7professional.activity.RequestCancelActivity;
import com.zoyo7professional.databinding.RowshoworderlayoutBinding;
import com.zoyo7professional.databinding.RowshowrequestsBinding;
import com.zoyo7professional.model.ShowOrderData;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowRequestAdapter extends RecyclerView.Adapter<ShowRequestAdapter.ViewHolder> {
    private Context mcontext;
    private ArrayList<ShowOrderData> orderList;
    TextView  tvRating, tvTime, tvNumber, tvAddress, tvDate, txDesp, txName,txService;
    ImageView  ivService;
    ProgressBar progressBar;
    RequestQueue queue;
    RatingBar ratingStar;
    String user_Id="";
    Button btCancel,btaccept,btAlreadyAccept;
    public ShowRequestAdapter(Context mcontext, ArrayList<ShowOrderData> orderList) {
        this.mcontext = mcontext;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ShowRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowshowrequestsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShowRequestAdapter.ViewHolder holder, int position) {
        ShowOrderData modelObject = orderList.get(position);
        queue = SingletonRequestQueue.getInstance(mcontext).getRequestQueue();
        user_Id = SharedHelper.getKey(mcontext, AppConstats.USER_ID);
        if (modelObject != null) {

            holder.rowshowrequestsBinding.txName.setText(modelObject.getServiceTittle());
            holder.rowshowrequestsBinding.txDate.setText(modelObject.getOrderDate());
            holder.rowshowrequestsBinding.txTime.setText(modelObject.getOrderTime());
             holder.rowshowrequestsBinding.txDetails.setText(modelObject.getServiceName());


             holder.rowshowrequestsBinding.btDetails.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     final Dialog dialog = new Dialog(mcontext);
                     dialog.setContentView(R.layout.dialog_accept_layout);
                     dialog.setCancelable(true);
                      btCancel = dialog.findViewById(R.id.btCancel);
                      btaccept = dialog.findViewById(R.id.btaccept);
                     btAlreadyAccept = dialog.findViewById(R.id.btAlreadyAccept);
                     ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
                     ivService = dialog.findViewById(R.id.ivService);
                     txName = dialog.findViewById(R.id.txName);
                     tvRating = dialog.findViewById(R.id.tvRating);
                     txDesp = dialog.findViewById(R.id.txDesp);
                     tvAddress = dialog.findViewById(R.id.tvAddress);
                     txService = dialog.findViewById(R.id.txService);
                     tvNumber = dialog.findViewById(R.id.tvNumber);
                     tvDate = dialog.findViewById(R.id.tvDate);
                     tvTime = dialog.findViewById(R.id.tvTime);
                     ratingStar = dialog.findViewById(R.id.ratingStar);
                     progressBar = dialog.findViewById(R.id.progressBar);
                     myOrder_Details(modelObject.getServiceId());

                     Toast.makeText(mcontext,modelObject.getServiceId(), Toast.LENGTH_SHORT).show();
                     btCancel.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             mcontext.startActivity(new Intent(mcontext, RequestCancelActivity.class));
                             SharedHelper.putKey(mcontext, AppConstats.ORDER_ID,modelObject.getServiceId());

                         }
                     });
                     ivCancel.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             dialog.dismiss();
                         }
                     });

                     btaccept.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                           accept_Request(modelObject.getServiceId());
                         }
                     });


                     dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                     dialog.show();

                 }
             });



            try {
                Picasso.with(mcontext).load(modelObject.getServicePath() + modelObject.getServiceImage()).into(holder.rowshowrequestsBinding.ivService);
            } catch (Exception e) {

                e.printStackTrace();
            }



        }
    }

    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowshowrequestsBinding rowshowrequestsBinding;

        public ViewHolder(RowshowrequestsBinding rowshowrequestsBinding) {
            super(rowshowrequestsBinding.getRoot());
            this.rowshowrequestsBinding = rowshowrequestsBinding;
        }
    }

    public void accept_Request(String orderId){

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("hjbhjhjg", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {
                            Toast.makeText(mcontext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            mcontext.startActivity(new Intent(mcontext,MainActivity.class));


                        }
                        else {
                            Toast.makeText(mcontext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }
                    }


                } catch (Exception ex) {
                    Log.e("jgvkdfj", ex.getMessage());

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
                map.put("action", acceptOrder);
                map.put("id", orderId); /*order id*/
                map.put("accecpt_by", user_Id);
                return map;
            }
        };
        queue.add(request);
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

                                    String accecpt_cancel_status=jsonData.getString("accecpt_cancel_status");
/*            "accecpt_cancel_status": 0    = not accecpted and not canceled , "accecpt_cancel_status": 1    = accepted and not canceled , "accecpt_cancel_status": 2    = not accepted and canceled

10*/
                                    if (accecpt_cancel_status.equals("0")){
                                      btaccept.setVisibility(View.VISIBLE);
                                      btAlreadyAccept.setVisibility(View.GONE);
                                      btCancel.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Toast.makeText(mcontext, "You have not accepted this order that's why you can't cancel it.", Toast.LENGTH_SHORT).show();
                                          }
                                      });

                                    }/*else if (accecpt_cancel_status.equals("1")) {
                                        btaccept.setVisibility(View.GONE);
                                        btAlreadyAccept.setVisibility(View.VISIBLE);



                                    }
*/

                                    txName.setText(jsonData.getString("service_name"));
                                    txService.setText(jsonData.getString("services_name"));
                                    tvAddress.setText(jsonData.getString("address")+(jsonData.getString("landmark")));
                                    tvDate.setText(jsonData.getString("scheduled_date"));
                                    tvNumber.setText(jsonData.getString("contact_no"));
                                    ratingStar.setRating(Float.parseFloat(jsonData.getString("avrage_rate")));
                                    tvRating.setText(jsonData.getString("avrage_rate")+"Rating");
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

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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
}
