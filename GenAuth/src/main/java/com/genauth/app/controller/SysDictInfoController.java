package com.genauth.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.genauth.app.util.RequestParaBuilder;
import com.genauth.sys.util.Excelexport;

import pojo.PageBean;
import pojo.Sysdict;
import services.ServerDictService;
import util.ExportDatas;
import util.RequestPara;

@Controller
@RequestMapping(value="sysDict")
public class SysDictInfoController implements BaseController<Sysdict>{

	private Logger logger = Logger.getLogger(this.getClass());

	@Reference(check = true,retries = 0)
	private ServerDictService serverDictService;
	@ResponseBody
    @RequestMapping(value="/queryData",produces="application/json;charset=UTF-8",method={RequestMethod.POST})
	public PageBean<Sysdict> queryData(HttpServletRequest request, PageBean<Sysdict> pageBean) throws Exception {
		logger.info("字典数据查询");
		RequestPara requestPara = RequestParaBuilder.buildPara(request);
		//requestPara.setQueryCond(""); //无法装入pojo属性中的查询条件放在这里拼凑 ，比如时间范围
		return serverDictService.queryData(requestPara,pageBean);
	}

	@ResponseBody
    @RequestMapping(value="/exportData",produces="application/json;charset=UTF-8",method={RequestMethod.GET})
	public Object exportData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("字典数据导出");
		RequestPara requestPara = RequestParaBuilder.buildPara(request);
		//requestPara.setQueryCond(""); //无法装入pojo属性中的查询条件放在这里拼凑 ，比如时间范围
		ExportDatas datas = serverDictService.exportData(requestPara);
        Excelexport excelExport=new Excelexport(request, response,datas.getFileName());
        excelExport.export(datas.getFileName(), datas.getInfoList(),datas.getColNames());
        return "EXCEL FILE "+datas.getFileName()+" EXPORTED";
	}

	@Override
	public Object deleteData(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
