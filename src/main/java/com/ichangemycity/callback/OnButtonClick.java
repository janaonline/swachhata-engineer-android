package com.ichangemycity.callback;

import android.content.DialogInterface;

/**
 * Created by pattabi.raman on 01-09-2017.
 */

public interface OnButtonClick {
    public void onPositiveButtonClicked(DialogInterface dialogInterface);
    public void onNegativeButtonClicked();
}
