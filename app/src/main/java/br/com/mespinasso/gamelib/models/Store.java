package br.com.mespinasso.gamelib.models;

import java.io.Serializable;

/**
 * Created by MatheusEspinasso on 14/09/17.
 */

public class Store implements Serializable {

    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String phoneValue;

    public Store(String name, String address, Double latitude, Double longitude, String phone, String phoneValue) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.phoneValue = phoneValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneValue() {
        return phoneValue;
    }

    public void setPhoneValue(String phoneValue) {
        this.phoneValue = phoneValue;
    }
}
