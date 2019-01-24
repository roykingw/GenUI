package com.genauth.sys.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.genauth.sys.config.MySessionListener;
import com.genauth.sys.entity.UserBean;
import com.genauth.sys.service.UserService;
import com.genauth.sys.util.CommonUtils;

@Controller
@RequestMapping(value="/PlatUserManageAction")
public class PlatUserManageAction {
	@Resource
	private UserService userService;

	private Logger logger = Logger.getLogger(PlatUserManageAction.class);
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@ResponseBody
	@RequestMapping(value="/getLoginUsers.do",produces = "application/json;charset=UTF-8")
	public Object getLoginUsers(HttpServletRequest request) throws Exception{
		logger.info("PlatUserManageAction.getLoginUsers: ");
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		Map<String,Object> allSession = MySessionListener.getAllSession();
		for(String sessionId:allSession.keySet()){
			//HttpSession httpSession = (HttpSession)((Map<String,Object>)allSession.get(sessionId)).get("session");
			Map<String,Object> mySessionInfo = ((Map<String,Object>)allSession.get(sessionId));
			UserBean user = (UserBean)(mySessionInfo.get("user"));
			String loginTime = mySessionInfo.get("loginTime").toString();
			String loginIp = mySessionInfo.get("loginIP").toString();
			List<String> operations = (List<String>)(mySessionInfo.get("operations"));
			Map<String,Object> bean = CommonUtils.bean2Map(user);
			//bean.put("session", httpSession);
			bean.put("sessionId", sessionId);
			bean.put("loginTime", loginTime);
			bean.put("loginIp", loginIp);
			bean.put("operations", operations);
			res.add(bean);
		}
		return res;
	}
	@ResponseBody
	@RequestMapping(value="/forcelogout.do",produces = "application/json;charset=UTF-8")
	public void forcelogout(HttpServletRequest request,String sessionId) throws Exception{
		logger.info("PlatUserManageAction.forcelogout: sessionId = "+sessionId);
		Map<String,Object> allSession = MySessionListener.getAllSession();
		if(allSession.containsKey(sessionId)){
			Map<String,Object> sessionInfo = (Map<String,Object>)allSession.get(sessionId);
			HttpSession session = ((HttpSession)sessionInfo.get("session"));
			session.invalidate();
		}
	}
	
}
