package com.pingan.demo.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pingan.demo.MyApplication;
import com.pingan.demo.NumUtil;
import com.pingan.demo.R;
import com.pingan.demo.UrlTools;
import com.pingan.demo.base.BaseFragment;
import com.pingan.demo.controller.FragmentManagerControl;
import com.pingan.demo.model.entity.Insurance;
import com.pingan.demo.model.entity.ProfileRes;
import com.pingan.demo.model.service.LoginService;
import com.pingan.http.framework.network.ServiceTask;
import com.pingan.http.framework.task.NetworkExcuter;
import com.pingan.http.framework.task.NetwrokTaskError;
import com.pingan.http.framework.task.ServiceCallback;

/**
 * Created by guolidong752 on 16/1/2.
 */

public class BillDetailFragment extends BaseFragment {
    private BaseFragment billBuyFragment;
    private Insurance mInsurance;
    private ProfileRes profileRes;
    private Button joinButton;
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

    public BillDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainLayout = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_bill_detail, baseLayout);
        return baseLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mainLayout != null) {
            mInsurance = (Insurance) getArguments().getSerializable("Insurance");
            TextView name = (TextView) mainLayout.findViewById(R.id.name);
            name.setText(mInsurance.getName());

            View infoLayout = mainLayout.findViewById(R.id.info_layout);
            if (mInsurance.getName() != null) {
                if (mInsurance.getName().contains("上海滴滴司机互助")) {
                    infoLayout.setBackgroundResource(R.drawable.bill_list_item1_bg);
                } else if (mInsurance.getName().contains("中青年抗癌互助")) {
                    infoLayout.setBackgroundResource(R.drawable.bill_list_item2_bg);
                } else {
                    infoLayout.setBackgroundResource(R.drawable.bill_list_item3_bg);
                }
            }

            if (mInsurance.getDescription() != null && mInsurance.getDescription().size() >= 2) {
                TextView description1 = (TextView) mainLayout.findViewById(R.id.description1);
                description1.setText(mInsurance.getDescription().get(0));
                TextView description2 = (TextView) mainLayout.findViewById(R.id.description2);
                description2.setText(mInsurance.getDescription().get(1));
            }

            TextView amount_max = (TextView) mainLayout.findViewById(R.id.amount_max);
            int value = NumUtil.changeToInt(mInsurance.getAmount_max());
            if (value / 10000 > 0) {
                amount_max.setText(String.valueOf(value / 10000) + "万");
            } else {
                amount_max.setText(String.valueOf(mInsurance.getAmount_max()));
            }

            TextView count_bought = (TextView) mainLayout.findViewById(R.id.count_bought);
            int value1 = NumUtil.changeToInt(mInsurance.getCount_bought());
            if (value1 / 10000 > 0) {
                count_bought.setText(String.valueOf(value1 / 10000) + "万人参与");
            } else {
                count_bought.setText(String.valueOf(mInsurance.getCount_bought()) + "人参与");
            }
            TextView fee = (TextView) mainLayout.findViewById(R.id.fee);
            fee.setText(String.valueOf(mInsurance.getFee()) + "元／月起");
            TextView issuer = (TextView) mainLayout.findViewById(R.id.issuer);
            issuer.setText(mInsurance.getIssuer());


            //保险名称
            final TextView name_text = (TextView) mainLayout.findViewById(R.id.name_text);
            name_text.setText(mInsurance.getName());
            final ImageView nameImage = (ImageView) mainLayout.findViewById(R.id.name_arrow);
            mainLayout.findViewById(R.id.name_arrow).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (name_text.getVisibility() == View.VISIBLE) {
                        name_text.setVisibility(View.GONE);
                        nameImage.setImageResource(R.drawable.arrow2_07);
                    } else {
                        name_text.setVisibility(View.VISIBLE);
                        nameImage.setImageResource(R.drawable.arrow1_07);
                    }
                    return false;
                }
            });

            //发起时间
            final TextView time_text = (TextView) mainLayout.findViewById(R.id.time_text);
            time_text.setText(mInsurance.getTime_founded());
            final ImageView timeImage = (ImageView) mainLayout.findViewById(R.id.time_arrow);
            mainLayout.findViewById(R.id.time_arrow).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (time_text.getVisibility() == View.VISIBLE) {
                        time_text.setVisibility(View.GONE);
                        timeImage.setImageResource(R.drawable.arrow2_07);
                    } else {
                        time_text.setVisibility(View.VISIBLE);
                        timeImage.setImageResource(R.drawable.arrow1_07);
                    }
                    return false;
                }
            });

            //发起人
            final TextView issuer_text = (TextView) mainLayout.findViewById(R.id.issuer_text);
            issuer_text.setText(mInsurance.getIssuer());
            final ImageView issuerImage = (ImageView) mainLayout.findViewById(R.id.issuer_arrow);
            mainLayout.findViewById(R.id.issuer_arrow)
                    .setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (issuer_text.getVisibility() == View.VISIBLE) {
                                issuer_text.setVisibility(View.GONE);
                                issuerImage.setImageResource(R.drawable.arrow2_07);
                            } else {
                                issuer_text.setVisibility(View.VISIBLE);
                                issuerImage.setImageResource(R.drawable.arrow1_07);
                            }
                            return false;
                        }
                    });


            //启动资金
            final TextView money_text = (TextView) mainLayout.findViewById(R.id.money_text);
            int value22 = NumUtil.changeToInt(mInsurance.getSource());
            if (value22 / 10000 > 0) {
                money_text.setText(String.valueOf(value22 / 10000) + "万");
            } else {
                money_text.setText(String.valueOf(mInsurance.getSource()));
            }


            //来源
            final TextView source_text = (TextView) mainLayout.findViewById(R.id.source_text);
            if (mInsurance.getDescription() != null && mInsurance.getDescription().size() >= 7) {
                source_text.setText(mInsurance.getDescription().get(6));
            }

            final ImageView sourceImage = (ImageView) mainLayout.findViewById(R.id.source_arrow);
            mainLayout.findViewById(R.id.source_arrow)
                    .setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (source_text.getVisibility() == View.VISIBLE) {
                                source_text.setVisibility(View.GONE);
                                sourceImage.setImageResource(R.drawable.arrow2_07);
                            } else {
                                source_text.setVisibility(View.VISIBLE);
                                sourceImage.setImageResource(R.drawable.arrow1_07);
                            }
                            return false;
                        }
                    });

            //最高获捐
            final TextView max_text = (TextView) mainLayout.findViewById(R.id.max_text);
            int value2 = NumUtil.changeToInt(mInsurance.getAmount_max());
            if (value2 / 10000 > 0) {
                max_text.setText(String.valueOf(value2 / 10000) + "万");
            } else {
                max_text.setText(String.valueOf(mInsurance.getAmount_max()));
            }


            //参与人数
            final TextView count_text = (TextView) mainLayout.findViewById(R.id.count_text);
            int value3 = NumUtil.changeToInt(mInsurance.getCount_bought());
            if (value3 / 10000 > 0) {
                count_text.setText(String.valueOf(value3 / 10000) + "万");
            } else {
                count_text.setText(String.valueOf(mInsurance.getCount_bought()));
            }


            //资助人数
            final TextView helped_text = (TextView) mainLayout.findViewById(R.id.helped_text);
            int value4 = NumUtil.changeToInt(mInsurance.getCount_helped());
            if (value4 / 10000 > 0) {
                helped_text.setText(String.valueOf(value4 / 10000) + "万");
            } else {
                helped_text.setText(String.valueOf(mInsurance.getCount_helped()));
            }


            //筹款余额
            final TextView balance_text = (TextView) mainLayout.findViewById(R.id.balance_text);
            int value5 = NumUtil.changeToInt(mInsurance.getBalance());
            if (value5 / 10000 > 0) {
                balance_text.setText(String.valueOf(value5 / 10000) + "万");
            } else {
                balance_text.setText(String.valueOf(mInsurance.getBalance()));
            }


            //总筹款金额
            final TextView amount_text = (TextView) mainLayout.findViewById(R.id.amount_text);

            int value6 = NumUtil.changeToInt(mInsurance.getAmount());
            if (value6 / 10000 > 0) {
                amount_text.setText(String.valueOf(value6 / 10000) + "万");
            } else {
                amount_text.setText(String.valueOf(mInsurance.getAmount()));
            }


            //上月分摊
            final TextView fee_text = (TextView) mainLayout.findViewById(R.id.fee_text);
            int value7 = NumUtil.changeToInt(mInsurance.getFee());
            if (value7 / 10000 > 0) {
                fee_text.setText(String.valueOf(value7 / 10000) + "万");
            } else {
                fee_text.setText(String.valueOf(mInsurance.getFee()));
            }


            //互助规则1互助范围
            final TextView rule_text1 = (TextView) mainLayout.findViewById(R.id.rule_text1);
            if (mInsurance.getDescription() != null && mInsurance.getDescription().size() >= 3) {
                rule_text1.setText(mInsurance.getDescription().get(2));
            }

            final ImageView ruleImage1 = (ImageView) mainLayout.findViewById(R.id.rule_arrow1);
            mainLayout.findViewById(R.id.rule_arrow1)
                    .setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (rule_text1.getVisibility() == View.VISIBLE) {
                                rule_text1.setVisibility(View.GONE);
                                ruleImage1.setImageResource(R.drawable.arrow2_07);
                            } else {
                                rule_text1.setVisibility(View.VISIBLE);
                                ruleImage1.setImageResource(R.drawable.arrow1_07);
                            }
                            return false;
                        }
                    });

            //互助规则2 加入条件
            final TextView rule_text2 = (TextView) mainLayout.findViewById(R.id.rule_text2);
            if (mInsurance.getDescription() != null && mInsurance.getDescription().size() >= 4) {
                rule_text2.setText(mInsurance.getDescription().get(3));
            }

            final ImageView ruleImage2 = (ImageView) mainLayout.findViewById(R.id.rule_arrow2);
            mainLayout.findViewById(R.id.rule_arrow2)
                    .setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (rule_text2.getVisibility() == View.VISIBLE) {
                                rule_text2.setVisibility(View.GONE);
                                ruleImage2.setImageResource(R.drawable.arrow2_07);
                            } else {
                                rule_text2.setVisibility(View.VISIBLE);
                                ruleImage2.setImageResource(R.drawable.arrow1_07);
                            }
                            return false;
                        }
                    });

            //互助规则1退出条件
            final TextView rule_text3 = (TextView) mainLayout.findViewById(R.id.rule_text3);
            if (mInsurance.getDescription() != null && mInsurance.getDescription().size() >= 5) {
                rule_text3.setText(mInsurance.getDescription().get(4));
            }
            final ImageView ruleImage3 = (ImageView) mainLayout.findViewById(R.id.rule_arrow3);
            mainLayout.findViewById(R.id.rule_arrow3)
                    .setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (rule_text3.getVisibility() == View.VISIBLE) {
                                rule_text3.setVisibility(View.GONE);
                                ruleImage3.setImageResource(R.drawable.arrow2_07);
                            } else {
                                rule_text3.setVisibility(View.VISIBLE);
                                ruleImage3.setImageResource(R.drawable.arrow1_07);
                            }
                            return false;
                        }
                    });

            //互助规则1等待期
            final TextView rule_text4 = (TextView) mainLayout.findViewById(R.id.rule_text4);
            if (mInsurance.getDescription() != null && mInsurance.getDescription().size() >= 6) {
                rule_text4.setText(mInsurance.getDescription().get(5));
            }
            final ImageView ruleImage4 = (ImageView) mainLayout.findViewById(R.id.rule_arrow4);
            mainLayout.findViewById(R.id.rule_arrow4)
                    .setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (rule_text4.getVisibility() == View.VISIBLE) {
                                rule_text4.setVisibility(View.GONE);
                                ruleImage4.setImageResource(R.drawable.arrow2_07);
                            } else {
                                rule_text4.setVisibility(View.VISIBLE);
                                ruleImage4.setImageResource(R.drawable.arrow1_07);
                            }
                            return false;
                        }
                    });


            profileRes = new ProfileRes();

            joinButton = (Button) mainLayout.findViewById(R.id.join_btn);
            joinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    billBuyFragment = new BillBuyFragment();
                    Bundle data = new Bundle();
                    data.putString("id_insurance", mInsurance.getId());
                    data.putString("insurance_name", mInsurance.getName());
                    billBuyFragment.setArguments(data);
                    FragmentManagerControl.getInstance().addFragment(billBuyFragment);
                    joinButton.setEnabled(false);
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

    private void initData() {
        joinButton.setEnabled(true);
        if (profileRes != null && profileRes.getData() != null
                && profileRes.getData().getInsurances() != null) {
            for (Insurance current : profileRes.getData().getInsurances()) {
                if (current.getId().equals(mInsurance.getId())) {
                    joinButton.setEnabled(false);
                }
            }
        }
    }

    private void getMeDetail() {
        ServiceTask serviceTask = new ServiceTask(
                LoginService.getProfile(UrlTools.GET_ME_DETAIL, profileRes));
        serviceTask.setCancelable(true).setShowProcess(true);
        serviceTask.setCallback(taskCallback);
        NetworkExcuter.getInstance().excute(serviceTask, this);
    }
}
