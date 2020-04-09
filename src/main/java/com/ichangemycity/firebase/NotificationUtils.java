package com.ichangemycity.firebase;

/**
 * Created by pattabi.raman on 04-08-2017.
 */

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import com.ichangemycity.swachhbharatengineer.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * Created by Ravi on 31/03/15.
 */
public class NotificationUtils {

  private static String TAG = NotificationUtils.class.getSimpleName();

  private Context mContext;

  public NotificationUtils(Context mContext) {
    this.mContext = mContext;
  }

  public void showNotificationMessage(String title, String message, String timeStamp,
      Intent intent) {
    showNotificationMessage(title, message, timeStamp, intent, null);
  }

  public void showNotificationMessage(final String title, final String message,
      final String timeStamp, Intent intent, String imageUrl) {
    {
      // Check for empty push message
      if (TextUtils.isEmpty(message)) {
        return;
      }

      // notification icon
      final int icon = R.mipmap.ic_launcher_round;

      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      final PendingIntent resultPendingIntent =
          PendingIntent.getActivity(
              mContext,
              new Random().nextInt(),
              intent,
              PendingIntent.FLAG_CANCEL_CURRENT
          );

      final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
          mContext);

      final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
          + "://" + mContext.getPackageName() + "/raw/notification.mp3");

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
        showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent,
            alarmSound);
      } else {
        showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent,
            alarmSound);

      }
      playNotificationSound();
    }
  }


  private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title,
      String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

    final String Channel_id = "SBM-Engineer";
    CharSequence name = "SBM Engineer App";// The user-visible name of the channel.
    int importance = 0;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      importance = NotificationManager.IMPORTANCE_HIGH;
    }
    NotificationChannel mChannel = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      mChannel = new NotificationChannel(Channel_id, name, importance);
    }

    Notification notification;
    notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher_round).setTicker(title)
        .setAutoCancel(false)
        .setContentTitle(title)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
        .setContentIntent(resultPendingIntent)
//        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setChannelId(Channel_id)
        .setWhen(System.currentTimeMillis())
        .setContentText(message)
        .build();

    NotificationManager notificationManager = (NotificationManager) mContext
        .getSystemService(Context.NOTIFICATION_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mChannel != null) {
      notificationManager.createNotificationChannel(mChannel);
    }
    notificationManager.notify(Config.NOTIFICATION_ID, notification);
  }


  // Playing notification sound
  public void playNotificationSound() {
    try {
      Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
          + "://" + mContext.getPackageName() + "/raw/notification");
      Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
      r.play();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}