package com.pingan.demo.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.pingan.demo.MyApplication;
import com.pingan.demo.R;
import com.pingan.demo.UrlTools;
import com.pingan.demo.adapter.MyHomeListAdapter;
import com.pingan.demo.base.BaseFragment;
import com.pingan.demo.controller.FragmentManagerControl;
import com.pingan.demo.model.entity.ProfileRes;
import com.pingan.demo.model.service.LoginService;
import com.pingan.demo.refreshlist.XListView;
import com.pingan.http.framework.network.ServiceTask;
import com.pingan.http.framework.task.NetworkExcuter;
import com.pingan.http.framework.task.NetwrokTaskError;
import com.pingan.http.framework.task.ServiceCallback;

/**
 * Created by guolidong752 on 16/1/1.
 */

public class MyHomeFragment extends BaseFragment {
    private MyInsuranceDetailFragment myInsuranceDetailFragment;
    private ChargeFragment chargeFragment;
    private XListView mListView;
    private ProfileRes profileRes;
    private TextView balance;//充值余额
    private TextView count_helped;//资助人数
    private TextView fee;//分摊金额
    private ServiceCallback taskCallback = new ServiceCallback() {
        @Override
        public void onTaskStart(String serverTag) {
            content_ll.showProcess();
        }

        @Override
        public void onTaskSuccess(String serverTag) {
            MyApplication.getAppContext().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    initData();
                    content_ll.removeProcess();
                }
            });
        }

        @Override
        public void onTaskFail(final NetwrokTaskError error, String serverTag) {
            MyApplication.getAppContext().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getAppContext().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), error.errorString, Toast.LENGTH_SHORT)
                                    .show();
                            content_ll.showRequestError();
                        }
                    });
                }
            });
        }
    };

    public MyHomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainLayout = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_myhome, baseLayout);
        return baseLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mainLayout != null) {
            profileRes = new ProfileRes();
            mListView = (XListView) mainLayout.findViewById(R.id.list);
            balance = (TextView) mainLayout.findViewById(R.id.balance);
            count_helped = (TextView) mainLayout.findViewById(R.id.count_helped);
            fee = (TextView) mainLayout.findViewById(R.id.fee);

            mainLayout.findViewById(R.id.charge).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chargeFragment = new ChargeFragment();
                    Bundle data = new Bundle();
                    if (profileRes == null || profileRes.getData() == null) {
                        Toast.makeText(getActivity(), "至少加入一个互助计划才可以充值哦～", Toast.LENGTH_SHORT)
                                .show();
                        return;
                        //                        data.putString("getBalance", "0.00");
                        //                        data.putString("getFee", "0.00");
                    } else {
                        data.putString("getBalance",
                                String.valueOf(profileRes.getData().getBalance()));
                        data.putString("getFee", String.valueOf(profileRes.getData().getFee()));
                    }
                    chargeFragment.setArguments(data);
                    FragmentManagerControl.getInstance().addFragment(chargeFragment);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMeDetail();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getMeDetail();
        }
    }

    private void getMeDetail() {
        ServiceTask serviceTask = new ServiceTask(
                LoginService.getProfile(UrlTools.GET_ME_DETAIL, profileRes));
        serviceTask.setCancelable(true).setShowProcess(true);
        serviceTask.setCallback(taskCallback);
        NetworkExcuter.getInstance().excute(serviceTask, this);
    }


    private void initData() {

        if (profileRes == null || profileRes.getData() == null) {
            return;
        }
        balance.setText(String.valueOf(profileRes.getData().getBalance()));
        count_helped.setText(String.valueOf(profileRes.getData().getCount_helped()));
        fee.setText(String.valueOf(profileRes.getData().getFee()));

        MyHomeListAdapter adapter = new MyHomeListAdapter(getActivity(),
                profileRes.getData().getInsurances());
        mListView.setAdapter(adapter);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mListView.stopRefresh();
                getMeDetail();
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
                if (position <= profileRes.getData().getInsurances().size()) {
                    myInsuranceDetailFragment = new MyInsuranceDetailFragment();
                    Bundle data = new Bundle();
                    data.putSerializable("Insurance",
                            profileRes.getData().getInsurances().get(position - 1));
                    myInsuranceDetailFragment.setArguments(data);
                    FragmentManagerControl.getInstance().addFragment(myInsuranceDetailFragment);
                }
            }
        });
    }
}
