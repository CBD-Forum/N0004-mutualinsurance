package com.pingan.demo.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pingan.demo.MyApplication;
import com.pingan.demo.R;
import com.pingan.demo.SpUtils;
import com.pingan.demo.UrlTools;
import com.pingan.demo.adapter.PayExListAdapter;
import com.pingan.demo.base.BaseFragment;
import com.pingan.demo.controller.UserController;
import com.pingan.demo.model.entity.Insurance;
import com.pingan.demo.model.entity.InsuranceReq;
import com.pingan.demo.model.entity.ProfileRes;
import com.pingan.demo.model.service.LoginService;
import com.pingan.http.framework.network.ServiceTask;
import com.pingan.http.framework.task.NetworkExcuter;
import com.pingan.http.framework.task.NetwrokTaskError;
import com.pingan.http.framework.task.ServiceCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolidong752 on 17/5/4.
 */

public class PayFragment extends BaseFragment {
    private ExpandableListView expandableListView;
    private PayExListAdapter payExListAdapter;
    private List<String> groupList;
    private List<List<IPayEntry>> itemList;
    private ProfileRes profileRes;
    private List<Insurance> mInsurances;
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
                    Toast.makeText(getActivity(), error.errorString, Toast.LENGTH_SHORT).show();
                    content_ll.showRequestError();
                }
            });
        }
    };

    public PayFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainLayout = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_pay, baseLayout);
        return baseLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mainLayout != null) {
            profileRes = new ProfileRes();
            expandableListView = (ExpandableListView) mainLayout.findViewById(R.id.exlist);
            expandableListView.setGroupIndicator(null);
            expandableListView
                    .setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v,
                                                    int groupPosition, long id) {

                            if (itemList == null || itemList.size() > groupPosition && itemList
                                    .get(groupPosition).isEmpty()) {
                                return true;
                            }
                            return false;
                        }
                    });
            expandableListView
                    .setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v,
                                                    int groupPosition, int childPosition, long id) {
                            return false;
                        }
                    });
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getMeDetail();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMeDetail();
    }

    private void getMeDetail() {
        ServiceTask serviceTask = new ServiceTask(
                LoginService.getProfile(UrlTools.GET_ME_DETAIL, profileRes));
        serviceTask.setCancelable(true).setShowProcess(true);
        serviceTask.setCallback(taskCallback);
        NetworkExcuter.getInstance().excute(serviceTask, this);
    }

    private void initData() {
        groupList = new ArrayList<>();
        itemList = new ArrayList<>();
        if (profileRes != null && profileRes.getData() != null
                && profileRes.getData().getInsurances() != null) {
            UserController.getInstance().setProfile(profileRes.getData());
            mInsurances = profileRes.getData().getInsurances();
            for (int i = 0; i < mInsurances.size(); i++) {
                groupList.add(mInsurances.get(i).getName());
                if (mInsurances.get(i).getName().contains("上海滴滴司机互助")) {
                    List<IPayEntry> payItemList1 = new ArrayList<>();
                    InsurancePayEntry1 insurancePayEntry11 = new InsurancePayEntry1();
                    insurancePayEntry11.payPhoneNum = "021-87654321";
                    insurancePayEntry11.paperList1 = "身份证原件，复印件";
                    insurancePayEntry11.paperList2 = "驾驶证原件，复印件";
                    payItemList1.add(insurancePayEntry11);
                    itemList.add(i, payItemList1);
                } else if (mInsurances.get(i).getName().contains("中青年抗癌互助")) {
                    List<IPayEntry> payItemList2 = new ArrayList<>();
                    InsurancePayEntry1 insurancePayEntry12 = new InsurancePayEntry1();
                    insurancePayEntry12.payPhoneNum = "021-87654321";
                    insurancePayEntry12.paperList1 = "身份证原件，复印件";
                    insurancePayEntry12.paperList2 = "病历卡原件，复印件";
                    payItemList2.add(insurancePayEntry12);
                    itemList.add(i, payItemList2);
                } else if (mInsurances.get(i).getName().contains("南航金卡延误互助")) {
                    List<IPayEntry> payItemList3 = new ArrayList<>();
                    InsurancePayEntry2 insurancePayEntry21 = new InsurancePayEntry2();
                    insurancePayEntry21.description = "＊延误信息数据由中国南方航空提供";


                    String jsonString = (String) SpUtils.getInstance().getParam(
                            UserController.getInstance().getUser() + mInsurances.get(i).getId(),
                            "");
                    InsuranceReq info = null;
                    try {
                        info = new Gson().fromJson(jsonString, InsuranceReq.class);
                    } catch (Exception e) {

                    }


                    if (info != null && info.getName() != null) {
                        insurancePayEntry21.name = "姓名：" + info.getName();
                    } else {
                        insurancePayEntry21.name = "姓名：张三";
                    }
                    if (info != null && info.getId_csa() != null) {
                        insurancePayEntry21.cardId = "会员卡号：" + info.getId_csa();
                    } else {
                        insurancePayEntry21.cardId = "会员卡号：5300****1234";
                    }

                    insurancePayEntry21.delayTimes = "本年延误次数16次";
                    payItemList3.add(insurancePayEntry21);
                    itemList.add(i, payItemList3);
                } else {
                    List<IPayEntry> payItemList1 = new ArrayList<>();
                    InsurancePayEntry1 insurancePayEntry11 = new InsurancePayEntry1();
                    insurancePayEntry11.payPhoneNum = "021-87654321";
                    insurancePayEntry11.paperList1 = "身份证原件，复印件";
                    payItemList1.add(insurancePayEntry11);
                    itemList.add(i, payItemList1);
                }
            }
        }

        payExListAdapter = new PayExListAdapter(getActivity(), groupList, itemList, mInsurances);
        expandableListView.setAdapter(payExListAdapter);
        for (int i = 0; i < groupList.size(); i++) {
            expandableListView.expandGroup(i);
        }
    }

    public interface IPayEntry {

    }

    public class InsurancePayEntry1 implements IPayEntry {
        public String payPhoneNum;
        public String paperList1;
        public String paperList2;
    }

    public class InsurancePayEntry2 implements IPayEntry {
        public String description;
        public String name;
        public String cardId;
        public String delayTimes;
    }
}
