package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.callback.OnButtonClick;
import com.ichangemycity.model.ComplaintData;
import com.ichangemycity.model.SelectedImageModel;
import com.ichangemycity.webservice.AppHelper;
import com.ichangemycity.webservice.URLData;
import com.ichangemycity.webservice.VolleyMultipartRequest;
import com.ichangemycity.webservice.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.ichangemycity.swachhbharatengineer.ComplaintDetail.isToRefresh;

public class ChangeStatusActivity extends BaseAppCompatActivity {
    Toolbar toolbar;
    public static Activity activity;
    private static String url;
    ComplaintData data = new ComplaintData();
    ImageView addImage, send;
    private ImageView imageToUpload;
    TextView statusTitleValue;
    String mStatus = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.assignLanguage(ChangeStatusActivity.this);
        setContentView(R.layout.change_status_activity);
        activity = ChangeStatusActivity.this;
        BaseAppCompatActivity.activity = activity;
        clearSelectedImage();
        mStatus = AppController.selectedComplaintChangeStatusOptions.getStatusName();
        statusTitleValue = (TextView) findViewById(R.id.statusTitleValue);
        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        send = (ImageView) findViewById(R.id.send);
        url = URLData.BASE_URL + URLData.GET_POSTED_COMMENT
                + AppController.selectedComplaintData.getComplaintId()
                + URLData.GET_POSTED_COMMENT_SORT;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        addImage = (ImageView) findViewById(R.id.addImage);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertToPickImage();
            }
        });
        setToolbarAndCustomizeTitle(getResources().getString(R.string.id_)+ AppController.selectedComplaintData.getGeneric_id());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) findViewById(R.id.textComment)).getText().toString().trim().length() > 0) {
                    AppController.showProgressDialog(activity, "");
                    new InitiateChangeStatus().execute();
                } else {
                    AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO,activity.getResources().getString(R.string.write_a_comment));

