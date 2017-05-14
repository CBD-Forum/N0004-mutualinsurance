package com.pingan.demo.controller;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.pingan.demo.R;
import com.pingan.demo.base.BaseFragment;

/**
 * Created by guolidong752 on 16/1/2.
 */

public class FragmentManagerControl {
    static private FragmentManagerControl instance;

    private FragmentManager fragmentManager;
    private BaseFragment current;

    private FragmentManagerControl() {

    }

    public static FragmentManagerControl getInstance() {
        if (instance == null) {
            instance = new FragmentManagerControl();
        }
        return instance;
    }

    public void setFragmentManager(FragmentManager manager) {
        fragmentManager = manager;
        fragmentManager
                .addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        fragmentManager.findFragmentById(R.id.main_view).onHiddenChanged(false);
                    }
                });
    }

    public void switchFragment(BaseFragment from, BaseFragment to) {
        if (from != to) {
            while (from != null && from.getChildFragmentManager().getBackStackEntryCount() > 0) {
                from.getChildFragmentManager().popBackStackImmediate();
            }
            while (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (!to.isAdded()) {
                if (from != null) {
                    transaction.hide(from).add(R.id.main_view, to).commit();
                } else {
                    transaction.add(R.id.main_view, to).commit();
                }
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }

    public void addFragment(BaseFragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_view, fragment);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    public FragmentManager getFragmanager() {
        return fragmentManager;
    }
}
