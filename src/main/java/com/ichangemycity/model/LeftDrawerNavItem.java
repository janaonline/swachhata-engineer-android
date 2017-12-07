package com.ichangemycity.model;

import android.graphics.drawable.Drawable;

public class LeftDrawerNavItem {
    public String mTitle;
    public String mSubtitle;
    public Drawable  mIcon;
 
    public LeftDrawerNavItem(String title, String subtitle, Drawable icon) {
        mTitle = title;
        mSubtitle = subtitle;
        mIcon = icon;
    }

}
