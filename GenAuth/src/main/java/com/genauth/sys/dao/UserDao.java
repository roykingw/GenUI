package com.genauth.sys.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.genauth.app.util.IdGen;
import com.genauth.sys.entity.AuthBean;
import com.genauth.sys.entity.RoleBean;
import com.genauth.sys.entity.SubMenu;
import com.genauth.sys.entity.UserBean;

@Repository
public class UserDao extends MetaCommonDao {

	private Logger logger = Logger.getLogger(UserDao.class);
	
	public List<UserBean> userlogin(UserBean user) {
		String sql = "select * from t_user_info where user_name=? and user_password=?";
		List<UserBean> queryRes = metaJdbcTemplate.query(sql, new Object[] { user.getUserName(), user.getUserPassword() },
				new BeanPropertyRowMapper<UserBean>(UserBean.class));
		if(queryRes.size()>0){
			this.addUserInfo(queryRes);
		}
		return queryRes;
	}

	public List<UserBean> getAllUser() {
		String sql = "select user_id,user_name,user_realname,user_menucode,user_pagecode,user_controllercode from t_user_info";
		return metaJdbcTemplate.query(sql, new BeanPropertyRowMapper<UserBean>(UserBean.class));
	}

//	public List<UserBean> getAllUserInfo() {
//		String sql = "select user_id,user_name,user_realname from t_user_info";
//		return metaJdbcTemplate.query(sql, new BeanPropertyRowMapper<UserBean>(UserBean.class));
//	}

	public List<UserBean> queryUserInfoByName(String userName) {
		String sql = "select user_id,user_name,user_realname from t_user_info where 1=1";
		List<UserBean> res = new ArrayList<>();
		if(StringUtils.isNotEmpty(userName)){
			sql += " and user_realname like ?";
			res =  metaJdbcTemplate.query(sql, new BeanPropertyRowMapper<UserBean>(UserBean.class),new Object[]{"%"+userName+"%"});
		}else{
			res =  metaJdbcTemplate.query(sql, new BeanPropertyRowMapper<UserBean>(UserBean.class));
		}
		addUserInfo(res);
		return res;
	}
	
//	//Activiti User
//	public User queryUserById(String userId) {
//		String sql = "select user_id,user_name,user_realname from t_user_info where user_id=?";
//		UserBean userBean = metaJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<UserBean>(UserBean.class),new Object[]{userId});
//		return ActUtil.toActivitiUser(userBean);
//	}
	
	//给用户补充权限相关的信息
	private void addUserInfo(List<UserBean> users){
		for(UserBean user:users){
			StringBuffer sbRoles = new StringBuffer("|");
			StringBuffer sbMenuCode = new StringBuffer("|");
			StringBuffer sbControllerCode = new StringBuffer("|");
			StringBuffer sbPageCode = new StringBuffer("|");
			
			String URSQL = "select t.user_id,t.role_id,ra.menu_id,ra.menu_code,ra.controller_code,ra.page_code from ("+
					"select u.user_id,ur.role_id"+
					"  from t_user_info u , t_user_role ur"+
					" where u.user_id = ur.user_id) t"+
					" left join t_role_auth ra on t.role_id=ra.role_id"+
					" where t.user_id=?";
			List<Map<String,Object>> queryRes = metaJdbcTemplate.queryForList(URSQL, new Object[]{user.getUserId()});
			for(Map<String,Object>  userInfo : queryRes){
				//所属角色
				if(!ObjectUtils.isEmpty(userInfo.get("role_id")) && sbRoles.indexOf("|"+userInfo.get("role_id")+"|")<0){
					sbRoles.append(userInfo.get("role_id")).append("|");
				}
				//菜单权限
				if(!ObjectUtils.isEmpty(userInfo.get("menu_code")) && sbMenuCode.indexOf("|"+userInfo.get("menu_code")+"|")<0){
					sbMenuCode.append(userInfo.get("menu_code")).append("|");
				}
				//后台请求权限
				if(!ObjectUtils.isEmpty(userInfo.get("controller_code")) && sbControllerCode.indexOf("|"+userInfo.get("controller_code")+"|")<0){
					sbControllerCode.append(userInfo.get("controller_code")).append("|");
				}
				//页面控件权限
				if(!ObjectUtils.isEmpty(userInfo.get("page_code")) && sbPageCode.indexOf("|"+userInfo.get("page_code")+"|")<0){
					sbPageCode.append(userInfo.get("page_code")).append("|");
				}
				
			}
			
			user.setUserRoles(sbRoles.toString());
			user.setUserMenucode(sbMenuCode.toString());
			user.setUserControllercode(sbControllerCode.toString());
			user.setUserPagecode(sbPageCode.toString());
		}
	}

