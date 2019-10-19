package com.shangmei.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date Utility Class used to convert Strings to Dates and Timestamps
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by
 *         <a href="mailto:dan@getrolling.com">Dan Kibler </a> to correct time
 *         pattern. Minutes should be mm not MM (MM is month).
 */
public final class DateUtil {
	private static final String FORMAT_YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	private static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	private static Log log = LogFactory.getLog(DateUtil.class);
	private static final String TIME_PATTERN = "HH:mm";
	private static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	private static final String FORMATYYYYMMDD = "yyyyMMdd";
	/**
	 * Checkstyle rule: utility classes should not have public constructor
	 */
	private DateUtil() {
	}

	/***
	 * SQL date 转换为util.Date
	 * @param udate
	 * @return
     */
	public static Date converSqlToUtil(java.sql.Date udate) {
		if(udate==null){
			return null;
		}
		return new Date(udate.getTime());
	}

	/**
	 * 取得指定日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 取得指定日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	/**
	 * Return default datePattern (yyyy-MM-dd)
	 * 
	 * @return a string representing the date pattern on the UI
	 */
	public static String getDatePattern() {
		String defaultDatePattern = FORMAT_YYYY_MM_DD;
		return defaultDatePattern;
	}
	
	/**
	 * 包含时分秒的格式
	 * 
	 * @return
	 */
	public static String getDefaultDatePattern() {
		String defaultDatePattern = FORMAT_YYYY_MM_DD_HH_MM_SS;
		return defaultDatePattern;
	}
	
	/***
	 * Return getDatePatternYMDHM (yyyy-MM-dd HH:MM)
	 * 
	 * @return
	 */
	public static String getDatePatternYMDHM() {
		String defaultDatePattern = FORMAT_YYYY_MM_DD_HH_MM;
		return defaultDatePattern;
	}

	public static String getDateTimePattern() {
		return DateUtil.getDatePattern() + " HH:mm:ss.S";
	}

	/**
	 * Clear time, only keep yyyy-MM-dd 清除时分秒，只保存年月日
	 * 
	 * @return
	 */
	public static Date getCleanDate(Date date) {
		Date cleanDate = null;
		try {
			String strDate = DateUtil.getDate(date);
			cleanDate = DateUtil.convertStringToDate(strDate);
		} catch (ParseException e) {
			log.error(e);
		}
		return cleanDate;
	}

