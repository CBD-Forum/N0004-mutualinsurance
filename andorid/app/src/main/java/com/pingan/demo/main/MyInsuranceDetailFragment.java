package com.pingan.demo.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pingan.demo.R;
import com.pingan.demo.adapter.MyInsuranceDetailListAdapter;
import com.pingan.demo.base.BaseFragment;
import com.pingan.demo.controller.FragmentManagerControl;
import com.pingan.demo.model.entity.Insurance;
import com.pingan.demo.model.entity.InsuranceSta;
import com.pingan.demo.refreshlist.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolidong752 on 16/1/1.
 */

public class MyInsuranceDetailFragment extends BaseFragment {
    private MyInsuranceHelpDetailFragment myInsuranceHelpDetailFragment;
    private MyInsuranceStatisticalFragment myInsuranceStatisticalFragment;
    private Insurance mInsurance;
    private TextView name;
    private TextView count_bought;
    private TextView count_helped;
    private TextView balance;
    private TextView fee;
    private XListView mListView;

    public MyInsuranceDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainLayout = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_my_insurance_detail, baseLayout);
        return baseLayout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mainLayout != null) {
            mInsurance = (Insurance) getArguments().getSerializable("Insurance");
            name = (TextView) mainLayout.findViewById(R.id.name);
            count_bought = (TextView) mainLayout.findViewById(R.id.count_bought);
            count_helped = (TextView) mainLayout.findViewById(R.id.count_helped);
            balance = (TextView) mainLayout.findViewById(R.id.balance);
            fee = (TextView) mainLayout.findViewById(R.id.fee);
            name.setText(mInsurance.getName());
            count_bought.setText(String.valueOf(mInsurance.getCount_bought()));
            count_helped.setText(String.valueOf(mInsurance.getCount_helped()));
            balance.setText(String.valueOf(mInsurance.getBalance()));
            fee.setText(String.valueOf(mInsurance.getFee()));
            mListView = (XListView) mainLayout.findViewById(R.id.list);

            mainLayout.findViewById(R.id.to_help_detail)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myInsuranceHelpDetailFragment = new MyInsuranceHelpDetailFragment();
                            Bundle data = new Bundle();
                            data.putSerializable("Insurance", mInsurance);
                            myInsuranceHelpDetailFragment.setArguments(data);
                            FragmentManagerControl.getInstance()
                                    .addFragment(myInsuranceHelpDetailFragment);
                        }
                    });
            mainLayout.findViewById(R.id.to_statistical)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myInsuranceStatisticalFragment = new MyInsuranceStatisticalFragment();
                            Bundle data = new Bundle();
                            data.putSerializable("Insurance", mInsurance);
                            myInsuranceStatisticalFragment.setArguments(data);
                            FragmentManagerControl.getInstance()
                                    .addFragment(myInsuranceStatisticalFragment);
                        }
                    });
            initData();
        }
    }


    private void initData() {
        if (mInsurance.getStatistics() == null) {
            List<InsuranceSta> stas = new ArrayList<>();
            InsuranceSta sta1 = new InsuranceSta();
            sta1.setMonth("2017-05");
            sta1.setCount_new(30);
            sta1.setCount_quit(6);
            sta1.setCount_helped(2);
            sta1.setAmount(207766);
            sta1.setCost(3000);
            sta1.setFee(5);
            InsuranceSta sta2 = new InsuranceSta();
            sta2.setMonth("2017-04");
            sta2.setCount_new(30);
            sta2.setCount_quit(6);
            sta2.setCount_helped(2);
            sta2.setAmount(207766);
            sta2.setCost(3000);
            sta2.setFee(5);
            InsuranceSta sta3 = new InsuranceSta();
            sta3.setMonth("2017-03");
            sta3.setCount_new(30);
            sta3.setCount_quit(6);
            sta3.setCount_helped(2);
            sta3.setAmount(207766);
            sta3.setCost(3000);
            sta3.setFee(5);
            stas.add(sta1);
            stas.add(sta2);
            stas.add(sta3);
            mInsurance.setStatistics(stas);
        }
        MyInsuranceDetailListAdapter adapter = new MyInsuranceDetailListAdapter(getActivity(),
                mInsurance.getStatistics());
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
    }
}
