
package com.travel.gwhk.Model;

import java.util.List;

public class Place {
    private String Name;
    private String District;
    private String Category;
    private String Image;
    private String Region;
    private String Description;
    private String Time;
    private long RatingCount;
    private double RatingValue;
    private List<Bookmark> place;

    public Place() {
    }

    public Place(String name, String district, String category, String image, String region, String description,String time, double ratingValue,long ratingCount, List<Bookmark> place) {
        Name = name;
        District = district;
        Category = category;
        Image = image;
        Region = region;
        Description = description;
        Time=time;
        RatingCount = ratingCount;
        RatingValue = ratingValue;
        this.place = place;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public double getRatingValue() {
        return RatingValue;
    }

    public void setRatingValue(double ratingValue) {
        RatingValue = ratingValue;
    }

    public List<Bookmark> getPlace() {
        return place;
    }

    public void setPlace(List<Bookmark> place) {
        this.place = place;
    }

    public long getRatingCount() {
        return RatingCount;
    }

    public void setRatingCount(long ratingCount) {
        RatingCount = ratingCount;
    }
}


