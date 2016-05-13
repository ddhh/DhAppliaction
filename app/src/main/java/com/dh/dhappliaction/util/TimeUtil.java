package com.dh.dhappliaction.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 端  辉  on 2016/1/16.
 */
public class TimeUtil {

    private static long getStartTime(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }
    private static long getEndTime(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 29);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime().getTime();
    }

    public static String myTime(Date nowTime,long time){
        SimpleDateFormat sdf = null;

        if(nowTime.getTime() -(60*1000) < time){
            return "刚刚";
        }else if(nowTime.getTime() - (60*1000*60) < time){
            int m = (int)(nowTime.getTime()-time)/(60*1000);
            return m+"分钟前";
        }else if(getStartTime()<time&&time<getEndTime()){
            sdf = new SimpleDateFormat("今天 HH:mm");
            return sdf.format(new Date(time));
        }else if(time<getStartTime()&&time>getStartTime()-(60*1000*60*24)){
            sdf = new SimpleDateFormat("昨天 HH:mm");
            return sdf.format(new Date(time));
        }else if(nowTime.getTime() - (60*1000*60*24*7) <time){
            sdf = new SimpleDateFormat("EEEE HH:mm");
            return sdf.format(new Date(time));
        }else {
            sdf = new SimpleDateFormat("yyyy年MM月dd");
            return sdf.format(new Date(time));
        }
    }

    public static String SecondToTime(long t){
        int h=(int)(t/3600%24);
        int m=(int)(t/60%60);
        int s=(int)(t%60);
        if(h>0) {
            return h + "小时" + m + "分钟" + s+"秒";
        }else{
            if(m>0){
                return m+"分钟"+s+"秒";
            }else{
                return s+"秒";
            }
        }
    }

}
