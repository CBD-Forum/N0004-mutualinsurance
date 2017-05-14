package com.pingan.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pingan.demo.R;
import com.pingan.demo.model.entity.Insurance;

import java.util.List;

/**
 * Created by guolidong752 on 16/1/1.
 */

public class MyHomeListAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private List<Insurance> mList;
    private Context mContext;

    public MyHomeListAdapter(Context context, List<Insurance> list) {
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
            convertView = mInflater.inflate(R.layout.item_myhome_listview_item, null);
            itemHolder = new ItemHolder();
            itemHolder.name = (TextView) convertView.findViewById(R.id.name);
            itemHolder.count_helped = (TextView) convertView.findViewById(R.id.count_helped);
            itemHolder.balance = (TextView) convertView.findViewById(R.id.balance);
            itemHolder.count_bought = (TextView) convertView.findViewById(R.id.count_bought);
            itemHolder.fee = (TextView) convertView.findViewById(R.id.fee);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        itemHolder.name.setText(data.getName());
        itemHolder.count_helped.setText(data.getCount_helped() + "人");
        itemHolder.balance.setText(String.valueOf(data.getBalance()) + "元");
        itemHolder.count_bought.setText(String.valueOf(data.getCount_bought()) + "人");
        itemHolder.fee.setText(String.valueOf(data.getFee()) + "元");

        return convertView;
    }

    class ItemHolder {
        public TextView name;
        public TextView count_bought;
        public TextView count_helped;
        public TextView balance;
        public TextView fee;
    }
}
