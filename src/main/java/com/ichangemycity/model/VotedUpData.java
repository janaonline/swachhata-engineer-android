package com.ichangemycity.model;

public class VotedUpData {
private String id, user_id,full_name,user_image_url,voted_up_on,complaint_count;

  /**
 * @param id the id to set
 */
public void setId(String id) {
    this.id = id;
}

  /**
 * @param user_id the user_id to set
 */
public void setUser_id(String user_id) {
    this.user_id = user_id;
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
 * @return the voted_up_on
 */
public String getVoted_up_on() {
    return voted_up_on;
}

/**
 * @param voted_up_on the voted_up_on to set
 */
public void setVoted_up_on(String voted_up_on) {
    this.voted_up_on = voted_up_on;
}

/**
 * @return the complaint_count
 */
public String getComplaint_count() {
    return complaint_count;
}

/**
 * @param complaint_count the complaint_count to set
 */
public void setComplaint_count(String complaint_count) {
    this.complaint_count = complaint_count;
}
}
