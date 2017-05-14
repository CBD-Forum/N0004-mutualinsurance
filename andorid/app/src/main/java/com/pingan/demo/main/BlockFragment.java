package com.pingan.demo.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pingan.demo.MyApplication;
import com.pingan.demo.R;
import com.pingan.demo.adapter.BlockDetailAdapter;
import com.pingan.demo.base.BaseFragment;
import com.pingan.demo.model.entity.Block;
import com.pingan.demo.model.entity.BlockDetail;
import com.pingan.demo.model.entity.Blockchaininfo;
import com.pingan.demo.model.service.BlockService;
import com.pingan.demo.refreshlist.XListView;
import com.pingan.http.framework.network.ServiceTask;
import com.pingan.http.framework.task.NetworkExcuter;
import com.pingan.http.framework.task.NetwrokTaskError;
import com.pingan.http.framework.task.ServiceCallback;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import static com.pingan.demo.model.service.BlockService.SERVICE_TAG_getBlock;
import static com.pingan.demo.model.service.BlockService.SERVICE_TAG_getBlockDetail;

/**
 * Created by guolidong752 on 17/5/4.
 */

public class BlockFragment extends BaseFragment {
    private XListView mListView;
    private Blockchaininfo blockchaininfo;
    private TextView blockMaxCount;
    private TextView currentBlockHash;
    private TextView previousBlockHash;
    private Block cusorBlock;
    private int cusorHeight_down;
    private final LinkedList<BlockDetail> blockDetails = new LinkedList<>();
    private BlockDetailAdapter adapter;
    private ServiceCallback taskCallback = new ServiceCallback() {
        @Override
        public void onTaskStart(String serverTag) {
            content_ll.showProcess();
        }

        @Override
        public void onTaskSuccess(final String serverTag) {
            MyApplication.getAppContext().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (serverTag.equals(SERVICE_TAG_getBlock)) {
                        initTopData();
                        content_ll.removeProcess();
                    } else if (serverTag.equals(SERVICE_TAG_getBlockDetail)) {
                        initData();
                    }
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

    public BlockFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainLayout = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_block, baseLayout);
        return baseLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mainLayout != null) {
            mListView = (XListView) mainLayout.findViewById(R.id.list);
            blockchaininfo = new Blockchaininfo();
            cusorBlock = new Block();
            adapter = new BlockDetailAdapter(getActivity(), blockDetails);
            mListView.setAdapter(adapter);
            currentBlockHash = (TextView) mainLayout.findViewById(R.id.currentBlockHash);
            previousBlockHash = (TextView) mainLayout.findViewById(R.id.previousBlockHash);
            cusorHeight_down = 0;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getBlock();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getBlock();
    }

    private void getBlock() {
        synchronized (blockDetails) {
            blockDetails.clear();
            //        //入参：basic auth 出参：Error || Blockchaininfo
            //        public static String GET_CHAIN = getHost() + "/chain";//获取区块链信息
            ServiceTask serviceTask = new ServiceTask(BlockService.getBlock(blockchaininfo));
            serviceTask.setCancelable(true).setShowProcess(true);
            serviceTask.setCallback(taskCallback);
            NetworkExcuter.getInstance().excute(serviceTask, this);
        }
    }


    private void initTopData() {
        synchronized (blockDetails) {
            blockMaxCount = (TextView) mainLayout.findViewById(R.id.blockMaxCount);
            blockMaxCount.setText(String.valueOf(blockchaininfo.getHeight()));
            currentBlockHash.setText(blockchaininfo.getCurrentBlockHash());
            previousBlockHash.setText(blockchaininfo.getPreviousBlockHash());
            blockDetails.clear();
            cusorHeight_down = blockchaininfo.getHeight() - 1;
            getBlockDetail(cusorHeight_down);
        }
    }

    private void initData() {
        synchronized (blockDetails) {
            if (cusorBlock != null && cusorBlock.getTransactions() != null) {
                if (!blockDetails.contains(cusorBlock)) {
                    blockDetails.addLast(
                            new BlockDetail(cusorHeight_down, cusorBlock.getPreviousBlockHash(),
                                    cusorBlock.getTransactions().size()+""));
                }
//                for (int i = 0; i < cusorBlock.getTransactions().size(); i++) {
//                    if (!blockDetails.contains(cusorBlock)) {
//                        blockDetails.addLast(
//                                new BlockDetail(cusorHeight_down, cusorBlock.getPreviousBlockHash(),
//                                        cusorBlock.getTransactions().get(i).getUuid()));
//                    }
//                }
            }
            Collections.sort(blockDetails, new Comparator<BlockDetail>() {
                @Override
                public int compare(BlockDetail lhs, BlockDetail rhs) {
                    if (lhs.getHeight() < rhs.getHeight()) {
                        return 1;
                    } else {
                        return -1;
                    }

                }
            });
            if (cusorHeight_down > 0) {
                cusorHeight_down--;
                getBlockDetail(cusorHeight_down);
            }

            adapter.notifyDataSetChanged();
        }
    }

    private void getBlockDetail(int height) {
        synchronized (blockDetails) {
            ServiceTask serviceTask = new ServiceTask(
                    BlockService.getBlockDetail(height, cusorBlock));
            serviceTask.setCancelable(true).setShowProcess(true);
            serviceTask.setCallback(taskCallback);
            NetworkExcuter.getInstance().excute(serviceTask, this);
        }
    }
}
