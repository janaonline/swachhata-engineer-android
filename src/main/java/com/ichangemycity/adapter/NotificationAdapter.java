package com.ichangemycity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.customui.RoundedBackgroundSpan;
import com.ichangemycity.model.NotificationHeaderData;
import com.ichangemycity.swachhbharatengineer.ComplaintDetail;
import com.ichangemycity.swachhbharatengineer.NotificationActivity;
import com.ichangemycity.swachhbharatengineer.R;
import com.ichangemycity.webservice.URLData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ichangemycity.appdata.AppController.TAG;

public class NotificationAdapter extends
        RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private static Activity activity;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_HEADER = 0;

    private static ArrayList<NotificationHeaderData> data = new ArrayList<>();

    public NotificationAdapter(final Activity activity,
                               final ArrayList<NotificationHeaderData> data1) {
        NotificationAdapter.activity = activity;
        this.data.clear();
        this.data.addAll(data1);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;
        if (i == TYPE_HEADER) {
            v = LayoutInflater.from(activity).inflate(
                    R.layout.inflate_notification_card, null, false);
        } else if (i == TYPE_ITEM) {
            v = LayoutInflater.from(activity).inflate(
                    R.layout.inflate_card_relative_layout, null, false);
        }
        return new ViewHolder(v, i);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return data.get(position).getTYPE_ITEM();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_feed_user_name, tv_feed;
        RelativeLayout rl_top_feed, rl_top_cc;
        LinearLayout notification;
        View viewLine;
        TextView title;
        ImageView user_image;

        public ViewHolder(View convertView, int type) {
            super(convertView);
            if (type == TYPE_ITEM) {
                title = (TextView) convertView.findViewById(R.id.tv_username);
                user_image = (ImageView) convertView
                        .findViewById(R.id.user_image);
                rl_top_cc = (RelativeLayout) convertView
                        .findViewById(R.id.rl_top_cc);
                viewLine = (View) convertView.findViewById(R.id.view);

            } else if (type == TYPE_HEADER) {
                tv_feed = (TextView) convertView.findViewById(R.id.tv_feed);
                tv_feed_user_name = (TextView) convertView
                        .findViewById(R.id.tv_feed_user_name);
                rl_top_feed = (RelativeLayout) convertView
                        .findViewById(R.id.rl_top_feed);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder v, final int arg1) {
        final NotificationHeaderData nData = data.get(arg1);
        if (nData.getTYPE_ITEM() == 0) { // header
            v.tv_feed.setText(nData.getDateValue());
            v.tv_feed_user_name.setText(nData.getHeaderTitle());
            v.rl_top_feed.setTag(nData);
        } else { // item
            if (nData.isRead()) {
                v.rl_top_cc.setBackground(activity.getResources().getDrawable(
                        R.drawable.notif_selected_reverse));
            } else {
                v.rl_top_cc.setBackground(activity.getResources().getDrawable(
                        R.drawable.notif_item_click));
            }
            if (nData.getTextMsg().contains("RE-OPENED"))
                searchFor("RE-OPENED", Color.WHITE, activity.getResources()
                                .getColor(R.color.red_reopn_open), nData.getTextMsg(),
                        v.title);
            else if (nData.getTextMsg().contains("RESOLVED"))
                searchFor("RESOLVED", Color.WHITE, activity.getResources()
                                .getColor(R.color.green_resolved), nData.getTextMsg(),
                        v.title);
            else if (nData.getTextMsg().contains("On-THE-JOB"))
                searchFor("On-THE-JOB", Color.WHITE, activity.getResources()
                                .getColor(R.color.blue_on_the_job), nData.getTextMsg(),
                        v.title);
            else
                v.title.setText(nData.getTextMsg());

            v.rl_top_cc.setTag(nData);
            try {
                v.user_image.setColorFilter(Color.WHITE);
                v.user_image.setImageResource(nData.getImageIcon());

            } catch (Exception e) {
                v.user_image
                        .setImageResource(R.mipmap.ic_notifications_active_white_48dp);
            }

            v.rl_top_cc.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View m) {
                    NotificationHeaderData notifData = (NotificationHeaderData) v.rl_top_cc
                            .getTag();
//                    if (!notifData.isRead()) {
                    markAsRead(notifData);
//                    } else {
//                        // redirectToAppropriateScreens(nData);
//                    }

                }

            });

        }
    }

    private void markAsRead(final NotificationHeaderData nData) {
        AppController.showProgressDialog(activity, activity.getResources().getString(R.string.loading));
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URLData.BASE_URL
                + URLData.NOTIFICATION_STATUS_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject responseJsonObject = null;
                        try {
                            AppController.hideProgressDialog(activity);
                            responseJsonObject = new JSONObject(response);
                            int index = data.indexOf(nData);
                            nData.setRead(true);
                            data.set(index, nData);
                            NotificationActivity.data.set(index, nData);
                            AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO,responseJsonObject.get("message").toString());
                            // notifyItemChanged(index);
                            // Toast.makeText(
                            // activity,
                            // new JSONObject(response)
                            // .getString("message"),
                            // Toast.LENGTH_SHORT).show();
                            redirectToAppropriateScreens(nData);

//                            Toast.makeText(activity,
//                                    responseJsonObject.get("message").toString(),
//                                    Toast.LENGTH_LONG).show();

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
                        try {
                            redirectToAppropriateScreens(nData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("apiKey", URLData.API_KEY);
                params.put("notificationId", Integer
                        .toString(nData.getNotificationId()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return URLData.getHeaders(activity);
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppController.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);

    }

    private void redirectToAppropriateScreens(NotificationHeaderData notifData) {
        String redirectTo = notifData.getRedirectTo();
        AppController.selectedComplaintData.setComplaintId(notifData
                .getContentId() + "");
        activity.startActivity(new Intent(activity, ComplaintDetail.class));

    }

    private Spannable searchFor(String text, int foreground, int bg,
                                String descr, final TextView title) {
        foreground = bg;
        Spannable raw = new SpannableString(descr.replace(text, "  " + text
                + "  "));
        // BackgroundColorSpan[] spans = raw.getSpans(0, descr.length(),
        // BackgroundColorSpan.class);
        //
        // for (BackgroundColorSpan span : spans) {
        // raw.removeSpan(span);
        // }
        text = "  " + text + "  ";
        int index = TextUtils.indexOf(raw, text);

        // while (index >= 0) {
        // raw.setSpan(new BackgroundColorSpan(bg), index,
        // index + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // raw.setSpan(new ForegroundColorSpan(foreground), index, index
        // + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // index = TextUtils.indexOf(raw, text,
        // index + text.length());
        raw.setSpan(
                new RoundedBackgroundSpan(raw, index,
                        ((index) + text.length()), bg), (index),
                ((index) + text.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // }
        title.setText(raw);
        return raw;
    }

}
