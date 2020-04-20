package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.callback.OnResponseListener;
import com.ichangemycity.customui.WrapContentViewPager;
import com.ichangemycity.fragment.CommentsFragment;
import com.ichangemycity.fragment.VoteupFragment;
import com.ichangemycity.model.ChangeStatusListData;
import com.ichangemycity.model.CommentsData;
import com.ichangemycity.model.ComplaintData;
import com.ichangemycity.model.VotedUpData;
import com.ichangemycity.webservice.ParseComplaintData;
import com.ichangemycity.webservice.URLData;
import com.ichangemycity.webservice.WebserviceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.ichangemycity.adapter.ChangeStatusListAdapter;


/**
 * Created by pattabi.raman on 19-10-2017.
 */

public class ComplaintDetail extends BaseAppCompatActivity {

    public static Activity activity;
    TabLayout tabLayout;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appBarLayout;
    FrameLayout frameLoading;
    //    private ViewPager viewPagerPictures;
    private WrapContentViewPager viewPager;
    public ComplaintData complaintDetailData = new ComplaintData();
    TextView tv_username, hours_ago, complaint_category,
            complaintLocation, voteup, comments, complaint_status, complaint_landmark;
    CircleImageView user_image;
    NetworkImageView complaint_image;
    public ViewPagerAdapter adapters;
    //  vote up/ feedback
    LinearLayout /*cta_btn, cta_feedback, */resolved;
    TextView locationText, locationlandmark;
    ImageView change_status;
    ImageView locateComplaint, navigateComplaint;
    public static boolean isToRefresh = false;
    private Spinner changeStatus;
    FrameLayout frameSpinner;
    TextView satisfaction, un_satisfied, neutral;
    TextView comment, share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.assignLanguage(ComplaintDetail.this);
        setContentView(R.layout.complaint_details);
        activity = ComplaintDetail.this;
        BaseAppCompatActivity.activity = activity;
        d = new Dialog(activity);
        satisfaction = findViewById(R.id.satisfaction);
        un_satisfied = findViewById(R.id.un_satisfied);
        neutral = findViewById(R.id.neutral);
        frameSpinner = findViewById(R.id.frameSpinner);
        locateComplaint = findViewById(R.id.locateComplaint);
        navigateComplaint = findViewById(R.id.navigateComplaint);
        change_status = findViewById(R.id.change_status);
         complaint_image = findViewById(R.id.complaint_image);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        toolbar = findViewById(R.id.maintoolbar);
        tabLayout = findViewById(R.id.tabs);
        complaint_landmark = findViewById(R.id.complaint_landmark);
        frameLoading = findViewById(R.id.frameLoading);
        frameLoading.setVisibility(View.VISIBLE);
        viewPager = findViewById(R.id.viewpager);
        adapters = new ViewPagerAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        resolved = findViewById(R.id.resolved);
        hours_ago = findViewById(R.id.hours_ago);
        user_image = findViewById(R.id.user_image);
        complaintLocation = findViewById(R.id.complaintLocation);
        complaint_category = findViewById(R.id.complaint_category);
        voteup = findViewById(R.id.voteup);
        comments = findViewById(R.id.comments);
        appBarLayout = findViewById(R.id.appbarlayout);
        complaint_status = findViewById(R.id.complaint_status);
        //voteup/feedback
//        cta_btn = (LinearLayout) ComplaintDetail.this
//                .findViewById(R.id.not_resolved);
//        cta_feedback = (LinearLayout) ComplaintDetail.this
//                .findViewById(R.id.resolved);
        locationText = findViewById(R.id.locationText);
        locationlandmark = findViewById(R.id.locationlandmark);
         changeStatus = findViewById(R.id.changeStatus);
        change_status.setVisibility(View.GONE);
        setToolbarAndCustomizeTitle(toolbar, " ");
        runGetComplaintWebService();

