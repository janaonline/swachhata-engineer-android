package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.ichangemycity.appdata.AppConstant;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.appdata.AppUtils;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.callback.OnButtonClick;
import com.ichangemycity.model.CustomGallery;
import com.ichangemycity.model.SelectedImageModel;
import com.ichangemycity.permission.GetPermissionResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by pattabi.raman on 18-10-2017.
 */

public class SelectImageDialogActivity extends BaseAppCompatActivity {
    private static Activity activity;
    private RippleView rippleViewCamera, rippleViewGallery;
    ProgressBar progress;
    List<String> permissionsRequired = new ArrayList<>();

    public Activity getActivity() {
        if (activity == null)
            activity = SelectImageDialogActivity.this;
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image_dialog_activity);
        AppController.assignLanguage(SelectImageDialogActivity.this);

        permissionsRequired.clear();

        activity = SelectImageDialogActivity.this;
        progress = (ProgressBar) activity.findViewById(R.id.progress);
        rippleViewCamera = (RippleView) findViewById(R.id.rippleViewCamera);
        rippleViewGallery = (RippleView) findViewById(R.id.rippleViewGallery);
        AppController.mSelectedImageModels = new SelectedImageModel();
        AppController.location = "";
        AppController.latitude = 0.0;
        AppController.longitude = 0.0;
        checkForStoragePermission();
    }

    private void checkForStoragePermission() {
        permissionsRequired.add(android.Manifest.permission.INTERNET);
        permissionsRequired.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionsRequired.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsRequired.add(android.Manifest.permission.CAMERA);
        runtimePermissionManager(activity, permissionsRequired, new GetPermissionResult() {
            @Override
            public void resultPermissionSuccess() {
//                Toast.makeText(activity, "Thanks for allowing permissions", Toast.LENGTH_SHORT).show();
                rippleViewCamera.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {


                        GenerateFolders();
                        captureImage();
                    }
                });
                rippleViewGallery.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {


                        startActivity(new Intent(activity, AndroidCustomGalleryActivity.class));
                    }
                });

            }

            @Override
            public void resultPermissionRevoked() {
                AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO, "We suggest to allow permissions to make app work as expected");

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (AppController.mSelectedImageModels.getPathOfSelectedImage() != null) {
                if (AppController.mSelectedImageModels.getLatitude() != 0.0 && AppController.mSelectedImageModels.getLongitude() != 0.0) {
                    AppController.latitude = AppController.mSelectedImageModels.getLatitude();
                    AppController.longitude = AppController.mSelectedImageModels.getLongitude();
                    getAddressFromLatLong();
                } else {
                    new CheckImageSize().execute();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CheckImageSize extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (AppController.mSelectedImageModels.getSizeInMB() > 8) {
                AppController.showAlert(activity, "Alert", "Total size exceeded 10MB " +
                        "of size. Please " +
                        "select an image with lesser memory to upload", false, new OnButtonClick() {

                    @Override
                    public void onPositiveButtonClicked(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }

                    @Override
                    public void onNegativeButtonClicked(DialogInterface dialogInterface) {

                    }
                });
            } else {
                redirectAccordingToPurposeOfImageUpload();
            }
        }
    }


    private void redirectAccordingToPurposeOfImageUpload() {
        switch (AppController.selectedPurposeToUploadImage) {
            case AppController.PURPOSE_CHANGE_STATUS:
                break;
            case AppController.PURPOSE_POST_COMMENT:
                activity.finish();
                break;

        }
        activity.finish();
    }

    private void GenerateFolders() {
        File folder = new File(Environment.getExternalStorageDirectory()
                + "/Swachhata/Images/");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }


    Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Swachhata";

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Looper.prepare();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fileUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider",
                    getOutputMediaFile(MEDIA_TYPE_IMAGE));
        } else {
//            // Android version is lesser than 6.0 or the permission is already granted.
            GenerateFolders();
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME
                        + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"
                    + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    previewCapturedImage();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "You have cancelled image selection",
                            Toast.LENGTH_SHORT).show();
                } else {

                    AppUtils.showToast(activity, AppConstant.TOAST_TYPE_INFO, activity.getResources().getString(R.string.select_an_image));

                }
                break;
        }
    }

    private void previewCapturedImage() {

        try {
            String all_path = fileUri.getPath();
            CustomGallery item = new CustomGallery();
            item.sdcardPath = all_path;
//            item.sdcardPath = PublicEye.compressImage(activity,
//                    item.sdcardPath);
            SelectedImageModel selectedImageModel = new SelectedImageModel();
            File myFile = new File(fileUri.getPath());
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            selectedImageModel.setDATE_TAKEN(AppController.getDate(cal.getTimeInMillis(), AppController.DATE_FORMAT));
            selectedImageModel.setPathOfSelectedImage(myFile.getAbsolutePath());
            selectedImageModel.setUriOfImage(fileUri);
            selectedImageModel.setThumbnails(BitmapFactory.decodeFile((myFile.getAbsolutePath())));
            AppController.mSelectedImageModels = selectedImageModel;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getAddressFromLatLong() {
        progress.setVisibility(View.VISIBLE);
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(AppController.latitude, AppController.longitude, 1); // Here 1 represent max location result to
            // returned, by
        } catch (IOException e) {
            e.printStackTrace();
        }
        // documents
        // it
        // recommended 1
        // to 5
        if (addresses != null) {
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address
            // lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            AppController.location = address;
            progress.setVisibility(View.GONE);

        } else {
            AppUtils.hideProgressDialog(activity);
        }

    }

    @Override
    public void finish() {
        super.finish();
        AppController.selectedPurposeToUploadImage = -1;
    }
}
