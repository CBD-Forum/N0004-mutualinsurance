package com.pingan.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pingan.demo.R;
import com.pingan.demo.model.entity.Claim;

import java.util.List;

/**
 * Created by guolidong752 on 16/1/1.
 */

public class HelpListAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private List<Claim> mList;
    private Context mContext;

    public HelpListAdapter(Context context, List<Claim> list) {
        mContext = context;
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mList != null && !mList.isEmpty()) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Claim data = mList.get(position);
        ItemHolder itemHolder = null;
        convertView = mInflater.inflate(R.layout.item_help_listview_item, null);
        itemHolder = new ItemHolder();
        itemHolder.title = (TextView) convertView.findViewById(R.id.title);
        itemHolder.name = (TextView) convertView.findViewById(R.id.name_text);
        final ImageView nameImage = (ImageView) convertView.findViewById(R.id.name_arrow);
        final ItemHolder finalItemHolder = itemHolder;
        convertView.findViewById(R.id.name_arrow).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (finalItemHolder.name.getVisibility() == View.VISIBLE) {
                    finalItemHolder.name.setVisibility(View.GONE);
                    nameImage.setImageResource(R.drawable.arrow2_07);
                } else {
                    finalItemHolder.name.setVisibility(View.VISIBLE);
                    nameImage.setImageResource(R.drawable.arrow1_07);
                }
                return false;
            }
        });
        itemHolder.mobile = (TextView) convertView.findViewById(R.id.mobile_text);
        final ImageView mobileImage = (ImageView) convertView.findViewById(R.id.mobile_arrow);
        final ItemHolder finalItemHoldermobile = itemHolder;
        convertView.findViewById(R.id.mobile_arrow).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (finalItemHoldermobile.mobile.getVisibility() == View.VISIBLE) {
                    finalItemHoldermobile.mobile.setVisibility(View.GONE);
                    mobileImage.setImageResource(R.drawable.arrow2_07);
                } else {
                    finalItemHoldermobile.mobile.setVisibility(View.VISIBLE);
                    mobileImage.setImageResource(R.drawable.arrow1_07);
                }
                return false;
            }
        });
        itemHolder.amount = (TextView) convertView.findViewById(R.id.amount_text);
        itemHolder.report_third = (TextView) convertView.findViewById(R.id.report_third_text);
        final ImageView report_thirdImage = (ImageView) convertView
                .findViewById(R.id.report_third_arrow);
        final ItemHolder finalItemHolderreport_third = itemHolder;
        convertView.findViewById(R.id.report_third_arrow)
                .setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (finalItemHolderreport_third.report_third.getVisibility()
                                == View.VISIBLE) {
                            finalItemHolderreport_third.report_third.setVisibility(View.GONE);
                            report_thirdImage.setImageResource(R.drawable.arrow2_07);
                        } else {
                            finalItemHolderreport_third.report_third.setVisibility(View.VISIBLE);
                            report_thirdImage.setImageResource(R.drawable.arrow1_07);
                        }
                        return false;
                    }
                });
        itemHolder.report_visitor = (TextView) convertView.findViewById(R.id.report_visitor_text);
        final ImageView report_visitorImage = (ImageView) convertView
                .findViewById(R.id.report_visitor_arrow);
        final ItemHolder finalItemHolderreport_visitor = itemHolder;
        convertView.findViewById(R.id.report_visitor_arrow)
                .setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (finalItemHolderreport_visitor.report_visitor.getVisibility()
                                == View.VISIBLE) {
                            finalItemHolderreport_visitor.report_visitor.setVisibility(View.GONE);
                            report_visitorImage.setImageResource(R.drawable.arrow2_07);
                        } else {
                            finalItemHolderreport_visitor.report_visitor
                                    .setVisibility(View.VISIBLE);
                            report_visitorImage.setImageResource(R.drawable.arrow1_07);
                        }
                        return false;
                    }
                });
        itemHolder.receipt = (TextView) convertView.findViewById(R.id.receipt_text);
        final ImageView receiptImage = (ImageView) convertView.findViewById(R.id.receipt_arrow);
        final ItemHolder finalItemHolderreceipt = itemHolder;
        convertView.findViewById(R.id.receipt_arrow).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (finalItemHolderreceipt.receipt.getVisibility() == View.VISIBLE) {
                    finalItemHolderreceipt.receipt.setVisibility(View.GONE);
                    receiptImage.setImageResource(R.drawable.arrow2_07);
                } else {
                    finalItemHolderreceipt.receipt.setVisibility(View.VISIBLE);
                    receiptImage.setImageResource(R.drawable.arrow1_07);
                }
                return false;
            }
        });
        itemHolder.time_claimed = (TextView) convertView.findViewById(R.id.time_claimed_text);
        final ImageView time_claimedImage = (ImageView) convertView
                .findViewById(R.id.time_claimed_arrow);
        final ItemHolder finalItemHoldertime_claimed = itemHolder;
        convertView.findViewById(R.id.time_claimed_arrow)
                .setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (finalItemHoldertime_claimed.time_claimed.getVisibility()
                                == View.VISIBLE) {
                            finalItemHoldertime_claimed.time_claimed.setVisibility(View.GONE);
                            time_claimedImage.setImageResource(R.drawable.arrow2_07);
                        } else {
                            finalItemHoldertime_claimed.time_claimed.setVisibility(View.VISIBLE);
                            time_claimedImage.setImageResource(R.drawable.arrow1_07);
                        }
                        return false;
                    }
                });
        itemHolder.time_approved = (TextView) convertView.findViewById(R.id.time_approved_text);
        final ImageView time_approvedImage = (ImageView) convertView
                .findViewById(R.id.time_approved_arrow);
        final ItemHolder finalItemHoldertime_approved = itemHolder;
        convertView.findViewById(R.id.time_approved_arrow)
                .setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (finalItemHoldertime_approved.time_approved.getVisibility()
                                == View.VISIBLE) {
                            finalItemHoldertime_approved.time_approved.setVisibility(View.GONE);
                            time_approvedImage.setImageResource(R.drawable.arrow2_07);
                        } else {
                            finalItemHoldertime_approved.time_approved.setVisibility(View.VISIBLE);
                            time_approvedImage.setImageResource(R.drawable.arrow1_07);
                        }
                        return false;
                    }
                });


        itemHolder.title.setText(data.getTime_claimed() + "-" + data.getName());
        itemHolder.name.setText(data.getName());//理赔人姓名
        itemHolder.mobile.setText(data.getMobile());//联系方式
        if (data.getAmount() / 10000 > 0) {
            itemHolder.amount.setText(String.valueOf(data.getAmount() / 10000) + "万");
        } else {
            itemHolder.amount.setText(String.valueOf(data.getAmount()));
        }//理赔金额
        itemHolder.report_third.setText(data.getReport_third());//第三方报告
        itemHolder.report_visitor.setText(data.getReport_visitor());//探望者报告
        itemHolder.receipt.setText(data.getReceipt());//收款凭证
        itemHolder.time_claimed.setText(data.getTime_claimed());//公示时间，申请理赔时间
        itemHolder.time_approved.setText(data.getTime_approved());//筹款时间，实际理赔时间

        return convertView;
    }

    class ItemHolder {
        public TextView title;
        public TextView name;//理赔人姓名
        public TextView mobile;//联系方式
        public TextView amount;//理赔金额
        public TextView report_third;//第三方报告
        public TextView report_visitor;//探望者报告
        public TextView receipt;//收款凭证
        public TextView time_claimed;//公示时间，申请理赔时间
        public TextView time_approved;//筹款时间，实际理赔时间
    }
}
