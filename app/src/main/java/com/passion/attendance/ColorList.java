package com.passion.attendance;

import android.content.Context;
import android.support.annotation.ColorInt;

import java.util.Random;

/**
 * Created by Aayush on 3/28/2016.
 */
public class ColorList {
    @ColorInt
    private static int[] colorList = {
            R.color.ListRed,
            R.color.ListPink,
            R.color.ListPurple,
            R.color.ListDeepPurple,
            R.color.ListIndigo,
            R.color.ListTeal,
            R.color.ListGreen,
            R.color.ListAmber,
            R.color.ListOrange,
            R.color.ListDeepOrange,
    };

    @ColorInt
    public static int getRandomColor(Context context) {
        int randomNumber = new Random().nextInt(colorList.length);

        return context.getColor(colorList[randomNumber]);
    }

}
