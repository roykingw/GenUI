package com.genauth.sys.util;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {
	/**
	 * 将日期转换为yyyy-MM-dd格式
	 * @param str
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String rowDateFormat(String str,String format) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		SimpleDateFormat sd=new SimpleDateFormat("yyyyMMdd");
		Date parse = sd.parse(str);
		return sdf.format(parse);
	}

	/**
	 * 自定义格式转换
	 * @param firstFormat返回格式
	 * @param secondFormat转换格式
	 * @return
	 * @throws Exception
	 */
	public static String rowDateFormatOther(String str,String firstFormat,String secondFormat) throws Exception{
		if(StringUtils.isBlank(str)){
			return "";
		}
		SimpleDateFormat sdf=new SimpleDateFormat(firstFormat);
		SimpleDateFormat sd=new SimpleDateFormat(secondFormat);
		Date parse = sd.parse(str);
		return sdf.format(parse);
	}
	/**
	 * 字符串转日期
	 * @param str
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Date String2Date(String str,String format) throws Exception{
		SimpleDateFormat sd=new SimpleDateFormat(format);
		return sd.parse(str);
	}
	/**
	 * 日期转字符串
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String Date2String(Date date,String format) throws Exception{
		SimpleDateFormat sd=new SimpleDateFormat(format);
		return sd.format(date);
	}

	/**
	 * 从starttime开始往后查days天,日期格式为format
	 * @param starttime
	 * @param days
	 * @return
	 * @throws Exception
	 */
	public static String getQueryDate(String starttime,int days,String format) throws Exception{
		String res = "";

		SimpleDateFormat sd=new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.setTime(sd.parse(starttime));
		cal.add(Calendar.DAY_OF_MONTH, days);
		res = sd.format(cal.getTime());

		return res;
	}

	public static String getYesterdayStr(String format) throws Exception{
		String res = "";

		SimpleDateFormat sd=new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		res = sd.format(cal.getTime());

		return res;
	}
}
