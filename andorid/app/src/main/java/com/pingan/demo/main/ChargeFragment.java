package com.pingan.demo.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pingan.demo.MyApplication;
import com.pingan.demo.R;
import com.pingan.demo.base.BaseFragment;
import com.pingan.demo.controller.UserController;
import com.pingan.demo.model.service.ChargeService;
import com.pingan.http.framework.network.PostServiceParams;
import com.pingan.http.framework.network.ServiceTask;
import com.pingan.http.framework.task.NetworkExcuter;
import com.pingan.http.framework.task.NetwrokTaskError;
import com.pingan.http.framework.task.ServiceCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guolidong752 on 16/1/2.
 */

public class ChargeFragment extends BaseFragment {


    private int amount = 0;//充值金额
    private TextView money_text_10;
    private TextView money_text_20;
    private TextView money_text_30;
    private TextView money_text_50;
    private TextView money_text_100;
    private TextView money_text_200;
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
                    content_ll.removeProcess();
                    Toast.makeText(getActivity(), "充值成功", Toast.LENGTH_SHORT).show();
                    MyApplication.getAppContext().getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (getActivity() != null) {
                                getActivity().onBackPressed();
                            }
                        }
                    }, 2000);
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

    public ChargeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainLayout = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_charge, baseLayout);
        return baseLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mainLayout != null) {

            String balance = getArguments().getString("getBalance");
            String fee = getArguments().getString("getFee");
            ((TextView) mainLayout.findViewById(R.id.balance)).setText(balance);
            ((TextView) mainLayout.findViewById(R.id.fee)).setText(fee);
            money_text_10 = (TextView) mainLayout.findViewById(R.id.money_text_10);//充值金额
            money_text_20 = (TextView) mainLayout.findViewById(R.id.money_text_20);//充值金额
            money_text_30 = (TextView) mainLayout.findViewById(R.id.money_text_30);//充值金额
            money_text_50 = (TextView) mainLayout.findViewById(R.id.money_text_50);//充值金额
            money_text_100 = (TextView) mainLayout.findViewById(R.id.money_text_100);//充值金额
            money_text_200 = (TextView) mainLayout.findViewById(R.id.money_text_200);//充值金额
            money_text_10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doChangeText(10, money_text_10);
                }
            });
            money_text_20.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doChangeText(20, money_text_20);
                }
            });
            money_text_30.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doChangeText(30, money_text_30);
                }
            });
            money_text_50.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doChangeText(50, money_text_50);
                }
            });
            money_text_100.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doChangeText(100, money_text_100);
                }
            });
            money_text_200.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doChangeText(200, money_text_200);
                }
            });


            mainLayout.findViewById(R.id.charge_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doPostCharge();
                }
            });
        }
    }

    private void doChangeText(int num, TextView money_text) {
        if (amount != num) {
            money_text_10.setBackgroundResource(R.drawable.money_text_2);
            money_text_10.setTextColor(getResources().getColor(R.color.color_494949));
            money_text_20.setBackgroundResource(R.drawable.money_text_2);
            money_text_20.setTextColor(getResources().getColor(R.color.color_494949));
            money_text_30.setBackgroundResource(R.drawable.money_text_2);
            money_text_30.setTextColor(getResources().getColor(R.color.color_494949));
            money_text_50.setBackgroundResource(R.drawable.money_text_2);
            money_text_50.setTextColor(getResources().getColor(R.color.color_494949));
            money_text_100.setBackgroundResource(R.drawable.money_text_2);
            money_text_100.setTextColor(getResources().getColor(R.color.color_494949));
            money_text_200.setBackgroundResource(R.drawable.money_text_2);
            money_text_200.setTextColor(getResources().getColor(R.color.color_494949));
            money_text.setBackgroundResource(R.drawable.money_text_1);
            money_text.setTextColor(getResources().getColor(R.color.white));
            amount = num;
        } else {
            money_text.setBackgroundResource(R.drawable.money_text_2);
            money_text.setTextColor(getResources().getColor(R.color.color_494949));
            amount = 0;
        }
    }

    private void doPostCharge() {
        ServiceTask serviceTask = new ServiceTask(ChargeService.doPostCharge());
        serviceTask.setCancelable(true).setShowProcess(true);
        serviceTask.setCallback(taskCallback);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_user", UserController.getInstance().getUser());
            jsonObject.put("amount", String.valueOf(amount));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((PostServiceParams) serviceTask.getServiceParams()).setJsonObject(jsonObject);
        NetworkExcuter.getInstance().excute(serviceTask, this);
    }
}
