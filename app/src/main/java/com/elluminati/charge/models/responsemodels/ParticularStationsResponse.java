package com.elluminati.charge.models.responsemodels;

import com.elluminati.charge.models.datamodels.StationsItem;
import com.google.gson.annotations.SerializedName;

/**
 * Created by elluminati on 21-Sep-17.
 */

public class ParticularStationsResponse {
    @SerializedName("station")
    private StationsItem station;

    public StationsItem getStation() {
        return station;
    }

    public void setStation(StationsItem stations) {
        this.station = stations;
    }
}
