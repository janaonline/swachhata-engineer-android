package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.callback.OnResponseListener;
import com.ichangemycity.model.LanguageData;
import com.ichangemycity.webservice.URLData;
import com.ichangemycity.webservice.WebserviceHelper;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pattabi.raman on 23-09-2017.
 */

public class Splashscreen extends BaseAppCompatActivity {

  //    List<String> permissionsRequired = new ArrayList<>();
  public static Activity activity;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash);
    activity = Splashscreen.this;
    proceedAfterPermissionGranted();
  }

  private void proceedAfterPermissionGranted() {
    final String url = URLData.GET_LANGUAGES;
    new WebserviceHelper(activity, WebserviceHelper.METHOD_GET, url, null,
        new OnResponseListener() {
          @Override
          public void OnResponseFailure(JSONObject response) {

          }

          @Override
          public void OnResponseSuccess(JSONObject response1) {
            try {
              JSONArray response = response1.optJSONArray("languages");
              new GetParsedData(Splashscreen.this, response)
                  .execute();
            } catch (Exception e) {
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
      final String url = URLData.GET_LANGUAGES;
      new WebserviceHelper(activity, WebserviceHelper.METHOD_GET, url, null,
          new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {

            }

            @Override
            public void OnResponseSuccess(JSONObject response) {
              new GetParsedData(activity, response.optJSONArray("languages")).execute();
            }
          }, false, WebserviceHelper.HEADER_TYPE_NORMAL);
    }

  }

  public class GetParsedData extends AsyncTask<Void, Void, Void> {

    Activity activity;
    JSONArray response;

    public GetParsedData(Activity activity, JSONArray response) {
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

          for (int i = 0; i < this.response.length(); i++) {
            JSONObject mJsonObject = this.response.optJSONObject(i);
            LanguageData lData = new LanguageData();
            lData.setLanguage_code(mJsonObject
                .optString(AppController.language_code));
            lData.setLanguage_label(mJsonObject
                .optString(AppController.language_label));
            if (!AppController.languageArrayList.contains(lData)) {
              AppController.languageArrayList.add(lData);
            }
          }
        }
      } catch (Exception e) {
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

  @Override
  protected void onNewIntent(Intent intent) {
    String action = intent.getAction();
    String data = intent.getDataString();
    if (data != null) {
      ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.isDeeplinked, "1");
      if (Intent.ACTION_VIEW.equals(action) && data.contains("/complaints/")) {
//              Complaint deeplink
        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.isDeeplinked, "1");
        AppController.selectedComplaintData.setComplaintId(data
            .substring(data.lastIndexOf("/") + 1));
      }
    } else {
      // do nothing
    }

  }
}
