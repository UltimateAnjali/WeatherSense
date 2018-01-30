package com.katra.weathersense.classes;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by anjali desai on 30-01-2018.
 */

public class MyFonts {

    Context mContext;
    Typeface mTypeface;

    public MyFonts(Context context){
        this.mContext = context;
    }

    public Typeface getMerri(){
        mTypeface = Typeface.createFromAsset(mContext.getAssets(),"fonts/Merriweather-Regular.ttf");
        return mTypeface;
    }

    public Typeface getPoppinsThin(){
        mTypeface = Typeface.createFromAsset(mContext.getAssets(),"fonts/Poppins-Thin.ttf");
        return mTypeface;
    }

    public Typeface getSlabo(){
        mTypeface = Typeface.createFromAsset(mContext.getAssets(),"fonts/Slabo27px-Regular.ttf");
        return mTypeface;
    }
}
