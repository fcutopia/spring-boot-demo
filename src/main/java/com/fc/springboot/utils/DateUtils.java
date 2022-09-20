package com.fc.springboot.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 系统日期 时间工具类
 * @author teli_
 *
 */
public class DateUtils {
	
	public static DateTimeFormatter pattern_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
	public static DateTimeFormatter pattern_yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
	public static DateTimeFormatter pattern_yyMMdd = DateTimeFormatter.ofPattern("yyMMdd");
	public static DateTimeFormatter pattern_yyyy_MM_DD_HH_mm_ss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static DateTimeFormatter pattern_yyyy__MM__dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static DateTimeFormatter pattern_yyyyMMDDHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	public static DateTimeFormatter pattern_yyyy_MM = DateTimeFormatter.ofPattern("yyyy-MM");
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String HH_mm_ss = "HH:mm:ss";
	public static DateTimeFormatter pattern_yyyy_MM_DD = DateTimeFormatter.ofPattern(YYYY_MM_DD);

	public static final SimpleDateFormat SDF_YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static Date getNow() {
		return new Date();
	}

	/**
	 * 
	 * @description 根据指定long 返回date
	 * @params 时间戳
	 * @return Date
	 * @author 麻木神
	 * @time 2019年12月3日 下午4:56:36
	 */
	public static Date getDate(long l) {
		return new Date(l);
	}

	/**
	 * 获取年月日格式
	 */
	public static String getDateYYYY_MM_DD() {
		LocalDate date = LocalDate.now();
		return date.format(pattern_yyyyMMdd);
	}

	/**
	 * 获取年月日格式
	 */
	public static String getDateYYMMDD() {
		LocalDate date = LocalDate.now();
		return date.format(pattern_yyMMdd);
	}

	/**
	 * 获取年月日格式
	 */
	public static String getDateYY_MM_DD() {
		LocalDate date = LocalDate.now();
		return date.format(pattern_yyyy__MM__dd);
	}

	/**
	 * 时间转换成字符串，年-月-日 时:分:秒格式
	 * 
	 * @param date
	 * @return
	 */
	public static String getDate2Str(Date date) {
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		return localDateTime.format(pattern_yyyy_MM_DD_HH_mm_ss);
	}

	/**
	 * 时间转换成指定格式的字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getDate2Str(Date date, DateTimeFormatter formatter) {
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		return localDateTime.format(formatter);
	}

	/**
	 * localDateTime转date
	 * 
	 * @param localDateTime
	 * @return
	 */
	public static Date localDateTime2Date(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		Date date = Date.from(zdt.toInstant());
		return date;
	}

	/**
	 * 将字符串的全日期时间格式 转为date类型
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date getStr2Date(String strDate) {
		LocalDateTime localDateTime = LocalDateTime.parse(strDate, pattern_yyyy_MM_DD_HH_mm_ss);

		return localDateTime2Date(localDateTime);
	}

	/**
	 * 获得当前时间的 字符串格式yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String getDate2StrYYYYMMDDHHMMSS() {
		LocalDateTime date = LocalDateTime.now();
		return date.format(pattern_yyyyMMDDHHmmss);
	}

	/**
	 * 当前时间 加上30分钟 返回
	 * 
	 * @return
	 */
	public static String getDate2StrYYYYMMDDHHMMSSAdd30M() {
		LocalDateTime date = LocalDateTime.now();
		LocalDateTime dateNew = date.plus(30l, ChronoUnit.MINUTES);

		return dateNew.format(pattern_yyyyMMDDHHmmss);
	}

	/**
	 * 将java日期格式 转换为定时任务执行的cronExpression
	 * 
	 * @param date
	 * @return
	 */
	public static String getCron(Date date) {
		String dateFormat = "ss mm HH dd MM ? yyyy";
		return formatDateByPattern(date, dateFormat);
	}