//                    Toast.makeText(activity, getResources().getString(R.string.write_a_comment), Toast
//                            .LENGTH_SHORT).show();
                }

            }
        });
        ((RelativeLayout) findViewById(R.id.postComm)).setVisibility(View.VISIBLE);
        setStatusForTitle(AppController.selectedComplaintChangeStatusOptions.getStatusID());
    }

    private class InitiateChangeStatus extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((RelativeLayout) findViewById(R.id.postComm)).setVisibility(View.GONE);
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AppController.hideProgressDialog(activity);
            if (AppController.selectedComplaintChangeStatusOptions.getStatusID() == AppController.COMPLAINT_RESOLVED && TextUtils.isEmpty(AppController.mSelectedImageModels.getPathOfSelectedImage())) {

                AppController.showAlert(activity, "", getResources().getString(R.string
                        .please_upload_an_image_and_then_resolve_the_complaint_to_resolved), false, new OnButtonClick() {
                    @Override
                    public void onPositiveButtonClicked(DialogInterface dialogInterface) {
                        addImage.performClick();
                    }

                    @Override
                    public void onNegativeButtonClicked(DialogInterface dialogInterface) {

                    }
                });
            } else {
                if (!TextUtils.isEmpty(AppController.mSelectedImageModels.getPathOfSelectedImage())) {
                    uploadImage();
                } else {
                    if (!TextUtils.isEmpty(((EditText) findViewById(R.id.textComment)).getText().toString()))
                        changeStatus(false);
                    else
                        AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO, getResources().getString(R.string.write_a_comment));
//                    Toast.makeText(activity, getResources().getString(R.string.write_a_comment), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void changeStatus(final boolean hasImage) {
        AppController.showProgressDialog(activity, activity.getResources().getString(R.string.loading));
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URLData.BASE_URL + URLData.COMPLAINT_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject responseJsonObject = null;
                        try {
                            AppController.hideProgressDialog(activity);
                            responseJsonObject = new JSONObject(response);
                            try {
                                int httpCode = responseJsonObject.getInt("httpCode");
                                if (httpCode == 200 || httpCode == 201) {
                                    ICMyCPreferenceData
                                            .setPreference(
                                                    activity,
                                                    ICMyCPreferenceData.commentUploadedImageFile,
                                                    "");
                                    isToRefresh = true;
                                    activity.finish();
                                }
                                AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO, responseJsonObject.optString("message"));
//                                Toast.makeText(activity,
//                                        responseJsonObject.get("message").toString(),
//                                        Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppController.hideProgressDialog(activity);
                        AppController.handleVolleyError(activity, (RelativeLayout) activity.findViewById(R.id.parentLayout), error);


                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("apiKey", URLData.API_KEY);
                params.put("statusId", "" + AppController.selectedComplaintChangeStatusOptions.getStatusID());
                params.put("userId", ICMyCPreferenceData
                        .getPreferenceItem(activity,
                                ICMyCPreferenceData.id, ""));
                params.put("complaintId",
                        AppController.selectedComplaintData.getComplaintId());
                params.put("commentDescription",
                        ((EditText) findViewById(R.id.textComment)).getText().toString());
                if (hasImage)
                    params.put("fileId", ICMyCPreferenceData
                            .getPreferenceItem(
                                    activity,
                                    ICMyCPreferenceData.commentUploadedImageFile,
                                    ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = "Bearer " + ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.token, "");
                final HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token);
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppController.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    private void clearSelectedImage() {
        AppController.mSelectedImageModels = new SelectedImageModel();
        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.commentUploadedImageFile, "");
    }

    private void uploadImage() {
        final String uploadImageURL = "http://api.swachh.city/sbm/v1/" + URLData.FILE;
        AppController.showProgressDialog(activity, activity.getResources().getString(R.string.loading));
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadImageURL, new
                Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        JSONObject mJsonObject;
                        try {
                            mJsonObject = new JSONObject(resultResponse);

                            switch (mJsonObject.optInt("httpCode")) {
                                case 200:
                                case 201:
                                    JSONObject fileJsonObject = (JSONObject) mJsonObject
                                            .get("file");
                                    int fileId = fileJsonObject.optInt("id");
                                    ICMyCPreferenceData
                                            .setPreference(
                                                    activity,
                                                    ICMyCPreferenceData.commentUploadedImageFile,
                                                    "" + fileId);
                                    // runCommentsWebService();
                                    break;

                                default:
                                    break;
                            }

                            AppController.hideProgressDialog(activity);
                            changeStatus(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AppController.hideProgressDialog(activity);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppController.hideProgressDialog(activity);
                AppController.handleVolleyError(activity, (RelativeLayout) findViewById(R.id.parentLayout), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("apiKey", URLData.API_KEY);
                params.put("deviceWidth", 1024 + "");
                params.put("deviceHeight", 768 + "");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = "Bearer " + ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.token, "");
                final HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token);
                return headers;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                params.put("file", new DataPart("image" + new Random().nextInt() + ".jpg", AppHelper
                        .getFileDataFromDrawable(activity, AppController.mSelectedImageModels), "image/jpeg"));

                return params;
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppController.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (!TextUtils.isEmpty(AppController.mSelectedImageModels.getPathOfSelectedImage())) {
                showSelectedImage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Handler handler = new Handler();


    private void showSelectedImage() {
        if (!TextUtils.isEmpty(AppController.mSelectedImageModels.getPathOfSelectedImage())) {
            imageToUpload.setVisibility(View.VISIBLE);
            Glide.with(activity).load((AppController.mSelectedImageModels.getUriOfImage())).into(imageToUpload);
        } else {
            imageToUpload.setVisibility(View.GONE);
        }
    }

    private void setToolbarAndCustomizeTitle(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                isToRefresh = false;
            }
        });
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
    }


    private void showAlertToPickImage() {
        ((RelativeLayout) findViewById(R.id.postComm)).setVisibility(View.VISIBLE);
        AppController.selectedPurposeToUploadImage = AppController.PURPOSE_POST_COMMENT;
        startActivity(new Intent(activity, SelectImageDialogActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    private void setStatusForTitle(int complaintStatus) {
        int complaintStatusTextColor = 0;
        if (complaintStatus > 0) {
            switch (complaintStatus) {
                case AppController.COMPLAINT_REOPEN:
                    complaintStatus = R.drawable.complaint_status_red;
                    complaintStatusTextColor = activity.getResources().getColor(
                            R.color.red_reopn_open);
                    break;
                case AppController.COMPLAINT_OPEN:
                    complaintStatus = R.drawable.complaint_status_red;
                    complaintStatusTextColor = activity.getResources().getColor(
                            R.color.red_reopn_open);
                    break;
                case AppController.COMPLAINT_ON_THE_JOB:
                    complaintStatus = R.drawable.complaint_status_on_the_job;
                    complaintStatusTextColor = activity.getResources().getColor(
                            R.color.blue_on_the_job);
                    break;
                case AppController.COMPLAINT_RESOLVED:
                    complaintStatus = R.drawable.complaint_status_resolved;
                    complaintStatusTextColor = activity.getResources().getColor(
                            R.color.green_resolved);
                    break;
                case AppController.COMPLAINT_REJECTED:
                    complaintStatus = R.drawable.complaint_status_closed;
                    complaintStatusTextColor = activity.getResources().getColor(
                            R.color.gray_closed);
                    break;
                default:
                    complaintStatus = R.drawable.complaint_status_closed;
                    complaintStatusTextColor = activity.getResources().getColor(
                            R.color.gray_closed);
                    break;
            }
            statusTitleValue.setBackgroundDrawable(getResources().getDrawable(
                    complaintStatus));
            statusTitleValue.setTextColor(complaintStatusTextColor);
            statusTitleValue.setText(mStatus.toUpperCase());
        }
    }
}
