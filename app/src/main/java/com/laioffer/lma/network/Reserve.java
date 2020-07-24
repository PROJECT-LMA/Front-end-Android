package com.laioffer.lma.network;

import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class Reserve {

    static public class Result {
        private boolean isSuccess;
        private String message;
        private String machineId;

        public Result(boolean isSuccess, String message) {
            this.isSuccess = isSuccess;
            this.message = message;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public String getMessage() {
            return message;
        }

        public String getMachineId() {
            return machineId;
        }
    }

    public static Result reserveWasher(String token) {
        Result result;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.reverseWasher);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            JSONObject object = new JSONObject();
            object.put("token", token);
            HttpUtils.writeJsonObject(conn, object);
            conn.connect();

            JSONObject response = HttpUtils.readJsonObjectFromResponse(conn);

            Log.e("db", response.toString());

            if (response.getBoolean("isSuccess")) {
                result = new Result(true, response.getString("msg"));
            } else {
                result = new Result(false, response.getString("msg"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, "Network error");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }

    public static Result reserveDryer(String token) {
        Result result;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.reverseDryer);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            JSONObject object = new JSONObject();
            object.put("token", token);
            HttpUtils.writeJsonObject(conn, object);
            conn.connect();

            JSONObject response = HttpUtils.readJsonObjectFromResponse(conn);

            if (response.getBoolean("isSuccess")) {
                result = new Result(true, response.getString("msg"));
            } else {
                result = new Result(false, response.getString("msg"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, "Network error");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }
}
