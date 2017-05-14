package com.pingan.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pingan.demo.R;
import com.pingan.demo.NumUtil;
import com.pingan.demo.model.entity.Insurance;

import java.util.List;


public class BillListAdapter extends BaseAdapter {

    private LayoutInflater mInflater = null;
    private List<Insurance> mList;
    private Context mContext;

    public BillListAdapter(Context context, List<Insurance> list) {
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

        final Insurance data = mList.get(position);
        ItemHolder itemHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_bill_listview_item, null);
            itemHolder = new ItemHolder();
            itemHolder.name = (TextView) convertView.findViewById(R.id.name);
            itemHolder.description1 = (TextView) convertView.findViewById(R.id.description1);
            itemHolder.description2 = (TextView) convertView.findViewById(R.id.description2);
            itemHolder.amount_max = (TextView) convertView.findViewById(R.id.amount_max);
            itemHolder.count_bought = (TextView) convertView.findViewById(R.id.count_bought);
            itemHolder.fee = (TextView) convertView.findViewById(R.id.fee);
            itemHolder.issuer = (TextView) convertView.findViewById(R.id.issuer);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        itemHolder.name.setText(data.getName());
        if (data.getName() != null) {
            if (data.getName().contains("上海滴滴司机互助")) {
                convertView.setBackgroundResource(R.drawable.bill_list_item1_bg);
            } else if (data.getName().contains("中青年抗癌互助")) {
                convertView.setBackgroundResource(R.drawable.bill_list_item2_bg);
            } else {
                convertView.setBackgroundResource(R.drawable.bill_list_item3_bg);
            }
        }

        if (data.getDescription() != null) {
            itemHolder.description1.setText(data.getDescription().get(0));
            itemHolder.description2.setText(data.getDescription().get(1));
        }

        int value = NumUtil.changeToInt(data.getAmount_max());
        if (value / 10000 > 0) {
            itemHolder.amount_max.setText(String.valueOf(value / 10000) + "万");
        } else {
            itemHolder.amount_max.setText(data.getAmount_max());
        }
        int value1 = NumUtil.changeToInt(data.getCount_bought());
        if (value1 / 10000 > 0) {
            itemHolder.count_bought.setText(String.valueOf(value1 / 10000) + "万人参与");
        } else {
            itemHolder.count_bought.setText(String.valueOf(data.getCount_bought()) + "人参与");
        }
        itemHolder.fee.setText(String.valueOf(data.getFee()) + "元／月起");
        itemHolder.issuer.setText(data.getIssuer());

        return convertView;
    }

    class ItemHolder {
        public TextView name;
        public TextView description1;
        public TextView description2;
        public TextView amount_max;
        public TextView count_bought;
        public TextView fee;
        public TextView issuer;
    }
}
