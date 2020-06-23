package com.web.leaftracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.web.leaftracker.R;
import com.web.leaftracker.interfaces.SupplyClickListener;
import com.web.leaftracker.model.SupplyModel;

import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

public class supplyadapter extends RecyclerView.Adapter<supplyadapter.ViewHolder> {
    private static String TAG = supplyadapter.class.getSimpleName();
    private ArrayList<SupplyModel> supplyArraylist;
    private SupplyClickListener clickListener;
    private Context context;

    public supplyadapter(Context context, SupplyClickListener clickListener, ArrayList<SupplyModel> supplyArraylist) {
        this.context = context;
        this.supplyArraylist = supplyArraylist;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supply_row_layout, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SupplyModel supply_item = supplyArraylist.get(position);
        holder.id.setText(supply_item.getId());
        holder.weight.setText(supply_item.getWeight());
        holder.date.setText(supply_item.getDate());
        if(supply_item.getConfirm_status()==0){
            holder.status.setText( context.getResources().getString(R.string.status_pending));
        }else if(supply_item.getConfirm_status()==1){
            holder.status.setText( context.getResources().getString(R.string.status_declined));
        }else if(supply_item.getConfirm_status()==2){
            holder.status.setText( context.getResources().getString(R.string.status_confirmed));
        }
        holder.data = position;
    }

    @Override
    public int getItemCount() {
        return supplyArraylist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id,date,weight,status;
        int data;
        SupplyClickListener listener;

        public ViewHolder(View itemView, SupplyClickListener listener) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.id);
            date = (TextView) itemView.findViewById(R.id.date);
            weight = (TextView) itemView.findViewById(R.id.teaWeight);
            status = (TextView) itemView.findViewById(R.id.status);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("TAG","clicked");
            if (listener != null) {
                listener.onSupplyClicked(data);
                Log.d("TAG","clicked new");
            }
        }
    }
}
