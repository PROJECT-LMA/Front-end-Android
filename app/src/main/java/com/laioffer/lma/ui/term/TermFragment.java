package com.laioffer.lma.ui.term;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.laioffer.lma.R;


public class TermFragment extends DialogFragment {

    private OnAgreeTermListener callback;

    public interface OnAgreeTermListener {
        public void onAgreeTermListener();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (OnAgreeTermListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View term = inflater.inflate(R.layout.fragment_term, container, false);

        Button accept = term.findViewById(R.id.accept);
        TextView view = term.findViewById(R.id.term_page);

        view.setMovementMethod(new ScrollingMovementMethod());

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment loginFrag = getTargetFragment();
                callback.onAgreeTermListener();
                dismiss();
            }
        });

        return term;
    }
}