package com.ichangemycity.webservice;

import android.app.Activity;

import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.callback.OnResponseListener;
import com.ichangemycity.callback.OnTaskCompleted;

import org.json.JSONObject;

import java.util.HashMap;

public class GenerateNewAccessToken {
    public void generateNewAccessToken(final Activity activity, final OnTaskCompleted onTaskCompleted) {
        String url = URLDataSwachhManch.AUTH + "oauth/token";
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("refresh_token", ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.refresh_token, ""));
        requestParams.put("client_id", "1");
        requestParams.put("client_secret", "puvgFGNz0OOi4oLJlwfqjpMBA7aMYGLLTdOPjgd4");
        requestParams.put("grant_type", "refresh_token");

        new WebserviceHelper(activity, WebserviceHelper.METHOD_POST, url, requestParams, new OnResponseListener() {
            @Override
            public void OnResponseFailure() {
                ICMyCPreferenceData.clearPreferences(activity);
                onTaskCompleted.onTaskFailure();
            }

            @Override
            public void OnResponseSuccess(JSONObject response) {
                ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.TOKEN_TYPE, response.optString("token_type"));
                ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.access_token, response.optString("access_token"));
                ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.token, response.optString("access_token"));
                ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.refresh_token, response.optString("refresh_token"));
                onTaskCompleted.onTaskSuccess();
            }
        }, true, WebserviceHelper.HEADER_TYPE_AUTH);
    }
}
