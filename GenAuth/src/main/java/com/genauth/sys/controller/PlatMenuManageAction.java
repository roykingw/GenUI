package com.genauth.sys.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.genauth.sys.entity.SubMenu;
import com.genauth.sys.service.SubMenuService;
import com.genauth.sys.util.CommonUtils;

@RestController
@RequestMapping(value="/PlatMenuManageAction")
public class PlatMenuManageAction {

	private Logger logger = Logger.getLogger(PlatMenuManageAction.class);
	@Resource
	private SubMenuService service;
	
	@RequestMapping(value = "/queryMenu.do",produces = "application/json;charset=UTF-8",method={RequestMethod.POST })
	public Object queryMenu(SubMenu cond) throws Exception{
		Map<String,Object> res = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(cond.getName()) && cond.getName().indexOf("%")>=0){
			cond.setName(URLDecoder.decode(cond.getName(),"UTF-8"));
		}
		logger.info("queryMenu cond => "+JSON.toJSONString(cond));
		
		List<SubMenu> queryRes = service.queryMenu(cond);
		res.put("queryRes", queryRes);
		return res;
	}
	
	@CacheEvict(value = "sysCache", key = "'user.querySubMenu'")
	@RequestMapping(value = "/deleteMenu.do",produces = "application/json;charset=UTF-8",method={RequestMethod.POST })
	public Object deleteMenu(SubMenu cond) throws Exception{
		Map<String,Object> res = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(cond.getName()) && cond.getName().indexOf("%")>=0){
			cond.setName(URLDecoder.decode(cond.getName(),"UTF-8"));
		}
		logger.info("deleteMenu cond => "+JSON.toJSONString(cond));
		int opRes = service.deleteMenu(cond);
		res.put("desc", "成功删除"+opRes+"条记录");
		return res;
	}
	
	@CacheEvict(value = "sysCache", key = "'user.querySubMenu'")
	@RequestMapping(value = "/updateMenu.do",produces = "application/json;charset=UTF-8",method={RequestMethod.POST })
	public Object updateMenu(SubMenu cond) throws Exception{
		Map<String,Object> res = new HashMap<String,Object>();
//		if(StringUtils.isNotEmpty(cond.getName()) && cond.getName().indexOf("%")>=0){
//			cond.setName(URLDecoder.decode(cond.getName(),"UTF-8"));
//		}
		CommonUtils.decodePara(cond);
		logger.info("updateMenu cond => "+JSON.toJSONString(cond));
		int opRes = 0 ;
		if(StringUtils.isNotBlank(cond.getMenuId())){
			opRes = service.update(cond);
		}else{
			opRes = service.createMenu(cond);
		}
		res.put("desc", "成功更新"+opRes+"条记录");
		return res;
	}
}
