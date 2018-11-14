package com.ichangemycity.swachhbharatengineer;

/**
 * Created by pattabi.raman on 28-09-2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.callback.OnResponseListener;
import com.ichangemycity.callback.OnTaskCompleted;
import com.ichangemycity.webservice.URLData;
import com.ichangemycity.webservice.URLDataSwachhManch;
import com.ichangemycity.webservice.WebserviceHelper;
import com.mukesh.OtpView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ichangemycity.webservice.URLDataSwachhManch.BASE_URL_PROFILE;
import static com.ichangemycity.webservice.URLDataSwachhManch.CHANNEL_KEY_VALUE;

public class OTPVerification extends BaseAppCompatActivity {

    public static Activity activity;
    @Nullable
    @BindView(R.id.otp)
    OtpView otp;
    private final String TAG = OTPVerification.class.getSimpleName();
    @Nullable
    @BindView(R.id.resendCode)
    TextView resendCode;
    @Nullable
    @BindView(R.id.done)
    Button done;
    @Nullable
    @BindView(R.id.enterotp)
    TextView enterotp;
    @Nullable
    @BindView(R.id.parentLayout)
    RelativeLayout parentLayout;
//    public boolean isToShowOTPView = false;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.assignLanguage(this);
        setContentView(R.layout.otp_verify);
        activity = OTPVerification.this;
        ButterKnife.bind(this);
        parentLayout.setVisibility(View.VISIBLE);
        enterotp.setText(activity.getResources().getString(R.string.enter_verification_code_sent_to_)
                + "\n(+91 - " + ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.Mobile_No, "") + ")");
        done.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                if (otp.hasValidOTP()) {
                    runOTPWebService();
                } else {
                }
            }
        });

        resendCode.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                AppUtils.showProgressDialog(activity, getString(R.string.loading));
                onResendOtp();
                //wait for 40 sec until receive resent otp
                new CountDownTimer(40000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        resendCode.setClickable(false);
                        resendCode.setText("Please wait for " + (millisUntilFinished / 1000)+" seconds,\n to resend OTP code");
                    }


                    public void onFinish() {
                        resendCode.setClickable(true);
                        resendCode.setText("Resend code");
                    }
                }.start();
            }
        });
        enterotp.setText(activity.getResources().getString(R.string.enter_verification_code_sent_to_));

        setToolbarAndCustomizeTitle();

    }

    @Nullable
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    private void setToolbarAndCustomizeTitle() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(" ");
        toolbar.setTitleTextColor(Color.TRANSPARENT);
    }

    @Override
    public void onStart() {
        super.onStart();
        AppUtils.hideProgressDialog(activity);
//        if (!isToShowOTPView) {
//            parentLayout.setVisibility(View.GONE);
//        } else {
//            isToShowOTPView = false;
//            AppUtils.hideProgressDialog(activity);
//            parentLayout.setVisibility(View.VISIBLE);
//        }
    }

    private void runOTPWebService() { //1.2
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile_number", ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.Mobile_No, ""));
        params.put("otp", otp.getOTP());
        params.put("device_token", ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.deviceToken, ""));
        params.put("mac_address", ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.deviceUniqueID, ""));
        params.putAll(URLDataSwachhManch.getChannelParam());
        new WebserviceHelper(activity, WebserviceHelper.METHOD_POST, URLDataSwachhManch.BASE_URL_AUTH, params, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {
                AppUtils.hideProgressDialog(activity);
            }

            @Override
            public void OnResponseSuccess(JSONObject response) {
                if (response.has("access_token")) {
                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.TOKEN_TYPE, response.optString("token_type"));
                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.token, response.optString("access_token"));
                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.refresh_token, response.optString("refresh_token"));
//                  getProfileAPICall
                    getProfileAPICall();
                }
            }
        }, true, WebserviceHelper.HEADER_TYPE_AUTH);

    }

    private void getProfileAPICall() {
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
//                                if (!jsonObject.optBoolean("has_password")) {
//                                    // Goto Email,password and confirm password screen
//                                    startActivity(new Intent(activity, SetEmailPasswordActivity.class).putExtra("type", AppConstant
//                                            .ONBOARDING_TYPE_LOGIN));
//                                }  else {
                                    startActivity(new Intent(activity, MainActivity.class));
//                                }
                            }

                            @Override
                            public void onTaskFailure(VolleyError error) {

                            }
                        });

                    }
                }, true, WebserviceHelper.HEADER_TYPE_PROFILE);
    }

    private void onResendOtp() {
        AppUtils.showProgressDialog(activity, activity.getResources().getString(R.string.loading));
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile_number",ICMyCPreferenceData.getPreferenceItem(activity,ICMyCPreferenceData.Mobile_No,""));
        final String url = URLDataSwachhManch.BASE_URL_AUTH
               + URLData.GENERATE_OTP+"?mobile_number="+params.get("mobile_number").toString().trim() ;
        new WebserviceHelper(activity, WebserviceHelper.METHOD_POST, url, params, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {
                AppUtils.hideProgressDialog(activity);
            }

            @Override
            public void OnResponseSuccess(JSONObject responseJsonObject) {
                try {
                     AppUtils.hideProgressDialog(activity);
                    if (responseJsonObject.optInt("httpCode") == 200 || responseJsonObject.optInt("httpCode") == 201) {
                        AppUtils.showToast(activity, AppConstant.TOAST_TYPE_SUCCESS, responseJsonObject.optString("message"));
                    } else {
                        AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO, responseJsonObject.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                        runCommentFeedWebService(true);
            }
        }, true, WebserviceHelper.HEADER_TYPE_NORMAL);
    }

    private void handleResponse(JSONObject mJsonObject) {

        try {

            if (mJsonObject.getInt("httpCode") == 200
                    || mJsonObject.getInt("httpCode") == 201) {
                String userData = mJsonObject.getString("user");
                JSONObject userDataJsonObject = new JSONObject(userData);

                ICMyCPreferenceData.setPreference(activity,
                        ICMyCPreferenceData.location, userDataJsonObject
                                .getString(ICMyCPreferenceData.location));
                ICMyCPreferenceData.setPreference(
                        activity,
                        ICMyCPreferenceData.activated,
                        userDataJsonObject
                                .getInt(ICMyCPreferenceData.activated) + "");

                // insert fresh token into Shared Preference
                if (userDataJsonObject.has(ICMyCPreferenceData.token)) {
                    if (!userDataJsonObject
                            .getString(ICMyCPreferenceData.token).isEmpty())
                        ICMyCPreferenceData.setPreference(
                                activity,
                                ICMyCPreferenceData.token,
                                userDataJsonObject
                                        .getString(ICMyCPreferenceData.token)
                                        + "");
                }
            } else {

                // ICMyCPreferenceData
                // .setPreference(UserOTPVerification.this,
                // ICMyCPreferenceData.token, userDataJson
                // .getString(ICMyCPreferenceData.token));

                try {
                    String errors = mJsonObject
                            .getString("errors");
                    JSONArray mJsonArray = new JSONArray(errors);
                    String error = "";
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        error += mJsonArray.getJSONObject(i).getString(
                                "message")
                                + "\n";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            // TODO: handle exception
        }
        // ICMyCPreferenceData.isOTPVerified = true;

            startActivity(new Intent(activity,
                    MainActivity.class));
            try {
                SelectLanguage.act.finish();
            } catch (Exception e) {
            }
            try {
                activity.finish();
            } catch (Exception e) {
            }

    }
}