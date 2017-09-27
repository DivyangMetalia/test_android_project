package com.elluminati.charge.models.responsemodels;

import com.elluminati.charge.models.datamodels.StationsItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StationsResponse {

	@SerializedName("stations")
	private List<StationsItem> stations;

	public void setStations(List<StationsItem> stations){
		this.stations = stations;
	}

	public List<StationsItem> getStations(){
		return stations;
	}

	@Override
 	public String toString(){
		return 
			"StationsResponse{" +
			"stations = '" + stations + '\'' + 
			"}";
		}



}