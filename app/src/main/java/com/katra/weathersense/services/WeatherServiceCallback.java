package com.katra.weathersense.services;

import com.katra.weathersense.dataModels.Channel;

/**
 * Created by anjali desai on 28-01-2018.
 */

public interface WeatherServiceCallback {

    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
