package com.laioffer.lma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.laioffer.lma.R;
import com.laioffer.lma.model.Machine;
import com.laioffer.lma.model.User;

import java.time.OffsetDateTime;
import java.util.List;

public class WasherAdapter extends RecyclerView.Adapter<WasherAdapter.ViewHolder> {
    private List<Machine> washers;
    User user;
    private Context context;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView endTime;
        public ImageView icon;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.washer_sn);
            txtFooter = (TextView) v.findViewById(R.id.washer_status);
            icon = (ImageView)v.findViewById(R.id.icon);
            endTime = (TextView) v.findViewById(R.id.washer_time);
        }


    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public WasherAdapter(List<Machine> washers) {
        this.washers = washers;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public WasherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.washer_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        user = User.getInstance(context);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element\
        Machine washer = washers.get(position);
        final String name = washer.getSN();
        String cur_status = null;
        if (washer.getIsAvailable() == "true") {
            cur_status = "Available";
        } else if (washer.getUserID().equals(user.getId())) {
            cur_status = "In use";
        } else {//reversed machines
            cur_status = "Reserved";
        }
        final String status = cur_status;
        String estimated_endTime = null;
        switch (status) {
            case "Available" :
                estimated_endTime = "";
                break;
            case "In use" :
                if(washer.getUserID().equals(user.getId())) {
                    holder.txtFooter.setTextColor(Color.parseColor("#FF7F50"));
                    holder.icon.setImageResource(R.drawable.using_ic_washer);
                }
                estimated_endTime = getEndTime(washer.getStartTime(), user.getLocation().getDefaultPickupTime());
                break;
            case "Reserved":
                if(washer.getUserID().equals(user.getId())) {
                    holder.txtFooter.setTextColor(Color.parseColor("#FF7F50"));
                    holder.icon.setImageResource(R.drawable.using_ic_washer);
                }
                estimated_endTime = getEndTime(washer.getStartTime(), user.getLocation().getDefaultReservationExpireTime());
                break;
        }
        final String endTime = estimated_endTime;



        holder.txtHeader.setText("ID: " + name);
        holder.txtFooter.setText(status);
        holder.endTime.setText(endTime);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return washers.size();
    }

    private String getEndTime(String startTime, int time) {
        Log.d("washer" , "washer start time" + startTime);
        Log.d("washer" , "minutes" + time);

        OffsetDateTime odt = OffsetDateTime.parse(startTime);
        OffsetDateTime end_odt = odt.plusMinutes(time);
        String output = end_odt.getHour() + ":" + end_odt.getMinute();
        Log.d("washer" , "output is" + output);
        return output;
    }
}