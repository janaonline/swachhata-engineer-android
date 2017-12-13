package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ichangemycity.adapter.ComplaintFilterSpinnerAdapter;
import com.ichangemycity.adapter.HomeTabLocalFeedAdapter;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.model.ComplaintData;
import com.ichangemycity.model.ComplaintFilterModel;
import com.ichangemycity.webservice.URLData;
import com.prashantsolanki.secureprefmanager.SecurePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.color.holo_blue_bright;
import static android.R.color.holo_green_light;
import static android.R.color.holo_orange_light;
import static android.R.color.holo_red_light;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    public static Activity activity;
    com.jude.easyrecyclerview.EasyRecyclerView mRecyclerView;
    @SuppressWarnings("rawtypes")
    private RecyclerView.Adapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ComplaintFilterModel> complaintFilterModel = new ArrayList<ComplaintFilterModel>();
    public static Spinner complaintFilter;
    private static Toolbar toolbar;
    public static android.support.v7.app.ActionBar actionBar;
    private DrawerLayout drawer;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = MainActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // recycler view
        mRecyclerView = (com.jude.easyrecyclerview.EasyRecyclerView) findViewById(R.id.list);
        refreshLayout = (android.support.v4.widget.SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initSwipeOptions();
        setToolbarAndCustomizeTitle(toolbar, getResources().getString(R.string.app_name));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getProfileDetailsAndRunHomeFeed();
        setSpinnerData();


    }

    private void setSpinnerData() {

        // TODO Auto-generated method stub
        complaintFilterModel.clear();
        ComplaintFilterModel mComplaintFilterModel = new ComplaintFilterModel();
        if (ICMyCPreferenceData.getPreferenceItem(activity,
                ICMyCPreferenceData.roleId, "").equalsIgnoreCase("2")) {
            // 2 or ULB , 4 for Engineer
            mComplaintFilterModel
                    .setDisplayTitle(activity
                            .getResources().getString(
                                    R.string.un_assigned_complaints));
            mComplaintFilterModel
                    .setComplaintType(URLData.UN_ASSIGNED_COMPLAINTS);

        } else {
            mComplaintFilterModel.setDisplayTitle("Assigned Complaints");
            mComplaintFilterModel
                    .setComplaintType(URLData.ASSIGNED_COMPLAINTS_ENGINEER);

        }

        mComplaintFilterModel.setComplaintColor((getResources()
                .getColor(R.color.black)));
        complaintFilterModel.add(mComplaintFilterModel);

        mComplaintFilterModel = new ComplaintFilterModel();
        mComplaintFilterModel.setComplaintType(URLData.ALL_COMPLAINTS);
        mComplaintFilterModel
                .setDisplayTitle(getString(R.string.all_complaints));
        mComplaintFilterModel.setComplaintColor((getResources()
                .getColor(R.color.black)));
        complaintFilterModel.add(mComplaintFilterModel);

        mComplaintFilterModel = new ComplaintFilterModel();
        mComplaintFilterModel.setComplaintType(URLData.PRIORITY_COMPLAINTS);
        mComplaintFilterModel
                .setDisplayTitle(getString(R.string.high_priority_complaints));
        mComplaintFilterModel.setComplaintColor((getResources()
                .getColor(R.color.red_reopn_open)));
        complaintFilterModel.add(mComplaintFilterModel);

        mComplaintFilterModel = new ComplaintFilterModel();
        mComplaintFilterModel
                .setComplaintType(URLData.ON_THE_JOB_COMPLAINT_LISTS);
        mComplaintFilterModel
                .setDisplayTitle(getString(R.string.on_the_job_complaints));
        mComplaintFilterModel.setComplaintColor((getResources()
                .getColor(R.color.blue_on_the_job)));

        complaintFilterModel.add(mComplaintFilterModel);

        mComplaintFilterModel = new ComplaintFilterModel();
        mComplaintFilterModel
                .setComplaintType(URLData.REOPENED_COMPLAINT_LISTS);
        mComplaintFilterModel
                .setDisplayTitle(getString(R.string.re_opened_complaints));
        mComplaintFilterModel.setComplaintColor((getResources()
                .getColor(R.color.red_reopn_open)));
        complaintFilterModel.add(mComplaintFilterModel);

        mComplaintFilterModel = new ComplaintFilterModel();
        mComplaintFilterModel
                .setComplaintType(URLData.RESOLVED_COMPLAINT_LISTS);
        mComplaintFilterModel
                .setDisplayTitle(getString(R.string.resolved_complaints));
        mComplaintFilterModel.setComplaintColor((getResources()
                .getColor(R.color.green_resolved)));
        complaintFilterModel.add(mComplaintFilterModel);

        mComplaintFilterModel = new ComplaintFilterModel();
        mComplaintFilterModel
                .setComplaintType(URLData.GET_REJECTED_COMPLAINT_LISTS);
        mComplaintFilterModel
                .setDisplayTitle(getString(R.string.rejected_complaints));
        mComplaintFilterModel.setComplaintColor((getResources()
                .getColor(R.color.gray_closed)));
        complaintFilterModel.add(mComplaintFilterModel);

        complaintFilter = (Spinner) findViewById(R.id.complaintFilter);
        ComplaintFilterSpinnerAdapter complaintFilterSpinnerAdapter = new ComplaintFilterSpinnerAdapter(
                activity, complaintFilterModel);
        complaintFilter.setAdapter(complaintFilterSpinnerAdapter);

        complaintFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                runHomeFeedWebService(complaintFilterModel.get(position)
                        .getComplaintType(), true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void setToolbarAndCustomizeTitle(Toolbar toolbar, String string) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notifs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static MenuItem menuItem;


    TextView on_the_job, resolved, rejected, re_opened,high_priority;
    NavigationView navigationView;

    private void initializeCountForNavItems() {
        high_priority = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.high_priority));
        on_the_job = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.on_the_job));
        resolved = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.resolved));
        rejected = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.rejected));
        re_opened = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.re_opened));
        setPropertyForNavItemCount();
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        setLeftMenuProfileDetails();
    }

    private static View headerView;
    private static ImageView menuIcon1;
    private TextView userNameLeftMenu, textViewLocation;

    @Deprecated
    private void setLeftMenuProfileDetails() {
        headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        menuIcon1 = (ImageView) drawer.findViewById(R.id.menuIcon1);
        menuIcon1.setVisibility(View.VISIBLE);
        CircleImageView imageView = (CircleImageView) drawer.findViewById(R.id.imageView1);
        imageView.setImageResource(R.mipmap.ic_not_found);
        imageView.setTag(SecurePrefManager.with(activity).get(ICMyCPreferenceData.userProfileImage).defaultValue("http://icmycsaasqa.ichangemycity" +
                ".com/android/image/image_not_found.png").go());
        AppUtils.setImage(activity, imageView, null, SecurePrefManager.with(activity).get(ICMyCPreferenceData.userProfileImage).defaultValue("")
                .go(), true);
        userNameLeftMenu = (TextView) drawer.findViewById(R.id.userNameLeftMenu);
        textViewLocation = (TextView) drawer.findViewById(R.id.textViewLocation);

        menuIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
        userNameLeftMenu.setText(SecurePrefManager.with(activity).get(ICMyCPreferenceData.user_full_name).defaultValue(activity.getResources()
                .getString(R
                        .string.you)).go());
        textViewLocation.setText(SecurePrefManager.with(activity).get(ICMyCPreferenceData
                .location).defaultValue
                (getString(R.string.location)).go());


    }


    private void setPropertyForNavItemCount() {

        high_priority.setGravity(Gravity.CENTER_VERTICAL);
        high_priority.setTypeface(null, Typeface.BOLD);
        high_priority.setTextColor(getResources().getColor(R.color.secondary_text_color));
        high_priority.setText(SecurePrefManager.with(activity).get(ICMyCPreferenceData.high_priority_count).defaultValue("0").go());

        on_the_job.setGravity(Gravity.CENTER_VERTICAL);
        on_the_job.setTypeface(null, Typeface.BOLD);
        on_the_job.setTextColor(getResources().getColor(R.color.secondary_text_color));
        on_the_job.setText(SecurePrefManager.with(activity).get(ICMyCPreferenceData.on_the_job_count).defaultValue("0").go());

        resolved.setGravity(Gravity.CENTER_VERTICAL);
        resolved.setTypeface(null, Typeface.BOLD);
        resolved.setTextColor(getResources().getColor(R.color.secondary_text_color));
        resolved.setText(SecurePrefManager.with(activity).get(ICMyCPreferenceData.resolved_count).defaultValue("0").go());

        rejected.setGravity(Gravity.CENTER_VERTICAL);
        rejected.setTypeface(null, Typeface.BOLD);
        rejected.setTextColor(getResources().getColor(R.color.secondary_text_color));
        rejected.setText(SecurePrefManager.with(activity).get(ICMyCPreferenceData.rejected_count).defaultValue("0").go());

        re_opened.setGravity(Gravity.CENTER_VERTICAL);
        re_opened.setTypeface(null, Typeface.BOLD);
        re_opened.setTextColor(getResources().getColor(R.color.secondary_text_color));
        re_opened.setText(SecurePrefManager.with(activity).get(ICMyCPreferenceData.re_opened_count).defaultValue("0").go());

    }

    boolean isFirstTime = true;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if (!isFirstTime) {
            int id = item.getItemId();
            switch (id) {
                case R.id.high_priority:
                    complaintFilter.setSelection(2);
                    break;
                case R.id.on_the_job:
                    complaintFilter.setSelection(3);
                    break;
                case R.id.resolved:
                    complaintFilter.setSelection(5);
                    break;
                case R.id.rejected:
                    complaintFilter.setSelection(6);
                    break;
                case R.id.re_opened:
                    complaintFilter.setSelection(4);
                    break;
                case R.id.rate_us_on_playstore:
                    String appPackageName = activity.getPackageName();
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse("http://play.google.com/store/apps/details?id="
                                        + appPackageName)));
