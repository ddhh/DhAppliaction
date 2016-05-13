package com.dh.dhappliaction.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dh.dhappliaction.R;
import com.dh.dhappliaction.bean.SMSBean;

import java.util.List;

/**
 * Created by 端辉 on 2016/3/2.
 */
public class SMSRecyclerViewAdapter extends RecyclerView.Adapter{

    private List<SMSBean> list;

    private onItemClickListener listener;

    public SMSRecyclerViewAdapter(List<SMSBean> list) {
        super();
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sms,parent,false);
        TypedValue typedValue = new TypedValue();
        parent.getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        view.setBackgroundResource(typedValue.resourceId);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder mHolder = (MyViewHolder)holder;
        holder.itemView.setClickable(true);
        mHolder.position = position;
        mHolder.textFrom.setText(list.get(position).getFrom());
        mHolder.textContent.setText(list.get(position).getContent());
        mHolder.textTime.setText(list.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textFrom;
        TextView textContent;
        TextView textTime;

        int position;
        public MyViewHolder(View itemView) {
            super(itemView);
            textFrom = (TextView)itemView.findViewById(R.id.tv_sms_from);
            textContent = (TextView)itemView.findViewById(R.id.tv_sms_content);
            textTime = (TextView)itemView.findViewById(R.id.tv_sms_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return listener.onItemLongClick(position);
                }
            });
        }
    }

    public interface onItemClickListener{
        public void onItemClick(int position);
        public boolean onItemLongClick(int position);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
}
