package com.pingan.demo.model.service;

import com.pingan.demo.UrlTools;
import com.pingan.demo.controller.UserController;
import com.pingan.demo.model.entity.Insurance;
import com.pingan.demo.model.entity.InsurancesRes;
import com.pingan.demo.model.entity.Result;
import com.pingan.http.framework.network.BusinessParser;
import com.pingan.http.framework.network.BusinessResult;
import com.pingan.http.framework.network.GetServiceParams;
import com.pingan.http.framework.network.PostServiceParams;
import com.pingan.http.framework.network.ServiceParams;

import java.util.ArrayList;

import okhttp3.Credentials;


/**
 * Created by guolidong752 on 16/1/3.
 */

public class InsurancesService {
    public static final String SERVICE_TAG_getInsurance = "Insurances";

    public static ServiceParams getInsurances(final InsurancesRes viewModel) {
        GetServiceParams<InsurancesRes> serviceParams = new GetServiceParams<>(
                UrlTools.GET_INSURANCE, InsurancesRes.class);
        String credential = Credentials.basic(UserController.getInstance().getUser(),
                UserController.getInstance().getPass());
        serviceParams.addHeader("Authorization", credential);
        serviceParams.setServiceTag(SERVICE_TAG_getInsurance)
                .setBusinessParser(new BusinessParser<InsurancesRes>() {
                    @Override
                    public BusinessResult parseData(InsurancesRes data) {

                        BusinessResult result = new BusinessResult();
                        if (result.isSuccess) {
                            viewModel.setData(data.getData());
                        } else {
                            viewModel.setData(new ArrayList<Insurance>());
                            result.isSuccess = false;
                        }
                        return result;
                    }
                });
        return serviceParams;
    }

    public static ServiceParams doPostInsurance() {
        PostServiceParams<Result> serviceParams = new PostServiceParams<>(UrlTools.POST_REQUEST,
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
