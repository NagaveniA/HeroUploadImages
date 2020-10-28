package com.example.uploadimages.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class AppConstants {

    public static final String KEY_DATA = "data";
    public static final String KEY_ACCESS_TOKEN = "Accesstoken";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_SUBCATEGORY_ID = "sub_category_id";
    public static final String KEY_SUBCATEGORY_NAME = "sub_category_name" ;
    public static String KEY_CATEGORY_NAME="category_name";
    public static String KEY_TASK_ID="key_task_id";
    public static String KEY_SUBTASK_START = "subtask_start";

    public static String getStringImage(Bitmap bmp) {
        byte[] imageBytes = new byte[0];
        if (bmp!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp = getResizedBitmap(bmp, 500);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageBytes = baos.toByteArray();
        }
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }


    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = 0;
        int height = 0;
        if (image!=null) {
            width = image.getWidth();
            height = image.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }
}
