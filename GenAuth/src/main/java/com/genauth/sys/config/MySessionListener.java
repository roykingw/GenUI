package com.genauth.sys.config;

import com.alibaba.fastjson.JSONObject;
import com.genauth.sys.entity.UserBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计系统登陆用户的监听器
 *
 * @author roykingw
 *         接口：ServletContextListener  j2ee容器启动时，会为每个应用创建一个ServletContext对象，这个接口可监听ServletContext创建和销毁事件。
 *         接口：HttpSessionAttributeListener 监听session的attrbute变化事件
 */
@WebListener
public class MySessionListener implements HttpSessionAttributeListener, HttpSessionListener {
    private Logger logger = Logger.getLogger(MySessionListener.class);
    /*
     * 用户量不大，可以简单用内存管理所有用户。用户量大了再改用
     * allSession 数据结构[{String sessionId,Map<String,Object> contextInfo}]
     * contextInfo 数据结构[{"session":session,"user":userBean,"loginTime":loginTime,"loginIP":loginIP,"operation":List<String>(fromState+"=>"+toState+";"+sdf.format(new Date())) ]
     */
    private static Map<String, Object> allSession = new HashMap<String, Object>();

    /**
     * HttpSessionListener 监听客户浏览器与服务器的session建立与销毁  只要访问了服务器就会建立session
     */
    @Override
    public void sessionCreated(HttpSessionEvent env) {
        logger.debug("建立访问连接 建立session " + env.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent env) {
        logger.debug("session 超时  ：" + env.getSession().getId());
        // session失效时(比如session超时)
        if (allSession.containsKey(env.getSession().getId())) {
            logger.debug("监控到用户登陆超时事件 userTimeOut：" + formatContextInfo(allSession.get(env.getSession().getId())));
            allSession.remove(env.getSession().getId());
        }
    }

    /**
     * HttpSessionAttributeListener 监听session中的参数变动事件。即 session.setAttrbute 和 session.removeAttribute
     */
    @Override
    public void attributeAdded(HttpSessionBindingEvent env) {
        // 统计登陆用户只要监听登陆时往session中保存当前用户时的事件
        String attrName = env.getName();
        Object value = env.getValue();
        logger.debug("监听到 session.setAttribute 事件；attrName = " + attrName + ";value = " + JSONObject.toJSONString(value));
        if (StringUtils.isNotEmpty(attrName) && "currentUser".equals(attrName) && value instanceof UserBean) {
            HttpSession session = env.getSession();
            UserBean user = (UserBean) value;
            //TODO 需要增加逻辑，一个账号只能一个人登录。把前面那个人踢掉。找到session，把session失效掉。
            if (!allSession.containsKey(session.getId())) {
                Map<String, Object> contextInfo = new HashMap<String, Object>();
                contextInfo.put("session", session);
                contextInfo.put("user", user);
                allSession.put(session.getId(), contextInfo);
            }
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent env) {
        // logout中session.removeAttribute
        String attrName = env.getName();
        logger.debug("监听到 session.removeAttribute 事件；attrName = " + attrName);
        if (StringUtils.isNotEmpty(attrName) && "currentUser".equals(attrName) && allSession.containsKey(env.getSession().getId())) {
            logger.debug("监控到用户登出事件 userLogout：" + formatContextInfo(allSession.get(env.getSession().getId())));
            allSession.remove(env.getSession().getId());
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent env) {
        // 登陆后重复登陆时更新session信息
        String attrName = env.getName();
        Object value = env.getValue();
        logger.debug("监听到 session.setAttribute 事件；attrName = " + attrName + ";value = " + value);
        if (StringUtils.isNotEmpty(attrName) && "currentUser".equals(attrName) && value instanceof UserBean && allSession.containsKey(env.getSession().getId())) {
            HttpSession session = env.getSession();
            UserBean user = (UserBean) value;
            Map<String, Object> contextInfo = new HashMap<String, Object>();
            contextInfo.put("session", session);
            contextInfo.put("user", user);
            allSession.put(session.getId(), contextInfo);
        }

    }

    public static Map<String, Object> getAllSession() {
        synchronized (allSession) {
            return allSession;
        }
    }

    private String formatContextInfo(Object objcontextInfo) {
        try {
            Map<String, Object> contextInfo = (Map<String, Object>) objcontextInfo;
            StringBuffer sb = new StringBuffer();
            UserBean user = contextInfo.containsKey("user") ? (UserBean) contextInfo.get("user") : null;
            String loginTime = contextInfo.containsKey("loginTime") ? (String) contextInfo.get("loginTime") : null;
            String loginIP = contextInfo.containsKey("loginIP") ? (String) contextInfo.get("loginIP") : null;
           // HttpSession session = contextInfo.containsKey("session") ? (HttpSession) contextInfo.get("session") : null;
            List<String> operations = contextInfo.containsKey("operations") ? (List<String>) contextInfo.get("operations") : new ArrayList<String>();
            sb.append("用户：{ userId:" + user.getUserId() + ";userName:" + user.getUserName() + ";userRealName:" + user.getUserRealname() + "}"
                    + ";登陆时间:" + loginTime + ";登陆IP:" + loginIP + ";活动轨迹为：");
            for (String operation : operations) {
                sb.append(operation + ";");
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }

    }

}
