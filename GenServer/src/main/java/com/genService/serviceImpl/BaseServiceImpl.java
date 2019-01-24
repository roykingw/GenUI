package com.genService.serviceImpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import com.genService.dao.BaseDao;
import com.genService.mapper.BaseMapper;
import com.genService.util.CommonUtils;
import com.genService.util.ReflectUtil;

import annotation.Table;
import annotation.TableColumn;
import pojo.PageBean;
import services.BaseService;
import util.ExportDatas;
import util.RequestPara;

/**
 * Created by roykingw on 2017/12/15 0015.
 * 通用查询的核心实现类。封装通用的分页查询、导出全量查询、修改数据、删除数据 四个方法。
 * 目前只支持mysql 和 oracle。对其他数据库的封装最主要的差距就是拼分页SQL的方法。
 * 理解一下不难进行扩展。
 */
public class BaseServiceImpl<T> implements BaseService<T> {
	private Logger logger = Logger.getLogger(this.getClass());
	protected final String QUERY_CONDITION = "condition";
	@Resource
	private BaseDao baseDao;
	
	@Resource
	private BaseMapper mapper;

	@Value("${appDataSource.type}")
	private String dbType;

	@Resource(name = "appJdbcTemplate")
	private JdbcTemplate appJdbcTemplate;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
	//查询改为使用mybatis去查。字段兼容性应该会比较好一点。
	//TODO 需要增加typeHandler，来对不同类型的列做操作，如回填、格式化等。如果能用上mybatis，就更好了。
	/**
	 * 通用的查询方法。配合自动生成，比较好用。不用自动生成也尽量让他比较好进行扩展。 
	 * 通过Bean上的Table table和 TableColumn col 注释拼凑成SQL执行。 select col.dbFunc
	 * col.dbColumn,... from table.tableName where 1=1 and
	 * [col.dbFunc|col.dbColumn]= request.getParameter(field) and
	 * request.getAttribute("condition") order by table.orderBy;
	 * 目前where后面的条件比对，暂时只支持 = 操作。其他操作如 大于 小于 等，等以后代码跑稳定了再扩展。
	 * 返回的结果PageBean中，主要组织了查询列表和结果导出 包括 ：
	 * List<String> tableheader: 列表列头。 tableColumn的excelheader 属性
	 * List<Map<String,Object>> pages : 列表内容。有excelheader属性的列。
	 * List<T> pageT: 每一列的查询对象示例。用于查询页面进行增删改等操作。有tableColumn注释的列。
	 * 对于Bean中不太好定义的查询条件,比如时间范围，目前用request的attribute("conditon")自己写好了。
	 */
	
