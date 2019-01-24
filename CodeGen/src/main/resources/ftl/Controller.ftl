package com.genauth.app.controller.${module_name};

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.genauth.app.controller.BaseController;
import com.genauth.app.util.RequestParaBuilder;
import com.genauth.sys.util.CommonUtils;
import com.genauth.sys.util.Excelexport;

import pojo.PageBean;
import pojo.${pojo_name};
import services.${module_name}.${pojo_name}Service;
import util.ExportDatas;
import util.RequestPara;

/**
* @auth CodeGen
* @time ${gen_time}
*/

@Controller
@RequestMapping(value="${pojo_name}")
public class ${pojo_name}Controller implements BaseController<${pojo_name}>{

	private Logger logger = Logger.getLogger(this.getClass());

	@Reference(check = true,retries = 0)
	private ${pojo_name}Service service;
	
	@ResponseBody
    @RequestMapping(value="/queryData",produces="application/json;charset=UTF-8",method={RequestMethod.POST})
	public Object queryData(HttpServletRequest request, PageBean<${pojo_name}> pageBean) throws Exception {
		logger.info("数据查询");
		RequestPara requestPara = RequestParaBuilder.buildPara(request);
		//requestPara.setQueryCond(""); //无法装入pojo属性中的查询条件放在这里拼凑 ，比如时间范围
		try {
			return service.queryData(requestPara,pageBean);
		}catch(Exception e) {
			logger.error("查询数据出错",e);
			JSONObject res = new JSONObject();
			res.put("error", -1);
			res.put("message", e.getMessage());
			return res;
		}
	}

	@ResponseBody
    @RequestMapping(value="/exportData",produces="application/json;charset=UTF-8",method={RequestMethod.GET})
	public Object exportData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("数据导出");
		RequestPara requestPara = RequestParaBuilder.buildPara(request);
		//requestPara.setQueryCond(""); //无法装入pojo属性中的查询条件放在这里拼凑 ，比如时间范围
		ExportDatas datas = service.exportData(requestPara);
        Excelexport excelExport=new Excelexport(request, response,datas.getFileName());
        excelExport.export(datas.getFileName(), datas.getInfoList(),datas.getColNames());
        return "EXCEL FILE "+datas.getFileName()+" EXPORTED";
	}
	
	@ResponseBody
    @RequestMapping(value="/deleteData",produces="application/json;charset=UTF-8",method={RequestMethod.POST})
	public Object deleteData(HttpServletRequest request,HttpServletResponse response) {
		logger.info("数据删除");
		RequestPara requestPara = RequestParaBuilder.buildPara(request);
		int resCode = -1;
		JSONObject res = new JSONObject();
		try {
			resCode = service.deleteData(requestPara);
		}catch(Exception e) {
			logger.error("数据删除出错："+e.getMessage());
			res.put("message", e.getMessage());
		}
		res.put("resCode", resCode);
		return res;
	}
	
	@ResponseBody
    @RequestMapping(value="/updateData",produces="application/json;charset=UTF-8",method={RequestMethod.POST})
	public Object updateData(HttpServletRequest request,HttpServletResponse response) {
		logger.info("数据修改");
		RequestPara requestPara = RequestParaBuilder.buildPara(request);
		int resCode = -1;
		JSONObject res = new JSONObject();
		try {
			resCode = service.updateData(requestPara);
		}catch(Exception e) {
			logger.error("数据更新出错："+e.getMessage());
			res.put("message", e.getMessage());
		}
		res.put("resCode", resCode);
		return res;
	}
	
}
