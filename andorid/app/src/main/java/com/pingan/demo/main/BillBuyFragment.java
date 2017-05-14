package com.pingan.demo.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pingan.demo.MyApplication;
import com.pingan.demo.R;
import com.pingan.demo.SpUtils;
import com.pingan.demo.base.BaseFragment;
import com.pingan.demo.controller.UserController;
import com.pingan.demo.model.entity.InsuranceReq;
import com.pingan.demo.model.service.InsurancesService;
import com.pingan.http.framework.network.PostServiceParams;
import com.pingan.http.framework.network.ServiceTask;
import com.pingan.http.framework.task.NetworkExcuter;
import com.pingan.http.framework.task.NetwrokTaskError;
import com.pingan.http.framework.task.ServiceCallback;
import com.pingan.http.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guolidong752 on 16/1/2.
 */

public class BillBuyFragment extends BaseFragment {


    private EditText name;//姓名
    private EditText idcard;//身份证
    private EditText mobile;//手机号
    private int amount = 0;//充值金额
    private boolean medical = false;//病史确认
    private EditText id_driver;//驾驶证号
    private EditText id_driving;//行驶证号
    private EditText id_didi;//滴滴账号
    private EditText id_csa;//南航会员号
    private Button button;
    private InsuranceReq insuranceReq;
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
                    button.setEnabled(true);
                }
            });

        }
    };

    public BillBuyFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainLayout = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_bill_buy, baseLayout);
        return baseLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mainLayout != null) {
            name = (EditText) mainLayout.findViewById(R.id.name);//姓名
            idcard = (EditText) mainLayout.findViewById(R.id.idcard);//身份证
            mobile = (EditText) mainLayout.findViewById(R.id.mobile);//手机号

            final TextView money_text_20 = (TextView) mainLayout
                    .findViewById(R.id.money_text_20);//充值金额
            final TextView money_text_50 = (TextView) mainLayout
                    .findViewById(R.id.money_text_50);//充值金额
            final TextView money_text_100 = (TextView) mainLayout
                    .findViewById(R.id.money_text_100);//充值金额
            money_text_20.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (amount != 20) {
                        money_text_20.setBackgroundResource(R.drawable.money_text_1);
                        money_text_20.setTextColor(getResources().getColor(R.color.white));
                        money_text_50.setBackgroundResource(R.drawable.money_text_2);
                        money_text_50.setTextColor(getResources().getColor(R.color.color_494949));
                        money_text_100.setBackgroundResource(R.drawable.money_text_2);
                        money_text_100.setTextColor(getResources().getColor(R.color.color_494949));
                        amount = 20;
                    } else {
                        money_text_20.setBackgroundResource(R.drawable.money_text_2);
                        money_text_20.setTextColor(getResources().getColor(R.color.color_494949));
                        amount = 0;
                    }
                }
            });
            money_text_50.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (amount != 50) {
                        money_text_50.setBackgroundResource(R.drawable.money_text_1);
                        money_text_50.setTextColor(getResources().getColor(R.color.white));
                        money_text_20.setBackgroundResource(R.drawable.money_text_2);
                        money_text_20.setTextColor(getResources().getColor(R.color.color_494949));
                        money_text_100.setBackgroundResource(R.drawable.money_text_2);
                        money_text_100.setTextColor(getResources().getColor(R.color.color_494949));
                        amount = 50;
                    } else {
                        money_text_50.setBackgroundResource(R.drawable.money_text_2);
                        money_text_50.setTextColor(getResources().getColor(R.color.color_494949));
                        amount = 0;
                    }
                }
            });
            money_text_100.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (amount != 100) {
                        money_text_100.setBackgroundResource(R.drawable.money_text_1);
                        money_text_100.setTextColor(getResources().getColor(R.color.white));
                        money_text_20.setBackgroundResource(R.drawable.money_text_2);
                        money_text_20.setTextColor(getResources().getColor(R.color.color_494949));
                        money_text_50.setBackgroundResource(R.drawable.money_text_2);
                        money_text_50.setTextColor(getResources().getColor(R.color.color_494949));
                        amount = 100;
                    } else {
                        money_text_100.setBackgroundResource(R.drawable.money_text_2);
                        money_text_100.setTextColor(getResources().getColor(R.color.color_494949));
                        amount = 0;
                    }
                }
            });
            final TextView medical_confirm1 = (TextView) mainLayout
                    .findViewById(R.id.medical_confirm1);
            final TextView medical_confirm2 = (TextView) mainLayout
                    .findViewById(R.id.medical_confirm2);
            medical_confirm1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!medical) {
                        medical_confirm1.setBackgroundResource(R.drawable.money_text_1);
                        medical_confirm1.setTextColor(getResources().getColor(R.color.white));
                        medical_confirm2.setBackgroundResource(R.drawable.money_text_2);
                        medical_confirm2
                                .setTextColor(getResources().getColor(R.color.color_494949));
                        medical = true;
                    } else {
                        medical_confirm1.setBackgroundResource(R.drawable.money_text_2);
                        medical_confirm1
                                .setTextColor(getResources().getColor(R.color.color_494949));
                        medical = false;
                    }
                }
            });

            medical_confirm2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!medical) {
                        medical_confirm2.setBackgroundResource(R.drawable.money_text_1);
                        medical_confirm2.setTextColor(getResources().getColor(R.color.white));
                        medical_confirm1.setBackgroundResource(R.drawable.money_text_2);
                        medical_confirm1
                                .setTextColor(getResources().getColor(R.color.color_494949));
                        medical = true;
                    } else {
                        medical_confirm2.setBackgroundResource(R.drawable.money_text_2);
                        medical_confirm2
                                .setTextColor(getResources().getColor(R.color.color_494949));
                        medical = false;
                    }
                }
            });

            id_driver = (EditText) mainLayout.findViewById(R.id.id_driver);//驾驶证号
            id_driving = (EditText) mainLayout.findViewById(R.id.id_driving);//行驶证号
            id_didi = (EditText) mainLayout.findViewById(R.id.id_didi);//滴滴账号
            id_csa = (EditText) mainLayout.findViewById(R.id.id_csa);//南航会员号
            final String id_insurance = getArguments().getString("id_insurance");
            button = (Button) mainLayout.findViewById(R.id.charge_btn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insuranceReq = new InsuranceReq();
                    insuranceReq.setId_insurance(id_insurance == null ? "" : id_insurance);
                    insuranceReq.setId_user(
                            UserController.getInstance().getUser() == null ? "" : UserController
                                    .getInstance().getUser());
                    insuranceReq.setName(name.getText().toString());
                    insuranceReq.setIdcard(idcard.getText().toString());
                    insuranceReq.setMobile(mobile.getText().toString());
                    insuranceReq.setAmount(amount);
                    insuranceReq.setMedical(String.valueOf(medical));
                    insuranceReq.setId_driver(id_driver.getText().toString());
                    insuranceReq.setId_driving(id_driving.getText().toString());
                    insuranceReq.setId_didi(id_didi.getText().toString());
                    insuranceReq.setId_csa(id_csa.getText().toString());
                    insuranceReq.setReserved("");
                    button.setEnabled(false);
                    doPostInsurance();
                }
            });

            String insurance_name = getArguments().getString("insurance_name");
            if (insurance_name.contains("上海滴滴司机互助")) {
                mainLayout.findViewById(R.id.id_driver_layout).setVisibility(View.VISIBLE);
                mainLayout.findViewById(R.id.id_driving_layout).setVisibility(View.VISIBLE);
                mainLayout.findViewById(R.id.id_didi_layout).setVisibility(View.VISIBLE);
            } else if (insurance_name.contains("中青年抗癌互助")) {
                mainLayout.findViewById(R.id.medical_layout).setVisibility(View.VISIBLE);
            } else if (insurance_name.contains("南航金卡延误互助")) {
                mainLayout.findViewById(R.id.id_csa_layout).setVisibility(View.VISIBLE);
            }

            ((CheckBox) mainLayout.findViewById(R.id.check_id))
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            button.setEnabled(isChecked);
                        }
                    });
        }
    }

    private void doPostInsurance() {
        ServiceTask serviceTask = new ServiceTask(InsurancesService.doPostInsurance());
        serviceTask.setCancelable(true).setShowProcess(true);
        serviceTask.setCallback(taskCallback);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_insurance", insuranceReq.getId_insurance());
            jsonObject.put("id_user", insuranceReq.getId_user());
            jsonObject.put("name", insuranceReq.getName());
            jsonObject.put("idcard", insuranceReq.getIdcard());
            jsonObject.put("mobile", insuranceReq.getMobile());
            jsonObject.put("amount", String.valueOf(insuranceReq.getAmount()));
            jsonObject.put("medical", insuranceReq.getMedical());
            jsonObject.put("id_driver", insuranceReq.getId_driver());
            jsonObject.put("id_driving", insuranceReq.getId_driving());
            jsonObject.put("id_didi", insuranceReq.getId_didi());
            jsonObject.put("id_csa", insuranceReq.getId_csa());
            jsonObject.put("time_bought", DateUtil.getCurrentTime("yyyy-MM-dd hh:mm:ss"));
            jsonObject.put("reserved", insuranceReq.getReserved());
            JSONObject temp = new JSONObject();
            temp.put("name", insuranceReq.getName());
            temp.put("id_insurance", insuranceReq.getId_insurance());
            temp.put("id_user", insuranceReq.getId_user());
            temp.put("name", insuranceReq.getName());
            temp.put("idcard", insuranceReq.getIdcard());
            temp.put("mobile", insuranceReq.getMobile());
            temp.put("amount", insuranceReq.getAmount());
            temp.put("medical", insuranceReq.getMedical());
            temp.put("id_driver", insuranceReq.getId_driver());
            temp.put("id_driving", insuranceReq.getId_driving());
            temp.put("id_didi", insuranceReq.getId_didi());
            temp.put("id_csa", insuranceReq.getId_csa());
            temp.put("time_bought", DateUtil.getCurrentTime("yyyy-MM-dd hh:mm:ss"));
            temp.put("reserved", insuranceReq.getReserved());
            SpUtils.getInstance().setParam(
                    UserController.getInstance().getUser() + insuranceReq.getId_insurance(), temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((PostServiceParams) serviceTask.getServiceParams()).setJsonObject(jsonObject);
        NetworkExcuter.getInstance().excute(serviceTask, this);
    }
}
