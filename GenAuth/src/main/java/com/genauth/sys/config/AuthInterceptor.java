package com.genauth.sys.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.genauth.sys.entity.UserBean;

@Repository
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = Logger.getLogger(AuthInterceptor.class);

    @Resource
    private AuthConfig authConfig;
    
    public void setAuthConfig(AuthConfig authConfig) {
        this.authConfig = authConfig;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ignoreControllers = authConfig.getIgnoreControllers();
        logger.debug("开始验证权限 ; ignoreControllers: " + ignoreControllers + ";request.getRequestURI():" + request.getRequestURI());
        UserBean currentUser = (UserBean) request.getSession().getAttribute("currentUser");
        Map<String, Object> resCode = new HashMap<String, Object>();
        String requestURI = request.getRequestURI();
        
        
        
        //登陆验证
        for (String ignoreController : ignoreControllers.split(",")) {
            if (StringUtils.isNotEmpty(ignoreController) &&
                    (requestURI.indexOf("/" + ignoreController + "/") >= 0) ||
                    (requestURI.endsWith("/" + ignoreController))) {
                logger.info("验证权限：" + request.getRequestURI() + ";res=白名单通过");
                return true;
            }
        }
        //后台权限验证,防止绕过前台直接访问后台接口套取数据的情况
        Boolean res = true;
        if (null == currentUser) { //没有登陆 
            logger.info("验证权限：" + request.getRequestURI() + ";res=未登陆");
            resCode.put("errorCode", 1);
            resCode.put("error", "需要登陆才能访问");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSONObject.toJSONString(resCode));
			//目前系统的前台采用Angularjs，后台部分只负责提供数据， 不负责进行页面跳转。因此后台权限只要返回错误信息就行了。
//            response.sendRedirect(request.getContextPath()+"/#/login?error=1");
            res = false;
        }else {
        	logger.info("验证权限："+request.getRequestURI() +";用户ControllerCode："+currentUser.getUserControllercode()+"已登录，验证策略未定");
//        	以请求路径的第一部分(一般为Action类上配置的requestMapping)为controllercode，配置到menu中，作为权限标识。适合于一个页面对应一个后台Action的方式，约定大于配置。
//            String servletPath = request.getServletPath();
//            String controllercode = servletPath.substring(1);
//            controllercode = controllercode.substring(0, controllercode.indexOf("/"));
//            if (currentUser.getUserControllercode().indexOf("|" + controllercode + "|") < 0) {//无权限
//                logger.info("验证权限：" + request.getRequestURI() + ";controllercode = " + controllercode + ";res=无权限");
//                resCode.put("errorCode", 2);
//                resCode.put("error", "没有访问权限，请联系管理员");
//                response.setCharacterEncoding("UTF-8");
//                response.getWriter().write(JSONObject.toJSONString(resCode));
////				response.sendRedirect(request.getContextPath()+"/#/login?error=2");
//                res = false;
//            } else {
//                logger.info("验证权限：" + request.getRequestURI() + ";controllercode = " + controllercode + ";res=通过");
//            }
        }
        return res;
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
//		logger.info("afterCompletion:请求路径 "
//			+arg0.getRequestURI() +",时间花费:"
//			+((new Date()).getTime()
//			-costs.get())+" 毫秒");
    }


}
