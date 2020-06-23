package com.web.leaftracker.home;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.web.leaftracker.R;
import com.web.leaftracker.adapter.factoryadapter;
import com.web.leaftracker.base.BaseFragment;
import com.web.leaftracker.interfaces.SupplyClickListener;
import com.web.leaftracker.interfaces.getfactoriesResponse;
import com.web.leaftracker.model.FactoryModel;

import java.util.ArrayList;

public class CreateNewOfferFragment extends BaseFragment implements getfactoriesResponse , View.OnClickListener , SupplyClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    Context context;
    ArrayList<FactoryModel> factoriesArrayList;
    int selcted_factory_id=0;

    public CreateNewOfferFragment() {
        // Required empty public constructor
    }

    public static CreateNewOfferFragment newInstance(String param1, String param2) {
        CreateNewOfferFragment fragment = new CreateNewOfferFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_new_offer, container, false);
        initdata(v);
        return  v;
    }

    private void initdata(View v) {
        factoriesArrayList = new ArrayList<>();
        TextInputLayout factory_select = v.findViewById(R.id.factory_select);
        TextInputLayout create_offer_button = v.findViewById(R.id.create);
        factory_select.setOnClickListener(this);
        create_offer_button.setOnClickListener(this);
    }

    @Override
    public void getfactoriesResponse(ArrayList<FactoryModel> factories) {
        factoriesArrayList = factories;
    }

    private void showFactoryList() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.factory_listing_alert, null);
            alertDialog.setCancelable(true);
            alertDialog.setView(dialogView);
            TextView title = dialogView.findViewById(R.id.factory_name);
            RecyclerView rv_factory_listing = dialogView.findViewById(R.id.rv_factory_listing);
            TextView submit = dialogView.findViewById(R.id.ok_btn);
            title.setText("Select Factory List");
            factoryadapter mAdapter = new factoryadapter(context, this, factoriesArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            rv_factory_listing.setLayoutManager(mLayoutManager);
            rv_factory_listing.setItemAnimator(new DefaultItemAnimator());
            rv_factory_listing.setAdapter(mAdapter);
            final AlertDialog show = alertDialog.show();
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show.dismiss();
                }
            });
            //alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
             case R.id.factory_select:
                    showFactoryList();
                    break;
            case R.id.create:
                    validate();
                    break;

        }
    }

    private void validate() {

    }

    @Override
    public void onSupplyClicked(int position) {
        selcted_factory_id = factoriesArrayList.get(position).getId();
    }
}