package com.pingan.demo.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pingan.demo.MyApplication;
import com.pingan.demo.R;
import com.pingan.demo.SpUtils;
import com.pingan.demo.base.BaseFragment;
import com.pingan.demo.controller.UserController;
import com.pingan.demo.model.entity.Insurance;
import com.pingan.demo.model.entity.InsuranceReq;
import com.pingan.demo.model.service.ClaimService;
import com.pingan.http.framework.network.PostServiceParams;
import com.pingan.http.framework.network.ServiceTask;
import com.pingan.http.framework.task.NetworkExcuter;
import com.pingan.http.framework.task.NetwrokTaskError;
import com.pingan.http.framework.task.ServiceCallback;
import com.pingan.http.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guolidong752 on 17/5/8.
 */

public class PayDetailFragment extends BaseFragment {
    private Insurance insurance;
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
                    Toast.makeText(getActivity(), "申请成功", Toast.LENGTH_SHORT).show();
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

    public PayDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainLayout = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_pay_detail, baseLayout);
        return baseLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mainLayout != null) {
            insurance = (Insurance) getArguments().getSerializable("Insurance");
            final EditText cardId = (EditText) mainLayout.findViewById(R.id.cardId_0);
            final EditText cardId1 = (EditText) mainLayout.findViewById(R.id.cardId_1);
            TextView title = (TextView) mainLayout.findViewById(R.id.insurance_title);
            TextView name = (TextView) mainLayout.findViewById(R.id.cardId);
            TextView csa = (TextView) mainLayout.findViewById(R.id.name);
            title.setText(insurance.getName());
            String jsonString = (String) SpUtils.getInstance()
                    .getParam(UserController.getInstance().getUser() + insurance.getId(), "");
            InsuranceReq info = null;
            try {
                info = new Gson().fromJson(jsonString, InsuranceReq.class);
            } catch (Exception e) {

            }

            String nametext = "";
            String csaText = "";

            if (info != null && info.getName() != null) {
                nametext = "姓名：" + info.getName();
            } else {
                nametext = "姓名：张三";
            }
            if (info != null && info.getId_csa() != null) {
                csaText = "会员卡号：" + info.getId_csa();
            } else {
                csaText = "会员卡号：5300****1234";
            }
            name.setText(nametext);
            csa.setText(csaText);

            mainLayout.findViewById(R.id.pay_btn_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cardId1.getText() == null || cardId.getText() == null || cardId.getText()
                            .toString().trim().equals("") || cardId1.getText().toString().trim()
                            .equals("")) {
                        Toast.makeText(getActivity(), "银行卡号不可为空", Toast.LENGTH_SHORT).show();
                    } else if (!cardId1.getText().toString().trim()
                            .equals(cardId.getText().toString().trim())) {
                        Toast.makeText(getActivity(), "银行卡号输入的不同", Toast.LENGTH_SHORT).show();
                    } else {
                        doPostClaim(cardId.getText().toString());
                    }

                }
            });
        }
    }

    private void doPostClaim(String card) {
        ServiceTask serviceTask = new ServiceTask(ClaimService.doPostClaim());
        serviceTask.setCancelable(true).setShowProcess(true);
        serviceTask.setCallback(taskCallback);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_insurance", insurance.getId());
            jsonObject.put("id_user", UserController.getInstance().getUser());
            jsonObject.put("name", UserController.getInstance().getUser());
            jsonObject.put("mobile", UserController.getInstance().getUser());
            jsonObject.put("cardnum", card);
            jsonObject.put("time_claimed", DateUtil.getCurrentTime("yyyy-MM-dd hh:mm:ss"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ((PostServiceParams) serviceTask.getServiceParams()).setJsonObject(jsonObject);
        NetworkExcuter.getInstance().excute(serviceTask, this);
    }
}
