package com.pingan.demo.main;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pingan.demo.MyApplication;
import com.pingan.demo.R;
import com.pingan.demo.base.BaseActivity;
import com.pingan.demo.controller.UserController;

/**
 * Created by guolidong752 on 17/5/4.
 */

public class RegisterActivity extends BaseActivity {


    private EditText phoneText;
    private EditText pwd;
    private EditText pwd1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phoneText = (EditText) findViewById(R.id.phone_num);
        pwd = (EditText) findViewById(R.id.pwd);
        pwd1 = (EditText) findViewById(R.id.pwd1);

        findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneText.getText() == null || phoneText.getText().toString().trim().equals("") ) {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.getText() == null || pwd.getText().toString().trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd1.getText() == null || pwd1.getText().toString().trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "确认两次输入的密码相同", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pwd.getText().toString().trim().equals(pwd1.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, "确认两次输入的密码相同", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                MyApplication.getAppContext().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);

            }
        });
    }
}
