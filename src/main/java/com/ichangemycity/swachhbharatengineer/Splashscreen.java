package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.callback.OnResponseListener;
import com.ichangemycity.model.LanguageData;
import com.ichangemycity.webservice.URLData;
import com.ichangemycity.webservice.WebserviceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


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
        setContentView(R.layout.splash);
        activity = Splashscreen.this;
        proceedAfterPermissionGranted();
    }

    private void proceedAfterPermissionGranted() {
        final String url = "http://api.swachh.city/languages";
        new WebserviceHelper(activity, WebserviceHelper.METHOD_GET, url, null, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {

            }

            @Override
            public void OnResponseSuccess(JSONObject response1) {
                try {
                    String response = response1.optString("languages");
                    new GetParsedData(Splashscreen.this, new JSONObject(response))
                            .execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new RegisterBackground().execute();
            }
        }, true, WebserviceHelper.HEADER_TYPE_NORMAL);


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
            final String url = "http://api.swachh.city/languages";
            new WebserviceHelper(activity, WebserviceHelper.METHOD_GET, url, null, new OnResponseListener() {
                @Override
                public void OnResponseFailure(JSONObject response) {

                }

                @Override
                public void OnResponseSuccess(JSONObject response) {
                    new GetParsedData(activity, response).execute();
                }
            }, false, WebserviceHelper.HEADER_TYPE_NORMAL);
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
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
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
            super.onPostExecute(result);
            if (Integer.parseInt(ICMyCPreferenceData.getPreferenceItem(
                    Splashscreen.this, ICMyCPreferenceData.activated, "0")) == 0) {
                startActivity(new Intent(Splashscreen.this,
                        SelectLanguage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            } else {
                startActivity(new Intent(Splashscreen.this,
                        MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
            Splashscreen.this.finish();

        }
    }

}
