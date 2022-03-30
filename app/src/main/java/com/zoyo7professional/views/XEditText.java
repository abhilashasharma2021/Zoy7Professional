package com.zoyo7professional.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.zoyo7professional.R;
import com.zoyo7professional.utilities.FontCache;


/**
 * Created by Manndeep Vachhani on 11/24/2016.
 */

public class XEditText extends AppCompatEditText {

    public XEditText(Context context) {
        super(context);
    }

    public XEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public XEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @Override
    public int getAutofillType() {
        return AUTOFILL_TYPE_NONE;
    }

    private void init(AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFont);
            String fontName = a.getString(R.styleable.CustomFont_fontName);
            if (fontName != null) {
                setTypeface(FontCache.getTypeface(fontName, getContext()));
            }
            a.recycle();
        }
    }
}
