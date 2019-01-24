package com.genauth.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.genauth.sys.dao.UserDao;
import com.genauth.sys.entity.AuthBean;
import com.genauth.sys.entity.RoleBean;
import com.genauth.sys.entity.SubMenu;
import com.genauth.sys.entity.UserBean;
import com.genauth.sys.util.CommonUtils;

@Service
public class UserService {
	@Resource
	private UserDao userdao;

	private Logger logger = Logger.getLogger(this.getClass());
	public void setUserdao(UserDao userdao) {
		this.userdao = userdao;
	}
	
	public List<UserBean> userLogin(UserBean user) throws Exception{
		logger.info("UserService.userLogin: user => "+user.toString());
		List<UserBean> users = new ArrayList<UserBean>();
		users = userdao.userlogin(user);
		return users;
	}
	
	public List<UserBean> queryUserByName(String userName) {
		List<UserBean> users = new ArrayList<UserBean>();
//		if(StringUtils.isNotEmpty(userName)){
			users = userdao.queryUserInfoByName(userName);
//		}else{
//			users = userdao.getAllUserInfo();
//		}
		
		return users;
	}
	
	public List<RoleBean> queryRole() {
		List<RoleBean> roles = new ArrayList<>();
		roles = userdao.queryRole();
		return roles;
	}
	
	public List<AuthBean> queryAuthByUserName(String userName){
		return userdao.queryAuthByUserName(userName);
	}
	
	public List<Map<String,Object>> querySubMenu(){
		List<SubMenu> allMenu = userdao.querySubMenu();
		
		List<Map<String,Object>> res = new ArrayList<>();
		//Stream里面嵌套for循环。感觉怪怪的。
		CommonUtils.beanList2Map(allMenu).stream()
		.filter(menu -> "1".equals(menu.get("menuLevel")) && ("root").equals(menu.get("parent")))
		.forEach(menu->{
			List<SubMenu> subMenus = new ArrayList<>();
			for(SubMenu sMenu:allMenu){
				if(StringUtils.isNotEmpty(sMenu.getParent()) && sMenu.getParent().equals(menu.get("menuCode"))){
					subMenus.add(sMenu);
				}
			}
			menu.put("subMenus", CommonUtils.beanList2Map(subMenus));
			res.add(menu);
		});
		return res;
	}

	public int updateMenu(UserBean user) {
		int opRes = 0;
//		String menuCodes = user.getUserMenucode();
//		for(String menuCode:menuCodes.split("\\|")){
//			if(StringUtils.isNotEmpty(menuCode)){
//				AuthBean ab = new AuthBean();
//				ab.setAuthCode(user.getUserName());
//				ab.setAuthType("U");
//				ab.setMenuCode(menuCode);
//				if(user.getUserPagecode().indexOf("|"+menuCode+"|")>=0){
//					ab.setPageCode(menuCode);
//				}
//				ab.setControllerCode(userdao.queryControllerByMenuCode(menuCode));
//				opRes += userdao.updateAuth(ab);
//			}
//		}
		return opRes;
	}

	public int deleteUser(String userId) {
		//删除用户角色对应关系
		userdao.deleteUserRole(userId);
		//删除用户信息 
		return userdao.delete(userId);
	}

	public int addUser(UserBean user) throws Exception {
		return userdao.addUser(user);
	}

	public int updateUser(UserBean user) throws Exception {
		// TODO Auto-generated method stub
		return userdao.updateUser(user);
	}

	public int updateUserPassword(String userId, String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		return userdao.updateUserPassword(userId,oldPassword,newPassword);
	}

	public int updateUserAndResetPass(UserBean user) {
		return userdao.updateUserAndResetPass(user);
	}

	public Map<String, Object> moveMenu() {
		Map<String,Object> res = new HashMap<String,Object>();
//		// TODO Auto-generated method stub
//		List<Map<String,Object>> allmenu = userdao.querySubMenu();
//		List<UserBean> allUser = userdao.getAllUser();
//		int opRes = 0;
//		for(UserBean user : allUser){
//			String menuCodes = user.getUserMenucode();
//			for(String menuCode:menuCodes.split("\\|")){
//				if(StringUtils.isNotEmpty(menuCode)){
//					AuthBean ab = new AuthBean();
//					ab.setAuthCode(user.getUserName());
//					ab.setAuthType("U");
//					ab.setMenuCode(menuCode);
//					if(user.getUserPagecode().indexOf("|"+menuCode+"|")>=0){
//						ab.setPageCode(menuCode);
//					}
//					ab.setControllerCode(userdao.queryControllerByMenuCode(menuCode));
//					opRes += userdao.insertAuth(ab);
//				}
//			}
//		}
//		res.put("userNum", allUser.size());
//		res.put("authNum", opRes);
		return res;
	}

	public int updateUR(String userId, String roles) {
		//先删除用户所有的角色对应关系
		userdao.deleteUserRole(userId);
		int res = 0 ; 
		//逐行添加用户角色对应关系
		String[] roleInfos = roles.split("\\|");
		for(int i = 0 ; i < roleInfos.length; i ++){
			String role = roleInfos[i];
			if(StringUtils.isNotEmpty(role)){
				res += userdao.updateUR(userId, role);
			}
		}
		return res;
	}

	public int addRole(RoleBean role) {
		return userdao.addRole(role);
	}

	public int delelteRole(String roleId) {
		//删除角色信息 
		return userdao.deleteRole(roleId);
	}

	public void deleteRA(String roleId) {
		userdao.deleteRA(roleId);
	}

	public int insertRA(String role,String raMenuId,String raMenuCode,String raMenuControllerCode,String raPage) {
		return userdao.insertRA(role,raMenuId,raMenuCode,raMenuControllerCode,raPage);
	}
}
