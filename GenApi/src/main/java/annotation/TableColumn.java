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
@Target(ElementType.FIELD)  
@Retention(RetentionPolicy.RUNTIME) 
public @interface TableColumn {
	//查询时对应的数据库列
	public String dbColumn();
	//导出时的excel表头 不需要导出的列可以不配置
	public String excelHeader() default "";
	//对应字段的处理函数。 比如 将TimeStamp 用to_char转成字符串。
	//2017年11月3日 发现个BUG，如果这个dbFunc如果把查询结果的类型转换掉了，那查询完后无法再把结果设置回来。
	//例如，时间类型的字段，可能需要to_char转成字符串去查询及展示，但是转成字符串后，jdbc查询的结果就是string，调用setter就会报错了。
	//目前只能限定这个属性就用在String类型的属性上把。 
	public String dbFunc() default "";
	//2018年4月19日 声明该列是否主键。用于对数据的删除和修改。只要不是空就为主键。
	public String isPK() default "";
	//2018年8月14日 把这个数据库列的类型也生成到POJO中。暂时还没想好怎么用。
	public String dbColumnType() default "";
}
