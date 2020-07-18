package com.laioffer.lma.model;

// eager init when app starts

import android.content.Context;
import android.content.SharedPreferences;
import android.util.NoSuchPropertyException;

import com.laioffer.lma.LauncherActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private static User instance;

    private boolean isLoggedIn;
    private boolean isFirstVisit;
    private boolean rememberLoggedIn;

    private String firstName;
    private String lastName;
    private String token;
    private String locationId;

    public User(boolean isLoggedIn, boolean isFirstVisit, boolean rememberLoggedIn, String firstName, String lastName, String token, String locationId) {
        this.isLoggedIn = isLoggedIn;
        this.isFirstVisit = isFirstVisit;
        this.rememberLoggedIn = rememberLoggedIn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.token = token;
        this.locationId = locationId;
    }

    // should only be called in Launcher Activity !!!
    synchronized public static void initializeUser(Context context) {
        if (context instanceof LauncherActivity) {
            instance = SharedPreferenceUtils.initializeUser(context);
        }
    }

    public static User getInstance(Context context) {
        if (instance == null) {
            initializeUser(context);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isFirstVisit() {
        return isFirstVisit;
    }

    public boolean isRememberLoggedIn() {
        return rememberLoggedIn;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getToken() {
        return token;
    }

    public String getLocationId() {
        return locationId;
    }

    // Log in the user
    static public void login(JSONObject user, String token) throws JSONException {
        instance.firstName = user.getString("firstName");
        instance.lastName = user.getString("lastName");
        instance.locationId = user.getString("locationID");
        instance.token = token;
        instance.isLoggedIn = true;
    }

    static public void logout() {
        instance.firstName = "";
        instance.lastName = "";
        instance.locationId = "";
        instance.token = "";
        instance.isLoggedIn = false;
        instance.rememberLoggedIn = false;
    }

    public void setFirstVisit(boolean firstVisit) {
        isFirstVisit = firstVisit;
    }

    public void setRememberLoggedIn(boolean rememberLoggedIn) {
        this.rememberLoggedIn = rememberLoggedIn;
    }

    public void saveUserStats(Context context) {
        SharedPreferenceUtils.writeAttributes(context, "isLoggedIn", instance.isLoggedIn);
        SharedPreferenceUtils.writeAttributes(context, "isFirstVisit", instance.isFirstVisit);
        SharedPreferenceUtils.writeAttributes(context, "rememberLoggedIn", instance.rememberLoggedIn);
        SharedPreferenceUtils.writeAttributes(context, "firstName", instance.firstName);
        SharedPreferenceUtils.writeAttributes(context, "lastName", instance.lastName);
        SharedPreferenceUtils.writeAttributes(context, "token", instance.token);
        SharedPreferenceUtils.writeAttributes(context, "locationId", instance.locationId);
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}

class SharedPreferenceUtils {

    private static final String EDITOR_NAME = "LMA";

    static User initializeUser(Context context) {
        SharedPreferences reader = context.getSharedPreferences(EDITOR_NAME, Context.MODE_PRIVATE);
        boolean rememberLoggedIn = reader.getBoolean("rememberLoggedIn", false);
        boolean isLoggedIn = rememberLoggedIn && reader.getBoolean("isLoggedIn", false);
        boolean isFirstVisit = reader.getBoolean("isFirstVisit", true);

        String firstName = isLoggedIn ? reader.getString("firstName", "") : "";
        String lastName = isLoggedIn ? reader.getString("lastName", "") : "";
        String token = isLoggedIn ? reader.getString("token", "") : "";
        String locationId = isLoggedIn ? reader.getString("locationId", "") : "";

        return new User(isLoggedIn, isFirstVisit, rememberLoggedIn, firstName, lastName, token, locationId);
    }

    static void writeAttributes(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(EDITOR_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    static void writeAttributes(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(EDITOR_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
