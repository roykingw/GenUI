package com.genauth.sys.config;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.genauth.sys.entity.UserBean;

@Repository
public class ActionLogInterceptor implements HandlerInterceptor {

	private Logger logger = Logger.getLogger(this.getClass());
	private ThreadLocal<Long> timer = new ThreadLocal<Long>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Date now = new Date();
		timer.set(now.getTime());
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception arg3)
			throws Exception {
		Date now = new Date();
		Long spendTime = now.getTime() - timer.get();
		logger.info("访问后台功能: " + request.getRequestURI() + ";花费 " + spendTime + " 毫秒");
		UserBean currentUser = (UserBean) request.getSession().getAttribute("currentUser");
		new Thread(()->{
        	try{
    			logger.debug("发送websocket消息");
    			String name = currentUser == null? "匿名用户":currentUser.getUserName();
    			MyWebSocket.sendInfo("后台功能日志：用户 ："+name+";IP地址："+request.getRemoteAddr()+";访问后台功能: " + request.getRequestURI() + ";耗时 " + spendTime + " 毫秒");
    		}catch(Exception e){
    			logger.error("发送websocket消息失败",e);
    		}
        }).run();
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView arg3)
			throws Exception {
		}
}
