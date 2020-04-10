package com.ichangemycity.appdata;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ichangemycity.callback.OnButtonClick;
import com.ichangemycity.callback.OnTaskCompleted;
import com.ichangemycity.model.ComplaintData;
import com.ichangemycity.swachhbharatengineer.OTPVerification;
import com.ichangemycity.swachhbharatengineer.R;
import com.ichangemycity.swachhbharatengineer.SetEmailPasswordActivity;
import com.ichangemycity.swachhbharatengineer.Splashscreen;
import com.ichangemycity.swachhbharatengineer.UserMobileNumber;
import com.ichangemycity.webservice.GenerateNewAccessToken;
import com.ichangemycity.webservice.HTTPResponseCode;
import com.prashantsolanki.secureprefmanager.SecurePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

/**
 * Created by pattabi.raman on 03-10-2017.
 */

public class AppUtils {
    public static void setImage(final CircleImageView circleImageView,
        final NetworkImageView imageView, final String
        imageUrl, final boolean isCircularImageView) {
        final ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        if (isCircularImageView) {
            circleImageView.setTag(imageUrl);
            final ImageLoader.ImageContainer container = imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    circleImageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    circleImageView.setImageResource(R.mipmap.ic_not_found);
                }
            });
        } else {
            final ImageLoader.ImageContainer container = imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    imageView.setImageUrl(imageUrl, imageLoader);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    imageView.setImageResource(R.mipmap.ic_not_found);

                }
            });


        }

    }

    public static void showToast(final Activity activity, final int type, final String message) {
        switch (type) {
            case AppConstant.TOAST_TYPE_ERROR:
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

    public static boolean validateMobileNumber(final Activity activity, final EditText mobileNumber) {

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
                    activity,
                    "",
                    activity.getResources().getString(
                            R.string.mobile_number_cannot_be_empty), false, new OnButtonClick() {
                        @Override
                        public void onPositiveButtonClicked(DialogInterface dialogInterface) {

                        }

                        @Override
                        public void onNegativeButtonClicked() {

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
                    activity,
                    "",
                    activity.getResources().getString(
                            R.string.mobile_number_needs_10_digits), false, new OnButtonClick() {
                        @Override
                        public void onPositiveButtonClicked(DialogInterface dialogInterface) {

                        }

                        @Override
                        public void onNegativeButtonClicked() {

                        }
                    });
            // alertdialog_with_one_button
            // .showErrorAlert("Mobile Number cannot be empty");
            // et_emailid.setError(null);
            // et_phone_number.setError(null);
            // mMobileNumber.setError("Password cannot be empty");
            mobileNumber.setFocusable(true);
            mobileNumber.requestFocus();
        }/* else if (!(mobnobegin.equalsIgnoreCase("7"))
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
        }*/ else {
            isValid = true;
        }
        return isValid;
    }

    public static View view;

    public static void showProgressDialog(final Activity activity) {
        if (view != null)
            hideProgressDialog(activity);
        view = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_loading, null);
        activity.addContentView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup
                        .LayoutParams.MATCH_PARENT));

    }

    public static void hideProgressDialog(final Activity activity) {
        try {

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

    public void parseProfileGetResponse(final Activity activity, final JSONObject jsonObject, final OnTaskCompleted onTaskCompleted) {
        class ParseResponse extends AsyncTask<Void, Void, Void> {
            JSONObject jsonObject = new JSONObject();

            ParseResponse(JSONObject jsonObject1) {
                this.jsonObject = jsonObject1;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                JSONObject data = jsonObject.optJSONObject("data") == null ? null : jsonObject.optJSONObject("data");
                JSONObject location = data.optJSONObject("location") == null ? null : data.optJSONObject("location");

                if (data != null) {
                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.id, data.optString("id"));
                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.user_full_name, data.optString("full_name"));
                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.Mobile_No, data.optString("mobile_number"));
                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.userProfileImage, data.optString("avatar"));
                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.organization, data.optString("organization"));
                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.email, data.optString("email"));
                }
                if (location != null) {
                    ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.location, location.optString("name"));
                    JSONArray coordinates = location.optJSONArray("coordinates");
                    if (coordinates.length() == 2) {
                        try {
                            ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.Latitude, coordinates.get(1) + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.Latitude, "0.0");
                        }
                        try {
                            ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.Longitude, coordinates.get(0) + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.Longitude, "0.0");
                        }
                    }

                    try {
                        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.ward_id, location.optString("ward_id"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.ward_id, "");
                    }
                    try{
                        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.city_id, location.optString("city_id"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.city_id, "");
                    }

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                onTaskCompleted.onTaskSuccess();
            }

        }
        new ParseResponse(jsonObject).execute();
    }

    private static String errors = "";
    private static JSONObject responseObject = new JSONObject();

    public static void handleVolleyError(final Activity act,final VolleyError volleyError) {
        NetworkResponse response = volleyError.networkResponse;
        responseObject = new JSONObject();
        errors = "";
        try {
            responseObject = new JSONObject(new String(response.data));
        } catch (JSONException e) {
        } catch (NullPointerException ex) {
            errors = ex.getMessage();
        }
        if (response != null)
            switch (response.statusCode) {
                case HTTPResponseCode.HTTP_SUCCESS_OK:
                case HTTPResponseCode.HTTP_SUCCESS_OK_:
                    break;
                case HTTPResponseCode.HTTP_NOT_FOUND:
                    break;
                case HTTPResponseCode.HTTP_VALIDATION_ERROR:
                    try {
                        Iterator<String> iterator = responseObject.keys();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            try {
                                if (key.equalsIgnoreCase("errors")) {
                                    JSONObject value = (responseObject.optJSONObject(key));
                                    Iterator<String> iterator1 = value.keys();
                                    while (iterator1.hasNext()) {
                                        String key1 = iterator1.next();
                                        errors += value.optJSONArray(key1).get(0) + "\n";
                                    }
                                }
                            } catch (Exception e) {
                                errors = "Error Parsing Failed!\n";
                                errors+=responseObject.optJSONArray("errors");
                            }
                        }
                        AppController.showAlert(act, responseObject.optString("message"), errors, false, new OnButtonClick() {
                            @Override
                            public void onPositiveButtonClicked(DialogInterface dialogInterface) {

                            }

                            @Override
                            public void onNegativeButtonClicked() {

                            }
                        });
                    } catch (Exception e) {
                    }
                    break;
                case HTTPResponseCode.HTTP_TOO_MANY_ATTEMPTS:
                    errors = responseObject.optString("message") + "";
                    if (errors != null)
                        if (errors.trim().length() > 0)
                            AppUtils.showToast(act, AppConstant.TOAST_TYPE_ERROR, errors);
                    break;
                case HTTPResponseCode.HTTP_BAD_REQUEST:
                    errors = responseObject.optString("message");
                    AppUtils.showToast(act, AppConstant.TOAST_TYPE_ERROR, errors);
                    break;
                case HTTPResponseCode.HTTP_UN_AUTHORIZED:
                    break;
                case HTTPResponseCode.HTTP_UNAUTHENTICATED:
                    errors = responseObject.optString("message") + "";
                    if (errors != null)
                        if (errors.trim().length() > 0)
                            AppUtils.showToast(act, AppConstant.TOAST_TYPE_ERROR, errors);
                    new GenerateNewAccessToken().generateNewAccessToken(act, new OnTaskCompleted() {
                        @Override
                        public void onTaskSuccess() {

                        }

                        @Override
                        public void onTaskFailure() {
                            if ((act.getClass().getSimpleName().equalsIgnoreCase(UserMobileNumber.class.getSimpleName()) ||
                                    act.getClass().getSimpleName().equalsIgnoreCase(SetEmailPasswordActivity.class.getSimpleName()) ||
                                     act.getClass().getSimpleName().equalsIgnoreCase(OTPVerification.class.getSimpleName()))) {
                                errors = responseObject.optString("message") + "";
                                AppController.traceLog("error", responseObject + "");
                                AppUtils.showToast(act, AppConstant.TOAST_TYPE_ERROR, errors);
                            } else {
                                ICMyCPreferenceData.clearPreferences(act);

                                new AppController().cancelPendingRequests(AppController.TAG);
                                act.startActivity(new Intent(act, Splashscreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                act.finish();
                            }
                        }
                    });

                    break;
                default:
                    errors = responseObject + "";
                    AppController.traceLog("error", responseObject + "");
//                AppUtils.showToast(act, AppConstant.TOAST_TYPE_ERROR, errors);
                    break;


            }
    }
}
