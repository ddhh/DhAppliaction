package com.dh.dhappliaction.bean;

/**
 * Created by 端辉 on 2016/3/3.
 */
public class ContactsBean {

    private String _id;
    private String firstLetter;
    private String name;
    private String number;

    public ContactsBean(){}

    public ContactsBean(String _id,String firstLetter, String name) {
        this._id = _id;
        this.firstLetter = firstLetter;
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "ContactsBean{" +
                "_id='" + _id + '\'' +
                ", firstLetter='" + firstLetter + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
