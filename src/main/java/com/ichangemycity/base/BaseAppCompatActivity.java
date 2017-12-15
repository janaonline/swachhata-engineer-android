package com.ichangemycity.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ichangemycity.appdata.AppController;
import com.ichangemycity.permission.GetPermissionResult;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pattabi.raman on 23-09-2017.
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    public String TAG = this.getClass().getSimpleName();
    private String TAG_GET_MAP_KEY = "TAG_GET_MAP_KEY";
    PermissionManager permission;
    private SharedPreferences permissionStatus;
    protected static Activity activity;
    private static GetPermissionResult onGetPermissionResult;
    private static List<String> customPermission;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        AppController.assignLanguage(BaseAppCompatActivity.this);
    }

    public void runtimePermissionManager(Activity activity, List<String> customPermission, GetPermissionResult onGetPermissionResult) {
        this.activity = BaseAppCompatActivity.this;
        this.onGetPermissionResult = onGetPermissionResult;
        this.customPermission=customPermission;

        if (android.os.Build.VERSION.SDK_INT >= 23  && checkPermissionStatus(activity, customPermission)) {
            permissionStatus = activity.getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);
            checkForPermissions();
        } else {
            proceedAfterPermissionSuccess();
        }
    }

    private boolean checkPermissionStatus(Activity activity, List<String> permissions) {
        boolean isAnyPermissionDenied = false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) ==
                    PackageManager.PERMISSION_DENIED) {
                isAnyPermissionDenied = true;
            }

        }
        return isAnyPermissionDenied;
    }

    private void checkForPermissions() {
        permission = new PermissionManager() {
            @Override
            public void ifCancelledAndCanRequest(Activity activity) {
                // Do Customized operation if permission is cancelled without checking "Don't ask again"
                // Use super.ifCancelledAndCanRequest(activity); or Don't override this method if not in use
                Toast.makeText(activity, "Please go to App settings and enable permissions", Toast.LENGTH_SHORT).show();
//                proceedAfterPermissionFailure();
            }

            @Override
            public void ifCancelledAndCannotRequest(Activity activity) {
                // Do Customized operation if permission is cancelled with checking "Don't ask again"
                // Use super.ifCancelledAndCannotRequest(activity); or Don't override this method if not in use
//                Toast.makeText(activity, "You've forcefully denied permissions, please go to App settings and enable permissions", Toast
// .LENGTH_LONG)
//                        .show();
//                proceedAfterPermissionFailure();
            }

            @Override
            public List<String> setPermission() {
                // If You Don't want to check permission automatically and check your own custom permission
                // Use super.setPermission(); or Don't override this method if not in use
//                List<String> customPermission = new ArrayList<>();
//                customPermission.add(Manifest.permission.INTERNET);
//                customPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//                customPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                customPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
//
//                customPermission.add(Manifest.permission.CAMERA);
                return customPermission;
            }
        };

        //To initiate checking permission
        permission.checkAndRequestPermissions(activity);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        permission.checkResult(requestCode, permissions, grantResults);
        //To get Granted Permission and Denied Permission
        ArrayList<String> granted = permission.getStatus().get(0).granted;
        ArrayList<String> denied = permission.getStatus().get(0).denied;
        if (denied.size() == 0) {
            proceedAfterPermissionSuccess();
        } else {
            proceedAfterPermissionFailure();
        }
    }

    private void proceedAfterPermissionSuccess() {
        onGetPermissionResult.resultPermissionSuccess();
    }

    private void proceedAfterPermissionFailure() {
        onGetPermissionResult.resultPermissionRevoked();
    }
}