	/**
	 * This method attempts to convert date to format yyyy-MM-dd.
	 * 
	 * @param aDate
	 *            date from database as a string
	 * @return formatted string for the ui
	 */
	public static String getDate(Date aDate) {
		SimpleDateFormat df;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date/time in the
	 * format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param strDate
	 *            a string representation of a date
	 * @return a converted Date object
	 * @throws ParseException
	 *             when String doesn't match the expected format
	 * @see SimpleDateFormat
	 */
	public static Date convertStringToDate(String aMask, String strDate) throws ParseException {
		if (strDate == null) {
			return null;
		}
		SimpleDateFormat df;
		Date date;
		df = new SimpleDateFormat(aMask);

		// if (log.isDebugEnabled()) {
		// log.debug("converting '" + strDate + "' to date with mask '" + aMask
		// + "'");
		// }

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			// log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	/**
	 * This method returns the current date time in the format: MM/dd/yyyy HH:MM
	 * a
	 * 
	 * @param theTime
	 *            the current time
	 * @return the current date/time
	 */
	public static String getTimeNow(Date theTime) {
		return getDateTime(TIME_PATTERN, theTime);
	}

	/**
	 * This method returns the current date with clean time: yyyy-MM-dd
	 * 
	 * @return the current date
	 * @throws ParseException
	 *             when String doesn't match the expected format
	 */
	public static Calendar getToday() {
		Date today = currentDateTime();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

		// This seems like quite a hack (date -> string -> date),
		// but it works ;-)
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		try {
			cal.setTime(convertStringToDate(todayAsString));
		} catch (ParseException e) {
			throw new RuntimeException("unexpcepted exception, should not happen", e);
		}

		return cal;
	}

	/** 返回今天的日期，带时分秒 */
	public static Date currentDateTime() {
		return new Date();
	}

	/** 返回今天的日期，不带时分秒 */
	public static Date currentDate() {
		try {
			return convertStringToDate(convertDateToString(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 返回不带时分秒的日期 */
	public static Date cleanTimeDate(Date date) {
		try {
			return convertStringToDate(convertDateToString(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 在当前日期上加多少天 */
	public static Date addDateDays(Date date, int days) {
		if (date == null)
			return null;
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DAY_OF_YEAR, days);
		return ca.getTime();
	}
	
	/** 在当前日期上加多少天 */
	public static Date addDateDays(String date, int days) {
		if (date == null)
			return null;
		Calendar ca = Calendar.getInstance();
		ca.setTime(DateUtil.convert(date));
		ca.add(Calendar.DAY_OF_YEAR, days);
		return ca.getTime();
	}
	
	/** 在当前日期上加多少月 */
	public static Date addDateMonths(Date date, int months) {
		if (date == null)
			return null;
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.MONTH, months);
		return ca.getTime();
	}
	
	public static boolean isBetween(Date compareDate, Date startDate, Date endDate) {
		return (compareDate.compareTo(startDate) >= 0 && compareDate.compareTo(endDate) <= 0);
	}
	
	/**
	 * 取得下一天的凌晨0:10时间
	 * 
	 * @return
	 */
	public static Date getNextDay() {
		Calendar cal = Calendar.getInstance();
		Calendar calnextday = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1);
		calnextday.add(Calendar.MINUTE, 10);
		return calnextday.getTime();
	}
	
	/**
	 * 取得下一天的时间字符串
	 * 
	 * @return
	 */
	public static String getNextDay(String date) {

		SimpleDateFormat sdf = new SimpleDateFormat(getDatePattern());
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(date));
		} catch (ParseException e) {
			log.error("Exception:", e);
		}
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return sdf.format(cal.getTime());
	}
	
	public static Date toDate(String dateStr) {
		String _format = null;
		switch (dateStr.length()) {
		case 6:
			_format = "yyMMdd";
			break;
		case 7:
			_format = "yyyy-MM";
			break;
		case 8:
			_format = "yyyyMMdd";
			break;
		case 10:
			_format = dateStr.indexOf("-") > 0 ? "yyyy-MM-dd" : "yyyyMMddHH";
			break;
		case 12:
			_format = "yyyyMMddHHmm";
			break;
		case 14:
			_format = "yyyyMMddHHmmss";
			break;
		case 17:
			_format = "yyyyMMddHHmmssSSS";
			break;
		case 19:
			_format = "yyyy-MM-dd HH:mm:ss";
			break;
		}
		if (null == _format) {
			return null;
		}
		return DateUtil.convert(dateStr, _format);
	}
	
	/**
	 * 字符串转化成日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date convert(String date) {
		Date retValue = null;
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YYYY_MM_DD);
		try {
			retValue = sdf.parse(date);
		} catch (ParseException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
		return retValue;
	}

	public static Date convert(String date, String format) {
		Date retValue = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			retValue = sdf.parse(date);
		} catch (ParseException e) {
			log.error("字符串转日期失败", e);
			throw new RuntimeException(e);
		}
		return retValue;
	}


	public static Date getNextDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	
	/**
	 * 按日加
	 * 
	 * @param value
	 * @return
	 */
	public static Date addDay(int value) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, value);
		return now.getTime();
	}

	/**
	 * 按日加,指定日期
	 * 
	 * @param date
	 * @param value
	 * @return
	 */
	public static Date addDay(Date date, int value) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DAY_OF_YEAR, value);
		return now.getTime();
	}
	
	/**
	 * 昨天
	 * 
	 * @return
	 */
	public static Date yesterday() {
		return addDay(-1);
	}

	/**
	 * 明天
	 * 
	 * @return
	 */
	public static Date tomorrow() {
		return addDay(1);
	}
	

	/** 时间的加减 */
	public static Date addOneday(int day) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = new Date();// 取时间
		Date date = DateUtil.currentDate();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		return date;
	}
	
