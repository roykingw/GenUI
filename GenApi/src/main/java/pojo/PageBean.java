package pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 需要注意一个问题，PageBean对象不能接受来自于页面的数字类型的参数
 * 
 * 分页核心思路：1、计算总的数据量
 *          2、当前页数据获取
 * 不再需要支持Hbase的rowkey结构，这个分页类可以简单很多。
 * 主要用currPage 和 pageSize来分页
 */
public class PageBean <T> implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 当前页
	 */
	private int currPage;
	/**
	 * 总记录数
	 */
	private int totalCount;
	/**
	 * 总页数
	 */
	private int totalPageCount;
	/**
	 * 每页的记录条数
	 */
	private int pageSize=30;
	/**
	 * 显示的统计标题
	 */
	private String pageTitle = "";
	/**
	 * 当前页的记录
	 */
	private List<T> pageT;;
	/**
	 * Map形式 保存数据库查出来的结果。 
	 * 通用模版展现时，可以不用过滤
	 */
	private List<Map<String,Object>> page;
	/**
	 * 通用报表表头列
	 */
	private Object[] tableHeaders;
	/**
	 * 搜索条件
	 */
	private Map<String,Object> conditions;
	/**
	 * 下一页的开始行
	 */
//	public String startRowKey;
	/**
	 * 上一页的开始字段
	 * 
	 */
//	public String backStartRowKey;
	/**
	 * 开始时间
	 */
	public String starttime;
	/**
	 * 结束时间
	 */
	public String endtime;
	
	/**
	 * 开始行
	 */
//	public String startRow;
	/**
	 * 结束行
	 */
//	public String stopRow;
	
	/**
	 * 查询方向--向上分页(left)或向下分页(right)
	 */
	private String pagingType;
	
	/**
	 * 分页第一行数据
	 */
	private String beginRow;
	
	
	public PageBean(){
		 
	}
	
	public String getBeginRow() {
		return beginRow;
	}


	public void setBeginRow(String beginRow) {
		this.beginRow = beginRow;
	}

	
	/**
	 * 设置总页数
	 */
	public void updateTotalPageCount(){
		if(totalCount%pageSize==0){
			totalPageCount=totalCount/pageSize;
		}else{
			totalPageCount=totalCount/pageSize+1;
		}
	}
	
	@Override
	public String toString(){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("pageBean", this);
		return jsonObject.toString();
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		if(totalCount%pageSize==0){
			totalPageCount=totalCount/pageSize;
		}else{
			totalPageCount=totalCount/pageSize+1;
		}
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

//	public List<T> getPage() {
//		return page;
//	}
//
//	public void setPage(List<T> page) {
//		this.page = page;
//	}

	public Map<String, Object> getConditions() {
		return conditions;
	}

	public void setConditions(Map<String, Object> conditions) {
		this.conditions = conditions;
	}

	public String getPagingType() {
		return pagingType;
	}

	public void setPagingType(String pagingType) {
		this.pagingType = pagingType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public Object[] getTableHeaders() {
		return tableHeaders;
	}

	public void setTableHeaders(Object[] tableHeaders) {
		this.tableHeaders = tableHeaders;
	}

	public List<Map<String, Object>> getPage() {
		return page;
	}

	public void setPage(List<Map<String, Object>> page) {
		this.page = page;
	}

	public List<T> getPageT() {
		return pageT;
	}

	public void setPageT(List<T> pageT) {
		this.pageT = pageT;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
	public int getFirstResult() {
		int firstResult = (this.getCurrPage() - 1) * getPageSize();
		if (firstResult >= this.getTotalCount()) {
			firstResult = 0;
		}
		return firstResult;
	}
}
