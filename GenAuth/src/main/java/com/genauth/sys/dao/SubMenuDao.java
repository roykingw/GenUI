package com.genauth.sys.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.genauth.app.util.IdGen;
import com.genauth.sys.entity.SubMenu;

@Repository
public class SubMenuDao extends MetaCommonDao {
	
	public List<SubMenu> queryMenu(SubMenu cond) {
		List<String> lparas = new ArrayList<String>();
		StringBuffer sql = new StringBuffer("select menu_id,name,state_path,"
				+ "menu_code,controller_code,page_code,"
				+ "parent,menu_level from t_menu_info where 1=1 ");
		if(StringUtils.isNotEmpty(cond.getName())){
			sql.append(" and INSTR(name,?)>0");
			lparas.add(cond.getName());
		}
		if(StringUtils.isNotEmpty(cond.getMenuCode())){
			sql.append(" and menu_code = ?");
			lparas.add(cond.getMenuCode());
		}
		if(StringUtils.isNotEmpty(cond.getParent())){
			sql.append(" and parent = ?");
			lparas.add(cond.getParent());
		}
		Object[] sparas = new Object[lparas.size()];
		for(int i = 0 ; i < lparas.size(); i ++){
			sparas[i] = lparas.get(i);
		}
		return metaJdbcTemplate.query(sql.toString(),sparas,
				new BeanPropertyRowMapper<SubMenu>(SubMenu.class));
	}

	public int deleteMenu(SubMenu cond) {
		String deleteAuthSql = "delete from t_role_auth where menu_id = ?";
		metaJdbcTemplate.update(deleteAuthSql,new Object[]{cond.getMenuId()});
		String sql = "delete from t_menu_info where menu_id = ?";
		return metaJdbcTemplate.update(sql, new Object[]{cond.getMenuId()});
	}

	public int createMenu(SubMenu cond) {
		String sql = "insert into t_menu_info(menu_id,name,state_path,menu_code,controller_code,page_code,parent,menu_level) values(?,?,?,?,?,?,?,?)";
		return metaJdbcTemplate.update(sql, new Object[]{IdGen.uuid(),cond.getName(),cond.getStatePath(),cond.getMenuCode(),cond.getControllerCode(),cond.getPageCode(),cond.getParent(),cond.getMenuLevel()});
//		String sql = "insert into t_menu_info(name,state_path,menu_code,controller_code,page_code,parent) values(?,?,?,?,?,?)";
//		return metaJdbcTemplate.update(sql, new Object[]{cond.getName(),cond.getStatePath(),cond.getMenuCode(),cond.getControllerCode(),cond.getPageCode(),cond.getParent()});
	}

	public int updateMenu(SubMenu cond) {
		String sql = "update t_menu_info set name=?,state_path=?,menu_code= ?,controller_code = ?,page_code = ? ,parent= ?,menu_level=? where menu_id=?";
		return metaJdbcTemplate.update(sql, new Object[]{cond.getName(),cond.getStatePath(),cond.getMenuCode(),cond.getControllerCode(),cond.getPageCode(),cond.getParent(),cond.getMenuLevel(),cond.getMenuId()});
	}

	public String getMenuCount() {
		String sql = "select count(*) COUNT from t_menu_info";
		Map<String,Object> res =  metaJdbcTemplate.queryForMap(sql);
		return res.containsKey("COUNT")?res.get("COUNT").toString():"0";
	}
	
}
