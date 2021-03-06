package com.ecnu.security.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

import com.ecnu.security.Helper.MLog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Phuylai on 2017/4/26.
 */

public class StringUtil {
    private static final Pattern PASSWORD_PATTERN = Pattern
            .compile("^[\\Sa-zA-Z0-9_.]{6,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    /**
     * check if the string is null
     * @param str
     *
     */

    public static boolean isNull(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }

        return str.equalsIgnoreCase("null");

    }

    /**
     * check if the string is not null
     * @param str
     */

    public static boolean notNull(String str) {
        return !isNull(str);
    }


    /**
     * convert int to string
     * @param value
     */
    public static String convertIntegerToString(Integer value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
     * convert number to string
     * @param value
     */
    public static String convertNumberToString(Number value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
     * convert string to date
     * @param value
     */
    public static Date getDate(String value){
        if(value == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date date = simpleDateFormat.parse(value);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateHourMinute(String value){
        if(value == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
        try {
            return simpleDateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getHourMinute(String time){
        if(time == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm",Locale.CHINA);
        try {
            return simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateTime(String value){
        if(value == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date date = simpleDateFormat.parse(value);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String today = simpleDateFormat.format(new Date());
        return today;
    }

    public static Date getDateWithTime() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return simpleDateFormat.parse(simpleDateFormat.format(date));
    }

    public static String getDateTimeString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public static String twoDigit(int minute) {
        if(minute < 10){
            StringBuilder sb =  new StringBuilder();
            sb.append(0);
            sb.append(minute);
            return sb.toString();
        }
        return String.valueOf(minute);
    }

    public static void setPassWordEditTextHintType(EditText editText) {
        if (editText == null) {
            return;
        }
        editText.setTypeface(Typeface.DEFAULT);
        editText.setTransformationMethod(new PasswordTransformationMethod());
    }

    /*
    convert from Date to String
    * */
    public static String dateToString(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(date);
    }

    public static String dateTimeToString(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public static String dateString(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     *get current time string
     */
    public static String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return year + "/" + (month + 1) + "/" + day + "  " +
                twoDigit(hour) + ":" + twoDigit(minute);
    }

    /**
     *format currency string
     */
    public static String currencyFormat(double unitPrice){
        DecimalFormat format = new DecimalFormat("#,###,###.##");
        return format.format(unitPrice);
    }

    public static boolean checkPassWord(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isTrueEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches() != false;
    }

    public static boolean matchREGEX(String REGEX, String matchString) {
        Pattern pattern = Pattern.compile(REGEX);
        return matchREGEX(pattern, matchString);
    }

    public static boolean matchREGEX(Pattern pattern, String matchString) {
        return pattern.matcher(matchString).matches();
    }

    public static String timeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    public static String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String post(String url, String body)
    {
        //MLog.i("url" , System.lineSeparator() + url);
        //MLog.i("body:" , System.lineSeparator() + body);

        String result = "";
        try
        {
            OutputStreamWriter out = null;
            BufferedReader in = null;
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();

            // 璁剧疆杩炴帴鍙傛暟
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(20000);

            // 鎻愪氦鏁版嵁
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(body);
            out.flush();

            // 璇诲彇杩斿洖鏁版嵁
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = "";
            boolean firstLine = true; // 璇荤涓 琛屼笉鍔犳崲琛岀
            while ((line = in.readLine()) != null)
            {
                if (firstLine)
                {
                    firstLine = false;
                } else
                {
                    result += System.lineSeparator();
                }
                result += line;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

}
