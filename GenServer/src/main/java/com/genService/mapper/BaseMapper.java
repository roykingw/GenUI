package com.genService.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.genService.util.GenSQLProvider;

public interface BaseMapper {

	@SelectProvider(method = "genQueryExportData",type = GenSQLProvider.class)
	public List<Map<String,Object>> queryExportData(@Param("requestData")Map<String,Object> requestPara,@Param("repoClass")Class<?> repo);
	
	@SelectProvider(method = "genQueryCountSQL",type = GenSQLProvider.class)
	public int queryCountData(@Param("requestData")Map<String,Object> requestPara,@Param("repoClass")Class<?> repo);
	
	@SelectProvider(method = "genQueryPagedSQL",type = GenSQLProvider.class)
	public List<Map<String,Object>> queryPagedData(@Param("requestData")Map<String,Object> requestPara,@Param("repoClass")Class<?> repo);
	
	@DeleteProvider(method = "genDeleteData", type = GenSQLProvider.class)
	public int deleteData(@Param("requestData")Map<String, String> requestPara,@Param("repoClass")Class<?> repo);
	
	@UpdateProvider(method = "genUpdateData", type = GenSQLProvider.class)
	public int updateData(@Param("requestData")Map<String, String> requestPara,@Param("repoClass")Class<?> repo);
}
