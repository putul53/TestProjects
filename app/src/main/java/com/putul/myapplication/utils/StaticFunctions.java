package com.putul.myapplication.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.putul.myapplication.R;
import android.Manifest;

import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StaticFunctions {

    private static ProgressDialog progressDialog = null;
    public static int PERMISSION_STORAGE = 222;

    public static boolean checkPermission(Activity context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            return true;
        }
        else{
            String PERMISSIONS[] = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE} ;
            if (!hasPermissions(context, PERMISSIONS)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context.requestPermissions(PERMISSIONS, PERMISSION_STORAGE);
                }
            } else {
                return true;
            }
            return false;
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void showDialog(Activity mContext) {
        try {
            if(mContext!=null){
                progressDialog = new ProgressDialog(mContext);
                progressDialog.getWindow().setBackgroundDrawable(new
                        ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                progressDialog.setContentView(R.layout.my_progress);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void hideDialog(Activity context) {
        if (context!=null && progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void ToastFunction(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    private static String rootDir(Context context) {
        return context.getExternalFilesDir(null).toString();
    }

    public static String commonDocumentDirPath(Context context) {
        String parentDir="";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            parentDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString();
        } else {
            try {
                parentDir  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                        .toString();
            } catch (Exception e) {
                parentDir = rootDir(context);
            }
        }
        return parentDir;
    }

    public static String getCurrentDate() {
        try {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault());
            String formattedDate = df.format(c);
            return formattedDate;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static long getCurrentDateAndTimeInMillis() {
        try {
            Date date = new Date();
            long msec = date.getTime();
            return msec;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

}
