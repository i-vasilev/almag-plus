package com.elamed.almag;

import android.content.res.Resources;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.ViewGroup;

public abstract class ToolbarSizer {


    public static void setAppBarHeight(AppBarLayout appBarLayout, Resources resources) {
        appBarLayout.setLayoutParams(
                new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(resources) + dp2px(48, resources)));

    }

    public static int getStatusBarHeight(Resources resources) {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int dp2px(int dp, Resources resources) {
        float density = resources
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
