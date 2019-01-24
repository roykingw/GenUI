package com.genService.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import annotation.TableColumn;

public class ReflectUtil {

	// 解析出pojo里的主键 返回map<fieldname,dbColumn>
	public static Map<String, Object> getPK(Class<?> repoClass) {
		Map<String, Object> res = new HashMap<String, Object>();
		Field[] fields = repoClass.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();// .toLowerCase();
			TableColumn col = field.getAnnotation(TableColumn.class);
			if (null != col && StringUtils.isNotBlank(col.isPK())) {
				if (StringUtils.isNotBlank(col.dbFunc())) {
					res.put(fieldName, col.dbFunc());
				} else {
					res.put(fieldName, col.dbColumn());
				}
			}
		}
		return res;
	}

	// 解析出pojo里的所有查询列(所有配置了TableColumn注释的列) 返回map<fieldname,dbColumn>
	public static Map<String, Object> getQueryCols(Class<?> repoClass) {
		Map<String, Object> res = new HashMap<String, Object>();
		Field[] fields = repoClass.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();// .toLowerCase();
			TableColumn col = field.getAnnotation(TableColumn.class);
			if (null != col && StringUtils.isNotBlank(col.dbColumn())) {
				res.put(fieldName, col.dbColumn());
			}
		}
		return res;
	}

	// 解析出pojo所有列的配置情况。预留，以后可以考虑把这个做成基于repoClass的缓存，这样可以简化上面的一些实现逻辑。
	public static Map<String, TableColumn> getTableCol(Class<?> repoClass) {
		Map<String, TableColumn> res = new HashMap<String, TableColumn>();
		Field[] fields = repoClass.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();// .toLowerCase();
			TableColumn col = field.getAnnotation(TableColumn.class);
			if (null != col) {
				res.put(fieldName, col);
			}
		}
		return res;
	}
}
