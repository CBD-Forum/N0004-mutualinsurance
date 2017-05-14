package com.pingan.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pingan.demo.R;
import com.pingan.demo.model.entity.BlockDetail;

import java.util.List;


public class BlockDetailAdapter extends BaseAdapter {

    private LayoutInflater mInflater = null;
    private List<BlockDetail> mList;
    private Context mContext;

    public BlockDetailAdapter(Context context, List<BlockDetail> list) {
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

        final BlockDetail data = mList.get(position);
        ItemHolder itemHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_block_item, null);
            itemHolder = new ItemHolder();
            itemHolder.height = (TextView) convertView.findViewById(R.id.block_height);
            itemHolder.previousBlockHash = (TextView) convertView.findViewById(R.id.block_prehash);
            itemHolder.trans_uuid = (TextView) convertView.findViewById(R.id.block_trans);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        itemHolder.height.setText(data.getHeight() + "");
        itemHolder.previousBlockHash.setText(data.getPreviousBlockHash());
        itemHolder.trans_uuid.setText(data.getTrans_uuid());

        return convertView;
    }

    class ItemHolder {
        public TextView height;
        public TextView previousBlockHash;
        public TextView trans_uuid;
    }
}
