package com.ichangemycity.swachhbharatengineer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ichangemycity.appdata.AppController;
import com.ichangemycity.base.BaseAppCompatActivity;
import com.ichangemycity.model.SelectedImageModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class AndroidCustomGalleryActivity extends BaseAppCompatActivity {
 ArrayList<SelectedImageModel> mModel = new ArrayList<SelectedImageModel>();
 SelectedImageModel mSelectedImagesModel = new SelectedImageModel();
 ArrayList<String> ids = new ArrayList<String>();
 private ImageAdapter imageAdapter;
 Toolbar toolbar;
 GridView imagegrid;
 FrameLayout frameLoader;
 int myLastVisiblePos;
 public static Activity activity;

 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  AppController.assignLanguage(AndroidCustomGalleryActivity.this);
  setContentView(R.layout.custom_gallery);
  activity = AndroidCustomGalleryActivity.this;
  BaseAppCompatActivity.activity = activity;
  frameLoader = (FrameLayout) findViewById(R.id.frameLoader);
  frameLoader.setVisibility(View.VISIBLE);
  toolbar = (Toolbar) findViewById(R.id.toolbar);
  setToolbarAndCustomizeTitle(toolbar, getResources().getString(R.string.loading));
  imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
  imageAdapter = new ImageAdapter();
  myLastVisiblePos = imagegrid.getFirstVisiblePosition();
  imagegrid.setAdapter(imageAdapter);
  mModel.clear();
  imagegrid.setOnScrollListener(new OnScrollListener() {
   @Override
   public void onScrollStateChanged(AbsListView view, int scrollState) {
    int currentFirstVisPos = view.getFirstVisiblePosition();
    if (currentFirstVisPos > myLastVisiblePos) {
     if (isLoadMore) {
      try {
       if (mStoreSelectImages != null) {
        mStoreSelectImages.cancel(true);
       }
       if (mModel.size() <= 1000) {
        mStoreSelectImages = new StoreSelectImages();
        mStoreSelectImages.execute();
       }
      } catch (Exception e) {
       e.printStackTrace();
      }
     } else {
     }
    }
    myLastVisiblePos = currentFirstVisPos;
   }

   @Override
   public void onScroll(AbsListView view, int firstVisibleItem,
                        int visibleItemCount, int totalItemCount) {
   }
  });
 }

 public class ImageAdapter extends BaseAdapter {
  private LayoutInflater mInflater;

  public ImageAdapter() {
   mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public int getCount() {
   return mModel.size();
  }

  public Object getItem(int position) {
   return position;
  }

  public long getItemId(int position) {
   return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
   final ViewHolder holder;
   if (convertView == null) {
    holder = new ViewHolder();
    convertView = mInflater.inflate(R.layout.galleryitem, null);
    holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
    holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
    convertView.setTag(holder);
   } else {
    holder = (ViewHolder) convertView.getTag();
   }
   holder.checkbox.setId(position);
   holder.imageview.setId(position);
   holder.imageview.setTag(mModel.get(position));
   holder.checkbox.setVisibility(View.GONE);
            /*holder.checkbox.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    SelectedImageModel m = (SelectedImageModel) holder.imageview.getTag();
                    int index = mModel.indexOf(m);
                    if (m.isThumbnailsselection()) {
                        int selectedIndex = mSelectedImagesModel.indexOf(m);
                        cb.setChecked(false);
                        mSelectedImagesModel.remove(selectedIndex);
                        m.setThumbnailsselection(false);
                    } else {
                        cb.setChecked(true);
                        m.setThumbnailsselection(true);
                        mSelectedImagesModel.add(m);
                    }
                    mModel.set(index, m);
                    notifyDataSetChanged();
                }
            });*/
   holder.imageview.setOnClickListener(new OnClickListener() {
    public void onClick(View v) {
     SelectedImageModel m = (SelectedImageModel) holder.imageview.getTag();
     mSelectedImagesModel = m;
     if(mSelectedImagesModel.getSizeInMB()<=8) {
      new ProceedToDescriptionScreen().execute();
     }else{
      showAlertToSelectImageNumbers(activity.getResources().getString(R.string.image_size_exceeded));
     }
    }
   });
   holder.imageview.setImageBitmap(mModel.get(position).getThumbnails());
   holder.checkbox.setChecked(mModel.get(position).isThumbnailsselection());
   holder.id = position;
   return convertView;
  }
 }

 private void setToolbarAndCustomizeTitle(Toolbar toolbar, String title) {
  setSupportActionBar(toolbar);
  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  getSupportActionBar().setDisplayShowHomeEnabled(true);
  getSupportActionBar().setTitle(title);

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
 }

 class ViewHolder {
  ImageView imageview;
  CheckBox checkbox;
  int id;
 }

 Cursor imagecursor;
 private boolean isLoadMore;
 int page = 1;

 private class StoreSelectImages extends AsyncTask<Void, Void, Void> {
  private int count;

  @Override
  protected void onPreExecute() {
   // TODO Auto-generated method stub
   super.onPreExecute();
   frameLoader.setVisibility(View.VISIBLE);
  }

  @SuppressWarnings("deprecation")
  @Override
  protected Void doInBackground(Void... params) {
   try {
    final String[] columns = {MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media
            .LONGITUDE, MediaStore.Images.Media.SIZE};
    final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
    imagecursor = null;
    if (mModel.size() > 0) {
     String joinedIds = TextUtils.join(",", ids);
     imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
             columns, MediaStore.Images.Media._ID + " not in (" + joinedIds + ")",
             null, orderBy + " DESC LIMIT 50");
    } else
     imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
             columns, null, null, orderBy + " DESC LIMIT 20");
    int image_column_index = imagecursor
            .getColumnIndex(MediaStore.Images.Media._ID);
    this.count = imagecursor.getCount();
    if (this.count > 0) {
     isLoadMore = true;
    } else {
     isLoadMore = false;
    }
    if (mModel.size() > 500) {
     freeMemory();
    }
    SelectedImageModel m;
    for (int i = 0; i < this.count; i++) {
     m = new SelectedImageModel();
//                    SelectedImageModel selectedImageModel = new SelectedImageModel();
     imagecursor.moveToPosition(i);
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
     m.setUriOfImage(Uri.parse(Uri.fromFile(new File(m.getArrPath())).toString()));

     m.setSizeInMB(((int) new File(m.getArrPath()).length() / 1024) / 1024);
     // DateTaken
     Calendar c = Calendar.getInstance();
     c.setTimeInMillis(imagecursor.getLong(imagecursor
             .getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)));
     m.setDATE_TAKEN(AppController.getDate(c.getTimeInMillis(),
             AppController.DATE_FORMAT));
//                    selectedImageModel.setDATE_TAKEN(m.getDATE_TAKEN());
//                    selectedImageModel.setSizeInMB(m.getSizeInMB());
     m.setDATE_TAKEN(m.getDATE_TAKEN());
     m.setSizeInMB(m.getSizeInMB());

     if (m.getSizeInMB() >= 0) {
      mModel.add(m);
      ids.add(m.getId() + "");

     } else {
     }
    }
   } catch (OutOfMemoryError e) {
    this.cancel(true);
    e.printStackTrace();
   }
   return null;
  }

  public void freeMemory() {
   System.runFinalization();
   Runtime.getRuntime().gc();
   System.gc();
  }

  @Override
  protected void onPostExecute(Void result) {
   super.onPostExecute(result);
   frameLoader.setVisibility(View.GONE);
   imageAdapter.notifyDataSetChanged();
   getSupportActionBar().setTitle(activity.getResources().getString(R.string.recentimages)
           + "(" + mModel.size() + "/1000)");
   TextView tvNoDataFound = (TextView) findViewById(R.id.empty_list_view);
   tvNoDataFound.setVisibility(View.VISIBLE);
   imagegrid.setEmptyView(findViewById(R.id.empty_list_view));

  }
 }

 @SuppressWarnings("deprecation")
 private void showAlertToSelectImageNumbers(String messageInfo) {
  // TODO Auto-generated method stub
  AlertDialog.Builder ab = new AlertDialog.Builder(
          AndroidCustomGalleryActivity.this);
  ab.setTitle("Message");
  ab.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info));
  ab.setMessage(messageInfo);
  ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
   @Override
   public void onClick(DialogInterface dialog, int which) {
    dialog.dismiss();
   }
  });
  ab.show();
 }

 StoreSelectImages mStoreSelectImages;

 @Override
 protected void onResume() {
  // TODO Auto-generated method stub
  super.onResume();
  try {
   if (mStoreSelectImages != null) {
    if (!mStoreSelectImages.isCancelled()) {
     mStoreSelectImages.cancel(true);
    }
   } else {
    mStoreSelectImages = new StoreSelectImages();
   }
   mStoreSelectImages.execute();
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

 @Override
 protected void onDestroy() {
  // TODO Auto-generated method stub
  super.onDestroy();
  try {
   imagecursor.close();
  } catch (Exception e) {
   // TODO: handle exception
  }
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
  try {
   mStoreSelectImages.cancel(true);
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

}