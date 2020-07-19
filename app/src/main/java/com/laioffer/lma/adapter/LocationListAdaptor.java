package com.laioffer.lma.adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laioffer.lma.R;
import com.laioffer.lma.model.Location;

import java.util.List;

public class LocationListAdaptor extends RecyclerView.Adapter<LocationListAdaptor.MyViewHolder> {

    private List<Location> locations;
    private int selected;

    public LocationListAdaptor(List<Location> locations) {
        this.locations = locations;
        selected = -1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_list_item, parent, false);

        itemView.setClickable(true);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = locations.get(position).getName();
        holder.locationName.setText(name);

        holder.button.setChecked(selected == position);

        View.OnClickListener handleClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = position;
                notifyDataSetChanged();
            }
        };

        holder.view.setOnClickListener(handleClick);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView locationName;
        RadioButton button;
        View view;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            locationName = view.findViewById(R.id.location_name);
            button = view.findViewById(R.id.location_select);
        }

    }

    public String getSelectedLocationId() {
        if (selected == -1) {
            return null;
        }

        return locations.get(selected).getId();
    }

    public String getSelectedLocationName() {
        if (selected == -1) {
            return null;
        }

        return locations.get(selected).getName();
    }
}
