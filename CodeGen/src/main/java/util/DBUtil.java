package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtil {

	private static Connection connection;
	
    public static Connection getDBConn() throws Exception {
    	if(null == DBUtil.connection) {
    		String dbType = "";
    		if(PropLoader.getProp("jdbc.driver-class-name").toLowerCase().indexOf("mysql.")>1) {
    			dbType = "mysql";
    		}else if(PropLoader.getProp("jdbc.driver-class-name").toLowerCase().indexOf("oracle.")>1) {
    			dbType = "oracle";
    		}else {
    			throw new Exception("暂只支持 mysql 和 Oracle 数据库。其他数据库后续优化");
    		}
    		Properties dbProps = new Properties();
    		dbProps.put("user", PropLoader.getProp("jdbc.username"));
    		dbProps.put("password", PropLoader.getProp("jdbc.password"));
    		if("mysql".equals(dbType)) {
    			Class.forName("com.mysql.jdbc.Driver");
    			dbProps.put("useInformationSchema", "true"); //mysql获取表注释需要加上这个属性 这个很重要 
    		}else if("oracle".equals(dbType)) {
    			Class.forName("oracle.jdbc.driver.OracleDriver");
    			dbProps.put("remarksReporting", "true");  //要获取注释，需要增加这个属性。这个很重要 
    		}
    		connection = DriverManager.getConnection(PropLoader.getProp("jdbc.url"),dbProps);
    	}
    	return DBUtil.connection;
    }
}