	public PageBean<T> queryData(RequestPara requestPara, PageBean<T> pageBean) throws Exception{
		logger.info(">>>开始通用报表数据查询V2");
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		// 得到t的实现类型
		Class<T> repoClass = (Class<T>) type.getActualTypeArguments()[0];
		// 根据bean的注释配置SQL
		Table table = repoClass.getAnnotation(Table.class);
		if (null == table) {
			throw new Exception(repoClass.getSimpleName() + "未配置 Table 注释");
		}
		Map<String,Object> queryMap = new HashMap<String,Object>();
		for(String key: requestPara.getRequestParas().keySet()) {
			queryMap.put(key, requestPara.getRequestParas().get(key));
		}
		queryMap.put("_table", table.tableName());
		queryMap.put("_condition", requestPara.getQueryCond());
		queryMap.put("_dbType", this.dbType.trim());
		int startKey = (pageBean.getCurrPage() - 1) * pageBean.getPageSize();
		queryMap.put("_pageSize", pageBean.getPageSize());
		queryMap.put("_startKey", startKey);
		queryMap.put("_endKey", startKey + pageBean.getPageSize());
		//查总数
		logger.debug("通用查询，参数列表  : "+requestPara);
		int queryCount = mapper.queryCountData(queryMap, repoClass);
		logger.debug("通用查询分页数据，总数目 ："+queryCount);
		pageBean.setTotalCount(queryCount);
		//查所有配了TableColumn注释的列 这个结果查出来的全是dbColumn
		List<Map<String, Object>> queryPagedData = mapper.queryPagedData(queryMap, repoClass);
		String convertErrorCols = "";
		//将所有配了TableColumn属性的列回填到新对象实例中，保存到pageBean.pageT里。用于做数据操作。以对象方式做数据操作。
		List<T> pageT = new ArrayList<>();
		//将所有配了TableColumn的excelheader()属性的列原样保存到pageBean.page里，用于列表展示。只要进行展示，不用封装对象。
		List<Map<String, Object>> pages = new ArrayList<>();
		//将所有配置了的TableColumn的excelheader()属性值保存到tableHeaders里，用于展示列表表头。
		List<String> tableHeaders = new ArrayList<String>();
		//收集tableHeader
		for(Field field:repoClass.getDeclaredFields()) {
			TableColumn tableColumn = field.getAnnotation(TableColumn.class);
			if(null == tableColumn) continue;
			if(StringUtils.isNotEmpty(tableColumn.excelHeader())
					&& !tableHeaders.contains(tableColumn.excelHeader())) 
				tableHeaders.add(tableColumn.excelHeader());
		}
		//数据回填
		for(Map<String,Object> res:queryPagedData) {
			T t = repoClass.newInstance();//回填的实例
			Map<String,Object> record = new HashMap<String,Object>();
			for(int i = 0 ; i < repoClass.getDeclaredFields().length;i++) {
				Field field=repoClass.getDeclaredFields()[i];
				TableColumn tableColumn = field.getAnnotation(TableColumn.class);
				if(null == tableColumn || StringUtils.isEmpty(tableColumn.dbColumn())) continue;
				Object colRes = res.get(tableColumn.dbColumn());
				// 数据库查出来的结果可能为null
				if(null == colRes) {
					record.put(""+i, "");
					continue;
				}
				//2018年8月31日 用于列表展示的结果格式化
				if(StringUtils.isNotEmpty(tableColumn.excelHeader())) {
					if(colRes instanceof java.sql.Date) {
						record.put(""+i, dateFormat.format((java.sql.Date)colRes));
					}else if(colRes instanceof java.sql.Timestamp) {
						record.put(""+i, dateTimeFormat.format((java.sql.Timestamp)colRes));
					}else {
						record.put(""+i, res.get(tableColumn.dbColumn()));
					}
				}
				//结果回填到示例对象，用于做数据操作。2018年8月28日 改用mybatis进行查询后，这一部分可以简单一点了。
				String fieldName = field.getName();
				String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method setter = repoClass.getMethod(setterName, field.getType());
				try {
					// 通用类型的转换
					setter.invoke(t, field.getType().cast(colRes));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| ClassCastException ex) {
					// 类型转换后可能无法注入原属性的情况。常见Date类型to_char后转成了String无法注入。
					if (convertErrorCols.indexOf(fieldName) < 0) {
						convertErrorCols += fieldName + ",";
					}
					continue;
				} catch (Exception e) {
					logger.error("通用查询结果转换报错;" + e.getMessage(), e);
				}
			}
			pages.add(record);
			pageT.add(t);
		}
		if (StringUtils.isNotEmpty(convertErrorCols)) {
			logger.error("分页查询类型转换错误 ，属性" + repoClass.getName() + "的" + convertErrorCols
					+ "属性注入失败，请检查数据库查询返回类型与Bean的属性类型。");
		}
		pageBean.setPageT(pageT);
		pageBean.setPage(pages);
		pageBean.setTableHeaders(tableHeaders.toArray());
		return pageBean;
	}
	
