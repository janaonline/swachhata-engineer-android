package com.ichangemycity.callback;

import org.json.JSONObject;

public interface OnResponseListener {
    void OnResponseFailure();
    void OnResponseSuccess(JSONObject response);
}
