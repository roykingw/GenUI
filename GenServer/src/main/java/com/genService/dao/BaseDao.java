package com.genService.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Dao直接使用jdbcTemplate的方式
 * @author roykingw
 *
 */
@Repository
public class BaseDao {
	
	@Resource
	@Qualifier("appJdbcTemplate")
	protected JdbcTemplate appJdbcTemplate;

	public JdbcTemplate getAppJdbcTemplate() {
		return appJdbcTemplate;
	}

	public void setAppJdbcTemplate(JdbcTemplate appJdbcTemplate) {
		this.appJdbcTemplate = appJdbcTemplate;
	}
	
}
