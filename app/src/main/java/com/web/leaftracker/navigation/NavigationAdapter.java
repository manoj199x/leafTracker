package com.web.leaftracker.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.web.leaftracker.R;
import com.web.leaftracker.external.ServiceManager;

import java.util.ArrayList;


public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.viewholder> {
    private static String TAG = NavigationAdapter.class.getSimpleName();
    static ArrayList<NavigationModel> navigationModelArrayList;
    Context context;
    private ClickListner clickListener;


    public NavigationAdapter(Context context, ClickListner clickListener, ArrayList<NavigationModel> navigationModelArrayList) {
        this.navigationModelArrayList = navigationModelArrayList;
        this.clickListener = clickListener;
        this.context = context;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_drawer, parent, false);
        return new viewholder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(viewholder holder, int position) {
        try {
            NavigationModel navigationModel = navigationModelArrayList.get(position);
            String nav_item = navigationModel.getName();
            holder.drawer_item.setText(nav_item);
            switch (nav_item) {
                case "Home":
                    holder.drawer_image.setImageResource(R.drawable.ic_home);
                    break;

                case "Account":
                    holder.drawer_image.setImageResource(R.drawable.ic_account);
                    break;

                case "Logout":
                    holder.drawer_image.setImageResource(R.drawable.ic_logout);
                    break;


            }

            ServiceManager.loadAnimation(holder.itemView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return navigationModelArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView drawer_item;
        ClickListner listener;
        ImageView drawer_image;

        public viewholder(View itemView, ClickListner listener) {
            super(itemView);
            this.listener = listener;
            drawer_item = (TextView) itemView.findViewById(R.id.navigation_text);
            drawer_image = (ImageView) itemView.findViewById(R.id.drawer_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(navigationModelArrayList.get(getAdapterPosition()).getId());
            }
        }
    }
}
