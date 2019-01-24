package com.genService.util;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import annotation.TableColumn;
import jxl.common.Logger;

/**
 * mybatis的sql提供方法。目前主要用于生成update方法。
 * 
 * @author roykingw
 *
 */
public class GenSQLProvider {

	private Logger logger = Logger.getLogger(this.getClass());
	//拼凑不分页的查询SQL供导出用。
	public String genQueryExportData(Map<String,Object> param) {
		logger.info("=================开始拼凑查询SQL=========================");
		Map<String,String> requestPara = (Map<String,String>)param.get("requestData");
		Class<?> repo = (Class<?>)param.get("repoClass");
		StringBuffer sql  = new StringBuffer();
		StringBuffer selectSQL = new StringBuffer("SELECT ");
		StringBuffer whereSQL = new StringBuffer(" WHERE 1=1 ");
		for(Field field:repo.getDeclaredFields()) {
			TableColumn anno = field.getAnnotation(TableColumn.class);
			if(null != anno && StringUtils.isNotEmpty(anno.dbColumn()) && StringUtils.isNotEmpty(anno.excelHeader())) {
				if(StringUtils.isNotEmpty(anno.dbFunc())) {
					selectSQL.append(anno.dbFunc()+" ");
				}
				selectSQL.append(anno.dbColumn());
				selectSQL.append(",");
			}
			
			if(requestPara.containsKey(field.getName()) && null != requestPara.get(field.getName())) {
				whereSQL.append(" AND ");
				if(null!= anno && StringUtils.isNotEmpty(anno.dbFunc())) {
					whereSQL.append(anno.dbFunc());
				}else if(StringUtils.isNotEmpty(anno.dbColumn())) {
					whereSQL.append(anno.dbColumn());
				}
				whereSQL.append("= #{requestData."+field.getName()+"}");
			}
		}
		if(StringUtils.isNotEmpty(requestPara.get("_condition"))) {
			whereSQL.append(requestPara.get("_condition"));
		}
		sql.append(selectSQL.subSequence(0, selectSQL.length()-1)).append(" from "+ requestPara.get("_table")+" ").append(whereSQL);
		logger.debug("=================完成拼凑查询SQL > "+sql.toString());
		return sql.toString();
	}
	//生成分页查询总数的SQL
	public String genQueryCountSQL(Map<String,Object> param) {
		logger.info("=================开始拼凑查询总数SQL=========================");
		Map<String,String> requestPara = (Map<String,String>)param.get("requestData");
		Class<?> repo = (Class<?>)param.get("repoClass");
		String dbType = requestPara.get("_dbType");
		StringBuffer countSql = new StringBuffer();
		String baseSQL = this.genQuerySQL(requestPara, repo);
		if ("oracle".equals(dbType)) {
			countSql.append("select count(*) COUNT from (").append(baseSQL).append(") t");// mysql还必须对from 后的表有个别名
		} else if ("mysql".equals(dbType)) {
			countSql.append("select count(*) COUNT from (").append(baseSQL).append(") t");
		}
		logger.debug("=================完成拼凑查询总数SQL > "+countSql.toString());
		return countSql.toString();
	}
	//生成分页查询数据的SQL
	public String genQueryPagedSQL(Map<String,Object> param) {
		logger.info("=================开始拼凑查询分页SQL=========================");
		Map<String,String> requestPara = (Map<String,String>)param.get("requestData");
		Class<?> repo = (Class<?>)param.get("repoClass");
		String dbType = requestPara.get("_dbType");
		StringBuffer pagedSql = new StringBuffer();
		String baseSQL = this.genQuerySQL(requestPara, repo);
		if ("oracle".equals(dbType)) {
			pagedSql.append("select * from (select rownum rn,t.* from (").append(baseSQL).append(") t) p where p.rn> #{requestData._startKey}")
			.append(" and p.rn<=#{requestData._endKey");
		} else if ("mysql".equals(dbType)) {
			pagedSql.append("select * from (").append(baseSQL).append(") t limit #{requestData._startKey} , #{requestData._pageSize}");
		}
		logger.debug("=================完成拼凑查询分页SQL > "+pagedSql.toString());
		return pagedSql.toString();
	}
	
	private String genQuerySQL(Map<String,String> requestPara,Class<?> repo) {
		StringBuffer sql = new StringBuffer();
		StringBuffer selectSQL = new StringBuffer("SELECT ");
		StringBuffer whereSQL = new StringBuffer(" WHERE 1=1 ");
		for(Field field:repo.getDeclaredFields()) {
			TableColumn anno = field.getAnnotation(TableColumn.class);
			if(null != anno && StringUtils.isNotEmpty(anno.dbColumn())) {
				if(StringUtils.isNotEmpty(anno.dbFunc())) {
					selectSQL.append(anno.dbFunc()+" ");
				}
				selectSQL.append(anno.dbColumn());
				selectSQL.append(",");
			}
			
			if(requestPara.containsKey(field.getName()) && null != requestPara.get(field.getName())) {
				whereSQL.append(" AND ");
				if(null!= anno && StringUtils.isNotEmpty(anno.dbFunc())) {
					whereSQL.append(anno.dbFunc());
				}else if(StringUtils.isNotEmpty(anno.dbColumn())) {
					whereSQL.append(anno.dbColumn());
				}
				whereSQL.append("= #{requestData."+field.getName()+"}");
			}
		}
		if(StringUtils.isNotEmpty(requestPara.get("_condition"))) {
			whereSQL.append(requestPara.get("_condition"));
		}
		sql.append(selectSQL.subSequence(0, selectSQL.length()-1)).append(" from "+requestPara.get("_table").toString()).append(whereSQL);
		logger.info("=================完成拼凑查询SQL > "+sql.toString());
		return sql.toString();
	}
	
	
	public String genUpdateData(Map<String, Object> param) {
		Map<String,String> requestPara = (Map<String,String>)param.get("requestData");
		Class<?> repo = (Class<?>)param.get("repoClass");
		String tableName = requestPara.get("_table");
		Field[] fields = repo.getDeclaredFields();
		SQL sql = new SQL();

		sql.UPDATE(tableName);
		for (Field field : fields) {
			String fieldName = field.getName();
			TableColumn col = field.getAnnotation(TableColumn.class);
			if (null != col && requestPara.containsKey(fieldName)) {
				sql.SET(col.dbColumn() + "= #{requestData." + fieldName + "}");
			}
		}
		Map<String,Object> repoPK = ReflectUtil.getPK(repo);
		for(String fieldName:repoPK.keySet()) {
			if(requestPara.containsKey(fieldName)) {
				sql.WHERE(repoPK.get(fieldName) +"= #{requestData."+fieldName+"}");
			}
		}
		String res = sql.toString();
		logger.debug("=========拼凑更新数据SQL > " + res);
		return res;
	}

	public String genDeleteData(Map<String, Object> param) {
		Map<String,String> requestPara = (Map<String,String>)param.get("requestData");
		Class<?> repo = (Class<?>)param.get("repoClass");
		String tableName = requestPara.get("_table");
		SQL sql = new SQL();
		sql.DELETE_FROM(tableName);
		Map<String,Object> repoPK = ReflectUtil.getPK(repo);
		for(String fieldName:repoPK.keySet()) {
			if(requestPara.containsKey(fieldName)) {
				sql.WHERE(repoPK.get(fieldName) +"= #{requestData."+fieldName+"}");
			}
		}
		String res = sql.toString();
		logger.debug("==========拼凑删除数据SQL > " + res);
		return res;
	}

}
