package com.dh.dhappliaction.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dh.dhappliaction.R;
import com.dh.dhappliaction.bean.LoginBean;
import com.dh.dhappliaction.util.HttpUtil;
import com.dh.dhappliaction.util.SharedPreferencesUtil;

/**
 * Created by 端辉 on 2016/5/25.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView register_text;

    Button btn_reset;
    Button btn_login;

    TextInputLayout input_user;
    TextInputLayout input_pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }


    private void initView() {

        getSupportActionBar().setTitle("登录");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        register_text = (TextView) findViewById(R.id.register_text);
        register_text.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);
        input_user = (TextInputLayout) findViewById(R.id.input_id);
        input_pwd = (TextInputLayout) findViewById(R.id.input_password);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ProgressDialog pd = null;
    String user;
    String pwd;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                user = input_user.getEditText().getText().toString();
                pwd = input_pwd.getEditText().getText().toString();
                if (TextUtils.isEmpty(user)) {
                    input_user.setError("账号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    input_pwd.setError("密码不能为空");
                    return;
                }
                //TODO 提交账号密码注册
                pd = ProgressDialog.show(this, "提示", "正在登录...");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        LoginBean lb = HttpUtil.login(user, pwd);
                        Message msg = mHandler.obtainMessage();
                        msg.obj = lb;
                        mHandler.sendMessage(msg);
                    }
                }.start();
                break;
            case R.id.btn_reset:
                input_user.getEditText().setText("");
                input_pwd.getEditText().setText("");
                break;
            case R.id.register_text:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                break;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean flag = false;
            LoginBean lb = (LoginBean) msg.obj;
            Log.d("Log", lb.toString());
            if (lb != null) {
                if (lb.getCode() == 1) {
                    flag = true;
                    SharedPreferencesUtil.editor(LoginActivity.this, user, lb.getUser_id());
                }
            }
            if (pd != null) {
                pd.dismiss();
            }
            if (flag) {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                //TODO 跳转
                setResult(1);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
