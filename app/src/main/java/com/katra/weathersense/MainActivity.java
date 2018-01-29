package com.katra.weathersense;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.katra.weathersense.dataModels.Channel;
import com.katra.weathersense.services.WeatherServiceCallback;
import com.katra.weathersense.services.YahooWeatherService;

public class MainActivity extends AppCompatActivity implements WeatherServiceCallback {

    private TextView tempText, locText, condText;
    private ImageView weatherIcon;

    private YahooWeatherService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempText = (TextView)findViewById(R.id.tempTextView);
        locText = (TextView)findViewById(R.id.locTextView);
        condText = (TextView)findViewById(R.id.condTextView);
        weatherIcon = (ImageView)findViewById(R.id.weatherCondImageView);

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
