package com.dh.dhappliaction.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dh.dhappliaction.MainActivity;
import com.dh.dhappliaction.R;
import com.dh.dhappliaction.adapter.SMSChatRecyclerViewAdapter;
import com.dh.dhappliaction.bean.SMSChatBean;
import com.dh.dhappliaction.db.OperateSmsUtil;
import com.dh.dhappliaction.observer.SmsChatObserver;
import com.dh.dhappliaction.util.ReceiverAndSendSms;
import com.dh.dhappliaction.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 端辉 on 2016/3/2.
 */
public class SMSChatActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recyclerview_sms_chat)
    RecyclerView mRecyclerView;
    @Bind(R.id.et_sent_content)
    EditText mEditText;
    @Bind(R.id.bt_sent)
    Button mBtn;
    @Bind(R.id.sms_chat_layout)
    CoordinatorLayout mRootView;

    public static String INTENT_SMS_NUM = "NUMBER";
    public static String INTENT_SMS_THREAD_ID = "THREAD_ID";

    public static boolean IS_START = false;

    private String number;
    private String thread_id;

    private List<SMSChatBean> mList = new ArrayList<>();
    private SMSChatRecyclerViewAdapter mAdapter;

    private boolean isSent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_chat);
        ButterKnife.bind(this);
        initToolbar();
        initView();
        getData();
        setAdapter();

        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                    }
                }
        );
        setSmsChatObserver();
    }

    private void setSmsChatObserver(){
        SmsChatObserver observer = new SmsChatObserver(this,new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    SMSChatBean scb = (SMSChatBean) msg.obj;
                    mList.add(scb);
                    mAdapter.notifyDataSetChanged();
                }
            }
        },thread_id);
        Uri uri = Uri.parse(OperateSmsUtil.SMS_URI_ALL);
        this.getContentResolver().registerContentObserver(uri,true,observer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IS_START = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        IS_START = false;
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        number = getIntent().getStringExtra(INTENT_SMS_NUM);
        getSupportActionBar().setTitle(number);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_sms_chat);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
    }

    private void getData() {
        thread_id = getIntent().getStringExtra(INTENT_SMS_THREAD_ID);
        mList = OperateSmsUtil.getSMSChatData(this, thread_id);
    }

    private void setAdapter() {
        mAdapter = new SMSChatRecyclerViewAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scrollToPosition(mList.size() - 1);
    }

    @OnClick(R.id.bt_sent)
    public void onClick() {
        Toast.makeText(this, "发送", Toast.LENGTH_SHORT).show();
        String msg = mEditText.getText().toString();
        if (msg != null && !msg.equals("")) {
            ReceiverAndSendSms.senTSms(this, number, msg);
            Date nowTime = new Date(System.currentTimeMillis());
            mList.add(new SMSChatBean(number, msg, TimeUtil.myTime(nowTime, nowTime.getTime() - 1), R.layout.sms_chat_me_list_item));
            setAdapter();
            mEditText.setText("");
            isSent = true;
        } else {
            Toast.makeText(this, "发送消息不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sms_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult();
                return true;
            case R.id.action_call:
                Toast.makeText(this, "拨号", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReceiverAndSendSms.unRegisterReceiver(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setResult() {
        if (isSent) {
            setResult(MainActivity.RESULT_UPDATE_SMS);
        }
        finish();
    }

}
