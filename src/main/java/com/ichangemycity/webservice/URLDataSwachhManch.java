package com.ichangemycity.webservice;

import java.util.HashMap;

public class URLDataSwachhManch {

  public static final String AUTH = "auth.ichangemycity.com/";

  //Functions
    public static final String USERS = "users";
  public static final String EMAIL = "email";
  public static final String CHANNEL_KEY_VALUE = "?channel=swachhata-citizen-android";

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
  public static final String Feed = "https://dataapi.swachhmanch.in/";


  //BASE URL
    public static final String BASE_URL_AUTH = Auth;
    public static final String BASE_URL_PROFILE = Profile;


  //Channel
    private static final String CHANNEL_VALUE = "swachhata-engineer-android";
    private static final String CHANNEL_KEY = "channel";

  public static HashMap<String, String> getChannelParam() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(CHANNEL_KEY, CHANNEL_VALUE);
        return map;
    }
}
