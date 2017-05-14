package com.pingan.http.framework.task;


import com.pingan.http.framework.network.BusinessResult;

/**
 * 封装每一个task的处理结果,主要包含网络层返回的错误和业务层返回的错误
 */
public class NetwrokTaskError {
    public String errorString;
    public int errorCode;
    public NetwrokTaskError(BusinessResult result) {
        this.errorCode = TaskCodeList.BusinessError;
        this.errorString = result.errorString;
    }


    public NetwrokTaskError(int errorCode, String errorString) {
        this.errorCode = errorCode;
        this.errorString = errorString;
    }

    @Override
    public String toString() {
        return "errorCode:" + errorCode + ",errString:" + errorString;
    }

    public interface TaskCodeList {
        int ServerError = 1000;
        int TimeoutError = 1001;
        int NetworkError = 1002;
        int BusinessError = 1003;
    }
}
