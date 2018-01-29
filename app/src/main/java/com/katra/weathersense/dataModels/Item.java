package com.katra.weathersense.dataModels;

import org.json.JSONObject;

/**
 * Created by anjali desai on 28-01-2018.
 */

public class Item implements JSONPopulator {

    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject object) {
        condition = new Condition();
        condition.populate(object.optJSONObject("condition"));
    }
}
