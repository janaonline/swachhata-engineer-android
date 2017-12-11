package com.ichangemycity.model;

import java.util.ArrayList;

public class ComplaintData {

    private static ComplaintData instance;

    public static ComplaintData getInstance() {
        // if (instance == null) {
        instance = new ComplaintData();
        // }
        return instance;
    }

    private String complaintId, generic_id, city_id, created_at, user_id,
            category_id, location, landmark, parent_id, full_name,
            complaint_status_id, complaint_status, feed_id, feed_module_id,
            feed_user_activity_id, feed_content_id, feed_created_at,
            feed_user_id, feed_title, feed_full_name, feed_description,
            feed_color, is_feed_high_priority = "0", category_name,
            complaint_image, complaint_image_height, user_image,   radius, affected, neutral, satisfaction,
            un_satisfied, complaint_url, complaint_image_l1,
            complaint_image_l2, latitude, longitude;
    boolean hasFeed;
    private ArrayList<CommentsData> commentsData = new ArrayList<>();

    public ArrayList<CommentsData> getCommentsData() {
        return commentsData;
    }

    public void setCommentsData(ArrayList<CommentsData> commentsData) {
        this.commentsData = commentsData;
    }

    public ArrayList<VotedUpData> getVotedUpData() {
        return votedUpData;
    }

    public void setVotedUpData(ArrayList<VotedUpData> votedUpData) {
        this.votedUpData = votedUpData;
    }

    private  ArrayList<VotedUpData> votedUpData = new ArrayList<>();

    /**
     * @return the hasFeed
     */
    public boolean isHasFeed() {
        return hasFeed;
    }

