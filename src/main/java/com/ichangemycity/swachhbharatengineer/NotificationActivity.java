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

import com.ichangemycity.adapter.NotificationAdapter;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.callback.OnResponseListener;
import com.ichangemycity.model.NotificationHeaderData;
import com.ichangemycity.webservice.URLData;
import com.ichangemycity.webservice.WebserviceHelper;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class NotificationActivity extends BaseAppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener {
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
        refreshLayout = findViewById(R.id.swipe_container);
        activity = NotificationActivity.this;
        initSwipeOptions();
        // recycler view
        mRecyclerView = findViewById(R.id.list);
        mRecyclerView2 = findViewById(R.id.listRead);
        mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager2 = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
        slideHandleButton = findViewById(R.id.slideHandleButton);
        slidingDrawer = findViewById(R.id.SlidingDrawer);
        pb_loader = findViewById(R.id.pb_loader);
        pb_loader.setBarColor(Color.rgb(85, 146, 251));
        pb_loader.setVisibility(View.VISIBLE);
        runHomeFeedWebService();
        setToolbarAndCustomizeTitle(findViewById(R.id.toolbar), getResources().getString(R.string.notification));
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
        toolbar.setNavigationOnClickListener(v -> activity.finish());
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
        new WebserviceHelper(activity, WebserviceHelper.METHOD_GET, url, null, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {
                AppController.hideProgressDialog(activity);
                //  AppController.handleVolleyError(activity, (RelativeLayout) findViewById(R.id.parentLayout), error);
            }

            @Override
            public void OnResponseSuccess(JSONObject response) {
                mRecyclerView.setVisibility(View.VISIBLE);
                new ParseJSONResponse(response).execute();
            }
        }, true, WebserviceHelper.HEADER_TYPE_NORMAL);

    }


    private void runMoreHomeFeedWebService() {
        current_page += 1;
        String url = URLData.BASE_URL
                + URLData.ENGINEER_NOTIFICATION + URLData.PAGE + current_page + URLData.NOTIFICATION_STATUS
                + URLData.UNREAD;
        // currentPosition = data.size() - 1;
        new WebserviceHelper(activity, WebserviceHelper.METHOD_GET, url, null, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {
                AppController.hideProgressDialog(activity);
            }

            @Override
            public void OnResponseSuccess(JSONObject response) {
                mRecyclerView.setVisibility(View.VISIBLE);
                new ParseMoreJSONResponse(response).execute();
            }
        }, true, WebserviceHelper.HEADER_TYPE_NORMAL);
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
            slidingDrawer.setOnDrawerOpenListener(() -> {
                runReadNotifWebService();
                isDrawerOpened = true;
            });
            slidingDrawer.setOnDrawerCloseListener(() -> isDrawerOpened = false);
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
        new WebserviceHelper(activity, WebserviceHelper.METHOD_GET, url, null, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {
                AppController.hideProgressDialog(activity);

            }

            @Override
            public void OnResponseSuccess(JSONObject response) {
                new ParseReadNotifJSONResponse(response).execute();
            }
        }, true, WebserviceHelper.HEADER_TYPE_NORMAL);
    }

    private void runMoreReadNotifWebService() {
        current_page_read += 1;
        // currentPosition = data1.size() - 1;
        String url = URLData.BASE_URL
                + URLData.ENGINEER_NOTIFICATION + URLData.PAGE
                + current_page_read + URLData.NOTIFICATION_STATUS
                + URLData.READ;
        new WebserviceHelper(activity, WebserviceHelper.METHOD_GET, url, null, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {
                AppController.hideProgressDialog(activity);
            }

            @Override
            public void OnResponseSuccess(JSONObject response) {
                new ParseMoreReadNotifJSONResponse(response).execute();
            }
        }, true, WebserviceHelper.HEADER_TYPE_NORMAL);
    }

    @Override
    public void onResume() {
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
                AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO, getResources().getString(R.string.no_read_notification));
//                Toast.makeText(
//                        activity,
//                        getResources().getString(R.string.no_read_notification),
//                        Toast.LENGTH_LONG).show();
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
        final String url = URLData.BASE_URL+ URLData.NOTIFICATION_STATUS_READ;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("allReadStatus", Integer
                .toString(1));
        params.put("apiKey", URLData.API_KEY);
        //return params;
        new WebserviceHelper(activity, WebserviceHelper.METHOD_PUT, url,params, new OnResponseListener() {
            @Override
            public void OnResponseFailure(JSONObject response) {
                AppController.hideProgressDialog(activity);
            }

            @Override
            public void OnResponseSuccess(JSONObject response) {
                try {
                    AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO, (response).optString("message"));
//                            Toast.makeText(
//                                    activity,
//                                    (response).optString("message")
//                                    , Toast.LENGTH_SHORT)
//                                    .show();
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
        }, true, WebserviceHelper.HEADER_TYPE_NORMAL);


    }

}