package com.travel.gwhk.Model;

public class Place2 {
    private String name;
    private String district;
    private String category;
    private String image;
    private String region;
    private String description;
    private String detail;
    private String time;
    private String transport;
    private String fee;
    private String PlaceId;
    private String latitude;
    private String longitude;



    public Place2() {
    }



    public Place2(String name, String district, String category, String image, String region, String description, String detail, String time, String transport, String fee, String placeId, String latitude, String longitude) {
        this.name = name;
        this.district = district;
        this.category = category;
        this.image = image;
        this.region = region;
        this.description = description;
        this.detail = detail;
        this.time = time;
        this.transport = transport;
        this.fee = fee;
        PlaceId = placeId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(String placeId) {
        PlaceId = placeId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