	//jdbcTemplate方式执行查询。参数类型解析比较麻烦，改用MyBatis查询。留个纪念把。
//	public PageBean<T> queryData(RequestPara requestPara, PageBean<T> pageBean) throws Exception {
//		logger.info(">>>开始通用报表数据查询");
//		
//		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
//		// 得到t的实现类型
//		Class<T> repoClass = (Class<T>) type.getActualTypeArguments()[0];
//		StringBuilder sql = new StringBuilder("select ");// 要执行的SQL
//		// 把request里的参数插入到查询对象中。
//		Field[] fields = repoClass.getDeclaredFields();
//		StringBuilder sqlCols = new StringBuilder();// sql的字段名
//		StringBuilder sqlCond = new StringBuilder(" where 1=1 ");// sql的查询条件
//		List<Object> paras = new ArrayList<Object>();// sql的执行参数
//		List<String> tableHeaders = new ArrayList<String>();
//		for (int i = 0; i < fields.length; i++) {
//			Field field = fields[i];
//			TableColumn col = field.getAnnotation(TableColumn.class);
//			if (null != col) {
//				// 把所有配置了TabelColumn注释的列全部都查出来
//				String dbFunc = col.dbFunc();
//				String dbColumn = col.dbColumn();
//				sqlCols.append(dbFunc + " " + dbColumn + ",");
//				// 只有TableColumn里配置了excelHeader属性的列才会最终展示出来。
//				if (StringUtils.isNotEmpty(col.excelHeader())) {
//					tableHeaders.add(col.excelHeader());
//					String fieldName = field.getName();// .toLowerCase()
//					if (requestPara.getRequestParas().containsKey(fieldName)) {
//						String para = requestPara.getRequestParas().get(fieldName);
//						if (para.indexOf("%") >= 0) {
//							para = URLDecoder.decode(para, "UTF-8");
//						}
//						// 有dbFunc就用dbFunc作为查询条件。如果没有，就用dbColumn
//						sqlCond.append(" and ").append(StringUtils.isEmpty(dbFunc) ? dbColumn : dbFunc).append("=?");
//						paras.add(para);
//					}
//				}
//			} else {
//				// 没有配置col的暂时就不管了。
//			}
//		}
//		// 增加扩展一个Bean中不好定义的自定义查询条件，比如时间范围。
//		if (StringUtils.isNotEmpty(requestPara.getQueryCond())) {
//			sqlCond.append(requestPara.getQueryCond());
//		}
//		if (StringUtils.isEmpty(sqlCols)) {
//			throw new Exception(repoClass.getSimpleName() + "的TableColumns注释配置不正确");
//		}
//		// 根据bean的注释配置SQL
//		Table table = repoClass.getAnnotation(Table.class);
//		if (null == table) {
//			throw new Exception(repoClass.getSimpleName() + "未配置 Table 注释");
//		}
//		sql.append(sqlCols.substring(0, sqlCols.length() - 1)).append(" from ").append(table.tableName())
//				.append(sqlCond);
//		if (StringUtils.isNotEmpty(table.orderBy())) {
//			sql.append(" order by " + table.orderBy()).append(" ").append(table.order());
//		}
//		logger.info(">>>封装查询 sql=>" + sql + ";paras =>" + paras.toArray());
//		// 封装分页SQL
//		String pagedSQL = "";// 分页查询数据SQL
//		String countSQL = "";// 获取总记录数SQL
//		int startKey = (pageBean.getCurrPage() - 1) * pageBean.getPageSize();
//		int endKey = startKey + pageBean.getPageSize();
//		if ("oracle".equals(this.getDbType().trim())) {
//			countSQL = "select count(*) COUNT from (" + sql + ") t";// mysql还必须对from 后的表有个别名
//			pagedSQL = "select * from (select rownum rn,t.* from (" + sql.toString() + ") t) p where p.rn>" + startKey
//					+ " and p.rn<=" + endKey;
//		} else if ("mysql".equals(this.getDbType().trim())) {
//			countSQL = "select count(*) COUNT from (" + sql + ") t";
//			pagedSQL = "select * from (" + sql.toString() + ") t limit " + startKey + "," + pageBean.getPageSize();
//		} else {
//			throw new Exception("暂不支持的数据库类型 => " + this.getDbType());
//		}
//		int totalCount = Integer
//				.parseInt(appJdbcTemplate.queryForMap(countSQL, paras.toArray()).get("COUNT").toString());
//		pageBean.setTotalCount(totalCount);
//		// 查询SQL
//		logger.info(">>>准备执行分页查询 sql=>" + pagedSQL + ";paras =>" + paras);
//		// 查询的结果包含了所有配置了tableColumn注释的field。
//		List<Map<String, Object>> queryRes = appJdbcTemplate.queryForList(pagedSQL, paras.toArray());
//		List<T> pageT = new ArrayList<>();
//		String convertErrorCols = "";
//		for (Map<String, Object> res : queryRes) {
//			// Oracle分页查询时多出了个rn字段用来分页，结果中要把他去掉。
//			if ("oracle".equals(this.getDbType().trim()) && res.containsKey("rn")) {
//				res.remove("rn");
//			}
//			// 结果过滤
//			T t = repoClass.newInstance();
//			for (Field field : fields) {
//				TableColumn col = field.getAnnotation(TableColumn.class);
//				// 如果没有配置TableColumn注解，直接跳过。
//				if (null == col) {
//					continue;
//				} else {
//					// 将结果拼装成Bean
//					if (res.containsKey(col.dbColumn().toUpperCase())) {
//						String fieldName = field.getName();
//						String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
//						Method setter = t.getClass().getMethod(setterName, field.getType());
//						// 数据库查出来的结果可能为null
//						Object colRes = res.get(col.dbColumn().toUpperCase());
//						if(null == colRes) {
//							continue;
//						}else{
//							try {
//								/*
//								 * 类型转换说明：常用的String,int,float等类型可以正常转换注入原属性。
//								 * 而有些特殊类型，如Date,timestamp等类型，一般查询都会引入dbFunc()转换成字符串进行条件匹配。
//								 * 这样就会造成返回类型(String)与原类型(Date,Timestamp)不符合而无法注入回去的情况。
//								 * 以下的处理对Date类型做了特殊的转换，应该能注入进去。 另外，Date类型的变量可以通过增加@JSONField注释定制转到页面上的JSON的格式。
//								 */
//								// 这里对Date类型字段，在数据库中通过函数转换成了标准字符串的情况做特殊处理。
//								if (field.getType().getName().equals("java.util.Date")
//										&& StringUtils.isNotEmpty(col.dbFunc())) {
//									if ("oracle".equals(this.getDbType().trim())) {
//										// 这是针对Oracle一般用to_char('','yyyyMMddhh')来进行格式化，把类型做一下简单转换。
//										String pattern = "[\\'\\\"](?<dateformat>([yYmMDdHhSs]|[^a-z])*)[\\'\\\"]";
//										Pattern p = Pattern.compile(pattern);
//										Matcher matcher = p.matcher(col.dbFunc());
//										if (matcher.find()) {
//											SimpleDateFormat format = new SimpleDateFormat(matcher.group("dateformat"));
//											setter.invoke(t, format.parse(colRes.toString()));
//										}
//									} else {
//										// 其他类型的数据库，对结果做一下几种常见格式的匹配，不行就往后面的错误字段里扔了。
//										Date date = null;
//										for (String df : APIConstants.commonDateFormatters) {
//											SimpleDateFormat sdf = new SimpleDateFormat(df);
//											try {
//												date = sdf.parse(colRes.toString());
//												break;
//											} catch (ParseException e) {
//												continue;
//											}
//										}
//										if (null != date) {
//											setter.invoke(t, date);
//										}
//									}
//								} else {
//									// 通用类型的转换
//									setter.invoke(t, field.getType().cast(colRes));
//								}
//							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
//									| ClassCastException ex) {
//								// 类型转换后可能无法注入原属性的情况。常见Date类型to_char后转成了String无法注入。
//								if (convertErrorCols.indexOf(fieldName) < 0) {
//									convertErrorCols += fieldName + ",";
//								}
//								continue;
//							} catch (Exception e) {
//								logger.error("通用查询结果转换报错;" + e.getMessage(), e);
//							}
//						}
//					}
//					// 如果没有配置excelHeader，表明该列不需要返回到结果中(依然可以用来做查询过滤)。也直接跳过。
//					if (StringUtils.isEmpty(col.excelHeader())) {
//						res.remove(col.dbColumn().toUpperCase());
//						// continue;
//					}
//				}
//			}
//			pageT.add(t);
//		}
//		if (StringUtils.isNotEmpty(convertErrorCols)) {
//			logger.error("分页查询类型转换错误 ，属性" + repoClass.getName() + "的" + convertErrorCols
//					+ "属性的TableColumn.dbFunc返回类型与原有类型不匹配，无法注入");
//		}
//		// 将结果全部封装到pageBean里
//		pageBean.setTableHeaders(tableHeaders.toArray());
//		pageBean.setPage(queryRes);
//		pageBean.setPageT(pageT);
//		logger.info(">>>通用报表分页查询成功。");
//		return pageBean;
//	}

