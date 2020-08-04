package com.laioffer.lma.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.laioffer.lma.R;
import com.laioffer.lma.model.Machine;
import com.laioffer.lma.model.User;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DryerAdapter extends RecyclerView.Adapter<DryerAdapter.ViewHolder> {
    private List<Machine> dryers;
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

            txtHeader = (TextView) v.findViewById(R.id.dryer_sn);
            txtFooter = (TextView) v.findViewById(R.id.dryer_status);
            icon = (ImageView)v.findViewById(R.id.icon_dryer);
            time_bar = (TextView) v.findViewById(R.id.dryer_est_time);
            endTime = (TextView) v.findViewById(R.id.dryer_time);
        }
    }

    public static class FooterViewHolder extends ViewHolder {
        public View layout;
        public FooterViewHolder(View v) {
            super(v);
            layout = v;
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public DryerAdapter(List<Machine> dryers) {
        this.dryers = dryers;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public DryerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
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
                inflater.inflate(R.layout.dryer_list_item, parent, false);
        DryerAdapter.ViewHolder vh = new ViewHolder(v);
        Context context = parent.getContext();
        user = User.getInstance(context);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull DryerAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footer_vh = (FooterViewHolder) holder;

        } else {
            Machine dryer = dryers.get(position);

            final String name = dryer.getSN();
            String cur_status = null;
            if (dryer.getIsAvailable().equals("true")) {
                cur_status = "Available";
            } else if (dryer.getUserID().equals(user.getId())) {
                cur_status = "In use";
            } else {//reversed machines
                cur_status = "Reserved";
            }
            final String status = cur_status;
            boolean showTimeBar = false;
            String estimated_endTime = null;
            switch (status) {
                case "Available" :
                    estimated_endTime = "";
                    break;
                case "In use" :
                    if(dryer.getUserID().equals(user.getId())) {
                        holder.txtFooter.setTextColor(Color.parseColor("#FF7F50"));
                        holder.icon.setImageResource(R.drawable.using_ic_dryer);
                    }
                    estimated_endTime = getEndTime(dryer.getStartTime(), user.getLocation().getDefaultRunningTime()); //after finished, calculate the pick up time //warning
                    showTimeBar = true;
                    break;
                case "Reserved":
                    if(dryer.getUserReservedID().equals(user.getId())) {
                        holder.txtFooter.setTextColor(Color.parseColor("#FF7F50"));
                        holder.icon.setImageResource(R.drawable.using_ic_dryer);
                    }
                    estimated_endTime = getEndTime(dryer.getStartTime(), user.getLocation().getDefaultRunningTime() + user.getLocation().getDefaultPickupTime()); //location.defaultRunningTime - helper.millisToMinutes(Date.now() - dryer.startTime) + location.defaultPickupTime;
                    showTimeBar = true;
                    break;
            }
            final String endTime = estimated_endTime;
            String text_id_name = "ID: " + name;
            ((ViewHolder) holder).txtHeader.setText(text_id_name);
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
        if (dryers == null) {
            return 0;
        }

        if (dryers.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return dryers.size() + 1;
    }


    private String getEndTime(String startTime, int time) {
        Log.d("washer" , "dryer start time" + startTime);
        Log.d("washer" , "minutes" + time);
        OffsetDateTime odt = OffsetDateTime.parse(startTime);
        OffsetDateTime end_odt = odt.plusMinutes(time);
        Log.d("washer" , "output is " + end_odt.getHour() + ":" + end_odt.getMinute());
        return end_odt.getHour() + ":" + end_odt.getMinute();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == dryers.size()) {
            return FOOTER_VIEW;
        }
        return ITEM_VIEW;
    }

}
