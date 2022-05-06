package com.zoyo7professional.RazorpayPayment;

import android.content.Context;

public interface RazorPayInterface {

    void startPayment(Context context, String amount, String currency, String AppName,String mobile,String email);

}
