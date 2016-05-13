package com.dh.dhappliaction.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

import com.dh.dhappliaction.bean.SMSBean;
import com.dh.dhappliaction.db.OperateSmsUtil;

/**
 * Created by 端辉 on 2016/3/11.
 */
public class SmsObserver extends ContentObserver{
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    Context context;
    Handler mHandler;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        this.context = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.d("SmsObserver", "短信的监视器");
        SMSBean sb = OperateSmsUtil.getNewSms(context);
        mHandler.obtainMessage(1,sb).sendToTarget();
    }
}
