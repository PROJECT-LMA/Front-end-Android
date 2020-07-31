package com.laioffer.lma.network;


import com.laioffer.lma.model.Machine;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MachinesList {

    public static List<Machine> checkMachineStatus(String locationID) {
        List<Machine> list = new ArrayList<>();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(HttpUtils.serverUrl + HttpUtils.machines + '/' + locationID);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //conn.connect();
            int responseCode = conn.getResponseCode();
            JSONObject response = HttpUtils.readJsonObjectFromResponse(conn);

            if (response.getBoolean("isSuccess")) {
                JSONArray array = response.getJSONArray("machines");

                for (int i = 0; i < array.length(); i++) {
                    Machine machine = new Machine();
                    machine.setupMachine(array.getJSONObject(i));
                    list.add(machine);
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            list = null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return list;
    }

}
