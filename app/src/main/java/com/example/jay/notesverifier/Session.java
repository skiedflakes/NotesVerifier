package com.example.jay.notesverifier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by jay on 13/02/2018.
 */

public class Session {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    public SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_COMPANY = "company_id";

    public static final String KEY_COMPANY_CODE = "key_company_code";

    public static final String KEY_COMPANY_NAME = "key_company_name";

    // Constructor
    public Session(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String password, String email, String company_id,String company_code,String company_name){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_PASSWORD, password);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing company id pref
        editor.putString(KEY_COMPANY, company_id);

        editor.putString(KEY_COMPANY_CODE, company_code);

        editor.putString(KEY_COMPANY_NAME, company_name);

        // commit changes
        editor.commit();
    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();

        // user password
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // user username
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user company id
        user.put(KEY_COMPANY, pref.getString(KEY_COMPANY, null));

        // user company id
        user.put(KEY_COMPANY_CODE, pref.getString(KEY_COMPANY_CODE, null));

        // user company id
        user.put(KEY_COMPANY_NAME, pref.getString(KEY_COMPANY_NAME, null));

        // return user
        return user;
    }


    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }



}


