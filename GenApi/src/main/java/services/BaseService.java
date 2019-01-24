package services;

import pojo.PageBean;
import util.ExportDatas;
import util.RequestPara;

/**
 * Created by roykingw on 2017/12/15 0015.
 */
public interface BaseService<T> {

    PageBean<T> queryData(RequestPara requestPara, PageBean<T> pageBean) throws Exception;

    ExportDatas exportData(RequestPara requestPara) throws Exception;
    
    int deleteData(RequestPara requestPara) throws Exception;
    
    int updateData(RequestPara requestPara) throws Exception;
}
