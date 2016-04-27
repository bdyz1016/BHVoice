package com.bhsc.mobile.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lynn on 12/7/15.
 */
public class DateFormat {
    private final String TAG = DateFormat.class.getSimpleName();
    private static final long HOUR = 60 * 60 * 1000;
    private static final long MINUTE = 60 * 1000;
    private static final long DAY = 24 * 60 * 60 * 1000;

    private String mHour;
    private String mMinute;
    private String mJust;

    private Locale mLocal;

    private java.text.DateFormat mDateFormatFull;
    private java.text.DateFormat mDateFormatShort;
    private java.text.DateFormat mTimeFormat;

    private Calendar today;
    private Calendar yesterday;
    private Calendar beforeYesterday;

    public DateFormat(){
        mLocal = Locale.getDefault();
        mHour = "小时前";
        mMinute = "分钟前";
        mJust = "刚刚";
        mDateFormatFull = new SimpleDateFormat("yyyy-MM-dd");
        mDateFormatShort = new SimpleDateFormat("MM月dd日");
        mTimeFormat = new SimpleDateFormat("hh:mm");

        today = Calendar.getInstance(mLocal);
        yesterday = Calendar.getInstance(mLocal);
        beforeYesterday = Calendar.getInstance(mLocal);

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        yesterday.setTimeInMillis(today.getTimeInMillis() - DAY);

        beforeYesterday.setTimeInMillis(yesterday.getTimeInMillis() - DAY);
    }

//    public String[] format(long millisecond){
//        String dateStr = "";
//        String timeStr = "";
//        long currentTime = System.currentTimeMillis();
//        long interval = currentTime - millisecond;
//
//        Calendar measureDate = Calendar.getInstance(mLocal);
//        measureDate.setTimeInMillis(millisecond);
//        if(today.before(measureDate)){// 当天数据
//            if(interval > HOUR){
//                timeStr = (int)Math.floor(interval / HOUR) + mHour;
//            } else if(interval > MINUTE){
//                timeStr = (int)Math.floor(interval / MINUTE) + mMinute;
//            } else {
//                timeStr = mJust;
//            }
//        } else if(today.get(Calendar.YEAR) == measureDate.get(Calendar.YEAR)){//今年数据
//            dateStr = mDateFormatShort.format(new Date(millisecond));
//            timeStr = mTimeFormat.format(new Date(millisecond));
//        } else {
//            dateStr = mDateFormatFull.format(new Date(millisecond));
//            timeStr = mTimeFormat.format(new Date(millisecond));
//        }
//        return new String[]{dateStr, timeStr};
//    }

    public String[] format(long millisecond){
        String dateStr = "";
        String timeStr = "";

        Calendar measureDate = Calendar.getInstance(mLocal);
        measureDate.setTimeInMillis(millisecond);
        if(measureDate.after(today)){// 当天数据
            timeStr = mTimeFormat.format(new Date(millisecond));
        } else if(measureDate.before(today) && measureDate.after(yesterday)){
            dateStr = "昨天";
            timeStr = mTimeFormat.format(new Date(millisecond));
        } else if(measureDate.before(yesterday) && measureDate.after(beforeYesterday)){
            dateStr = "前天";
            timeStr = mTimeFormat.format(new Date(millisecond));
        } else if(measureDate.get(Calendar.YEAR) >= today.get(Calendar.YEAR)){
            dateStr = mDateFormatShort.format(new Date(millisecond));
            timeStr = mTimeFormat.format(new Date(millisecond));
        } else if(measureDate.get(Calendar.YEAR) < today.get(Calendar.YEAR)){
            dateStr = mDateFormatFull.format(new Date(millisecond));
            timeStr = mTimeFormat.format(new Date(millisecond));
        }
        return new String[]{dateStr, timeStr};
    }
}
