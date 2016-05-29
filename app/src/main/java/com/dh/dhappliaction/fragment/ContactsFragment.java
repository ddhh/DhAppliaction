package com.dh.dhappliaction.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dh.dhappliaction.R;
import com.dh.dhappliaction.activity.ContactsInfoActivity;
import com.dh.dhappliaction.adapter.ContactsRecyclerViewAdapter;
import com.dh.dhappliaction.bean.ContactsBean;
import com.dh.dhappliaction.db.OperateContactsUtil;
import com.dh.dhappliaction.observer.ContactsObserver;
import com.dh.dhappliaction.view.RightSideBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 端辉 on 2016/3/2.
 */
public class ContactsFragment extends Fragment{

    private View view;

    private TextView tv_no_msg;
    private RecyclerView mRecyclerView;
    private RightSideBar mRightSideBar;
    private List<ContactsBean> mList = new ArrayList<>();
    private ContactsRecyclerViewAdapter mAdapter;

    private String[] dialogStrs = {"呼叫","发短信","通过短信发送号码","编辑联系人","从联系人列表中删除"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contacts_layout,container,false);
        initView(view);
        getData();
        setAdapter();
        initSideBar(view);
        return view;
    }

    private void initView(View v){
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_contacts);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration
                        .Builder(getActivity())
                        .marginResId(R.dimen.activity_horizontal_margin, R.dimen.activity_horizontal_margin)
                        .build());
        tv_no_msg = (TextView) v.findViewById(R.id.tv_no_msg);
    }

    private void initSideBar(View v){
        mRightSideBar = (RightSideBar) v.findViewById(R.id.right_bar);
        mRightSideBar.setOnTouchingLetterChangeListener(new RightSideBar.OnTouchingLetterChangeListener() {
            @Override
            public void onTouchingLetterChangeListener(String str) {
                int position = mAdapter.getPositionForSection(str.charAt(0));
                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                llm.scrollToPositionWithOffset(position,0);
                mRecyclerView.setLayoutManager(llm);
                mRightSideBar.showPopup(str);
            }
        });
    }

    private void getData(){
        mList = OperateContactsUtil.getContacts(getActivity());
    }
    private void setAdapter(){
        if(mList.size()<=0){
            mRecyclerView.setVisibility(View.GONE);
            tv_no_msg.setVisibility(View.VISIBLE);
            return;
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        tv_no_msg.setVisibility(View.GONE);
        mAdapter = new ContactsRecyclerViewAdapter(mList);
        mAdapter.setOnItemClickListener(new ContactsRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ContactsInfoActivity.class);
                intent.putExtra(ContactsInfoActivity.INTENT_CONTACTS_NAME,mList.get(position).getName());
                intent.putExtra(ContactsInfoActivity.INTENT_CONTACTS_NUMBER,mList.get(position).getNumber());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(int position) {
                showDialog(position);
                return true;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showDialog(final int position){

        String title = mList.get(position).getName();
        final String number = mList.get(position).getNumber();
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(title);
        b.setItems(dialogStrs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                Uri uri;
                switch (which){
                    case 0:
                        intent = new Intent(Intent.ACTION_CALL);
                        uri = Uri.parse("tel:"+number);
                        intent.setData(uri);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(Intent.ACTION_SENDTO);
                        uri = Uri.parse("sms:"+number);
                        intent.setData(uri);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(Intent.ACTION_SENDTO);
                        uri = Uri.parse("sms:");
                        intent.setData(uri);
                        intent.putExtra("sms_body",number);
                        startActivity(intent);
                        break;
                    case 3:
                        break;
                    case 4:
                        showEnsureDialog(position);
                        break;
                }
            }
        });
        b.create();
        b.show();
    }

    private void deleteInfo(int position){
        mList.remove(position);
        mAdapter.setList(mList);
        mAdapter.notifyDataSetChanged();
    }
    private void showEnsureDialog(final int position){
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
                if(OperateContactsUtil.delete(getActivity(),mList.get(position).getName())){
                    deleteInfo(position);
                }else{
                    Snackbar.make(mRecyclerView,"删除出错，请重试！",Snackbar.LENGTH_LONG).show();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactsObserver observer = new ContactsObserver(getActivity(),new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    mList = (List<ContactsBean>) msg.obj;
                    mAdapter.setList(mList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        getActivity().getContentResolver().registerContentObserver(uri,true,observer);
    }

    public void updateData(){
        getData();
        mAdapter.setList(mList);
        mAdapter.notifyDataSetChanged();
    }

}
