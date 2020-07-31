package com.laioffer.lma.ui.overview;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.laioffer.lma.R;
import com.laioffer.lma.adapter.DryerAdapter;
import com.laioffer.lma.adapter.WasherAdapter;
import com.laioffer.lma.model.Machine;
import com.laioffer.lma.model.User;
import com.laioffer.lma.network.MachinesList;
import com.laioffer.lma.network.Reserve;
import com.laioffer.lma.service.ReserveTimerService;

import java.util.ArrayList;
import java.util.List;

public class OverviewFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    //private TextView totalAvailable;
    private TextView location_name;
    private TextView washer_count_text;
    private TextView dryer_count_text;
    private TextView dryerAvailable;
    private Button reverseWasher_btn;
    private Button reverseDryer_btn;
    private int num = 0;
    private User user;
    private RecyclerView washer_recyclerView;
    private RecyclerView.Adapter washerAdapter;
    private RecyclerView.LayoutManager washer_layoutManager;
    private RecyclerView dryer_recyclerView;
    private RecyclerView.Adapter dryerAdapter;
    private RecyclerView.LayoutManager dryer_layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        user = User.getInstance(getContext());
        View root = inflater.inflate(R.layout.fragment_overview, container, false);

        washer_recyclerView = (RecyclerView) root.findViewById(R.id.washer_recycler_list);
        dryer_recyclerView = (RecyclerView) root.findViewById(R.id.dryer_recycler_list);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe);
        location_name = root.findViewById(R.id.location_bar);
        washer_count_text = root.findViewById(R.id.washer_count);
        dryer_count_text = root.findViewById(R.id.dryer_count);


        reverseWasher_btn = root.findViewById(R.id.reserve_washer_btn);
        reverseDryer_btn = root.findViewById(R.id.reserve_dryer_btn);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe);


        //initialize Screen
        location_name.setText(user.getLocation().getName());
        loadMachines();


        //swipe update, load machines and update lists
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        location_name.setText(user.getLocation().getName());
                        loadMachines();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        //reverse_setup
        setup_reserve();


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMachines();
    }

    private void setup_reserve() {
        reverseWasher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Reserve.Result result = Reserve.reserveWasher(user.getToken());
                        if (result.isSuccess()) {
                            Intent intent = new Intent(getActivity(), ReserveTimerService.class);
                            intent.putExtra("reservationTime", result.getEstimateTime());
                            getActivity().startService(intent);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                thread.start();
            }
        });

        reverseDryer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Reserve.Result result = Reserve.reserveDryer(user.getToken());
                        if (result.isSuccess()) {
                            Intent intent = new Intent(getActivity(), ReserveTimerService.class);
                            intent.putExtra("reservationTime", result.getEstimateTime());
                            getActivity().startService(intent);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                thread.start();
            }
        });
    }

    private void setupWashersRecyclerList(List<Machine> washers, Machine user_machine, Machine reserved_machine) {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        washer_recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        washer_layoutManager = new LinearLayoutManager(getContext());
        washer_recyclerView.setLayoutManager(washer_layoutManager);
        if (user_machine != null) {
            washers.add(0, user_machine);
        } else if (reserved_machine != null) {
            washers.add(0, reserved_machine);
        }
        // specify an adapter (see also next example)
        washerAdapter = new WasherAdapter(washers);
        washer_recyclerView.setAdapter(washerAdapter);


    }

    private void setupDryersRecyclerList(List<Machine> dryers, Machine user_machine, Machine reserved_machine) {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        dryer_recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        dryer_layoutManager = new LinearLayoutManager(getContext());
        dryer_recyclerView.setLayoutManager(dryer_layoutManager);

        if (user_machine != null) {
            dryers.add(0, user_machine);
        } else if (reserved_machine != null) {
            dryers.add(0, reserved_machine);
        }
        // specify an adapter (see also next example)
        dryerAdapter = new DryerAdapter(dryers);
        dryer_recyclerView.setAdapter(dryerAdapter);
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
                        if (washers.isReservable == false || washers.availableCount > 0) {
                            reverseWasher_btn.setBackgroundResource(R.drawable.unclickable_btn);
                            reverseWasher_btn.setEnabled(false);
                        }
                        Dryers dryers = getDryers(list);
                        if (dryers.isReservable == false || dryers.availableCount > 0) {
                            reverseDryer_btn.setBackgroundResource(R.drawable.unclickable_btn);
                            reverseDryer_btn.setEnabled(false);
                        }
                        washer_count_text.setText(washers.availableCount + " of " + washers.totalCount + " available");
                        dryer_count_text.setText(dryers.availableCount + " of " + dryers.totalCount + " available");
                        setupWashersRecyclerList(washers.available_list, washers.user_machine, washers.reserved_machine);
                        setupDryersRecyclerList(dryers.available_list, dryers.user_machine, dryers.reserved_machine);
                        list.clear();
                    }
                });
            }
        });
        thread.start();

    }

    private Washers getWashers(List<Machine> list) {
        Washers washers = new Washers();
        boolean machine_available_to_be_reserved = false;
        for (Machine m : list) {
            if (m.getMachineType().equals("washer")) {
                if (m.getIsAvailable().equals("true")) {
                    washers.availableCount++;
                    washers.available_list.add(m);
                } else if (m.getIsAvailable().equals("false") && m.getUserID().equals(user.getId())) {// user is using a washer
                    washers.user_machine = m;
                    washers.isReservable = false;
                } else if (m.getIsReserved().equals("true") && m.getUserReservedID().equals(user.getId())) { //user is reserving a washer
                    washers.reserved_machine = m;
                    washers.isReservable = false;
                } else if (m.getIsReserved().equals("false")) {
                    machine_available_to_be_reserved = true;
                }
                washers.totalCount++;
            }
        }
        if (machine_available_to_be_reserved == false) {
            washers.isReservable = false;
        }
        return washers;
    }

    private Dryers getDryers(List<Machine> list) {
        Dryers dryers = new Dryers();
        boolean machine_available_to_be_reserved = false;
        for (Machine m : list) {
            if (m.getMachineType().equals("dryer")) {
                if (m.getIsAvailable().equals("true")) {
                    dryers.availableCount++;
                    dryers.available_list.add(m);
                } else if (m.getIsAvailable().equals("false") && m.getUserID().equals(user.getId())) {
                    dryers.user_machine = m;
                    dryers.isReservable = false;
                } else if (m.getIsReserved().equals("true") && m.getUserReservedID().equals(user.getId())) {
                    dryers.reserved_machine = m;
                    dryers.isReservable = false;
                }else if (m.getIsReserved().equals("false")) {
                    machine_available_to_be_reserved = true;
                }
                dryers.totalCount++;
            }
        }
        if (machine_available_to_be_reserved == false) {
            dryers.isReservable = false;
        }

        return dryers;
    }

    public class Washers {
        List<Machine> available_list = new ArrayList<>();
        Machine user_machine;
        Machine reserved_machine;
        int availableCount = 0;
        int totalCount = 0;
        boolean isReservable = true;
    }

    public class Dryers {
        List<Machine> available_list = new ArrayList<>();
        Machine user_machine;
        Machine reserved_machine;
        int availableCount = 0;
        int totalCount = 0;
        boolean isReservable = true;
    }
}