	public List<UserBean> queryUserByName(String userName) {
		String sql = "select user_id,user_name,user_realname,user_menucode,user_pagecode,user_controllercode from t_user_info where user_realname like '%"+userName+"%'";
		return metaJdbcTemplate.query(sql, new BeanPropertyRowMapper<UserBean>(UserBean.class));
	}

	public int updateMenu(UserBean user) {
//		String sql = "update t_user_info set user_menucode=?,user_pagecode=?,user_controllercode=? where user_id = ?";
//		return metaJdbcTemplate.update(sql,new Object[]{user.getUserMenucode(),user.getUserPagecode(),user.getUserControllercode(),user.getUserId()});
		return 1;
	}

	public int delete(String userId) {
		String sql = "delete from t_user_info where user_id = ?";
		return metaJdbcTemplate.update(sql,new Object[]{userId});
	}

	public int addUser(UserBean user) throws Exception {
		String sql = "insert into t_user_info(user_id,user_name,user_realname,user_password) values(?,?,?,?)";
		return metaJdbcTemplate.update(sql,new Object[]{IdGen.uuid(),user.getUserName(),user.getUserRealname(),"123456"});
	}

	public int updateUser(UserBean user) throws Exception {
		String sql = "update t_user_info set user_name = ? ,user_realname=? where user_id=?";
		return metaJdbcTemplate.update(sql,new Object[]{user.getUserName(),user.getUserRealname(),user.getUserId()});
	}

	public int updateUserPassword(String userId, String oldPassword, String newPassword) {
		String sql = "update t_user_info set user_password = ? where user_id=? and user_password=?";
		return metaJdbcTemplate.update(sql,new Object[]{newPassword,userId,oldPassword});
	}

	public int updateUserAndResetPass(UserBean user) {
		String sql = "update t_user_info set user_name = ? ,user_realname=?,user_password=? where user_id=?";
		return metaJdbcTemplate.update(sql,new Object[]{user.getUserName(),user.getUserRealname(),"123456",user.getUserId()});
	}
	
	public List<SubMenu> querySubMenu() {
		String sql = "select * from t_menu_info order by menu_id";
		return metaJdbcTemplate.query(sql,new BeanPropertyRowMapper<SubMenu>(SubMenu.class));
	}

	//暂时不用了。这里权限已经改造成由角色关联权限。
	public int insertAuth(AuthBean ab) {
		String sql = "insert into user_auth(auth_code,menu_code,controller_code,page_code,auth_type) values(?,?,?,?,?)";
		return metaJdbcTemplate.update(sql, new Object[]{ab.getAuthCode(),ab.getMenuCode(),ab.getControllerCode(),ab.getPageCode(),ab.getAuthType()});
	}

	//暂时不用了。这里权限已经改造成由角色关联权限。
	public List<AuthBean> queryAuthByUserName(String userName) {
		String sql = "select auth_code,menu_code,controller_code,page_code,auth_type from t_user_auth where auth_type='U' and auth_code = ?";

		return metaJdbcTemplate.query(sql,new String[]{userName}, new BeanPropertyRowMapper<AuthBean>(AuthBean.class));
	}

	public String queryControllerByMenuCode(String menuCode) {
		String res = "";
		String sql = "select controllerCode from t_menu_info where menuCode=?";
		try{
			Map<String,Object> opRes = metaJdbcTemplate.queryForMap(sql, new Object[]{menuCode});
			if(null != opRes && opRes.size()>0 && null != opRes.get("controllerCode")){
				res = opRes.get("controllerCode").toString();
			}
		}catch(EmptyResultDataAccessException e){
		}
		return res;
	}

	//暂时不用了。这里权限已经改造成由角色关联权限。
	public int updateAuth(AuthBean ab) {
		int res = 0 ;
		String updateSql = "update t_user_auth set controller_code = ? ,page_code = ? where auth_code = ? and menu_code = ? ";
		String insertSql = "insert into t_user_auth(auth_code,menu_code,controller_code,page_code,auth_type) values(?,?,?,?,?)";
		res = metaJdbcTemplate.update(updateSql,new Object[]{ab.getControllerCode(),ab.getPageCode(),ab.getAuthCode(),ab.getMenuCode()});
		if(res < 1){
			res = metaJdbcTemplate.update(insertSql, new Object[]{ab.getAuthCode(),ab.getMenuCode(),ab.getControllerCode(),ab.getPageCode(),ab.getAuthType()});
		}
		return res;
	}

