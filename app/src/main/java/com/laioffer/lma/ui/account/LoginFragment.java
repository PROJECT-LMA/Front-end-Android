package com.laioffer.lma.ui.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.laioffer.lma.MainActivity;
import com.laioffer.lma.R;
import com.laioffer.lma.SetupActivity;
import com.laioffer.lma.model.User;
import com.laioffer.lma.network.Account;
import com.laioffer.lma.utils.EditTextValidator;
import com.laioffer.lma.utils.Encryption;


public class LoginFragment extends Fragment {
    public static LoginFragment newInstance() {
        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_login, container, false);

        final Activity activity = getActivity();
        final User user = User.getInstance(getContext());
        /* get all fields */
        final Button loginBtn = inflatedView.findViewById(R.id.login);
        final EditText email = inflatedView.findViewById(R.id.username);
        final EditText password = inflatedView.findViewById(R.id.password);
        final CheckBox rememberLoggedIn = inflatedView.findViewById(R.id.kept_logged_in_checkbox);
        final CheckBox serviceAgreement = inflatedView.findViewById(R.id.term_of_service_checkbox);

        // disable button until all fields are filled correctly
        loginBtn.setClickable(false);

        email.addTextChangedListener(new EditTextValidator(email) {
            @Override
            public void validate(final TextView textView, String text) {
                if (text == null || text.length() == 0) {
                    textView.setError("Please enter your email address");
                    loginBtn.setClickable(false);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    textView.setError("Not a valid email address");
                    loginBtn.setClickable(false);
                } else if (text.trim().isEmpty()) {
                    textView.setError("Not a valid email address");
                    loginBtn.setClickable(false);
                }
            }
        });

        password.addTextChangedListener(new EditTextValidator(password) {
            @Override
            public void validate(TextView textView, String text) {
                if (text == null || text.length() < 6) {
                    textView.setError("Please must be at least 6 characters");
                    loginBtn.setClickable(false);
                } else {
                    loginBtn.setClickable(true);
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().isEmpty() ||
                        email.getText().toString().isEmpty()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }

                if (!serviceAgreement.isChecked()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Please read and agree term of service", Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Account.Result result = Account.userLogin(
                                Encryption.md5Encryption(password.getText().toString()),
                                email.getText().toString());
                        Activity activity = getActivity();

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                        if (result.isStatus()) {
                            user.setRememberLoggedIn(rememberLoggedIn.isChecked());
                            user.saveUserStats(getContext());
                            Intent intent;
                            if (user.getLocationId().isEmpty()) {
                                intent = new Intent(activity, SetupActivity.class);
                            } else {
                                intent = new Intent(activity, MainActivity.class);
                            }
                            startActivity(intent);
                            activity.finish();
                        }
                    }
                });
                thread.start();
            }
        });

        return inflatedView;
    }

}