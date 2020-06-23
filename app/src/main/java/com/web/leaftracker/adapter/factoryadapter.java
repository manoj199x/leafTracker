package com.web.leaftracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.web.leaftracker.R;
import com.web.leaftracker.interfaces.SupplyClickListener;
import com.web.leaftracker.model.FactoryModel;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class factoryadapter extends RecyclerView.Adapter<factoryadapter.ViewHolder> {
    private static String TAG = factoryadapter.class.getSimpleName();
    private ArrayList<FactoryModel> factoryArraylist;
    private SupplyClickListener clickListener;
    private Context context;

    public factoryadapter(Context context, SupplyClickListener clickListener, ArrayList<FactoryModel> factoryArraylist) {
        this.context = context;
        this.factoryArraylist = factoryArraylist;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.factory_item_row, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FactoryModel factory_item = factoryArraylist.get(position);
        holder.factory_name.setText(factory_item.getFactory_name());
        holder.data = position;
    }

    @Override
    public int getItemCount() {
        return factoryArraylist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView factory_name;
        RadioButton select_value;
        int data;
        SupplyClickListener listener;

        public ViewHolder(View itemView, SupplyClickListener listener) {
            super(itemView);
            factory_name = (TextView) itemView.findViewById(R.id.factory_name);
            select_value = (RadioButton) itemView.findViewById(R.id.radio_select);
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
