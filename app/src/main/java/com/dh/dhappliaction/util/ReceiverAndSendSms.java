package com.dh.dhappliaction.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by 端辉 on 2016/3/3.
 */
public class ReceiverAndSendSms {

    public static String SEND_SMS_ACTION = "SEND_SMS_ACTION";
    public static String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    private static boolean isRegister = false;

    public static void senTSms(Context context,String phoneNumber,String msg) {
        Intent sendIntent = new Intent(SEND_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sendIntent, 0);
        context.registerReceiver(sendBroadcastReceiver,new IntentFilter(SEND_SMS_ACTION));

        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);
        context.registerReceiver(deliverBroadcastReceiver, new IntentFilter(DELIVERED_SMS_ACTION));

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber,null,msg,sentPI,deliverPI);

        isRegister = true;

    }

    private static BroadcastReceiver sendBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:

                    Toast.makeText(context,
                            "短信发送成功", Toast.LENGTH_LONG)
                            .show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    break;
            }
        }
    };

    private static BroadcastReceiver deliverBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,
                    "收信人已经成功接收", Toast.LENGTH_LONG).show();
        }
    };

    public static void unRegisterReceiver(Context context){
        if(isRegister) {
            context.unregisterReceiver(deliverBroadcastReceiver);
            context.unregisterReceiver(sendBroadcastReceiver);
            isRegister = false;
        }
    }

}
