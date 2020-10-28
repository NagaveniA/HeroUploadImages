package com.example.uploadimages.sessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.uploadimages.activities.LoginActivity;
import com.example.uploadimages.responsepojo.LoginPojo;

import java.util.HashMap;

public class LoginSessionManager {
    private static final String KEY_LAST_ACTIVITY = "last_activity" ;
    SharedPreferences sharedPreferences;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor, editor1;

    //context
    Context context;

    //shared pref mode
    int PRIVATE_MODE = 0;

    //Shared Pref File name
    private static final String PREFER_NAME = "Exstream";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    // User name (make variable public to access from outside)
    public static final String KEY_SHOWRROM = "showroom_name";
        public static final String KEY_Mobile = "mobile";
    public static final String KEY_email = "email";
    public static final String KEY_Accesstoken = "getAccesstoken";
    public static final String KEY_user_type = "user_type";
    public static final String KEY_Doj = "Doj";
    public static final String KEY_address = "address";


    //constructor with context parameter
    public LoginSessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        editor1 = sharedPreferences.edit();
    }


    //Create login session
    public void createUserLoginSession(String accessToken, String fullname, String mobile,
                                       String address, String email, String doj){
        // Storing login value as seat
        editor.putBoolean(IS_USER_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_SHOWRROM,fullname);
        // Storing email in pref
        editor.putString(KEY_Accesstoken, accessToken);
        editor.putString(KEY_address, address);
        editor.putString(KEY_email, email);
        editor.putString(KEY_Doj, doj);
        editor.putString(KEY_Mobile, mobile);



        // commit changes
        editor.commit();
    }

    public boolean checkLogin() {
        // Check login status
        if (!this.isUserLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);

            return true;
        }
        return false;
    }


    public HashMap<String, String> getUserDetails() {

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_SHOWRROM, sharedPreferences.getString(KEY_SHOWRROM, null));

//        user.put(KEY_user_type, sharedPreferences.getString(KEY_user_type, null));
        //accessToken
        user.put(KEY_Accesstoken, sharedPreferences.getString(KEY_Accesstoken, null));
        //session_name
        user.put(KEY_email, sharedPreferences.getString(KEY_email, null));
        user.put(KEY_Mobile, sharedPreferences.getString(KEY_Mobile, null));
//        user.put(KEY_Password, sharedPreferences.getString(KEY_Password, null));
        user.put(KEY_Doj, sharedPreferences.getString(KEY_Doj, null));
//        user.put(KEY_City, sharedPreferences.getString(KEY_City, null));
        user.put(KEY_address, sharedPreferences.getString(KEY_address, null));

        return user;
    }

    public void logoutUser() {
        editor.putBoolean(IS_USER_LOGIN, false);

        editor.clear();
        editor.commit();

    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }


    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(IS_USER_LOGIN,false);
    }


    public void createUserSession(LoginPojo loginPojo) {
        editor.putBoolean(IS_USER_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_SHOWRROM,loginPojo.getEmail());
        // Storing email in pref
        editor.putString(KEY_Accesstoken, loginPojo.getAccesstoken());
        editor.putString(KEY_address, loginPojo.getAddress());
        editor.putString(KEY_email, loginPojo.getEmail());
        editor.putString(KEY_Mobile, loginPojo.getMobile());



        // commit changes
        editor.commit();

    }
}

