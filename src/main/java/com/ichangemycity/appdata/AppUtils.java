package com.ichangemycity.appdata;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ichangemycity.model.ComplaintData;
import com.ichangemycity.swachhbharatengineer.R;
import com.prashantsolanki.secureprefmanager.SecurePrefManager;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pattabi.raman on 03-10-2017.
 */

public class AppUtils {
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
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    imageView.setImageResource(R.mipmap.ic_not_found);

                }
            });


        }

    }

    public static int setBgDrawableForComplaintStatus(Activity activity, final ComplaintData cData,
                                                      final TextView complaintStatusTextView) {
        String ComplaintStatusID = cData.getComplaint_status_id();
        int complaintStatusTextColor = Color.BLACK;
        int complaintStatusBgDrawable = R.drawable.complaint_status_closed;
        if (ComplaintStatusID != null) {
            complaintStatusBgDrawable = Integer.parseInt(ComplaintStatusID);
            switch (complaintStatusBgDrawable) {
                case AppController.COMPLAINT_REOPEN:
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
            complaintStatusTextView.setBackgroundResource(complaintStatusBgDrawable);
            return complaintStatusBgDrawable;
        } else {
            complaintStatusTextView.setTextColor(complaintStatusTextColor);
            complaintStatusTextView.setText(cData.getComplaint_status());
            complaintStatusTextView.setBackgroundResource(complaintStatusBgDrawable);
            return complaintStatusBgDrawable;
        }

    }

    public static String getSpanColorForStatusTitle(final Activity activity, final int statusId) {
//        Log.i("getSpanColor", "--------------->" + statusId + "");
        try {
            if (statusId == AppController.COMPLAINT_OPEN || statusId == AppController
                    .COMPLAINT_REOPEN) {
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
