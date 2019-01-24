package com.genauth.sys.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.genauth.sys.dao.SubMenuDao;
import com.genauth.sys.entity.SubMenu;

@Service
public class SubMenuService {
	@Resource
	@Qualifier("metaJdbcTemplate")
	private JdbcTemplate metaJdbcTemplate;
	
	@Resource
	private SubMenuDao dao;

	@Cacheable(value = "sysCache")
	public List<SubMenu> queryMenu(SubMenu cond) {
		return dao.queryMenu(cond);
	}

	@Transactional
	public int deleteMenu(SubMenu cond) {
		return dao.deleteMenu(cond);
	}

	public int createMenu(SubMenu cond) {
		return dao.createMenu(cond);
	}

	public int update(SubMenu cond) {
		return dao.updateMenu(cond);
	}

	public String getMenuCount() {
		return dao.getMenuCount();
	}
}
