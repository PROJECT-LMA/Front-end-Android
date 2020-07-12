package com.laioffer.lma.network;

import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class Account {

    public static class AccountResult {
        private final boolean status;
        private final String message;

        public AccountResult(boolean status, String message) {
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
    //validate login account
    public static AccountResult userLogin(String password, String email){
        AccountResult result;
        HttpURLConnection conn = null;

        try{
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.login);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            JSONObject object = new JSONObject();
            object.put("email", email);
            object.put("password", password);

            HttpUtils.writeJsonObject(conn, object);
            conn.connect();

            JSONObject response = HttpUtils.readJsonObjectFromResponse(conn);
            Log.d("net", response.toString());

            if (response.getBoolean("isSuccess")) {
                result = new AccountResult(true, "Successfully login");
            }else{
                result = new AccountResult(false, "login fail");
            }
        }catch (Exception e) {
            e.printStackTrace();
            result = new AccountResult(false, "network error");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }

    public static AccountResult userRegister(String firstName, String lastName, String password,
                                            String email) {
        AccountResult result;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.register);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            JSONObject object = new JSONObject();
            object.put("email", email);
            object.put("password", password);
            object.put("firstName", firstName);
            object.put("lastName", lastName);
            HttpUtils.writeJsonObject(conn, object);
            conn.connect();

            JSONObject response = HttpUtils.readJsonObjectFromResponse(conn);
            Log.d("net", response.toString());
            if (response.getBoolean("isSuccess")) {
                result = new AccountResult(true, "Successfully registered");
            } else {
                result = new AccountResult(false, "This email address has been registered");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new AccountResult(false, "Network error");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }

    public static AccountResult checkEmail(String email) {
        AccountResult result;
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
            Log.d("net", response.toString());
            if (response.getBoolean("isAvailable")) {
                result = new AccountResult(true, "");
            } else {
                result = new AccountResult(false, "This email address has been used");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new AccountResult(true, "");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }
}
