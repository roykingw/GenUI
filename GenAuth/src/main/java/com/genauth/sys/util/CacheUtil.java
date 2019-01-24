package com.genauth.sys.util;

import org.apache.log4j.Logger;

public class CacheUtil {

	private static Logger logger = Logger.getLogger(CacheUtil.class);

	public void initCache() throws Exception{
		logger.info("开始加载首页缓存");
		logger.info("首页缓存加载完成");
	}
}
