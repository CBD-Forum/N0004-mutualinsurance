package com.pingan.demo.model.service;

import com.pingan.demo.UrlTools;
import com.pingan.demo.controller.UserController;
import com.pingan.demo.model.entity.Result;
import com.pingan.http.framework.network.BusinessParser;
import com.pingan.http.framework.network.BusinessResult;
import com.pingan.http.framework.network.PostServiceParams;
import com.pingan.http.framework.network.ServiceParams;

import okhttp3.Credentials;

import static com.pingan.demo.model.service.InsurancesService.SERVICE_TAG_getInsurance;

/**
 * Created by guolidong752 on 17/5/12.
 */

public class ChargeService {

    public static ServiceParams doPostCharge() {
        PostServiceParams<Result> serviceParams = new PostServiceParams<>(UrlTools.POST_CHARGE,
                Result.class);
        String credential = Credentials.basic(UserController.getInstance().getUser(),
                UserController.getInstance().getPass());
        serviceParams.addHeader("Authorization", credential);
        serviceParams.addHeader("Content-Type", "application/json;charset=UTF-8");
        serviceParams.setServiceTag(SERVICE_TAG_getInsurance)
                .setBusinessParser(new BusinessParser<Result>() {
                    @Override
                    public BusinessResult parseData(Result data) {

                        BusinessResult result = new BusinessResult();
                        if (result.isSuccess) {
                        } else {
                            result.isSuccess = false;
                        }
                        return result;
                    }
                });
        return serviceParams;
    }
}
