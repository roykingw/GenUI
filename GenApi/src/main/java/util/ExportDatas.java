package util;

import java.io.Serializable;
import java.util.List;

/**
 * Created by roykingw on 2017/12/15 0015.
 * 转为dubbo分布式调用后，远程调用所有的参数必须都实现序列化接口。
 * 用于通用导出。保存导出的结果。
 */
public class ExportDatas implements Serializable{
	private static final long serialVersionUID = 1L;
	private String fileName;
    private List<String[]> infoList;
    private String[] colNames;

    public ExportDatas(String fileName, List<String[]> infoList, String[] colNames) {
        this.fileName = fileName;
        this.infoList = infoList;
        this.colNames = colNames;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String[]> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<String[]> infoList) {
        this.infoList = infoList;
    }

    public String[] getColNames() {
        return colNames;
    }

    public void setColNames(String[] colNames) {
        this.colNames = colNames;
    }

}
