package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.ichangemycity.adapter.CommentsAdapter;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.model.CommentsData;
import com.ichangemycity.model.ComplaintData;
import com.ichangemycity.model.SelectedImageModel;
import com.ichangemycity.webservice.AppHelper;
import com.ichangemycity.webservice.ParseComplaintData;
import com.ichangemycity.webservice.URLData;
import com.ichangemycity.webservice.VolleyMultipartRequest;
import com.ichangemycity.webservice.VolleySingleton;
import com.jude.easyrecyclerview.EasyRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CommentsActivity extends BaseAppCompatActivity {
    Toolbar toolbar;
    public static Activity activity;
    private EasyRecyclerView recycler_view;
    private static String url;
    private int currentPage = 0;
    ComplaintData data = new ComplaintData();
    int visibleItemCount, totalItemCount, pastVisiblesItems;
    RecyclerView.LayoutManager layoutManager;
    boolean isLoadMore = true;
    ImageView addImage, send;
    private float wt_px, ht_px, margin;
    private ImageView imageToUpload;
    TextView comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.assignLanguage(CommentsActivity.this);
        setContentView(R.layout.comments_activity);
        activity = CommentsActivity.this;
        BaseAppCompatActivity.activity = activity;
        clearSelectedImage();
        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        send = (ImageView) findViewById(R.id.send);
        data = AppController.selectedComplaintData;
        url = URLData.BASE_URL + URLData.GET_POSTED_COMMENT
                + data.getComplaintId()
                + URLData.GET_POSTED_COMMENT_SORT;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        addImage = (ImageView) findViewById(R.id.addImage);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertToPickImage();
            }
        });
        recycler_view = (EasyRecyclerView) findViewById(R.id.mRecyclerview);
        setToolbarAndCustomizeTitle(getResources().getString(R.string.comments));
        layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);
        wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, activity
                .getResources().getDisplayMetrics());
        ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, activity
                .getResources().getDisplayMetrics());
        margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, activity
                .getResources().getDisplayMetrics());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) findViewById(R.id.textComment)).getText().toString().trim().length() > 0) {
                    AppController.showProgressDialog(activity, "");
                    new InitiatePostComment().execute();
                } else {
                    AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO,activity.getResources().getString(R.string.write_a_comment));

