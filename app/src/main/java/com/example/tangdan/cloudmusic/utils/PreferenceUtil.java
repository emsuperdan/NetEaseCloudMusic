package com.example.tangdan.cloudmusic.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.tangdan.cloudmusic.utils.Constants.PREF_PROVIDER_FILE_NAMES;

public class PreferenceUtil {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private static volatile PreferenceUtil mSp;

    private PreferenceUtil(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_PROVIDER_FILE_NAMES, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static PreferenceUtil getInstance(Context context) {
        if (mSp == null) {
            synchronized (PreferenceUtil.class) {
                if (mSp == null) {
                    mSp = new PreferenceUtil(context);
                }
            }
        }
        return mSp;
    }

    public void putPreferenceString(String key, String value){
        mEditor.putString(key,value);
        mEditor.commit();
    }

    public String getPreferenceString(String key){
        return mSharedPreferences.getString(key,null);
    }
}

