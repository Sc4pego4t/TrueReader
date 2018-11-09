package ru.scapegoats.truereader.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import ru.scapegoats.truereader.modules.BaseActivity;

public class Utils {

    static public float getActionBarHeight(Context context){
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        } else return 0;
    }

    static public float getStatusBarHeight(Context context) {
        float result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);

        }
        return result;
    }

    static public int getNavigationBarSize(Context context){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(
                "navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