	public ExportDatas exportData(RequestPara requestPara) throws Exception {
		logger.info(">>>开始通用报表数据导出V2");
		
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		// 得到t的实现类型
		Class<T> repoClass = (Class<T>) type.getActualTypeArguments()[0];
		// 根据bean的注释配置SQL
		Table table = repoClass.getAnnotation(Table.class);
		if (null == table) {
			throw new Exception(repoClass.getSimpleName() + "未配置 Table 注释");
		}
		Map<String,Object> queryMap = new HashMap<String,Object>();
		for(String key: requestPara.getRequestParas().keySet()) {
			queryMap.put(key, requestPara.getRequestParas().get(key));
		}
		queryMap.put("_dbType", this.dbType.trim());
		queryMap.put("_table", table.tableName());
		queryMap.put("_condition", requestPara.getQueryCond());
		//把所有配了TableColumn的ExcelHeader属性的列，才放到导出结果当中全都查出来
		List<Map<String, Object>> queryData = mapper.queryExportData(queryMap, repoClass);

		List<String> tableHeaders = new ArrayList<>();// excel导出的列头
		List<String[]> infoList = new ArrayList<String[]>(); //excel导出的列内容
		
		Field[] fields = repoClass.getDeclaredFields();
		//收集tableHeader
		for(Field field:repoClass.getDeclaredFields()) {
			TableColumn tableColumn = field.getAnnotation(TableColumn.class);
			if(null == tableColumn) continue;
			if(StringUtils.isNotEmpty(tableColumn.excelHeader())
					&& !tableHeaders.contains(tableColumn.excelHeader())) 
				tableHeaders.add(tableColumn.excelHeader());
		}
		for(Map<String,Object> res:queryData) {
			List<String> rowRecord = new ArrayList<String>();
			for(int i = 0 ; i < fields.length;i++) {
				Field field = fields[i];
				TableColumn col = field.getAnnotation(TableColumn.class);
				if(null == col) {
					continue;
				}
				if(StringUtils.isNotEmpty(col.excelHeader())) {
					Object colRes = res.get(col.dbColumn());
					if(null == colRes) {
						rowRecord.add("");
					}else if(colRes instanceof java.sql.Date) {
						rowRecord.add(dateFormat.format((java.sql.Date)colRes));
					}else if(colRes instanceof java.sql.Timestamp) {
						rowRecord.add(dateTimeFormat.format((java.sql.Timestamp)colRes));
					}else {
						rowRecord.add(colRes.toString());
					}
				}
			}
			infoList.add(CommonUtils.listString2Array(rowRecord));
		}
		ExportDatas res = new ExportDatas(table.expFileName(), infoList, CommonUtils.listString2Array(tableHeaders));
		return res;		
	}

