package com.pingan.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pingan.demo.R;
import com.pingan.demo.model.entity.InsuranceSta;

import java.util.List;

/**
 * Created by guolidong752 on 16/1/1.
 */

public class MyInsuranceDetailListAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private List<InsuranceSta> mList;
    private Context mContext;

    public MyInsuranceDetailListAdapter(Context context, List<InsuranceSta> list) {
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

        final InsuranceSta data = mList.get(position);
        ItemHolder itemHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_myhome_detail_list_item, null);
            itemHolder = new ItemHolder();
            itemHolder.month = (TextView) convertView.findViewById(R.id.month);
            itemHolder.count_new = (TextView) convertView.findViewById(R.id.count_new);
            itemHolder.count_quit = (TextView) convertView.findViewById(R.id.count_quit);
            itemHolder.count_helped = (TextView) convertView.findViewById(R.id.count_helped);
            itemHolder.amount = (TextView) convertView.findViewById(R.id.amount);
            itemHolder.cost = (TextView) convertView.findViewById(R.id.cost);
            itemHolder.fee = (TextView) convertView.findViewById(R.id.fee);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        itemHolder.month.setText(data.getMonth());
        itemHolder.count_new.setText("本月新增人数：" + data.getCount_new());
        itemHolder.count_quit.setText("本月减少人数：" + data.getCount_quit());
        itemHolder.count_helped.setText("本月资助人数：" + data.getCount_helped());
        itemHolder.amount.setText("本月资助金额：" + String.valueOf(data.getAmount()) + "元");
        itemHolder.cost.setText("本月运营成本：" + String.valueOf(data.getCost()) + "元");
        itemHolder.fee.setText("本月个人分摊：" + String.valueOf(data.getFee()) + "元");

        return convertView;
    }

    class ItemHolder {
        public TextView month;//某月 2017-05
        public TextView count_new;//本月新增人数
        public TextView count_quit;//本月减少人数
        public TextView count_helped;//本月资助人数
        public TextView amount;//本月资助金额
        public TextView cost;//本月运营成本
        public TextView fee;//本月分摊
    }
}
