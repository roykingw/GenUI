/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50173
Source Host           : localhost:3306
Source Database       : genauth

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2017-11-09 16:14:41
*/
-- metaDatasource元数据初始化
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_menu_info
-- ----------------------------
DROP TABLE IF EXISTS `t_menu_info`;
CREATE TABLE `t_menu_info` (
  `menu_id` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `state_path` varchar(255) DEFAULT NULL,
  `menu_code` varchar(255) DEFAULT NULL,
  `controller_code` varchar(255) DEFAULT NULL,
  `parent` varchar(32) DEFAULT NULL,
  `page_code` varchar(255) DEFAULT NULL,
  `menu_level` int(11) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_menu_info
-- ----------------------------
INSERT INTO `t_menu_info` VALUES ('0', '根节点', null, 'root', null, null, null, '0');
INSERT INTO `t_menu_info` VALUES ('10', '通用报表', 'genReport', 'genReport', null, 'root', null, '1');
INSERT INTO `t_menu_info` VALUES ('11', '黑名单管理', 'genReport.uiblackInfo', 'uiblackInfo', 'UiBlackInfoController', 'genReport', null, '2');
INSERT INTO `t_menu_info` VALUES ('12', '黑名单管理3', 'genReport.uiblackInfo3', 'uiblackInfo3', 'UiBlackInfoController', 'genReport', null, '2');
INSERT INTO `t_menu_info` VALUES ('13', '快照查询', 'genReport.snapshot', 'snapshot', 'SnapShotController', 'genReport', null, '2');
INSERT INTO `t_menu_info` VALUES ('7', '自定义报表', 'otherReport', 'otherReport', null, 'root', null, '1');
INSERT INTO `t_menu_info` VALUES ('8', '测试通用1', 'genReport.testGen1', 'testGen1', 'testGen1', 'genReport', null, '2');
INSERT INTO `t_menu_info` VALUES ('9', '测试自定义1', 'otherReport.testOther1', 'testOther1', 'testOther1', 'otherReport', null, '2');

-- ----------------------------
-- Table structure for t_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_role_auth`;
CREATE TABLE `t_role_auth` (
  `role_id` varchar(11) NOT NULL,
  `menu_id` varchar(255) DEFAULT NULL,
  `menu_code` varchar(255) DEFAULT NULL,
  `controller_code` varchar(255) DEFAULT NULL,
  `page_code` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_auth
-- ----------------------------
INSERT INTO `t_role_auth` VALUES ('1', '0', 'manage', ' ', '');
INSERT INTO `t_role_auth` VALUES ('1', '7', 'otherReport', ' ', '');
INSERT INTO `t_role_auth` VALUES ('1', '10', 'genReport', ' ', '');
INSERT INTO `t_role_auth` VALUES ('1', '-1', 'usermanage', 'UserManageAction', '');
INSERT INTO `t_role_auth` VALUES ('1', '-2', 'platUserManage', 'UserManageAction', '');
INSERT INTO `t_role_auth` VALUES ('1', '-3', 'platMenuManage', 'UserManageAction', '');
INSERT INTO `t_role_auth` VALUES ('1', '-4', 'druidManage', ' ', '');
INSERT INTO `t_role_auth` VALUES ('1', '-5', 'realTimeLog', 'RealTimeLogController', '');
INSERT INTO `t_role_auth` VALUES ('1', '9', 'testOther1', 'testOther1', 'testOther1_export|testOther1_query|');
INSERT INTO `t_role_auth` VALUES ('1', '8', 'testGen1', 'testGen1', 'testGen1_query|testGen1_export|');
INSERT INTO `t_role_auth` VALUES ('1', '11', 'uiblackInfo', 'UiBlackInfoController', 'uiblackInfo_query|uiblackInfo_export|uiblackInfo3_query|uiblackInfo3_export|');
INSERT INTO `t_role_auth` VALUES ('1', '12', 'uiblackInfo3', 'UiBlackInfoController', 'uiblackInfo3_query|uiblackInfo3_export|');
INSERT INTO `t_role_auth` VALUES ('1', '13', 'snapshot', 'SnapShotController', '');

-- ----------------------------
-- Table structure for t_role_info
-- ----------------------------
DROP TABLE IF EXISTS `t_role_info`;
CREATE TABLE `t_role_info` (
  `role_id` varchar(32) NOT NULL,
  `role_code` varchar(255) DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_info
-- ----------------------------
INSERT INTO `t_role_info` VALUES ('1', 'admin', '管理员角色');

-- ----------------------------
-- Table structure for t_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info` (
  `user_id` varchar(32) NOT NULL,
  `user_name` varchar(32) DEFAULT NULL,
  `user_realname` varchar(32) DEFAULT NULL,
  `user_password` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of t_user_info
-- ----------------------------
INSERT INTO `t_user_info` VALUES ('1', 'admin', '管理员', '123456');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `user_id` varchar(11) NOT NULL,
  `role_id` varchar(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', '1');
