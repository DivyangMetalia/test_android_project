package com.elluminati.charge.models.datamodels;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StationsItem {
    @SerializedName("images")
    private List<ImagesItem> images;
    @SerializedName("rating")
    private double rating;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("publish")
    private boolean publish;

    @SerializedName("name")
    private String name;

    @SerializedName("coordinates")
    private Coordinates coordinates;

    @SerializedName("id")
    private int id;

    @SerializedName("chargers")
    private List<ChargersItem> chargers;
    private Bitmap bitmap;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ChargersItem> getChargers() {
        return chargers;
    }

    public void setChargers(List<ChargersItem> chargers) {
        this.chargers = chargers;
    }

    @Override
    public String toString() {
        return
                "StationsItem{" +
                        "address = '" + address + '\'' +
                        ",phone = '" + phone + '\'' +
                        ",publish = '" + publish + '\'' +
                        ",name = '" + name + '\'' +
                        ",coordinates = '" + coordinates + '\'' +
                        ",id = '" + id + '\'' +
                        ",chargers = '" + chargers + '\'' +
                        "}";
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<ImagesItem> getImages() {
        return images;
    }

    public void setImages(List<ImagesItem> images) {
        this.images = images;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}