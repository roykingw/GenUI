package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义的注释，用来实现通用的报表功能。主要针对一个报表对应一张表的场景。
 * 2018年3月6日 目前实现的只有查询和excel导出。 以后会添加其他功能。
 * @author Administrator
 */
@Target(ElementType.TYPE)  
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	//查询时对应的数据库中的表
	public String tableName();
	//导出时的文件名
	public String expFileName() default "";
	//查询时的排序字段
	public String orderBy() default "";
	//排序顺序
	public String order() default "desc"; 
}
