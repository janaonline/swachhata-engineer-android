package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.andexert.library.RippleView;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.base.BaseAppCompatActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;

/**
 * Created by Sindhu BC on 7/9/2018.
 */

public class PublicToiletNearbyActivity extends BaseAppCompatActivity {

    private Button addToiletBtn;
    public static Activity activity;
    Toolbar toolbar;
    RippleView rippleView2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppController.assignLanguage(PublicToiletNearbyActivity.this);
        setContentView(R.layout.public_toilet_nearby);

        activity = PublicToiletNearbyActivity.this;
        rippleView2 = (RippleView) findViewById(R.id.rippleView2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarAndCustomizeTitle(getResources().getString(R.string.public_toilet_nearby));

        addToiletBtn = (Button) findViewById(R.id.add_toilet_btn);
        addToiletBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent intent = new Intent(packagec, .class);
                rippleView2.performClick();
                // startActivity(intent);
            }
        });
        rippleView2.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                startActivity(new Intent(PublicToiletNearbyActivity.this,
                        PublicToiletAddToiletActivity.class));
            }
        });

    }

    private void setToolbarAndCustomizeTitle(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        /*if (mobileNumber != null)
            mobileNumber.setText("");*/
    }
}


