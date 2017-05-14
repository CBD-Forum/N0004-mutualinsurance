package com.pingan.http.framework.task;

import android.app.FragmentManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pingan.http.framework.BaseConfig;
import com.pingan.http.framework.network.BusinessResult;
import com.pingan.http.framework.network.OkhttpUtil;
import com.pingan.http.framework.network.ServiceParams;
import com.pingan.http.framework.network.ServiceTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by iceman on 16/10/27 17:26
 * 邮箱：xubin865@pingan.com.cn
 * 执行网络请求task
 */

public class NetworkExcuter {
    private static NetworkExcuter excuter = new NetworkExcuter();
    private Handler handler;
    private OkHttpClient okHttpClient;
    private HashMap<String, ServiceTask> taskModelHashMap = new HashMap<>();

    private NetworkExcuter() {
        handler = new Handler(Looper.getMainLooper());
        okHttpClient = OkhttpUtil.getmOkHttpClient();
    }

    public static NetworkExcuter getInstance() {
        return excuter;
    }

    /**
     * 执行网络请求
     *
     * @param taskModel 封装数据参数和UI参数
     * @param markAble  上下文唯一标示,用来取消
     */
    public void excute(ServiceTask taskModel, MarkAble markAble) {
        taskModel.getTagConfig().contextTag = markAble.getInstanceTag();
        taskModelHashMap.put(taskModel.getTagConfig().getTag(), taskModel);
        getDataWithLoading(null, taskModel);
    }

    private void getDataWithLoading(FragmentManager fragmentManager, final ServiceTask taskModel) {
        ServiceCallback callback = taskModel.getCallback();
        ServiceParams serviceParams = taskModel.getServiceParams();

        final String tag = taskModel.getTag();
        Request okrequest = makeOkRequest(taskModel);
        Log.e("guolidong", okrequest.toString() + (
                okrequest.body() == null ? " body is null" : (" body is " + okrequest.body())));
        if (okrequest.body() != null) {
            RequestBody requestBody = okrequest.body();
            requestBody.toString();
        }
        Callback networkcallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络异常会走到这里
                handlerNetworkError(tag);
                releaseTask(tag);
            }

