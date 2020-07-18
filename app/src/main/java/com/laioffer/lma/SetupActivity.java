package com.laioffer.lma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.laioffer.lma.adapter.LocationListAdaptor;
import com.laioffer.lma.model.Location;
import com.laioffer.lma.model.User;

import java.util.List;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        final Context context = this;
        final User user = User.getInstance(context);
        final Button saveBtn = findViewById(R.id.choose_location);

        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.location_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Location> locations = com.laioffer.lma.network.Location.getAllLocations().getLocations();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new LocationListAdaptor(locations));
                    }
                });
            }
        });
        thread.start();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // uncomment the following code if you want to jump to Main Activity
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();

//                final String selected = ((LocationListAdaptor)recyclerView.getAdapter()).getSelectedLocationId();
//                if (selected == null) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(context, "Please choose one wash room", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                } else {
//                    Thread thread1 = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            com.laioffer.lma.network.Location.Result result =
//                                    com.laioffer.lma.network.Location.setLocation(selected, user.getToken());
//
//                            if (result.isStatus()) {
//                                Toast.makeText(context, "Location set up", Toast.LENGTH_LONG).show();
//                                user.setLocationId(selected);
//                                user.saveUserStats(context);
//                                Intent intent = new Intent(context, MainActivity.class);
//                                startActivity(intent);
//                                finish();
//                            } else {
//                                Toast.makeText(context, "Network error", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                    thread1.start();
//                }
            }
        });
    }
}
