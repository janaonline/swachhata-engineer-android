package com.ichangemycity.adapter;

/**
 * Created by pattabi.raman on 24-07-2017.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.ichangemycity.model.VotedUpData;
import com.ichangemycity.swachhbharatengineer.R;
import com.ichangemycity.webservice.ParseComplaintData;

import java.util.ArrayList;

public class VoteupsAdapter extends RecyclerView.Adapter<VoteupsAdapter.AddRemarkViewHolder> {

    private  ArrayList<VotedUpData>  arrayList;
    private Activity activity;
    private boolean isToShowLoadMore;

    public VoteupsAdapter(Activity mAct, ArrayList<VotedUpData> arrayList, boolean isToShowLoadMore) {
        activity = mAct;
       this.arrayList = arrayList;
        this.isToShowLoadMore = isToShowLoadMore;
    }

    @Override
    public AddRemarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_comment, parent, false);
        return new AddRemarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddRemarkViewHolder holder, int position) {
        VotedUpData voteupData = arrayList.get(position);

        holder.mDescription.setText(voteupData.getComplaint_count());
        holder.mName.setText(voteupData.getFull_name());
        holder.postedOn.setText(voteupData.getVoted_up_on());
        holder.imageLayout.setVisibility(View.GONE);
        ParseComplaintData.setImage(activity, holder.mUserImage, null, voteupData.getUser_image_url(), true);
    }

    @Override
    public int getItemCount() {
        if (isToShowLoadMore) {
            return arrayList.size() > 5 ? 5 : arrayList.size();
        }
        return arrayList.size();
    }

    class AddRemarkViewHolder extends RecyclerView.ViewHolder {
        private TextView mName, mDescription, postedOn;
        private de.hdodenhof.circleimageview.CircleImageView mUserImage;
        private HorizontalScrollView imageLayout;

        public AddRemarkViewHolder(View itemView) {
            super(itemView);
            mDescription = (TextView) itemView.findViewById(R.id.description_count);
            mName = (TextView) itemView.findViewById(R.id.userName);
            postedOn = (TextView) itemView.findViewById(R.id.postedOn);
            mUserImage = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.userImage);
            imageLayout = (HorizontalScrollView) itemView.findViewById(R.id.imageLayout);

        }
    }
}
