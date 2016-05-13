package com.dh.dhappliaction.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

import com.dh.dhappliaction.bean.CallLogBean;
import com.dh.dhappliaction.bean.ContactsBean;
import com.dh.dhappliaction.db.OperateCallLogUtil;
import com.dh.dhappliaction.db.OperateContactsUtil;

import java.util.List;

/**
 * Created by 端辉 on 2016/3/11.
 */
public class ContactsObserver extends ContentObserver{
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    Context context;
    Handler mHandler;

    public ContactsObserver(Context context, Handler handler) {
        super(handler);
        this.context = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.d("ContactsObserver", "联系人监视器");
        List<ContactsBean> list = OperateContactsUtil.getContacts(context);
        mHandler.obtainMessage(1,list).sendToTarget();
    }
}
