package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.bumptech.glide.Glide;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.ICMyCPreferenceData;
import com.ichangemycity.model.SelectedImageModel;

import java.util.ArrayList;

public class PublicToiletAddImageActivity extends AppCompatActivity {
    private ImageView outsideAddImage;
    private ImageView insideAddImage;
    private ImageView additionalAddImage;
    Activity activity;
    private LinearLayout publicToiletOutsidelinearLayout;
    private LinearLayout publicToiletInsideLinearLayout;
    private LinearLayout publicToiletAdditionalLinearLayout;
    private ArrayList<SelectedImageModel> publicToiletOutside = new ArrayList<>();
    private ArrayList<SelectedImageModel> publicToiletInside = new ArrayList<>();
    private ArrayList<SelectedImageModel> publicToiletAdditional = new ArrayList<>();
    float ht_px, wt_px, margin;
    private Handler handler = new Handler();
    private TextView publicToiletOutsideLatitude, publicToiletOutsideLongitude, publicToiletOutsideAccuracy;
    private TextView  publicToiletInsideLatitude, publicToiletInsideLongitude, publicToiletInsideAccuracy;
    private Toolbar toolbar;
    private Button submitButton;
    RippleView rippleViewImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.assignLanguage(PublicToiletAddImageActivity.this);
        setContentView(R.layout.activity_add_imagefile);
        activity = PublicToiletAddImageActivity.this;
        clearSelectedImage();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarAndCustomizeTitle("Add Images");

        initialization();
        wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, activity
                .getResources().getDisplayMetrics());
        ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, activity
                .getResources().getDisplayMetrics());
        margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, activity
                .getResources().getDisplayMetrics());

        outsideAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertToPickImage(AppConstant.PUBLIC_TOILET_OUTSIDE_PIC);
            }
        });
        insideAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertToPickImage(AppConstant.PUBLIC_TOILET_INSIDE_PIC);
            }
        });
        additionalAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertToPickImage(AppConstant.PUBLIC_TOILET_ADDITIONAL_PIC);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rippleViewImages.performClick();
            }
        });
        rippleViewImages.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                startActivity(new Intent(PublicToiletAddImageActivity.this,
                        PublicToiletThankYouActivity.class));
            }
        });

        // clear memory for arraylist
        publicToiletOutside.clear();
        publicToiletInside.clear();
        publicToiletAdditional.clear();


    }
