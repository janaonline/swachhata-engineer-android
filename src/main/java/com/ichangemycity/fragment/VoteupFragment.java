package com.ichangemycity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ichangemycity.adapter.VoteupsAdapter;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.swachhbharatengineer.ComplaintDetail;
import com.ichangemycity.swachhbharatengineer.R;
import com.ichangemycity.swachhbharatengineer.VoteupsActivity;
import com.jude.easyrecyclerview.EasyRecyclerView;

/**
 * Created by pattabi.raman on 20-10-2017.
 */

public class VoteupFragment extends Fragment {

    public static Activity activity;
    View v;
    LinearLayout layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.assignLanguage(ComplaintDetail.activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.comments_fragment, null);
        activity = getActivity();
        layout = (LinearLayout) v.findViewById(R.id.layoutData);
        addRemark();
        return v;
    }

    EasyRecyclerView mAdd_remarkrecyclerview;

    private void addRemark() {
        AppController.votedUpData = AppController.selectedComplaintData.getVotedUpData();
        final VoteupsAdapter commentsAdapter = new VoteupsAdapter(activity, AppController.votedUpData, true);
        mAdd_remarkrecyclerview = (EasyRecyclerView) v.findViewById(R.id.mRecyclerview);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(activity);
        mAdd_remarkrecyclerview.setLayoutManager(manager);
        mAdd_remarkrecyclerview.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));
        mAdd_remarkrecyclerview.setItemAnimator(new DefaultItemAnimator());
        mAdd_remarkrecyclerview.setAdapter(commentsAdapter);
        AppController.setEmptyViewForRecyclerViewFragments(ComplaintDetail.activity,mAdd_remarkrecyclerview, (TextView)v.findViewById(R.id.viewEmpty));

        if (AppController.selectedComplaintData.getVotedUpData().size() > 5) {
            try {
                ((TextView) v.findViewById(R.id.viewEmpty)).setVisibility(View.GONE);
            } catch (Exception e) {
            }
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View loadMore = inflater.inflate(R.layout.inflate_loadmore, null);
            ((Button) loadMore.findViewById(R.id.loadmore)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, VoteupsActivity.class));
                }
            });
            layout.addView(loadMore);


        }


    }
}
