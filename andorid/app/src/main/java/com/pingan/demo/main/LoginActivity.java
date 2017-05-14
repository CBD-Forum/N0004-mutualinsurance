package com.pingan.demo.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pingan.demo.MyApplication;
import com.pingan.demo.R;
import com.pingan.demo.UrlTools;
import com.pingan.demo.base.BaseActivity;
import com.pingan.demo.controller.UserController;
import com.pingan.demo.model.entity.ProfileRes;
import com.pingan.demo.model.service.LoginService;
import com.pingan.http.framework.network.ServiceTask;
import com.pingan.http.framework.task.NetworkExcuter;
import com.pingan.http.framework.task.NetwrokTaskError;
import com.pingan.http.framework.task.ServiceCallback;

/**
 * Created by guolidong752 on 17/5/4.
 */

public class LoginActivity extends BaseActivity {


    private EditText phoneNum;
    private EditText pwd;
    private ProfileRes profileRes;
    private ServiceCallback taskCallback = new ServiceCallback() {
        @Override
        public void onTaskStart(String serverTag) {
            content_ll.showProcess();
        }

        @Override
        public void onTaskSuccess(String serverTag) {
            if (serverTag.equals(LoginService.SERVICE_TAG_getLogin)) {

            }
            MyApplication.getAppContext().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    content_ll.removeProcess();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onTaskFail(final NetwrokTaskError error, String serverTag) {
            MyApplication.getAppContext().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, error.errorString, Toast.LENGTH_SHORT)
                            .show();
                    content_ll.removeProcess();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }


    private void initView() {
        phoneNum = (EditText) findViewById(R.id.phone_num);
        pwd = (EditText) findViewById(R.id.pwd);
        profileRes = new ProfileRes();
        this.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNum.getText() == null || phoneNum.getText().toString().trim().equals("") ||
                        pwd.getText() == null || pwd.getText().toString().trim().equals("")) {
                    Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    UserController.getInstance().setUser(phoneNum.getText().toString());
                    UserController.getInstance().setPass(pwd.getText().toString());
                    getLogin();
                }
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getLogin() {
        ServiceTask serviceTask = new ServiceTask(
                LoginService.getProfile(UrlTools.GET_LOGIN, profileRes));
        serviceTask.setCancelable(true).setShowProcess(true);
        serviceTask.setCallback(taskCallback);
        NetworkExcuter.getInstance().excute(serviceTask, this);
    }
}
