package com.pingan.demo.model.service;

import com.pingan.demo.controller.UserController;
import com.pingan.demo.model.entity.ProfileRes;
import com.pingan.http.framework.model.request.BaseRequest;
import com.pingan.http.framework.network.BusinessParser;
import com.pingan.http.framework.network.BusinessResult;
import com.pingan.http.framework.network.GetServiceParams;
import com.pingan.http.framework.network.ServiceParams;

import okhttp3.Credentials;


/**
 * Created by guolidong752 on 16/1/3.
 */

public class LoginService {
    public static final String SERVICE_TAG_getLogin = "User";

    //    //入参：basic auth 出参：Error || ProfileRes
    //    public static String GET_ME_DETAIL = getHost() + "/me";//获取用户详情

    public static ServiceParams getProfile(String url, final ProfileRes profileRes) {
        BaseRequest request = new BaseRequest();
        GetServiceParams<ProfileRes> serviceParams = new GetServiceParams<>(
                url + request.getReqUrl(), ProfileRes.class);
        String credential = Credentials.basic(UserController.getInstance().getUser(),
                UserController.getInstance().getPass());
        serviceParams.addHeader("Authorization", credential);
        serviceParams.setServiceTag(SERVICE_TAG_getLogin)
                .setBusinessParser(new BusinessParser<ProfileRes>() {
                    @Override
                    public BusinessResult parseData(ProfileRes data) {

                        BusinessResult result = new BusinessResult();
                        if (data.getStatus() != null && data.getStatus().getCode() == 0) {
                            result.isSuccess = true;
                            profileRes.setData(data.getData());
                        } else {
                            result.isSuccess = false;
                        }
                        return result;
                    }
                });
        return serviceParams;
    }
}
