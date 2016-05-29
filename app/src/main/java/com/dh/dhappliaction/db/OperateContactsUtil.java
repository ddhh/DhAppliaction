package com.dh.dhappliaction.db;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.dh.dhappliaction.bean.ContactsBean;
import com.dh.dhappliaction.other.ContactInfo;
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
                String _id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String firstLetter = PinYinUtil.getPinyinOfHanyu(name)
                        .substring(0, 1).toUpperCase();
                if (firstLetter.matches("[A-Z]")) {
                    cb.setFirstLetter(firstLetter);
                } else {
                    cb.setFirstLetter("#");
                }
                cb.set_id(_id);
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



    public static Cursor queryContact(Activity context, String[] projection){
        // 获取联系人的所需信息
        Cursor cur = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
        return cur;
    }

    public static List<ContactInfo> getContactInfo(Activity context){
        List<ContactInfo> infoList = new ArrayList<ContactInfo>();

        Cursor cur = queryContact(context, null);

        if(cur.moveToFirst()){
            do{

                // 获取联系人id号
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                // 获取联系人姓名
                String displayName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                ContactInfo info = new ContactInfo(displayName);// 初始化联系人信息

                // 查看联系人有多少电话号码, 如果没有返回0
                int phoneCount = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if(phoneCount>0){

                    Cursor phonesCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id , null, null);

                    if(phonesCursor.moveToFirst()) {
                        List<ContactInfo.PhoneInfo> phoneNumberList = new ArrayList<ContactInfo.PhoneInfo>();
                        do{
                            // 遍历所有电话号码
                            String phoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            // 对应的联系人类型
                            int type = phonesCursor.getInt(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                            // 初始化联系人电话信息
                            ContactInfo.PhoneInfo phoneInfo = new ContactInfo.PhoneInfo();
                            phoneInfo.type=type;
                            phoneInfo.number=phoneNumber;

                            phoneNumberList.add(phoneInfo);
                        }while(phonesCursor.moveToNext());
                        // 设置联系人电话信息
                        info.setPhoneList(phoneNumberList);
                    }
                }

                // 获得联系人的EMAIL
                Cursor emailCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID+"="+id, null, null);

                if(emailCur.moveToFirst()){
                    List<ContactInfo.EmailInfo> emailList = new ArrayList<ContactInfo.EmailInfo>();
                    do{
                        // 遍历所有的email
                        String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
                        int type = emailCur.getInt(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                        // 初始化联系人邮箱信息
                        ContactInfo.EmailInfo emailInfo=new ContactInfo.EmailInfo();
                        emailInfo.type=type;    // 设置邮箱类型
                        emailInfo.email=email;    // 设置邮箱地址

                        emailList.add(emailInfo);
                    }while(emailCur.moveToNext());

                    info.setEmail(emailList);
                }

                //Cursor postalCursor = getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + "=" + id, null, null);
                infoList.add(info);
            }while(cur.moveToNext());
        }
        return infoList;
    }

}
