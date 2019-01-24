package com.genauth.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.genauth.sys.config.MySessionListener;
import com.genauth.sys.config.MyWebSocket;
import com.genauth.sys.entity.RoleBean;
import com.genauth.sys.entity.UserBean;
import com.genauth.sys.service.SubMenuService;
import com.genauth.sys.service.UserService;
import com.genauth.sys.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/userManage")
public class UserManageAction {
	@Resource
	private UserService userService;
	@Resource
	private SubMenuService subMenuService;

	private Logger logger = Logger.getLogger(UserManageAction.class);

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@ResponseBody
	@RequestMapping(value = "/userLogin.do", produces = "application/json;charset=UTF-8")
	public Object userLogin(HttpServletRequest request, UserBean user) throws Exception {
		logger.info("UserManageAction.userLogin: user.name => " + user.getUserName());
		Map<String, Object> res = new HashMap<>();
		res.put("userInfo", new UserBean());
		if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getUserPassword())) {
			res.put("error", "用户名或密码不能为空");
			return res;
		} else {
			user.setUserName(URLDecoder.decode(user.getUserName(), "UTF-8"));
			List<UserBean> users = userService.userLogin(user);
			if (users.size() == 0) {
				res.put("error", "用户名或密码不正确");
				return res;
			} else if (users.size() > 1) {
				res.put("error", "用户信息不正确");
				return res;
			} else {
				UserBean cuser = users.get(0);

				request.getSession().setAttribute("currentUser", cuser);
				// 用来统计系统所有登陆用户
				Map<String, Object> loginUsers = MySessionListener.getAllSession();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if (loginUsers.containsKey(request.getSession().getId())) {
					Map<String, Object> loginUser = (Map<String, Object>) loginUsers.get(request.getSession().getId());
					sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
					loginUser.put("loginTime", sdf.format(new Date()));
					loginUser.put("loginIP", request.getRemoteAddr());
				}
				res.put("userInfo", cuser);
				res.put("error", "");
			}
			return res;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/userLogout.do", produces = "application/json;charset=UTF-8")
	public Object userLogout(HttpServletRequest request) throws Exception {
		logger.info("UserManageAction.userLogout: ");
		request.getSession().removeAttribute("currentUser");
		return new JSONObject();
	}

	@ResponseBody
	@RequestMapping(value = "/updateMenu.do", produces = "application/json;charset=UTF-8")
	public Object updateMenu(UserBean user) throws Exception {
		CommonUtils.decodePara(user);
		logger.info("UserManageAction.updateMenu: user = " + JSON.toJSONString(user));
		Map<String, Object> res = new HashMap<>();
		int result = userService.updateMenu(user);
		if (result > 0) {
			res.put("msg", "修改成功,重新登陆时将生效");
		}
		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/updateUR.do", produces = "application/json;charset=UTF-8")
	public Object updateUR(String userId, String roles) throws Exception {
		if(StringUtils.isNotEmpty(roles)){
			roles = URLDecoder.decode(roles,"UTF-8");
		}
		logger.info("UserManageAction.updateUR: userId => " + userId + ";roles=>" + roles);
		int res = userService.updateUR(userId, roles);
		return res;
	}

	@ResponseBody
	@Transactional(transactionManager="metaTransactionManager")
	@RequestMapping(value = "/updateRA.do", produces = "application/json;charset=UTF-8")
	public Object updateRA(String role, String menus, String pages) throws Exception {
		if (StringUtils.isNotEmpty(menus))
			menus = java.net.URLDecoder.decode(menus, "UTF-8");
		if (StringUtils.isNotEmpty(pages))
			pages = java.net.URLDecoder.decode(pages, "UTF-8");
		logger.info("UserManageAction.updateRA: role => " + role + ";menus => " + menus + ";pages => " + pages);
		// 删除角色所有权限。
		userService.deleteRA(role);
		// 重新插入角色所有权限
		int res = 0;
		if (StringUtils.isNotEmpty(menus) && !("|").equals(menus)) {// 所选菜单非空
			String[] allMenu = menus.split("\\|");
			for (String raMenu : allMenu) {
				if (StringUtils.isNotEmpty(raMenu)) {
					//menuInfo=" {{ menu.menuId+','+menu.menuCode+','+menu.controllerCode }}"
					String raMenuId = raMenu.split(",")[0];
					String raMenuCode = raMenu.split(",")[1];
					String raMenuControllerCode = raMenu.split(",")[2];
					StringBuilder raPage = new StringBuilder();
					if (StringUtils.isNotEmpty(pages) && !("|").equals(pages)) {// 所选页面权限非空
						for (String page : pages.split("\\|")) {
							if (StringUtils.isNotBlank(page) && page.startsWith(raMenuCode)) {
								raPage.append(page).append("|");
							}
						}
					}
					res += userService.insertRA(role, raMenuId, raMenuCode, raMenuControllerCode, raPage.toString());
				}
			}
		}
		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/getCurrentUser.do", produces = "application/json;charset=UTF-8")
	public Object getCurrentUser(HttpServletRequest request, String fromState, String toState) throws Exception {
		logger.info("UserManageAction.getCurrentUser: fromState => " + fromState + ";toState =>" + toState);
		Map<String, Object> res = new HashMap<>();
		UserBean user = (UserBean) request.getSession().getAttribute("currentUser");
		logger.info("UserManageAction.getCurrentUser: currentUser => " + JSON.toJSONString(user));
		if (null == user) {
			res.put("userInfo", "");
			res.put("error", "no User");
		} else {
			res.put("userInfo", user);
			res.put("error", "");
			// 每次页面跳转都会请求过来获取登陆用户
			// 用来统计系统所有登陆用户
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String, Object> loginUsers = MySessionListener.getAllSession();
			if (loginUsers.containsKey(request.getSession().getId())) {
				Map<String, Object> loginUser = (Map<String, Object>) loginUsers.get(request.getSession().getId());
				sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
				List<String> operations = new ArrayList<>();
				if (loginUser.containsKey("operations")) {
					operations = (List<String>) loginUser.get("operations");
				}
				operations.add(fromState + "=>" + toState + ";" + sdf.format(new Date()));
				loginUser.put("operations", operations);
				//发送websocket日志信息
				MyWebSocket.sendInfo("前台操作日志===》用户 "+user.getUserName()+" 从页面 "+fromState+" 跳转到  "+toState);
			}
		}
		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/queryUser.do", produces = "application/json;charset=UTF-8")
	public Object queryUser(HttpServletRequest request, String userName) throws Exception {
		// post请求过来的中文， 经过config.js里的http配置已经进行了UTF-8转码，这是还是要解码下。
		if (StringUtils.isNotEmpty(userName)) {
			userName = java.net.URLDecoder.decode(userName, "UTF-8");
		}
		logger.info("UserManageAction.queryUser: userName => " + userName);
		List<UserBean> res = new ArrayList<>();
		res = userService.queryUserByName(userName);
		logger.info("UserManageAction.queryUser: " + res.size());
		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/queryRole.do", produces = "application/json;charset=UTF-8")
	public Object queryRole() throws Exception {
		logger.info("UserManageAction.queryRole:");
		List<RoleBean> res = new ArrayList<>();
		res = userService.queryRole();
		logger.info("UserManageAction.queryRole: " + res.size());
		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteUser.do", produces = "application/json;charset=UTF-8")
	public Object deleteUser(String userId) throws Exception {
		Map<String, Object> res = new HashMap<>();
		logger.info("UserManageAction.deleteUser: userId => " + userId);
		int result = userService.deleteUser(userId);
		if (result > 0) {
			res.put("msg", "用户删除成功");
		}
		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteRole.do", produces = "application/json;charset=UTF-8")
	public Object deleteRole(String roles) throws Exception {
		Map<String, Object> res = new HashMap<>();
		logger.info("UserManageAction.deleteRole: roles => " + roles);
		int result = 0;
		for (String roleId : roles.split("|")) {
			if (StringUtils.isNotEmpty(roleId)) {
				result += userService.delelteRole(roleId);
			}
		}
		if (result > 0) {
			res.put("msg", "成功删除" + result + "个角色");
		}
		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/modifyUser.do", produces = "application/json;charset=UTF-8")
	public Object modifyUser(UserBean user, Boolean resetPass) throws Exception {
		if (StringUtils.isNotEmpty(user.getUserName())) {
			user.setUserName(java.net.URLDecoder.decode(user.getUserName(), "UTF-8"));
		}
		if (StringUtils.isNotEmpty(user.getUserRealname())) {
			user.setUserRealname(java.net.URLDecoder.decode(user.getUserRealname(), "UTF-8"));
		}
		logger.info("UserManageAction.modifyUser: userId => " + user.getUserId() + ";userName => " + user.getUserName()
				+ ";resetPass => " + resetPass);
		Map<String, Object> res = new HashMap<>();
		int result;
		if (StringUtils.isEmpty(user.getUserId())) {
			try {
				result = userService.addUser(user);
				if (result > 0) {
					res.put("code", "1");
					res.put("msg", "用户新增成功");
				}
			} catch (Exception e) {
				logger.warn("用户新增失败;" + e);
				res.put("msg", "用户新增失败;请确认没有重复用户或联系管理员");
			}
		} else {
			try {
				if (null != resetPass && resetPass) {
					result = userService.updateUserAndResetPass(user);
				} else {
					result = userService.updateUser(user);
				}
				if (result > 0) {
					res.put("code", "1");
					res.put("msg", "用户修改成功");
				}
			} catch (Exception e) {
				logger.warn("用户修改失败;" + e);
				res.put("msg", "用户修改失败" + e.getMessage());
			}
		}
		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/modifyRole.do", produces = "application/json;charset=UTF-8")
	public Object modifyRole(RoleBean role) throws Exception {
		if (StringUtils.isNotEmpty(role.getRoleName())) {
			role.setRoleName(java.net.URLDecoder.decode(role.getRoleName(), "UTF-8"));
		}
		logger.info("UserManageAction.modifyRole: roleCode => " + role.getRoleCode() + ";roleName => "
				+ role.getRoleName() + ";");
		Map<String, Object> res = new HashMap<>();
		int result;
		try {
			result = userService.addRole(role);
			if (result > 0) {
				res.put("code", "1");
				res.put("msg", "角色修改成功");
			}
		} catch (Exception e) {
			logger.warn("角色新增失败", e);
			res.put("msg", "角色新增失败" + e.getMessage());
		}
		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/changePassword.do", produces = "application/json;charset=UTF-8")
	public Object changePassword(String userId, String oldPassword, String newPassword) throws Exception {
		logger.info("usermanage.changePassword: userId => " + userId + ";oldPassword =>" + oldPassword
				+ ";newPassword => " + newPassword);
		Map<String, String> res = new HashMap<>();
		int result = userService.updateUserPassword(userId, oldPassword, newPassword);
		if (result == 0) {
			res.put("code", "0");
			res.put("msg", "密码错误。");
		} else {
			res.put("code", "1");
			res.put("msg", "密码修改完成");
		}
		logger.info("usermanage.changePassword: res =>" + res.get("code") + ":" + res.get("msg"));
		return res;
	}

	/**
	 * 加载所有的菜单。这个东东变化很少，加个缓存。
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@Cacheable(value = "sysCache", key = "'user.querySubMenu'")
	@RequestMapping(value = "/querySubMenu.do", produces = "application/json;charset=UTF-8")
	public Object querySubMenu() throws Exception {
		logger.info("UserManageAction.querySubMenu: ");
		List<Map<String, Object>> res = userService.querySubMenu();
		logger.info("UserManageAction.querySubMenu: res = " + JSONArray.toJSONString(res));
		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/moveMenu.do", produces = "application/json;charset=UTF-8")
	public Object moveMenu() throws Exception {
		logger.info("UserManageAction.moveMenu");
		Map<String, Object> opRes = userService.moveMenu();
		String res = "共 " + opRes.get("userNum") + "	 个用户，共插入 " + opRes.get("authNum") + " 条权限数据";
		return res;
	}

	@RequestMapping(value = "/getMenuCount.do", produces = "application/json;charset=UTF-8")
	@Cacheable(value = "sysCache")
	public Object getMenuCount() throws Exception {
		logger.info("UserManageAction.getMenuCount");
		String menuCount = subMenuService.getMenuCount();
		Map<String, Object> res = new HashMap<>();
		res.put("menucount", menuCount);
		return res;
	}
}
