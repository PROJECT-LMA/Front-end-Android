package com.laioffer.lma.ui.account;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
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

import com.laioffer.lma.R;
import com.laioffer.lma.network.Account;
import com.laioffer.lma.ui.term.TermFragment;
import com.laioffer.lma.utils.EditTextValidator;
import com.laioffer.lma.utils.Encryption;


public class SignUpFragment extends Fragment implements TermFragment.OnAgreeTermListener  {

    CheckBox term;
    private Context context;
    View inflatedView;

    public static SignUpFragment newInstance() {

        Bundle args = new Bundle();

        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView =  inflater.inflate(R.layout.fragment_signup, container, false);

        /* get all fields */
        final Button registerBtn = inflatedView.findViewById(R.id.register);
        final EditText firstName = inflatedView.findViewById(R.id.first_name);
        final EditText lastName = inflatedView.findViewById(R.id.last_name);
        final EditText email = inflatedView.findViewById(R.id.username_register);
        final EditText password = inflatedView.findViewById(R.id.password_register);
        final CheckBox serviceAgreement = inflatedView.findViewById(R.id.term_of_service_checkbox);
        this.term = serviceAgreement;
        final TextView term = inflatedView.findViewById(R.id.term_of_service);
        Fragment self = this;

        // disable button until all fields are filled correctly
        registerBtn.setClickable(false);

        firstName.addTextChangedListener(new EditTextValidator(firstName) {
            @Override
            public void validate(TextView textView, String text) {
                if (text == null || text.length() == 0) {
                    textView.setError("Please enter your first name");
                    registerBtn.setClickable(false);
                } else {
                    registerBtn.setClickable(true);
                }
            }
        });

        lastName.addTextChangedListener(new EditTextValidator(lastName) {
            @Override
            public void validate(TextView textView, String text) {
                if (text == null || text.length() == 0) {
                    textView.setError("Please enter your last name");
                    registerBtn.setClickable(false);
                } else {
                    registerBtn.setClickable(true);
                }
            }
        });

        email.addTextChangedListener(new EditTextValidator(email) {
            @Override
            public void validate(final TextView textView, String text) {
                if (text == null || text.length() == 0) {
                    textView.setError("Please enter your email address");
                    registerBtn.setClickable(false);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    textView.setError("Not a valid email address");
                    registerBtn.setClickable(false);
                } else if (text.trim().isEmpty()) {
                    textView.setError("Not a valid email address");
                    registerBtn.setClickable(false);
                } else {
                    final Activity activity = getActivity();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Account.Result result = Account.checkEmail(email.getText().toString());
                            if (result.isStatus()) {
                                registerBtn.setClickable(true);
                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setError(result.getMessage());
                                    }
                                });
                                registerBtn.setClickable(false);
                            }
                        }
                    });
                    thread.start();
                }
            }
        });

        password.addTextChangedListener(new EditTextValidator(password) {
            @Override
            public void validate(TextView textView, String text) {
                if (text == null || text.length() < 6) {
                    textView.setError("Please must be at least 6 characters");
                    registerBtn.setClickable(false);
                } else {
                    registerBtn.setClickable(true);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstName.getText().toString().trim().isEmpty() ||
                lastName.getText().toString().trim().isEmpty() ||
                password.getText().toString().isEmpty() ||
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
                        final Account.Result result = Account.userRegister(
                                firstName.getText().toString(),
                                lastName.getText().toString(),
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
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewPager pager = (ViewPager)getActivity().findViewById(R.id.account_viewpager);
                                    pager.setCurrentItem(0, true);
                                }
                            });
                        }
                    }
                });
                thread.start();
            }
        });

        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment agreement = new TermFragment();
                agreement.setTargetFragment(self, 0);
                agreement.show(getParentFragmentManager(), "service agreement");
            }
        });

        return inflatedView;
    }

    @Override
    public void onAgreeTermListener() {
        term.setChecked(true);
    }
}