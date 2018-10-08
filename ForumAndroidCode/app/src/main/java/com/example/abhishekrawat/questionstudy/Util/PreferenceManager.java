package com.example.abhishekrawat.questionstudy.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static PreferenceManager sInstance;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    public interface Keys {
        String USER_INFO = "user_info";
        String IS_LOGGED_IN="is_logged_in";
    }
    private PreferenceManager(Context ctx) {
        mPrefs = ctx.getApplicationContext().getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }
    public static PreferenceManager getInstance(Context ctx) {
        if (sInstance == null) {
            sInstance = new PreferenceManager(ctx);
        }
        return sInstance;
    }
    public void setUserInfo(String userInfo) {
        mEditor.putString(Keys.USER_INFO, userInfo).apply();
    }

    public String getUserInfo() {
        return mPrefs.getString(Keys.USER_INFO, "");
    }

    public void setLoggedInStatus(boolean status) {
        mEditor.putBoolean(Keys.IS_LOGGED_IN, status).apply();
    }

    public boolean getLoggedInStatus() {
        return mPrefs.getBoolean(Keys.IS_LOGGED_IN, false);
    }

}
