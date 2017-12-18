package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ichangemycity.adapter.VoteupsAdapter;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.model.VotedUpData;
import com.ichangemycity.webservice.URLData;
import com.jude.easyrecyclerview.EasyRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pattabi.raman on 25-07-2017.
 */

public class VoteupsActivity extends BaseAppCompatActivity {
    Toolbar toolbar;
    private Activity activity;
    private EasyRecyclerView recycler_view;
    private static String url;
    private int currentPage = 0;
    int visibleItemCount, totalItemCount, pastVisiblesItems;
    RecyclerView.LayoutManager layoutManager;
    boolean isLoadMore = true;
    ImageView addImage, send;
    VoteupsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.assignLanguage(VoteupsActivity.this);
        setContentView(R.layout.comments_activity);
//        AppController.trackEvent(GAData.VOTEUP, GAData.SCREEN_REACHED, GAData.SCREEN_REACHED);
        activity = VoteupsActivity.this;
        BaseAppCompatActivity.activity = activity;
        send = (ImageView) findViewById(R.id.send);
        url = 	URLData.BASE_URL
                + URLData.GET_VOTED_UP
                + AppController.selectedComplaintData
                .getComplaintId()
                + URLData.GET_VOTED_UP_SORT;
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        adapter = new VoteupsAdapter(activity, AppController.votedUpData, false);
        ((RelativeLayout) findViewById(R.id.postComm)).setVisibility(View.GONE);
        addImage = (ImageView) findViewById(R.id.addImage);
        recycler_view = (EasyRecyclerView) findViewById(R.id.mRecyclerview);
        setToolbarAndCustomizeTitle(getResources().getString(R.string.vote_up) + "(" + AppController.selectedComplaintData.getVote_up_count() + ")");
        layoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(layoutManager);

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
        runVotedupFeedWebService(true);

    }

    private void runVotedupFeedWebService(final boolean isToScroll) {
        if (isToScroll) {
            currentPage = 0;
            AppController.showProgressDialog(activity, getResources().getString(R.string.loading));
        }
        currentPage += 1;
        if (currentPage == 1) {
            AppController.votedUpData.clear();
            recycler_view.setAdapter(new VoteupsAdapter(activity, AppController.votedUpData, false));

        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + currentPage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject responseJsonObject = null;
                        try {
                            AppController.hideProgressDialog(activity);
                            responseJsonObject = new JSONObject(response);
                            new ParseResponse(responseJsonObject, isToScroll).execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(UserMobileNumber.this, error.toString(), Toast.LENGTH_LONG).show();
                        AppController.hideProgressDialog(activity);

                        AppController.handleVolleyError(activity, (RelativeLayout) findViewById(R.id.parentLayout), error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<String, String>();
                }
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppController.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    private class ParseResponse extends AsyncTask<Void, Void, Void> {
        private JSONObject jsonObject;
        private boolean isToScroll;

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
            parseData(this.jsonObject);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//            if (isToScroll) {
            AppController.hideProgressDialog(activity);
            addVoteups();
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
//                            if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount - 5)) {
                        // loading = false;
                        try {
                            runVotedupFeedWebService(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                            }

                    }

                }
            });
           /* } else {
                adapter.notifyDataSetChanged();
            }*/
        }


    }

    private void parseData(final JSONObject json_comp_object) {

        try {
            String complaintString = json_comp_object
                    .optString("vote_up_users");

            JSONArray jsonArray = new JSONArray(complaintString);
            if (jsonArray.length() == 0) {
                isLoadMore = false;
            } else {
                isLoadMore = true;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject voted_up_usersJsonObject = jsonArray
                            .getJSONObject(i);
                    try {

                        VotedUpData mVotedUpData = new VotedUpData();
                        mVotedUpData.setId(voted_up_usersJsonObject
                                .optInt("id") + "");
                        mVotedUpData
                                .setComplaint_count(voted_up_usersJsonObject
                                        .optString("complaint_count") + "");
                        mVotedUpData.setFull_name(voted_up_usersJsonObject
                                .optString("full_name"));
                        mVotedUpData.setUser_id(voted_up_usersJsonObject
                                .optInt("user_id") + "");
                        mVotedUpData.setUser_image_url(voted_up_usersJsonObject
                                .optString("user_image_url"));
                        mVotedUpData.setVoted_up_on(voted_up_usersJsonObject
                                .optString("voted_up_on"));
                        AppController.votedUpData.add(mVotedUpData);
                    } catch (Exception e) {
                        e.printStackTrace();
//                        isLoadMore = false;

                    }

                }
                AppController.selectedComplaintData.setVotedUpData(AppController.votedUpData);
                AppController.selectedComplaintData.setVote_up_count(AppController.votedUpData.size()+"");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//            isLoadMore = false;
            AppController.hideProgressDialog(activity);
        }


    }

    private void addVoteups() {

        RecyclerView.LayoutManager manager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(new VoteupsAdapter(activity, AppController.votedUpData, false));
        ((TextView) activity.findViewById(R.id.viewEmpty)).setText(getResources().getString(R.string.no_complaints));
        adapter.notifyDataSetChanged();

    }

}
