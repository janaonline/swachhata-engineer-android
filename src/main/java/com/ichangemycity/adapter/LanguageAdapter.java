package com.ichangemycity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.model.LanguageData;
import com.ichangemycity.swachhbharatengineer.R;
import com.ichangemycity.swachhbharatengineer.UserMobileNumber;

import java.util.ArrayList;

public class LanguageAdapter extends BaseAdapter {
    private ArrayList<LanguageData> mLanguage = new ArrayList<LanguageData>();
    Activity activity;

    public LanguageAdapter(Activity activity, ArrayList<LanguageData> mLanguage) {
        this.mLanguage.clear();
        this.activity = activity;
        this.mLanguage.addAll(mLanguage);
    }

    public void clear() {
        mLanguage.clear();
    }

    public void addItem(LanguageData yourObject) {
        mLanguage.add(yourObject);
    }

    public void addItems(ArrayList<LanguageData> yourObjectList) {
        mLanguage.addAll(yourObjectList);
    }

    @Override
    public int getCount() {
        return mLanguage.size();
    }

    @Override
    public Object getItem(int position) {
        return mLanguage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * @Override public View getDropDownView(int position, View view, ViewGroup
     * parent) { if (view == null ||
     * !view.getTag().toString().equals("DROPDOWN")) { view =
     * activity.getLayoutInflater().inflate(
     * R.layout.inflate_toolbar_spinner_item_dropdown, parent, false);
     * view.setTag("DROPDOWN"); }
     * 
     * TextView textView = (TextView) view.findViewById(android.R.id.text1);
     * textView.setText(getTitle(position));
     * 
     * return view; }
     */


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = activity.getLayoutInflater().inflate(
                    R.layout.inflate_toolbar_spinner_item_dropdown, parent,
                    false);
            view.setTag("NON_DROPDOWN");
        }
        final TextView textView = view
                .findViewById(android.R.id.text1);
        textView.setGravity(Gravity.CENTER);
        textView.setText(getTitle(position));
        view.setTag(mLanguage.get(position));
        textView.setTag(position);
        final RippleView rippleView = view.findViewById(R.id.rippleView);
        final View finalView = view;
        rippleView.setOnClickListener(new OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                rippleView.setBackgroundColor(activity.getResources().getColor(
                        R.color.blue_pressed_bottom));
                textView.setTextColor(Color.WHITE);
            }
        });
        rippleView.setOnRippleCompleteListener(rippleView1 -> {
            int sid = (int) textView.getTag();
            LanguageData languageData = mLanguage.get(sid);
            ICMyCPreferenceData.setPreference(activity,
                    ICMyCPreferenceData.selectedLanguage,
                    languageData.getLanguage_code());

            ICMyCPreferenceData.setPreference(activity,
                    ICMyCPreferenceData.selectedLanguagePosition, sid + "");
            activity.startActivity(new Intent(activity,
                    UserMobileNumber.class));

        });
        return view;
    }

    private String getTitle(int position) {
        return position >= 0 && position < mLanguage.size() ? mLanguage.get(
                position).getLanguage_label() : "";
    }
}