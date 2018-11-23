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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.callback.OnButtonClick;
import com.ichangemycity.callback.OnResponseListener;
import com.ichangemycity.model.ComplaintData;
import com.ichangemycity.model.SelectedImageModel;
import com.ichangemycity.webservice.AppHelper;
import com.ichangemycity.webservice.URLData;
import com.ichangemycity.webservice.VolleyMultipartRequest;
import com.ichangemycity.webservice.VolleySingleton;
import com.ichangemycity.webservice.WebserviceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.ichangemycity.swachhbharatengineer.ComplaintDetail.isToRefresh;

public class ChangeStatusActivity extends BaseAppCompatActivity {
    Toolbar toolbar;
    RelativeLayout postComm;
    public static Activity activity;
    private static String url;
    ComplaintData data = new ComplaintData();
    ImageView addImage, send;
    private ImageView imageToUpload;
    TextView statusTitleValue;
    TextView messageToShow;
    String mStatus = "";
    ListView list;
    ArrayList<String> listOfReasonToRejectComplaint = new ArrayList<>();

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
        postComm = ((RelativeLayout) findViewById(R.id.postComm));
        messageToShow = (TextView) findViewById(R.id.messageToShow);
        list = (ListView) findViewById(R.id.list);
        listOfReasonToRejectComplaint.clear();

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
        setToolbarAndCustomizeTitle(getResources().getString(R.string.id_) + AppController.selectedComplaintData.getGeneric_id());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) findViewById(R.id.textComment)).getText().toString().trim().length() > 0) {
                    AppController.showProgressDialog(activity, "");
                    new InitiateChangeStatus().execute();
                } else {
                    AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO, activity.getResources().getString(R.string.write_a_comment));

//                    Toast.makeText(activity, getResources().getString(R.string.write_a_comment), Toast
//                            .LENGTH_SHORT).show();
                }

            }
        });
        postComm.setVisibility(View.VISIBLE);
        messageToShow.setText(R.string
                .you_are_changing_the_status_of_the_complaint_please_leave_a_comment_about_your_experience_or_if_you_have_any_remarks_you_can_add_photos_as_well_to_your_comment_);
        setStatusForTitle(AppController.selectedComplaintChangeStatusOptions.getStatusID());
    }

    private class InitiateChangeStatus extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postComm.setVisibility(View.GONE);
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
            if (AppController.selectedComplaintChangeStatusOptions.getStatusID() == AppController.COMPLAINT_RESOLVED && TextUtils.isEmpty
                    (AppController.mSelectedImageModels.getPathOfSelectedImage())) {

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
        //need clarify
        AppController.showProgressDialog(activity, activity.getResources().getString(R.string.loading));
        final String url = URLData.BASE_URL + URLData.COMPLAINT_STATUS;
        HashMap<String, String> params = new HashMap<String, String>();
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
                    .getPreferenceItem(activity, ICMyCPreferenceData.commentUploadedImageFile, ""));
        String URLParams =
                "?apiKey=" + URLData.API_KEY +
                        "&statusId=" + AppController.selectedComplaintChangeStatusOptions.getStatusID() +
                        "&userId=" + ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.id, "") +
                        "&complaintId=" + AppController.selectedComplaintData.getComplaintId() +
                        "&commentDescription=" + ((EditText) findViewById(R.id.textComment)).getText().toString().replace(" ", "%20");
        if (hasImage)
            URLParams = URLParams + "&fileId=" + ICMyCPreferenceData
                    .getPreferenceItem(activity, ICMyCPreferenceData.commentUploadedImageFile, "");

        new WebserviceHelper(activity, WebserviceHelper.METHOD_PUT, url + URLParams, null, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {
                AppController.hideProgressDialog(activity);
            }

            @Override
            public void OnResponseSuccess(JSONObject response) {

                //  JSONObject responseJsonObject = null;
                try {
                    AppController.hideProgressDialog(activity);
                    // responseJsonObject = new JSONObject(response);

                    try {
                        int httpCode = response.getInt("httpCode");
                        if (httpCode == 200 || httpCode == 201) {
                            ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.commentUploadedImageFile,
                                    "");
                            isToRefresh = true;
                            activity.finish();
                        }
                        AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO, response.optString("message"));
//                                Toast.makeText(activity,responseJsonObject.get("message").toString(),Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true, WebserviceHelper.HEADER_TYPE_NORMAL);


      /*  StringRequest stringRequest = new StringRequest(Request.Method.PUT, URLData.BASE_URL + URLData.COMPLAINT_STATUS,
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
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);*/
    }

    private void clearSelectedImage() {
        AppController.mSelectedImageModels = new SelectedImageModel();
        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.commentUploadedImageFile, "");
    }

    private void uploadImage() {
        final String uploadImageURL = URLData.BASE_URL_UPLOAD_IMAGE;
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
        postComm.setVisibility(View.VISIBLE);
        AppController.selectedPurposeToUploadImage = AppController.PURPOSE_POST_COMMENT;
        startActivity(new Intent(activity, SelectImageDialogActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    private void setStatusForTitle(int complaintStatus) {
        int complaintStatusTextColor = 0;
        list.setVisibility(View.GONE);
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
                    list.setVisibility(View.VISIBLE);
                    complaintStatus = R.drawable.complaint_status_closed;
                    complaintStatusTextColor = activity.getResources().getColor(
                            R.color.gray_closed);
                    listOfReasonToRejectComplaint.add("Complaint out of the city");
                    listOfReasonToRejectComplaint.add("Image not clear");
                    listOfReasonToRejectComplaint.add("Location not correct");
                    list.setAdapter(new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, listOfReasonToRejectComplaint));
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            view.setBackgroundColor(Color.LTGRAY);
                            ((EditText) findViewById(R.id.textComment)).setText(listOfReasonToRejectComplaint.get(i));
                            changeStatus(false);
                        }
                    });
                    messageToShow.setText(R.string.change_status_rejected);
                    postComm.setVisibility(View.GONE);
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
