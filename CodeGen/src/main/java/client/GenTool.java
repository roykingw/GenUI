package client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import util.Generator;
import util.PropLoader;

/**
 * 主入口类 用法：java GenTool <tableName> 数据库配置在MyGen.properties。
 * 这个文件名是配置在CGConstants里。
 * 
 * @author Administrator
 */
public class GenTool {

	public static void main(String[] args) {
		Long startTime = new Date().getTime();
		System.out.println("=========================================");
		System.out.println("============ GenUI配套生成器 加紧工作中  =======");
		System.out.println("=========================================");
		// 检查参数
		if (args.length < 1) {
			System.err.println("Usage: GenTool <propfilename>");
			System.err.println("the propfilename is placed in the 'plan' folder of the 'resource' folder");
			System.exit(-1);
			return;
		}
		// 加载通用配置文件
		try {
			PropLoader.load();
		} catch (FileNotFoundException e1) {
			System.err.println("配置文件common.properties 没有找到");
			System.exit(-1);
			return;
		} catch (IOException e2) {
			System.err.println("加载配置文件common.properties 出错");
			System.exit(-1);
			return;
		}

		// 加载生成计划配置文件
		String planName = args[0];
		try {
			PropLoader.loadPlan(planName);;
		} catch (FileNotFoundException e1) {
			System.err.println("生成计划配置文件 "+planName+".properties 没有找到");
			System.exit(-1);
			return;
		} catch (IOException e2) {
			System.err.println("生成计划配置文件 "+planName+".properties 没有找到");
			System.exit(-1);
			return;
		}

//		String errTbName = "";
		try {
			//先只生成一个。 多个的以后再优化
//			for (String tableName : args) {
//				errTbName = tableName;
				// 生成代码
				new Generator().generate();
//			}
		} catch (Exception e) {
			//TODO 后面要改成失败后自动删除
			System.err.println("生成 " + planName + " 配置文件指定的相关文件出错，请删除相关文件重新生成");
			e.printStackTrace();
			System.exit(-1);
			return;
		}
		Long spendTime = new Date().getTime()-startTime;
		System.out.println("============ 生成成功,操作耗时"+spendTime+"毫秒  =======");

	}

}