//                        AppController.trackEvent(AppController.RATE_US_ON_PLAYSTORE,
//                                AppController.RATE_US_ON_PLAYSTORE_LANDED,
//                                AppController.RATE_US_ON_PLAYSTORE_LANDED);
                    } catch (android.content.ActivityNotFoundException anfe) {

                    }
                    break;
                case R.id.nav_privacypolicy:
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse("http://www.ichangemycity.com/privacy-policy-mobile?app=sbmengineer")));
                    } catch (android.content.ActivityNotFoundException anfe) {

                    }
                    break;

                case R.id.report_bug:
                    try {
                        Intent emailIntent = new Intent(
                                Intent.ACTION_SENDTO,
                                Uri.fromParts(
                                        "mailto",
                                        "champa.r@janaagraha.org,pattabi.raman@janaagraha.org",
                                        null));
                        String sAux = "\n";
                        sAux = sAux + "Bug : \n";
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, activity
                                .getResources().getString(R.string.app_name)
                                + " - Android App - Bug Report");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, sAux);
                        activity.startActivity(Intent.createChooser(emailIntent,
                                "Report bug using"));

//                        AppController.trackEvent(AppController.REPORT_BUG,
//                                AppController.REPORT_BUG_LANDED,
//                                AppController.REPORT_BUG_LANDED);

                    } catch (Exception e) { // e.toString();
                    }
                    break;
                case R.id.nav_logout:
