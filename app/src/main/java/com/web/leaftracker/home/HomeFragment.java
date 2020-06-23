package com.web.leaftracker.home;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.web.leaftracker.R;
import com.web.leaftracker.adapter.supplyadapter;
import com.web.leaftracker.base.BaseFragment;
import com.web.leaftracker.external.Constants;
import com.web.leaftracker.interfaces.SupplyClickListener;
import com.web.leaftracker.model.FactoryModel;
import com.web.leaftracker.model.SupplyModel;
import com.web.leaftracker.interfaces.getofferingsResponse;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements SupplyClickListener, getofferingsResponse {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView rv_vendor_supply;
    private supplyadapter rv_adapter;
    private ArrayList<SupplyModel> offeringlist;
    private FloatingActionButton create_offer;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        initData(v);
        iniListener();
        return  v;
    }

    private void iniListener() {
        create_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right)
                        .replace(R.id.frameContainer,new CreateNewOfferFragment(),
                                Constants.TAGCreateOfferFragment).commit();

            }
        });
    }

    private void initData(View v) {
        offeringlist = new ArrayList<>();
        create_offer = v.findViewById(R.id.action_create_offer);
        rv_vendor_supply = v.findViewById(R.id.rv_vendor_supply);
        rv_adapter = new supplyadapter(getContext(),this,offeringlist);
        rv_vendor_supply.setAdapter(rv_adapter);
        rv_vendor_supply.setLayoutManager(new LinearLayoutManager(getContext()));
        getOfferings(this);

    }

    @Override
    public void onSupplyClicked(int position) {

    }

    @Override
    public void getofferingsResponse(ArrayList<SupplyModel> offerings) {
        offeringlist = offerings;
    }
}