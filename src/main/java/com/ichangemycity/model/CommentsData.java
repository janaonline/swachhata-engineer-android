package com.ichangemycity.model;

public class CommentsData {
    private String comment_id, comment_user_id, comment_type_id,
            comment_full_name, comment_description, comment_posted_on,
            comment_complaint_status, comment_complaint_status_id,
            comment_image_url,user_image_url;
    public String getSpanColorForCoplaintStatus() {
        return spanColorForCoplaintStatus;
    }

    public void setSpanColorForCoplaintStatus(String spanColorForCoplaintStatus) {
        this.spanColorForCoplaintStatus = spanColorForCoplaintStatus;
    }

    private String spanColorForCoplaintStatus;

    /**
     * @return the user_image_url
     */
    public String getUser_image_url() {
        return user_image_url;
    }

    /**
     * @param user_image_url the user_image_url to set
     */
    public void setUser_image_url(String user_image_url) {
        this.user_image_url = user_image_url;
    }

    /**
     * @return the comment_id
     */
    public String getComment_id() {
        return comment_id;
    }

    /**
     * @param comment_id the comment_id to set
     */
    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    /**
     * @return the comment_user_id
     */
    public String getComment_user_id() {
        return comment_user_id;
    }

    /**
     * @param comment_user_id the comment_user_id to set
     */
    public void setComment_user_id(String comment_user_id) {
        this.comment_user_id = comment_user_id;
    }

    /**
     * @return the comment_type_id
     */
    public String getComment_type_id() {
        return comment_type_id;
    }

    /**
     * @param comment_type_id the comment_type_id to set
     */
    public void setComment_type_id(String comment_type_id) {
        this.comment_type_id = comment_type_id;
    }

    /**
     * @return the comment_full_name
     */
    public String getComment_full_name() {
        return comment_full_name;
    }

    /**
     * @param comment_full_name the comment_full_name to set
     */
    public void setComment_full_name(String comment_full_name) {
        this.comment_full_name = comment_full_name;
    }

    /**
     * @return the comment_description
     */
    public String getComment_description() {
        return comment_description;
    }

    /**
     * @param comment_description the comment_description to set
     */
    public void setComment_description(String comment_description) {
        this.comment_description = comment_description;
    }

    /**
     * @return the comment_posted_on
     */
    public String getComment_posted_on() {
        return comment_posted_on;
    }

    /**
     * @param comment_posted_on the comment_posted_on to set
     */
    public void setComment_posted_on(String comment_posted_on) {
        this.comment_posted_on = comment_posted_on;
    }

    /**
     * @return the comment_complaint_status
     */
    public String getComment_complaint_status() {
        return comment_complaint_status;
    }

    /**
     * @param comment_complaint_status the comment_complaint_status to set
     */
    public void setComment_complaint_status(String comment_complaint_status) {
        this.comment_complaint_status = comment_complaint_status;
    }

    /**
     * @return the comment_complaint_status_id
     */
    public String getComment_complaint_status_id() {
        return comment_complaint_status_id;
    }

    /**
     * @param comment_complaint_status_id the comment_complaint_status_id to set
     */
    public void setComment_complaint_status_id(String comment_complaint_status_id) {
        this.comment_complaint_status_id = comment_complaint_status_id;
    }

    /**
     * @return the comment_image_url
     */
    public String getComment_image_url() {
        return comment_image_url;
    }

    /**
     * @param comment_image_url the comment_image_url to set
     */
    public void setComment_image_url(String comment_image_url) {
        this.comment_image_url = comment_image_url;
    }
}
