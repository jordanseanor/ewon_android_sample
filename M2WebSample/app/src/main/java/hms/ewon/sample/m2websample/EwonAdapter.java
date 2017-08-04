package hms.ewon.sample.m2websample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ewon.Ewon;
import ewon.LanDevice;

/**
 * Created by jorda on 7/18/2017.
 */

public class EwonAdapter extends RecyclerView.Adapter<EwonAdapter.EwonViewHolder> {
    List<Ewon> devices;
    Context context;
    public EwonAdapter(List<Ewon> ewons, Context context){
        this.devices = ewons;
        this.context = context;
    }

    @Override
    public EwonAdapter.EwonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ewon_list_item, parent, false);
        return new EwonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EwonAdapter.EwonViewHolder holder, int position) {
        Ewon ewon = devices.get(position);
        holder.deviceName.setText("Device Name: " + ewon.getName());
        holder.txtDescription.setText("Description: " + ewon.getDescription());
        if(ewon.getStatus().equalsIgnoreCase("online")){
            holder.imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.online));
        }else{
            holder.imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.offline));
        }

        if(ewon.getLanDevices().size() > 0){
            holder.lanDevices.setText("LAN Devices: \n");
            for(LanDevice lan: ewon.getLanDevices()){
                holder.lanDevices.append("        Name: " + lan.getName() + "\n");
            }
        }
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class EwonViewHolder extends RecyclerView.ViewHolder{
        public TextView deviceName, txtDescription, lanDevices;
        public ImageView imgStatus;

        public EwonViewHolder(View view){
            super(view);
            deviceName = (TextView) view.findViewById(R.id.txtDeviceName);
            txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            lanDevices = (TextView) view.findViewById(R.id.lanDevices);
            imgStatus = (ImageView) view.findViewById(R.id.imgStatus);
        }
    }
}
