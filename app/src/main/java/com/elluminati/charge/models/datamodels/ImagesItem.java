package com.elluminati.charge.models.datamodels;

import com.google.gson.annotations.SerializedName;

public class ImagesItem{

	@SerializedName("ext")
	private String ext;

	@SerializedName("date_modified")
	private String dateModified;

	@SerializedName("date_created")
	private String dateCreated;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("position")
	private int position;

	@SerializedName("url")
	private String url;

	public void setExt(String ext){
		this.ext = ext;
	}

	public String getExt(){
		return ext;
	}

	public void setDateModified(String dateModified){
		this.dateModified = dateModified;
	}

	public String getDateModified(){
		return dateModified;
	}

	public void setDateCreated(String dateCreated){
		this.dateCreated = dateCreated;
	}

	public String getDateCreated(){
		return dateCreated;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPosition(int position){
		this.position = position;
	}

	public int getPosition(){
		return position;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"ImagesItem{" + 
			"ext = '" + ext + '\'' + 
			",date_modified = '" + dateModified + '\'' + 
			",date_created = '" + dateCreated + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",position = '" + position + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}