// Id's initialization
    private void initialization() {
        outsideAddImage = (ImageView) findViewById(R.id.complte_toliet_imageview);
        insideAddImage = (ImageView) findViewById(R.id.cleanliness_toliet_img);
        additionalAddImage = (ImageView) findViewById(R.id.additional_photos_img);
        publicToiletOutsideLatitude = (TextView) findViewById(R.id.complte_toliet_latitxt);
        publicToiletOutsideLongitude = (TextView) findViewById(R.id.complte_toliet_longtxt);
        publicToiletOutsideAccuracy = (TextView) findViewById(R.id.complte_toliet_accuracytxt);
        publicToiletInsideLatitude = (TextView) findViewById(R.id.cleanliness_toliet_latitxt);
        publicToiletInsideAccuracy = (TextView) findViewById(R.id.cleanliness_toliet_longtxt);
        publicToiletInsideLongitude = (TextView) findViewById(R.id.cleanliness_toliet_accuracytxt);
        publicToiletOutsidelinearLayout = (LinearLayout) findViewById(R.id.imageLinear);
        publicToiletInsideLinearLayout = (LinearLayout) findViewById(R.id.cleanliness_toliet_imageLinear);
        publicToiletAdditionalLinearLayout = (LinearLayout) findViewById(R.id.additional_pic_linerLayout);
        submitButton= (Button) findViewById(R.id.submit);
        rippleViewImages=(RippleView) findViewById(R.id.rippleViewImages);

    }

    private void showAlertToPickImage(final int selectedPublicToiletSection) {
        AppConstant.selectedPublicToiletSection = selectedPublicToiletSection;
        startActivity(new Intent(activity, SelectImageDialogActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

    }
    // clear the images
    private void clearSelectedImage() {
        AppController.mSelectedImageModels = new SelectedImageModel();
        ICMyCPreferenceData.setPreference(activity, ICMyCPreferenceData.commentUploadedImageFile, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (!TextUtils.isEmpty(AppController.mSelectedImageModels.getPathOfSelectedImage())) {
                String imgurl = AppController.mSelectedImageModels.getUriOfImage().toString();
                if (imgurl != null && AppConstant.selectedPublicToiletSection != -1) {
                    pickSectionAndUpdateImageData(AppConstant.selectedPublicToiletSection);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pickSectionAndUpdateImageData(final int selectedPublicToiletSection) {
        switch (selectedPublicToiletSection) {

            case AppConstant.PUBLIC_TOILET_OUTSIDE_PIC:
                publicToiletOutside.add(AppController.mSelectedImageModels);
                showSelectedImage(publicToiletOutside, selectedPublicToiletSection);
                break;
            case AppConstant.PUBLIC_TOILET_INSIDE_PIC:
                publicToiletInside.add(AppController.mSelectedImageModels);
                showSelectedImage(publicToiletInside, selectedPublicToiletSection);
                break;
            case AppConstant.PUBLIC_TOILET_ADDITIONAL_PIC:
                publicToiletAdditional.add(AppController.mSelectedImageModels);
                showSelectedImage(publicToiletAdditional, selectedPublicToiletSection);
                break;
            default:
                break;
        }
    }

    private void setToolbarAndCustomizeTitle(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void showSelectedImage(final ArrayList<SelectedImageModel> galleryImageUrls, final int selectedPublicToiletSection) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {

                    switch (selectedPublicToiletSection) {
                        case AppConstant.PUBLIC_TOILET_OUTSIDE_PIC:
                            publicToiletOutsidelinearLayout.removeAllViews();
                            if (publicToiletOutside.size() == 3) {
                                outsideAddImage.setVisibility(View.GONE);
                            } else {
                                outsideAddImage.setVisibility(View.VISIBLE);
                            }
                            break;
                        case AppConstant.PUBLIC_TOILET_INSIDE_PIC:
                            publicToiletInsideLinearLayout.removeAllViews();
                            if (publicToiletInside.size() == 3) {
                                insideAddImage.setVisibility(View.GONE);
                            } else {
                                insideAddImage.setVisibility(View.VISIBLE);
                            }
                            break;
                        case AppConstant.PUBLIC_TOILET_ADDITIONAL_PIC:
                            publicToiletAdditionalLinearLayout.removeAllViews();
                            if (publicToiletAdditional.size() == 3) {
                                additionalAddImage.setVisibility(View.GONE);
                            } else {
                                additionalAddImage.setVisibility(View.VISIBLE);
                            }
                            break;
                    }
                    for (int i = 0; i < galleryImageUrls.size(); i++) {
                       // String imgUrl = galleryImageUrls.get(i).getArrPath();
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                (int) wt_px, (int) ht_px);
                        if (i == 0) {
                            layoutParams.setMargins(0, 0, (int) margin, 0);
                        } else if (i == galleryImageUrls.size() - 1) {
                            layoutParams.setMargins((int) margin, 0, 0, 0);
                        } else {
                            layoutParams.setMargins((int) margin, 0, (int) margin, 0);
                        }
                        final ImageView image = new ImageView(activity);
                        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        image.setLayoutParams(layoutParams);
                      Glide.with(activity).load(galleryImageUrls.get(i).getUriOfImage()).into(image);
                        image.setId(i);
                        switch (selectedPublicToiletSection) {
                            case AppConstant.PUBLIC_TOILET_OUTSIDE_PIC:
                                publicToiletOutsidelinearLayout.addView(image);
                                publicToiletOutsideLatitude.setText(AppController.mSelectedImageModels.getLatitude() == 0.0 ? "0.0" : AppController.mSelectedImageModels.getLatitude() + "");
                                publicToiletOutsideLongitude.setText(AppController.mSelectedImageModels.getLongitude() == 0.0 ? "0.0" : AppController.mSelectedImageModels.getLongitude() + "");
                                publicToiletOutsideAccuracy.setText(AppController.mSelectedImageModels.getAccuracy() == 0 ? "0" : AppController.mSelectedImageModels.getAccuracy() + "");
                                break;
                            case AppConstant.PUBLIC_TOILET_INSIDE_PIC:
                                publicToiletInsideLinearLayout.addView(image);
                                publicToiletInsideLatitude.setText(AppController.mSelectedImageModels.getLatitude() == 0.0 ? "0.0" : AppController.mSelectedImageModels.getLatitude() + "");
                                publicToiletInsideLongitude.setText(AppController.mSelectedImageModels.getLongitude() == 0.0 ? "0.0" : AppController.mSelectedImageModels.getLongitude() + "");
                                publicToiletInsideAccuracy .setText(AppController.mSelectedImageModels.getAccuracy() == 0 ? "0" : AppController.mSelectedImageModels.getAccuracy() + "");
                                break;
                            case AppConstant.PUBLIC_TOILET_ADDITIONAL_PIC:
                                publicToiletAdditionalLinearLayout.addView(image);
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
