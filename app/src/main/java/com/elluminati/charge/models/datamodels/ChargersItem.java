package com.elluminati.charge.models.datamodels;

import com.google.gson.annotations.SerializedName;

public class ChargersItem{

	@SerializedName("user_id")
	private Object userId;

	@SerializedName("level")
	private String level;

	@SerializedName("rate")
	private String rate;

	@SerializedName("available")
	private boolean available;

	@SerializedName("id")
	private int id;

	@SerializedName("power")
	private double power;

	@SerializedName("type")
	private String type;

	@SerializedName("status")
	private String status;

	@SerializedName("gain")
	private String gain;

	public void setUserId(Object userId){
		this.userId = userId;
	}

	public Object getUserId(){
		return userId;
	}

	public void setLevel(String level){
		this.level = level;
	}

	public String getLevel(){
		return level;
	}

	public void setRate(String rate){
		this.rate = rate;
	}

	public String getRate(){
		return rate;
	}

	public void setAvailable(boolean available){
		this.available = available;
	}

	public boolean isAvailable(){
		return available;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPower(double power){
		this.power = power;
	}

	public double getPower(){
		return power;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setGain(String gain){
		this.gain = gain;
	}

	public String getGain(){
		return gain;
	}

	@Override
 	public String toString(){
		return 
			"ChargersItem{" + 
			"user_id = '" + userId + '\'' + 
			",level = '" + level + '\'' + 
			",rate = '" + rate + '\'' + 
			",available = '" + available + '\'' + 
			",id = '" + id + '\'' + 
			",power = '" + power + '\'' + 
			",type = '" + type + '\'' + 
			",status = '" + status + '\'' + 
			",gain = '" + gain + '\'' + 
			"}";
		}
}