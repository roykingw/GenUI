package com.genauth.sys.dao;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MetaCommonDao {
	@Resource
	@Qualifier("metaJdbcTemplate")
	protected JdbcTemplate metaJdbcTemplate;

	public void setMetaJdbcTemplate(JdbcTemplate metaJdbcTemplate) {
		this.metaJdbcTemplate = metaJdbcTemplate;
	}
}
