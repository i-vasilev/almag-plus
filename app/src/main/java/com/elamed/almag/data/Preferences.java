package com.elamed.almag.data;

import android.content.SharedPreferences;

public class Preferences {
    public static final String APP_PREFERENCES = "com.elamed.almag.preferences";
    public static final String APP_PREFERENCES_IS_NOT_FIRST = "is_not_first";
    private static SharedPreferences mSettings;

    public static boolean isFirst(){
        boolean result = false;
        if(!mSettings.contains(APP_PREFERENCES_IS_NOT_FIRST)){
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(APP_PREFERENCES_IS_NOT_FIRST, true);
            editor.apply();
            result = true;
        }
        return result;
    }

    public static void setmSettings(SharedPreferences mSettings) {
        Preferences.mSettings = mSettings;
    }
}
