package com.furtadofagundes.myapplication.Fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.furtadofagundes.myapplication.Adapter.BottomBarAdapter;
import com.furtadofagundes.myapplication.R;


public class Principal extends Fragment implements ViewPager.OnPageChangeListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager viewPager;
    private OnFragmentInteractionListener mListener;
    private BottomNavigationView navigation;


    public Principal() {
        // Required empty public constructor
    }


    public static Principal newInstance(String param1, String param2) {
        Principal fragment = new Principal();
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
        View v = inflater.inflate(R.layout.inside, container, false);

        viewPager = v.findViewById(R.id.vp_pagina);


        navigation = v.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        BottomBarAdapter bottomBarAdapter = new BottomBarAdapter(getActivity().getSupportFragmentManager());
        bottomBarAdapter.addFragments(new Pesquisa());
        bottomBarAdapter.addFragments(new Produtos());
        bottomBarAdapter.addFragments(new Notificacoes());


        viewPager.setAdapter(bottomBarAdapter);
        viewPager.setOnPageChangeListener(this);

        viewPager.setCurrentItem(1);

        return v;
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    hideKeyboard();
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    hideKeyboard();
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    private void hideKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {

            case 0:
                navigation.setSelectedItemId(R.id.navigation_home);
                break;
            case 1:
                navigation.setSelectedItemId(R.id.navigation_dashboard);
                break;
            case 2:
                navigation.setSelectedItemId(R.id.navigation_notifications);
                break;


        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


//    @Override
//    public void onBackPressed() {
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            if (viewPager.getCurrentItem() == 0) {
//                super.onBackPressed();
//            } else {
//                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
//            }
//        }
//    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
