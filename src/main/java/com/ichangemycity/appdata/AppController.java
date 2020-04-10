package com.ichangemycity.appdata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.ichangemycity.adapter.ChangeStatusSpinnerAdapter;
import com.ichangemycity.callback.OnButtonClick;
import com.ichangemycity.model.ChangeStatusListData;
import com.ichangemycity.model.ChangeStatusModel;
import com.ichangemycity.model.CommentsData;
import com.ichangemycity.model.ComplaintData;
import com.ichangemycity.model.LanguageData;
import com.ichangemycity.model.SelectedImageModel;
import com.ichangemycity.model.VotedUpData;
import com.ichangemycity.swachhbharatengineer.OTPVerification;
import com.ichangemycity.swachhbharatengineer.R;
import com.ichangemycity.swachhbharatengineer.Splashscreen;
import com.ichangemycity.swachhbharatengineer.UserMobileNumber;
import com.ichangemycity.webservice.LruBitmapCache;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.prashantsolanki.secureprefmanager.SecurePrefManagerInit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.ichangemycity.appdata.AppConstant.TOAST_TYPE_ERROR;

/**
 * Created by pattabi.raman on 23-09-2017.
 */

public class AppController extends MultiDexApplication {
    public static ArrayList<LanguageData> languageArrayList = new ArrayList<LanguageData>();
    public static String language_code = "code";
    public static String language_label = "label";
    public static double latitude = 0.0;
    public static double longitude = 0.0;

    public static ComplaintData selectedComplaintData = new ComplaintData();
    public static ArrayList<CommentsData> commentData = new ArrayList<>();
    public static ArrayList<VotedUpData> votedUpData = new ArrayList<>();
    public static ChangeStatusModel selectedComplaintChangeStatusOptions = new ChangeStatusModel();
    public static final int PURPOSE_POST_COMMENT = 1;
    public static final int PURPOSE_CHANGE_STATUS = 2;
    public static int selectedPurposeToUploadImage;

    private Locale locale = null;


