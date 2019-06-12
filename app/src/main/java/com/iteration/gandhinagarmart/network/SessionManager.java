package com.iteration.gandhinagarmart.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.iteration.gandhinagarmart.activity.HomeActivity;

import java.util.HashMap;

public class SessionManager {

    public static final String user_id = "user_id";
    public static final String user_name = "user_name";
    public static final String user_email = "user_email";

    // Sharedpref file name
    private static final String PREF_NAME = "userdetail";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    private Editor editor;
    // Context
    private Context _context;
    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String userId, String userName, String userEmail) {

        try {

            editor.putBoolean(IS_LOGIN, true);
            // Storing name in pref
            editor.putString(user_id, userId);
            editor.putString(user_name, userName);
            editor.putString(user_email, userEmail);

            // commit changes
            editor.commit();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public int checkLogin() {
        // Check login status

        int flag;

        if (!this.isLoggedIn()) {
            flag = 0;
        } else {
            flag = 1;
        }

        return flag;
    }

    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(user_id, pref.getString(user_id, null));
        user.put(user_name, pref.getString(user_name, null));
        user.put(user_email, pref.getString(user_email, null));

        return user;
    }

    /**
     * Clear session details
     */
    public void clearUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, HomeActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }
    private boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void RemoveUserdata() {
        editor.remove(user_id);
        editor.remove(user_name);
        editor.remove(user_email);

        editor.commit();
    }

}
