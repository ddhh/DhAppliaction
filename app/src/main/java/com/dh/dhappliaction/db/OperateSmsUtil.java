package com.dh.dhappliaction.db;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import com.dh.dhappliaction.R;
import com.dh.dhappliaction.bean.SMSBean;
import com.dh.dhappliaction.bean.SMSChatBean;
import com.dh.dhappliaction.util.TimeUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 端辉 on 2016/3/2.
 */
public class OperateSmsUtil {


    /**
     *    content://sms/             所有短信
     *    content://sms/inbox        收件箱
     *    content://sms/sent         已发送
     *    content://sms/draft        草稿
     *    content://sms/outbox       发件箱
     *    content://sms/failed       发送失败
     *    content://sms/queued       待发送列表
     */

    /**
     * 所有的短信
     */
    public static final String SMS_URI_ALL = "content://sms/";
    /**
     * 收件箱短信
     */
    public static final String SMS_URI_INBOX = "content://sms/inbox";
    /**
     * 已发送短信
     */
    public static final String SMS_URI_SEND = "content://sms/sent";
    /**
     * 草稿箱短信
     */
    public static final String SMS_URI_DRAFT = "content://sms/draft";


    public static final String SMS_URI_DELETE = "content://sms/conversations/";

    public static List<SMSBean> getAllSMS(Context context) {
        List<SMSBean> list = new ArrayList<>();
        Set<SMSBean> set = new LinkedHashSet<>();
        Uri uri = Uri.parse(SMS_URI_ALL);
        String[] projection =
                {
                        "thread_id", "address", "body", "date"
                };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SMSBean sms = new SMSBean();
                String thread_id = cursor.getString(cursor.getColumnIndex("thread_id"));
                String from = cursor.getString(cursor.getColumnIndex("address"));
                String content = cursor.getString(cursor.getColumnIndex("body"));
                long date = cursor.getLong(cursor.getColumnIndex("date"));
                sms.setThread_id(thread_id);
                sms.setFrom(from);
                sms.setContent(content);
                sms.setTime(setDate(date));
                set.add(sms);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        list.addAll(set);
        return list;
    }

    public static SMSChatBean getNewSmsChatData(Context context, String thread_id) {
        SMSChatBean scb = new SMSChatBean();
        Uri uri = Uri.parse(SMS_URI_INBOX);
        String[] projection =
                {
                        "address", "body", "date", "type"
                };
        Cursor cursor = context.getContentResolver().query(uri, projection, "thread_id=" + thread_id, null, "date desc");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String from = cursor.getString(cursor.getColumnIndex("address"));
            String content = cursor.getString(cursor.getColumnIndex("body"));
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            scb.setName(from);
            scb.setContent(content);
            scb.setDate(setDate(date));
            if (type == 1) {
                scb.setLayoutId(R.layout.sms_chat_he_list_item);
            } else {
                scb.setLayoutId(R.layout.sms_chat_me_list_item);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return scb;
    }

    public static List<SMSChatBean> getSMSChatData(Context context, String thread_id) {
        List<SMSChatBean> list = new ArrayList<>();
        Uri uri = Uri.parse(SMS_URI_ALL);
        String[] projection =
                {
                        "address", "body", "date", "type"
                };
        Cursor cursor = context.getContentResolver().query(uri, projection, "thread_id=" + thread_id, null, "date asc");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SMSChatBean smschat = new SMSChatBean();
                String from = cursor.getString(cursor.getColumnIndex("address"));
                String content = cursor.getString(cursor.getColumnIndex("body"));
                long date = cursor.getLong(cursor.getColumnIndex("date"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                smschat.setName(from);
                smschat.setContent(content);
                smschat.setDate(setDate(date));
                if (type == 1) {
                    smschat.setLayoutId(R.layout.sms_chat_he_list_item);
                } else {
                    smschat.setLayoutId(R.layout.sms_chat_me_list_item);
                }
                list.add(smschat);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public static SMSBean getOnSMSInfo(Context context, String phoneNumber, String thread_id) {
        SMSBean sms = new SMSBean();
        Uri uri = Uri.parse(SMS_URI_ALL);
        String[] projection =
                {
                        "thread_id", "address", "body", "date"
                };
        if (phoneNumber.startsWith("+86")) {
            phoneNumber = phoneNumber.substring(3, phoneNumber.length());
        }
        Cursor cursor = context.getContentResolver().query(uri, projection, "thread_id=" + thread_id + " and address=" + phoneNumber, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            thread_id = cursor.getString(cursor.getColumnIndex("thread_id"));
            String number = cursor.getString(cursor.getColumnIndex("address"));
            String content = cursor.getString(cursor.getColumnIndex("body"));
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            sms.setThread_id(thread_id);
            sms.setFrom(number);
            sms.setContent(content);
            sms.setTime(setDate(date));
        }
        if (cursor != null) {
            cursor.close();
        }
        return sms;
    }

    private static String setDate(long date) {
        Date nowTime = new Date(System.currentTimeMillis());
        return TimeUtil.myTime(nowTime, date);
    }

    public static boolean delete(Context context, String thread_id) {
        Uri uri = Uri.parse(SMS_URI_DELETE + thread_id);
        int result = context.getContentResolver().delete(uri, null, null);
        if (result > 0) {
            return true;
        }
        return false;
    }


    public static SMSBean getNewSms(Context context) {
        SMSBean sms = new SMSBean();
        Uri uri = Uri.parse(SMS_URI_ALL);
        String[] projection =
                {
                        "thread_id", "address", "body", "date"
                };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String thread_id = cursor.getString(cursor.getColumnIndex("thread_id"));
            String from = cursor.getString(cursor.getColumnIndex("address"));
            String content = cursor.getString(cursor.getColumnIndex("body"));
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            sms.setThread_id(thread_id);
            sms.setFrom(from);
            sms.setContent(content);
            sms.setTime(setDate(date));
        }
        if (cursor != null) {
            cursor.close();
        }
        return sms;
    }
}
