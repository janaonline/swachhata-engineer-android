package com.ichangemycity.appdata;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.prashantsolanki.secureprefmanager.SecurePrefManager;

/**
 * Created by pattabi.raman on 25-09-2017.
 */

public class ICMyCPreferenceData {
    public static final String preferenceName = "preferenceName";
    public static final String selectedLanguage = "selectedLanguage";
    public static final String selectedLanguagePosition = "selectedLanguagePosition";
    public static final String deviceUniqueID = "deviceUniqueID";
    public static final String access_token = "access_token";
    public static final String token = "token";
  public static final String activated = "activated";
    public static final String location = "location";
    public static final String otp = "otp";
    public static final String Latitude = "Latitude";
    public static final String Longitude = "Longitude";
    public static final String userProfileImage = "userProfileImage";
    public static final String roleId = "role_id";
    public static final String designation = "designation";
  public static final String un_assigned_count = "un_assigned_count";

  public static final String deviceToken = "deviceToken";
  public static final String Mobile_No = "Mobile_No";
  public static final String unreadNotificationsCnt = "unreadNotificationsCnt";
  public static final String user_full_name = "user_full_name";
    public static final String id = "id";// logged in user id
  public static final String commentUploadedImageFile = "commentUploadedImageFile";
    public static final String shareImage = "shareImage";
    public static final String deviceWidth = "deviceWidth";
    public static final String deviceHeight = "deviceHeight";
    public static final String high_priority_count = "high_priority_count";
    public static final String on_the_job_count = "on_the_job_count";
    public static final String resolved_count = "resolved_count";
    public static final String re_opened_count = "re_opened_count";
    public static final String rejected_count = "rejected_count";
    public static final String isDeeplinked = "isDeeplinked";
    public static final String assignedCount = "assignedCount";
    public static final String toiletName = "toiletName";
    public static final String toiletAddress = "toiletAddress";
    public static final String wardNo = "wardNo";
    public static final String toiletPincode = "toiletPincode";
    public static final String ulbName = "ulbName";
    public static final String careTakerName = "careTakerName";
    public static final String careTakerNumber = "careTakerNumber";
    public static final String organization = "organization";
    public static final String email = "email";
  public static final String profileData = "profileData";
    public static final String ward_id = "ward_id";
    public static final String city_id = "city_id";
    public static final String TOKEN_TYPE = "token_type";
    public static final String refresh_token = "refresh_token";
    public static final String REDIRECT_TYPE="REDIRECT_TYPE";
  public static final String COMPLAINTVIEWID = "complaintviewid";
    public static final String isResolved = "isResolved";

    public static void setPreference(Context activity, String key, String value) {
        SecurePrefManager.with(activity).set(key).value(value).go();
        AppController.traceLog(key, value);
    }

    public static String getPreferenceItem(Activity activity, String key,
                                           String defaultValue) {
        String value = "NA";
        try {
            value = SecurePrefManager.with(activity).get(key).defaultValue(defaultValue).go();
            AppController.traceLog(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

  public static void clearPreferences(Context activity) {
        SecurePrefManager.with(activity).clear().confirm();
    }

}
