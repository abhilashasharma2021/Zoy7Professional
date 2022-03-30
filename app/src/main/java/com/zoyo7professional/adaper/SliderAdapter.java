package com.zoyo7professional.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.zoyo7professional.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }


    //Array
    public int[] slideImages = {
            R.drawable.slideimg1,
            R.drawable.slideimg2,
            R.drawable.slideimg3
    };


    public String[] slideHeading = {

            "Book Your \n Favourite Stylist",
            "Facilities at \n Door Step",
            "Book Multiple \n Services"
    };


    public String[] slideDesc = {
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's",

            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's",

            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's"
    };


    @Override
    public int getCount() {
        return slideHeading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.screen1, container, false);

        ImageView slide_image = view.findViewById(R.id.slide_image);
        TextView slide_heading = view.findViewById(R.id.slide_heading1);
        TextView slide_descp = view.findViewById(R.id.slide_descp1);

        slide_image.setImageResource(slideImages[position]);
        slide_heading.setText(slideHeading[position]);
        slide_descp.setText(slideDesc[position]);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
