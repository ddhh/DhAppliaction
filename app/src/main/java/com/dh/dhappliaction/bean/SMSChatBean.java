package com.dh.dhappliaction.bean;

/**
 * Created by 端辉 on 2016/3/2.
 */
public class SMSChatBean {

    private String name;
    private String content;
    private String date;
    private int layoutId;//用布局Id来区分发送者，接收者

    public SMSChatBean() {
    }

    public SMSChatBean(String name, String content, String date,int layoutId) {
        this.name = name;
        this.content = content;
        this.date = date;
        this.layoutId = layoutId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }
}
