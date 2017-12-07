package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.appdata.StrictMode;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.model.LanguageData;
import com.ichangemycity.webservice.URLData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ichangemycity.appdata.AppController.WITHOUT_OTP;

/**
 * Created by pattabi.raman on 23-09-2017.
 */

public class Splashscreen extends BaseAppCompatActivity {
//    List<String> permissionsRequired = new ArrayList<>();
    Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode smo = new StrictMode();
        smo.StrictModeMethod();
        setContentView(R.layout.splash);
        activity = Splashscreen.this;
        proceedAfterPermissionGranted();
    }

    private void proceedAfterPermissionGranted() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
               "http://api.swachh.city/languages", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject mJsonObject) {
                        try {
                           String response = mJsonObject.optString("languages");
                            new GetParsedData(Splashscreen.this, new JSONObject(response))
                                    .execute();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        new RegisterBackground().execute();
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

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);

    }

    GoogleCloudMessaging gcm;
    private static String regid = "";

    public class RegisterBackground extends AsyncTask<String, String, String> {
        // public boolean isFirstTime;
        String msg = "";
        private boolean isFailure = false;

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String msg = "";
            try {
                performGCMRegistration();

            } catch (Exception e) {
                e.printStackTrace();
                isFailure = true;
            }
            return msg;

        }

        private void performGCMRegistration() {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(activity);
                }
                regid = gcm.register(URLData.GCM_SENDER_ID);
                String reg = regid;
                // Log.i("reg", reg);

                ICMyCPreferenceData.setPreference(activity,
                        ICMyCPreferenceData.deviceToken, regid);
                msg = "Device registered, registration ID=" + regid;
                System.out.println("Regid is=============>" + regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                isFailure = true;
            }
        }

        @Override
        protected void onPostExecute(String msg) {

            isFailure = false;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                  "http://api.swachh.city/languages", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            new GetParsedData(activity, response)
                                    .execute();

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(final VolleyError volleyError) {
                    AppController.handleVolleyError(activity, (RelativeLayout) findViewById(R.id.parentLayout), volleyError);
                }

            }) {

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return URLData.getHeaders(activity);

                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }

            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    AppController.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq,
                    TAG);
        }

    }

    public class GetParsedData extends AsyncTask<Void, Void, Void> {

        Activity activity;
        JSONObject response;

        public GetParsedData(Activity activity, JSONObject response) {
            this.activity = activity;
            this.response = response;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            // TODO Auto-generated method stub
            try {
                if (AppController.languageArrayList.size() <= 0) {
                    AppController.languageArrayList.clear();
                    JSONArray jArray = (response.optJSONArray("languages"));
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject mJsonObject = jArray.getJSONObject(i);
                        LanguageData lData = new LanguageData();
                        lData.setLanguage_code(mJsonObject
                                .getString(AppController.language_code));
                        lData.setLanguage_label(mJsonObject
                                .getString(AppController.language_label));
                        AppController.languageArrayList.add(lData);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
//            AppController.hideProgressDialog(activity);
            setConditionToNavigateScreens();

        }
    }

    private void setConditionToNavigateScreens() {
        DoSetMandatoryData mDoSetMandatoryData = new DoSetMandatoryData();
        mDoSetMandatoryData.execute();

    }

    class DoSetMandatoryData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AppController.setMACAddressInPreference(activity);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);


            if (ICMyCPreferenceData.getPreferenceItem(Splashscreen.this,
                    ICMyCPreferenceData.selectedLanguage, "un").equalsIgnoreCase(
                    "un")) {
                startActivity(new Intent(Splashscreen.this,
                        SelectLanguage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            } else if ((ICMyCPreferenceData.getPreferenceItem(Splashscreen.this,
                    ICMyCPreferenceData.Mobile_No, "")) == ""
                    || ICMyCPreferenceData.getPreferenceItem(Splashscreen.this,
                    ICMyCPreferenceData.token, "").equalsIgnoreCase("")) {
                // startActivity(new Intent(SplashScreen.this,
                // UserMobileNumber.class));
                startActivity(new Intent(Splashscreen.this,
                        SelectLanguage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            } /*
		 * else if (Integer.parseInt(ICMyCPreferenceData.getPreferenceItem(
		 * SplashScreen.this, ICMyCPreferenceData.activated, "0")) == 1 &&
		 * ICMyCPreferenceData.getPreferenceItem(SplashScreen.this,
		 * ICMyCPreferenceData.location, "").equalsIgnoreCase("")) {
		 * startActivity(new Intent(SplashScreen.this,
		 * UserSelectLocation.class)); }
		 */else if (Integer.parseInt(ICMyCPreferenceData.getPreferenceItem(
                    Splashscreen.this, ICMyCPreferenceData.activated, "0")) == 0) {
                startActivity(new Intent(Splashscreen.this,
                        UserOTPVerification.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            } else {
                startActivity(new Intent(Splashscreen.this,
                        MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
            Splashscreen.this.finish();

        }
    }

}
