package com.katra.weathersense.dataModels;

import org.json.JSONObject;

/**
 * Created by anjali desai on 28-01-2018.
 */

public class Condition implements JSONPopulator {

    private int code;
    private int temperature;
    private String desc;

    public int getCode() {
        return code;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public void populate(JSONObject object) {
        code = object.optInt("code");
        temperature = object.optInt("temp");
        desc = object.optString("text");
    }
}
