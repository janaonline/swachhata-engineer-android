package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ichangemycity.adapter.NotificationAdapter;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.model.NotificationHeaderData;
import com.ichangemycity.webservice.URLData;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class NotificationActivity extends BaseAppCompatActivity implements
        android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {
    public static int btn_acknowledged_position;
    Context context_activity;

    private int current_page, current_page_read;
    Dialog myDialog = null;
    public static ArrayList<NotificationHeaderData> data = new ArrayList<NotificationHeaderData>();
    ArrayList<NotificationHeaderData> data1 = new ArrayList<NotificationHeaderData>();
    boolean isLoadMore = true;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private SwipeRefreshLayout refreshLayout;
    ProgressWheel pb_loader = null;

    private com.jude.easyrecyclerview.EasyRecyclerView mRecyclerView,
            mRecyclerView2;
    @SuppressWarnings("rawtypes")
    public static RecyclerView.Adapter mAdapter, mAdapter2;
    private RecyclerView.LayoutManager mLayoutManager, mLayoutManager2;
    private static boolean isDrawerOpened = false;
    Button slideHandleButton;
    SlidingDrawer slidingDrawer;
    private static Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setHasOptionsMenu(true);
        AppController.assignLanguage(NotificationActivity.this);
        setContentView(R.layout.notification_activity);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        activity = NotificationActivity.this;
        initSwipeOptions();
        // recycler view
        mRecyclerView = (com.jude.easyrecyclerview.EasyRecyclerView) findViewById(R.id.list);
        mRecyclerView2 = (com.jude.easyrecyclerview.EasyRecyclerView) findViewById(R.id.listRead);
        mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager2 = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());


        slideHandleButton = (Button) findViewById(R.id.slideHandleButton);
        slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);

        pb_loader = (ProgressWheel) findViewById(R.id.pb_loader);
        pb_loader.setBarColor(Color.rgb(85, 146, 251));
        pb_loader.setVisibility(View.VISIBLE);

        runHomeFeedWebService();
        setToolbarAndCustomizeTitle((Toolbar) findViewById(R.id.toolbar), getResources().getString(R.string.notification));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mark_notifs_read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.markAllAsRead) {
           markAllAsRead(activity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setToolbarAndCustomizeTitle(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    private void runHomeFeedWebService() {
        mRecyclerView.setVisibility(View.GONE);
        current_page = 1;
        String url = URLData.BASE_URL
                + URLData.ENGINEER_NOTIFICATION + URLData.PAGE
                + current_page + URLData.NOTIFICATION_STATUS + URLData.UNREAD;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        new ParseJSONResponse(response).execute();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(final VolleyError error) {

                AppController.hideProgressDialog(activity);
                AppController.handleVolleyError(activity, (RelativeLayout) findViewById(R.id.parentLayout), error);
            }

        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return URLData.getHeaders(activity);
            }

            @Override
            protected Map<String, String> getParams() {
                return null;
            }

        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                AppController.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                TAG);

    }

    private void runMoreHomeFeedWebService() {
        current_page += 1;
        String url = URLData.BASE_URL
                + URLData.ENGINEER_NOTIFICATION + URLData.PAGE + current_page + URLData.NOTIFICATION_STATUS
                + URLData.UNREAD;
        // currentPosition = data.size() - 1;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        new ParseMoreJSONResponse(response).execute();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(final VolleyError error) {

                AppController.hideProgressDialog(activity);
                AppController.handleVolleyError(activity, (RelativeLayout) findViewById(R.id.parentLayout), error);
            }

        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return URLData.getHeaders(activity);
            }

            @Override
            protected Map<String, String> getParams() {
                return null;
            }

        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                AppController.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                TAG);

    }

    private class ParseJSONResponse extends AsyncTask<Void, Void, Void> {
        JSONObject response = new JSONObject();

        public ParseJSONResponse(final JSONObject response) {
            this.response = response;
            pb_loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {
            data.clear();
            data = GetParsedJsonFromResponse(this.response, data, false);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            // mAdapter.notifyDataSetChanged();
        }

        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pb_loader.setVisibility(View.GONE);
            // refreshLayout.setEnabled(true);
            mAdapter = new NotificationAdapter(activity, data);
            mRecyclerView.setAdapter(mAdapter);
            // mAdapter.notifyDataSetChanged();

            onProgressUpdate();
            mRecyclerView
                    .setOnScrollListener(new RecyclerView.OnScrollListener() {

                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            boolean enable = false;
                            visibleItemCount = mLayoutManager.getChildCount();
                            totalItemCount = mLayoutManager.getItemCount();
                            pastVisiblesItems = ((LinearLayoutManager) mLayoutManager)
                                    .findFirstVisibleItemPosition();
                            if (visibleItemCount > 0 && mRecyclerView != null) {
                                boolean firstItemVisible = pastVisiblesItems == 0;
                                // check if the top of the first item is
                                // visible
                                boolean topOfFirstItemVisible = ((LinearLayoutManager) mLayoutManager)
                                        .findFirstCompletelyVisibleItemPosition() == 0;
                                enable = firstItemVisible
                                        && topOfFirstItemVisible;
                            }
                            if (!isDrawerOpened)
                                refreshLayout.setEnabled(enable);

                            if (isLoadMore) {
                                if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount - 5)) {
                                    isLoadMore = false;
                                    // loading = false;
                                    try {
                                        runMoreHomeFeedWebService();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if ((controlsVisible && dy > 0)
                                        || (!controlsVisible && dy < 0)) {
                                    scrolledDistance += dy;
                                }
                            }

                        }

                        // private static final int HIDE_THRESHOLD = 10;
                        private int scrolledDistance = 0;
                        private boolean controlsVisible = true;
                    });
            // lastListItemPosition =
            // lv_readcomplaints.getLastVisiblePosition();
            if (pb_loader.isShown()) {
                pb_loader.setVisibility(View.GONE);


            }
            hideSwipeProgress();

            slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

                @Override
                public void onDrawerOpened() {
                    runReadNotifWebService();
                    isDrawerOpened = true;
                }
            });

            slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

                @Override
                public void onDrawerClosed() {
                    isDrawerOpened = false;
                }
            });
            if (data.size() <= 0) {
                // HandleWebService.showAlert(Notifs.this, "", getResources()
                // .getString(R.string.no_read_notification),
                // new AlertCallback() {
                //
                // @Override
                // public void onPositiveButtonClicked(
                // DialogInterface dialog) {
                // // TODO Auto-generated method stub
                // slidingDrawer.toggle();
                // }
                //
                // @Override
                // public void onNegativeButtonClicked(
                // DialogInterface dialog) {
                // // TODO Auto-generated method stub
                //
                // }
                // });
                // Toast.makeText(Notifs.this,
                // getResources().getString(R.string.no_read_notification),
                // Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ParseMoreJSONResponse extends AsyncTask<Void, Void, Void> {
        JSONObject response = new JSONObject();

        public ParseMoreJSONResponse(final JSONObject response) {
            this.response = response;
            pb_loader.setVisibility(View.GONE);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            // mAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            data = GetParsedJsonFromResponse(this.response, data, false);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // refreshLayout.setEnabled(true);
            mAdapter.notifyDataSetChanged();
            // lastListItemPosition =
            // lv_readcomplaints.getLastVisiblePosition();
            if (pb_loader.isShown()) {
                pb_loader.setVisibility(View.GONE);

//                linear.startAnimation(upBottom);

            }
            hideSwipeProgress();
        }
    }

    private ArrayList<NotificationHeaderData> GetParsedJsonFromResponse(
            JSONObject json_comp_object, ArrayList<NotificationHeaderData> data,
            boolean readStatus) {

        try {
            // Log.i("page", "------------------------------->" + current_page);
            // apiResponse = IChangeMyCity.loadJSONFromAsset(Notifs.this,
            // "complaints");

            JSONArray json_comp_array = json_comp_object
                    .getJSONArray("notifications");
            if (json_comp_array.length() == 0) {
                Log.i("page",
                        "--------PAGINATION--------->"
                                + json_comp_array.toString());
                isLoadMore = false;
            } else {
                Log.i("page", "------------------------------->"
                        + json_comp_array.toString());

                data.addAll(getParsedNotificationData(json_comp_array,
                        readStatus));
                isLoadMore = true;
            }
            return data;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isLoadMore = false;
        }
        return data;
    }

    int lastListItemPosition = 0;

    // int currentPosition;

    private void initSwipeOptions() {
        refreshLayout.setOnRefreshListener(this);
        setAppearance();
        // enableSwipe();
        refreshLayout.setEnabled(true);
    }

    private void setAppearance() {
        refreshLayout.setColorScheme(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_bright);
    }

    /**
     * It shows the SwipeRefreshLayout progress
     */
    public void showSwipeProgress() {
        refreshLayout.setRefreshing(true);
    }

    /**
     * It shows the SwipeRefreshLayout progress
     */
    public void hideSwipeProgress() {
        refreshLayout.setRefreshing(false);
    }

    /**
     * It must be overriden by parent classes if manual swipe is enabled.
     */
    @Override
    public void onRefresh() {
        // Empty implementation
        try {
            // if (!isDrawerOpened)
            runHomeFeedWebService();
            // else
            // runReadNotifWebService();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<NotificationHeaderData> getParsedNotificationData(
            final JSONArray json_comp_array, boolean readStatus) {
        final ArrayList<NotificationHeaderData> data = new ArrayList<NotificationHeaderData>();
        if (json_comp_array != null)
            if (json_comp_array.length() > 0) {
                for (int i = 0; i < json_comp_array.length(); i++) {
                    try {
                        JSONObject mJsonObject = json_comp_array
                                .getJSONObject(i);
                        NotificationHeaderData notificationHeaderData0 = new NotificationHeaderData();
                        notificationHeaderData0.setHeaderTitle(mJsonObject
                                .optString("header-title"));
                        notificationHeaderData0.setDateValue(mJsonObject
                                .optString("date-value"));
                        String notificationDataString = mJsonObject
                                .optString("notification-data");
                        notificationHeaderData0.setTYPE_ITEM(0);// HEADER
                        data.add(notificationHeaderData0);

                        final JSONArray notificationDataJsonArray = new JSONArray(
                                notificationDataString);

                        for (int j = 0; j < notificationDataJsonArray.length(); j++) {
                            NotificationHeaderData notificationHeaderData = new NotificationHeaderData();
                            notificationHeaderData.setTYPE_ITEM(1);// ITEM TYPE
                            final JSONObject notificationDataJsonObject = notificationDataJsonArray
                                    .getJSONObject(j);
                            notificationHeaderData
                                    .setNotificationId(notificationDataJsonObject
                                            .optInt("notificationId"));
                            notificationHeaderData
                                    .setContentId(notificationDataJsonObject
                                            .optInt("contentId"));
                            notificationHeaderData
                                    .setFeedCreatedOn(notificationDataJsonObject
                                            .optString("feedCreatedOn"));
                            notificationHeaderData
                                    .setContentCreatedOn(notificationDataJsonObject
                                            .optString("contentCreatedOn"));
                            notificationHeaderData
                                    .setFeedType(notificationDataJsonObject
                                            .optString("feedType"));
                            notificationHeaderData
                                    .setRedirectTo(notificationDataJsonObject
                                            .optString("redirectTo"));
                            notificationHeaderData
                                    .setTextMsg(notificationDataJsonObject
                                            .optString("textMsg").replace(" ",
                                                    "-"));

                            notificationHeaderData.setRead(readStatus);
                            if (notificationHeaderData.getFeedType()
                                    .equalsIgnoreCase("Posted")) {
                                notificationHeaderData
                                        .setImageIcon(R.mipmap.ic_mode_edit_white_24dp);
                            } else if (notificationHeaderData.getFeedType()
                                    .equalsIgnoreCase("Voted")) {
                                notificationHeaderData
                                        .setImageIcon(R.mipmap.ic_thumb_up_white_24dp);
                            } else if (notificationHeaderData.getFeedType()
                                    .equalsIgnoreCase("Commented")) {
                                notificationHeaderData
                                        .setImageIcon(R.mipmap.ic_comment_white_48dp);
                            } else if (notificationHeaderData.getFeedType()
                                    .equalsIgnoreCase("StatusChange")) {
                                notificationHeaderData
                                        .setImageIcon(R.mipmap.ic_notifications_active_white_48dp);
                            } else {
                                notificationHeaderData
                                        .setImageIcon(R.mipmap.ic_notifications_active_white_48dp);
                            }
                            data.add(notificationHeaderData);
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        return data;
    }

    // Read Notification
    private void runReadNotifWebService() {
        current_page_read = 1;
        // data1.clear();
        String url = URLData.BASE_URL
                + URLData.USERS + URLData.NOTIFICATION + URLData.PAGE
                + current_page_read + URLData.NOTIFICATION_STATUS
                + URLData.READ;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        new ParseReadNotifJSONResponse(response).execute();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(final VolleyError volleyError) {

                AppController.hideProgressDialog(activity);
                AppController.handleVolleyError(activity, (RelativeLayout) findViewById(R.id.parentLayout), volleyError);
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

    }

    private void runMoreReadNotifWebService() {
        current_page_read += 1;
        // currentPosition = data1.size() - 1;
        String url = URLData.BASE_URL
                + URLData.ENGINEER_NOTIFICATION + URLData.PAGE
                + current_page_read + URLData.NOTIFICATION_STATUS
                + URLData.READ;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        new ParseMoreReadNotifJSONResponse(response).execute();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(final VolleyError volleyError) {

                AppController.hideProgressDialog(activity);
                AppController.handleVolleyError(activity, (RelativeLayout) findViewById(R.id.parentLayout), volleyError);
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

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        runHomeFeedWebService();
    }

    private class ParseReadNotifJSONResponse extends
            AsyncTask<Void, Void, Void> {
        JSONObject response = new JSONObject();

        public ParseReadNotifJSONResponse(JSONObject response) {
            this.response = response;
            data1.clear();
            pb_loader.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            data1 = GetParsedJsonFromResponse(this.response, data1, true); // default
            // status
            // read
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            // mAdapter2.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            // refreshLayout.setEnabled(true);
            if (data1.size() <= 0) {
                // HandleWebService.showAlert(Notifs.this, "", getResources()
                // .getString(R.string.no_read_notification),
                // new AlertCallback() {
                //
                // @Override
                // public void onPositiveButtonClicked(
                // DialogInterface dialog) {
                // // TODO Auto-generated method stub
                // slidingDrawer.toggle();
                // }
                //
                // @Override
                // public void onNegativeButtonClicked(
                // DialogInterface dialog) {
                // // TODO Auto-generated method stub
                //
                // }
                //
                // });
                Toast.makeText(
                        activity,
                        getResources().getString(R.string.no_read_notification),
                        Toast.LENGTH_LONG).show();
            }
            mAdapter2 = new NotificationAdapter(activity, data1);
            mRecyclerView2.setAdapter(mAdapter2);
            mAdapter2.notifyDataSetChanged();
            onProgressUpdate();
            mRecyclerView2
                    .setOnScrollListener(new RecyclerView.OnScrollListener() {

                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            boolean enable = false;
                            visibleItemCount = mLayoutManager.getChildCount();
                            totalItemCount = mLayoutManager.getItemCount();
                            pastVisiblesItems = ((LinearLayoutManager) mLayoutManager)
                                    .findFirstVisibleItemPosition();
                            if (visibleItemCount > 0 && mRecyclerView != null) {
                                boolean firstItemVisible = pastVisiblesItems == 0;
                                // check if the top of the first item is
                                // visible
                                boolean topOfFirstItemVisible = ((LinearLayoutManager) mLayoutManager)
                                        .findFirstCompletelyVisibleItemPosition() == 0;
                                enable = firstItemVisible
                                        && topOfFirstItemVisible;
                            }
                            if (isDrawerOpened)
                                refreshLayout.setEnabled(enable);

                            if (isLoadMore) {
                                if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount - 5)) {
                                    isLoadMore = false;
                                    // loading = false;
                                    try {
                                        runMoreReadNotifWebService();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if ((controlsVisible && dy > 0)
                                        || (!controlsVisible && dy < 0)) {
                                    // scrolledDistance += dy;
                                }
                            }

                        }

                        // private static final int HIDE_THRESHOLD = 10;
                        // private int scrolledDistance = 0;
                        private boolean controlsVisible = true;
                    });
            // lastListItemPosition =
            // lv_readcomplaints.getLastVisiblePosition();
            if (pb_loader.isShown()) {
                pb_loader.setVisibility(View.GONE);
//                linear.startAnimation(upBottom);
            }
            hideSwipeProgress();
        }
    }

    private class ParseMoreReadNotifJSONResponse extends
            AsyncTask<Void, Void, Void> {
        JSONObject response = new JSONObject();

        public ParseMoreReadNotifJSONResponse(JSONObject response) {
            this.response = response;
            pb_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            mAdapter2.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            data1 = GetParsedJsonFromResponse(this.response, data1, true); // default
            // read
            // true
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // refreshLayout.setEnabled(true);
            mAdapter2.notifyDataSetChanged();
            // lastListItemPosition =
            // lv_readcomplaints.getLastVisiblePosition();
            if (pb_loader.isShown()) {
                pb_loader.setVisibility(View.GONE);
//                linear.startAnimation(upBottom);

            }
            hideSwipeProgress();
        }
    }

    private void markAllAsRead(final Activity activity) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT, URLData.BASE_URL
                + URLData.NOTIFICATION_STATUS_READ
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            Toast.makeText(
                                    activity,
                                    (response).optString("message")
                                    , Toast.LENGTH_SHORT)
                                    .show();
                            data.clear();
                            ICMyCPreferenceData.setPreference(activity,
                                    ICMyCPreferenceData.unreadNotificationsCnt,
                                    data.size() + "");
                            mAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(final VolleyError volleyError) {

                AppController.hideProgressDialog(activity);
                AppController.handleVolleyError(activity, (RelativeLayout) findViewById(R.id.parentLayout), volleyError);
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
                params.put("allReadStatus", Integer
                        .toString(1));
                params.put("apiKey", URLData.API_KEY);
                return params;
            }

        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                AppController.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                AppController.TAG);

    }
}
