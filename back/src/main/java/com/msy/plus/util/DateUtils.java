package com.msy.plus.util;

import com.google.common.base.Throwables;
import com.google.common.collect.Range;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * wzp created on 2021/06/03
 */

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static synchronized String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static synchronized String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static synchronized String formatDate(Date date, Object... pattern) {
        if (date == null) {
            return "";
        }
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 将 Calendar 转为字符串
     * @param curCalendar
     * @param string
     * @return
     */
    public static synchronized String formatDate(Calendar curCalendar, String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(curCalendar.getTime());
        return dateStr;
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static synchronized String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static synchronized String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static synchronized String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    public static synchronized String getYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.YEAR, year);
        return formatDate(cal.getTime(), "yyyy");
    }

    public static synchronized String getSubYear() {
        return formatDate(new Date(), "yy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static synchronized String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static synchronized String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static synchronized String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
     * "yyyy/MM/dd HH:mm", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static synchronized Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     * @param date
     * @return
     */
    public static synchronized long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     * @param date
     * @return
     */
    public static synchronized long pastHour(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     * @param date
     * @return
     */
    public static synchronized long pastMinutes(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     * @param timeMillis
     * @return
     */
    public static synchronized String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static synchronized String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取两个日期之间的天数
     * @param before
     * @param after
     * @return
     */
    public static synchronized double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    public static synchronized long tryNextDelay(long retries, long scale) {
        long delay = (long) Math.pow(2.0D, retries) * scale;
        return delay;
    }

    public static synchronized void delay(long retries, long scale) {
        long delay = tryNextDelay(retries, scale);
        System.out.println("retries:" + retries + "," + " wait..." + delay);
        Uninterruptibles.sleepUninterruptibly(delay, TimeUnit.MILLISECONDS);
    }

    public static synchronized void delay(long retries, long scale, TimeUnit timeUnit) {
        long delay = tryNextDelay(retries, scale);
        System.out.println("retries:" + retries + "," + " wait..." + delay);
        Uninterruptibles.sleepUninterruptibly(delay, timeUnit);
    }

    public static synchronized int dateToInt(Date date) {
        return Long.valueOf(date.getTime() / 1000).intValue();
    }

    public static synchronized int nowToInt() {
        return dateToInt(new Date());
    }

    public static synchronized Date intToDate(int ival) {
        return new Date(Long.valueOf(ival).longValue() * 1000L);
    }

    public static synchronized String intToDateFmt(int ival, String fmt) {
        return DateFormatUtils.format(intToDate(ival), fmt);
    }

    public static synchronized int dateFmtToInt(String fmt) throws ParseException {
        Date date = DateUtils.parseDate(fmt);
        return dateToInt(date);
    }

    public static synchronized boolean laterThan(String httpDateFmt, int dateInt) {
        int createdAt = dateToInt(StringUtils.parseDateForHttp(httpDateFmt));
        return createdAt > dateInt;
    }

    public static synchronized boolean containsOpen(Date value, Date start, Date end) {
        Range<Date> range = Range.open(start, end);
        return range.contains(value);
    }

    public static synchronized boolean containsOpen(Date start, Date end) {
        Range<Date> range = Range.open(start, end);
        return range.contains(new Date());
    }

    public static synchronized boolean containsClosed(Date value, Date start, Date end) {
        Range<Date> range = Range.closed(start, end);
        return range.contains(value);
    }

    public static synchronized int minusDays(int createdAt, int days) {
        Date date = DateUtils.addDays(intToDate(createdAt), -1 * days);
        return dateToInt(date);
    }

    public static synchronized Date yesterday() {
        return DateUtils.addDays(new Date(), -1);
    }

    public static synchronized Date getNextHourStart(Date date, int aNumHours) {
        date = DateUtils.addHours(date, aNumHours);
        date = DateUtils.truncate(date, Calendar.HOUR_OF_DAY);
        return date;
    }

    public static synchronized Date getNextMinuteStart(Date date, int aNumMinutes) {
        date = DateUtils.addMinutes(date, aNumMinutes);
        date = DateUtils.truncate(date, Calendar.MINUTE);
        return date;
    }

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate 较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(betweenDays));
        } catch (ParseException e) {
            throw Throwables.propagate(e);
        }
    }

    public static synchronized String getTimeState(int timestamp) {
        return getTimeState(String.valueOf(timestamp));
    }

    /**
     * 根据 timestamp 生成各类时间状态串
     * @param timestamp 距1970 00:00:00 GMT的秒数
     * @return 时间状态串(如：刚刚5分钟前)
     */
    public static synchronized String getTimeState(String timestamp) {
        if (timestamp == null || "".equals(timestamp)) {
            return "";
        }
        try {
            long ltimestamp = Long.parseLong(timestamp) * 1000;
            if (System.currentTimeMillis() - ltimestamp < 1 * 60 * 1000) {
                return "刚刚";
            } else if (System.currentTimeMillis() - ltimestamp < 30 * 60 * 1000) {
                return ((System.currentTimeMillis() - ltimestamp) / 1000 / 60) + "分钟前";
            } else {
                Calendar now = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(ltimestamp);
                if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                        && c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                    return sdf.format(c.getTime());
                }
                if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                        && c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                    return sdf.format(c.getTime());
                } else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm:ss");
                    return sdf.format(c.getTime());
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm:ss");
                    return sdf.format(c.getTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 当前月，日期向前/后调整至具体的日期
     * @param days
     * @return
     * @throws ParseException
     */
    public static synchronized Date getCurMonthAddDate(Date date, Integer days) throws ParseException {
        if (days == null || days == 0) {
            return DateUtils.getNow();
        }
        date = DateUtils.addDays(date, days);
        switch (days.compareTo(0)) {
            case -1:
                Date firstDate = DateUtils.getMonthFirstDay(date);
                if (date.before(firstDate)) {
                    date = firstDate;
                }
                break;
            case 1:
                Date lastDate = DateUtils.getMonthLastDate(date);
                if (date.after(lastDate)) {
                    date = lastDate;
                }
                break;
            default:
                break;
        }
        return date;
    }

    /**
     * 获取当前月份的第一天
     */
    public static synchronized Calendar getCurMonthFirstDay(int year, int month) throws ParseException {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, year);
        startCalendar.set(Calendar.MONTH, month);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        return startCalendar;
    }

    public static synchronized Date getCurMonthFirstDay() throws ParseException {
        return getMonthFirstDay(getNow());
    }

    public static synchronized Date getMonthFirstDay(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前月份的最后一天
     */
    public static synchronized Calendar getCurMonthLastDay(int year, int month) throws ParseException {
        Calendar thisMonth = DateUtils.getCurMonthFirstDay(year, month);
        thisMonth.set(Calendar.YEAR, year);
        thisMonth.set(Calendar.MONTH, month);
        thisMonth.setFirstDayOfWeek(Calendar.SUNDAY);
        thisMonth.set(Calendar.DAY_OF_MONTH, 1);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, year);
        endCalendar.set(Calendar.MONTH, month);
        endCalendar.set(Calendar.DAY_OF_MONTH, thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        return endCalendar;
    }

    public static synchronized int getCurMonthLastDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getNow());
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static synchronized Date getCurMonthLastDate() {
        return getMonthLastDate(getNow());
    }

    public static synchronized Date getMonthLastDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static synchronized int getCurrentDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(getNow());
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static synchronized Date getNow() {
        return new Date();
    }

    /**
     * 获取当前日期，形式为"yyyy-MM-dd 00:00:00"
     * @return
     */
    public static synchronized Date getCurrentDateOfZero() {
        String currentFormatDate = DateUtils.formatDate(new Date());
        Date currentDate = DateUtils.parseDate(currentFormatDate);
        return currentDate;
    }

    /**
     * 获取有效日期
     * @param date
     * @param front
     * @return
     */
    public static Integer getDateFront(Date date, int front) {
        Date today = getCurrentDateOfZero();
        while (addDays(today, front * -1).before(date)) {
            front--;
            if (front == 0) {
                break;
            }
        }
        return front;
    }

    /**
     * 获取季度数
     * @param currentDate
     * @return
     */
    public static String getQuarter(Date currentDate) {
        String quarter = "";
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        int month = c.get(Calendar.MONTH) + 1;
        System.out.println(month);
        if (month >= 1 && month <= 3) {
            quarter = "1";
        }
        if (month >= 4 && month <= 6) {
            quarter = "2";
        }
        if (month >= 7 && month <= 9) {
            quarter = "3";
        }
        if (month >= 10 && month <= 12) {
            quarter = "4";
        }
        return quarter;
    }

    /* 基于当前日期得到下周几
     * @param dayOfWeek 下周几
     * @return Date
     */
    public static Date getNextDayOfWeek(Date date, DayOfWeek dayOfWeek) {
        if (null == dayOfWeek) {
            return null;
        }
        Date thisDate = null == date ? getNow() : date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(thisDate);
        // 国外周日算第一天
        int thisDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 == 0 ? 7 : calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int thisWeekday = dayOfWeek.getValue() - thisDayOfWeek;
        calendar.add(GregorianCalendar.DATE, thisWeekday + 7);
        return calendar.getTime();
    }

    /**
     * java8中Date转LocalDateTime
     * @param date
     * @return
     * @author v_xuxuesong
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zi = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zi);
    }

    /**
     * java8中Date转LocalDate
     * @param date
     * @return
     * @author v_xuxuesong
     */
    public static LocalDate date2LocalDate(Date date) {
        LocalDateTime localDateTime = date2LocalDateTime(date);
        return localDateTime.toLocalDate();
    }

    /**
     * java8中Date转LocalTime
     * @param date
     * @return
     * @author v_xuxuesong
     */
    public static LocalTime date2LocalTime(Date date) {
        LocalDateTime localDateTime = date2LocalDateTime(date);
        return localDateTime.toLocalTime();
    }

    /**
     * java8中LocalDateTime转Date
     * @param localDateTime
     * @return
     * @author v_xuxuesong
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * java8中LocalDate转Date
     * @param localDate
     * @return
     * @author v_xuxuesong
     */
    public static Date localDate2Date(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * java8中LocalDate和LocalTime转Date
     * @param localDate
     * @param localTime
     * @return
     * @author v_xuxuesong
     */
    public static Date localDateAndLocalTime2Date(LocalDate localDate, LocalTime localTime) {
        if (localDate == null || localTime == null) {
            return null;
        }
        return Date.from(LocalDateTime.of(localDate, localTime).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @param args
     * @throws ParseException
     */
//    public static void main(String[] args) throws ParseException {
//        System.out.println(getNextDayOfWeek(new Date(), DayOfWeek.MONDAY));
//    }

}
