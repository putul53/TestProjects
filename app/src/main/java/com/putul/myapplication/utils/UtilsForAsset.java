package com.putul.myapplication.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UtilsForAsset {

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static void readAssets(Context context, String fileName, String destPath) {
        AssetManager assetManager = context.getAssets();
        InputStream in;
        FileOutputStream out;
        try {
            File dir = new File(destPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(destPath, fileName);
            in = assetManager.open(fileName);
            out = new FileOutputStream(file);
            copyFile(in, out);
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tag", e.getMessage());
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
