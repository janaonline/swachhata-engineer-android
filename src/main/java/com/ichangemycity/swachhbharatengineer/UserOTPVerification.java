package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.callback.OnButtonClick;
import com.ichangemycity.callback.OnResponseListener;
import com.ichangemycity.webservice.URLData;
import com.ichangemycity.webservice.WebserviceHelper;
import com.mukesh.OtpView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserOTPVerification extends BaseAppCompatActivity {
    public static OtpView otp;
    public static Activity activity;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.assignLanguage(UserOTPVerification.this);
        setContentView(R.layout.otp_verification);
        activity = UserOTPVerification.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarAndCustomizeTitle(getResources().getString(R.string.enter_otp));
        ((TextView) findViewById(R.id.enterotp)).setText(getResources()
                .getString(R.string.enter_verification_code_sent_to_)
                + " \n ("
                + ICMyCPreferenceData.getPreferenceItem(
                UserOTPVerification.this,
                ICMyCPreferenceData.Mobile_No, "") + ")");
        otp = (OtpView) findViewById(R.id.otp);

        ((Button) findViewById(R.id.done))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (otp.hasValidOTP()) {
                            validateOTP(otp.getOTP());
                        }
                    }
                });
        ((TextView) findViewById(R.id.resendCode))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        doOTPTimer();
                        AppController.showProgressDialog(activity,getResources().getString(R.string.loading));
                        String url = (URLData.BASE_URL
                                + URLData.CHECK_ACTIVE_ENGINEER
                                + ICMyCPreferenceData.getPreferenceItem(
                                UserOTPVerification.this,
                                ICMyCPreferenceData.Mobile_No, "")
                                + "&deviceToken="
                                + ICMyCPreferenceData.getPreferenceItem(
                                UserOTPVerification.this,
                                ICMyCPreferenceData.deviceToken, "")
                                + "&lang="
                                + ICMyCPreferenceData.getPreferenceItem(
                                UserOTPVerification.this,
                                ICMyCPreferenceData.selectedLanguage,
                                "en") + "&macAddress=" + ICMyCPreferenceData
                                .getPreferenceItem(UserOTPVerification.this,
                                        ICMyCPreferenceData.deviceUniqueID, ""));

                        new WebserviceHelper(activity, WebserviceHelper.METHOD_GET, url, null, new OnResponseListener() {
                            @Override
                            public void OnResponseFailure(JSONObject response) {
                                AppController.hideProgressDialog(activity);

                            }

                            @Override
                            public void OnResponseSuccess(final JSONObject mJsonObject) {

                                try {
                                    AppController.hideProgressDialog(activity);
                                    if (mJsonObject.optInt("httpCode") == 200
                                            || mJsonObject
                                            .optInt("httpCode") == 201) {

                                        String userData = mJsonObject
                                                .optString("engineer");

                                        JSONObject userDataJsonObject = new JSONObject(
                                                userData);
                                        if (Integer
                                                .parseInt(userDataJsonObject
                                                        .get(ICMyCPreferenceData.activated)
                                                        .toString()) == 1) {
                                            ICMyCPreferenceData
                                                    .setPreference(
                                                            UserOTPVerification.this,
                                                            ICMyCPreferenceData.token,
                                                            userDataJsonObject
                                                                    .getString(ICMyCPreferenceData.token)
                                                                    + "");

                                            handleResendOTPResponse(mJsonObject);
                                        }

                                    } else {
                                        AppUtils.showToast( UserOTPVerification.this, AppConstant.TOAST_TYPE_INFO,  mJsonObject
                                                .optString("message"));
//                                                Toast.makeText(
//                                                        UserOTPVerification.this,
//                                                        mJsonObject
//                                                                .optString("message"),
//                                                        Toast.LENGTH_LONG)
//                                                        .show();
                                        String errors = "";
                                        try { // more than one error
                                            errors = (mJsonObject)
                                                    .optString("errors");
                                            JSONArray mJsonArray = new JSONArray(
                                                    errors);
                                            String error = "";
                                            for (int i = 0; i < mJsonArray
                                                    .length(); i++) {
                                                error += mJsonArray
                                                        .getJSONObject(i)
                                                        .optString("message")
                                                        + "\n";
                                            }
                                            AppController
                                                    .showAlert(
                                                            UserOTPVerification.this, mJsonObject
                                                                    .optString("message"), error, false, new OnButtonClick() {
                                                                @Override
                                                                public void onPositiveButtonClicked(DialogInterface dialogInterface) {

                                                                }

                                                                @Override
                                                                public void onNegativeButtonClicked(DialogInterface dialogInterface) {

                                                                }
                                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            try { // one error
                                                AppController
                                                        .showAlert(
                                                                UserOTPVerification.this,
                                                                "",
                                                                mJsonObject
                                                                        .optString("message"), false, new OnButtonClick() {
                                                                    @Override
                                                                    public void onPositiveButtonClicked(DialogInterface dialogInterface) {

                                                                    }

                                                                    @Override
                                                                    public void onNegativeButtonClicked(DialogInterface dialogInterface) {

                                                                    }
                                                                });
                                            } catch (Exception e1) {
                                                // TODO Auto-generated catch
                                                // block
                                                e1.printStackTrace();
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                            }
                        },true,WebserviceHelper.HEADER_TYPE_NORMAL);


                    }
                });



        doOTPTimer();

    }

    private void setToolbarAndCustomizeTitle(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        getSupportActionBar().setTitle(" ");
        toolbar.setTitleTextColor(Color.BLACK);


    }

    @SuppressWarnings("deprecation")
    private void  validateOTP(final String otpText) {
        AppController.showProgressDialog(activity, activity.getResources().getString(R.string.loading));
        final String url=URLData.BASE_URL + URLData.AUTH;

       HashMap<String, String> params = new HashMap<String, String>();
        params.put("apiKey", URLData.API_KEY);
        params.put("mobileNumber",
                ICMyCPreferenceData.getPreferenceItem(UserOTPVerification.this,
                        ICMyCPreferenceData.Mobile_No, ""));
        params.put("otp", otpText);
        params.put("macAddress",
                ICMyCPreferenceData.getPreferenceItem(UserOTPVerification.this,
                        ICMyCPreferenceData.deviceUniqueID, ""));
        params.put("lang", ICMyCPreferenceData
                .getPreferenceItem(UserOTPVerification.this,
                        ICMyCPreferenceData.selectedLanguage, ""));
        params.put("deviceToken",
                ICMyCPreferenceData.getPreferenceItem(UserOTPVerification.this,
                        ICMyCPreferenceData.deviceToken, ""));
        new WebserviceHelper(activity, WebserviceHelper.METHOD_POST, url, params, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {
             //  Toast.makeText(UserMobileNumber.this, error.toString(), Toast.LENGTH_LONG).show();
                AppController.hideProgressDialog(activity);
               // AppController.handleVolleyError(activity, (RelativeLayout) activity.findViewById(R.id.parentLayout), error);
            }

            @Override
            public void OnResponseSuccess(JSONObject response) {

             /*   JSONObject responseJsonObject = null;
                try {
                    AppController.hideProgressDialog(activity);
                    responseJsonObject = new JSONObject(response);
                    handleResponse(response);
                    AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO,  responseJsonObject
                            .optString("message"));
//                            Toast.makeText(activity, responseJsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                AppController.hideProgressDialog(activity);
                handleResponse(response);
                AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO,  response
                        .optString("message"));
//                            Toast.makeText(activity, responseJsonObject.optString("message"), Toast.LENGTH_SHORT).show();

            }
        },true,WebserviceHelper.HEADER_TYPE_NORMAL);




        // http://devapi.ichangemycity.in/engineer/v1/auth?apiKey=af4e61d75d2782a33eac7641e42bba6f&mobileNumber=9845224845&otp=1964&macAddress
        // =af4e61d75d2782a33eac7641e42bba6f&lang=en

    }

    private void handleResendOTPResponse(JSONObject mJsonObject) {
        try {
            if (mJsonObject.optInt("httpCode") == 200
                    || mJsonObject.optInt("httpCode") == 201) {
                if (!mJsonObject.getString(ICMyCPreferenceData.otp).isEmpty()) {
                    /*
                     * ICMyCPreferenceData.setPreference(UserOTPVerification.this
					 * , ICMyCPreferenceData.otp,
					 * mJsonObject.getString(ICMyCPreferenceData.otp));
					 */
                    otp.setOTP(ICMyCPreferenceData.getPreferenceItem(
                            UserOTPVerification.this, ICMyCPreferenceData.otp,
                            ""));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   /* private void handleResponse(String response) {

        try {
            *//*
             * response: {"location":"","activated":1,"token":
			 * "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNSIsImlzcyI6Imh0dHA6XC9cL2RldmFwaS5pY2hhbmdlbXljaXR5LmluXC9zYm1cL3YxXC9hdXRoIiwiaWF0IjoxNDU0MzMzNzYyLCJleHAiOjE0NTQzMzczNjIsIm5iZiI6MTQ1NDMzMzc2MiwianRpIjoiNzc4NDcyZTljYjI3Zjg1MDIxY2E2ZjVlZjIxZWIwMjYifQ.urjVhPEothlbnWX84GI4WEuOswc0S9pGZA11cLdK9Eg"
			 * }
			 *//*
            JSONObject mJsonObject = new JSONObject(response);
            if (mJsonObject.optInt("httpCode") == 200
                    || mJsonObject.optInt("httpCode") == 201) {
                String userData = mJsonObject.optString("engineer");
                JSONObject userDataJsonObject = new JSONObject(userData);

                ICMyCPreferenceData
                        .setPreference(UserOTPVerification.this,
                                ICMyCPreferenceData.token, userDataJsonObject
                                        .getString(ICMyCPreferenceData.token));
                ICMyCPreferenceData.setPreference(
                        UserOTPVerification.this,
                        ICMyCPreferenceData.activated,
                        userDataJsonObject
                                .getInt(ICMyCPreferenceData.activated) + "");
                ICMyCPreferenceData.setPreference(UserOTPVerification.this,
                        ICMyCPreferenceData.id,
                        userDataJsonObject.getString(ICMyCPreferenceData.id)
                                + "");
            } else {

                try {
                    String errors = new JSONObject(response)
                            .optString("errors");
                    JSONArray mJsonArray = new JSONArray(errors);
                    String error = "";
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        error += mJsonArray.getJSONObject(i).getString(
                                "message")
                                + "\n";
                    }
                    AppController.showAlert(UserOTPVerification.this,
                            new JSONObject(response).optString("message"),
                            error, false, new OnButtonClick() {
                                @Override
                                public void onPositiveButtonClicked(DialogInterface dialogInterface) {

                                }

                                @Override
                                public void onNegativeButtonClicked(DialogInterface dialogInterface) {

                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            // TODO: handle exception
        }
        // ICMyCPreferenceData.isOTPVerified = true;
        if (ICMyCPreferenceData.getPreferenceItem(UserOTPVerification.this,
                ICMyCPreferenceData.activated, "0").equalsIgnoreCase("1")) {
            startActivity(new Intent(UserOTPVerification.this,
                    MainActivity.class));
            UserOTPVerification.this.finish();
        }

    }*/
 private void handleResponse(JSONObject mJsonObject) {

        try {
            /*
             * response: {"location":"","activated":1,"token":
			 * "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNSIsImlzcyI6Imh0dHA6XC9cL2RldmFwaS5pY2hhbmdlbXljaXR5LmluXC9zYm1cL3YxXC9hdXRoIiwiaWF0IjoxNDU0MzMzNzYyLCJleHAiOjE0NTQzMzczNjIsIm5iZiI6MTQ1NDMzMzc2MiwianRpIjoiNzc4NDcyZTljYjI3Zjg1MDIxY2E2ZjVlZjIxZWIwMjYifQ.urjVhPEothlbnWX84GI4WEuOswc0S9pGZA11cLdK9Eg"
			 * }
			 */
           // JSONObject mJsonObject = new JSONObject(response);
            if (mJsonObject.optInt("httpCode") == 200
                    || mJsonObject.optInt("httpCode") == 201) {
                String userData = mJsonObject.optString("engineer");
                JSONObject userDataJsonObject = new JSONObject(userData);

                ICMyCPreferenceData
                        .setPreference(UserOTPVerification.this,
                                ICMyCPreferenceData.token, userDataJsonObject
                                        .getString(ICMyCPreferenceData.token));
                ICMyCPreferenceData.setPreference(
                        UserOTPVerification.this,
                        ICMyCPreferenceData.activated,
                        userDataJsonObject
                                .getInt(ICMyCPreferenceData.activated) + "");
                ICMyCPreferenceData.setPreference(UserOTPVerification.this,
                        ICMyCPreferenceData.id,
                        userDataJsonObject.getString(ICMyCPreferenceData.id)
                                + "");
            } else {

                try {
                    /*String errors = new JSONObject(response)
                            .optString("errors");*/
                    String errors = mJsonObject.optString("errors");
                    JSONArray mJsonArray = new JSONArray(errors);
                    String error = "";
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        error += mJsonArray.getJSONObject(i).getString(
                                "message")
                                + "\n";
                    }
                    AppController.showAlert(UserOTPVerification.this,
                            mJsonObject.optString("message"),
                            error, false, new OnButtonClick() {
                                @Override
                                public void onPositiveButtonClicked(DialogInterface dialogInterface) {

                                }

                                @Override
                                public void onNegativeButtonClicked(DialogInterface dialogInterface) {

                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            // TODO: handle exception
        }
        // ICMyCPreferenceData.isOTPVerified = true;
        if (ICMyCPreferenceData.getPreferenceItem(UserOTPVerification.this,
                ICMyCPreferenceData.activated, "0").equalsIgnoreCase("1")) {
            startActivity(new Intent(UserOTPVerification.this,
                    MainActivity.class));
            UserOTPVerification.this.finish();
        }

    }
    private void doOTPTimer() {
        new CountDownTimer(40000, 1000) {

            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.resendCode)).setClickable(false);
                ((TextView) findViewById(R.id.resendCode))
                        .setLongClickable(false);
                ((TextView) findViewById(R.id.resendCode)).setFocusable(false);
                ((TextView) findViewById(R.id.resendCode))
                        .setFocusableInTouchMode(false);
                ((TextView) findViewById(R.id.resendCode))
                        .setText(""
                                + String.format(
                                "00:%1$02d",
                                TimeUnit.MILLISECONDS
                                        .toSeconds(millisUntilFinished)
                                        - TimeUnit.MINUTES
                                        .toSeconds(TimeUnit.MILLISECONDS
                                                .toMinutes(millisUntilFinished))));
            }

            public void onFinish() {

                ((TextView) findViewById(R.id.resendCode)).setClickable(true);
                ((TextView) findViewById(R.id.resendCode))
                        .setLongClickable(true);
                ((TextView) findViewById(R.id.resendCode)).setFocusable(true);
                ((TextView) findViewById(R.id.resendCode))
                        .setFocusableInTouchMode(true);

                ((TextView) findViewById(R.id.resendCode))
                        .setText(getResources()
                                .getString(R.string.resend_code_));
            }
        }.start();
    }
}
