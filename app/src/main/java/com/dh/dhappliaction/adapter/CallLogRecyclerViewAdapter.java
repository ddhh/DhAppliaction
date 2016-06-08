package com.dh.dhappliaction.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dh.dhappliaction.R;
import com.dh.dhappliaction.bean.CallLogBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 端辉 on 2016/3/3.
 */
public class CallLogRecyclerViewAdapter extends RecyclerView.Adapter{

    private List<CallLogBean> list = new ArrayList<>();

    private onItemClickListener listener;

    public List<CallLogBean> getList() {
        return list;
    }

    public void setList(List<CallLogBean> list) {
        this.list = list;
    }

    public CallLogRecyclerViewAdapter(List<CallLogBean> list) {
        this.list = list;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_calllog,parent,false);
        TypedValue typedValue = new TypedValue();
        parent.getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        view.setBackgroundResource(typedValue.resourceId);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder mHolder = (MyViewHolder)holder;
        mHolder.tv_name.setText(list.get(position).getName());
        mHolder.tv_number.setText("");
        if (!TextUtils.isEmpty(list.get(position).getPartStr())) {
            int i = list.get(position).getNumber().indexOf(list.get(position).getPartStr());
            mHolder.tv_number.append(list.get(position).getNumber().substring(0, i));
            SpannableString spanString = new SpannableString(list.get(position).getPartStr());
            ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
            spanString.setSpan(span, 0, list.get(position).getPartStr().length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mHolder.tv_number.append(spanString);
            mHolder.tv_number.append(list.get(position).getNumber().substring(
                    list.get(position).getPartStr().length() + i, list.get(position).getNumber().length()));
        }else{
            mHolder.tv_number.setText(list.get(position).getNumber());
        }
        mHolder.tv_date.setText(list.get(position).getDate());
        mHolder.iv_type.setImageResource(getImageId(list.get(position).getType()));
        mHolder.position = position;
    }

    private int getImageId(int type){
        switch (type){
            case 1:
                return R.drawable.ic_call_received_blue_700_18dp;
            case 2:
                return R.drawable.ic_call_made_green_a700_18dp;
            case 3:
                return R.drawable.ic_call_missed_red_700_18dp;
            default:
                return 0;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_type;
        private TextView tv_name;
        private TextView tv_number;
        private TextView tv_date;
        private int position;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_type = (ImageView) itemView.findViewById(R.id.iv_calllog_type);
            tv_name = (TextView) itemView.findViewById(R.id.tv_calllog_name);
            tv_number = (TextView) itemView.findViewById(R.id.tv_calllog_number);
            tv_date = (TextView) itemView.findViewById(R.id.tv_calllog_date);
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
