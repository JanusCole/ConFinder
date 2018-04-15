package com.example.janus.confinder.data;

// This is the convention location object used for mapping

public  class ConventionLocation {

    private String name;

    private String website;

    private Double latitude;

    private Double longitude;


    public String getName () {
        return name;
    }

    public String getWebsite() {
        return website;
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

    public void setWebsite(String website) {this.website = website;}

    public void setName (String name) {
        this.name = name;
    }


}