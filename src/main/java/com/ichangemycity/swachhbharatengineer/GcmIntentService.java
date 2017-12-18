package com.ichangemycity.swachhbharatengineer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.ICMyCPreferenceData;

public class GcmIntentService extends IntentService {
	public static final String TAG = "GCM ";
	public static int NOTIFICATION_ID = 1;
	public static int numMessages = 0;
	Context context;
	NotificationCompat.Builder builder;
	private NotificationManager mNotificationManager;
	SharedPreferences mSharedPreferences = null;
	private String Message_Recieved = "";
	String ContentId = null, pushImage = null;
	public static boolean isToOpenFeedback = false;
	String title = "";
	public GcmIntentService() {
		super("GcmIntentService");
		// TODO Auto-generated constructor stub
	}

	String redirect = "",url="";

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		mSharedPreferences = GcmIntentService.this.getSharedPreferences(
				ICMyCPreferenceData.preferenceName, 0);
		Bundle extras = intent.getExtras();
		JSONObject jobj = null;
		String msg = intent.getStringExtra("message");
		try {
			jobj = new JSONObject(msg);
			Message_Recieved = jobj.getString("message");
			ContentId = jobj.optString("contentId");
			if (jobj.has("image_url")) {
				pushImage = jobj.getString("image_url");
			} else {
				pushImage = null;
			}
			if(jobj.has("url")){
				url = jobj.optString("url");
			}
			if(jobj.has("title")){
				title = jobj.optString("title");
			}
			redirect = jobj.getString("redirect");

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (jobj != null) {
			System.out.println("Push Notifiication Message Recieved=====>"
					+ Message_Recieved);
			System.out
					.println("Push Notifiication ContentId=====>" + ContentId);
			GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
			String messageType = gcm.getMessageType(intent);

			if (!extras.isEmpty()) {
				if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
						.equals(messageType)) {
					sendNotification("Send error: " + extras.toString(),
							"Failure", pushImage);
				} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
						.equals(messageType)) {
					sendNotification(
							"Deleted messages on server: " + extras.toString(),
							"Deleted", pushImage);
					// If it's a regular GCM message, do some work.
				} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
						.equals(messageType)) {
					// This loop represents the service doing some work.
					for (int i = 0; i < 5; i++) {

						// Log.i(TAG, "Working... " + (i + 1) + "/5 @ "
						// + SystemClock.elapsedRealtime());
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}
					}
					// Log.i(TAG,
					// "Completed work @ " + SystemClock.elapsedRealtime());
					// Post notification of received message.
					// sendNotification("Received: " + extras.toString());

					boolean shared_pref_notifications_ckeckboxstate = mSharedPreferences
							.getBoolean(
									"shared_pref_notifications_ckeckboxstate",
									false);
					System.out
							.println("shared_pref_notifications_ckeckboxstate===>"
									+ shared_pref_notifications_ckeckboxstate);

					if (shared_pref_notifications_ckeckboxstate == false) {
						sendNotification(Message_Recieved, ContentId, pushImage);
					}
					// Log.i(TAG, "Received: " + extras.toString());
				}
			}
			GcmBroadcastReceiver.completeWakefulIntent(intent);
		}
	}

	private void sendNotification(String msg, String ContentId, String pushImage) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		NOTIFICATION_ID++;

		Uri soundUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		getNotificationPendingIntent(msg, ContentId).cancel();

		if (pushImage != null && !pushImage.isEmpty()) {
			mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(NOTIFICATION_ID,
					setBigPictureStyleNotification(msg, ContentId, pushImage));

		} else {
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setContentTitle(
							title)
					.setStyle(
							new NotificationCompat.BigTextStyle().bigText(msg))
					.setContentText(msg)

					.setContentIntent(
							getNotificationPendingIntent(msg, ContentId))

					// .setNumber(numMessages)
					.setSound(soundUri).setAutoCancel(true);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		}

	}

	private Notification setBigPictureStyleNotification(String msg,
														String ContentId, String pushImage) {

		Bitmap remote_picture = null;

		// Create the style object with BigPictureStyle subclass.
		NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
		notiStyle.setBigContentTitle(getResources()
				.getString(R.string.app_name));
		notiStyle.setSummaryText(msg);

		try {
			remote_picture = BitmapFactory.decodeStream((InputStream) new URL(
					pushImage).getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		notiStyle.bigPicture(remote_picture);
		PendingIntent resultPendingIntent = getNotificationPendingIntent(msg,
				ContentId);
		return new NotificationCompat.Builder(this)
				.setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true)
				.setContentIntent(resultPendingIntent)
				.setContentTitle(getResources().getString(R.string.app_name))
				.setContentText(msg).setStyle(notiStyle).build();
	}

	private PendingIntent getNotificationPendingIntent(String msg,
													   String ContentId) {
		// Creates an explicit intent for an Activity in your app
		Intent myintent = new Intent(GcmIntentService.this,
				MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder
				.create(GcmIntentService.this);
		AppController.selectedComplaintData.setComplaintId(ContentId);
		stackBuilder.addParentStack(MainActivity.class);
		if (redirect.equalsIgnoreCase("Detail")) {
			AppController.selectedComplaintData.setComplaintId(ContentId);
			ICMyCPreferenceData.setPreference(GcmIntentService.this,
					ICMyCPreferenceData.isDeeplinked, "1");
			myintent = new Intent(GcmIntentService.this, Splashscreen.class);
			// .putExtra("openFeedback", true);//
			// ComplaintDetailFromPushNotification
			isToOpenFeedback = true;

		}  else if (redirect.equalsIgnoreCase("playstore")) {
			String appPackageName = "com.ichangemycity.swachhbharat";
			try {
				myintent = (new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id="
								+ appPackageName)));
			} catch (android.content.ActivityNotFoundException anfe) {

			}
		}else if(redirect.equalsIgnoreCase("url")){
			try {
				myintent = (new Intent(
						Intent.ACTION_VIEW,
						Uri.parse(url)));
			} catch (Exception e) {

			}
		}

		myintent.putExtra("message", msg);
		myintent.putExtra("ComplaintContentId", ContentId);
		// ComplaintDetailFromPushNotification

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(myintent);

		return stackBuilder.getPendingIntent(new Random().nextInt(),
				PendingIntent.FLAG_UPDATE_CURRENT);

	}

}