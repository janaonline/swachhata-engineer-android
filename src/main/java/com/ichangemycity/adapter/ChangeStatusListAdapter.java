package com.ichangemycity.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ichangemycity.appdata.AppController;
import com.ichangemycity.model.ChangeStatusListData;
import com.ichangemycity.swachhbharatengineer.R;

public class ChangeStatusListAdapter extends BaseAdapter {
    ArrayList<ChangeStatusListData> changeStatusListData = new ArrayList<ChangeStatusListData>();
    Activity activity;
    LayoutInflater inflater;

    public ChangeStatusListAdapter(Activity activity,
            final ArrayList<ChangeStatusListData> changeStatusListData) {
        this.changeStatusListData = changeStatusListData;
        this.activity = activity;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return changeStatusListData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return changeStatusListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChangeStatusListData cData = changeStatusListData.get(position);
        if (convertView == null)
            convertView = inflater
                    .inflate(R.layout.inflate_change_status, null);
        final TextView textData;

        textData = (TextView) convertView.findViewById(android.R.id.text1);
        textData.setText(cData.getStatus());

        convertView.setTag(cData);
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                activity.startActivity(new Intent(activity,
//                        CommentsActivity.class));
                AppController.selectedComplaintData.setToChangeStatus(true);
            }
        });
        return convertView;
    }

    public int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
