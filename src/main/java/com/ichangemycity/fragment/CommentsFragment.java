package com.ichangemycity.fragment;

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

import com.ichangemycity.adapter.CommentsAdapter;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.swachhbharatengineer.ComplaintDetail;
import com.ichangemycity.swachhbharatengineer.R;
import com.jude.easyrecyclerview.EasyRecyclerView;

public class CommentsFragment extends Fragment {
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
        layout = (LinearLayout)v.findViewById(R.id.layoutData);
        addRemark();
        return v;
    }


    EasyRecyclerView mRecyclerview;

    private void addRemark() {
        final CommentsAdapter commentsAdapter = new CommentsAdapter(ComplaintDetail.activity , true);
        mRecyclerview = (EasyRecyclerView) v.findViewById(R.id.mRecyclerview);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(ComplaintDetail.activity);
        mRecyclerview.setLayoutManager(manager);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(ComplaintDetail.activity, LinearLayoutManager.VERTICAL));
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.setAdapter(commentsAdapter);
        AppController.setEmptyViewForRecyclerViewFragments(ComplaintDetail.activity,mRecyclerview, (TextView)v.findViewById(R.id.viewEmpty));

        if (Integer.parseInt(AppController.selectedComplaintData.getComment_count()) > 5) {
            try {
                ((TextView) v.findViewById(R.id.viewEmpty)).setVisibility(View.GONE);
            } catch (Exception e) {
            }
            LayoutInflater inflater = (LayoutInflater) ComplaintDetail.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View loadMore = inflater.inflate(R.layout.inflate_loadmore, null);
            ((Button)loadMore.findViewById(R.id.loadmore)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivity(new Intent(ComplaintDetail.activity, CommentsActivity.class));
                }
            });
            layout.addView(loadMore);

        }


    }

}
