package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ichangemycity.appdata.AppController;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.model.SelectedImageModel;
import com.ichangemycity.webservice.AppHelper;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

public class AndroidCustomGalleryActivity extends BaseAppCompatActivity {
    SelectedImageModel mSelectedImagesModel = new SelectedImageModel();
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.PhoneImageGrid)
    GridView imagegrid;
    @Nullable
    @BindView(R.id.frameLoader)
    FrameLayout frameLoader;
  private Activity activity;
    @Nullable
    @BindView(R.id.image)
    ImageView image;
    @Nullable
    @BindView(R.id.changePic)
    TextView changePic;
    @Nullable
    @BindView(R.id.next)
    Button next;
    @Nullable
    @BindView(R.id.pb_loader)
    ProgressWheel pb_loader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.assignLanguage(AndroidCustomGalleryActivity.this);
        setContentView(R.layout.custom_gallery);
        activity = AndroidCustomGalleryActivity.this;
        ButterKnife.bind(this);
        pb_loader.setVisibility(View.GONE);
        frameLoader.setVisibility(View.VISIBLE);
        setToolbarAndCustomizeTitle(toolbar, getResources().getString(R.string.select_an_image));

//        fetchImage();

        changePic.setOnClickListener(view -> pickImage());
        image.setOnClickListener(view -> pickImage());

        pickImage();

        next.setOnClickListener(view -> {
            if (mSelectedImagesModel.getSizeInMB() > 8) {
                showAlertToSelectImageNumbers("Please select an image less than 8MB");
            } else {
                new ProceedToDescriptionScreen().execute();
            }
        });
    }


    private void setToolbarAndCustomizeTitle(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

//        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> activity.finish());
        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }


  private void showAlertToSelectImageNumbers(String messageInfo) {
        // TODO Auto-generated method stub
        AlertDialog.Builder ab = new AlertDialog.Builder(
                AndroidCustomGalleryActivity.this);
        ab.setTitle("Message");
        ab.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info));
        ab.setMessage(messageInfo);
        ab.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        ab.show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
       /* try {
            if (mStoreSelectImages != null) {
                if (!mStoreSelectImages.isCancelled()) {
                    mStoreSelectImages.cancel(true);
                }
            } else {
                mStoreSelectImages = new StoreSelectImages();
            }
//            mStoreSelectImages.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private class ProceedToDescriptionScreen extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            frameLoader.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            AppController.mSelectedImageModels = mSelectedImagesModel;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            frameLoader.setVisibility(View.GONE);
//            Toast.makeText(activity,AppController.mSelectedImageModels.getSizeInMB()+" MB",Toast.LENGTH_SHORT).show();
            AndroidCustomGalleryActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
//        try {
//            mStoreSelectImages.cancel(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private final int RESULT_LOAD_IMAGE = 1;

    private void pickImage() {
        String action;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            action = Intent.ACTION_OPEN_DOCUMENT;
        } else {
            action = Intent.ACTION_PICK;
        }
        Intent i = new Intent(
                action,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        i.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            i.addFlags(FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String[] filePathColumn = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media
                .LONGITUDE, MediaStore.Images.Media.SIZE};
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            Cursor imagecursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            imagecursor.moveToFirst();

            final SelectedImageModel m = new SelectedImageModel();
            int image_column_index = imagecursor
                    .getColumnIndex(MediaStore.Images.Media._ID);
            m.setId(imagecursor.getInt(image_column_index));
            int dataColumnIndex = imagecursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            m.setThumbnails(MediaStore.Images.Thumbnails.getThumbnail(
                    getApplicationContext().getContentResolver(), m.getId(),
                    MediaStore.Images.Thumbnails.MINI_KIND, null));
            m.setArrPath(imagecursor.getString(dataColumnIndex));
            m.setLatitude(imagecursor.getDouble(imagecursor.getColumnIndex(MediaStore.Images.Media.LATITUDE)));
            m.setLongitude(imagecursor.getDouble(imagecursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE)));
            m.setSizeInMB((imagecursor.getInt(imagecursor.getColumnIndex(MediaStore.Images.Media.SIZE)) / 1024) / 1024);
//                    selectedImageModel.setPathOfSelectedImage(m.getArrPath());
            m.setPathOfSelectedImage(m.getArrPath());
//                    selectedImageModel.setUriOfImage(Uri.parse(Uri.fromFile(new File(m.getArrPath())).toString()));


            m.setUriOfImage(selectedImage);

            // DateTaken
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(imagecursor.getLong(imagecursor
                    .getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)));
            m.setDATE_TAKEN(AppController.getDate(c.getTimeInMillis(),
                    AppController.DATE_FORMAT));
            m.setDATE_TAKEN(m.getDATE_TAKEN());
            if (m.getArrPath() == null) {
                m.setArrPath(new File(String.valueOf(m.getUriOfImage())).getPath());
                m.setPathOfSelectedImage(m.getArrPath());
                AppHelper.getFileDataFromDrawable(activity, m);
            }
            mSelectedImagesModel = m;

            imagecursor.close();

//                ImageView imageView = (ImageView) findViewById(R.id.imgView);
//                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Glide.with(activity).load(m.getUriOfImage()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    image.setImageResource(R.mipmap.add);
                    pb_loader.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean
                        isFirstResource) {
                    pb_loader.setVisibility(View.GONE);
                    return false;
                }
            }).into(image);
            pb_loader.setVisibility(View.VISIBLE);
        } else if (resultCode == RESULT_CANCELED) {
            activity.finish();
        }


    }

}