package com.laioffer.lma.ui.term;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.laioffer.lma.R;


public class TermFragment extends Fragment {

    private Button accept;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View term = inflater.inflate(R.layout.fragment_term, container, false);

         Button accept = term.findViewById(R.id.accept);


         accept.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

             }
        });
         return term;
    }
}