package com.katra.weathersense.dataModels;

import org.json.JSONObject;

/**
 * Created by anjali desai on 28-01-2018.
 */

public class Channel implements JSONPopulator {

    private Units units;
    private Item item;

    public Units getUnits() {
        return units;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public void populate(JSONObject object) {

        units = new Units();
        units.populate(object.optJSONObject("units"));

        item = new Item();
        item.populate(object.optJSONObject("item"));
    }
}
