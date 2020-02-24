package com.legendwd.hyperpay.aelf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Colin
 * @date 2019/10/18.
 * description：时间戳工具
 */
public class DateUtils {

    /**
     * 获取当前时间戳
     */
    public static long getSystemMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 将时间转换为时间戳
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public String dateToStamp(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    /**
     * 将时间戳转换为时间
     *
     * @param timeMillis
     * @return
     */
    public String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }
}
