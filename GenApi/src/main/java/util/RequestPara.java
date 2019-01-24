package util;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by roykingw on 2017/12/15 0015.
 * 转为dubbo分布式调用后，远程调用所有的参数必须都实现序列化接口。
 * 这个工具类用来转存通用查询时用到的request里的数据。
 */
public class RequestPara implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, String> requestParas;//request.getParameterMap()
    private String queryCond; //request.getAttribute("condition")

    public RequestPara(Map<String, String> requestParas,String queryCond){
        this.requestParas=requestParas;
        this.queryCond = queryCond;
    }

    public Map<String, String> getRequestParas() {
        return requestParas;
    }

    public void setRequestParas(Map<String, String> requestParas) {
        this.requestParas = requestParas;
    }

    public String getQueryCond() {
        return queryCond;
    }

    public void setQueryCond(String queryCond) {
        this.queryCond = queryCond;
    }
    
    public String toString() {
    	StringBuffer sb = new StringBuffer("RequestPara info: ");
    	sb.append("queryCond > "+this.queryCond+";");
    	sb.append("requestParas > ");
    	for(String key:this.requestParas.keySet()) {
    		sb.append(key +" = "+this.requestParas.get(key)+";");
    	}
    	return sb.toString();
    }
}
