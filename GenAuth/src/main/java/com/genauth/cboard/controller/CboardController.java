package com.genauth.cboard.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.genauth.cboard.excel.XlsProcessService;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import services.CboardService;

import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by roykingw on 2017/12/14 0014.
 * 与cboard进行交互。改为使用dubbo的方式调用cboard的dubbo服务。
 */
@Controller
@RequestMapping(value = "dashboard")
public class CboardController {

	@Autowired
	private XlsProcessService xlsProcessService;
	
    private Logger logger = Logger.getLogger(this.getClass());

    //远程Dubbo服务， 设为启动不检查。服务端没有启动不影响调用端的启动。
    @Reference(check = false)
    private CboardService cboardService;

    @ResponseBody
    @RequestMapping(value = "/getDatasetList",produces="application/json;charset=UTF-8")
    public Object getDatasetList() {
        return cboardService.getDatasetList();
    }

    @ResponseBody
    @RequestMapping(value = "/getBoardParam",produces="application/json;charset=UTF-8")
    public Object getBoardParam(@RequestParam(name = "boardId") Long boardId) {
        return cboardService.getBoardData(boardId);
    }

    @ResponseBody
    @RequestMapping(value = "/getBoardData",produces="application/json;charset=UTF-8")
    public Object getBoardData(@RequestParam(name = "id") String id) {
        return cboardService.getBoardData(Long.parseLong(id));
    }

//    @ResponseBody
    @RequestMapping(value = "/exportBoard",produces="application/json;charset=UTF-8")
    public ResponseEntity<byte[]> exportBoard(@RequestParam(name = "id") Long id) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "report.xls");
//        return new ResponseEntity<>(boardService.exportBoard(id), headers, HttpStatus.CREATED);
        return new ResponseEntity<>(cboardService.exportBoard(id), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/getDimensionValues",produces="application/json;charset=UTF-8")
    public void getDimensionValues(@RequestParam(name = "datasourceId", required = false) Long datasourceId,
                                     @RequestParam(name = "query", required = false) String query,
                                     @RequestParam(name = "datasetId", required = false) Long datasetId,
                                     @RequestParam(name = "colmunName", required = true) String colmunName,
                                     @RequestParam(name = "cfg", required = false) String cfg,
                                     @RequestParam(name = "reload", required = false, defaultValue = "false") Boolean reload,
                                     HttpServletResponse response) throws IOException {
        String[] res = cboardService.getDimensionValues(datasourceId,query,datasetId,colmunName, cfg,reload);
        response.getWriter().write(JSON.toJSONString(res));
//        return cboardService.getDimensionValues(datasourceId,query,datasetId,colmunName, cfg,reload);
}

    @ResponseBody
    @RequestMapping(value = "/getAggregateData",produces="application/json;charset=UTF-8")
    public Object getAggregateData(@RequestParam(name = "datasourceId", required = false) Long datasourceId,
                                   @RequestParam(name = "query", required = false) String query,
                                   @RequestParam(name = "datasetId", required = false) Long datasetId,
                                   @RequestParam(name = "cfg",required = false) String cfg,
                                   @RequestParam(name = "reload", required = false, defaultValue = "false") Boolean reload) {
        if(cfg.indexOf("%")>=0){
            try {
                cfg = URLDecoder.decode(cfg,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.warn("CboardController.getAggregateData 报表调用 参数转换错误：cfg = "+cfg);
            }
        }
        return cboardService.getAggregateData(datasourceId,query,datasetId,cfg,reload);
    }

    @ResponseBody
    @RequestMapping(value = "/tableToxls")
    public Object tableToxls(@RequestParam(name = "data") String data) {
        HSSFWorkbook wb = xlsProcessService.tableToxls(JSONObject.parseObject(data));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            logger.info("外部调用数据导出");
            wb.write(out);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "table.xls");
            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
        } catch (IOException e) {
            logger.error("cboard报表导出失败", e);
            return null;
        }
    }
}
