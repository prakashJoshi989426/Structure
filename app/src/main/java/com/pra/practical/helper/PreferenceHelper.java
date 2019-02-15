package com.pra.practical.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/*
 * Copyright (c) 2015-2016 Sailfin Technologies, Pvt. Ltd.  All Rights Reserved.
 * This software is the confidential and proprietary information
 * (Confidential Information) of Sailfin Technologies, Pvt. Ltd.  You shall not
 * disclose or use Confidential Information without the express written
 * agreement of Sailfin Technologies, Pvt. Ltd.
 */

public class PreferenceHelper {

    // PREFERENCE TAG
    public static final int GET_STRING = 0;
    public static final int GET_INT = 1;
    public static final int GET_LONG = 2;
    public static final int GET_FLOAT = 3;
    public static final int GET_BOOLEAN = 4;

    // Shared Preferences
    SharedPreferences mSharedPreferences;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared mSharedPreferences mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "cfo_g";


    public PreferenceHelper(Context context) {
        this._context = context;
        mSharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }

    private static final String CATEGORYID = "category_id";

    private static SharedPreferences sharedPreferences = null;

    /**
     * open preference this call method in method
     *
     * @param context
     */
    public static void openPref(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * this method is used for get string value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getValue(Context context, String key,
                                  String defaultValue) {
        try {
            PreferenceHelper.openPref(context);
            String result = PreferenceHelper.sharedPreferences.getString(key, defaultValue);
            PreferenceHelper.sharedPreferences = null;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    /**
     * this method is used for set int value
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setValue(Context context, String key, int value) {
        try {
            PreferenceHelper.openPref(context);
            Editor prefsPrivateEditor = PreferenceHelper.sharedPreferences.edit();
            prefsPrivateEditor.putInt(key, value);
            prefsPrivateEditor.commit();
            prefsPrivateEditor = null;
            PreferenceHelper.sharedPreferences = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is used for get string value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getValue(Context context, String key,
                               int defaultValue) {
        try {
            PreferenceHelper.openPref(context);
            int result = PreferenceHelper.sharedPreferences.getInt(key, defaultValue);
            PreferenceHelper.sharedPreferences = null;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * this method is used for set string value
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setValue(Context context, String key, String value) {
        try {
            PreferenceHelper.openPref(context);
            Editor prefsPrivateEditor = PreferenceHelper.sharedPreferences.edit();
            prefsPrivateEditor.putString(key, value);
            prefsPrivateEditor.commit();
            prefsPrivateEditor = null;
            PreferenceHelper.sharedPreferences = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is used for get boolen value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getValue(Context context, String key,
                                   boolean defaultValue) {
        try {
            PreferenceHelper.openPref(context);
            boolean result = PreferenceHelper.sharedPreferences.getBoolean(key, defaultValue);
            PreferenceHelper.sharedPreferences = null;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    /**
     * this method is used for set boolen value
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setValue(Context context, String key, boolean value) {
        try {
            PreferenceHelper.openPref(context);
            Editor prefsPrivateEditor = PreferenceHelper.sharedPreferences.edit();
            prefsPrivateEditor.putBoolean(key, value);
            prefsPrivateEditor.commit();
            prefsPrivateEditor = null;
            PreferenceHelper.sharedPreferences = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete all sp value
     *
     * @param context
     */
    public static void deleteAll(Context context) {
        try {
            PreferenceHelper.openPref(context);
            PreferenceHelper.sharedPreferences.edit().clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