	/**
	 * 通用数据删除。改用了MyBatis进行查询。用MyBatis生成SQL
	 * delete from table where pk1 = requestPara.get(col1) and pk2 = requestPara.get(col2)...;
	 */
	public int deleteData(RequestPara requestPara) throws Exception {
		logger.info(">>>开始通用数据删除");
		int res = -1;
		// 得到t的实现类型
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<T> repoClass = (Class<T>) type.getActualTypeArguments()[0];
		//参数检查
		//1、Pojo需配置对应的表名。
		Table table = repoClass.getAnnotation(Table.class);
		if (null == table) {
			throw new Exception(repoClass.getSimpleName() + "未配置 Table 注释");
		}
		//3、POJO中需配置主键列
		Map<String,Object> repoPK = ReflectUtil.getPK(repoClass);
		if(repoPK.size()<=0) {
			logger.error(repoClass.getSimpleName() + "未配置 主键列，请检查@TableColumn 的 isPK 属性");
			throw new Exception(repoClass.getSimpleName() + "未配置 主键列，请检查@TableColumn 的 isPK 属性");
		}
		//4、主键列需提交参数。每个都必须有
		for(String fieldName:repoPK.keySet()) {
			if(!requestPara.getRequestParas().containsKey(fieldName)) {
				throw new Exception(repoClass.getSimpleName() + " 未提交主键参数");
			}
		}
		//调用mybatis执行。
		requestPara.getRequestParas().put("_table", table.tableName());
		try {
			res = mapper.deleteData(requestPara.getRequestParas(), repoClass);
		} catch (Exception e) {
			logger.error(">>>SQL执行错误 =>",e);
			throw new Exception(repoClass.getSimpleName()+" SQL执行错误："+e.getMessage());
		}
		return res;
	}
	