//                    Toast.makeText(activity, getResources().getString(R.string.write_a_comment), Toast
//                            .LENGTH_SHORT).show();
                }

            }
        });
        ((RelativeLayout) findViewById(R.id.postComm)).setVisibility(View.VISIBLE);
        runCommentFeedWebService(true);
    }

    private class InitiatePostComment extends AsyncTask<Void, Void, Void> {
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
            if (!TextUtils.isEmpty(AppController.mSelectedImageModels.getPathOfSelectedImage())) {
                uploadImage();
            }/* else if (TextUtils.isEmpty(ICMyCPreferenceData.getPreferenceItem(activity,
                    ICMyCPreferenceData.commentUploadedImageFile, null))) {
                postComment(true);
            }*/ else {
                if (!TextUtils.isEmpty(((EditText) findViewById(R.id.textComment)).getText().toString()))
                    postComment(false);
                else
//                    Toast.makeText(activity, getResources().getString(R.string.write_a_comment), Toast.LENGTH_SHORT).show();
                AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO,activity.getResources().getString(R.string.write_a_comment));

            }
        }
    }

    private void postComment(final boolean hasImage) {
        AppController.showProgressDialog(activity, activity.getResources().getString(R.string.loading));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLData.BASE_URL
                + URLData.COMMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject responseJsonObject = null;
                        try {
                            AppController.hideProgressDialog(activity);
                            ((EditText) CommentsActivity.this.findViewById(R.id.textComment)).setText("");
                            if (hasImage) {
//                                AppController.trackEvent(GAData.POST_A_COMMENT + GAData.WITH_IMAGE, GAData.DONE, GAData.DONE);
                                clearSelectedImage();
                                showSelectedImage();
                            } else {
//                                AppController.trackEvent(GAData.POST_A_COMMENT, GAData.DONE, GAData.DONE);
                            }
                            responseJsonObject = new JSONObject(response);
//                            Toast.makeText(activity, responseJsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                            AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO, responseJsonObject.optString("message"));

                            parseData(responseJsonObject, true);

                            if (recycler_view != null)
                                if (recycler_view.getAdapter() != null)
                                    recycler_view.getAdapter().notifyDataSetChanged();
                            ((RelativeLayout) findViewById(R.id.postComm)).setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        runCommentFeedWebService(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(UserMobileNumber.this, error.toString(), Toast.LENGTH_LONG).show();
                        AppController.hideProgressDialog(activity);
                        ((RelativeLayout) findViewById(R.id.postComm)).setVisibility(View.VISIBLE);
                        AppController.handleVolleyError(activity, (RelativeLayout) findViewById(R.id.parentLayout), error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("complaintId", AppController.selectedComplaintData.getComplaintId());
                params.put("apiKey", URLData.API_KEY);
                params.put("commentTypeId", Integer.toString(1));
                params.put("commentDescription", ((EditText) findViewById(R.id.textComment)).getText().toString());
                if (hasImage)
                    params.put("fileId", ICMyCPreferenceData
                            .getPreferenceItem(
                                    CommentsActivity.this,
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
                        try {
                            AppController.hideProgressDialog(activity);
                            JSONObject result = new JSONObject(resultResponse);
                            try {
                                switch (result.optInt("httpCode")) {
                                    case 200:
                                    case 201:
                                        JSONObject fileJsonObject = (JSONObject) result
                                                .get("file");
                                        int fileId = fileJsonObject.optInt("id");
                                        ICMyCPreferenceData
                                                .setPreference(
                                                        CommentsActivity.this,
                                                        ICMyCPreferenceData.commentUploadedImageFile,
                                                        "" + fileId);
                                        postComment(true);
                                        // runCommentsWebService();
                                        break;

                                    default:
                                        break;
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            AppController.hideProgressDialog(activity);

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
            }
        });
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        runCommentFeedWebService(true);

    }

    private void runCommentFeedWebService(final boolean isToScroll) {
        if (isToScroll) {
            currentPage = 0;
//            AppController.showProgressDialog(activity, getResources().getString(R.string.loading));
        }
        ++currentPage;
        if (currentPage == 1) {
            AppController.commentData.clear();
            recycler_view.setAdapter(new CommentsAdapter(activity, false));
        }
        String getCommentRequestUrl = url + currentPage;
//        if (AppController.getInstance().getRequestQueue().getCache().get(url) != null) {
//            try {
//                JSONObject response = new JSONObject(String.valueOf(AppController.getInstance().getRequestQueue().getCache().get(url).data));
//                new ParseResponse(response, isToScroll).execute();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//        AppController.logTrace(activity, url + currentPage);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getCommentRequestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {

                        if (isToScroll) {
                            AppController.commentData.clear();
                            AppController.hideProgressDialog(activity);
                        }

                        new ParseResponse(response, isToScroll).execute();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                if (isToScroll)
                    AppController.hideProgressDialog(activity);
                AppController.handleVolleyError(activity, (RelativeLayout) findViewById(R.id.parentLayout), volleyError);
                NetworkResponse networkResponse = volleyError.networkResponse;
                AppController.setEmptyViewForRecyclerView(activity, recycler_view);
                if (retryCount < AppConstant.MAX_RETRY_API_REQUEST && currentPage == 1 && networkResponse != null) {
                    retryCount++;
                    runCommentFeedWebService(true);
                }
            }

        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return URLData.getHeaders(activity);

            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                AppController.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                TAG);
//        }
    }

    int retryCount;

    private class ParseResponse extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject;
        boolean isToScroll;

        ParseResponse(final JSONObject jsonObject, final boolean isToScroll) {
            this.jsonObject = jsonObject;
            this.isToScroll = isToScroll;
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
//            if (this.jsonObject.optJSONObject("paginator").optBoolean("hasMore"))
//                isLoadMore = true;
            parseData(this.jsonObject, false);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            addComments();
            AppController.hideProgressDialog(activity);
            if (isToScroll) {
                AppController.hideProgressDialog(activity);
                recycler_view.setAdapter(new CommentsAdapter(activity, false));
                AppController.setEmptyViewForRecyclerView(activity, recycler_view);
                recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    /**
                     * Callback method to be invoked when the RecyclerView has been scrolled. This will be
                     * called after the scroll has completed.
                     * <p>
                     * This callback will also be called if visible item range changes after a layout
                     * calculation. In that case, dx and dy will be 0.
                     *
                     * @param recyclerView The RecyclerView which scrolled.
                     * @param dx           The amount of horizontal scroll.
                     * @param dy           The amount of vertical scroll.
                     */
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        boolean enable = false;
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = ((LinearLayoutManager) layoutManager)
                                .findFirstVisibleItemPosition();
                        if (visibleItemCount > 0 && recycler_view != null) {
                            boolean firstItemVisible = pastVisiblesItems == 0;
                            // check if the top of the first item is
                            // visible
                            boolean topOfFirstItemVisible = ((LinearLayoutManager) layoutManager)
                                    .findFirstCompletelyVisibleItemPosition() == 0;
                            enable = firstItemVisible && topOfFirstItemVisible;
                        }

                        if (isLoadMore) {
                            if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount - 5)) {
                                isLoadMore = false;
                                // loading = false;
                                try {
                                    runCommentFeedWebService(false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    }
                });
            } else {
                recycler_view.getAdapter().notifyDataSetChanged();
            }
        }


    }

    private String totalCommentCount = "";

    private void parseData(final JSONObject json_comp_object, boolean isAfterPostComment) {
        try {


            String complaintString = json_comp_object.optString("comments");
            totalCommentCount = json_comp_object.optString("totalCommentCount")
                    .toString();
            JSONArray jsonArray = new JSONArray(complaintString);
            if (jsonArray.length() == 0) {
                isLoadMore = false;
            } else {
                isLoadMore = true;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject commentsJsonObject = jsonArray.getJSONObject(i);

                    try {
                        CommentsData ccData = new CommentsData();
                        ccData.setComment_id(commentsJsonObject.optInt("id")
                                + "");
                        ccData.setComment_user_id(commentsJsonObject
                                .optInt("user_id") + "");
                        ccData.setComment_full_name(commentsJsonObject
                                .optString("full_name"));
                        ccData.setComment_description(commentsJsonObject
                                .optString("description"));
                        ccData.setComment_posted_on(commentsJsonObject
                                .optString("posted_on"));
                        ccData.setComment_complaint_status(commentsJsonObject
                                .optString("complaint_status"));
                        ccData.setComment_complaint_status_id(commentsJsonObject
                                .get("complaint_status_id").toString() + "");
                        ccData.setComment_image_url(commentsJsonObject
                                .optString("comment_image_url"));
                        if (commentsJsonObject.has("user_image_url"))
                            ccData.setUser_image_url(commentsJsonObject
                                    .optString("user_image_url"));
                        try {
                            ccData.setSpanColorForCoplaintStatus(ParseComplaintData.getSpanColorForStatusTitle(activity, Integer
                                    .parseInt(ccData
                                            .getComment_complaint_status_id())));
                        } catch (NumberFormatException w) {
                            ccData.setSpanColorForCoplaintStatus("#00000000");
                        }
                        AppController.commentData.add(ccData);

                    } catch (Exception e) {
                        e.printStackTrace();
                        isLoadMore = false;

                    }
                }
                AppController.selectedComplaintData.setCommentsData(AppController.commentData);
                AppController.selectedComplaintData.setComment_count(AppController.commentData.size() + "");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void addComments() {
        final CommentsAdapter commentsAdapter = new CommentsAdapter(activity, false);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(commentsAdapter);


    }

    private void showAlertToPickImage() {
        AppController.selectedPurposeToUploadImage = AppController.PURPOSE_POST_COMMENT;
        startActivity(new Intent(activity, SelectImageDialogActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

}
