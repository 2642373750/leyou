package com.zs.ssm.Converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter<String,Date> {
    @Nullable
    @Override
    public Date convert(String source) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-HH-dd HH:mm");
        try {
            return  simpleDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
