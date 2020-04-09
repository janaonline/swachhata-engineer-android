package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.andexert.library.RippleView;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.callback.OnResponseListener;
import com.ichangemycity.callback.OnTaskCompleted;
import com.ichangemycity.webservice.URLDataSwachhManch;
import com.ichangemycity.webservice.WebserviceHelper;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ichangemycity.webservice.URLDataSwachhManch.BASE_URL_PROFILE;
import static com.ichangemycity.webservice.URLDataSwachhManch.CHANNEL_KEY_VALUE;

public class SetEmailPasswordActivity extends BaseAppCompatActivity {
    public static Activity activity;
    @Nullable
    @BindView(R.id.email)
    EditText email;
    @Nullable
    @BindView(R.id.password)
    EditText password;
    @Nullable
    @BindView(R.id.confirmPassword)
    EditText confirmPassword;
    @Nullable
    @BindView(R.id.rippleView)
    RippleView rippleView;
    //call 1.4 if type=setEmailPassword and show three fields in SetEmailPasswordActivity.class
    //call 1.2 if type=login and show three fields in SetEmailPasswordActivity.class
    String type = "";
    @Nullable
    @BindView(R.id.confirmPasswordTIL)
    TextInputLayout confirmPasswordTIL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_email_password);
        ButterKnife.bind(this);
        activity = SetEmailPasswordActivity.this;
        type = getIntent().getExtras().getString("type");
        if (type.equalsIgnoreCase(AppConstant.ONBOARDING_TYPE_SET_EMAIL_PASSWORD)) {
            confirmPasswordTIL.setVisibility(View.VISIBLE);
            setToolbarAndCustomizeTitle(getString(R.string.set_password));
        } else {
            email.setVisibility(View.GONE);
            setToolbarAndCustomizeTitle(getString(R.string.login_with_password));
            confirmPasswordTIL.setVisibility(View.GONE);
        }
        rippleView.setOnRippleCompleteListener(rippleView -> {
            if (type.equalsIgnoreCase(AppConstant.ONBOARDING_TYPE_SET_EMAIL_PASSWORD)) {
                //call 1.4 if type=setEmailPassword and show three fields in SetEmailPasswordActivity.class
                setEmailPasswordAPICall();
            } else if (type.equalsIgnoreCase(AppConstant.ONBOARDING_TYPE_LOGIN)) {
                //call 1.2 if type=login and show three fields in SetEmailPasswordActivity.class
                loginAPICall();

            }
        });

    }

    private void loginAPICall() {
        submitMobileNumberWithOTPSourceFaceBook();
    }

    // bug - otp is receiving if mobile_number_verified is true
    private void submitMobileNumberWithOTPSourceFaceBook() {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile_number", ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.Mobile_No, ""));
        params.put("device_token", ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.deviceToken, ""));
        params.put("password", password.getText().toString());
        params.put("mac_address", ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.deviceUniqueID, ""));
        params.putAll(URLDataSwachhManch.getChannelParam());

        new WebserviceHelper(activity, WebserviceHelper.METHOD_POST, URLDataSwachhManch.BASE_URL_AUTH, params, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {
                AppUtils.hideProgressDialog(activity);
            }

            @Override
            public void OnResponseSuccess(JSONObject response) {
                getProfileAPICall();
            }
        }, true, WebserviceHelper.HEADER_TYPE_AUTH);

    }

    // update email id and password with confirm password
    private void setEmailPasswordAPICall() {
        final String URLString = URLDataSwachhManch.BASE_URL_AUTH + URLDataSwachhManch.EMAIL;
        final String PARAMS = "?email=" + email.getText().toString() + "&password=" + password.getText().toString()
                + "&password_confirmation=" + confirmPassword.getText().toString();
        HashMap<String, String> request = new HashMap<>();
        request.put("email", email.getText().toString());
        request.put("password", password.getText().toString());
        request.put("password_confirmation", confirmPassword.getText().toString());

        new WebserviceHelper(activity, WebserviceHelper.METHOD_PATCH, URLString + PARAMS, null, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {
                AppUtils.hideProgressDialog(activity);
            }

            @Override
            public void OnResponseSuccess(JSONObject response) {
//                if (response.has("access_token")) {
//                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.TOKEN_TYPE, response.optString("token_type"));
//                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.token, response.optString("access_token"));
//                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.refresh_token, response.optString("refresh_token"));
//                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.activated, "1");
//                        getProfileAPICall
                getProfileAPICall();
//                }

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
                                startActivity(new Intent(activity, MainActivity.class));
                            }

                            @Override
                            public void onTaskFailure(VolleyError error) {

                            }
                        });

                    }
                }, true, WebserviceHelper.HEADER_TYPE_PROFILE);
    }

    @Nullable
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    private void setToolbarAndCustomizeTitle(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> activity.finish());
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
    }
}
