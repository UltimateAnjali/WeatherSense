package com.katra.weathersense.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.katra.weathersense.R;
import com.katra.weathersense.classes.MyFonts;
import com.katra.weathersense.dataModels.Channel;
import com.katra.weathersense.services.WeatherServiceCallback;
import com.katra.weathersense.services.YahooWeatherService;

public class MainActivity extends AppCompatActivity implements WeatherServiceCallback {

    private TextView tempText, locText, condText;
    private ImageView weatherIcon;
    private MyFonts fontFace;

    private YahooWeatherService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        tempText = (TextView)findViewById(R.id.tempTextView);
        locText = (TextView)findViewById(R.id.locTextView);
        condText = (TextView)findViewById(R.id.condTextView);
        weatherIcon = (ImageView)findViewById(R.id.weatherCondImageView);

        fontFace = new MyFonts(getApplicationContext());
        tempText.setTypeface(fontFace.getPoppins());
        locText.setTypeface(fontFace.getSlabo());
        condText.setTypeface(fontFace.getMerri());

        service = new YahooWeatherService(this);
        service.refreshWeather("Sydney, Australia");
    }

    @Override
    public void serviceSuccess(Channel channel) {
        int resId = getResources()
                .getIdentifier("drawable/wi_"+channel.getItem().getCondition().getCode(),
                        null,
                        getPackageName());
        Drawable weatherIconImage = getResources().getDrawable(resId);
        weatherIcon.setImageDrawable(weatherIconImage);
        locText.setText(service.getLocation());
        tempText.setText(channel.getItem().getCondition().getTemperature()+"\u00B0"+channel.getUnits().getTempUnit());
        condText.setText(channel.getItem().getCondition().getDesc());
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
        Log.e("-->>",exception.getMessage());
    }
}
