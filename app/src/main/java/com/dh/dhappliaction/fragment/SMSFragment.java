package com.dh.dhappliaction.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dh.dhappliaction.R;
import com.dh.dhappliaction.activity.SMSChatActivity;
import com.dh.dhappliaction.adapter.SMSRecyclerViewAdapter;
import com.dh.dhappliaction.bean.SMSBean;
import com.dh.dhappliaction.db.OperateSmsUtil;
import com.dh.dhappliaction.observer.SmsObserver;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 端辉 on 2016/3/2.
 */
public class SMSFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private List<SMSBean> mList = new ArrayList<>();
    private SMSRecyclerViewAdapter mAdapter;

    private String[] dialogStrs = {"呼叫", "发短信", "从信息列表中删除"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sms_layout, container, false);
        initView(view);
        getData();
        setAdapter();
        return view;
    }

    private void initView(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_sms);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration
                        .Builder(getActivity())
                        .marginResId(R.dimen.activity_horizontal_margin, R.dimen.activity_horizontal_margin)
                        .build());
    }

    private void getData() {
        mList = OperateSmsUtil.getAllSMS(getActivity());
    }

    private void setAdapter() {

        if (mList.size() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            return;
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter = new SMSRecyclerViewAdapter(mList);
        mAdapter.setOnItemClickListener(new SMSRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), SMSChatActivity.class);
                intent.putExtra(SMSChatActivity.INTENT_SMS_NUM, phoneNumber = mList.get(position).getFrom());
                intent.putExtra(SMSChatActivity.INTENT_SMS_THREAD_ID, thread_id = mList.get(position).getThread_id());
                startActivityForResult(intent, 1);
            }

            @Override
            public boolean onItemLongClick(int position) {
                showDialog(position);
                return true;
            }

        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private String phoneNumber;
    private String thread_id;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("SMSFragment", "onActivityResuslt");
        SMSBean sms = OperateSmsUtil.getOnSMSInfo(getActivity(), phoneNumber, thread_id);
        if (mList.contains(sms)) {
            mList.remove(sms);
            mList.add(0, sms);
        }
        setAdapter();
    }

    private void showDialog(final int position) {
        final String title = mList.get(position).getFrom();
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(title);
        b.setItems(dialogStrs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                Uri uri;
                switch (which) {
                    case 0:
                        intent = new Intent(Intent.ACTION_CALL);
                        uri = Uri.parse("tel:" + title);
                        intent.setData(uri);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), SMSChatActivity.class);
                        intent.putExtra(SMSChatActivity.INTENT_SMS_NUM, phoneNumber = mList.get(position).getFrom());
                        intent.putExtra(SMSChatActivity.INTENT_SMS_THREAD_ID, thread_id = mList.get(position).getThread_id());
                        startActivityForResult(intent, 1);
                        break;
                    case 2:
                        showEnsureDialog(position);
                        break;
                }
            }
        });
        b.create();
        b.show();
    }

    private void deleteInfo(int position) {
        mList.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    private void showEnsureDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("确定删除该联系人吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (OperateSmsUtil.delete(getActivity(), mList.get(position).getThread_id())) {
                    deleteInfo(position);
                } else {
                    Snackbar.make(mRecyclerView, "删除出错，请重试！", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmsObserver observer = new SmsObserver(getActivity(),new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    SMSBean sb = (SMSBean) msg.obj;
                    if(mList.contains(sb)){
                        mList.remove(sb);
                        mList.add(0,sb);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        Uri uri = Uri.parse(OperateSmsUtil.SMS_URI_ALL);
        getActivity().getContentResolver().registerContentObserver(uri,true,observer);
    }

}
