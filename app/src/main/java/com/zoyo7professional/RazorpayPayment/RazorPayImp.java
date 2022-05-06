package com.zoyo7professional.RazorpayPayment;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.razorpay.Checkout;

import org.json.JSONObject;

public class RazorPayImp implements RazorPayInterface {
    @Override
    public void startPayment(Context context, String amount, String currency, String AppName,String mobile ,String email) {

        final Checkout checkout = new Checkout();


        try {
            JSONObject options = new JSONObject();
            options.put("name", AppName);
            options.put("description", "Application Payment");
            options.put("currency", currency);
            double total = Double.parseDouble(amount);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", mobile);
            options.put("prefill", preFill);
            checkout.open((Activity) context, options);
        } catch (Exception e) {
            Toast.makeText(context, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }



}
