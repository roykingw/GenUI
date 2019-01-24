package com.genService.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvVariable {
	private static final Logger LOGGER = LoggerFactory.getLogger(EnvVariable.class);
	private static EnvValue envValue;
	private static String envName;

	static {
		String env = System.getProperty("boot.env");
		if (env == null || "".equals(env.trim())) {
			env = System.getenv("BOOT_ENV");
			if (env == null || "".equals(env.trim())) {
				env = "dev";
				LOGGER.warn("System property 'boot.env' or "
						+ "system environment 'BOOT_ENV' not exists, fallback to 'dev'.");
			}
		}
		envValue = EnvValue.valueOf(env.toUpperCase());
		envName = env.toLowerCase();
		LOGGER.info("Current Env is: "+ envValue);
	}

	/**
	 * 取当前环境变量的枚举值
	 *
	 * @return EnvValue
	 */
	public static EnvValue env() {
		return envValue;
	}

	/**
	 * 取当前环境变量名，即为枚举值的小写形式
	 *
	 * @return 环境名
	 */
	public static String name() {
		return envName;
	}

	/**
	 * 判断是否开发环境
	 *
	 * @return boolean
	 */
	public static boolean isDEV() {
		return env() == EnvValue.DEV;
	}

	/**
	 * 判断是否持续集成环境
	 *
	 * @return boolean
	 */
	public static boolean isCI() {
		return env() == EnvValue.CI

		;
	}

	/**
	 * 判断是否SIT环境
	 *
	 * @return boolean
	 */
	public static boolean isSIT() {
		return env() == EnvValue.SIT;
	}

	/**
	 * 判断是否UAT环境
	 *
	 * @return boolean
	 */
	public static boolean isUAT() {
		return env() == EnvValue.UAT;
	}

	/**
	 * 判断是否压力测试环境
	 *
	 * @return boolean
	 */
	public static boolean isSTR() {
		return env() == EnvValue.STR;
	}

	/**
	 * 判断是否预投产环境
	 *
	 * @return boolean
	 */
	public static boolean isPRE() {
		return env() == EnvValue.PRE;
	}

	/**
	 * 判断是否生产环境
	 *
	 * @return boolean
	 */
	public static boolean isPRD() {
		return env() == EnvValue.PRD;
	}

	public enum EnvValue {
		DEV, CI, SIT, UAT, STR, PRE, PRD,
	}

	private EnvVariable() {
	}

}
