package com.zs.ssm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    //日期转字符串
    public static String dataToString(Date date, String param){

        SimpleDateFormat sf=new SimpleDateFormat(param);
        return sf.format(date);
    }

    //字符串转日期
    public static Date stringToDate(String str, String param) throws ParseException {

        SimpleDateFormat sf=new SimpleDateFormat(param);

        return sf.parse(str);
    }
}