//                    AppController.trackEvent(
//                            AppController.LOGOUT,
//                            AppController.LOGGED_OUT_SUCCESS,
//                            AppController.LOGGED_OUT_SUCCESS);
                    SecurePrefManager.with(activity).clear().confirm();

                    activity.startActivity(new Intent(activity, Splashscreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    activity.finish();
                    break;
            }
        }
        if (isFirstTime)

        {
            isFirstTime = false;
        }

        drawer = (DrawerLayout)

                findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    private void getProfileDetailsAndRunHomeFeed() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URLData.BASE_URL + URLData.USERS + "?apiKey=" + URLData.API_KEY, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject mJsonObject) {
                        try {
                            if (mJsonObject.optInt("httpCode") == 200 || mJsonObject.optInt("httpCode") == 201) {
                                try {
                                    Toast.makeText(MainActivity.this,
                                            mJsonObject.optString("message"),
                                            Toast.LENGTH_LONG).show();
                                    handleSuccessResponse(mJsonObject);
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    Toast.makeText(MainActivity.this,
                                            mJsonObject.optString("message"),
                                            Toast.LENGTH_LONG).show();

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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
                AppController.TAG);


    }

    private void handleSuccessResponse(final JSONObject mJsonObject) {
        try {
            if (mJsonObject.has("engineer")) {
                String user = mJsonObject.optString("engineer");
                JSONObject userData = new JSONObject(user);
                String name = userData.optString("name");
                String mobile_number = userData
                        .optString("mobile_number");
                String latitude = userData.get("latitude")
                        .toString() + "";
                String roleId = userData.optString("role_id");

                String longitude = userData.get("longitude")
                        .toString() + "";
                // String complaint_count = userData
                // .getInt("complaint_count") + "";
                // String voted_up_count = userData
                // .getInt("voted_up_count") + "";
                String location = userData
                        .optString("location");
                // String language = userData
                // .optString("language");
                String language_code = userData
                        .optString("lang");
                String imageUrl = userData
                        .optString("image_urls");
                String unReadNotificationCount = userData
                        .optString("unread_notification_count");
                String high_priority_count = userData.get(
                        "high_priority_count").toString();
                String on_the_job_count = userData.get(
                        "on_the_job_count").toString();
                String resolved_count = userData.get(
                        "resolved_count").toString();
                String re_opened_count = userData.get(
                        "re_opened_count").toString();
                String rejected_count = userData.get(
                        "rejected_count").toString();
                String un_assigned_count = "0";
                if (userData.has("un_assigned_count"))
                    un_assigned_count = userData.getString(
                            "un_assigned_count").toString();

                if (!imageUrl.equalsIgnoreCase("")) {
                    JSONObject image_urls = new JSONObject(
                            imageUrl);
                    imageUrl = image_urls.optString("original");
                    ICMyCPreferenceData
                            .setPreference(
                                    MainActivity.this,
                                    ICMyCPreferenceData.userProfileImage,
                                    imageUrl);
                } else {
                    ICMyCPreferenceData
                            .setPreference(
                                    MainActivity.this,
                                    ICMyCPreferenceData.userProfileImage,
                                    "");
                }
                if (roleId != null) {
                    ICMyCPreferenceData.setPreference(
                            MainActivity.this,
                            ICMyCPreferenceData.roleId, roleId);
                    // 2 or ULB , 4 for Engineer
                    if (roleId.equalsIgnoreCase("2")) {

                    } else if (roleId.equalsIgnoreCase("4")) {
                        ICMyCPreferenceData
                                .setPreference(
                                        MainActivity.this,
                                        ICMyCPreferenceData.assignedCount,
                                        userData.getString(
                                                "assignedCount")
                                                .toString());
                    }
                }
                ICMyCPreferenceData.setPreference(
                        MainActivity.this,
                        ICMyCPreferenceData.un_assigned_count,
                        (un_assigned_count == null) ? "0"
                                : un_assigned_count);
                ICMyCPreferenceData
                        .setPreference(
                                MainActivity.this,
                                ICMyCPreferenceData.unreadNotificationsCnt,
                                (unReadNotificationCount == null) ? "0"
                                        : unReadNotificationCount);
                ICMyCPreferenceData
                        .setPreference(
                                MainActivity.this,
                                ICMyCPreferenceData.high_priority_count,
                                (high_priority_count == null) ? "0"
                                        : high_priority_count);
                ICMyCPreferenceData.setPreference(
                        MainActivity.this,
                        ICMyCPreferenceData.on_the_job_count,
                        (on_the_job_count == null) ? "0"
                                : on_the_job_count);
                ICMyCPreferenceData.setPreference(
                        MainActivity.this,
                        ICMyCPreferenceData.resolved_count,
                        (resolved_count == null) ? "0"
                                : resolved_count);
                ICMyCPreferenceData.setPreference(
                        MainActivity.this,
                        ICMyCPreferenceData.re_opened_count,
                        (re_opened_count == null) ? "0"
                                : re_opened_count);
                ICMyCPreferenceData.setPreference(
                        MainActivity.this,
                        ICMyCPreferenceData.rejected_count,
                        (rejected_count == null) ? "0"
                                : rejected_count);

                ICMyCPreferenceData.setPreference(
                        MainActivity.this,
                        ICMyCPreferenceData.Mobile_No,
                        mobile_number);
                // ICMyCPreferenceData.setPreference(
                // MainActivity.this,
                // ICMyCPreferenceData.selectedLanguage,
                // language_code);
                ICMyCPreferenceData.setPreference(
                        MainActivity.this,
                        ICMyCPreferenceData.location,
                        location.replace("%20", " "));
                ICMyCPreferenceData.setPreference(
                        MainActivity.this,
                        ICMyCPreferenceData.Latitude, latitude);
                ICMyCPreferenceData.setPreference(
                        MainActivity.this,
                        ICMyCPreferenceData.Longitude,
                        longitude);
                // ICMyCPreferenceData.setPreference(
                // MainActivity.this,
                // ICMyCPreferenceData.selectedLanguage,
                // language);

                // ICMyCPreferenceData
                // .setPreference(
                // MainActivity.this,
                // ICMyCPreferenceData.posted_complaint_count,
                // complaint_count);
                // ICMyCPreferenceData.setPreference(
                // MainActivity.this,
                // ICMyCPreferenceData.voted_up_count,
                // voted_up_count);
                ICMyCPreferenceData.setPreference(
                        MainActivity.this,
                        ICMyCPreferenceData.user_full_name,
                        name);
            }
            // new ParseJSONResponse(
            // complaintsResponse)
            // .execute();
            initializeCountForNavItems();

//            runHomeFeedWebService(complaintFilterModel.get(
//                    complaintFilter.getSelectedItemPosition())
//                    .getComplaintType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        onNavigationItemSelected(menuItem);
    }

    private int currentPage;
    public static ArrayList<ComplaintData> data = new ArrayList<>();

    private void runHomeFeedWebService(final String ComplaintType, final boolean isToScroll) {
        {
            if (isToScroll) {
                currentPage = 0;
//            ((TextView) (mRecyclerView.getEmptyView().findViewById(R.id.emptyView))).setText(activity.getResources().getString(R.string.loading));
//            mRecyclerView.getProgressView().setVisibility(View.VISIBLE);
//            AppController.showProgressDialog(activity, getString(R.string.loading));
            }
            currentPage += 1;
            if (currentPage == 1) {
                data.clear();
                mRecyclerView.setAdapter(new HomeTabLocalFeedAdapter(activity));
                AppController.setEmptyViewForRecyclerView(activity, mRecyclerView);
                try {
                    ((TextView) findViewById(R.id.viewEmpty)).setText(activity.getResources().getString(R.string.loading));
                } catch (Exception e) {
                }

            }
            final String url = URLData.BASE_URL
                    + ComplaintType + URLData.PAGE + currentPage;


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            AppController.traceLog("home", url + " ---> " + response);
                            new ParseJSONResponse(response, isToScroll).execute();

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(final VolleyError volleyError) {
//                AppController.hideProgressDialog(activity);
                    hideSwipeProgress();
                    AppController.handleVolleyError(activity, (RelativeLayout) activity.findViewById(R.id.parentLayout), volleyError);

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
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq,
                    AppController.TAG);

        }
    }

    private class ParseJSONResponse extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        boolean isToScroll;

        public ParseJSONResponse(JSONObject response, boolean isToScroll) {
            this.jsonObject = response;
            this.isToScroll = isToScroll;
        }

        @Override
        protected Void doInBackground(Void... params) {
            GetParsedJsonFromResponse(this.jsonObject);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mRecyclerView.setVisibility(View.VISIBLE);
            hideSwipeProgress();
            if (data.size() <= 0) {
                AppController.setEmptyViewForRecyclerView(activity, mRecyclerView);
                try {
                    ((TextView) findViewById(R.id.viewEmpty)).setText(activity.getResources().getString(R.string.loading));
                } catch (Exception e) {
                }
            }
            if (isToScroll) {
                AppController.hideProgressDialog(activity);
                mRecyclerView.setAdapter(new HomeTabLocalFeedAdapter(activity));
                AppController.setEmptyViewForRecyclerView(activity, mRecyclerView);
                try {
                    ((TextView) findViewById(R.id.viewEmpty)).setText(activity.getResources().getString(R.string.no_complaints));
                } catch (Exception e) {
                }
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                      /*  if (dy < 0) {
                            Animation hide = AnimationUtils.loadAnimation(activity, R.anim.up_bottom);
                            MainActivity.tabLayout.setVisibility(View.GONE);
                            MainActivity.tabLayout.startAnimation(hide);
                        } else if(dy <= 0 && dx<=0){
                            Animation show = AnimationUtils.loadAnimation(activity, R.anim.botton_up);
                            MainActivity.tabLayout.setVisibility(View.VISIBLE);
                            MainActivity.tabLayout.startAnimation(show);
                        }*/
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
                            enable = firstItemVisible && topOfFirstItemVisible;
                        }
                        refreshLayout.setEnabled(enable);
                        if (isLoadMore) {
                            if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount - 5)) {
                                isLoadMore = false;
                                // loading = false;
                                try {
                                    runHomeFeedWebService(complaintFilterModel.get(
                                            complaintFilter.getSelectedItemPosition()).getComplaintType(), false);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    }
                });
            } else {
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
            hideSwipeProgress();
        }


    }

    boolean isLoadMore;

    private ArrayList<ComplaintData> GetParsedJsonFromResponse(
            JSONObject json_comp_object) {
        try {
            JSONArray json_comp_array = json_comp_object
                    .getJSONArray("complaints");
            if (json_comp_array.length() == 0) {
                isLoadMore = false;
            } else {

                data.addAll(AppController
                        .getParsedComplaintData(json_comp_array));
                isLoadMore = true;
            }
            return data;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isLoadMore = false;
        }
        return null;
    }


    @Override
    public void onStart() {
        super.onStart();
        try {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    private void initSwipeOptions() {
        refreshLayout.setOnRefreshListener(this);
        setAppearance();
        // enableSwipe();
        refreshLayout.setEnabled(true);

    }

    private void setAppearance() {
        refreshLayout.setColorSchemeResources(holo_red_light,
                holo_green_light,
                holo_orange_light,
                holo_blue_bright);
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
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        mRecyclerView.setVisibility(View.GONE);
        runHomeFeedWebService(complaintFilterModel.get(
                complaintFilter.getSelectedItemPosition()).getComplaintType(), true);
    }
}
