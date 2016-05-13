package com.dh.dhappliaction.bean;

/**
 * Created by 端辉 on 2016/3/2.
 */
public class SMSBean {

    private String thread_id;
    private String from;
    private String content;
    private String time;

    public SMSBean() {
    }

    public SMSBean(String from, String content, String time) {
        this.from = from;
        this.content = content;
        this.time = time;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof SMSBean)) {
            return false;
        }
        SMSBean sms = (SMSBean) o;
        if (this.thread_id.equals(sms.getThread_id())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.thread_id.hashCode();
    }

    @Override
    public String toString() {
        return "SMSBean{" +
                "thread_id='" + thread_id + '\'' +
                ", from='" + from + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