            @Override
            public void onResponse(Call call, final Response response) {
                //只要server端有返回,就会走到这里
                if (response.code() == 200) {
                    handleResponse(response, tag);
                } else {
                    handleServerError(response, tag);
                }
                releaseTask(tag);
            }
        };
        if (okrequest != null) {
            if (callback != null) {
                callback.onTaskStart(serviceParams.getServiceTag());
            }
            serviceParams.modifyNetworkExecutor(okHttpClient).newCall(okrequest)
                    .enqueue(networkcallback);
        }
    }

    /**
     * 从map中释放
     *
     * @param tag
     */
    private void releaseTask(String tag) {
        taskModelHashMap.remove(tag);
    }

    /**
     * 处理正常的返回数据
     *
     * @param tag
     */
    private <T> void handleResponse(Response response, String tag) {
        ServiceTask taskModel = taskModelHashMap.get(tag);
        final ServiceParams serviceParams = taskModel.serviceParams;
        final ServiceCallback callback = taskModel.getCallback();
        final T t;

        try {
            if (serviceParams.getResponseType() == String.class) {
                String str = response.toString();
                if (response.body() != null) {
                    str = response.body().string();
                }
                t = (T) str;
            } else {
                String str = response.body().string();
                t = (T) new Gson().fromJson(str, serviceParams.getResponseType());
            }
        } catch (Exception e) {
            NetwrokTaskError error = new NetwrokTaskError(NetwrokTaskError.TaskCodeList.ServerError,
                    "接口异常");
            dispatchErrorResponse(callback, error, serviceParams.getServiceTag());
            return;
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (serviceParams.getBusinessParser() != null) {
                    if (t == null) {
                        NetwrokTaskError error = new NetwrokTaskError(
                                NetwrokTaskError.TaskCodeList.ServerError, "数据异常");
                        dispatchErrorResponse(callback, error, serviceParams.getServiceTag());
                        return;
                    }
                    BusinessResult result = serviceParams.getBusinessParser().parseData(t);
                    if (result.isSuccess) {
                        dispatchResponse(serviceParams.getServiceTag(), callback);
                    } else {
                        NetwrokTaskError error = new NetwrokTaskError(result);
                        dispatchErrorResponse(callback, error, serviceParams.getServiceTag());
                    }
                } else {
                    dispatchResponse(serviceParams.getServiceTag(), callback);
                }
            }
        });


    }

    /**
     * 网络正常,但接口返回异常状态码的处理,如401,404,500等
     */
    private void handleServerError(final Response response, String tag) {
        final ServiceTask taskModel = taskModelHashMap.get(tag);
        handler.post(new Runnable() {
            @Override
            public void run() {
                boolean needBlock = false;
                int code = response.code();
                final NetwrokTaskError taskError = new NetwrokTaskError(
                        NetwrokTaskError.TaskCodeList.ServerError, "请求失败");
                switch (code) {
                    case 401:
                        needBlock = true;
                        break;
                    case 430:
                        needBlock = true;
                        break;
                }
                if (taskModel.getUiConfig().showErrorToast) {
                    Toast.makeText(BaseConfig.getAppContext(), taskError.errorString,
                            Toast.LENGTH_SHORT).show();
                }
                if (needBlock) {
                    cancelRequest(null);
                }
                ServiceCallback callback = taskModel.getCallback();
                dispatchErrorResponse(callback, taskError,
                        taskModel.getServiceParams().getServiceTag());
            }
        });

    }

    /**
     * 根据指定tag取消Request,如果tag为空,则取消所有的请求
     *
     * @param tag
     */
    public void cancelRequest(String tag) {
        if (TextUtils.isEmpty(tag)) {//取消全部
            taskModelHashMap.clear();
            okHttpClient.dispatcher().cancelAll();
        } else {
            List<Call> callList = okHttpClient.dispatcher().queuedCalls();
            for (Call call : callList) {
                Object object = call.request().tag();
                //注意:项目中fresco图片加载也使用了okhttp的网络通道,因此若该call为图片加载,则tag为空
                if (object instanceof String) {
                    String str = (String) object;
                    if (str.contains(tag)) {
                        call.cancel();
                        taskModelHashMap.remove(str);
                    }
                }

            }
            List<Call> runningList = okHttpClient.dispatcher().runningCalls();
            for (Call call : runningList) {
                Object object = call.request().tag();
                //注意:项目中fresco图片加载也使用了okhttp的网络通道,因此若该call为图片加载,则tag为空
                if (object instanceof String) {
                    String str = (String) object;
                    if (str.contains(tag)) {
                        call.cancel();
                        taskModelHashMap.remove(str);
                    }
                }
            }
        }
    }

    /**
     * 网络异常的处理,如超时,无网络连接,请求取消也会走这里
     */

    private void handlerNetworkError(String tag) {
        final ServiceTask taskModel = taskModelHashMap.get(tag);
        if (taskModel != null) {//请求被取消时,此处是拿不到的
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (taskModel.getUiConfig().showErrorToast) {
                        Toast.makeText(BaseConfig.getAppContext(), "网络链接不可用,请稍后再试",
                                Toast.LENGTH_SHORT).show();
                    }
                    NetwrokTaskError taskError = new NetwrokTaskError(
                            NetwrokTaskError.TaskCodeList.NetworkError, "网络链接不可用,请稍后再试");
                    ServiceCallback callback = taskModel.getCallback();
                    dispatchErrorResponse(callback, taskError,
                            taskModel.getServiceParams().getServiceTag());
                }
            });

        }
    }

    /**
     * 最终回传给callback
     *
     * @param callback
     * @param taskError
     * @param serviceTag
     */
    private void dispatchErrorResponse(final ServiceCallback callback,
                                       final NetwrokTaskError taskError, final String serviceTag) {
        if (callback != null) {
            callback.onTaskFail(taskError, serviceTag);
        }
    }


    /**
     * 对response进行分发
     *
     * @param callback
     */
    private void dispatchResponse(final String serviceTag, final ServiceCallback callback) {
        if (callback != null) {
            callback.onTaskSuccess(serviceTag);
        }

    }


    /**
     * 构建okhttp reque
     *
     * @param taskModel
     * @return
     */
    private Request makeOkRequest(ServiceTask taskModel) {
        Request request = taskModel.serviceParams.getRequest(taskModel.getTagConfig().getTag());
        return request;
    }
}
