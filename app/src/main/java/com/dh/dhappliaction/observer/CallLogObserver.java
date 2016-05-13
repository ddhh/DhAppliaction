package com.dh.dhappliaction.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

import com.dh.dhappliaction.bean.CallLogBean;
import com.dh.dhappliaction.db.OperateCallLogUtil;

/**
 * Created by 端辉 on 2016/3/11.
 */
public class CallLogObserver extends ContentObserver{
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    Context context;
    Handler mHandler;

    public CallLogObserver(Context context,Handler handler) {
        super(handler);
        this.context = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        CallLogBean cb = OperateCallLogUtil.getNewCallLog(context);
        mHandler.obtainMessage(1,cb).sendToTarget();
    }
}
