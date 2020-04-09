package com.ichangemycity.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.swachhbharatengineer.Splashscreen;
import com.prashantsolanki.secureprefmanager.SecurePrefManager;


/**
 * Created by pattabi.raman on 04-08-2017.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

  private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    AppConstant.deviceToken = refreshedToken;
    Log.d(TAG, "Refreshed token: " + refreshedToken);
//        SecurePrefManager.with(Splashscreen.activity).set(ICMyCPreferenceData.deviceToken).value(refreshedToken).go();

  }
}