        comment = findViewById(R.id.comment);
        share = findViewById(R.id.share);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (isToRefresh) {
                isToRefresh = false;
                runGetComplaintWebService();
            }
            if (d != null) {
                if (d.isShowing())
                    d.dismiss();
            }
        } catch (Exception e) {
        }
    }

    private void runGetComplaintWebService() {
        frameLoading.setVisibility(View.GONE);
        findViewById(R.id.parentLayout).setVisibility(View.GONE);
        AppController.showProgressDialog(activity);
        final String url = URLData.BASE_URL
                + URLData.COMPLAINT_ID
                + AppController.selectedComplaintData.getComplaintId()
                + "&userId="
                + ICMyCPreferenceData.getPreferenceItem(
                ComplaintDetail.this, ICMyCPreferenceData.id,
                "");

        new WebserviceHelper(activity, WebserviceHelper.METHOD_GET, url, null, new OnResponseListener() {
            @Override
            public void OnResponseFailure() {
                frameLoading.setVisibility(View.GONE);
                AppController.hideProgressDialog(activity);
                AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO,"Unknown error, please refresh complaints");
            }

            @Override
            public void OnResponseSuccess(JSONObject response) {
               // AppController.logTrace(activity, url + " ---> " + response);

                new ParseComplaintDetailResponse(response).execute();
            }

        },false,WebserviceHelper.HEADER_TYPE_NORMAL);
}



    private class ParseComplaintDetailResponse extends AsyncTask<Void, Void, Void> {

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
        private JSONObject response;

        ParseComplaintDetailResponse(final JSONObject response) {
            this.response = response;
        }

        @Override
        protected Void doInBackground(Void... params) {
            parseComplaintDetailResponse(this.response);
            AppController.selectedComplaintData = complaintDetailData;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            frameLoading.setVisibility(View.GONE);
            findViewById(R.id.parentLayout).setVisibility(View.VISIBLE);
            AppController.hideProgressDialog(activity);
            loadDataIntoComponents();
            initiateChangeStatusEventListener();
        }
    }

    private void initiateChangeStatusEventListener() {

        switch (Integer
                .parseInt(AppController.selectedComplaintData
                        .getAffected())) {
            case 1:
                if (!(AppController.selectedComplaintData
                        .getComplaint_status_id().equalsIgnoreCase(""
                                + AppController.COMPLAINT_REJECTED))) {
                    change_status
                            .setVisibility(View.VISIBLE);
                    change_status
                            .setOnClickListener(v -> inflateDialogtoShowChangeStatusMenu());
                } else if ((AppController.selectedComplaintData
                        .getComplaint_status_id().equalsIgnoreCase(
                                AppController.COMPLAINT_REJECTED + "")
                        || AppController.selectedComplaintData
                        .getComplaint_status_id()
                        .equalsIgnoreCase(
                                AppController.COMPLAINT_OPEN
                                        + "") || AppController.selectedComplaintData
                        .getComplaint_status_id().equalsIgnoreCase(
                                AppController.COMPLAINT_REOPEN + ""))
                        && (AppController.selectedComplaintData
                        .getUser_id().trim()
                        .equalsIgnoreCase(ICMyCPreferenceData
                                .getPreferenceItem(activity,
                                        ICMyCPreferenceData.id,
                                        "")))) {
                    change_status
                            .setVisibility(View.VISIBLE);
                    // if owner of complaint - show
// edit/delete
                    change_status.setOnClickListener(v -> inflateDialogtoShowChangeStatusMenu());
                } else {
                    change_status
                            .setVisibility(View.INVISIBLE);
                }
                break;
            case 0:
                change_status
                        .setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    private void loadDataIntoComponents() {
        AppController.hideProgressDialog(activity);
        setupViewPager();
        AppController.customizeChangeStatusDropdown(activity, complaintDetailData, resolved, changeStatus, neutral, satisfaction, un_satisfied,
                frameSpinner);
        setOffsetChangeListenerWhileScroll(true);
        appBarLayout.setExpanded(true);

//        setOffsetChangeListenerWhileScroll();
        locationText.setText(complaintDetailData.getLocation());
        locationlandmark.setText(complaintDetailData.getLandmark());
        tv_username = findViewById(R.id.tv_username);
        complaint_landmark.setText(complaintDetailData.getLandmark());
        complaint_landmark
            .setText(
                Html.fromHtml("<font><b>"+getString(R.string.more_information) + "</font></b> - " + complaintDetailData.getLandmark()));

        tv_username.setText(complaintDetailData.getFull_name());
        hours_ago.setText(complaintDetailData.getPosted_on());
        ParseComplaintData.setImage(activity, user_image, null, complaintDetailData.getUser_image(), true);
        complaint_category.setText(complaintDetailData.getCategory_name());
        complaintLocation.setText(complaintDetailData.getLocation());
        voteup.setText(complaintDetailData.getVote_up_count() + " " + getString(R.string.vote_up));
        comments.setText(complaintDetailData.getComment_count() + " " + getString(R.string.comments));
        ParseComplaintData.setImage(activity, null, complaint_image, complaintDetailData.getComplaint_image(), false);
        ParseComplaintData.setBgDrawableForComplaintStatus(activity, complaintDetailData, complaint_status);
        comment.setOnClickListener(m -> {
            // TODO Auto-generated method stub
            ComplaintData mCData = complaintDetailData;
            AppController.selectedComplaintData = mCData;
            AppController.selectedComplaintData.setToChangeStatus(false);
            Intent toCommentsActivity = new Intent(activity,
                    CommentsActivity.class);
            activity.startActivity(toCommentsActivity);
        });
        share.setOnClickListener(
            m -> ParseComplaintData.shareComplaint(activity, complaintDetailData));

        locateComplaint.setOnClickListener(v -> {
            String uri = String.format(
                    Locale.ENGLISH,
                    "geo:0,0?q=" + complaintDetailData.getLatitude() + ","
                            + complaintDetailData.getLongitude() + "&z=12 ("
                            + complaintDetailData.getLocation() + ")");
            // Uri uri = Uri.parse("geo:" + cData.getLatitude() + ","
            // + cData.getLongitude());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri
                    .parse(uri));
            mapIntent.setPackage("com.google.android.apps.maps");
            // if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
            // }
        });
        navigateComplaint.setOnClickListener(v -> {
            // TODO Auto-generated method stub

            String uri = String.format(Locale.ENGLISH,
                    "google.navigation:q=" + complaintDetailData.getLatitude() + ","
                            + complaintDetailData.getLongitude());
            // Uri uri = Uri.parse("geo:" + cData.getLatitude() + ","
            // + cData.getLongitude());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri
                    .parse(uri));
            mapIntent.setComponent(new ComponentName(
                    "com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity"));
            mapIntent.setPackage("com.google.android.apps.maps");
            // if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
            // }

        });
        AppController.initiateCTAForShareComment();
        change_status.setOnClickListener(v -> inflateDialogtoShowChangeStatusMenu());
    }

    private void inflateDialogtoShowChangeStatusMenu() {

        d.setContentView(R.layout.inflate_listview_change_status);
        cStatusListData = new ArrayList<>();
         new SetListData(d).execute();
    }

    ArrayList<ChangeStatusListData> cStatusListData;
    public static Dialog d;

    private class SetListData extends AsyncTask<Void, Void, Void> {


        SetListData(Dialog dialog) {
            d = dialog;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // cStatusListData.clear();
            cStatusListData = AppController.customizeListData(
                    ComplaintDetail.this, cStatusListData);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
//            ChangeStatusListAdapter adapter = new ChangeStatusListAdapter(
//                    ComplaintDetail.this, cStatusListData);
//            list.setAdapter(adapter);
            d.show();
        }
    }

    private void setOffsetChangeListenerWhileScroll(final boolean isToShowComplaintIDByDefault) {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.id_) + AppController.selectedComplaintData.getGeneric_id());
                    isShow = true;
                } else if (isShow) {
                    if (isToShowComplaintIDByDefault) {
                        collapsingToolbar.setTitle(getString(R.string.id_) + AppController.selectedComplaintData.getGeneric_id());
                    } else {
                        collapsingToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    }
                    isShow = false;
                }
            }
        });
    }

    private ComplaintData parseComplaintDetailResponse(final JSONObject json_comp_object) {
        if (json_comp_object != null) {

            try {
                try {
                    // apiResponse =
                    // IChangeMyCity.loadJSONFromAsset(PostedComplaints.this,
                    // "complaints");
                    // commentData.clear();
                    // votedUpData.clear();
                    AppController.commentData.clear();
                    AppController.votedUpData.clear();
                    String complaintString = json_comp_object
                            .optString("complaint");
                    JSONObject json_obj = new JSONObject(complaintString);


                    complaintDetailData.setComplaintId(json_obj.optInt("id") + "");
                    complaintDetailData.setLatitude(json_obj.get("latitude").toString());
                    complaintDetailData.setLongitude(json_obj.get("longitude").toString());

                    complaintDetailData.setComplaint_url(json_obj.optString("complaint_url"));
                    complaintDetailData.setGeneric_id(json_obj.optString("generic_id"));
                    complaintDetailData.setCity_id(json_obj.optInt("city_id") + "");
                    complaintDetailData.setUser_id(json_obj.optInt("user_id") + "");
                    complaintDetailData.setPosted_on(json_obj.optString("posted_on"));
                    complaintDetailData.setAccess_token(json_obj.optString("access_token"));

                    complaintDetailData.setCategory_id(json_obj.optInt("category_id") + "");
                    complaintDetailData.setVote_up_count(json_obj.optInt("vote_up_count") + "");
                    complaintDetailData.setComment_count(json_obj.optInt("comment_count") + "");
                    complaintDetailData.setCategory_name(json_obj.optString("category_name"));
                    if (json_obj.has("complaint_image"))
                        complaintDetailData.setComplaint_image(json_obj
                                .optString("complaint_image"));
                    else
                        complaintDetailData.setComplaint_image("http://icmycsaasqa.ichangemycity.com/android/garbage.jpg");

                    complaintDetailData.setLocation(json_obj.optString("location"));
                    if (json_obj.has("landmark"))
                        complaintDetailData.setLandmark(json_obj.optString("landmark"));
                    else
                        complaintDetailData.setLandmark("Landmark missing in web service");

                    if (json_obj.has("complaint_image_height"))
                        complaintDetailData.setComplaint_image_height(json_obj
                                .optInt("complaint_image_height") + "");
                    else
                        complaintDetailData.setComplaint_image_height(320 + "");

                    complaintDetailData.setParent_id(json_obj.optString("parent_id"));
                    complaintDetailData.setFull_name(json_obj.optString("full_name"));
                    complaintDetailData.setAffected(json_obj.optInt("affected") + "");
                    if (json_obj.has("user_image"))
                        complaintDetailData.setUser_image(json_obj.optString("user_image"));
                    else
                        complaintDetailData.setUser_image("http://icmycsaasqa.ichangemycity.com/android/account.png");

                    complaintDetailData.setComplaint_status_id(json_obj
                            .optString("complaint_status_id"));
                    complaintDetailData.setComplaint_status(json_obj
                            .optString("complaint_status"));
                    complaintDetailData.setRadius("" + json_obj.optInt("radius"));
                    if (json_obj.has("comments")) {
                        String comments = json_obj.optString("comments");
                        JSONArray commentsArray = new JSONArray(comments);
                        try {
                            for (int j = 0; j < commentsArray.length(); j++) {
                                JSONObject commentsJsonObject = commentsArray
                                        .getJSONObject(j);
                                CommentsData ccData = new CommentsData();
                                ccData.setComment_id(commentsJsonObject
                                        .optInt("id") + "");
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
                                    ccData.setSpanColorForCoplaintStatus(ParseComplaintData.getSpanColorForStatusTitle(
                                        Integer
                                            .parseInt(ccData
                                                    .getComment_complaint_status_id())));
                                } catch (NumberFormatException w) {
                                    ccData.setSpanColorForCoplaintStatus("#00000000");
                                }
                                AppController.commentData.add(ccData);
                            }
                            complaintDetailData.setCommentsData(AppController.commentData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (json_obj.has("voted_up_users")) {
                        String voted_up_users = json_obj
                                .optString("voted_up_users");
                        JSONArray votedUpJsonArray = new JSONArray(voted_up_users);
                        for (int m = 0; m < votedUpJsonArray.length(); m++) {
                            JSONObject voted_up_usersJsonObject = votedUpJsonArray
                                    .getJSONObject(m);
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
                        }
                        complaintDetailData.setVotedUpData(AppController.votedUpData);
                    }
                    if (json_obj.has("feedback_count")) {
                        String feedback_count = json_obj
                                .optString("feedback_count");
                        JSONObject feedback = new JSONObject(feedback_count);
                        complaintDetailData.setFeedback_count(true);
                        complaintDetailData.setNeutral(feedback.optInt("neutral") + "");
                        complaintDetailData.setSatisfaction(feedback.optInt("satisfaction") + "");
                        complaintDetailData.setUn_satisfied(feedback.optInt("un_satisfied") + "");
                    } else {
                        complaintDetailData.setFeedback_count(false);
                        complaintDetailData.setNeutral("0");
                        complaintDetailData.setSatisfaction("0");
                        complaintDetailData.setUn_satisfied("0");
                    }

                    // IChangeMyCity.cData = new ComplaintDetailData();
                    AppController.selectedComplaintData = complaintDetailData;
                    // IChangeMyCity.selectedComplaintData.setAffected(cData
                    // .getAffected());
                    // IChangeMyCity.selectedComplaintData
                    // .setComplaint_status_id(complaintDetailData.getComplaint_status_id());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return complaintDetailData;

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return complaintDetailData;
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

    //dummy
    public void setupViewPager() {
        adapters = new ViewPagerAdapter(getSupportFragmentManager());
        adapters.addFrag(new CommentsFragment(), activity.getResources().getString(R.string.comment) + " (" + complaintDetailData.getComment_count()
                + ")");
        adapters.addFrag(new VoteupFragment(), activity.getResources().getString(R.string.vote_up) + " (" + complaintDetailData.getVote_up_count() +
                ")");

        viewPager.setAdapter(adapters);
        viewPager.setOffscreenPageLimit(2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewPager.setNestedScrollingEnabled(true);
        }
        adapters.notifyDataSetChanged();
        viewPager.invalidate();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.reMeasureCurrentPage(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            mFragmentList.clear();
            mFragmentTitleList.clear();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
