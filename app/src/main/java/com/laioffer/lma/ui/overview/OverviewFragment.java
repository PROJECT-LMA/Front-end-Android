package com.laioffer.lma.ui.overview;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.laioffer.lma.R;
import com.laioffer.lma.ui.overview.OverviewViewModel;

public class OverviewFragment extends Fragment {

    private OverviewViewModel overviewViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView;
    private int num = 0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        overviewViewModel =
                ViewModelProviders.of(this).get(OverviewViewModel.class);
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe);
        final TextView textView = root.findViewById(R.id.text_dashboard);
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

        /*overviewViewModel =
                ViewModelProviders.of(this).get(OverviewViewModel.class);
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        overviewViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        /*View root = inflater.inflate(R.layout.fragment_settings, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe);
        textView = (TextView) root.findViewById(R.id.text_home);
        textView.setText("Overview, swipe count = 0");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        num++;
                        textView.setText("Overview, swipe count = " + num);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });*/
        return root;
    }
}