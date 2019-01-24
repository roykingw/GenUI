package com.genauth.app.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import util.RequestPara;

import javax.servlet.http.HttpServletRequest;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by roykingw on 2017/12/15 0015.
 */
public class RequestParaBuilder {

    public static RequestPara buildPara(HttpServletRequest request){
        Map<String,String> paras = new HashMap<>();
        for(String key:request.getParameterMap().keySet()){
        	if(StringUtils.isNotEmpty(request.getParameter(key))) {
        		String val = request.getParameter(key);
        		if(key.indexOf("%")>=0) {
        			try{
        				key= URLDecoder.decode(key.toString(),"UTF-8");
        			}catch(Exception e) {}
        		}
        		if(null != val && val.toString().indexOf("%")>=0){
        			try{
        				val= URLDecoder.decode(val.toString(),"UTF-8");
        			}catch(Exception e) {}
				}
        		paras.put(key , val);
        	}
        }
        String queryCond = ObjectUtils.allNotNull(request.getAttribute("condition"))?request.getAttribute("condition").toString():"";
        return new RequestPara(paras,queryCond);
    }
}
