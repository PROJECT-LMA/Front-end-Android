package com.laioffer.lma.network;

import androidx.appcompat.view.menu.MenuBuilder;

import com.laioffer.lma.model.Machine;
import com.laioffer.lma.model.User;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MachinesList {

    public static List<Machine> list = new ArrayList<>();
    private final static String TAG = "lifecycle";

    /*public static class Result {

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


    }*/

    public static List<Machine> checkMachineStatus() {
        HttpURLConnection conn = null;
        String tmp_locationID = "/5f0899e9c6c5dc6994fe6e5f";
        try {
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.machines + tmp_locationID);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //conn.connect();
            int responseCode = conn.getResponseCode();
            Log.d(TAG, "\nSending 'GET' request to URL : " + url);
            Log.d(TAG, "Response Code : " + responseCode);
            JSONObject response = HttpUtils.readJsonObjectFromResponse(conn);
            Log.d(TAG, "response is : " + response.toString());

            if (response.getBoolean("isSuccess")) {
                Log.d(TAG, "setting up machines");
                JSONArray array = response.getJSONArray("machines");

                for (int i = 0; i < array.length(); i++) {
                    Machine machine = new Machine();
                    machine.setupMachine(array.getJSONObject(i));
                    list.add(machine);
                }
                Log.d(TAG, "There are " + list.size() + " machines in the list");
            } else {
                Log.d(TAG, "not successful");
                //result = new Account.Result(false, "This email address has been registered");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exceptions!!!!");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }




        return list;
    }


    public static JSONObject getRequest() throws Exception {
        String url = "http://lmapp.us-east-2.elasticbeanstalk.com/api/machines/5f0899e9c6c5dc6994fe6e5f";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        Log.d(TAG, "\nSending 'GET' request to URL : " + url);
        Log.d(TAG, "Response Code : " + responseCode);
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print in String
        //System.out.println(response.toString());
        return new JSONObject(response.toString());
    }
}
