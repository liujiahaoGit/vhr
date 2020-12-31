package com.cicro.vhr.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * @className: DateConverter
 * @description 日期格式化 当前台传入对应格式的日期字符串时  会直接解析成相应的格式的Date对象
 * @since JDK1.8
 * @author ljh
 * @createdAt  2020/8/5 0005
 * @version 1.0.0
 **/
@Component
public class DateConverter implements Converter<String, Date> {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date convert(String s) {
        try {
            return simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
