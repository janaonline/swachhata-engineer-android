package com.ichangemycity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.model.ComplaintData;
import com.ichangemycity.swachhbharatengineer.ComplaintDetail;
import com.ichangemycity.swachhbharatengineer.MainActivity;
import com.ichangemycity.swachhbharatengineer.R;
import com.ichangemycity.webservice.ParseComplaintData;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeTabLocalFeedAdapter extends
        RecyclerView.Adapter<HomeTabLocalFeedAdapter.ViewHolder> {
    ArrayList<Integer> linecount = new ArrayList<Integer>();
    private static Activity activity;
    // ArrayList<ComplaintData> data = new ArrayList<ComplaintData>();

    String mSharedUserId = null;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public HomeTabLocalFeedAdapter(Activity activity ) {
        HomeTabLocalFeedAdapter.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;
        if (i == TYPE_HEADER) {
            v = LayoutInflater.from(activity).inflate(R.layout.inflate_primer,
                    null, false);
        } else if (i == TYPE_ITEM) {
            v = LayoutInflater.from(activity).inflate(
                    R.layout.home_complaint_card, null, false);
        }
        return new ViewHolder(v, i);
    }

    @Override
    public int getItemCount() {
        return MainActivity.data.size();
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        // if (position == 0)
        // return TYPE_HEADER;
        return TYPE_ITEM;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, complaint_status, created_on, complaint_category,
                complaint_description, comment, tv_feed_user_name, tv_feed,
                votedUpCount, commentedCount, share;
        ImageView image1, image2, image3;
        RelativeLayout rl_cc_top, rl_top_feed;
        NetworkImageView complaint_image ;
        ImageView feed_flag;
      // LinearLayout cta_btn, cta_feedback, card;
        LinearLayout resolved;
        TextView satisfaction, un_satisfied, neutral;
        View viewLine;
        Spinner changeStatus;
        FrameLayout frameSpinner;
        TextView textPostComplaint;
        LinearLayout card;
        CircleImageView user_image;

        public ViewHolder(View convertView, int type) {
            super(convertView);
            if (type == TYPE_ITEM) {
                title = (TextView) convertView.findViewById(R.id.tv_username);
                created_on = (TextView) convertView
                        .findViewById(R.id.created_on);
                complaint_status = (TextView) convertView
                        .findViewById(R.id.complaint_status);
                user_image = (CircleImageView) convertView
                        .findViewById(R.id.user_image);
                complaint_image = (NetworkImageView) convertView
                        .findViewById(R.id.complaint_image);
                complaint_category = (TextView) convertView
                        .findViewById(R.id.complaint_category);
                complaint_description = (TextView) convertView
                        .findViewById(R.id.complaint_description);
                comment = (TextView) convertView.findViewById(R.id.comment);
                tv_feed = (TextView) convertView.findViewById(R.id.tv_feed);
                tv_feed_user_name = (TextView) convertView
                        .findViewById(R.id.tv_feed_user_name);
                votedUpCount = (TextView) convertView
                        .findViewById(R.id.votedUpCount);
                commentedCount = (TextView) convertView
                        .findViewById(R.id.commentedCount);
                rl_cc_top = (RelativeLayout) convertView
                        .findViewById(R.id.rl_cc_top);

                // cta_btn = (LinearLayout) convertView
                // .findViewById(R.id.not_resolved);
                resolved = (LinearLayout) convertView
                        .findViewById(R.id.resolved);
                satisfaction = (TextView) convertView
                        .findViewById(R.id.satisfaction);
                un_satisfied = (TextView) convertView
                        .findViewById(R.id.un_satisfied);
                neutral = (TextView) convertView.findViewById(R.id.neutral);
                viewLine = (View) convertView.findViewById(R.id.view);
                card = (LinearLayout) convertView.findViewById(R.id.card);
                changeStatus = (Spinner) convertView
                        .findViewById(R.id.changeStatus);
                frameSpinner = (FrameLayout) convertView
                        .findViewById(R.id.frameSpinner);
                rl_top_feed = (RelativeLayout) convertView
                        .findViewById(R.id.rl_top_feed);
                feed_flag = (ImageView) convertView
                        .findViewById(R.id.feed_flag);
                share = (TextView) convertView.findViewById(R.id.share);
            } else if (type == TYPE_HEADER) {
                textPostComplaint = (TextView) convertView
                        .findViewById(R.id.textPostComplaint);

            }
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder v, final int arg1) {
        // if (arg1 == 0) {
        // v.textPostComplaint.setText("All Complaints");
        // } else {
        final ComplaintData cData = MainActivity.data.get(arg1);
        v.title.setText(cData.getFull_name());
        v.created_on.setText(cData.getCreated_at());
        v.complaint_status.setText(cData.getComplaint_status());
        ParseComplaintData.setImage(activity, v.user_image, null, cData.getUser_image(), true);
        v.complaint_image.requestLayout();
        v.complaint_category.setText(cData.getCategory_name());
        v.complaint_description.setText(cData.getLocation());

        setBgDrawableForComplaintStatus(cData, v.complaint_status);

        v.votedUpCount.setText(cData.getVote_up_count()
                + activity.getResources().getString(R.string._votes) + "");
        v.commentedCount.setText(cData.getComment_count()
                + activity.getResources().getString(R.string._comments) + "");
        v.rl_cc_top.setTag(cData);
        v.card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View m) {
                // TODO Auto-generated method stub
                ComplaintData mCData = (ComplaintData) v.rl_cc_top.getTag();
                AppController.selectedComplaintData = mCData;
                Intent toComplaintDetail = new Intent(activity,
                        ComplaintDetail.class);
                activity.startActivity(toComplaintDetail);

            }
        });

        v.complaint_image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View m) {
                ComplaintData mCData = (ComplaintData) v.rl_cc_top.getTag();
                AppController.selectedComplaintData = mCData;
//                Intent toComplaintDetail = new Intent(activity,
//                        ComplaintDetail.class);
//                activity.startActivity(toComplaintDetail);
            }
        });
        v.comment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View m) {
                // TODO Auto-generated method stub
                ComplaintData mCData = (ComplaintData) v.rl_cc_top.getTag();
                AppController.selectedComplaintData = mCData;
                AppController.selectedComplaintData.setToChangeStatus(false);
//                Intent toCommentsActivity = new Intent(activity,
//                        CommentsActivity.class);
//                activity.startActivity(toCommentsActivity);
            }
        });
        v.share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View m) {
                ComplaintData mCData = (ComplaintData) v.rl_cc_top.getTag();
                ParseComplaintData.shareComplaint(activity, mCData);
            }
        });
        if (Integer.parseInt(cData.getComplaint_status_id()) == AppController.COMPLAINT_REJECTED) {
            v.rl_top_feed.setVisibility(View.GONE);
        } else {
            v.rl_top_feed.setVisibility(View.VISIBLE);
            v.tv_feed.setText(cData.getFeed_description());
            v.tv_feed_user_name.setText(cData.getFeed_full_name());
            setColor(cData, v.rl_top_feed, v.tv_feed, v.tv_feed_user_name,
                    v.feed_flag);
        }

        ParseComplaintData.setImage(activity, null,v.complaint_image, cData.getComplaint_image(),false);
        AppController.customizeChangeStatusDropdown(activity, cData, v.resolved,
                v.changeStatus, v.neutral, v.satisfaction, v.un_satisfied,
                v.frameSpinner);
        // }
    }

    private void setColor(final ComplaintData cData,
                          final RelativeLayout rl_top_feed, final TextView tv_feed,
                          final TextView tv_feed_user_name, ImageView feed_flag) {
        if (cData.isHasFeed()) {
            rl_top_feed.setVisibility(View.VISIBLE);
            if (cData.get_is_feed_high_priority().equalsIgnoreCase("1")) {
                feed_flag.setVisibility(View.VISIBLE);
                feed_flag.setColorFilter(activity.getResources().getColor(
                        R.color.red_reopn_open));
            } else {
                feed_flag.setVisibility(View.GONE);
            }

            if (cData.getFeed_color().equalsIgnoreCase("R")) {
                tv_feed_user_name.setTextColor(activity.getResources()
                        .getColor(R.color.red_reopn_open));
                tv_feed.setTextColor(activity.getResources().getColor(
                        R.color.red_reopn_open));
            } else if (cData.getFeed_color().equalsIgnoreCase("G")) {
                tv_feed_user_name.setTextColor(activity.getResources()
                        .getColor(R.color.green_resolved));
                tv_feed.setTextColor(activity.getResources().getColor(
                        R.color.green_resolved));
            } else if (cData.getFeed_color().equalsIgnoreCase("B")) {
                tv_feed_user_name.setTextColor(activity.getResources()
                        .getColor(R.color.blue_on_the_job));
                tv_feed.setTextColor(activity.getResources().getColor(
                        R.color.blue_on_the_job));
            } else {
                tv_feed_user_name.setTextColor(activity.getResources()
                        .getColor(R.color.black));
                tv_feed.setTextColor(activity.getResources().getColor(
                        R.color.black));
            }
        } else {
            rl_top_feed.setVisibility(View.GONE);
        }
    }

    public int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    private final int COMPLAINT_OPEN = 1;
    private final int COMPLAINT_ON_THE_JOB = 3;
    private final int COMPLAINT_RESOLVED = 4;
    private final int COMPLAINT_REOPEN = 5;
    private final int COMPLAINT_REJECTED = 6;

    private int setBgDrawableForComplaintStatus(final ComplaintData cData,
                                                final TextView complaintStatusTextView) {
        String ComplaintStatusID = cData.getComplaint_status_id();
        int complaintStatusBgDrawable = Integer.parseInt(ComplaintStatusID);
        int complaintStatusTextColor = Color.BLACK;
        switch (complaintStatusBgDrawable) {
            case COMPLAINT_REOPEN:
                complaintStatusBgDrawable = R.drawable.complaint_status_red;
                complaintStatusTextColor = activity.getResources().getColor(
                        R.color.red_reopn_open);
                break;
            case COMPLAINT_OPEN:
                complaintStatusBgDrawable = R.drawable.complaint_status_red;
                complaintStatusTextColor = activity.getResources().getColor(
                        R.color.red_reopn_open);
                break;
            case COMPLAINT_ON_THE_JOB:
                complaintStatusBgDrawable = R.drawable.complaint_status_on_the_job;
                complaintStatusTextColor = activity.getResources().getColor(
                        R.color.blue_on_the_job);
                break;
            case COMPLAINT_RESOLVED:
                complaintStatusBgDrawable = R.drawable.complaint_status_resolved;
                complaintStatusTextColor = activity.getResources().getColor(
                        R.color.green_resolved);
                break;
            case COMPLAINT_REJECTED:
                complaintStatusBgDrawable = R.drawable.complaint_status_closed;
                complaintStatusTextColor = activity.getResources().getColor(
                        R.color.gray_closed);
                break;
            default:
                complaintStatusBgDrawable = R.drawable.complaint_status_closed;
                complaintStatusTextColor = activity.getResources().getColor(
                        R.color.gray_closed);
                break;
        }
        complaintStatusTextView.setTextColor(complaintStatusTextColor);
        complaintStatusTextView.setText(cData.getComplaint_status());
        complaintStatusTextView
                .setBackgroundResource(complaintStatusBgDrawable);
        return complaintStatusBgDrawable;

    }

}
