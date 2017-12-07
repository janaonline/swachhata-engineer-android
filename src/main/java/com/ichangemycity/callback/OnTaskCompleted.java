package com.ichangemycity.callback;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by pattabi.raman on 01-09-2017.
 */

public interface OnTaskCompleted {
    public void onTaskSuccess(JSONObject jsonObject);
    public void onTaskFailure(VolleyError error);
}
