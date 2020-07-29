package com.laioffer.lma.ui.overview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.laioffer.lma.R;
import com.laioffer.lma.model.Machine;
import com.laioffer.lma.model.User;
import com.laioffer.lma.network.MachinesList;

import java.util.ArrayList;
import java.util.List;

public class OverviewFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    //private TextView totalAvailable;
    private TextView location_name;
    private TextView washer_count_text;
    private TextView dryer_count_text;
    private TextView dryerAvailable;
    private int num = 0;
    private User user;
    private RecyclerView washer_recyclerView;
    private RecyclerView.Adapter washerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        user = User.getInstance(getContext());
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        washer_recyclerView = (RecyclerView) root.findViewById(R.id.washer_recycler_list);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe);
        location_name = root.findViewById(R.id.location_bar);
        washer_count_text = root.findViewById(R.id.washer_count);
        dryer_count_text = root.findViewById(R.id.dryer_count);
        //washerAvailable = root.findViewById(R.id.washer);
        //dryerAvailable = root.findViewById(R.id.dryer);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe);


        //initialize Screen
        location_name.setText("Laundry Room : " + user.getLocation().getName());
        loadMachines();


        //swipe update
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        location_name.setText("Laundry Room : " + user.getLocation().getName());
                        loadMachines();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

        return root;
    }



    private void loadMachines() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Machine> list = MachinesList.checkMachineStatus(user.getLocation().getId());
                if (list == null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }

                Handler threadHandler = new Handler(Looper.getMainLooper());
                threadHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Washers washers = getWashers(list);
                        Dryers dryers = getDryers(list);
                        washer_count_text.setText(washers.availableCount + " of " + washers.totalCount + " available");
                        dryer_count_text.setText(dryers.availableCount + " of " + dryers.totalCount + " available");
                        list.clear();
                    }
                });
            }
        });
        thread.start();

    }

    private Washers getWashers(List<Machine> list){
        Washers washers = new Washers();
        for (Machine m : list){
            if (m.getMachineType().equals("washer")){
                if(m.getIsAvailable().equals("true")) {
                    washers.availableCount++;
                    washers.list.add(m);
                }
                washers.totalCount++;
            }
            Log.d("machines", m.getMachineType());
        }
        return washers;
    }

    private Dryers getDryers(List<Machine> list){
        Dryers dryers = new Dryers();
        for (Machine m : list){
            if (m.getMachineType().equals("dryer")){
                if(m.getIsAvailable().equals("true")) {
                    dryers.availableCount++;
                    dryers.list.add(m);
                }
                dryers.totalCount++;
            }
        }
        return dryers;
    }

    public class Washers{
        List<Machine> list = new ArrayList<>();
        int availableCount = 0;
        int totalCount = 0;
    }

    public class Dryers{
        List<Machine> list = new ArrayList<>();
        int availableCount = 0;
        int totalCount = 0;
    }
}

        /*

            private int countAvailableMachines(List<Machine> list) {
        int count = 0;
        for (Machine m : list) {
            if (m.getIsAvailable().equals("true")) {
                count++;
            }
        }
        return count;
    }
        overviewViewModel = new ViewModelProvider(this).get(OverviewViewModel.class);
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe);



        overviewViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        overviewViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(@Nullable String s) {
                                textView.setText(s);
                            }
                        });
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
*/