	/**
	 * 通用数据修改  改用MyBatis进行查询，生成SQL 
	 * update table.tableName set col.dbColumn = request.getParameter(field),...
	 * where 1=1 and col.dbFunc|col.dbColumn = request.getParameter(field)
	 * 2018年4月20日 这样有问题哦。主键列没法更新。。并且前台如果把主键列改了的话，可能会修改到另外的一条数据。
	 * 那针对这个情况，前台需要传递新旧两个对象了。。。、
	 * 要不就在自动生成的逻辑中加入主键列前台不能修改。
	 * @return
	 * @throws Exception
	 */
	public int updateData(RequestPara requestPara) throws Exception{
		logger.info(">>>开始通用数据修改");
		int res = -1;
		// 得到t的实现类型
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<T> repoClass = (Class<T>) type.getActualTypeArguments()[0];
		//参数检查
		//1、Pojo需配置对应的表名。
		Table table = repoClass.getAnnotation(Table.class);
		if (null == table) {
			throw new Exception(repoClass.getSimpleName() + "未配置 Table 注释");
		}
		
		//2、需有要修改的列传入。至少一个。
		Map<String,Object> setCols = ReflectUtil.getQueryCols(repoClass);
		StringBuilder setCheck = new StringBuilder();
		for(String fieldName:setCols.keySet()) {
			if(requestPara.getRequestParas().containsKey(fieldName) 
					&& StringUtils.isNotEmpty(requestPara.getRequestParas().get(fieldName))) {
				break;
			}
			setCheck.append(fieldName);
		}
		if(StringUtils.isNotBlank(setCheck.toString())) {
			throw new Exception(repoClass.getSimpleName()+" 未传递需要更新的列。请检查参数。");
		}
		//3、POJO中需配置主键列
		Map<String,Object> repoPK = ReflectUtil.getPK(repoClass);
		if(repoPK.size()<=0) {
			logger.error(repoClass.getSimpleName() + "未配置 主键列，请检查@TableColumn 的 isPK 属性");
			throw new Exception(repoClass.getSimpleName() + "未配置 主键列，请检查@TableColumn 的 isPK 属性");
		}
		//4、主键列需提交参数。每个都必须有
		for(String fieldName:repoPK.keySet()) {
			if(!requestPara.getRequestParas().containsKey(fieldName)) {
				throw new Exception(repoClass.getSimpleName() + " 未提交主键参数");
			}
		}
		//调用mybatis执行。
		requestPara.getRequestParas().put("_table", table.tableName());
		try {
			res = mapper.updateData(requestPara.getRequestParas(), repoClass);
		} catch (Exception e) {
			logger.error(">>>SQL执行错误 =>",e);
			throw new Exception(repoClass.getSimpleName()+" SQL执行错误："+e.getMessage());
		}
		return res;
	}
	
	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public BaseMapper getMapper() {
		return mapper;
	}

	public void setMapper(BaseMapper mapper) {
		this.mapper = mapper;
	}
	
}
