package com.dh.dhappliaction.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dh.dhappliaction.R;
import com.dh.dhappliaction.bean.SMSChatBean;

import java.util.List;

/**
 * Created by 端辉 on 2016/3/2.
 */
public class SMSChatRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<SMSChatBean> list;

    public SMSChatRecyclerViewAdapter(List<SMSChatBean> list) {
        super();
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder mHolder = (MyViewHolder)holder;
        mHolder.tv_content.setText(list.get(position).getContent());
        mHolder.tv_date.setText(list.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getLayoutId();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_content;
        private TextView tv_date;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView)itemView.findViewById(R.id.tv_sms_chat_content);
            tv_date = (TextView)itemView.findViewById(R.id.tv_sms_chat_date);
        }
    }

}
