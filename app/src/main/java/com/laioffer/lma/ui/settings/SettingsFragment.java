package com.laioffer.lma.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.laioffer.lma.LauncherActivity;
import com.laioffer.lma.LoginActivity;
import com.laioffer.lma.MainActivity;
import com.laioffer.lma.R;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView_firstName;
    private TextView textView_lastName;
    private int num = 0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_setting);
        textView_firstName = (TextView) root.findViewById(R.id.first_name);
        textView_lastName = (TextView) root.findViewById(R.id.last_name);
        textView_firstName.setText("haha 0");
        textView_lastName.setText("hoho 0");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        num++;
                        textView_firstName.setText("haha " + num);
                        textView_lastName.setText("hoho " + num);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

        final Button button = (Button)root.findViewById(R.id.button_sign_out);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity = new Intent(getActivity(), LauncherActivity.class);
                startActivity(launchActivity);
            }
        });
        return root;
    }
}