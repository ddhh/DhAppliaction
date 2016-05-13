package com.dh.dhappliaction.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

import com.dh.dhappliaction.bean.SMSChatBean;
import com.dh.dhappliaction.db.OperateSmsUtil;

/**
 * Created by 端辉 on 2016/3/11.
 */
public class SmsChatObserver extends ContentObserver {
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */

    private Context context;
    private Handler mHandler;

    private String thread_id;
    public SmsChatObserver(Context context,Handler handler,String thread_id) {
        super(handler);
        this.context = context;
        mHandler = handler;
        this.thread_id = thread_id;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.d("SmsChatObserver", "短信会话监视");
        SMSChatBean scb = OperateSmsUtil.getNewSmsChatData(context,thread_id);
        mHandler.obtainMessage(1,scb).sendToTarget();
    }
}
