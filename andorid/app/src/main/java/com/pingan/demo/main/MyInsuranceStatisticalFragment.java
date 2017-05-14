package com.pingan.demo.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pingan.demo.NumUtil;
import com.pingan.demo.R;
import com.pingan.demo.base.BaseFragment;
import com.pingan.demo.model.entity.Insurance;

/**
 * Created by guolidong752 on 16/1/1.
 */

public class MyInsuranceStatisticalFragment extends BaseFragment {
    private Insurance mInsurance;

    public MyInsuranceStatisticalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainLayout = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_insurance_detail, baseLayout);
        return baseLayout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mainLayout != null) {
            mInsurance = (Insurance) getArguments().getSerializable("Insurance");

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
            source_text.setText(mInsurance.getDescription().get(6));
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
            int value = NumUtil.changeToInt(mInsurance.getAmount_max());
            if (value / 10000 > 0) {
                max_text.setText(String.valueOf(value / 10000) + "万");
            } else {
                max_text.setText(String.valueOf(mInsurance.getAmount_max()));
            }


            //参与人数
            final TextView count_text = (TextView) mainLayout.findViewById(R.id.count_text);
            int value1 = NumUtil.changeToInt(mInsurance.getCount_bought());
            if (value1 / 10000 > 0) {
                count_text.setText(String.valueOf(value1 / 10000) + "万");
            } else {
                count_text.setText(String.valueOf(mInsurance.getCount_bought()));
            }


            //资助人数
            final TextView helped_text = (TextView) mainLayout.findViewById(R.id.helped_text);
            int value2 = NumUtil.changeToInt(mInsurance.getCount_helped());
            if (value2 / 10000 > 0) {
                helped_text.setText(String.valueOf(value2 / 10000) + "万");
            } else {
                helped_text.setText(String.valueOf(mInsurance.getCount_helped()));
            }


            //筹款余额
            final TextView balance_text = (TextView) mainLayout.findViewById(R.id.balance_text);
            int value3 = NumUtil.changeToInt(mInsurance.getBalance());
            if (value3 / 10000 > 0) {
                balance_text.setText(String.valueOf(value3 / 10000) + "万");
            } else {
                balance_text.setText(String.valueOf(mInsurance.getBalance()));
            }


            //总筹款金额
            final TextView amount_text = (TextView) mainLayout.findViewById(R.id.amount_text);
            int value4 = NumUtil.changeToInt(mInsurance.getAmount());
            if (value4 / 10000 > 0) {
                amount_text.setText(String.valueOf(value4 / 10000) + "万");
            } else {
                amount_text.setText(String.valueOf(mInsurance.getAmount()));
            }


            //上月分摊
            final TextView fee_text = (TextView) mainLayout.findViewById(R.id.fee_text);
            int value5 = NumUtil.changeToInt(mInsurance.getFee());
            if (value5 / 10000 > 0) {
                fee_text.setText(String.valueOf(value5 / 10000) + "万");
            } else {
                fee_text.setText(String.valueOf(mInsurance.getFee()));
            }


            //互助规则1互助范围
            final TextView rule_text1 = (TextView) mainLayout.findViewById(R.id.rule_text1);
            rule_text1.setText(mInsurance.getDescription().get(2));
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
            rule_text2.setText(mInsurance.getDescription().get(3));
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
            rule_text3.setText(mInsurance.getDescription().get(4));
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
            rule_text4.setText(mInsurance.getDescription().get(5));
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
        }
    }
}
