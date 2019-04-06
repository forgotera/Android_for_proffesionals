package com.example.mura.criminalntentd;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.ActionBarDrawerToggle;

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){
        //размеры изображения на диске
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float scrWidth = options.outWidth;
        float scrHeigt = options.outHeight;

        //степень мастабирования
        int inSapmleSize = 1;
        if(scrHeigt > destHeight || scrWidth > destWidth){
            float heightScale = scrHeigt / destHeight;
            float widthScale = scrWidth / destWidth;
            inSapmleSize = Math.round(heightScale > widthScale ? heightScale :widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSapmleSize;

        //чтение данных и создание нового растр изображения
        return BitmapFactory.decodeFile(path,options);
    }

    //маштобирование
    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path,size.x,size.y);
    }
}
