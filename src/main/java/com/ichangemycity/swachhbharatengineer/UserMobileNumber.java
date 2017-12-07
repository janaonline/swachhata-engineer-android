package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.callback.OnButtonClick;
import com.ichangemycity.webservice.URLData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class UserMobileNumber extends BaseAppCompatActivity {
    EditText mobileNumber;
    Button submit;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
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
                if (validateMobileNumber()) {
                     String url = (URLData.BASE_URL
                            + URLData.CHECK_ACTIVE_ENGINEER
                            + mobileNumber.getText().toString()
                            + "&deviceToken="
                            + ICMyCPreferenceData.getPreferenceItem(
                            UserMobileNumber.this,
                            ICMyCPreferenceData.deviceToken, "")
                            + "&lang="
                            + ICMyCPreferenceData.getPreferenceItem(
                            UserMobileNumber.this,
                            ICMyCPreferenceData.selectedLanguage,
                            "en") + "&macAddress=" + ICMyCPreferenceData
                            .getPreferenceItem(UserMobileNumber.this,
                                    ICMyCPreferenceData.deviceUniqueID, ""));
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                            url, null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(final JSONObject mJsonObject) {
                                    try {
                                        if (mJsonObject.getInt("httpCode") == 200
                                                || mJsonObject
                                                .getInt("httpCode") == 201) {
                                            ICMyCPreferenceData
                                                    .setPreference(
                                                            UserMobileNumber.this,
                                                            ICMyCPreferenceData.Mobile_No,
                                                            mobileNumber
                                                                    .getText()
                                                                    .toString());

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
                                                                UserMobileNumber.this,
                                                                ICMyCPreferenceData.id,
                                                                userDataJsonObject
                                                                        .getString(ICMyCPreferenceData.id));
                                                ICMyCPreferenceData
                                                        .setPreference(
                                                                UserMobileNumber.this,
                                                                ICMyCPreferenceData.activated,
                                                                userDataJsonObject
                                                                        .getInt(ICMyCPreferenceData.activated)
                                                                        + "");
                                                ICMyCPreferenceData
                                                        .setPreference(
                                                                UserMobileNumber.this,
                                                                ICMyCPreferenceData.token,
                                                                userDataJsonObject
                                                                        .getString(ICMyCPreferenceData.token)
                                                                        + "");
                                            }
                                            navigateToOTP();

                                        } else {
                                            Toast.makeText(
                                                    UserMobileNumber.this,
                                                    mJsonObject
                                                            .optString("message"),
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                            try {


                                                if (Integer.parseInt(mJsonObject
                                                        .optString("httpCode")) == 404) {
                                                    ICMyCPreferenceData
                                                            .setPreference(
                                                                    activity,
                                                                    ICMyCPreferenceData.Mobile_No,
                                                                    mobileNumber
                                                                            .getText()
                                                                            .toString());
                                                    AppController.showAlert(
                                                            activity,
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
                                                } else if (Integer
                                                        .parseInt(mJsonObject.get(
                                                                "httpCode")
                                                                .toString()) == 405) {
//                                                    startActivity(new Intent(
//                                                            UserMobileNumber.this,
//                                                            MobileNumberNotRegistered.class));
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AppController.handleVolleyError(activity, (RelativeLayout) findViewById(R.id.parentLayout), error);
                        }

                    }) {

                        /**
                         * Passing some request headers
                         * */
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return URLData.getHeaders(activity);
                        }

                        @Override
                        protected Map<String, String> getParams() {
                            return null;
                        }

                    };
                    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                            AppController.MY_SOCKET_TIMEOUT_MS,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    // Adding request to request queue
                    AppController.getInstance().addToRequestQueue(jsonObjReq,
                            AppController.TAG);


                }
            }

        });
    }

    private boolean validateMobileNumber() {

        String mobnobegin = "";
        try {
            mobnobegin = mobileNumber.getText().toString().substring(0, 1);
        } catch (Exception e) {
            e.printStackTrace();
            mobnobegin = "";
        }
        boolean isValid = true;
        if (mobnobegin == null || mobnobegin.length() <= 0) {
            isValid = false;
            AppController.showAlert(
                    UserMobileNumber.this,
                    "",
                    getResources().getString(
                            R.string.mobile_number_cannot_be_empty), false, new OnButtonClick() {
                        @Override
                        public void onPositiveButtonClicked(DialogInterface dialogInterface) {

                        }

                        @Override
                        public void onNegativeButtonClicked(DialogInterface dialogInterface) {

                        }
                    });
            // alertdialog_with_one_button
            // .showErrorAlert("Mobile Number cannot be empty");
            // et_emailid.setError(null);
            // et_phone_number.setError(null);
            // mMobileNumber.setError("Password cannot be empty");
            mobileNumber.setFocusable(true);
            mobileNumber.requestFocus();
        } else if (mobileNumber.getText().toString().length() > 0
                && mobileNumber.getText().toString().length() < 10) {
            isValid = false;
            AppController.showAlert(
                    UserMobileNumber.this,
                    "",
                    getResources().getString(
                            R.string.mobile_number_needs_10_digits), false, new OnButtonClick() {
                        @Override
                        public void onPositiveButtonClicked(DialogInterface dialogInterface) {

                        }

                        @Override
                        public void onNegativeButtonClicked(DialogInterface dialogInterface) {

                        }
                    });
            // alertdialog_with_one_button
            // .showErrorAlert("Mobile Number cannot be empty");
            // et_emailid.setError(null);
            // et_phone_number.setError(null);
            // mMobileNumber.setError("Password cannot be empty");
            mobileNumber.setFocusable(true);
            mobileNumber.requestFocus();
        } else if (!(mobnobegin.equalsIgnoreCase("7"))
                && !(mobnobegin.equalsIgnoreCase("8"))
                && !(mobnobegin.equalsIgnoreCase("9"))) {
            isValid = false;
            AppController
                    .showAlert(
                            UserMobileNumber.this,
                            "",
                            getResources()
                                    .getString(
                                            R.string.mobile_number_must_begin_with_7_or_8_or_9), false, new OnButtonClick() {
                                @Override
                                public void onPositiveButtonClicked(DialogInterface dialogInterface) {

                                }

                                @Override
                                public void onNegativeButtonClicked(DialogInterface dialogInterface) {

                                }
                            });
            // .showErrorAlert("Mobile number must begin with 7 or 8 or 9");
            mobileNumber.setFocusable(true);
            mobileNumber.requestFocus();
        } else {
            isValid = true;
        }
        return isValid;
    }

    private void navigateToOTP() {
        if (Integer.parseInt(ICMyCPreferenceData.getPreferenceItem(
                UserMobileNumber.this, ICMyCPreferenceData.activated, "0")) == 0
                || ICMyCPreferenceData.getPreferenceItem(UserMobileNumber.this,
                ICMyCPreferenceData.token, "").equalsIgnoreCase("")) {
            /* activate =0 or no token stored in preference */
            startActivity(new Intent(UserMobileNumber.this,
                    UserOTPVerification.class));
        } else if (Integer.parseInt(ICMyCPreferenceData.getPreferenceItem(
                UserMobileNumber.this, ICMyCPreferenceData.activated, "0")) == 1) {

            startActivity(new Intent(UserMobileNumber.this,
                    MainActivity.class));
        }
        UserMobileNumber.this.finish();
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mobileNumber != null)
            mobileNumber.setText("");
    }
}
