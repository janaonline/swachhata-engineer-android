package com.ichangemycity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.model.CommentsData;

import com.ichangemycity.swachhbharatengineer.R;
import com.ichangemycity.webservice.ParseComplaintData;

/**
 * Created by srimadhu.s on 19-07-2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.AddRemarkViewHolder> {

//    private ComplaintData arrayList;
    public static Activity activity;
    private static Handler handler = new Handler();
    private static boolean isToShowLoadMore;
    float wt_px, ht_px, margin;

    public CommentsAdapter(Activity mAct , boolean isToShowLoadMore) {
        activity = mAct;
//        arrayList = complaintData;
        wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, activity
                .getResources().getDisplayMetrics());
        ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, activity
                .getResources().getDisplayMetrics());
        margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, activity
                .getResources().getDisplayMetrics());
        this.isToShowLoadMore = isToShowLoadMore;
    }

    @Override
    public AddRemarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_comment, parent, false);
        return new AddRemarkViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final AddRemarkViewHolder holder, int position) {

        final CommentsData commentData = AppController.selectedComplaintData.getCommentsData().get(position);
        String text = "<b><font color=" + commentData.getSpanColorForCoplaintStatus() + " >" + commentData.getComment_complaint_status()
                .toUpperCase()
                + "</font></b>" + "<font color=#212121>" + " " + commentData.getComment_description() + "</font>";
        holder.mDescription.setText(Html.fromHtml(text));
        holder.mName.setText(commentData.getComment_full_name());
        holder.mpostedOn.setText(commentData.getComment_posted_on());
        ParseComplaintData.setImage(activity, holder.mUserImage, null, commentData.getUser_image_url(), true);

        if( commentData.getComment_image_url().trim().length()<=0){
            holder.imageLinear.setVisibility(View.GONE);
        }else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        holder.imageLayout.setVisibility(View.VISIBLE);
                        holder.imageLinear.setVisibility(View.VISIBLE);
                        holder.imageLinear.removeAllViews();
                        String imgUrl = commentData.getComment_image_url();


                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                (int) wt_px, (int) ht_px);

                        layoutParams.setMargins(0, 0, (int) margin, 0);

                        final NetworkImageView image = new NetworkImageView(activity);
                        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        image.setLayoutParams(layoutParams);
//                            imageLoader.DisplayImage(imgUrl, R.drawable.pl, image);
                        ParseComplaintData.setImage(activity, null, image, imgUrl, false);
                        holder.imageLinear.setTag(imgUrl);
                        holder.mDescription.setTag(commentData);
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg1) {
                                CommentsData cData = (CommentsData) holder.mDescription.getTag();
                                String url = cData.getComment_image_url();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                activity.startActivity(i);
                            }
                        });
                        holder.imageLinear.addView(image);

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (isToShowLoadMore)
            return (AppController.selectedComplaintData.getCommentsData().size()>5)?5: AppController.selectedComplaintData.getCommentsData().size();
        else
            return AppController.selectedComplaintData.getCommentsData().size();
    }

    class AddRemarkViewHolder extends RecyclerView.ViewHolder {
        private TextView mName, mDescription, mpostedOn;
        private de.hdodenhof.circleimageview.CircleImageView mUserImage;
        private LinearLayout imageLinear;
        private HorizontalScrollView imageLayout;

        public AddRemarkViewHolder(View itemView, int viewType) {
            super(itemView);
            imageLinear = (LinearLayout) itemView
                    .findViewById(R.id.imageLinear);
            mDescription = (TextView) itemView.findViewById(R.id.description_count);
            mName = (TextView) itemView.findViewById(R.id.userName);
            mUserImage = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.userImage);
            mpostedOn = (TextView) itemView.findViewById(R.id.postedOn);
            imageLayout = (HorizontalScrollView) itemView.findViewById(R.id.imageLayout);
        }
    }
}
