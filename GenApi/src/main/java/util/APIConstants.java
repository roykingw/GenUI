package util;

public class APIConstants {
	//常用的时间格式化匹配。目前用于genauth中Date属性接收Get请求参数，以及genServer中baseServiceImpl对mapper的Date类型进行尝试格式匹配
	public static final String[] commonDateFormatters= new String[] {"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd","yyyyMMdd"};
}
