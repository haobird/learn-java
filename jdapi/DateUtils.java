package com.jd.ecc.cloudbiz.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author hyc
 * @date 2017/7/8
 */
public class DateUtils {


    /**
     * 返回日期的字符串格式
     * @param date
     * @return
     */
    public static String getStringDate(Date date){
        if(date == null) {
            throw new IllegalArgumentException("日期不能是空!");
        } else {
            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sf.format(date);
        }
    }
}
