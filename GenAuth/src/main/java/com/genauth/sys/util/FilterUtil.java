package com.genauth.sys.util;

import org.springframework.util.StringUtils;

/**
 * 页面Filter的java实现
 * @author ftoul090
 *
 */
public class FilterUtil {

	/**
	 * 空过滤
	 */
	public static String nullFilter(String str){
		return StringUtils.isEmpty(str)||"null".equals(str) ? "" : str;
	}
	/**
	 * 是否投资成功
	 * @param str
	 * @return
	 */
	public static String isOk(String str){
		switch(str){
			case "0":
				return "投资未成功";
			case "1":
				return "投资成功";
			default:
				return  "其他";
		}
	}
	/**
	 * 是否有收益
	 * @param str
	 * @return
	 */
	public static String isHave(String str){
		switch (str) {
			case "true":
				return "无收益";
			case "false":
				return "有收益";
			default:
				return  "其他";
		}
	}
	/**
	 * 是否新手标
	 * @param str
	 * @return
	 */
	public static String isNew(String str){
		switch (str) {
			case "0":
				return "普通标";
			case "1":
				return "新手标";
			default:
				return  str;
		}
	}

	/**
	 * 蜂币来源
	 * @param type
	 * @return
	 */
	public static String getSourceType(String type){
		String res = "";
		switch(type){
			case "1":
				res = "开通汇付";
				break;
			case "2":
				res = "首冲";
				break;
			case "3":
				res = "手动赠送";
				break;
			case "4":
				res = "小贷通";
				break;
			case "5":
				res = "V计划";
				break;
			case "6":
				res = "A计划";
				break;
			case "7":
				res = "B计划";
				break;
			case "8":
				res = "C计划";
				break;
			case "9":
				res = "民生通";
				break;
			case "10":
				res = "政信通";
				break;
			case "11":
				res = "企信通";
				break;
			case "14":
				res = "D计划";
				break;
			case "20":
				res = "疯狂合伙人邀请好友赠送";
				break;
			default:
				res =  type;
				break;
		}
		return res;
	}
	/**
	 * 活动类型
	 * @param activity_type
	 * @return
	 */
	public static String getActivity_type(String activity_type){
		String res = "";
		switch(activity_type){
			case "1":
				res = "天天登录抽奖";
				break;
			case "2":
				res = "15/45天活动";
				break;
			case "3":
				res = "30/60天活动";
				break;
			case "4":
				res = "端午节活动";
				break;
			case "10":
				res = "7天活动";
				break;
			case "11":
				res = "15天活动";
				break;
			case "12":
				res = "21天活动";
				break;
			case "13":
				res = "30天活动";
				break;
			case "14":
				res = "微信九宫格抽奖活动";
				break;
			case "17":
				res = "万圣节活动";
				break;
			case "19":
				res = "铜蛋";
				break;
			case "20":
				res = "银蛋";
				break;
			case "21":
				res = "金蛋";
				break;
			case "36":
				res = "聚情元宵猜灯谜";
				break;
			case "37":
				res = "女神节活动赠送";
				break;
			default:
				res =  activity_type;
				break;
		}
		return res;
	}
	/**
	 * 活动登陆类型
	 * @param type
	 * @return
	 */
	public static String getActivity_login_type(String type){
		String res = "";
		switch(type){
			case "2":
				res = "pc登陆";
				break;
			case "320":
				res = "微信登陆";
				break;
			default:
				res =  type;
				break;
		}
		return res;
	}

	//时间单位期限
	public static String period_unit(String unit){
		switch (unit) {
			case "0":
				return "月";
			case "1":
				return "天";
			default:
				return  unit;
		}
	}
	/**
	 * 红包获取来源
	 * @return
	 */
	public static String getBribery_receive_type(String bribery_receive_type) {
		switch (bribery_receive_type) {
			case "1": return "首次充值";
			case "2": return "首次投资";
			case "3": return "蜂狂合伙人奖励";
			case "4": return "大转盘抽奖";
			case "5": return "开通汇付";
			case "6": return "蜂币兑换";
			case "8": return "平台赠送";
			case "9": return "心动礼包";
			case "11": return "抢红包";
			case "12": return "抢红包";
			case "13": return "抢红包";
			case "14": return "抢红包";
			case "15": return "邀请好友奖励";
			case "16": return "还款赠送";
			case "18": return "518活动";
			case "19": return "端午礼包";
			case "20": return "理财季第一期";
			case "21": return "充值赠送";
			case "22": return "微信九宫格";
			case "23": return "砸金蛋";
			case "24": return "秋收活动";
			case "25": return "中秋活动";
			case "26": return "国庆活动";
			case "27": return "生日福利";
			case "28": return "组合理财活动";
			case "29": return "万圣节活动";
			case "30": return "网贷天眼活动";
			case "31": return "双11活动";
			case "36": return "聚情元宵猜灯谜";
			case "37": return "女神节活动赠送";
			default: return  bribery_receive_type;
		}
	}
	/**
	 * 红包类型
	 * @param type
	 * @return
	 */
	public static String filterBriberyType(String type){
		switch (type) {
			case "1": return "普通红包";
			case "2": return "蜂蜜券";
			default: return  type;
		}
	}
}
