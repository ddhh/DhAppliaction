package com.dh.dhappliaction.bean;

import java.io.Serializable;

/**
 * Created by 端辉 on 2016/3/3.
 */
public class CallLogBean implements Serializable{

    private String name;
    private String number;
    private String date;
    private int type;
    private String time;

    private String partStr = "";

    public CallLogBean(){}

    public CallLogBean(String name, String number, int type, String date) {
        this.name = name;
        this.number = number;
        this.type = type;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPartStr() {
        return partStr;
    }

    public void setPartStr(String partStr) {
        this.partStr = partStr;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o){
            return true;
        }
        if(o==null){
            return false;
        }
        if(!(o instanceof CallLogBean)){
            return false;
        }
        CallLogBean cb = (CallLogBean)o;
        if(this.number.equals(cb.getNumber())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.number.hashCode();
    }

    @Override
    public String toString() {
        return "CallLogBean{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", date='" + date + '\'' +
                ", type=" + type +
                ", time='" + time + '\'' +
                ", partStr='" + partStr + '\'' +
                '}';
    }
}
