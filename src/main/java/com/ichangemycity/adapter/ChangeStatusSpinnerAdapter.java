package com.ichangemycity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ichangemycity.appdata.AppController;
import com.ichangemycity.model.ChangeStatusModel;
import com.ichangemycity.model.ComplaintData;
import com.ichangemycity.swachhbharatengineer.ChangeStatusActivity;
import com.ichangemycity.swachhbharatengineer.R;

import java.util.ArrayList;

import static com.ichangemycity.appdata.AppController.selectedComplaintChangeStatusOptions;

public class ChangeStatusSpinnerAdapter extends BaseAdapter {
    private ArrayList<ChangeStatusModel> changeStatusModel = new ArrayList<ChangeStatusModel>();
    Activity activity;
    ComplaintData cData;

    public ChangeStatusSpinnerAdapter(Activity activity, ComplaintData cData,
                                      ArrayList<ChangeStatusModel> changeStatusModel) {
        this.activity = activity;
        this.cData = cData;
        this.changeStatusModel = changeStatusModel;
    }

    public void clear() {
        changeStatusModel.clear();
    }

    public void addItem(ChangeStatusModel yourObject) {
        changeStatusModel.add(yourObject);
    }

    public void addItems(ArrayList<ChangeStatusModel> yourObjectList) {
        changeStatusModel.addAll(yourObjectList);
    }

    @Override
    public int getCount() {
        return changeStatusModel.size();
    }

    @Override
    public Object getItem(int position) {
        return changeStatusModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.inflate_change_status, parent, false);
            view.setTag("DROPDOWN");
        }
        final TextView textViewColor = (TextView) view.findViewById(R.id.statusColor);
        textViewColor.setBackgroundColor(changeStatusModel.get(position)
                .getColor());
        textViewColor.setTag(changeStatusModel.get(position));
        final View view1 = (View) view.findViewById(R.id.view);
        view1.setVisibility(View.VISIBLE);
        view1.setTag(cData);

        final TextView textView = (TextView) view
                .findViewById(android.R.id.text1);
        textView.setTag(position);
        textView.setGravity(Gravity.CENTER);
        textView.setText(getTitle(position));
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AppController.selectedComplaintDropdownIndex = (Integer) textView
                        .getTag();
                AppController.selectedComplaintData = (ComplaintData) view1
                        .getTag();
                AppController.selectedComplaintData.setToChangeStatus(true);
                selectedComplaintChangeStatusOptions = (ChangeStatusModel) textViewColor.getTag();
                activity.startActivity(new Intent(activity,
                        ChangeStatusActivity.class));
            }
        });
        view.setTag(changeStatusModel.get(position));

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.inflate_change_status, parent, false);
            view.setTag("NON_DROPDOWN");
        }
        final TextView textView = (TextView) view
                .findViewById(android.R.id.text1);
        TextView textViewColor = (TextView) view.findViewById(R.id.statusColor);
        final View view1 = (View) view.findViewById(R.id.view);
        view1.setTag(changeStatusModel.get(position));
        view1.setVisibility(View.GONE);
        // textViewColor.setBackgroundColor(changeStatusModel.get(position)
        // .getCurrentStatusColor());
        // textViewColor.setBackgroundColor(changeStatusModel.get(position)
        // .getColor());
        textViewColor.setVisibility(View.VISIBLE);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textViewColor.setBackgroundColor(changeStatusModel.get(0).getCurrentStatusColor());
        textView.setText(activity.getResources().getString(
                R.string.change_status));
        textView.setTag(position);
        view.setTag(changeStatusModel.get(position));

        return view;
    }


    private String getTitle(int position) {
        return position >= 0 && position < changeStatusModel.size() ? changeStatusModel
                .get(position).getStatusName() : "";
    }

}