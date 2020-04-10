package com.ichangemycity.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ichangemycity.model.ComplaintFilterModel;
import com.ichangemycity.swachhbharatengineer.R;

public class ComplaintFilterSpinnerAdapter extends BaseAdapter {
    private ArrayList<ComplaintFilterModel> complaintFilterModel = new ArrayList<ComplaintFilterModel>();
    Activity activity;

    public ComplaintFilterSpinnerAdapter(Activity activity,
            ArrayList<ComplaintFilterModel> complaintFilterModel) {
        this.activity = activity;
        this.complaintFilterModel = complaintFilterModel;
    }

  @Override
    public int getCount() {
        return complaintFilterModel.size();
    }

    @Override
    public Object getItem(int position) {
        return complaintFilterModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.inflate_complaint_filter_dropdown, parent, false);
            view.setTag("DROPDOWN");
        }
        TextView textView = view
                .findViewById(R.id.textPostComplaint);
        TextView filtercolor = view.findViewById(R.id.filtercolor);
        filtercolor.setBackgroundColor(complaintFilterModel.get(position).getComplaintColor());
        filtercolor.setVisibility(View.GONE);
        textView.setBackgroundColor(Color.TRANSPARENT);
        textView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        textView.setText(getTitle(position));
        view.setTag(complaintFilterModel.get(position));
        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.inflate_complaint_filter_dropdown, parent, false);
            view.setTag("NON_DROPDOWN");
        }
        TextView textView = view
                .findViewById(R.id.textPostComplaint);
        TextView filtercolor = view.findViewById(R.id.filtercolor);
        textView.setBackgroundColor(Color.WHITE);
        filtercolor.setVisibility(View.INVISIBLE);
        textView.setText(complaintFilterModel.get(position).getDisplayTitle());
        textView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        view.setTag(complaintFilterModel.get(position));
        return view;
    }

    private String getTitle(int position) {
        return position >= 0 && position < complaintFilterModel.size() ? complaintFilterModel
                .get(position).getDisplayTitle() : "";
    }

}