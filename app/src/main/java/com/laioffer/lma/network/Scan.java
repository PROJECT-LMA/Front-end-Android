package com.laioffer.lma.network;

import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class Scan {
    // washer isAvailable
    // scanToClose? token?
    public static Account.AccountResult scanToOpen(String scanString, String token) {
        Account.AccountResult result;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.scanToOpen);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            JSONObject object = new JSONObject();
            object.put("scanString", scanString);
            object.put("token", token);
            HttpUtils.writeJsonObject(conn, object);
            conn.connect();

            JSONObject response = HttpUtils.readJsonObjectFromResponse(conn);
            if (response.getBoolean("isUsing")) {
                result = new Account.AccountResult(false, "Washer is not available!");
            } else {
                result = new Account.AccountResult(true, "Washer is available!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new Account.AccountResult(false, "Network error");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }

    public static Account.AccountResult scanToClose(String scanString, String token) {
        Account.AccountResult result;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.scanToClose);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            JSONObject object = new JSONObject();
            object.put("scanString", scanString);
            object.put("token", token);
            HttpUtils.writeJsonObject(conn, object);
            conn.connect();

            JSONObject response = HttpUtils.readJsonObjectFromResponse(conn);
            if (response.getBoolean("isSuccess")) {
                result = new Account.AccountResult(true, "Thank you for using!");
            } else {
                result = new Account.AccountResult(false, "Error!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new Account.AccountResult(false, "Network error");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }
}