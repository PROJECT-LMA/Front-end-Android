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
    private Location location;

    User(boolean isLoggedIn, boolean isFirstVisit, boolean rememberLoggedIn, String firstName, String lastName, String token, Location location) {
        this.isLoggedIn = isLoggedIn;
        this.isFirstVisit = isFirstVisit;
        this.rememberLoggedIn = rememberLoggedIn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.token = token;
        this.location = location;
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

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() { return location; }

    // Log in the user
    static public void login(JSONObject payload) throws JSONException {
        JSONObject user = payload.getJSONObject("user");
        String token = payload.getString("token");
        instance.firstName = user.getString("firstName");
        instance.lastName = user.getString("lastName");
        instance.location.setId(user.getString("locationID"));
        instance.token = token;
        instance.isLoggedIn = true;
        if (!payload.isNull("location")) {
            JSONObject location = payload.getJSONObject("location");
            instance.location.setId(location.getString("_id"));
            instance.location.setName(location.getString("name"));
            instance.location.setDefaultPickupTime(location.getInt("defaultPickupTime"));
            instance.location.setDefaultReservationExpireTime(location.getInt("defaultReservationExpireTime"));
            instance.location.setDefaultRunningTime(location.getInt("defaultRunningTime"));
            instance.location.setAdminEmail(location.getString("email"));
        }
    }

    static public void logout() {
        instance.firstName = "";
        instance.lastName = "";
        instance.location = new Location();
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
        SharedPreferenceUtils.saveLocation(context, instance.location);
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

        Location location = readLocation(context);

        return new User(isLoggedIn, isFirstVisit, rememberLoggedIn, firstName, lastName, token, location);
    }

    static void saveLocation(Context context, Location location) {
        if (location == null) {
            writeAttributes(context, "locationId", "");
            writeAttributes(context, "locationName", "");
            writeAttributes(context, "locationDefaultRunningTime", "0");
            writeAttributes(context, "locationDefaultReservationExpireTime", "0");
            writeAttributes(context, "locationDefaultPickupTime", "0");
            writeAttributes(context, "locationEmail", "");
        } else {
            writeAttributes(context, "locationId", location.getId());
            writeAttributes(context, "locationName", location.getName());
            writeAttributes(context, "locationDefaultRunningTime", Integer.valueOf(location.getDefaultRunningTime()).toString());
            writeAttributes(context, "locationDefaultReservationExpireTime", Integer.valueOf(location.getDefaultReservationExpireTime()).toString());
            writeAttributes(context, "locationDefaultPickupTime", Integer.valueOf(location.getDefaultPickupTime()).toString());
            writeAttributes(context, "locationEmail", location.getAdminEmail());
        }
    }

    static Location readLocation(Context context) {
        SharedPreferences reader = context.getSharedPreferences(EDITOR_NAME, Context.MODE_PRIVATE);
        return new Location(
                reader.getString("locationId", ""),
                reader.getString("locationName", ""),
                Integer.valueOf(reader.getString("locationDefaultRunningTime", "0")),
                Integer.valueOf(reader.getString("locationDefaultReservationExpireTime", "0")),
                Integer.valueOf(reader.getString("locationDefaultPickupTime", "0")),
                reader.getString("locationEmail", "")
        );
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
