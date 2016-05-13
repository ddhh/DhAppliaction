package com.dh.dhappliaction.db;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

import com.dh.dhappliaction.bean.CallLogBean;
import com.dh.dhappliaction.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 端辉 on 2016/3/3.
 */
public class OperateCallLogUtil {

    public static CallLogBean getNewCallLog(Context context) {
        CallLogBean cb = new CallLogBean();
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            long duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
            if (name == null || name.equals("")) {
                name = number;
            }
            cb.setName(name);
            cb.setNumber(number);
            cb.setType(type);
            cb.setDate(setDate(date));
            cb.setTime(TimeUtil.SecondToTime(duration));
        }
        if (cursor != null) {
            cursor.close();
        }
        return cb;
    }

    public static List<CallLogBean> getCallLog(Context context, int i) {
        List<CallLogBean> list = new ArrayList<>();
        Set<CallLogBean> set = new LinkedHashSet<>();
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION
        };
        String selection = getSelectionType(i);
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                CallLogBean cb = new CallLogBean();
                String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                long duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
                if (name == null || name.equals("")) {
                    name = number;
                }
                cb.setName(name);
                cb.setNumber(number);
                cb.setType(type);
                cb.setDate(setDate(date));
                cb.setTime(TimeUtil.SecondToTime(duration));
                set.add(cb);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        list.addAll(set);
        return list;
    }

    private static String setDate(long date) {
        Date nowTime = new Date(System.currentTimeMillis());
        return TimeUtil.myTime(nowTime, date);
    }

    //    来电：CallLog.Calls.INCOMING_TYPE （常量值：1）
//    已拨：CallLog.Calls.OUTGOING_TYPE（常量值：2）
//    未接：CallLog.Calls.MISSED_TYPE（常量值：3）
    private static String getSelectionType(int i) {
        switch (i) {
            case 0:
                return "type=1 or type=2 or type=3";
            case 1:
                return "type=1";
            case 2:
                return "type=2";
            case 3:
                return "type=3";
            default:
                return "type=1 or type=2 or type=3";
        }
    }


    public static List<CallLogBean> getOnePersonAllCalllogInfo(Context context, String number) {
        List<CallLogBean> list = new ArrayList<>();
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION
        };
        String selection = "number=" + number;
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                CallLogBean cb = new CallLogBean();
                String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                long duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
                if (name == null || name.equals("")) {
                    name = number;
                }
                cb.setName(name);
                cb.setNumber(number);
                cb.setType(type);
                cb.setDate(setDate(date));
                cb.setTime(TimeUtil.SecondToTime(duration));
                list.add(cb);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public static boolean delete(Context context, String number) {

        Uri uri = CallLog.Calls.CONTENT_URI;
        int result = 0;
        result += context.getContentResolver().delete(uri, "number=?", new String[]{number});
        if (result > 0) {
            return true;
        }
        return false;
    }

}
