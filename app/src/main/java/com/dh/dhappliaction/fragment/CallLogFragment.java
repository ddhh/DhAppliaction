package com.dh.dhappliaction.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.dh.dhappliaction.R;
import com.dh.dhappliaction.activity.CallLogInfoActivity;
import com.dh.dhappliaction.adapter.CallLogRecyclerViewAdapter;
import com.dh.dhappliaction.bean.CallLogBean;
import com.dh.dhappliaction.db.OperateCallLogUtil;
import com.dh.dhappliaction.db.OperateSmsUtil;
import com.dh.dhappliaction.observer.CallLogObserver;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 端辉 on 2016/3/2.
 */
public class CallLogFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private List<CallLogBean> mList = new ArrayList<>();
    private List<CallLogBean> tList;
    private CallLogRecyclerViewAdapter mAdapter;

    private AppCompatSpinner mSpinner;

    private String[] dialogStrs = {"发短信", "编辑号码到联系人", "呼叫", "通过短信发送号码", "从通话记录列表删除"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calllog_layout, container, false);
        initView(view);
        getData();
        setAdapter();
        return view;
    }

    private void initView(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_calllog);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration
                        .Builder(getActivity())
                        .marginResId(R.dimen.activity_horizontal_margin, R.dimen.activity_horizontal_margin)
                        .build());

        mSpinner = (AppCompatSpinner) v.findViewById(R.id.spinner_calllog);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mList = OperateCallLogUtil.getCallLog(getActivity(), 0);
                        setAdapter();
                        break;
                    case 1:
                        mList = OperateCallLogUtil.getCallLog(getActivity(), 1);
                        setAdapter();
                        break;
                    case 2:
                        mList = OperateCallLogUtil.getCallLog(getActivity(), 2);
                        setAdapter();
                        break;
                    case 3:
                        mList = OperateCallLogUtil.getCallLog(getActivity(), 3);
                        setAdapter();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getData() {

        mList = OperateCallLogUtil.getCallLog(getActivity(), 0);
        tList = mList;
    }

    private void setAdapter() {
        if (mList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new CallLogRecyclerViewAdapter(mList);
            mAdapter.setOnItemClickListener(new CallLogRecyclerViewAdapter.onItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getActivity(), CallLogInfoActivity.class);
                    intent.putExtra(CallLogInfoActivity.INTENT_CALLLOG_NAME, mList.get(position).getName());
                    intent.putExtra(CallLogInfoActivity.INTENT_CALLLOG_TYPE, mList.get(position).getType());
                    intent.putExtra(CallLogInfoActivity.INTENT_CALLLOG_DATE, mList.get(position).getDate());
                    intent.putExtra(CallLogInfoActivity.INTENT_CALLLOG_TIME, mList.get(position).getTime());
                    intent.putExtra(CallLogInfoActivity.INTENT_CALLLOG_NUMBER, mList.get(position).getNumber());
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(int position) {
                    showDialog(position);
                    return true;
                }
            });
            mRecyclerView.setAdapter(mAdapter);
            return;
        }
        mRecyclerView.setVisibility(View.GONE);
    }

    private void showDialog(final int position) {
        String title = mList.get(position).getName();
        final String number = mList.get(position).getNumber();
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(title);
        b.setItems(dialogStrs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                Uri uri;
                switch (which) {
                    case 0:
                        intent = new Intent(Intent.ACTION_SENDTO);
                        uri = Uri.parse("sms:"+number);
                        intent.setData(uri);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(Intent.ACTION_INSERT);
                        intent.setType("vnd.android.cursor.dir/person");
                        intent.setType("vnd.android.cursor.dir/contact");
                        intent.setType("vnd.android.cursor.dir/raw_contact");
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(Intent.ACTION_CALL);
                        uri = Uri.parse("tel:"+number);
                        intent.setData(uri);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(Intent.ACTION_SENDTO);
                        uri = Uri.parse("sms:");
                        intent.setData(uri);
                        intent.putExtra("sms_body",number);
                        startActivity(intent);
                        break;
                    case 4:
                        showEnsureDialog(position);
                        break;
                }
                dialog.dismiss();
            }
        });
        b.create();
        b.show();
    }

    private void deleteInfo(int position){
        mList.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    private void showEnsureDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("确定删除该号码的所有通话记录吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(OperateCallLogUtil.delete(getActivity(),mList.get(position).getNumber())){
                    deleteInfo(position);
                }else{
                    Snackbar.make(mRecyclerView,"删除出错，请重试！",Snackbar.LENGTH_LONG).show();
                }
            }
        });
        builder.create().show();
    }

    public void reloadAdapter(String str) {
        if(TextUtils.isEmpty(str)){
            mSpinner.setVisibility(View.VISIBLE);
        }else{
            mSpinner.setVisibility(View.GONE);
        }
        mList = new ArrayList<>();
        for(int i=0;i<tList.size();i++){
            CallLogBean cb = tList.get(i);
            if(cb.getNumber().contains(str)){
                cb.setPartStr(str);
                mList.add(cb);
            }
        }
        setAdapter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CallLogObserver observer = new CallLogObserver(getActivity(),new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    CallLogBean cb = (CallLogBean) msg.obj;
                    if(mList.contains(cb)){
                        mList.remove(cb);
                        mList.add(0,cb);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        Uri uri = CallLog.Calls.CONTENT_URI;
        getActivity().getContentResolver().registerContentObserver(uri,true,observer);
    }
}
