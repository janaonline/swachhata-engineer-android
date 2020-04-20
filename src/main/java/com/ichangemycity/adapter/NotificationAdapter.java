package com.ichangemycity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.callback.OnResponseListener;
import com.ichangemycity.customui.RoundedBackgroundSpan;
import com.ichangemycity.model.NotificationHeaderData;
import com.ichangemycity.swachhbharatengineer.ComplaintDetail;
import com.ichangemycity.swachhbharatengineer.NotificationActivity;
import com.ichangemycity.swachhbharatengineer.R;
import com.ichangemycity.webservice.URLData;
import com.ichangemycity.webservice.WebserviceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
    TextView title;
    ImageView user_image;

    public ViewHolder(View convertView, int type) {
      super(convertView);
      if (type == TYPE_ITEM) {
        title = convertView.findViewById(R.id.tv_username);
        user_image = convertView
            .findViewById(R.id.user_image);
        rl_top_cc = convertView
            .findViewById(R.id.rl_top_cc);


      } else if (type == TYPE_HEADER) {
        tv_feed = convertView.findViewById(R.id.tv_feed);
        tv_feed_user_name = convertView
            .findViewById(R.id.tv_feed_user_name);
        rl_top_feed = convertView
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
      if (nData.getTextMsg().contains("RE-OPENED")) {
        searchFor("RE-OPENED", activity.getResources()
                .getColor(R.color.red_reopn_open), nData.getTextMsg(),
            v.title);
      } else if (nData.getTextMsg().contains("RESOLVED")) {
        searchFor("RESOLVED", activity.getResources()
                .getColor(R.color.green_resolved), nData.getTextMsg(),
            v.title);
      } else if (nData.getTextMsg().contains("On-THE-JOB")) {
        searchFor("On-THE-JOB", activity.getResources()
                .getColor(R.color.blue_on_the_job), nData.getTextMsg(),
            v.title);
      } else {
        v.title.setText(nData.getTextMsg());
      }

      v.rl_top_cc.setTag(nData);
      try {
        v.user_image.setColorFilter(Color.WHITE);
        v.user_image.setImageResource(nData.getImageIcon());

      } catch (Exception e) {
        v.user_image
            .setImageResource(R.mipmap.ic_notifications_active_white_48dp);
      }

      v.rl_top_cc.setOnClickListener(m -> {
        NotificationHeaderData notifData = (NotificationHeaderData) v.rl_top_cc
            .getTag();
//                    if (!notifData.isRead()) {
        markAsRead(notifData);
//                    } else {
//                        // redirectToAppropriateScreens(nData);
//                    }

      });

    }
  }

  private void markAsRead(final NotificationHeaderData nData) {
    AppController.showProgressDialog(activity);
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("apiKey", URLData.API_KEY);
    params.put("notificationId", Integer
        .toString(nData.getNotificationId()));
    final String url = URLData.BASE_URL
        + URLData.NOTIFICATION_STATUS_READ;
    new WebserviceHelper(activity, WebserviceHelper.METHOD_PUT, url, params,
        new OnResponseListener() {
          @Override
          public void OnResponseFailure() {
            AppController.hideProgressDialog(activity);
            //  AppController.handleVolleyError(activity, (RelativeLayout) activity.findViewById(R.id.parentLayout), error);
            try {
              redirectToAppropriateScreens(nData);
              AppController.hideProgressDialog(activity);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          @Override
          public void OnResponseSuccess(JSONObject response) {
            // JSONObject responseJsonObject;
            try {
              AppController.hideProgressDialog(activity);
              //  responseJsonObject = new JSONObject(response);
              int index = data.indexOf(nData);
              nData.setRead(true);
              data.set(index, nData);
              NotificationActivity.data.set(index, nData);
              AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO,
                  response.get("message").toString());
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
        }, false, WebserviceHelper.HEADER_TYPE_NORMAL);

  }

  private void redirectToAppropriateScreens(NotificationHeaderData notifData) {
    String redirectTo = notifData.getRedirectTo();
    AppController.selectedComplaintData.setComplaintId(notifData
        .getContentId() + "");
    activity.startActivity(new Intent(activity, ComplaintDetail.class));

  }

  private Spannable searchFor(String text, int bg,
      String descr, final TextView title) {
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
