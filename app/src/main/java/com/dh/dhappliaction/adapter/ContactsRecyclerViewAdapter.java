package com.dh.dhappliaction.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.dh.dhappliaction.R;
import com.dh.dhappliaction.bean.ContactsBean;

import java.util.List;

/**
 * Created by 端辉 on 2016/3/3.
 */
public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter implements SectionIndexer {

    private List<ContactsBean> list;
    private onItemClickListener listener;

    public ContactsRecyclerViewAdapter(List<ContactsBean> list) {
        this.list = list;
    }

    public List<ContactsBean> getList() {
        return list;
    }

    public void setList(List<ContactsBean> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contacts,parent,false);
        TypedValue typedValue = new TypedValue();
        parent.getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        view.setBackgroundResource(typedValue.resourceId);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder mHolder = (MyViewHolder)holder;
        holder.itemView.setClickable(true);
        int section = getSectionForPosition(position);
        if(position==getPositionForSection(section)){
            mHolder.tv_firstletter.setVisibility(View.VISIBLE);
            mHolder.tv_firstletter.setText(list.get(position).getFirstLetter());
        }else{
            mHolder.tv_firstletter.setVisibility(View.GONE);
        }
        mHolder.tv_name.setText(list.get(position).getName());
        mHolder.position = position;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for(int i=0;i<getItemCount();i++){
            char firstChar = list.get(i).getFirstLetter().charAt(0);
            if(sectionIndex==firstChar){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        if(list!=null){
            return list.get(position).getFirstLetter().charAt(0);
        }
        return -1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_firstletter;
        private TextView tv_name;
        private int position;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_firstletter = (TextView)itemView.findViewById(R.id.tv_contacts_firstletter);
            tv_name = (TextView)itemView.findViewById(R.id.tv_contacts_name);
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
