package com.travel.gwhk.Model;

public class Bookmark {

    private String PlaceId;
    private String PlaceName;
    private String PlaceDescription;
    private String PlaceCategory;
    private String PlaceDistrict;
    private String PlaceRegion;
    private String PlaceImage;
    private String CategoryId;


    public Bookmark() {
    }

    public Bookmark(String placeId, String placeName, String placeDescription, String placeCategory, String placeDistrict, String placeRegion, String placeImage, String categoryId) {
        PlaceId = placeId;
        PlaceName = placeName;
        PlaceDescription = placeDescription;
        PlaceCategory = placeCategory;
        PlaceDistrict = placeDistrict;
        PlaceRegion = placeRegion;
        PlaceImage = placeImage;
        CategoryId = categoryId;
    }

    public String getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(String placeId) {
        PlaceId = placeId;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public String getPlaceDescription() {
        return PlaceDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        PlaceDescription = placeDescription;
    }

    public String getPlaceCategory() {
        return PlaceCategory;
    }

    public void setPlaceCategory(String placeCategory) {
        PlaceCategory = placeCategory;
    }

    public String getPlaceDistrict() {
        return PlaceDistrict;
    }

    public void setPlaceDistrict(String placeDistrict) {
        PlaceDistrict = placeDistrict;
    }

    public String getPlaceRegion() {
        return PlaceRegion;
    }

    public void setPlaceRegion(String placeRegion) {
        PlaceRegion = placeRegion;
    }

    public String getPlaceImage() {
        return PlaceImage;
    }

    public void setPlaceImage(String placeImage) {
        PlaceImage = placeImage;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }
}