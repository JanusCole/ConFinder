package com.example.janus.confinder.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public  class Convention {

    @SerializedName("name")
    private String name;

    @SerializedName("url")
    private String website;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("location")
    private Location location;

    private class Location {

        @SerializedName("address")
        private Address address;

        private class Address {

            @SerializedName("addressLocality")
            private String addressLocality;

            @SerializedName("addressRegion")
            private String addressRegion;

            @SerializedName("addressCountry")
            private String addressCountry;

        }

    }

    private Double latitude;

    private Double longitude;


    public String getStartDate() {
        return startDate;
    }

    public String getName () {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getAddress() {
        return location.address.addressLocality + ", " +
                location.address.addressCountry;
    }

    public Double getLatitude () {
        return latitude;
    }

    public Double getLongitude () {
        return longitude;
    }

    public void setLatitude (Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude (Double longitude) {
        this.longitude = longitude;
    }

}