    /**
     * @param hasFeed the hasFeed to set
     */
    public void setHasFeed(boolean hasFeed) {
        this.hasFeed = hasFeed;
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
     * @return the complaint_image_l1
     */
    public String getComplaint_image_l1() {
        return complaint_image_l1;
    }

    /**
     * @param complaint_image_l1
     *            the complaint_image_l1 to set
     */
    public void setComplaint_image_l1(String complaint_image_l1) {
        this.complaint_image_l1 = complaint_image_l1;
    }

    /**
     * @return the complaint_image_l2
     */
    public String getComplaint_image_l2() {
        return complaint_image_l2;
    }

    /**
     * @param complaint_image_l2
     *            the complaint_image_l2 to set
     */
    public void setComplaint_image_l2(String complaint_image_l2) {
        this.complaint_image_l2 = complaint_image_l2;
    }

    /**
     * @return the complaint_url
     */
    public String getComplaint_url() {
        return complaint_url;
    }

    /**
     * @param complaint_url
     *            the complaint_url to set
     */
    public void setComplaint_url(String complaint_url) {
        this.complaint_url = complaint_url;
    }

    private String comment_count, vote_up_count, posted_on, access_token;
    private boolean isToChangeStatus;

    /**
     * @return the isToChangeStatus
     */
    public boolean isToChangeStatus() {
        return isToChangeStatus;
    }

    /**
     * @param isToChangeStatus
     *            the isToChangeStatus to set
     */
    public void setToChangeStatus(boolean isToChangeStatus) {
        this.isToChangeStatus = isToChangeStatus;
    }

    /**
     * @return the comment_count
     */
    public String getComment_count() {
        return comment_count;
    }

    /**
     * @param comment_count
     *            the comment_count to set
     */
    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    /**
     * @return the vote_up_count
     */
    public String getVote_up_count() {
        return vote_up_count;
    }

    /**
     * @param vote_up_count
     *            the vote_up_count to set
     */
    public void setVote_up_count(String vote_up_count) {
        this.vote_up_count = vote_up_count;
    }

    /**
     * @return the posted_on
     */
    public String getPosted_on() {
        return posted_on;
    }

    /**
     * @param posted_on
     *            the posted_on to set
     */
    public void setPosted_on(String posted_on) {
        this.posted_on = posted_on;
    }

    /**
     * @return the access_token
     */
    public String getAccess_token() {
        return access_token;
    }

    /**
     * @param access_token
     *            the access_token to set
     */
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    private boolean feedback_count;

    /**
     * @return the neutral
     */
    public String getNeutral() {
        return neutral;
    }

    /**
     * @param neutral
     *            the neutral to set
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
     * @param satisfaction
     *            the satisfaction to set
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
     * @param un_satisfied
     *            the un_satisfied to set
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
     * @param feedback_count
     *            the feedback_count to set
     */
    public void setFeedback_count(boolean feedback_count) {
        this.feedback_count = feedback_count;
    }

    /**
     * @return the radius
     */
    public String getRadius() {
        return radius;
    }

    /**
     * @return the affected
     */
    public String getAffected() {
        return affected;
    }

    /**
     * @param affected
     *            the affected to set
     */
    public void setAffected(String affected) {
        this.affected = affected;
    }

    /**
     * @param radius
     *            the radius to set
     */
    public void setRadius(String radius) {
        this.radius = radius;
    }

    /**
     * @return the feed_description
     */
    public String getFeed_description() {
        return feed_description;
    }

    /**
     * @param feed_description
     *            the feed_description to set
     */
    public void setFeed_description(String feed_description) {
        this.feed_description = feed_description;
    }

    /**
     * @return the complaint_image
     */
    public String getComplaint_image() {
        return complaint_image;
    }

    /**
     * @param complaint_image
     *            the complaint_image to set
     */
    public void setComplaint_image(String complaint_image) {
        this.complaint_image = complaint_image;
    }

    /**
     * @return the complaint_image_height
     */
    public String getComplaint_image_height() {
        return complaint_image_height;
    }

    /**
     * @param complaint_image_height
     *            the complaint_image_height to set
     */
    public void setComplaint_image_height(String complaint_image_height) {
        this.complaint_image_height = complaint_image_height;
    }

    /**
     * @return the user_image
     */
    public String getUser_image() {
        return user_image;
    }

    /**
     * @param user_image
     *            the user_image to set
     */
    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    /**
     * @return the complaint_status
     */
    public String getComplaint_status() {
        return complaint_status;
    }

    /**
     * @param complaint_status
     *            the complaint_status to set
     */
    public void setComplaint_status(String complaint_status) {
        this.complaint_status = complaint_status;
    }

    /**
     * @return the category_name
     */
    public String getCategory_name() {
        return category_name;
    }

    /**
     * @param category_name
     *            the category_name to set
     */
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    /**
     * @return the complaintId
     */
    public String getComplaintId() {
        return complaintId;
    }

    /**
     * @param complaintId
     *            the complaintId to set
     */
    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    /**
     * @return the generic_id
     */
    public String getGeneric_id() {
        return generic_id;
    }

    /**
     * @param generic_id
     *            the generic_id to set
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
     * @param city_id
     *            the city_id to set
     */
    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    /**
     * @return the created_at
     */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * @param created_at
     *            the created_at to set
     */
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /**
     * @return the user_id
     */
    public String getUser_id() {
        return user_id;
    }

    /**
     * @param user_id
     *            the user_id to set
     */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    /**
     * @return the category_id
     */
    public String getCategory_id() {
        return category_id;
    }

    /**
     * @param category_id
     *            the category_id to set
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
     * @param location
     *            the location to set
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
     * @param landmark
     *            the landmark to set
     */
    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    /**
     * @return the parent_id
     */
    public String getParent_id() {
        return parent_id;
    }

    /**
     * @param parent_id
     *            the parent_id to set
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
     * @param full_name
     *            the full_name to set
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
     * @param complaint_status_id
     *            the complaint_status_id to set
     */
    public void setComplaint_status_id(String complaint_status_id) {
        this.complaint_status_id = complaint_status_id;
    }

    /**
     * @return the feed_id
     */
    public String getFeed_id() {
        return feed_id;
    }

    /**
     * @param feed_id
     *            the feed_id to set
     */
    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    /**
     * @return the feed_module_id
     */
    public String getFeed_module_id() {
        return feed_module_id;
    }

    /**
     * @param feed_module_id
     *            the feed_module_id to set
     */
    public void setFeed_module_id(String feed_module_id) {
        this.feed_module_id = feed_module_id;
    }

    /**
     * @return the feed_user_activity_id
     */
    public String getFeed_user_activity_id() {
        return feed_user_activity_id;
    }

    /**
     * @param feed_user_activity_id
     *            the feed_user_activity_id to set
     */
    public void setFeed_user_activity_id(String feed_user_activity_id) {
        this.feed_user_activity_id = feed_user_activity_id;
    }

    /**
     * @return the feed_content_id
     */
    public String getFeed_content_id() {
        return feed_content_id;
    }

    /**
     * @param feed_content_id
     *            the feed_content_id to set
     */
    public void setFeed_content_id(String feed_content_id) {
        this.feed_content_id = feed_content_id;
    }

    /**
     * @return the feed_created_at
     */
    public String getFeed_created_at() {
        return feed_created_at;
    }

    /**
     * @param feed_created_at
     *            the feed_created_at to set
     */
    public void setFeed_created_at(String feed_created_at) {
        this.feed_created_at = feed_created_at;
    }

    /**
     * @return the feed_user_id
     */
    public String getFeed_user_id() {
        return feed_user_id;
    }

    /**
     * @param feed_user_id
     *            the feed_user_id to set
     */
    public void setFeed_user_id(String feed_user_id) {
        this.feed_user_id = feed_user_id;
    }

    /**
     * @return the feed_title
     */
    public String getFeed_title() {
        return feed_title;
    }

    /**
     * @param feed_title
     *            the feed_title to set
     */
    public void setFeed_title(String feed_title) {
        this.feed_title = feed_title;
    }

    /**
     * @return the feed_full_name
     */
    public String getFeed_full_name() {
        return this.feed_full_name;
    }

    /**
     * @param feed_full_name
     *            the feed_full_name to set
     */
    public void setFeed_full_name(String feed_full_name) {
        this.feed_full_name = feed_full_name;
    }

    /**
     * @return the feed_color
     */
    public String getFeed_color() {
        return feed_color;
    }

    /**
     * @param feed_color
     *            the feed_color to set
     */
    public void setFeed_color(String feed_color) {
        this.feed_color = feed_color;
    }

    /**
     * @return the is_feed_high_priority
     */
    public String get_is_feed_high_priority() {
        return is_feed_high_priority;
    }

    /**
     * @param is_feed_high_priority
     *            the is_feed_high_priority to set
     */
    public void set_is_feed_high_priority(String is_feed_high_priority) {
        this.is_feed_high_priority = is_feed_high_priority;
    }

}
