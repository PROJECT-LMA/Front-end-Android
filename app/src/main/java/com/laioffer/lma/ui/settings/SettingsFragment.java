package com.laioffer.lma.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.laioffer.lma.OnBoardingActivity;
import com.laioffer.lma.R;
import com.laioffer.lma.model.User;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        Preference preference = findPreference(getString(R.string.username));
        final User user = User.getInstance(getContext());
        preference.setTitle(user.getFirstName() + " " + user.getLastName());
    }



}
/*public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView_firstName;
    private TextView textView_lastName;
    private TextView textView_location;
    private int num = 0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_setting);
        textView_firstName = (TextView) root.findViewById(R.id.first_name);
        textView_lastName = (TextView) root.findViewById(R.id.last_name);
        textView_location = (TextView) root.findViewById(R.id.location_id);
        final Context context = getContext();
        final User user = User.getInstance(context);
        textView_firstName.setText(user.getFirstName());
        textView_lastName.setText(user.getLastName());
        textView_location.setText(user.getLocationName());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView_firstName.setText(user.getFirstName());
                        textView_lastName.setText(user.getLastName());
                        textView_location.setText(user.getLocationName());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

        final Button button = (Button)root.findViewById(R.id.button_sign_out);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logout();
                user.saveUserStats(context);
                Intent launchActivity = new Intent(getActivity(), OnBoardingActivity.class);
                startActivity(launchActivity);
                getActivity().finish();
            }
        });
        return root;
    }
}*/


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