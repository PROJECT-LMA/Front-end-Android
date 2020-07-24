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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.laioffer.lma.R;
import com.laioffer.lma.model.Machine;
import com.laioffer.lma.model.User;
import com.laioffer.lma.network.MachinesList;

import java.util.ArrayList;
import java.util.List;

public class OverviewFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView totalAvailable;
    private TextView washerAvailable;
    private TextView dryerAvailable;
    private int num = 0;
    private User user;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        user = User.getInstance(getContext());
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe);
        totalAvailable = root.findViewById(R.id.text_dashboard);
        washerAvailable = root.findViewById(R.id.washer);
        dryerAvailable = root.findViewById(R.id.dryer);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe);
        loadMachines();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                final List<Machine> list = MachinesList.checkMachineStatus(user.getLocationId());

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
                        int totalNum = list.size();
                        int num = countAvailableMachines(list);

                        totalAvailable.setText(num + " out of " + totalNum + " machines are OPEN: ");
                        washerAvailable.setText(countWasher(list));
                        dryerAvailable.setText(countDryer(list));
                        //machines.setValue(list);
                        list.clear();
                    }
                });
            }
        });
        thread.start();
    }

    private int countAvailableMachines(List<Machine> list) {
        int count = 0;
        for (Machine m : list) {
            if (m.getIsAvailable().equals("true")) {
                count++;
            }
        }
        return count;
    }

    private String countWasher(List<Machine> list){
        StringBuilder washer = new StringBuilder();
        for (Machine m : list){
            if (m.getIsAvailable().equals("true") && m.getMachineType().equals("washer")){
                washer.append("#");
                washer.append(m.getSN());
                washer.append(System.getProperty("line.separator"));
            }
        }
        Log.d("err", washer.toString());
        return washer.toString();
    }

    private String countDryer(List<Machine> list){
        StringBuilder dryer = new StringBuilder();
        for (Machine m : list){
            if (m.getIsAvailable().equals("true") && m.getMachineType().equals("dryer")){
                dryer.append("#");
                dryer.append(m.getSN());
                dryer.append(System.getProperty("line.separator"));
            }
        }
        Log.d("err", dryer.toString());
        return dryer.toString();
    }
}

        /*overviewViewModel = new ViewModelProvider(this).get(OverviewViewModel.class);
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