package com.elluminati.charge.models.datamodels;

import com.google.gson.annotations.SerializedName;

public class Coordinates{

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("longitude")
	private double longitude;

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"Coordinates{" + 
			"latitude = '" + latitude + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}
}