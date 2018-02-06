package com.katra.weathersense.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.katra.weathersense.R;
import com.katra.weathersense.classes.MyFonts;
import com.katra.weathersense.dataModels.Channel;
import com.katra.weathersense.dataModels.Item;
import com.katra.weathersense.services.WeatherServiceCallback;
import com.katra.weathersense.services.YahooWeatherService;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, WeatherServiceCallback {

    private static final String TAG = "<<MainActivity>>";
    private TextView tempText, locText, condText;
    private ImageView weatherIcon;

    private MyFonts fontFace;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    protected GeoDataClient mGeoDataClient;
    private GoogleApiClient mGoogleApiClient;

    private AutocompleteFilter typeFilter;

    private YahooWeatherService service;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Window window = getWindow();
//        if (Build.VERSION.SDK_INT >= 21) {
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }

        //Constructing a Geo Data Client
        mGeoDataClient = Places.getGeoDataClient(this, null);

        //Constructing a Google Api Client
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        //Mapping the Admob
        mAdView = (AdView) findViewById(R.id.adView);

        //Setting the ads
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Mapping all the elements
        tempText = (TextView)findViewById(R.id.tempTextView);
        locText = (TextView)findViewById(R.id.locTextView);
        condText = (TextView)findViewById(R.id.condTextView);
        weatherIcon = (ImageView)findViewById(R.id.weatherCondImageView);

        //Setting font styles
        fontFace = new MyFonts(getApplicationContext());
        tempText.setTypeface(fontFace.getPoppinsThin());
        locText.setTypeface(fontFace.getSlabo());
        condText.setTypeface(fontFace.getMerri());

        //Implementing the Yahoo Weather Service
        service = new YahooWeatherService(this);
        service.refreshWeather("Sydney, Australia");

        //Creating a filter for the search
        typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_place:{
                try {
                    //Creating an intent to open the autocomplete search for places
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setFilter(typeFilter)
                                    .build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG);
                    Log.e(TAG,e.getMessage());

                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG);
                    Log.e(TAG,e.getMessage());
                }
                return true;
            }

            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                service.refreshWeather((String) place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(TAG, "Operation cancelled");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void serviceSuccess(Channel channel) {

        Item item = channel.getItem();

        //Getting the drawable resource ID to set the icon
        int resId = getResources()
                .getIdentifier("drawable/wi_"+channel.getItem().getCondition().getCode(),
                        null,
                        getPackageName());
        @SuppressWarnings("deprecation") Drawable weatherIconImage = getResources().getDrawable(resId);
        weatherIcon.setImageDrawable(weatherIconImage);


        locText.setText(service.getLocation());

//        int tempInCelsius = (item.getCondition().getTemperature() - 32) * (5/9);
        tempText.setText(item.getCondition().getTemperature()+"\u00B0"+"F");
        condText.setText(item.getCondition().getDesc());
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
        Log.e(TAG,exception.getMessage());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"Connection Failed",Toast.LENGTH_LONG).show();
    }
}
