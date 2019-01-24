package com.genauth.app.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.genauth.app.util.IdGen;
import com.genauth.app.util.RequestParaBuilder;
import com.genauth.sys.entity.SubMenu;
import com.genauth.sys.entity.UserBean;
import com.genauth.sys.service.SubMenuService;
import com.genauth.sys.util.Excelexport;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.GrsnapshotLog;
import pojo.GrsnapshotLogWithBLOBs;
import pojo.PageBean;
import services.SnapShotService;
import util.ExportDatas;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/SnapShot")
//public class SnapShotController extends BaseController<GrsnapshotLog>{
public class SnapShotController{

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private SubMenuService subMenuService;
	
	@Reference
	private SnapShotService snapShotService;
	
	@ResponseBody
	@RequestMapping(value="/save.do",produces="application/json;charset=UTF-8",method={RequestMethod.POST})
	public Object save(HttpServletRequest request,String menuCode,String opt,String requestURI,String html,String values) throws Exception{
		if(StringUtils.isNotEmpty(opt) && opt.indexOf("%")>=0){
			opt = URLDecoder.decode(opt, "UTF-8");
		}
		if(StringUtils.isNotEmpty(requestURI) && requestURI.indexOf("%")>=0){
			requestURI = URLDecoder.decode(requestURI, "UTF-8");
		}
		html = URLDecoder.decode(html,"UTF-8");
		values = URLDecoder.decode(values,"UTF-8");
		GrsnapshotLogWithBLOBs snapShotData = new GrsnapshotLogWithBLOBs();
		snapShotData.setId(IdGen.uuid());
		snapShotData.setHtml(html);
		snapShotData.setPagevalue(values);;
		snapShotData.setRequestUri(requestURI);
		//操作人
		UserBean user = (UserBean) request.getSession().getAttribute("currentUser");
		snapShotData.setCreateBy(user.getUserId());
		snapShotData.setCreateDate(new Date());
		snapShotData.setRemoteAddr(request.getRemoteAddr());
		StringBuilder title = new StringBuilder();
		//查询菜单名
		for(String subCode:menuCode.split("\\.")){
			SubMenu cond = new SubMenu();
			cond.setMenuCode(subCode);
			List<SubMenu> queryRes = subMenuService.queryMenu(cond);
			if(1==queryRes.size()){
				title.append(queryRes.get(0).getName()).append(".");
			}
		}
		title.append(opt);
		snapShotData.setTitle(title.toString());
		int opRes = snapShotService.save(snapShotData);
		
		Map<String,Object> res = new HashMap<String,Object>();
		res.put("resCount", opRes);
		return res;
	}
	
	@ResponseBody
	@RequestMapping(value="/queryData",produces="application/json;charset=UTF-8",method={RequestMethod.POST})
	public PageBean<GrsnapshotLog> queryData(HttpServletRequest request, PageBean<GrsnapshotLog> pageBean) throws Exception{
		logger.info("页面快照查询");
		//默认查一天
		String startTime = pageBean.getStarttime();
		String endTime = pageBean.getEndtime();
		Calendar cal = Calendar.getInstance();
		if(StringUtils.isEmpty(pageBean.getStarttime())){
			startTime = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
			pageBean.setStarttime(startTime);
		}
		if(StringUtils.isEmpty(endTime)){
			cal.add(Calendar.DAY_OF_MONTH, 1);
			endTime = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
			pageBean.setEndtime(endTime);
		}
		String condition = " and to_char(create_date,'yyyymmdd')> =" + startTime +" and to_char(create_date,'yyyymmdd')<"+endTime;
		request.setAttribute("condition", condition);

		return snapShotService.queryData(RequestParaBuilder.buildPara(request),pageBean);
	}

	@ResponseBody
	@RequestMapping(value="/exportData",produces="application/json;charset=UTF-8",method={RequestMethod.GET})
	public Object exportData(HttpServletRequest request,HttpServletResponse response,String startTime,String endTime) throws Exception{
		logger.info("页面快照导出");
		//默认查一天
		Calendar cal = Calendar.getInstance();
		if(StringUtils.isEmpty(startTime)){
			startTime = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		}
		if(StringUtils.isEmpty(endTime)){
			cal.add(Calendar.DAY_OF_MONTH, 1);
			endTime = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		}
		String condition = " and to_char(create_date,'yyyymmdd')> =" + startTime +" and to_char(create_date,'yyyymmdd')<"+endTime;
		request.setAttribute("condition", condition);
		ExportDatas datas = snapShotService.exportData(RequestParaBuilder.buildPara(request));
		Excelexport excelExport=new Excelexport(request, response,datas.getFileName());
		excelExport.export(datas.getFileName(), datas.getInfoList(),datas.getColNames());
		return "EXCEL FILE "+datas.getFileName()+" EXPORTED";
//		return super.exportData(request, response);
	}
	
	@ResponseBody
	@RequestMapping(value="/getSnapShot",produces="application/json;charset=UTF-8",method={RequestMethod.POST})
	public Object getSnapShot(HttpServletRequest request,String snapshotId) throws Exception{
		logger.info("获取快照信息");
		GrsnapshotLogWithBLOBs snapInfo = snapShotService.getSnapShot(snapshotId);
		//去掉模版中的ng-repeat指令。因为加载页面时，这个指令会重新渲染。
		snapInfo.setHtml(snapInfo.getHtml().replaceAll("ng-repeat=\"[^\"]*\"", ""));
		Map<String,Object> res = new HashMap<>();
		res.put("snapInfo", snapInfo);
		return res;
	}
}
