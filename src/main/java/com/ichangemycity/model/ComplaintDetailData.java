package com.ichangemycity.model;

public class ComplaintDetailData {
    private String id, generic_id, city_id, user_id, access_token, category_id,
            location, landmark, category_name, parent_id, full_name,
            complaint_status_id, complaint_status, complaint_image, user_image,
            comment_count,vote_up_count, radius, posted_on, complaint_image_height, affected,neutral,
            satisfaction, un_satisfied;
    private boolean feedback_count;

    /**
     * @return the neutral
     */
    public String getNeutral() {
        return neutral;
    }

    /**
     * @param neutral the neutral to set
     */
    public void setNeutral(String neutral) {
        this.neutral = neutral;
    }

    /**
     * @return the satisfaction
     */
    public String getSatisfaction() {
        return satisfaction;
    }

    /**
     * @param satisfaction the satisfaction to set
     */
    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

    /**
     * @return the un_satisfied
     */
    public String getUn_satisfied() {
        return un_satisfied;
    }

    /**
     * @param un_satisfied the un_satisfied to set
     */
    public void setUn_satisfied(String un_satisfied) {
        this.un_satisfied = un_satisfied;
    }

    /**
     * @return the feedback_count
     */
    public boolean isFeedback_count() {
        return feedback_count;
    }

    /**
     * @param feedback_count the feedback_count to set
     */
    public void setFeedback_count(boolean feedback_count) {
        this.feedback_count = feedback_count;
    }

    /**
     * @return the vote_up_count
     */
    public String getVote_up_count() {
        return vote_up_count;
    }

    /**
     * @param vote_up_count the vote_up_count to set
     */
    public void setVote_up_count(String vote_up_count) {
        this.vote_up_count = vote_up_count;
    }

    

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the generic_id
     */
    public String getGeneric_id() {
        return generic_id;
    }

    /**
     * @param generic_id the generic_id to set
     */
    public void setGeneric_id(String generic_id) {
        this.generic_id = generic_id;
    }

    /**
     * @return the city_id
     */
    public String getCity_id() {
        return city_id;
    }

    /**
     * @param city_id the city_id to set
     */
    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    /**
     * @return the user_id
     */
    public String getUser_id() {
        return user_id;
    }

    /**
     * @param user_id the user_id to set
     */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    /**
     * @return the access_token
     */
    public String getAccess_token() {
        return access_token;
    }

    /**
     * @param access_token the access_token to set
     */
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    /**
     * @return the category_id
     */
    public String getCategory_id() {
        return category_id;
    }

    /**
     * @param category_id the category_id to set
     */
    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the landmark
     */
    public String getLandmark() {
        return landmark;
    }

    /**
     * @param landmark the landmark to set
     */
    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    /**
     * @return the category_name
     */
    public String getCategory_name() {
        return category_name;
    }

    /**
     * @param category_name the category_name to set
     */
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    /**
     * @return the parent_id
     */
    public String getParent_id() {
        return parent_id;
    }

    /**
     * @param parent_id the parent_id to set
     */
    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    /**
     * @return the full_name
     */
    public String getFull_name() {
        return full_name;
    }

    /**
     * @param full_name the full_name to set
     */
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    /**
     * @return the complaint_status_id
     */
    public String getComplaint_status_id() {
        return complaint_status_id;
    }

    /**
     * @param complaint_status_id the complaint_status_id to set
     */
    public void setComplaint_status_id(String complaint_status_id) {
        this.complaint_status_id = complaint_status_id;
    }

    /**
     * @return the complaint_status
     */
    public String getComplaint_status() {
        return complaint_status;
    }

    /**
     * @param complaint_status the complaint_status to set
     */
    public void setComplaint_status(String complaint_status) {
        this.complaint_status = complaint_status;
    }

    /**
     * @return the complaint_image
     */
    public String getComplaint_image() {
        return complaint_image;
    }

    /**
     * @param complaint_image the complaint_image to set
     */
    public void setComplaint_image(String complaint_image) {
        this.complaint_image = complaint_image;
    }

    /**
     * @return the user_image
     */
    public String getUser_image() {
        return user_image;
    }

    /**
     * @param user_image the user_image to set
     */
    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    /**
     * @return the comment_count
     */
    public String getComment_count() {
        return comment_count;
    }

    /**
     * @param comment_count the comment_count to set
     */
    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    /**
     * @return the radius
     */
    public String getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(String radius) {
        this.radius = radius;
    }

    /**
     * @return the posted_on
     */
    public String getPosted_on() {
        return posted_on;
    }

    /**
     * @param posted_on the posted_on to set
     */
    public void setPosted_on(String posted_on) {
        this.posted_on = posted_on;
    }

    /**
     * @return the complaint_image_height
     */
    public String getComplaint_image_height() {
        return complaint_image_height;
    }

    /**
     * @param complaint_image_height the complaint_image_height to set
     */
    public void setComplaint_image_height(String complaint_image_height) {
        this.complaint_image_height = complaint_image_height;
    }

    /**
     * @return the affected
     */
    public String getAffected() {
        return affected;
    }

    /**
     * @param affected the affected to set
     */
    public void setAffected(String affected) {
        this.affected = affected;
    }

    

}