    public static void traceLog(String key, String value) {
//        Log.i(key, value);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig,
                    getBaseContext().getResources().getDisplayMetrics());
        }
    }


    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static AppController mInstance;
    public static int MY_SOCKET_TIMEOUT_MS = 864000 * 2;
    //    public static ArrayList<String> images = new ArrayList<>();
    public static SelectedImageModel mSelectedImageModels = new SelectedImageModel();
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        try {
            new SecurePrefManagerInit.Initializer(mInstance.getApplicationContext())
                    .useEncryption(true)
                    .initialize();
            MultiDex.install(this);
            AnalyticsTracker.initialize(this);
            AnalyticsTracker.getInstance().get(AnalyticsTracker.Target.APP);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized AppController getInstance() {

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setRetryPolicy(new DefaultRetryPolicy(
                AppController.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static void handleVolleyError(Activity act,
        VolleyError volleyError) {
        try {
            if (act.getClass().getSimpleName().equalsIgnoreCase(OTPVerification.class.getSimpleName())
                    || act.getClass().getSimpleName().equalsIgnoreCase(UserMobileNumber.class.getSimpleName())) {
                //Swachh Manch api error handling
                AppUtils.handleVolleyError(act, volleyError);
            } else {
//            SBM Engineer api error handling
                VolleyLog.d(AppController.TAG, "Error: " + volleyError.getMessage());
                int statusCode = volleyError.networkResponse.statusCode;
                if (statusCode == 500 || statusCode == 504) {
                    AppUtils.showToast(act, TOAST_TYPE_ERROR, "Server Error / Too many Connections at a time. Please try again after sometime.");
                } else {
                    String message = "";
                    boolean isToLogOut = false;
                    int type = AppConstant.TOAST_TYPE_INFO;
                    try {
                        JSONObject responseObject = new JSONObject(new String(volleyError.networkResponse.data));
                        JSONArray mData = null;
                        if (responseObject.has("data")) {
                            try {
                                mData = responseObject.getJSONArray("data");
                            } catch (JSONException e1) {
                                // TODO Auto-generated catch
                                // block
                                e1.printStackTrace();
                            }
                            for (int i = 0; i < mData.length(); i++) {
                                try {
                                    message += mData.getJSONObject(i).optString("message") + " ";
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch
                                    // block
                                    e.printStackTrace();
                                }
                            }
                        } else if (responseObject.has("errors")) {
                            try {
                                mData = responseObject.getJSONArray("errors");
                            } catch (JSONException e1) {
                                // TODO Auto-generated catch
                                // block
                                e1.printStackTrace();
                            }
                            for (int i = 0; i < mData.length(); i++) {
                                try {
                                    message += mData.getJSONObject(i).optString("message") + " ";
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch
                                    // block
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            message = new JSONObject(new String(volleyError.networkResponse.data)).optString("message");
                        }
                        if (new JSONObject(new String(volleyError.networkResponse.data)).optInt("httpCode") == 401) {
                            isToLogOut = true;
                        }
                        AppController.traceLog("vollyErrorTrace", responseObject + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        message = "Error : " + e.getMessage();
                        type = TOAST_TYPE_ERROR;
                    } catch (NullPointerException ex) {
                        if (volleyError instanceof NetworkError) {
                            message = volleyError.getLocalizedMessage();//act.getString(R.string.network_error);
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            type = TOAST_TYPE_ERROR;
                        } else if (volleyError instanceof AuthFailureError) {
                            message = volleyError.getLocalizedMessage();
//                message = "Cannot connect to Internet...Please check your connection!";
                            type = TOAST_TYPE_ERROR;
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                            type = TOAST_TYPE_ERROR;
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            type = AppConstant.TOAST_TYPE_INFO;
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut";
                            type = AppConstant.TOAST_TYPE_INFO;
                        }
                    }

                    if (message != null) {
                        try {
                            if (message.trim().length() <= 0) {
                                message = volleyError.getMessage();
                                type = TOAST_TYPE_ERROR;
                            }
                            AppUtils.showToast(act, type, message);
//                Snackbar.make(layout, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                        } catch (Exception e) {
                            AppController.traceLog("ERROR_VOLLEYERROR", message);
//                Toast.makeText(act, message, Toast.LENGTH_SHORT).show();
                            type = TOAST_TYPE_ERROR;
                            AppUtils.showToast(act, type, message);
                        }
                        if (isToLogOut && !ICMyCPreferenceData.getPreferenceItem(act, ICMyCPreferenceData.token, "NA").equalsIgnoreCase("NA")) {
                            act.startActivity(new Intent(act, Splashscreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            ICMyCPreferenceData.clearPreferences(act);
                            act.finish();
                        }
                    }
                }
            }
        }catch (Exception e){}
    }

    public static View view;

    public static void showProgressDialog(final Activity activity) {
        view = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_loading, null);
        activity.addContentView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup
                        .LayoutParams.MATCH_PARENT));

    }

    public static void hideProgressDialog(final Activity activity) {
        try {
            (activity.findViewById(R.id.progress)).setVisibility(View.GONE);
        } catch (Exception e) {
        }
        try {
            ViewGroup rootView = activity.findViewById(android.R.id.content);
            for (int i = 0; i < rootView.getChildCount(); i++) {
                if (rootView.getChildAt(i) == view) {
                    rootView.removeView(view);
                }
            }
        } catch (Exception e) {
        }
        try {
            (activity.findViewById(R.id.progress)).setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }

    public static String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    // GA
    // /////////////////////////////GA////////////////////////



    public synchronized Tracker getGoogleAnalyticsTracker() {
        /*
         * AnalyticsTracker analyticsTrackers = AnalyticsTracker.getInstance(); return
         * analyticsTrackers.get(AnalyticsTracker.Target.APP);
         */
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.getInstance(this).setDryRun(true);
            analytics.getInstance(this).getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            analytics.setLocalDispatchPeriod(2);
            analytics.setDryRun(false);
            analytics.setAppOptOut(false);
//            GoogleAnalytics.getInstance(this).newTracker(getString(R.string.tracking_id));
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = GoogleAnalytics.getInstance(this).newTracker(getString(R.string.tracking_id));
            analytics.newTracker(R.xml.app_tracker);

        }
        return mTracker;
    }

    public static void assignLanguage(final Activity activity) {
        try {
            Locale locale = new Locale(ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData
                    .selectedLanguage, "en"));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            activity.getBaseContext()
                    .getResources()
                    .updateConfiguration(
                            config,
                            activity.getBaseContext().getResources()
                                    .getDisplayMetrics());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMACAddressInPreference(Activity act) {
        String address = "";
        TelephonyManager tm = (TelephonyManager) act
                .getSystemService(Context.TELEPHONY_SERVICE);
        address = Settings.Secure.getString(act.getContentResolver(), Settings.Secure.ANDROID_ID);
        ICMyCPreferenceData.setPreference(act, ICMyCPreferenceData.deviceUniqueID,
                address);
    }


    public static void showAlert(final Activity activity, final String title, final String message, final boolean isToShowNegativeButton, final
    OnButtonClick onButtonClick) {
        AlertDialog.Builder ab = new AlertDialog.Builder(activity);
        ab.setTitle(title);
        ab.setMessage(message);
        ab.setPositiveButton("Ok",
            (dialogInterface, i) -> onButtonClick.onPositiveButtonClicked(dialogInterface));
        if (isToShowNegativeButton)
            ab.setNegativeButton("Cancel",
                (dialogInterface, i) -> onButtonClick.onNegativeButtonClicked());
        ab.show();
    }


    public static void initiateCTAForShareComment() {
//        AppController.selectedComplaintData = complaintData;
//        ((TextView) activity.findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ParseComplaintData.shareComplaint(activity, complaintData);
//
//            }
//        });
//        ((TextView) activity.findViewById(R.id.comment)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activity.startActivity(new Intent(activity, CommentsActivity.class));
//            }
//        });
    }

    public static void setEmptyViewForRecyclerView(final Activity activity,
                                                   final EasyRecyclerView recyclerView) {

        if (recyclerView.getAdapter() != null) {
            if (recyclerView.getAdapter().getItemCount() <= 0) {
                try {
                    (activity.findViewById(R.id.viewEmpty)).setVisibility(View.VISIBLE);
                    ((TextView) activity.findViewById(R.id.viewEmpty)).setText(activity.getResources().getString(R.string.no_data));
                } catch (Exception e) {
                }
            } else {
                try {
                    (activity.findViewById(R.id.viewEmpty)).setVisibility(View.GONE);
                } catch (Exception e) {
                }
                try {
                    recyclerView.setEmptyView(null);
                } catch (Exception e) {

                }
            }
        }
    }

    public static void setEmptyViewForRecyclerViewFragments(final Activity activity, final EasyRecyclerView recyclerView, final TextView textview) {
        try {
            if (recyclerView.getAdapter() != null) {
                if (recyclerView.getAdapter().getItemCount() <= 0) {
                    (textview).setVisibility(View.VISIBLE);
                    textview.setText(activity.getResources().getString(R.string.no_data));
                } else {
                    textview.setText(activity.getResources().getString(R.string.loading));
                    textview.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
        }
    }

    public static final int COMPLAINT_OPEN = 1;
    public static final int COMPLAINT_ON_THE_JOB = 3;
    public static final int COMPLAINT_RESOLVED = 4;
    public static final int COMPLAINT_REOPEN = 5;
    public static final int COMPLAINT_REJECTED = 6;

    // Complaint cards
    public static void customizeChangeStatusDropdown(final Activity activity,
                                                     final ComplaintData cData, final LinearLayout resolved,
                                                     final Spinner changeStatusSpinner, final TextView neutral,
                                                     final TextView satisfaction, final TextView un_satisfied,
                                                     final FrameLayout frameSpinner) {
        ArrayList<ChangeStatusModel> changeStatusModel = new ArrayList<ChangeStatusModel>();
        ChangeStatusModel changeStatus = new ChangeStatusModel();

        if (Integer.parseInt(cData.getComplaint_status_id()) == COMPLAINT_OPEN
                || Integer.parseInt(cData.getComplaint_status_id()) == COMPLAINT_REOPEN) {
            changeStatus = new ChangeStatusModel();
            changeStatus.setStatusID(COMPLAINT_ON_THE_JOB);
            changeStatus.setStatusName(activity.getString(R.string.on_the_job)
                    .toUpperCase());
            changeStatus.setColor(Color.parseColor("#2bb5f9"));
            changeStatus.setCurrentStatusColor(activity.getResources()
                    .getColor(R.color.red_reopn_open));
            changeStatusModel.add(changeStatus);
            changeStatus = new ChangeStatusModel();
            changeStatus.setStatusID(COMPLAINT_REJECTED);
            changeStatus.setStatusName(activity.getString(R.string.rejected)
                    .toString().toUpperCase());
            changeStatus.setCurrentStatusColor(activity.getResources()
                    .getColor(R.color.red_reopn_open));
            changeStatus.setColor(Color.parseColor("#607d8b"));
            changeStatusModel.add(changeStatus);
            changeStatusSpinner.setVisibility(View.VISIBLE);
            frameSpinner.setVisibility(View.VISIBLE);
            resolved.setVisibility(View.GONE);
        } else if (Integer.parseInt(cData.getComplaint_status_id()) == COMPLAINT_ON_THE_JOB) {
            changeStatus = new ChangeStatusModel();
            changeStatus.setStatusID(COMPLAINT_RESOLVED);
            changeStatus.setCurrentStatusColor(activity.getResources()
                    .getColor(R.color.blue_on_the_job));
            changeStatus.setStatusName(activity.getString(R.string.resolved)
                    .toUpperCase());
            changeStatus.setColor(Color.parseColor("#00bd00"));
            changeStatusModel.add(changeStatus);

            changeStatus = new ChangeStatusModel();
            changeStatus.setStatusID(COMPLAINT_REJECTED);
            changeStatus.setCurrentStatusColor(activity.getResources()
                    .getColor(R.color.blue_on_the_job));
            changeStatus.setStatusName(activity.getString(R.string.rejected)
                    .toUpperCase());
            changeStatus.setColor(Color.parseColor("#607d8b"));
            changeStatusModel.add(changeStatus);
            changeStatusSpinner.setVisibility(View.VISIBLE);
            frameSpinner.setVisibility(View.VISIBLE);
            resolved.setVisibility(View.GONE);
        } else if (Integer.parseInt(cData.getComplaint_status_id()) == COMPLAINT_REJECTED) {
            changeStatusSpinner.setVisibility(View.INVISIBLE);
            frameSpinner.setVisibility(View.INVISIBLE);
            resolved.setVisibility(View.GONE);
        } else if (Integer.parseInt(cData.getComplaint_status_id()) == COMPLAINT_RESOLVED) {
            changeStatusSpinner.setVisibility(View.INVISIBLE);
            frameSpinner.setVisibility(View.INVISIBLE);
            resolved.setVisibility(View.VISIBLE);
        }

        if (Integer.parseInt(cData.getComplaint_status_id()) == COMPLAINT_RESOLVED)
            setSmileCounts(cData, neutral, satisfaction, un_satisfied, true,
                    resolved);

        else
            setSmileCounts(cData, neutral, satisfaction, un_satisfied, true,
                    resolved);

        if (changeStatusSpinner.getVisibility() == View.VISIBLE) {

            ChangeStatusSpinnerAdapter mAdapter = new ChangeStatusSpinnerAdapter(
                    activity, cData, changeStatusModel);
            changeStatusSpinner.setAdapter(mAdapter);
        }
    }

    public static void setSmileCounts(final ComplaintData cData,
                                      final TextView neutral, final TextView satisfaction,
                                      final TextView un_satisfied, final boolean isToClick,
                                      final LinearLayout cta_feedback) {
        if (isToClick) {
            neutral.setClickable(true);
            satisfaction.setClickable(true);
            un_satisfied.setClickable(true);
            cta_feedback.setClickable(true);

        } else {
            neutral.setClickable(false);
            satisfaction.setClickable(false);
            un_satisfied.setClickable(false);
            cta_feedback.setClickable(false);
        }


        if (cData.isFeedback_count()) {
            neutral.setText(cData.getNeutral());
            satisfaction.setText(cData.getSatisfaction());
            un_satisfied.setText(cData.getUn_satisfied());
        } else {

        }
    }

    // Mark as resolved or mark as reopen list
    public static ArrayList<ChangeStatusListData> customizeListData(
            Activity activity, ArrayList<ChangeStatusListData> cStatusListData) {
        cStatusListData = new ArrayList<ChangeStatusListData>();
        cStatusListData.clear();
        ChangeStatusListData temp = new ChangeStatusListData();
        int complaintStatusId = Integer
                .parseInt(AppController.selectedComplaintData
                        .getComplaint_status_id());
        if (complaintStatusId == COMPLAINT_OPEN
                || complaintStatusId == COMPLAINT_REOPEN) {
            temp = new ChangeStatusListData();
            temp.setStatus(activity.getResources().getString(
                    R.string.on_the_job));
            temp.setStatusID(COMPLAINT_ON_THE_JOB);
            cStatusListData.add(temp);

            temp = new ChangeStatusListData();
            temp.setStatus(activity.getResources().getString(R.string.rejected));
            temp.setStatusID(COMPLAINT_REJECTED);
            cStatusListData.add(temp);

        } else if (complaintStatusId == COMPLAINT_ON_THE_JOB) {
            temp = new ChangeStatusListData();
            temp.setStatus(activity.getResources().getString(R.string.resolved));
            temp.setStatusID(COMPLAINT_RESOLVED);
            cStatusListData.add(temp);

            temp = new ChangeStatusListData();
            temp.setStatus(activity.getResources().getString(R.string.rejected));
            temp.setStatusID(COMPLAINT_REJECTED);
            cStatusListData.add(temp);
        } else if (complaintStatusId == COMPLAINT_REJECTED
                || complaintStatusId == COMPLAINT_RESOLVED) {
            cStatusListData.clear();
        } else {
            cStatusListData.clear();
        }
        return cStatusListData;
    }
//    public static void logTrace(Activity activity, String value) {
//        Log.i(activity.getClass().getSimpleName().toString(), value);
//    }

}

