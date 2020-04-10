package com.ichangemycity.firebase;

import static android.content.Intent.ACTION_VIEW;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.swachhbharatengineer.Splashscreen;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pattabi.raman on 04-08-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

  private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

  private NotificationUtils notificationUtils;

  public static String url = "";


  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    Log.e(TAG, "From: " + remoteMessage.getFrom());

    if (remoteMessage == null) {
      return;
    }

    // Check if message contains a notification payload.
    // foreground
    if (remoteMessage.getNotification() != null) {
      Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
      String map = remoteMessage.getData().get("body");
      Log.e(TAG, "Notification Body: " + map + "");

      if (map == null) {
        try {
          handleNotification("" + new JSONObject()
              .put("message", remoteMessage.getNotification().getBody())
              .put("title", remoteMessage.getNotification().getTitle()));
        } catch (JSONException e) {
          e.printStackTrace();
        }
      } else {
        handleNotification(map);
      }
    }

//         Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {
      Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

      try {
        JSONObject json = new JSONObject(remoteMessage.getData().get("message").toString());
        handleDataMessage(json);
      } catch (Exception e) {
        Log.e(TAG, "Exception: " + e.getMessage());
      }
    }


  }

  private void handleNotification(String message1) {
    // app is in foreground, broadcast the push message
    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);

    try {
      JSONObject messageJsonObject = new JSONObject(message1);

      String title = messageJsonObject.optString("title");
      String message, subject_id, redirect_to;
      message = messageJsonObject.optString("message");
      pushNotification.putExtra("message", message);
      subject_id = messageJsonObject.optString("subject_id");
      redirect_to = messageJsonObject.optString("redirect_to");
      url = messageJsonObject.optString("url");

      // app is in background, show the notification in notification tray
      Intent resultIntent = new Intent(getApplicationContext(), Splashscreen.class);
      if (redirect_to.equalsIgnoreCase("Detail")) {
        AppController.selectedComplaintData.setComplaintId(subject_id);
        ICMyCPreferenceData.setPreference(this,
            ICMyCPreferenceData.COMPLAINTVIEWID, subject_id);
        ICMyCPreferenceData.setPreference(this,
            ICMyCPreferenceData.isDeeplinked, "1");
        ICMyCPreferenceData.setPreference(this, ICMyCPreferenceData.REDIRECT_TYPE, "Detail");
        ICMyCPreferenceData.setPreference(this, ICMyCPreferenceData.isResolved,
            message.toLowerCase().contains("resolve")
                ? "1" : "0");
        AppController.selectedComplaintData.setComplaintId(subject_id);
        resultIntent = new Intent(this, Splashscreen.class);
      } else if (redirect_to.equalsIgnoreCase("browser")) {
        try {
          resultIntent = (new Intent(ACTION_VIEW, Uri.parse(url)));
        } catch (Exception exception) {
        }
      } else {
        resultIntent = new Intent(this, Splashscreen.class);
      }
      resultIntent.putExtra("message", message);
      resultIntent.putExtra("ComplaintContentId", subject_id);

      showNotificationMessage(getApplicationContext(), title, message, "", resultIntent);

//            }
    } catch (JSONException e) {
      Log.e(TAG, "Json Exception: " + e.getMessage());
    } catch (Exception e) {
      Log.e(TAG, "Exception: " + e.getMessage());
    }
    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

    // play notification sound
    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
    notificationUtils.playNotificationSound();
//        } else {
//            // If the app is in background, firebase itself handles the notification
//        }
  }

  private void handleDataMessage(JSONObject data) {
    Log.e(TAG, "push json: " + data.toString());

    try {

      String title = data.optString("title");
      String message, subject_id, redirect_to, type;
      message = data.optString("message");
      subject_id = data.optString("subject_id");
      redirect_to = data.optString("redirect_to");
      type = data.optString("type");
      url = data.optString("url");

      // app is in background, show the notification in notification tray
      Intent resultIntent = new Intent(getApplicationContext(), Splashscreen.class);
      if (redirect_to.equalsIgnoreCase("Detail")) {
        AppController.selectedComplaintData.setComplaintId(subject_id);
        ICMyCPreferenceData.setPreference(this,
            ICMyCPreferenceData.COMPLAINTVIEWID, subject_id);
        ICMyCPreferenceData.setPreference(this,
            ICMyCPreferenceData.isDeeplinked, "1");
        ICMyCPreferenceData.setPreference(this, ICMyCPreferenceData.REDIRECT_TYPE, "Detail");
        ICMyCPreferenceData.setPreference(this, ICMyCPreferenceData.isResolved,
            message.toLowerCase().contains("resolve")
                ? "1" : "0");
        AppController.selectedComplaintData.setComplaintId(subject_id);
        resultIntent = new Intent(this, Splashscreen.class);
      } else if (redirect_to.equalsIgnoreCase("browser")) { // promotions
        try {
          resultIntent = (new Intent(ACTION_VIEW, Uri.parse(url)));
        } catch (Exception exception) {
        }
      } else {
        resultIntent = new Intent(this, Splashscreen.class);
      }
      resultIntent.putExtra("message", message);

      showNotificationMessage(getApplicationContext(), title, message, "", resultIntent);

    } catch (Exception e) {
      Log.e(TAG, "Json Exception: " + e.getMessage());
    }
  }

  /**
   * Showing notification with text only
   */
  private void showNotificationMessage(Context context, String title, String message,
      String timeStamp, Intent intent) {
    notificationUtils = new NotificationUtils(context);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
  }

}
