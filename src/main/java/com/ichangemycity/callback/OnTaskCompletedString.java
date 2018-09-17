package com.ichangemycity.callback;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pattabi.raman on 04-01-2018.
 */

public interface OnTaskCompletedString {
    public void onTaskSuccess(ArrayList<String> jsonObject);
    public void onTaskFailure(VolleyError error);
}
