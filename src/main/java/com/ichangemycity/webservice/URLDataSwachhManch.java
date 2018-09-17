package com.ichangemycity.webservice;

import java.util.HashMap;

public class URLDataSwachhManch {
    //Customize BASE URLs
    public static final String DEV = "http://devapi.";
    public static final String LIVE = "http://";
    public static final String QA = "http://qaapi.";
    public static final String AUTH = "auth.ichangemycity.com/";
    public static final String PROFILE = "profile.ichangemycity.com/";
    public static final String EVENTS = "events.ichangemycity.com/";
    public static final String LOCATION = "locations.ichangemycity.com/";

    //Functions
    public static final String USERS = "users";
    public static final String SOCIAL_ACCOUNT = "social-account";
    public static final String GENERATE_OTP = "generate-otp";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String RESET = "reset";
    public static final String VERIFY = "verify";
    public static final String LOCATIONS = "/locations";
    public static final String CHANNEL_KEY_VALUE = "?channel=swachhata-citizen-android";
    public static final String CHANNEL_SWACHHMANCH_PORTAL = "?channel=swachh-manch-portal";
    public static final String CATEGORIES = "events/categories";
    public static final String VOLUNTEER = "volunteer";
    public static final String VOLUNTEERS = "volunteers";
    public static final String AVATAR = "avatar";
    public static final String BOOKMARKS = "bookmarks";
    public static final String ORGANIZATION = "organizations/";
    public static final String FEED = "organizations/";
    public static final String EMPLOYEES = ORGANIZATION + "employees/";
    public static final String ORG_EMPLOYEES = "?employee_status=employee&channel=swachh-manch-android&per_page=5&page=";
    public static final String EVENTS_BY_EMPLOYEE = "?created_by=organization_employee&organization_id=";
    public static final String EVENTS_BY_ORGANIZATION = "?created_by=organization&organization_id=";
    public static final String EVENTS_BY_PAST = "?created_by=organization&type=past&organization_id=";

    public static final String ORG_FOLLOWERS = "/followers?follow_status=follow&per_page=5&page=";
    public static final String VIDEO_PNG = "https://www.ichangemycity.com/android/images/playvideo.jpg";
    //DEV
//    public static final String Auth = "http://auth.ichangemycity.com/";
//    public static final String Profile = "http://profile.ichangemycity.com/";
//    public static final String Events = "http://events.ichangemycity.com/";
//    public static final String Location = "http://locations.ichangemycity.com/";
//    public static final String Feed = "http://dataapi.ichangemycity.com/";
//    public static final String Terms= "https://swachhmanch.in/disclaimer";

    //QA
//    public static final String Auth = "https://qaauth.swachhmanch.in/";
//    public static final String Profile = "https://qaprofile.swachhmanch.in/";
//    public static final String Events = "https://qaevents.swachhmanch.in/";
//    public static final String Location = "https://qalocations.swachhmanch.in/";
//    public static final String Feed = "https://qadataapi.swachhmanch.in/";
//    public static final String Terms = "https://qa.swachhmanch.in/disclaimer";


    //LIVE
    public static final String Auth = "https://auth.swachhmanch.in/";
    public static final String Profile = "https://profile.swachhmanch.in/";
    public static final String Events = "https://events.swachhmanch.in/";
    public static final String Location = "https://locations.swachhmanch.in/";
    public static final String Feed = "https://dataapi.swachhmanch.in/";
    public static final String Terms = "https://swachhmanch.in/disclaimer";


    //BASE URL
    public static final String BASE_URL_AUTH = Auth;
    public static final String BASE_URL_PROFILE = Profile;
    public static final String BASE_URL_EVENTS = Events;
    public static final String BASE_URL_ORG_FEED = Feed;


    //Channel
    private static final String CHANNEL_VALUE = "swachhata-engineer-android";
    private static final String CHANNEL_KEY = "channel";

    //MapView Thumbnail
    public static final String MAP_THUMBNAIL = "http://maps.google" +
            ".com/maps/api/staticmap?markers=color:red%7C" + "<COORDINATES>" + "&zoom=18&size=1024x768&sensor=true";
    public static final String STAKEHOLDERS = "/stakeholders";

    public static HashMap<String, String> getChannelParam() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(CHANNEL_KEY, CHANNEL_VALUE);
        return map;
    }
}
