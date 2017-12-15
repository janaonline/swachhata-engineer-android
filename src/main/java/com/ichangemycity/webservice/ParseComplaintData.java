package com.ichangemycity.webservice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.model.ComplaintData;
import com.ichangemycity.swachhbharatengineer.R;
import com.prashantsolanki.secureprefmanager.SecurePrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.ACTION_VIEW;


/**
 * Created by pattabi.raman on 07-10-2017.
 */

public class ParseComplaintData {

    public static ArrayList<ComplaintData> getParsedComplaintData(final JSONArray json_comp_array) {

        ArrayList<ComplaintData> data = new ArrayList<ComplaintData>();
        try {
            for (int i = 0; i < json_comp_array.length(); i++) {
                JSONObject json_obj = json_comp_array.getJSONObject(i);
                ComplaintData cData = ComplaintData.getInstance();
                cData.setComplaintId(json_obj.getInt("complaintId") + "");
                cData.setGeneric_id(json_obj.getString("generic_id"));
                cData.setCity_id(json_obj.getInt("city_id") + "");
                cData.setCreated_at(json_obj.getString("created_at"));
                cData.setUser_id(json_obj.getInt("user_id") + "");
                cData.setCategory_id(json_obj.getInt("category_id") + "");
                cData.setVote_up_count(json_obj.getInt("voted_count") + "");
                cData.setComment_count(json_obj.getInt("commented_count")
                        + "");
                cData.setComplaint_url(json_obj.getString("complaint_url"));
                cData.setAffected(json_obj.get("affected").toString());
                cData.setCategory_name(json_obj.getString("category_name"));
                if (json_obj.has("complaint_image"))
                    cData.setComplaint_image(json_obj
                            .getString("complaint_image"));
                else
                    cData.setComplaint_image("http://icmycsaasqa.ichangemycity.com/android/garbage.jpg");

                cData.setComplaint_image_l1(json_obj
                        .getString("complaint_image_l1"));
                cData.setComplaint_image_l1(json_obj
                        .getString("complaint_image_l2"));

                if (json_obj.has("complaint_image_height"))
                    cData.setComplaint_image_height(json_obj
                            .getInt("complaint_image_height") + "");
                else
                    cData.setComplaint_image_height(300 + "");

                cData.setLocation(json_obj.getString("location"));
                cData.setLatitude(json_obj.get("latitude").toString());
                cData.setLongitude(json_obj.get("longitude").toString());

                if (json_obj.has("landmark"))
                    cData.setLandmark(json_obj.getString("landmark"));
                else
                    cData.setLandmark("Landmark missing in web service");
                cData.setParent_id(json_obj.getString("parent_id"));
                cData.setFull_name(json_obj.getString("full_name"));
                if (json_obj.has("user_image"))
                    cData.setUser_image(json_obj.getString("user_image"));
                else
                    cData.setUser_image("http://icmycsaasqa.ichangemycity.com/android/account.png");

                cData.setComplaint_status_id(json_obj
                        .getString("complaint_status_id"));
                cData.setComplaint_status(json_obj
                        .getString("complaint_status"));
                cData.setRadius("" + json_obj.getInt("radius"));
                if (json_obj.has("feed")) {
                    String feed = json_obj.getString("feed");
                    try {
                        cData.setHasFeed(true);
                        JSONObject mComplaintFeedJsonObject = new JSONObject(
                                feed);
                        cData.setFeed_id(mComplaintFeedJsonObject
                                .getString("feed_id"));
                        // cData.setFeed_user_id(mComplaintFeedJsonObject
                        // .getString("feed_user_id"));
                        cData.setFeed_description(mComplaintFeedJsonObject
                                .getString("feed_description"));
                        cData.setFeed_full_name(""/*
												 * mComplaintFeedJsonObject
												 * .getString
												 * ("feed_user_full_name")
												 */);
                        cData.setFeed_color(mComplaintFeedJsonObject
                                .getString("feed_color"));
                        cData.set_is_feed_high_priority(mComplaintFeedJsonObject
                                .get("is_feed_high_priority").toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        cData.setHasFeed(false);
                    }
                } else {
                    cData.setHasFeed(false);
                }
                if (json_obj.has("feedback_count")) {
                    String feedback_count = json_obj
                            .getString("feedback_count");
                    JSONObject feedback = new JSONObject(feedback_count);
                    cData.setFeedback_count(true);
                    cData.setNeutral(feedback.getInt("neutral") + "");
                    cData.setSatisfaction(feedback.getInt("satisfaction") + "");
                    cData.setUn_satisfied(feedback.getInt("un_satisfied") + "");
                } else {
                    cData.setFeedback_count(false);
                    cData.setNeutral("0");
                    cData.setSatisfaction("0");
                    cData.setUn_satisfied("0");
                }
                // if (!complaintId.contains(json_obj
                // .getString("complaintId"))) {
                data.add(cData);
                // }

                // }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;

    }

    public static void setImage(final Activity activity, final CircleImageView circleImageView, final NetworkImageView imageView, final String
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
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent toPreview = new Intent(ACTION_VIEW, Uri.parse(imageUrl));
                            activity.startActivity(toPreview);
                        }
                    });
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    imageView.setImageResource(R.mipmap.ic_not_found);

                }
            });
        }
    }

    public static String getSpanColorForStatusTitle(final Activity activity, final int statusId) {
//        Log.i("getSpanColor", "--------------->" + statusId + "");
        try {
            if (statusId == AppController.COMPLAINT_OPEN|| statusId == AppController.COMPLAINT_REOPEN) {
//                return Color.argb(1, 213, 0, 0);
                return ("#D50000");
            } else if (statusId == AppController.COMPLAINT_ON_THE_JOB) {
//                return Color.argb(1, 43, 181, 249);
                return ("#2BB5F9");
            } else if (statusId == AppController.COMPLAINT_RESOLVED) {
//                return Color.argb(0, 189, 0, 1);
                return ("#00BD00");
            } else {
                return ("#607D8B");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ("#607D8B");
    }

    public static int setBgDrawableForComplaintStatus(final Activity activity, final ComplaintData cData,
                                                      final TextView complaintStatusTextView) {
        String ComplaintStatusID = cData.getComplaint_status_id();
        int complaintStatusBgDrawable = Integer.parseInt(ComplaintStatusID);
        int complaintStatusTextColor = Color.BLACK;
        switch (complaintStatusBgDrawable) {
            case AppController.COMPLAINT_REOPEN:
                complaintStatusBgDrawable = R.drawable.complaint_status_red;
                complaintStatusTextColor = activity.getResources().getColor(
                        R.color.red_reopn_open);
                break;
            case AppController.COMPLAINT_OPEN:
                complaintStatusBgDrawable = R.drawable.complaint_status_red;
                complaintStatusTextColor = activity.getResources().getColor(
                        R.color.red_reopn_open);
                break;
            case AppController.COMPLAINT_ON_THE_JOB:
                complaintStatusBgDrawable = R.drawable.complaint_status_on_the_job;
                complaintStatusTextColor = activity.getResources().getColor(
                        R.color.blue_on_the_job);
                break;
            case AppController.COMPLAINT_RESOLVED:
                complaintStatusBgDrawable = R.drawable.complaint_status_resolved;
                complaintStatusTextColor = activity.getResources().getColor(
                        R.color.green_resolved);
                break;
            case AppController.COMPLAINT_REJECTED:
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
    public static void shareComplaint(Activity activity, ComplaintData cdata) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.putExtra(Intent.EXTRA_SUBJECT,
                    activity.getResources().getString(R.string.app_name));
            String sAux = SecurePrefManager.with(activity).get(ICMyCPreferenceData.user_full_name).defaultValue("").go()
                    + " shared a complaint with you.\n\n";
            sAux = sAux + cdata.getComplaint_url();
            if (SecurePrefManager.with(activity).get(ICMyCPreferenceData.shareImage).defaultValue("").go().trim().length() == 0) {
                i.setType("text/plain");
            } else {
                i.setType("image/jpeg");
                i.putExtra(
                        Intent.EXTRA_STREAM,
                        Uri.parse(SecurePrefManager.with(activity).get(ICMyCPreferenceData.shareImage).defaultValue("").go()));
            }
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            activity.startActivity(Intent.createChooser(i, "Share"));
            SecurePrefManager.with(activity).set(ICMyCPreferenceData.shareImage).value("").go();
        } catch (Exception e) { // e.toString();
        }
    }
}
