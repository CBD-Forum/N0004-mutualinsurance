package com.pingan.demo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.pingan.demo.R;
import com.pingan.demo.controller.FragmentManagerControl;
import com.pingan.demo.main.PayDetailFragment;
import com.pingan.demo.main.PayFragment;
import com.pingan.demo.model.entity.Insurance;

import java.util.List;

/**
 * Created by guolidong752 on 17/5/8.
 */

public class PayExListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> groupList;
    private List<List<PayFragment.IPayEntry>> itemList;
    private List<Insurance> mInsurances;

    public PayExListAdapter(Context context, List<String> groupList,
                            List<List<PayFragment.IPayEntry>> itemList,
                            List<Insurance> insurances) {
        this.context = context;
        this.groupList = groupList;
        this.itemList = itemList;
        this.mInsurances = insurances;
    }

    @Override
    public int getGroupCount() {
        if (groupList != null && !groupList.isEmpty()) {
            return groupList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (itemList != null && itemList.size() > groupPosition && !itemList.get(groupPosition)
                .isEmpty()) {
            return itemList.get(groupPosition).size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groupList != null && groupList.size() > groupPosition) {
            return groupList.get(groupPosition);
        } else {
            return null;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (itemList != null && itemList.size() > groupPosition
                && itemList.get(groupPosition).size() > childPosition) {
            return itemList.get(groupPosition).get(childPosition);
        } else {
            return null;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.layout_pay_exlist_group, null);
            groupHolder = new GroupHolder();
            groupHolder.txt = (TextView) convertView.findViewById(R.id.insurance_title);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.txt.setText(groupList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (itemList.get(groupPosition)
                .get(childPosition) instanceof PayFragment.InsurancePayEntry1) {
            ItemHolder1 itemHolder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.layout_pay_exlist_item1, null);
                itemHolder = setHolder1(convertView);
            } else {
                itemHolder = (ItemHolder1) convertView.getTag(R.id.tag_first);
                if (itemHolder == null || !(itemHolder instanceof ItemHolder1)) {
                    convertView = LayoutInflater.from(context)
                            .inflate(R.layout.layout_pay_exlist_item1, null);
                    itemHolder = setHolder1(convertView);
                }
            }
            PayFragment.InsurancePayEntry1 entry1 = (PayFragment.InsurancePayEntry1) itemList
                    .get(groupPosition).get(childPosition);
            itemHolder.payPhoneNum.setText(entry1.payPhoneNum);
            itemHolder.paperList1.setText(entry1.paperList1);
            itemHolder.paperList2.setText(entry1.paperList2);
        } else if (itemList.get(groupPosition)
                .get(childPosition) instanceof PayFragment.InsurancePayEntry2) {
            ItemHolder2 itemHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.layout_pay_exlist_item2, null);
                itemHolder = setHolder2(groupPosition, convertView);
            } else {
                itemHolder = (ItemHolder2) convertView.getTag(R.id.tag_second);
                if (itemHolder == null || !(itemHolder instanceof ItemHolder2)) {
                    convertView = LayoutInflater.from(context)
                            .inflate(R.layout.layout_pay_exlist_item2, null);
                    itemHolder = setHolder2(groupPosition, convertView);
                }
            }
            PayFragment.InsurancePayEntry2 entry2 = (PayFragment.InsurancePayEntry2) itemList
                    .get(groupPosition).get(childPosition);
            itemHolder.description.setText(entry2.description);
            itemHolder.name.setText(entry2.name);
            itemHolder.cardId.setText(entry2.cardId);
            itemHolder.delayTimes.setText(entry2.delayTimes);
        }

        return convertView;
    }

    private ItemHolder2 setHolder2(final int groupPosition, View convertView) {
        ItemHolder2 itemHolder = new ItemHolder2();
        itemHolder.description = (TextView) convertView.findViewById(R.id.description);
        itemHolder.name = (TextView) convertView.findViewById(R.id.name);
        itemHolder.cardId = (TextView) convertView.findViewById(R.id.cardId);
        itemHolder.delayTimes = (TextView) convertView.findViewById(R.id.delayTimes);
        convertView.findViewById(R.id.pay_btn).setVisibility(View.VISIBLE);
        convertView.findViewById(R.id.pay_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayDetailFragment payDetailFragment = new PayDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Insurance", mInsurances.get(groupPosition));
                payDetailFragment.setArguments(bundle);
                FragmentManagerControl.getInstance().addFragment(payDetailFragment);
            }
        });
        convertView.setTag(R.id.tag_second, itemHolder);
        return itemHolder;
    }

    private ItemHolder1 setHolder1(View convertView) {
        ItemHolder1 itemHolder = new ItemHolder1();
        itemHolder.payPhoneNum = (TextView) convertView.findViewById(R.id.pay_phone_num);
        itemHolder.paperList1 = (TextView) convertView.findViewById(R.id.paper_list_1);
        itemHolder.paperList2 = (TextView) convertView.findViewById(R.id.paper_list_2);
        convertView.setTag(R.id.tag_first, itemHolder);
        return itemHolder;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        public TextView txt;
    }

    class ItemHolder1 {
        public TextView payPhoneNum;
        public TextView paperList1;
        public TextView paperList2;
    }

    class ItemHolder2 {
        public TextView description;
        public TextView name;
        public TextView cardId;
        public TextView delayTimes;
    }
}
