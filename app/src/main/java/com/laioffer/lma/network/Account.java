package com.laioffer.lma.network;

import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class Account {

    public static class Result {

        private final boolean status;
        private final String message;

        public Result(boolean status, String message) {
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

    public static Result userRegister(String firstName, String lastName, String password,
                                            String email) {
        Result result;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.register);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            JSONObject object = new JSONObject();
            object.put("email", email);
            object.put("password", password);
            object.put("firstName", firstName);
            object.put("lastName", lastName);
            HttpUtils.writeJsonObject(conn, object);
            conn.connect();

            JSONObject response = HttpUtils.readJsonObjectFromResponse(conn);
            if (response.getBoolean("isSuccess")) {
                result = new Result(true, "Successfully registered");
            } else {
                result = new Result(false, "This email address has been registered");
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

    public static Result checkEmail(String email) {
        Result result;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.checkEmail);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            JSONObject object = new JSONObject();
            object.put("email", email);
            HttpUtils.writeJsonObject(conn, object);
            conn.connect();

            JSONObject response = HttpUtils.readJsonObjectFromResponse(conn);
            if (response.getBoolean("isAvailable")) {
                result = new Result(true, "");
            } else {
                result = new Result(false, "This email address has been used");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(true, "");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }
}
