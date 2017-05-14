package com.pingan.demo.model.service;

import com.google.gson.Gson;
import com.pingan.demo.UrlTools;
import com.pingan.demo.controller.UserController;
import com.pingan.demo.model.entity.Block;
import com.pingan.demo.model.entity.Blockchaininfo;
import com.pingan.demo.model.entity.Result;
import com.pingan.http.framework.model.request.BaseRequest;
import com.pingan.http.framework.network.BusinessParser;
import com.pingan.http.framework.network.BusinessResult;
import com.pingan.http.framework.network.GetServiceParams;
import com.pingan.http.framework.network.ServiceParams;

import okhttp3.Credentials;


/**
 * Created by guolidong752 on 16/1/3.
 */

public class BlockService {
    public static final String SERVICE_TAG_getBlock = "Block";
    public static final String SERVICE_TAG_getBlockDetail = "BlockDetail";

    public static ServiceParams getBlock(final Blockchaininfo blockchaininfo) {
        BaseRequest request = new BaseRequest();
        GetServiceParams<String> serviceParams = new GetServiceParams<>(
                UrlTools.GET_CHAIN + request.getReqUrl(), String.class);
        String credential = Credentials.basic(UserController.getInstance().getUser(),
                UserController.getInstance().getPass());
        serviceParams.addHeader("Authorization", credential);
        serviceParams.setServiceTag(SERVICE_TAG_getBlock)
                .setBusinessParser(new BusinessParser<String>() {
                    @Override
                    public BusinessResult parseData(String data) {
                        Blockchaininfo info = null;
                        Result result1 = null;
                        BusinessResult result = new BusinessResult();
                        try {
                            info = new Gson().fromJson(data, Blockchaininfo.class);
                            blockchaininfo.setCurrentBlockHash(info.getCurrentBlockHash());
                            blockchaininfo.setHeight(info.getHeight());
                            blockchaininfo.setPreviousBlockHash(info.getPreviousBlockHash());
                            result.isSuccess = true;
                        } catch (Exception e) {
                            result.isSuccess = false;
                        }
                        return result;
                    }
                });
        return serviceParams;
    }

    //入参：basic auth && id 出参：Error || Block
    //    public static String GET_CHAIN_BLOCKS = getHost() + "/chain/blocks/";//+{id}获取区块信息
    public static ServiceParams getBlockDetail(int height, final Block block) {
//        BaseRequest request = new BaseRequest();
        GetServiceParams<String> serviceParams = new GetServiceParams<>(
                UrlTools.GET_CHAIN_BLOCKS + height, String.class);
//        String credential = Credentials.basic(UserController.getInstance().getUser(),
//                UserController.getInstance().getPass());
//        serviceParams.addHeader("Authorization", credential);
        serviceParams.setServiceTag(SERVICE_TAG_getBlockDetail)
                .setBusinessParser(new BusinessParser<String>() {
                    @Override
                    public BusinessResult parseData(String data) {
                        Block info = null;
                        Result result1 = null;
                        BusinessResult result = new BusinessResult();
                        try {
                            info = new Gson().fromJson(data, Block.class);
                            block.setTransactions(info.getTransactions());
                            block.setPreviousBlockHash(info.getPreviousBlockHash());
                            result.isSuccess = true;
                        } catch (Exception e) {
                            result.isSuccess = false;
                        }
                        return result;
                    }
                });
        return serviceParams;
    }
}
