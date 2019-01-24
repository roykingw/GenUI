package util;

public class CGUtil {

	/**
	 * 把表结果的_连接改为驼峰结构 首字母大写
	 * @param tableName
	 * @return
	 */
	public static String getPOJONameByTableName(String tableName) {
		//防止死循环，加个计数器。 
		tableName = tableName.substring(0,1).toUpperCase()+tableName.substring(1);
		int count = 5;
		while(tableName.indexOf("_")>-1 && count >0) {
			int index = tableName.indexOf("_");
			tableName = tableName.substring(0, index)
			+tableName.substring(index+1,index+2).toUpperCase()+tableName.substring(index+2);
			count --;
		}
		return tableName;
	}
	
	/**
	 * 把字段改为驼峰结构 首字母大写
	 * @param columnName
	 * @return
	 */
	public static String getCamelName(String columnName) {
		//防止死循环，加个计数器。 
		int count = 5;
		while(columnName.indexOf("_")>-1 && count >0) {
			int index = columnName.indexOf("_");
			columnName = columnName.substring(0,1).toLowerCase()+columnName.substring(1, index)
			+columnName.substring(index+1,index+2).toUpperCase()+columnName.substring(index+2);
			count --;
		}
		return columnName;
	}
}
