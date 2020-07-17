package com.laioffer.lma.ui.overview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.laioffer.lma.R;
import com.laioffer.lma.model.Machine;
import com.laioffer.lma.network.MachinesList;
import com.laioffer.lma.ui.overview.OverviewViewModel;

import java.util.List;

public class OverviewFragment extends Fragment {

    private OverviewViewModel overviewViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView;
    private int num = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe);
        textView = root.findViewById(R.id.text_dashboard);
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
                final List<Machine> list = MachinesList.checkMachineStatus();

                Handler threadHandler = new Handler(Looper.getMainLooper());
                threadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        int totalNum = list.size();
                        int num = countAvailableMachines(list);
                        textView.setText("The number of available machines are " + num + " out of " + totalNum);
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
            if (m.getIsAvailable() == "true") {
                count++;
            }
        }
        return count;
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