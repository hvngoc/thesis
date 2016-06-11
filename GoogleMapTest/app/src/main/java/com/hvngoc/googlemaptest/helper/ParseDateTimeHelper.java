package com.hvngoc.googlemaptest.helper;

import com.hvngoc.googlemaptest.activity.CONSTANT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Hoang Van Ngoc on 07/05/2016.
 */
public class ParseDateTimeHelper {

    public static String parse(String input){
        try {
            Date date = new SimpleDateFormat(CONSTANT.DATE_TIME_SAVE_FORMAT, Locale.US).parse(input);
            return new SimpleDateFormat(CONSTANT.DATE_TIME_VIEW_FORMAT, Locale.US).format(date);
        } catch (ParseException e) {
            return input;
        }
    }

    public static String getCurrent(){
        return new SimpleDateFormat(CONSTANT.DATE_TIME_SAVE_FORMAT, Locale.US).format(new Date());
    }

    public static String getTempTime(){
        return new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss", Locale.US).format(new Date());
    }
}
