package com.dh.dhappliaction.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dh.dhappliaction.R;
import com.dh.dhappliaction.bean.BaseBean;
import com.dh.dhappliaction.util.HttpUtil;

/**
 * Created by 端辉 on 2016/5/25.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputLayout input_user;
    private TextInputLayout input_pwd1;
    private TextInputLayout input_pwd2;

    private Button btn_reset;
    private Button btn_register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView(){
        getSupportActionBar().setTitle("注册");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        input_user = (TextInputLayout) findViewById(R.id.input_id);
        input_pwd1 = (TextInputLayout) findViewById(R.id.input_password);
        input_pwd2 = (TextInputLayout) findViewById(R.id.input_password_again);

        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
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

    String user;
    String pwd1;
    String pwd2;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reset:
                input_user.getEditText().setText("");
                input_pwd1.getEditText().setText("");
                input_pwd2.getEditText().setText("");
                break;
            case R.id.btn_register:
                user = input_user.getEditText().getText().toString();
                pwd1 = input_pwd1.getEditText().getText().toString();
                pwd2 = input_pwd2.getEditText().getText().toString();
                if(TextUtils.isEmpty(user)){
                    input_user.setError("注册账号不能为空");
                    return;
                }
                if(TextUtils.isEmpty(pwd1)){
                    input_pwd1.setError("注册密码不能为空");
                    return;
                }
                if(TextUtils.isEmpty(pwd2)){
                    input_pwd2.setError("确认密码不能为空");
                    return;
                }
                if(!pwd1.equals(pwd2)){
                    input_pwd2.setError("两次密码不相同");
                    return;
                }
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        BaseBean bb = HttpUtil.register(user,pwd1);
                        Message msg = mHandler.obtainMessage();
                        msg.obj = bb;
                        mHandler.sendMessage(msg);
                    }
                }.start();
                break;
        }
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseBean bb = (BaseBean)msg.obj;
            if(bb.getCode()==1){
                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
