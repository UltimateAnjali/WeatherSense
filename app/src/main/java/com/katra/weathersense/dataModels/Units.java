package com.katra.weathersense.dataModels;

import org.json.JSONObject;

/**
 * Created by anjali desai on 28-01-2018.
 */

public class Units implements JSONPopulator {

    private String tempUnit;

    public String getTempUnit() {
        return tempUnit;
    }

    @Override
    public void populate(JSONObject object) {
        tempUnit = object.optString("temperature");
    }
}
