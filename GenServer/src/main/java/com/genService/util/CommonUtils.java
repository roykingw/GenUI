
package com.genService.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtils {

	private static Logger logger = Logger.getLogger(CommonUtils.class);
	/**
	 * 以当前时间为基础,获取两个时间单位，单位为天
	 * @param days
	 * @return
	 */
	public static String[]  getCommonFindDate(Integer days){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String[] InitializationTime=new String[2];
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		InitializationTime[1]=sdf.format(calendar.getTime());
		calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-days);
		InitializationTime[0]=sdf.format(calendar.getTime());
//		for(int i=0;i<=days;i++){
//			  if(i==0){
//				  InitializationTime[0]=sdf.format(calendar.getTime());
//			  }else if(i==days){
//				  InitializationTime[1]=sdf.format(calendar.getTime());
//			  }
//			  calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1);
//		}
		return InitializationTime;
	}

	/**
	 * hbase查询的原则是：含头部含尾   所以当前时间增加1作为结束时间
	 * @param daystr 日期
	 * @param num 增加天数
	 * @return
	 */
	public static String addDate4Days(String daystr,Integer num){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Date date=null;
		try {
			date=sdf.parse(daystr);
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+num);
			date=calendar.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(date==null){
			return "";
		}
		return sdf.format(date);
	}
	/**
	 * 格式化数字结果，防止科学技术
	 * @param doubleStr
	 * @return
	 */
	public static String formatDouble(Object doubleStr){
		String res = "";
		DecimalFormat df=new DecimalFormat("#0.00");
		if(null == doubleStr){
			return res;
		}else{
			try{
				res = df.format(Double.parseDouble(doubleStr.toString()));
			}catch(Exception e){
				res = doubleStr.toString();
			}
		}
		return res;
	}

	public static Double getDouble(Object doubleStr){
		Double res = 0.00;
		if(null == doubleStr){
			return res;
		}else{
			try{
				res = Double.parseDouble(doubleStr.toString());
			}catch(Exception e){
				res = 0.00;
			}
		}
		return res;
	}
	/**
	 * 获取偏移天
	 * @param day_offset 天数偏移量
	 * @param format 输出格式
	 * @return
	 */
	public static String getOffsetDay(int day_offset,String format){
		SimpleDateFormat df = new SimpleDateFormat(format);
		Calendar cal= Calendar.getInstance();
		if(day_offset!= 0){
			cal.add(Calendar.DAY_OF_MONTH, day_offset);
		}
		return df.format(cal.getTime());
	}
	/**
	 * 获取偏移月
	 * @param month_offset 月偏移量
	 * @param format 输出格式
	 * @return
	 */
	public static String getOffsetMonth(int month_offset,String format){
		SimpleDateFormat df = new SimpleDateFormat(format);
		Calendar cal= Calendar.getInstance();
		if(month_offset!= 0){
			cal.add(Calendar.MONTH, month_offset);
		}
		return df.format(cal.getTime());
	}

	/**
	 * 获取偏移年
	 * @param month_offset 月偏移量
	 * @param format 输出格式
	 * @return
	 */
	public static String getOffsetYear(int year_offset,String format){
		SimpleDateFormat df = new SimpleDateFormat(format);
		Calendar cal= Calendar.getInstance();
		if(year_offset!= 0){
			cal.add(Calendar.YEAR, year_offset);
		}
		return df.format(cal.getTime());
	}

	public static Object[] list2array(List<String> paras){
		Object[] res = new Object[paras.size()];
		for(int i = 0 ; i < paras.size(); i++){
			res[i] = paras.get(i);
		}
		return res;
	}

	public static String getString(Object obj){
		return null==obj?"":obj.toString();
	}

	/**
	 * 对POJO类的所有属性进行UTF-8解码。
	 * @param obj 要解码的POJO类
	 */
	public static void decodePara(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			String getterName = "get"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1);
			String setterName = "set"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1);

			Method getterMethod = null;
			try {
				getterMethod = obj.getClass().getMethod(getterName);
				Object fieldValue = getterMethod.invoke(obj);
				if(null != fieldValue && fieldValue.toString().indexOf("%")>=0){
					String fieldRes= URLDecoder.decode(fieldValue.toString(),"UTF-8");
					obj.getClass().getMethod(setterName,String.class).invoke(obj,fieldRes);
				}
			} catch (NoSuchMethodException e) {
				continue;
			} catch (InvocationTargetException e) {
				continue;
			} catch (IllegalAccessException e) {
				continue;
			} catch (UnsupportedEncodingException e) {
				continue;
			}
		}
	}
	
	public static Map<String,Object> bean2Map(Object obj){
		Map<String,Object> res = new HashMap<String,Object>();
		for(Field field:obj.getClass().getDeclaredFields()){
			try{
				String getterName = "get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
				String getterRes =  obj.getClass().getMethod(getterName).invoke(obj).toString();
				res.put(field.getName()	, getterRes);
			}catch(Exception e){
				logger.debug("Bean转换异常："+e);
			}
		}
		return res;
	}
	
	public static List<Map<String,Object>> beanList2Map(Collection<?> col){
		List<Map<String,Object>> res = new ArrayList<>();
		col.stream().forEach(obj -> res.add(bean2Map(obj)) );
		return res;
	}
	
	public static String[] listString2Array(List<String> list){
		String[] res = new String[list.size()];
		for(int i = 0 ;  i  < list.size() ; i ++){
			String info = list.get(i);
			res[i] = StringUtils.isEmpty(info)?"":info;
		}
		return res;
	}
}
