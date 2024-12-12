package com.example.myapplication;


import static android.content.Context.MODE_PRIVATE;
import static android.media.MediaCodec.MetricsConstants.MODE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class PreferenceHelper {

    private SharedPreferences app_prefs;
    private Context context;



    public PreferenceHelper(Context context) {
        this.context = context;
        app_prefs = context.getSharedPreferences("shared", 0);

    }

    public void set_location(String location) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("location", location);
        edit.apply();
    }

    public String get_location() {
        String location = app_prefs.getString("location", "");
        return location;
    }

    public void set_Bundle_string(String bundle_string) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("bundle_string", bundle_string);
        edit.apply();
    }



    public void set_service_running(Boolean status) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean("running", status);
        edit.apply();

    }

    public boolean get_service_running() {
        boolean running = app_prefs.getBoolean("running", false);
        return running;
    }


    public void setUser_email(String user_email) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("user_email", user_email);
        edit.apply();

    }

    public void set_receiver_User_email(String receiver_user_email) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("receiver_user_email", receiver_user_email);
        edit.apply();

    }

    public String get_receiver_User_email() {
        String user_email = app_prefs.getString("receiver_user_email", "");
        return user_email;
    }

    public void set_sender_User_email(String sende_user_email) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("sender_user_email", sende_user_email);
        edit.apply();

    }

    public String get_sender_User_email() {
        String user_email = app_prefs.getString("sender_user_email", "");
        return user_email;
    }

    public void set_imageurl(String umageurl) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("image_url", umageurl);
        edit.apply();

    }

    public String get_imageurl() {
        String user_email = app_prefs.getString("image_url", "");
        return user_email;
    }


    public String getUser_email() {
        String user_email = app_prefs.getString("user_email", "");
        return user_email;
    }

    public void setLogin(boolean login) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean("login", login);
        edit.apply();
    }

public void clear(){
    SharedPreferences pref = app_prefs;
    SharedPreferences.Editor editor = pref.edit();
    editor.clear();
    editor.commit();
}

    public boolean getLogin() {
        boolean login = app_prefs.getBoolean("login", false);
        return login;
    }


}


