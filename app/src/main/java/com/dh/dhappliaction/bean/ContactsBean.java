package com.dh.dhappliaction.bean;

/**
 * Created by 端辉 on 2016/3/3.
 */
public class ContactsBean {

    private String firstLetter;
    private String name;
    private String number;

    public ContactsBean(){}

    public ContactsBean(String firstLetter, String name) {
        this.firstLetter = firstLetter;
        this.name = name;
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
                "firstLetter='" + firstLetter + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