	/**
	 * 转换方法
	 * 
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String formatDateByPattern(Date date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String formatTimeStr = null;
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}

	/**
	 * String时间格式：yyyy-MM-dd HH:mm:ss 转long型
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public static long StringToLong(String str) throws Exception {
		Date date = SDF_YYYY_MM_DD_HH_MM_SS.parse(str);
		// 转换为Date类
		long timestampStr = date.getTime();
		return timestampStr;
	}

	
	/**
	 * 
	 * @description
	 *  	根据两个时间，返回时间差的字符串表示
	 * @params
	 *      endDate 结束时间
	 *      nowDate 当前时间
	 * @return String
	 * @author 麻木神
	 * @time 2019年12月3日 下午4:58:05
	 */
	public static String getDateDiff(Date endDate, Date nowDate) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天" + hour + "小时" + min + "分钟";
	}
	
	/**
	 * 
	 * @description
	 *  	java任意对象转换成Date对象
	 * @params
	 * @return Date
	 * @author guyp
	 * @time 2019年12月17日 下午3:37:51
	 */
    public static Date parseDateObj(Object o) {
        if(o == null) {
            return null;
        }
        if(o instanceof Date) {
            return (Date) o;
        }
        if(o instanceof java.sql.Date) {
            return (Date) o;
        }
        if(o instanceof String) {
            // yyyy-MM-dd HH:mm:ss  /
            String d = (String) o;
            StringBuilder format = new StringBuilder("yyyy");
            if(d.charAt(4) == '-') {
                format.append("-MM-dd");
            }else if(d.charAt(4) == '/') {
                format.append("/MM/dd");
            }else if(d.charAt(4) == '_'){
                format.append("_MM_dd");
            }else {
                format.append("MMdd");
            }
            if(d.length() < format.length()) {
                return null;
            }else if(d.length() == format.length()) {
                return parseDate(d, format.toString());
            }
            if(d.charAt(format.length()) == ' ') {
                format.append(' ');
            }
            if(d.charAt(format.length() + 2) == ':') {
                format.append("HH:mm:ss");
            }else if(d.charAt(format.length() + 2) == '/') {
                format.append("HH/mm/ss");
            }else {
                format.append("HHmmss");
            }
            if(d.length() < format.length()) {
                return null;
            }
            if(d.length() == format.length()) {
                return parseDate(d, format.toString());
            }
            if(d.charAt(format.length()) == '.' && d.length() == (format.length() + 4)) {
                format.append(".SSS");
            }else if(d.length() == (format.length() + 3)){
                format.append("SSS");
            }else {
                d = d.substring(0, format.length());
            }
            return parseDate(d, format.toString());
        }
        if (o instanceof Long) {
            long l = (long) o;
            if (l < 10000000000l) {
                return new Date(l*1000); 
            }
            return new Date(l);
        }
        if (o instanceof Integer) {
            long l = (int) o * 1000;
            return new Date(l); 
        }
        return null;
    }
    
    /**
     * 
     * @description
     *  	解析字符串日期,不报错  异常返回null
     * @params
     * @return Date
     * @author guyp
     * @time 2019年12月17日 下午3:37:32
     */
    public static Date parseDate(String d, String format) {
        try {
            return new SimpleDateFormat(format).parse(d);
         } catch (ParseException e) {
            return null;
         }
    }

	/**
	 * 日期转换为字符串
	 *
	 * @param date
	 *            日期
	 * @return 字符串
	 */
	public static String date2Str(Date date, SimpleDateFormat date_sdf) {
		if (null == date) {
			return null;
		}
		return date_sdf.format(date);
	}


	public static String date2Str(Date date, String dateFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		return simpleDateFormat.format(date);
	}

	/**
	 * 获取前一日的日期
	 * @param dateStr "2020-01-01"
	 * @return "2019-12-31"
	 */
	public static String getPrevDayStr(String dateStr) {

		return getNDayStr(dateStr, -1);
	}

	/**
	 * 获取当前日期的前后几天
	 * @param days 为正：当前日期的后days天； 为负:当前日期的前days天
	 * @param dateStr 日期字符串 "2010-10-10"
	 */
	public static String getNDayStr(String dateStr, Integer days) {
		String format = "yyyy-MM-dd";
		Date date = parseDate(dateStr, format);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		date = calendar.getTime();

		return formatDateByPattern(date, format);
	}


	/**
	 * 返回两时间字符串的时间差
	 * @param signBeginTime "09:00:25"
	 * @param signEndTime  "18:30:23"
	 * @return  (毫秒数)
	 */
	public static long diffBetween2HHmmssStr(String signBeginTime, String signEndTime) {

		String format = "HH:mm:ss";
		Date date1 = parseDate(signBeginTime, format);
		Date date2 = parseDate(signEndTime, format);
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		return time2 - time1;

	}




	/**
	 * 将毫秒数转为小时数
	 * @param millsec
	 * @return
	 */
	public static float millsecons2Hours(long millsec) {
		return millsec / 1000.0f / 60 / 60;
	}

	/**
	 * 返回两时间字符串时间之内的所有时间字符串
	 *  notice：返回结果是闭区间， 包含minDate和maxDate
	 * @param minDate 2021-01-01
	 * @param maxDate 2021-01-03
	 * @return List<String>  [2021-01-01, 2021-01-02, 2021-01-03]
	 */
	public static List<String> dateStrBetween(String minDate, String maxDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<String> betweenList = new ArrayList<String>();

		try{
			Calendar startDay = Calendar.getInstance();
			startDay.setTime(format.parse(minDate));
			startDay.add(Calendar.DATE, -1);

			while(true){
				startDay.add(Calendar.DATE, 1);
				Date newDate = startDay.getTime();
				String newend=format.format(newDate);
				betweenList.add(newend);
				if(maxDate.equals(newend)){
					break;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

		return betweenList;
	}

	public static Date maxDate(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return null;
		}
		return date1.compareTo(date2) < 0 ? date2 : date1;

	}

	public static Date minDate(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return null;
		}
		return date1.compareTo(date2) < 0 ? date1 : date2;

	}

	public static void main(String[] args) {
		String signBeginTime = "01:43:29";
		String signEndTime = "20:27:34";
		System.out.println(plusMins(signBeginTime, 1));
		System.out.println(DateUtils.millsecons2Hours(DateUtils.diffBetween2HHmmssStr(signBeginTime, signEndTime)));

	}

	/**
	 *
	 * @param startTime HH:mm:ss
	 * @param mins
	 * @return
	 */
	public static String plusMins(String startTime, Integer mins) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(startTime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, mins);
			date = calendar.getTime();
			return sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
