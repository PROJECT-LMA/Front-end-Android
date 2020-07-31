package com.laioffer.lma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laioffer.lma.R;
import com.laioffer.lma.model.Machine;
import com.laioffer.lma.model.User;

import java.time.OffsetDateTime;
import java.util.List;

public class WasherAdapter extends RecyclerView.Adapter<WasherAdapter.ViewHolder> {
    private List<Machine> washers;
    User user;
    private static final int FOOTER_VIEW = 1;
    private static final int ITEM_VIEW = 2;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView endTime;
        public TextView time_bar;
        public ImageView icon;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.washer_sn);
            txtFooter = (TextView) v.findViewById(R.id.washer_status);
            icon = (ImageView) v.findViewById(R.id.icon);
            endTime = (TextView) v.findViewById(R.id.washer_time);
            time_bar = (TextView) v.findViewById(R.id.washer_est_time);
        }
    }

    public static class FooterViewHolder extends WasherAdapter.ViewHolder {
        public View layout;

        public FooterViewHolder(View v) {
            super(v);
            layout = v;
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public WasherAdapter(List<Machine> washers) {
        this.washers = washers;

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public WasherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v;
        if (viewType == FOOTER_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_of_list_item, parent, false);

            return new FooterViewHolder(v);
        }
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        v =
                inflater.inflate(R.layout.washer_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        Context context = parent.getContext();
        user = User.getInstance(context);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element\
        if (holder instanceof WasherAdapter.FooterViewHolder) {
            WasherAdapter.FooterViewHolder footer_vh = (WasherAdapter.FooterViewHolder) holder;

        } else {
            Machine washer = washers.get(position);
            final String name = washer.getSN();
            String cur_status = null;
            if (washer.getIsAvailable().equals("true")) {
                cur_status = "Available";
            } else if (washer.getUserID().equals(user.getId())) {
                cur_status = "In use";
            } else {//reversed machines
                cur_status = "Reserved";
            }
            final String status = cur_status;
            boolean showTimeBar = false;
            String estimated_endTime = null;
            switch (status) {
                case "Available":
                    estimated_endTime = "";
                    break;
                case "In use":
                    if (washer.getUserID().equals(user.getId())) {
                        holder.txtFooter.setTextColor(Color.parseColor("#FF7F50"));
                        holder.icon.setImageResource(R.drawable.using_ic_washer);
                    }
                    estimated_endTime = getEndTime(washer.getStartTime(), user.getLocation().getDefaultRunningTime());
                    showTimeBar = true;
                    break;
                case "Reserved":
                    if (washer.getUserReservedID().equals(user.getId())) {
                        holder.txtFooter.setTextColor(Color.parseColor("#FF7F50"));
                        holder.icon.setImageResource(R.drawable.using_ic_washer);
                    }
                    estimated_endTime = getEndTime(washer.getStartTime(), user.getLocation().getDefaultRunningTime() + user.getLocation().getDefaultPickupTime());
                    showTimeBar = true;
                    break;
            }
            final String endTime = estimated_endTime;


            ((ViewHolder) holder).txtHeader.setText("ID: " + name);
            ((ViewHolder) holder).txtFooter.setText(status);
            ((ViewHolder) holder).endTime.setText(endTime);
            if(!showTimeBar) {
                ((ViewHolder) holder).time_bar.setText("");
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (washers == null) {
            return 0;
        }

        if (washers.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return washers.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == washers.size()) {
            return FOOTER_VIEW;
        }
        return ITEM_VIEW;
    }


    private String getEndTime(String startTime, int time) {
        Log.d("washer", "washer start time" + startTime);
        Log.d("washer", "minutes" + time);

        OffsetDateTime odt = OffsetDateTime.parse(startTime);
        OffsetDateTime end_odt = odt.plusMinutes(time);
        String output = end_odt.getHour() + ":" + end_odt.getMinute();
        Log.d("washer", "output is" + output);
        return output;
    }
}