	/** 年的加减 */
	public static Date addOneYear(int year) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = new Date();// 取时间
		Date date = DateUtil.currentDate();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, year);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		return date;
	}

	/**返回1-7，1代表周日，2代表周一，以此类推，7代表周六
	 * @param date
	 * @return
	 */
	public static int getDayofweek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	/**返回0-6，0代表周日，1代表周一，以此类推，6代表周六
	 * @param date
	 * @return
	 */
	public static int getIndexofweek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}
	
	
	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String WeekOfDate(Date dt, boolean isS) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		if (isS)
			return weekDays[w];
		return w + "";
	}
	
	/**
	 * 返回 yyyy-MM-dd HH:mm 格式的日期
	 * @param dd
	 * @return
	 */
	public static Date getDateByStr(String dd) {
		if(dd == null || "".equals(dd)) return null;
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date;
		try {
			date = sd.parse(dd);
		} catch (ParseException e) {
			date = null;
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 返回 yyyy-MM-dd 格式的日期
	 * @param dd
	 * @return
	 */
	public static Date getDateByStr2(String dd) {
		if(dd == null || "".equals(dd)) return null;
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = sd.parse(dd);
		} catch (ParseException e) {
			date = null;
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * This method generates a string representation of a date's date/time in
	 * the format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param aDate
	 *            a date object
	 * @return a formatted string representation of the date
	 * @see SimpleDateFormat
	 */
	public static String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
			log.warn("aDate is null!");
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	public static Date convertDateToDate(String aMask, Date date) {
		if (date == null) {
			log.warn("date is null!");
			return null;
		}
		String createTime = DateUtil.getDateTime(aMask, date);
		try {
			return convertStringToDate(aMask, createTime);
		} catch (ParseException e) {
			log.warn(e);
			return null;
		}

	}

	/**
	 * yyyy-MM-dd
	 * This method generates a string representation of a date based on the
	 * System Property 'dateFormat' in the format you specify on input
	 * 
	 * @param aDate
	 *            A date to convert
	 * @return a string representation of the date
	 */
	public static String convertDateToString(Date aDate) {
		return getDateTime(getDatePattern(), aDate);
	}

	
	public static String convertDateToStr(Date aDate) {
		return getDateTime(getDateTimePattern(), aDate);
	}
	/**
	 * This method converts a String to a date using the datePattern
	 * 
	 * @param strDate
	 *            the date to convert (in format MM/dd/yyyy)
	 * @return a date object
	 * @throws ParseException
	 *             when String doesn't match the expected format
	 */
	public static Date convertStringToDate(final String strDate) throws ParseException {
		return convertStringToDate(getDatePattern(), strDate);
	}
	
	public static Date converStrToDate(String date){
		Date d = null;
		try {
			if(date == null || "".equals(date.trim()))
				return null;
			SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy KK:mm:ss:SSSaa",Locale.ENGLISH);
			d = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * 取一天的开始
	 * 
	 * @param aMask
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date getDaysBegin(String aMask, String strDate) {
		Date date = null;
		try {
			date = convertStringToDate(aMask, strDate);
			date = daysBegin(date);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		return date;
	}

	public static Date daysBegin(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		date = cal.getTime();
		return date;
	}

	/**
	 * 取一天的结束
	 * 
	 * @param aMask
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date getDaysEnd(String aMask, String strDate) {
		Date date = null;
		try {
			date = convertStringToDate(aMask, strDate);
			date = daysEnd(date);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		return date;

	}

	public static Date daysEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		date = cal.getTime();
		return date;
	}

	/** 获得每个月的第一天是星期几 */
	public static int getWeek(final int y, final int m) {
		Calendar cal = Calendar.getInstance();// 获得当前日期时间的方法
		cal.set(Calendar.YEAR, y); // 设置改为你输入的年
		cal.set(Calendar.MONTH, m - 1);
		cal.set(Calendar.DATE, 1);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;// 获得每个月第一天是星期几
		return (w);// 返回星期几
	}

	/** 获得每个月天数 */
	public static int getDay(final int y, final int m) {
		int day;
		if ((y % 100 == 0) || (y % 4 == 0 && y % 100 != 0)) {
			int[] days = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
			day = days[m - 1];
		} else {
			int[] days1 = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
			day = days1[m - 1];
		}
		return day;
	}

	/**
	 * 获取一段时间内的天
	 * 
	 * @param ksrq
	 *            格式：2012-05-01
	 * @param jsrq
	 *            格式：2012-06-01
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static List<String> getDays(String ksrq, String jsrq) {
		List<String> list = new ArrayList<String>();
		try {
			Date d_ksrq = convertStringToDate(FORMAT_YYYY_MM_DD, ksrq);
			Date d_jsrq = convertStringToDate(FORMAT_YYYY_MM_DD, jsrq);
			list.add(getDateTime(FORMAT_YYYY_MM_DD, d_ksrq));
			while (d_jsrq.after(d_ksrq)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(d_ksrq);
				cal.add(cal.DATE, 1);
				d_ksrq = cal.getTime();
				list.add(getDateTime(FORMAT_YYYY_MM_DD, d_ksrq));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取一段时间内的天
	 * 
	 * @param ksrq
	 *            格式：2012-05-01
	 * @param jsrq
	 *            格式：2012-06-01
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static List<String> getDays(Date d_ksrq, Date d_jsrq) {
		List<String> list = new ArrayList<String>();
		try {
			list.add(getDateTime(FORMAT_YYYY_MM_DD, d_ksrq));
			while (d_jsrq.after(d_ksrq)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(d_ksrq);
				cal.add(cal.DATE, 1);
				d_ksrq = cal.getTime();
				list.add(getDateTime(FORMAT_YYYY_MM_DD, d_ksrq));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取一段时间内的天
	 * 
	 * @param ksrq
	 *            格式：2012-05-01
	 * @param jsrq
	 *            格式：2012-06-01
	 * @return List<Date>
	 */
	@SuppressWarnings("static-access")
	public static List<Date> getDayList(Date d_ksrq, Date d_jsrq) {
		List<Date> list = new ArrayList<Date>();
		try {
			list.add(d_ksrq);
			while (d_jsrq.after(d_ksrq)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(d_ksrq);
				cal.add(cal.DATE, 1);
				d_ksrq = cal.getTime();
				list.add(d_ksrq);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 根据一段日期获取其中所有星期几的日期,星期以1~7表示 */
	public static List<String> getWeekDay(String dateFromStr, String dateToStr, int dayOfWeek) throws Exception {
		Date sd = DateUtil.convertStringToDate(FORMAT_YYYY_MM_DD, dateFromStr);
		Date ed = DateUtil.convertStringToDate(FORMAT_YYYY_MM_DD, dateToStr);
		if (dayOfWeek == 7) {
			dayOfWeek = 0;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(sd);
		int day = c.get(Calendar.DAY_OF_WEEK) - 1;
		List<String> tmp = new ArrayList<String>();
		if (day != dayOfWeek) {
			int dif = dayOfWeek < day ? (dayOfWeek - day + 7) : (dayOfWeek - day);
			c.add(Calendar.DATE, dif);
		}
		while (!c.getTime().after(ed)) {
			tmp.add(DateUtil.getDateTime(FORMAT_YYYY_MM_DD, c.getTime()));
			c.add(Calendar.DATE, 7);
		}
		return tmp;
	}

	public static List<String> getWeekDay(String dateFromStr, String dateToStr, int[] dayOfWeeks) throws ParseException {
		Date sd = DateUtil.convertStringToDate(FORMAT_YYYY_MM_DD, dateFromStr);
		Date ed = DateUtil.convertStringToDate(FORMAT_YYYY_MM_DD, dateToStr);
		Set<Integer> daySet = new HashSet<Integer>();
		for (int i = 0; i < dayOfWeeks.length; i++) {
			if (dayOfWeeks[i] == 7) {
				daySet.add(0);
			} else if (dayOfWeeks[i] < 0 || dayOfWeeks[i] > 6) {
				continue;
			} else {
				daySet.add(dayOfWeeks[i]);
			}
		}
		Calendar start = Calendar.getInstance();
		start.setTime(sd);

		List<String> tmp = new ArrayList<String>();
		while (!start.getTime().after(ed)) {
			if (daySet.contains(start.get(Calendar.DAY_OF_WEEK) - 1)) {
				tmp.add(DateUtil.getDateTime(FORMAT_YYYY_MM_DD, start.getTime()));
			}
			start.add(Calendar.DATE, 1);
		}
		return tmp;
	}

	public static List<String> getWeekDay(Date sd, Date ed, int[] dayOfWeeks) throws ParseException {
		Set<Integer> daySet = new HashSet<Integer>();
		for (int i = 0; i < dayOfWeeks.length; i++) {
			if (dayOfWeeks[i] == 7) {
				daySet.add(0);
			} else if (dayOfWeeks[i] < 0 || dayOfWeeks[i] > 6) {
				continue;
			} else {
				daySet.add(dayOfWeeks[i]);
			}
		}
		Calendar start = Calendar.getInstance();
		start.setTime(sd);

		List<String> tmp = new ArrayList<String>();
		while (!start.getTime().after(ed)) {
			if (daySet.contains(start.get(Calendar.DAY_OF_WEEK) - 1)) {
				tmp.add(DateUtil.getDateTime(FORMAT_YYYY_MM_DD, start.getTime()));
			}
			start.add(Calendar.DATE, 1);
		}
		return tmp;
	}

	public static List<String> getMonthWeekDay(String month, int[] dayOfWeeks) throws ParseException {
		Date startDate = DateUtil.convertStringToDate(month);
		Calendar first = Calendar.getInstance();
		first.setTime(startDate);
		first.set(Calendar.DAY_OF_MONTH, 1);

		int maxDays = first.getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar end = Calendar.getInstance();
		end.setTime(startDate);
		end.set(Calendar.DAY_OF_MONTH, maxDays);
		return getWeekDay(DateUtil.getDate(first.getTime()), DateUtil.getDate(end.getTime()), dayOfWeeks);
	}

	public static List<String> getMonthWeekDay(String[] months, int[] dayOfWeeks) throws ParseException {
		List<String> result = new ArrayList<String>();
		for (String month : months) {
			if (StringUtils.hasText(month)) {
				result.addAll(getMonthWeekDay(month, dayOfWeeks));
			}
		}
		return result;
	}

	/**
	 * 计算两个时间之间的分钟数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Integer dateTimeDiff(Date startTime, Date endTime){
		try{
			Float nd = new Float(1000 * 60);//一分钟的毫秒数
			Float diff = new Float(endTime.getTime() - startTime.getTime());
			Float day = diff / nd;// 计算差多少分钟
			return day.intValue();
		}catch(Exception e){
			return 0;
		}
	}
	
	/**
	 * 计算两个时间之间的秒数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Integer dateSSDiff(Date startTime, Date endTime){
		try{
			Float nd = new Float(1000);//一秒钟的毫秒数
			Float diff = new Float(endTime.getTime() - startTime.getTime());
			Float day = diff / nd;// 计算差多少秒
			return day.intValue();
		}catch(Exception e){
			return 0;
		}
	}

	/***
	 * 判断某个日期是否在一个范围之内(不包含结尾的日期)
	 * 
	 * @param sourceBeginDate
	 * @param sourceEndDate
	 * @param date
	 * @return
	 */
	public static boolean dateBetweenTwoDates(Date sourceBeginDate, Date sourceEndDate,String date){
		try{
			Date tempDate=sourceEndDate;
			int num=DateUtil.dateDiff(sourceBeginDate, sourceEndDate);
			if(num<0){
				sourceEndDate=sourceBeginDate;
				sourceBeginDate=tempDate;
			}
			Date tDate=DateUtil.convertStringToDate(getDatePattern(), date);
			int diff1=DateUtil.dateDiff(tDate, sourceBeginDate);
			int diff2=DateUtil.dateDiff(tDate, sourceEndDate);
			if(diff1<=0 && diff2 > 0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}

	public static int daysBetween(Date smallDate, Date bigDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smallDate = sdf.parse(sdf.format(smallDate));
		bigDate = sdf.parse(sdf.format(bigDate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smallDate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bigDate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
	
	/***
	 *  字符串日期比较
	 *  传递日期格式为YYYY-MM-DD
	 * 
	 * @param smallDate
	 * @param bigDate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(String smallDate, String bigDate) throws ParseException {
		Date beginDate=DateUtil.convert(smallDate);
		Date endDate=DateUtil.convert(bigDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(endDate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 计算两个日期之间的天数
	 * 
	 * @param startTime
	 * @param endTime
	 * @param format
	 * @return
	 */
	public static Integer dateDiff(Date startTime, Date endTime) {
		try {
			Float nd = new Float(1000 * 24 * 60 * 60);// 一天的毫秒数
			Float diff = new Float(cleanTimeDate(endTime).getTime() - cleanTimeDate(startTime).getTime());
			Float day = diff / nd;// 计算差多少天

			return day.intValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	/**
	 * 两个日期之间相差多少天(该方法获取两个日期差有误,如果时间含有时间秒并且时间格式也有时分秒,所以在方法内不使用format参数)
	 * 
	 * @param startTime
	 * @param endTime
	 * @param format
	 * @return
	 */
	public static Long dateDiff(String startTime, String endTime, String format) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(FORMAT_YYYY_MM_DD);
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long diff;
		try {
			// 获得两个时间的毫秒时间差异
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			long day = diff / nd;// 计算差多少天
			return day;
		} catch (ParseException e) {
			log.error(e);
		}
		return null;
	}
	
	/***
	 * 两个日期之间相差多少天
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Long dateDiff(String startTime, String endTime) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(FORMAT_YYYY_MM_DD);
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long diff;
		try {
			// 获得两个时间的毫秒时间差异
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			long day = diff / nd;// 计算差多少天
			return day;
		} catch (ParseException e) {
			log.error(e);
		}
		return null;
	}

	public static void main(String[] args) throws ParseException {
		//System.out.println(dateDiff("2016-06-12", "2016-09-10", getDatePattern()));
		//System.out.println(convertDateToString(addOneday(90)));
		//String strDate="Tue Mar 07 00:00:00 CST 2017";
		//System.out.println((new SimpleDateFormat("yyyy-MM-dd")).format((new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)).parse(strDate)));
		System.out.println(getYear(convertStringToDate("2015-12-12 12:00:00")));
		
	}

	/***
	 * 获取日期对应的年份
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
        Calendar first = Calendar.getInstance();
        first.setTime(date);
        return first.get(Calendar.YEAR);
    }
	
	public static Date[] getCalendar(Date date) {
		Calendar first = Calendar.getInstance();
		first.setTime(date);
		first.set(Calendar.DAY_OF_MONTH, 1);
		first.add(Calendar.DATE, -(first.get(Calendar.DAY_OF_WEEK) - 1));

		Calendar last = Calendar.getInstance();
		last.setTime(date);
		last.set(Calendar.DAY_OF_MONTH, getDay(last.get(Calendar.YEAR), last.get(Calendar.MONTH) + 1));
		last.add(Calendar.DATE, (7 - last.get(Calendar.DAY_OF_WEEK)));
		Date[] dates = new Date[] { first.getTime(), last.getTime() };
		return dates;
	}

	public static Date[] get42DayCalendar(Date date) {
		Calendar first = Calendar.getInstance();
		first.setTime(date);
		first.set(Calendar.DAY_OF_MONTH, 1);
		first.add(Calendar.DATE, -(first.get(Calendar.DAY_OF_WEEK) - 1));

		Calendar last = Calendar.getInstance();
		last.setTime(first.getTime());
		last.add(Calendar.DATE, 41);
		Date[] dates = new Date[] { first.getTime(), last.getTime() };
		return dates;
	}

	public static int getDayOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_YEAR);
	}

	public static Date nextDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, +1);
		return c.getTime();
	}

	public static Date previousMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		return c.getTime();
	}

	public static Date nextMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, +1);
		return c.getTime();
	}

	public static Date lastMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		return c.getTime();
	}

	public static Date cloneDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getTime();
	}

	public static String convertDateTimeToString(Date date) {
		String pattern = FORMAT_YYYY_MM_DD + " HH:mm:ss";
		return getDateTime(pattern, date);
	}
	
	public static String convertDateTimeToYYYYMMDD(Date date) {
		String pattern = FORMAT_YYYY_MM_DD;
		return getDateTime(pattern, date);
	}

	/**
	 * 生成从开始日期到结束日期内的日期列表,如果星期标识不为空的时候，只包含flag为true的日期
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Date> createDateList(String startDateStr, String endDateStr, Map weekFlag) {
		List<Date> dateList = new ArrayList<Date>();
		try {
			Date startDate = convertStringToDate(FORMAT_YYYY_MM_DD_T_HH_MM_SS, startDateStr);
			Date endDate = convertStringToDate(FORMAT_YYYY_MM_DD_T_HH_MM_SS, endDateStr);

			int days = dateDiff(startDate, endDate);

			final String weekNames[] = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

			for (int i = 0; i <= days; i++) {

				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				c.add(Calendar.DATE, i);
				Date varDate = c.getTime();

				if (weekFlag != null) {
					String weekName = weekNames[c.get(Calendar.DAY_OF_WEEK) - 1];

					boolean include = (Boolean) weekFlag.get(weekName);
					if (include) {
						dateList.add(varDate);
					}

				} else {
					dateList.add(varDate);
				}

			}

		} catch (ParseException e) {

			e.printStackTrace();
		}

		return dateList;
	}

	/**
	 * 获取当前年月+11个月
	 * 
	 * */
	public static List<Date> getOneYearDate() {
		List<Date> list = new ArrayList<Date>();
		Calendar c = Calendar.getInstance();
		list.add(c.getTime());
		for (int i = 0; i < 11; i++) {
			c.add(Calendar.MONTH, +1);
			list.add(c.getTime());
		}
		return list;
	}

	/**
	 * 取一年后的日期
	 * 
	 * @return
	 */
	public static Date getAfterYearTime() {
		Date date = currentDateTime();
		long afterTime = (date.getTime() / 1000) + 60 * 60 * 24 * 365;
		date.setTime(afterTime * 1000);
		return date;
	}

	public static List<Date[]> splitTimeByDays(Date start, Date end, int days) {
		return splitTimeByHours(start, end, 24 * days);
	}

	public static List<Date[]> splitTimeByHours(Date start, Date end, int hours) {
		List<Date[]> dl = new ArrayList<Date[]>();
		while (start.compareTo(end) < 0) {
			Date _end = addHours(start, hours);
			if (_end.compareTo(end) > 0) {
				_end = end;
			}
			Date[] dates = new Date[] { (Date) start.clone(), (Date) _end.clone() };
			dl.add(dates);

			start = _end;
		}
		return dl;
	}

	public static Date addMinutes(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, amount);
		return c.getTime();
	}

	/**
	 * 
	 * @param date
	 * @param hhmm
	 * @return
	 */
	public static Date setDateHHmmss(Date date, int hour, int minute, int second) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		return c.getTime();
	}

	public static Date addHours(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, amount);
		return c.getTime();
	}

	public static Date addDays(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, amount);
		return c.getTime();
	}

	public static Date addMonths(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, amount);
		return c.getTime();
	}

	/**
	 * 获取当前系统时间所在月的最后一天
	 * 
	 * @return
	 */
	public static String getCurrentMonthLastDay(Date start) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		int endday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DATE, endday);
		return convertDateToString(calendar.getTime());
	}

	/**
	 * 根据年和月获取所在月的最后一天的日期
	 * 
	 * @return
	 */
	public static String getMonthLastDay(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		int endday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DATE, endday);
		return convertDateToString(calendar.getTime());
	}
	
	/**
	 * 获取当前系统时间所在年的第一天
	 * 
	 * @return
	 */
	public static String getCurrentYearFirstDay(String date) {		
		String year = date.substring(0, 4).concat("-01-01");
		return year;
	}
	
	/**
	 * 获取当前系统时间所在年的最后一天
	 * 
	 * @return
	 */
	public static String getCurrentYearLastDay(String date) {
		String year = date.substring(0, 4).concat("-12-31");
		return year;
	}
	
	/**
	 * 获取去年的第一天
	 * 
	 * @return
	 */
	public static String getLastYearFirstDay(Date start) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(Calendar.YEAR, -1);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		return convertDateToString(calendar.getTime());
	}
	
	/**
	 * 获取去年的最后一天
	 * 
	 * @return
	 */
	public static String getLastYearLastDay(Date start) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.set(Calendar.DAY_OF_YEAR, 0);
		return convertDateToString(calendar.getTime());
	}
	
	/**
	 * 获取当前系统时间上个月的第一天
	 * 
	 * @return
	 */
	public static String getLastMonthFirstDay(Date start) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return convertDateToString(calendar.getTime());
	}
	
	/**
	 * 获取当前系统时间上个月的最后一天
	 * 
	 * @return
	 */
	public static String getLastMonthLastDay(Date start) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		return convertDateToString(calendar.getTime());
	}
	
	/**
	 * 获取当前系统时间所在月的第一天
	 * 
	 * @return
	 */
	public static String getCurrentMonthFirstDay(Date start) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		int startday = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DATE, startday);
		return convertDateToString(calendar.getTime());
	}

	/**
	 * 根据年和月获取所在月的最后一天的日期
	 * 
	 * @return
	 */
	public static Date getMonthLastDayByYM(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		int endday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DATE, endday);
		return calendar.getTime();
	}

	/**
	 * 判断日期是否是周末
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isWeekend(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		if (week == 6 || week == 7) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 一天的开始
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		return calendar.getTime();
	}

	/**
	 * 判断日期是星期几 2:星期一；3：星期二...7:星期六;1:星期天
	 */
	public static int getWeekday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		return weekday;
	}

	/**
	 * 判断日期是否在某一时间段内，包含起始天
	 * 
	 * @param start
	 * @param end
	 * @param target
	 * @return 存在返回true
	 */
	public static boolean judge(Date start, Date end, Date target) {
		if (target.before(start)) {
			return false;
		}
		if (target.after(end)) {
			return false;
		}
		return true;
	}

	public static Date addSecond(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, amount);
		return c.getTime();
	}
	
	public static String defaultSimpleFormater = "yyyy-MM-dd HH:mm:ss";
	
	public static Date parse(String dateString, String formatString) {
		SimpleDateFormat df = new SimpleDateFormat(formatString);
		try {
			return df.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date parse(String dateString) {
		return parse(dateString, defaultSimpleFormater);
	}
	
	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param formatString
	 * @return
	 */
	public static String format(Date date, String formatString) {
		if (date == null) {
			return null;
		} else {
			SimpleDateFormat df = new SimpleDateFormat(formatString);
			return df.format(date);
		}
	}

	/**
	 * 格式化日期(使用默认格式)
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date, defaultSimpleFormater);
	}

	/***
	 * 当前系统时间是否在6点以前
	 * 
	 * @return
	 */
	public static boolean isBeforeSixClock() {
		try {
			int nowHour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			return nowHour<6?true:false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 获得当前系统小时数
	 * 
	 * @return
	 */
	public static int getCurrentHours() {
		try {
			int nowHour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			return nowHour;
		} catch (Exception e) {
			return 0;
		}
	}
	
	/***
	 * 判断某一个时间是否实在6点之前
	 * 
	 * @param dateTime：时间需要包年月日含时分秒
	 * @return
	 */
	public static boolean isBeforeSixClock(Date dateTime) {
		try {
			Calendar calendarobj=Calendar.getInstance();
			calendarobj.setTime(dateTime);
			int nowHour=calendarobj.get(Calendar.HOUR_OF_DAY);
			return nowHour<6?true:false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/***
	 * 格式化日期信息
	 * 返回结果是yyyy-MM-dd
	 * 
	 * @param dateTimeStr：格式EEE MMM dd HH:mm:ss zzz yyyy
	 * 例如:Tue Mar 07 00:00:00 CST 2017
	 * @return
	 */
	public static String converStrToSimpleDate(String dateTimeStr) {
		try {
			SimpleDateFormat startFormate=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.US);
			SimpleDateFormat lastFormate=new SimpleDateFormat("yyyy-MM-dd");
			return lastFormate.format(startFormate.parse(dateTimeStr));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
     * 
     * 数字格式的年月日yyyyMMdd
     * 
     * @return
     */
    public static String getDatePatternNum() {
        String defaultDatePattern = FORMATYYYYMMDD;
        return defaultDatePattern;
    }
    
	
	public static Date converYYYYMMddHHmmssStrToDate(String date){
        Date d = null;
        try {
            if(date == null || "".equals(date.trim()))
                return null;
            SimpleDateFormat format = new SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_MM_SS,Locale.ENGLISH);
            d = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
}
