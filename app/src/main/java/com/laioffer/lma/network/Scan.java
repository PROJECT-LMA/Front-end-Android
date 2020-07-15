package com.laioffer.lma.network;

import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class Scan {

    public static class ScanResult {

        private final boolean status;
        private final String message;

        public ScanResult(boolean status, String message) {
            this.status = status;
            this.message = message;
        }

        public boolean isStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

    // check washer isAvailable or not
    public static Scan.ScanResult scanToOpen(String scanString, String token) {
        Scan.ScanResult result;
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
            if (response.getBoolean("isSuccess")) {
                result = new Scan.ScanResult(true, "Washer is available!");
            } else {
                result = new Scan.ScanResult(false, response.getString("msg"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new Scan.ScanResult(false, "Network error");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }

    // scan to get user's clothes
    public static Scan.ScanResult scanToClose(String scanString, String token) {
        Scan.ScanResult result;
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
                result = new Scan.ScanResult(true, "Thank you for using!");
            } else {
                result = new Scan.ScanResult(false, "Error!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new Scan.ScanResult(false, "Network error");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }
}