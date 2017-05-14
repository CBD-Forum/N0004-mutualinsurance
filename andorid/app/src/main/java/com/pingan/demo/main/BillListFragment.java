package com.pingan.demo.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.pingan.demo.MyApplication;
import com.pingan.demo.R;
import com.pingan.demo.adapter.BillListAdapter;
import com.pingan.demo.base.BaseFragment;
import com.pingan.demo.controller.FragmentManagerControl;
import com.pingan.demo.model.entity.Insurance;
import com.pingan.demo.model.entity.InsurancesRes;
import com.pingan.demo.model.service.InsurancesService;
import com.pingan.demo.refreshlist.XListView;
import com.pingan.http.framework.network.ServiceTask;
import com.pingan.http.framework.task.NetworkExcuter;
import com.pingan.http.framework.task.NetwrokTaskError;
import com.pingan.http.framework.task.ServiceCallback;

import java.util.ArrayList;

/**
 * Created by guolidong752 on 17/5/4.
 */

public class BillListFragment extends BaseFragment {
    InsurancesRes insurancesRes;
    private BaseFragment billDetailFragment;
    private XListView mListView;
    private ServiceCallback taskCallback = new ServiceCallback() {
        @Override
        public void onTaskStart(String serverTag) {
            content_ll.showProcess();
        }

        @Override
        public void onTaskSuccess(String serverTag) {
            if (serverTag.equals(InsurancesService.SERVICE_TAG_getInsurance)) {

            }
            MyApplication.getAppContext().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            });
            content_ll.removeProcess();
        }

        @Override
        public void onTaskFail(final NetwrokTaskError error, String serverTag) {

            insurancesRes.setData(new ArrayList<Insurance>());
            MyApplication.getAppContext().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), error.errorString, Toast.LENGTH_SHORT).show();
                    content_ll.showRequestError();
                }
            });
        }
    };

    public BillListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainLayout = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_bill_list, baseLayout);
        return baseLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mainLayout != null) {
            insurancesRes = new InsurancesRes();
            mListView = (XListView) mainLayout.findViewById(R.id.listView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getInsurances();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getInsurances();
        }
    }

    private void getInsurances() {
        ServiceTask serviceTask = new ServiceTask(InsurancesService.getInsurances(insurancesRes));
        serviceTask.setCancelable(true).setShowProcess(true);
        serviceTask.setCallback(taskCallback);
        NetworkExcuter.getInstance().excute(serviceTask, this);
    }


    private void initData() {
        BillListAdapter adapter = new BillListAdapter(getActivity(), insurancesRes.getData());
        mListView.setAdapter(adapter);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mListView.stopRefresh();
                getInsurances();
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
                if (position <= insurancesRes.getData().size()) {
                    billDetailFragment = new BillDetailFragment();
                    Bundle data = new Bundle();
                    data.putSerializable("Insurance", insurancesRes.getData().get(position - 1));
                    billDetailFragment.setArguments(data);
                    FragmentManagerControl.getInstance().addFragment(billDetailFragment);
                }
            }
        });
    }
}
