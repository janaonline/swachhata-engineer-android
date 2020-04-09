package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.andexert.library.RippleView;
import com.ichangemycity.appdata.AppController;

public class PublicToiletThankYouActivity extends AppCompatActivity {
    Button viewDetailsBtn,addAnotherToiletBtn,bckToHomeBtn;
    public static Activity activity;
    Toolbar toolbar;
    RippleView rippleViewAddToilet,rippleViewDetails,rippleViewHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppController.assignLanguage(PublicToiletThankYouActivity.this);
        setContentView(R.layout.activity_thank_you);
        activity = PublicToiletThankYouActivity.this;
        toolbar = findViewById(R.id.toolbar);
        setToolbarAndCustomizeTitle(getResources().getString(R.string.thankyou));

        rippleViewAddToilet = findViewById(R.id.rippleViewAddToilet);
        rippleViewDetails = findViewById(R.id.rippleViewDetails);
        rippleViewHome = findViewById(R.id.rippleViewHome);


        addAnotherToiletBtn = findViewById(R.id.add_antr_btn);
        addAnotherToiletBtn.setOnClickListener(view -> {
            //  Intent intent = new Intent(packagec, .class);
            /*startActivity(new Intent(PublicToiletThankYouActivity.this,
                    PublicToiletAddToiletActivity.class));*/
            // startActivity(intent);

            rippleViewAddToilet.performClick();
        });
        viewDetailsBtn = findViewById(R.id.add_antr_btn);
        viewDetailsBtn.setOnClickListener(view -> {
            //  Intent intent = new Intent(packagec, .class);
            /*startActivity(new Intent(PublicToiletThankYouActivity.this,
                    PublicToiletDetailsActivity.class));*/

            rippleViewDetails.performClick();

        });
        bckToHomeBtn = findViewById(R.id.add_antr_btn);
        bckToHomeBtn.setOnClickListener(view -> {
            //  Intent intent = new Intent(packagec, .class);
          /*  startActivity(new Intent(PublicToiletThankYouActivity.this,
                    MainActivity.class));*/
            rippleViewHome.performClick();

        });

        rippleViewAddToilet.setOnRippleCompleteListener(rippleView -> startActivity(new Intent(PublicToiletThankYouActivity.this,
                PublicToiletAddToiletActivity.class)));
        rippleViewDetails.setOnRippleCompleteListener(rippleView -> startActivity(new Intent(PublicToiletThankYouActivity.this,
                PublicToiletDetailsActivity.class)));
        rippleViewHome.setOnRippleCompleteListener(rippleView -> startActivity(new Intent(PublicToiletThankYouActivity.this,
                MainActivity.class)));
    }

    private void setToolbarAndCustomizeTitle(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> activity.finish());
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
