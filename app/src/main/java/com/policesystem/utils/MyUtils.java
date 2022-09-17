package com.policesystem.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MyUtils {

    public static Bitmap decodeBitmap(String imageString) {
        ByteArrayInputStream bArrIS = new ByteArrayInputStream(Base64.decode(imageString, Base64.DEFAULT));
        return BitmapFactory.decodeStream(bArrIS);
    }

    public static String encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream barrOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, barrOS);
        return Base64.encodeToString(barrOS.toByteArray(), Base64.DEFAULT);
    }
}
