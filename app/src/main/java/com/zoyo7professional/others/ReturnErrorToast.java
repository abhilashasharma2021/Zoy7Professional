package com.zoyo7professional.others;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zoyo7professional.R;

public  abstract class ReturnErrorToast {


    public static void showToast( Activity context) {

        LayoutInflater infl = context.getLayoutInflater();
        View layout = infl.inflate(R.layout.toast_layoyt, context.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.text);
        text.setText("Something went wrong");

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }




}