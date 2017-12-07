package com.ichangemycity.model;

public class VisitedUserProfileData {
    private String name, mobile_number, latitude, longitude, complaint_count,
            voted_up_count, location, language, language_code, imageUrl,userId;

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the mobile_number
     */
    public String getMobile_number() {
        return mobile_number;
    }

    /**
     * @param mobile_number
     *            the mobile_number to set
     */
    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    /**
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     *            the longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the complaint_count
     */
    public String getComplaint_count() {
        return complaint_count;
    }

    /**
     * @param complaint_count
     *            the complaint_count to set
     */
    public void setComplaint_count(String complaint_count) {
        this.complaint_count = complaint_count;
    }

    /**
     * @return the voted_up_count
     */
    public String getVoted_up_count() {
        return voted_up_count;
    }

    /**
     * @param voted_up_count
     *            the voted_up_count to set
     */
    public void setVoted_up_count(String voted_up_count) {
        this.voted_up_count = voted_up_count;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language
     *            the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the language_code
     */
    public String getLanguage_code() {
        return language_code;
    }

    /**
     * @param language_code
     *            the language_code to set
     */
    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    /**
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl
     *            the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
