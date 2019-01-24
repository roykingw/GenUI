package com.genauth.sys.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RealTimeLogController {

	//private Logger logger = Logger.getLogger(this.getClass());
	
	@ResponseBody
	@RequestMapping(value="realTimeLog/getSysDate",produces = "application/json;charset=UTF-8")
	public Object getSysDate(){
		String sysTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Map<String,Object> res = new HashMap<>();
		res.put("sysDate", sysTime);
		return res;
	}
}
