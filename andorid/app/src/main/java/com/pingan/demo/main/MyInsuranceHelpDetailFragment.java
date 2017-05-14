package com.pingan.demo.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.pingan.demo.R;
import com.pingan.demo.adapter.HelpListAdapter;
import com.pingan.demo.base.BaseFragment;
import com.pingan.demo.model.entity.Insurance;
import com.pingan.demo.refreshlist.XListView;

/**
 * Created by guolidong752 on 16/1/1.
 */

public class MyInsuranceHelpDetailFragment extends BaseFragment {

    private XListView mListView;
    private Insurance mInsurance;

    public MyInsuranceHelpDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainLayout = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_insurance_help_detail, baseLayout);
        return baseLayout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mainLayout != null) {
            mInsurance = (Insurance) getArguments().getSerializable("Insurance");
            mListView = (XListView) mainLayout.findViewById(R.id.list);
            initData();
        }
    }


    private void initData() {
        HelpListAdapter adapter = new HelpListAdapter(getActivity(), mInsurance.getClaims());
        mListView.setAdapter(adapter);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mListView.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                mListView.stopLoadMore();
            }

            @Override
            public void changeShortcut(boolean isShow) {

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                return;
            }
        });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }
}