	public List<RoleBean> queryRole() {
		List<RoleBean> res = new ArrayList<>();
		String sql ="select role_id,role_code,role_name from t_role_info";
		res = metaJdbcTemplate.query(sql, new BeanPropertyRowMapper<RoleBean>(RoleBean.class));
		
		//查询用户所属的角色及权限信息
		for(RoleBean role:res){
			StringBuffer sbMenuCode = new StringBuffer("|");
			StringBuffer sbControllerCode = new StringBuffer("|");
			StringBuffer sbPageCode = new StringBuffer("|");
			
			String URSQL = "select ra.role_id,ra.menu_id,ra.menu_code,ra.controller_code,ra.page_code"+
					" from t_role_auth ra where ra.role_id=?";
			List<Map<String,Object>> queryRes = metaJdbcTemplate.queryForList(URSQL, new Object[]{role.getRoleId()});
			for(Map<String,Object>  roleInfo : queryRes){
				//菜单权限
				if(!ObjectUtils.isEmpty(roleInfo.get("menu_code"))){
					sbMenuCode.append(roleInfo.get("menu_code")).append("|");
				}
				//后台请求权限
				if(!ObjectUtils.isEmpty(roleInfo.get("controller_code"))){
					sbControllerCode.append(roleInfo.get("controller_code")).append("|");
				}
				//页面控件权限
				if(!ObjectUtils.isEmpty(roleInfo.get("page_code"))){
					sbPageCode.append(roleInfo.get("page_code")).append("|");
				}
			}
			
			role.setMenucode(sbMenuCode.toString());
			role.setControllercode(sbControllerCode.toString());
			role.setPagecode(sbPageCode.toString());
		}
		return res;
	}

	public void deleteUserRole(String userId) {
		String sql = "delete from t_user_role where user_id=?";
		metaJdbcTemplate.update(sql, new Object[]{userId});
	}

	public int updateUR(String userId, String role) {
		String sql = "insert into t_user_role(user_id,role_id)values (?,?)";
		int res = metaJdbcTemplate.update(sql, new Object[]{userId,role});
		return res;
	}

	public int addRole(RoleBean role) {
		String sql = "insert into t_role_info(role_id,role_code,role_name) values(?,?,?)";
		return metaJdbcTemplate.update(sql, new Object[]{IdGen.uuid(),role.getRoleCode(),role.getRoleName()});
//		String sql = "insert into t_role_info(role_code,role_name) values(?,?)";
//		return metaJdbcTemplate.update(sql, new Object[]{role.getRoleCode(),role.getRoleName()});
	}

	@Transactional
	public int deleteRole(String roleId) {
		try{
			//删除用户角色对应关系
			String sql = "delete from t_user_role where role_id=?";
			metaJdbcTemplate.update(sql, new Object[]{roleId});
			//删除角色权限对应关系
			sql = "delete from t_role_auth where role_id=?";
			metaJdbcTemplate.update(sql, new Object[]{roleId});
			//删除角色
			sql = "delete from t_role_info where role_id=?";
			return metaJdbcTemplate.update(sql, new Object[]{roleId});
		}catch(Exception e){
			logger.warn("删除角色(roleId=>"+roleId+")失败",e);
			return 0;
		}
	}

	public void deleteRA(String roleId) {
		//删除用户角色对应关系
		String sql = "delete from t_role_auth where role_id=?";
		metaJdbcTemplate.update(sql, new Object[]{roleId});
	}

	public int insertRA(String role,String raMenuId,String raMenuCode,String raMenuControllerCode,String raPage) {
		String sql = "insert into t_role_auth(role_id,menu_id,menu_code,controller_code,page_code)values(?,?,?,?,?)";
		return metaJdbcTemplate.update(sql, new Object[]{role,raMenuId,raMenuCode,raMenuControllerCode,raPage});
	}


	//==============
	
	public List<RoleBean> queryRoleByUserId(String userId) {
		String sql ="select role_id,role_code,role_name from t_role_info r where exists(select 1 from t_user_role u where u.role_id=r.role_id and r.user_id = ? )";
		List<RoleBean> queryRes = metaJdbcTemplate.query(sql, new BeanPropertyRowMapper<RoleBean>(RoleBean.class), new Object[]{userId});
		return queryRes;
	}
}
