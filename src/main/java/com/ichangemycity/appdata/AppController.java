package com.ichangemycity.appdata;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.ichangemycity.adapter.ChangeStatusSpinnerAdapter;
import com.ichangemycity.callback.OnButtonClick;
import com.ichangemycity.model.ChangeStatusListData;
import com.ichangemycity.model.ChangeStatusModel;
import com.ichangemycity.model.CommentsData;
import com.ichangemycity.model.ComplaintCategoryData;
import com.ichangemycity.model.ComplaintData;
import com.ichangemycity.model.FeedbackData;
import com.ichangemycity.model.LanguageData;
import com.ichangemycity.model.SelectedImageModel;
import com.ichangemycity.model.VotedUpData;
import com.ichangemycity.swachhbharatengineer.OTPVerification;
import com.ichangemycity.swachhbharatengineer.R;
import com.ichangemycity.swachhbharatengineer.Splashscreen;
import com.ichangemycity.swachhbharatengineer.UserMobileNumber;
import com.ichangemycity.webservice.GPSTracker;
import com.ichangemycity.webservice.LruBitmapCache;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.prashantsolanki.secureprefmanager.SecurePrefManager;
import com.prashantsolanki.secureprefmanager.SecurePrefManagerInit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

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
    public static boolean isAnyLocationSuggestionClicked;
    public static String location = "";

    public static final int FROM_NEARBY_TAB = 100;
    public static final int FROM_CITY_TAB = 101;
    public static final int FROM_YOURS_TAB = 102;
    public static final int FROM_POSTED_COMPLAINTS = 103;
    public static final int FROM_VOTED_COMPLAINTS = 104;
    public static final int FROM_HOME_TAB = 99;
    public static final int FROM_SEARCH_COMPLAINTS = 105;

    public static int complaintDetailFrom = 0;
    public static ComplaintCategoryData selectedComplaintCategoryData = new ComplaintCategoryData();
    public static ComplaintData selectedComplaintData = new ComplaintData();
    public static ArrayList<CommentsData> commentData = new ArrayList<>();
    public static ArrayList<VotedUpData> votedUpData = new ArrayList<>();
    public static ChangeStatusModel selectedComplaintChangeStatusOptions = new ChangeStatusModel();
    public static final int PURPOSE_POST_COMPLAINT = 0;
    public static final int PURPOSE_POST_COMMENT = 1;
    public static final int PURPOSE_CHANGE_STATUS = 2;
    public static final int PURPOSE_CHANGE_PROFILE_PIC = 3;
    public static final int PURPOSE_EDIT_COMPLAINT = 4;
    public static final int COMPLAINT_EDIT = 1001;
    public static final int COMPLAINT_DELETE = 1002;
    public static int selectedPurposeToUploadImage;
    public static final int FEEDBACK_0 = 0;
    public static final int FEEDBACK_1 = 1;
    public static final int FEEDBACK_2 = 2;
    public static FeedbackData selectedOptionFeedbackData = new FeedbackData();
    public static ArrayList<FeedbackData> feedbackData = new ArrayList<FeedbackData>();
    public static String canBeReopenedWithInSla = "";
    public static int selectedFeedback;

    // Google Analytics
    public static final String CATEGORY_COMPLAINT = "COMPLAINT";
    public static final String CATEGORY_USER = "USER";
    public static final String COMPLAINT_POSTED = "COMPLAINT POSTED";
    public static final String POSTED_COMPLAINTS = "POSTED COMPLAINTS";
    public static final String POST_COMPLAINT = "POST COMPLAINT";
    public static final String RE_POST_COMPLAINT_CLICKED = "RE-POST COMPLAINT BTN CLICKED FROM FEEDBACK";
    public static final String RE_POST_COMPLAINT = "RE-POST COMPLAINT FROM FEEDBACK";
    public static final String MARK_AS_REOPEN_FROM_FEEDBACK = "MARK AS REOPEN FROM FEEDBACK";
    public static final String MARK_AS_REOPEN_CLICKED_FROM_FEEDBACK = "MARK AS REOPEN BUTTON CLICKED FROM FEEDBACK";

    public static final String IMAGE_SELECTED_CAMERA = "IMAGE SELECTED CAMERA";
    public static final String IMAGE_SELECTED_GALLERY = "IMAGE SELECTED GALLERY";
    public static final String SELECT_CATEGORY = "SELECT CATEGORY";
    public static final String ADD_LANDMARK_LOCATION = "ADD_LANDMARK LOCATION";
    public static final String NOTIFICATION = "NOTIFICATION";
    public static final String NOTIFICATION_LANDED = "NOTIFICATION LANDED";
    public static final String PROFILE = "PROFILE SETTINGS";
    public static final String PROFILE_LANDED = "PROFILE SETTINGS LANDED";
    public static final String VOTEDUP_COMPLAINTS = "VOTEDUP COMPLAINTS";
    public static final String VOTEDUP_COMPLAINTS_LANDED = "VOTEDUP COMPLAINTS LANDED";
    public static final String RATE_US_ON_PLAYSTORE = "RATE US ON PLAYSTORE";
    public static final String RATE_US_ON_PLAYSTORE_LANDED = "RATE US ON PLAYSTORE LANDED";
    public static final String REPORT_BUG = "REPORT BUG";
    public static final String REPORT_BUG_LANDED = "REPORT BUG LANDED";
    public static final String LOGOUT = "LOGOUT";
    public static final String LOGGED_OUT_SUCCESS = "LOGGED OUT SUCCESS";
    public static final String NEARBY = "Nearby";
    public static final String CITY = "City";
    public static final String YOURS = "Yours";
    public static final String POST_COMPLAINT_FAILURE = "PostComplaintFailure";
    public static final String POST_COMPLAINT_SUCCESS = "PostComplaintSuccess";
    public static final String USER_UPDATE_PROFILE = "USER UPDATE PROFILE";
    public static final String UPDATE_PROFILE_SCREEN_REACHED = "UPDATE PROFILE SCREEN REACHED";
    public static final String UPDATE_PROFILE_SUCCESS = "USER UPDATE PROFILE SUCCESS";
    public static final String UPDATE_PROFILE_FAILURE = "USER UPDATE PROFILE FAILURE";
    public static final String USER_LOGIN = "logged in";
    public static final String SURVEY_SUBMIT = "SurveySubmit";
    public static final String ONBOARDING = "Onboarding";
    public static final String WITHOUT_OTP = " Without OTP";
    public static final String WITH_MVAYO_OTP = " With MVAYOO OTP";
    public static final String WITH_FAK_OTP = " With FAK OTP";
    public static final String WITH_MVAYO_RESEND_OTP = " With MVAYOO RESEND OTP";
    public static final String FAILED_FAK_OTP = "Failed with FAK OTP";
    public static final String MANUAL_CANCELLED_FAK_OTP = "Manual cancelled FAK OTP";
    public static int selectedComplaintDropdownIndex = -1;

    private static void getScreenResolution(Application activity) {
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.deviceWidth,
                width + "");
        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.deviceHeight,
                height + "");
    }

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

    public static void hideKeyboard(Activity activity, EditText et) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
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
    public static final String GCM_SENDER_ID = "403948698822";

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

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static void handleVolleyError(Activity act, final RelativeLayout layout, VolleyError volleyError) {
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

    public static void showToast(final Activity activity, final int type, final String message) {
        switch (type) {
            case TOAST_TYPE_ERROR:
                Toasty.error(activity.getApplicationContext(), message, Toast.LENGTH_SHORT, true).show();
                break;
            case AppConstant.TOAST_TYPE_INFO:
                Toasty.info(activity.getApplicationContext(), message, Toast.LENGTH_SHORT, true).show();
                break;
            case AppConstant.TOAST_TYPE_SUCCESS:
                Toasty.success(activity.getApplicationContext(), message, Toast.LENGTH_SHORT, true).show();
                break;
        }
    }

    public static void setUniqueIDToPreference(Activity act) {
        final String address = Settings.Secure
                .getString(act.getContentResolver(), Settings.Secure.ANDROID_ID);
        SecurePrefManager.with(act).set(ICMyCPreferenceData.deviceUniqueID).value(address).go();
    }

    public static boolean validateMobileNumber(Activity activity, final RelativeLayout relativeLayout, String mobileNumber) {
        String mobnobegin = "";
        try {
            mobnobegin = mobileNumber.trim().substring(0, 1);
        } catch (Exception e) {
            e.printStackTrace();
            mobnobegin = "";
        }
        boolean isValid = true;
        if (mobnobegin == null || mobnobegin.length() <= 0) {
            isValid = false;
//            validateInputField(activity, relativeLayout, mobileNumber, "Mobile number");
            Snackbar.make(relativeLayout, R.string.mobile_number_cannot_be_empty, Snackbar.LENGTH_SHORT).setActionTextColor(Color.WHITE).show();

        } else if (mobileNumber.length() > 0
                && mobileNumber.length() < 10) {
            isValid = false;
            Snackbar.make(relativeLayout, R.string.mobile_number_needs_10_digits, Snackbar.LENGTH_SHORT).setActionTextColor(Color.WHITE).show();
        } /*else if (!(mobnobegin.equalsIgnoreCase("7"))
                && !(mobnobegin.equalsIgnoreCase("8"))
                && !(mobnobegin.equalsIgnoreCase("9"))) {
            isValid = false;
            Snackbar.make(relativeLayout, R.string.mobile_number_must_begin_with_7_or_8_or_9, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE)
                    .show();
        } */ else {
            isValid = true;
        }
        return isValid;
    }

    public static View view;

    public static void showProgressDialog(final Activity activity, final String loading) {
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
            ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
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


    @SuppressWarnings("static-access")
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

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            mTracker = analytics.newTracker(getString(R.string.tracking_id));
        }
        return mTracker;
    }

    /***
     * Tracking screen view
     *
     * @param screenName
     *         screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();
        // Set screen name.
        t.setScreenName(screenName);
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param e
     *         exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();
            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null).getDescription(Thread
                                    .currentThread().getName(), e)).setFatal(false).build());
        }
    }

    /***
     * Tracking event
     *
     * @param category
     *         event category
     * @param action
     *         action of the event
     * @param label
     *         label
     */
    public static void trackEvent(String category, String action, String label) {
        Tracker t = AppController.getInstance().getGoogleAnalyticsTracker();
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action)
                .setLabel(label).build());
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

    public static boolean setLatitudeLongitude(Activity activity) {
        GPSTracker gps = new GPSTracker(activity);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            AppController.latitude = gps.getLatitude();
            AppController.longitude = gps.getLongitude();
            return true;
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
            return false;
        }
    }


    public static void showAlert(final Activity activity, final String title, final String message, final boolean isToShowNegativeButton, final
    OnButtonClick onButtonClick) {
        AlertDialog.Builder ab = new AlertDialog.Builder(activity);
        ab.setTitle(title);
        ab.setMessage(message);
        ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onButtonClick.onPositiveButtonClicked(dialogInterface);
            }
        });
        if (isToShowNegativeButton)
            ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onButtonClick.onNegativeButtonClicked(dialogInterface);
                }
            });
        ab.show();
    }


    public static void initiateCTAForShareComment(final Activity activity, final ComplaintData complaintData) {
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
    public static final int COMPLAINT_FOLLOW_UP = 2;
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

