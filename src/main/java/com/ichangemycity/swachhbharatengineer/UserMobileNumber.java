package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.callback.OnButtonClick;
import com.ichangemycity.callback.OnResponseListener;
import com.ichangemycity.callback.OnTaskCompleted;
import com.ichangemycity.webservice.URLDataSwachhManch;
import com.ichangemycity.webservice.WebserviceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.ichangemycity.webservice.URLDataSwachhManch.BASE_URL_PROFILE;
import static com.ichangemycity.webservice.URLDataSwachhManch.CHANNEL_KEY_VALUE;

public class UserMobileNumber extends BaseAppCompatActivity {
    EditText mobileNumber;
    Button submit;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        AppController.assignLanguage(UserMobileNumber.this);
        setContentView(R.layout.mobile_number);
        activity = UserMobileNumber.this;

        mobileNumber = (EditText) findViewById(R.id.et_mobno);
        submit = (Button) findViewById(R.id.done);
        mobileNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.toString().trim().length() == 10) {
                    submit.setVisibility(View.VISIBLE);
//					submit.performClick();
                } else {
                    submit.setVisibility(View.GONE);
                }
            }
        });
        mobileNumber.setText(ICMyCPreferenceData.getPreferenceItem(
                UserMobileNumber.this, ICMyCPreferenceData.Mobile_No, ""));

        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppUtils.validateMobileNumber(activity, mobileNumber)) {
                    submitMobileNumberAPI(false);
                }
            }

        });
        setToolbarAndCustomizeTitle((Toolbar) findViewById(R.id.toolbar), " ");
    }

    private void submitMobileNumberAPI(final boolean isToAddOTPSource) {
        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.Mobile_No, mobileNumber.getText().toString());
        AppUtils.showProgressDialog(activity, getString(R.string.loading));
        final String url = URLDataSwachhManch.BASE_URL_AUTH + URLDataSwachhManch.USERS;
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile_number", mobileNumber.getText().toString());
        params.put("mac_address", ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.deviceUniqueID, ""));
        params.put("device_token", ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.deviceToken, ""));
        if (isToAddOTPSource)
            params.put("otp_source", "facebook");
        params.putAll(URLDataSwachhManch.getChannelParam());

        new WebserviceHelper(activity, WebserviceHelper.METHOD_POST, url, params, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {
                AppUtils.hideProgressDialog(activity);
            }

            @Override
            public void OnResponseSuccess(JSONObject response) {
                AppUtils.hideProgressDialog(activity);
                try {
                    if(response.has("access_token")){
                        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.TOKEN_TYPE, response.optString("token_type"));
                        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.token, response.optString("access_token"));
                        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.refresh_token, response.optString("refresh_token"));
//                  getProfileAPICall
                        getProfileAPICall(true);
                    }else {
                        JSONArray array = response.optJSONObject("data").optJSONArray("roles");
                        boolean isEngineer = false;
                        if (array != null)
                            for (int i = 0; i < array.length(); i++) {
                                if (array.get(i).toString().equalsIgnoreCase("Engineer") || array.get(i).toString().equalsIgnoreCase("ulb admin")) {
                                    isEngineer = true;
                                    break;
                                }
                            }
                        if (isEngineer) {
                            startActivity(new Intent(activity, OTPVerification.class));
                        } else {
                            AppController.showAlert(activity, "", "Provided mobile number is not registered as SBM Engineer, please check the " +
                                    "number and try again", false, new OnButtonClick() {
                                @Override
                                public void onPositiveButtonClicked(DialogInterface dialogInterface) {
                                    mobileNumber.setText("");
                                    mobileNumber.requestFocus();
                                }

                                @Override
                                public void onNegativeButtonClicked(DialogInterface dialogInterface) {

                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                if (isToAddOTPSource) {
//                    if (response.has("data")) {
//                        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.Mobile_No, mobileNumber.getText().toString());
//                        response = response.optJSONObject("data");
//                        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.activated, (response.optBoolean("mobile_number_verified")
//                                == false ? "0" : "1"));
//                        submitMobileNumberAPI(false);
//                    }
//                } else {
                //call 1.1 without otpsource
//                    startActivity(new Intent(activity, OTPVerification.class));
//                }
//                if (response.has("access_token")) {
//                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.TOKEN_TYPE, response.optString("token_type"));
//                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.token, response.optString("access_token"));
//                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.refresh_token, response.optString("refresh_token"));
//                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.activated, "1");
////                        getProfileAPICall
//                    getProfileAPICall(true);
//                }


            }
        }, true, WebserviceHelper.HEADER_TYPE_AUTH);
    }

    private void getProfileAPICall(final boolean hasAccessToken) {

        final String url = BASE_URL_PROFILE + CHANNEL_KEY_VALUE;
        new WebserviceHelper(activity, WebserviceHelper.METHOD_GET, url, null,
                new OnResponseListener() {

                    @Override
                    public void OnResponseFailure(JSONObject response) {
                        AppUtils.hideProgressDialog(activity);
                    }

                    @Override
                    public void OnResponseSuccess(final JSONObject response) {
                        final Gson gson = new Gson();
                        final String json = gson.toJson(response);
                        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.profileData, json);
                        new AppUtils().parseProfileGetResponse(activity, response, new OnTaskCompleted() {
                            @Override
                            public void onTaskSuccess(JSONObject jsonObject) {
//                                if (!hasAccessToken) {
//                                    if (!jsonObject.optBoolean("has_password")) {
//                                        // Goto Email,password and confirm password screen
//                                        startActivity(new Intent(activity, SetEmailPasswordActivity.class).putExtra("type", AppConstant
//                                                .ONBOARDING_TYPE_SET_EMAIL_PASSWORD));
//                                    } else {
                                        startActivity(new Intent(activity, MainActivity.class));
////                                    }
//                                } else {
//                                    if (!jsonObject.optBoolean("has_password")) {
//                                        // Goto Email,password and confirm password screen
//                                        startActivity(new Intent(activity, SetEmailPasswordActivity.class).putExtra("type", AppConstant
//                                                .ONBOARDING_TYPE_SET_EMAIL_PASSWORD));
//                                        //call 1.4 if type=setEmailPassword and show three fields in SetEmailPasswordActivity.class
//                                    } else {
//                                        startActivity(new Intent(activity, SetEmailPasswordActivity.class).putExtra("type", AppConstant
//                                                .ONBOARDING_TYPE_LOGIN));
//                                        //call 1.2 if type=login and show three fields in SetEmailPasswordActivity.class
//                                    }
//                                }
                            }

                            @Override
                            public void onTaskFailure(VolleyError error) {
                                submitMobileNumberAPI(false);
                            }
                        });

                    }
                }, true, WebserviceHelper.HEADER_TYPE_PROFILE);
    }

    int retryCount;

    private void setToolbarAndCustomizeTitle(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
        toolbar.setBackgroundColor(Color.WHITE);
//        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mobileNumber != null)
            mobileNumber.setText("");
    }
}
