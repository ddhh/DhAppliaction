package com.dh.dhappliaction.db;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.dh.dhappliaction.bean.ContactsBean;
import com.dh.dhappliaction.util.PinYinUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 端辉 on 2016/3/3.
 */
public class OperateContactsUtil {

    public static List<ContactsBean> getContacts(Context context) {
        List<ContactsBean> list = new ArrayList<>();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Cursor cursor = context.getContentResolver()
                .query(uri, projection, null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContactsBean cb = new ContactsBean();
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String firstLetter = PinYinUtil.getPinyinOfHanyu(name)
                        .substring(0, 1).toUpperCase();
                if (firstLetter.matches("[A-Z]")) {
                    cb.setFirstLetter(firstLetter);
                } else {
                    cb.setFirstLetter("#");
                }
                cb.setName(name);
                cb.setNumber(number);
                list.add(cb);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public static boolean delete(Context context, String name) {

        Uri uri = ContactsContract.RawContacts.CONTENT_URI;
        int result = 0;
        result += context.getContentResolver().delete(uri, "display_name=?", new String[]{name});
        if (result > 0) {
            return true;
        }
        return false;
    }

}
