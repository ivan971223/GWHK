package com.travel.gwhk.Model;

import java.util.Map;

public class Rating {
    private String uid;
    private String placeId;
    private double rateValue;
    private String comment;
    private Map<String,Object> commentTimeStamp;

    public Rating() {
    }

    public Rating(String uid, String placeId, double rateValue, String comment, Map<String, Object> commentTimeStamp) {
        this.uid = uid;
        this.placeId = placeId;
        this.rateValue = rateValue;
        this.comment = comment;
        this.commentTimeStamp = commentTimeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getRateValue() {
        return rateValue;
    }

    public void setRateValue(double rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Map<String, Object> getCommentTimeStamp() {
        return commentTimeStamp;
    }

    public void setCommentTimeStamp(Map<String, Object> commentTimeStamp) {
        this.commentTimeStamp = commentTimeStamp;
    }
}
