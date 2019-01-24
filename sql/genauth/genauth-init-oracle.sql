CREATE TABLE t_menu_info (
  menu_id varchar2(32) NOT NULL,
  name varchar2(255) DEFAULT NULL,
  state_path varchar2(255) DEFAULT NULL,
  menu_code varchar2(255) DEFAULT NULL,
  controller_code varchar2(255) DEFAULT NULL,
  parent varchar2(32) DEFAULT NULL,
  page_code varchar2(255) DEFAULT NULL,
  menu_level int DEFAULT NULL,
  PRIMARY KEY (menu_id)
);
INSERT INTO t_menu_info VALUES ('0', '根节点', null, 'root', null, null, null, '0');
INSERT INTO t_menu_info VALUES ('10', '通用报表', 'genReport', 'genReport', null, 'root', null, '1');
INSERT INTO t_menu_info VALUES ('13', '快照查询', 'genReport.snapshot', 'snapshot', 'SnapShotController', 'genReport', null, '2');
INSERT INTO t_menu_info VALUES ('7', '自定义报表', 'otherReport', 'otherReport', null, 'root', null, '1');
INSERT INTO t_menu_info VALUES ('8', '测试通用1', 'genReport.testGen1', 'testGen1', 'testGen1', 'genReport', null, '2');
INSERT INTO t_menu_info VALUES ('9', '测试自定义1', 'otherReport.testOther1', 'testOther1', 'testOther1', 'otherReport', null, '2');

CREATE TABLE t_role_auth (
  role_id varchar(11) NOT NULL,
  menu_id varchar(255) DEFAULT NULL,
  menu_code varchar(255) DEFAULT NULL,
  controller_code varchar(255) DEFAULT NULL,
  page_code varchar(255) DEFAULT NULL
);

INSERT INTO t_role_auth VALUES ('1', '0', 'manage', ' ', '');
INSERT INTO t_role_auth VALUES ('1', '7', 'otherReport', ' ', '');
INSERT INTO t_role_auth VALUES ('1', '10', 'genReport', ' ', '');
INSERT INTO t_role_auth VALUES ('1', '-1', 'usermanage', 'UserManageAction', '');
INSERT INTO t_role_auth VALUES ('1', '-2', 'platUserManage', 'UserManageAction', '');
INSERT INTO t_role_auth VALUES ('1', '-3', 'platMenuManage', 'UserManageAction', '');
INSERT INTO t_role_auth VALUES ('1', '-4', 'druidManage', ' ', '');
INSERT INTO t_role_auth VALUES ('1', '-5', 'realTimeLog', 'RealTimeLogController', '');
INSERT INTO t_role_auth VALUES ('1', '9', 'testOther1', 'testOther1', 'testOther1_export|testOther1_query|');
INSERT INTO t_role_auth VALUES ('1', '8', 'testGen1', 'testGen1', 'testGen1_query|testGen1_export|');
INSERT INTO t_role_auth VALUES ('1', '13', 'snapshot', 'SnapShotController', '');


CREATE TABLE t_role_info (
  role_id varchar(32) NOT NULL,
  role_code varchar(255) DEFAULT NULL,
  role_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (role_id)
);

-- ----------------------------
-- Records of t_role_info
-- ----------------------------
INSERT INTO t_role_info VALUES ('1', 'admin', '管理员角色');

-- ----------------------------
-- Table structure for t_user_info
-- ----------------------------
CREATE TABLE t_user_info (
  user_id varchar(32) NOT NULL,
  user_name varchar(32) DEFAULT NULL,
  user_realname varchar(32) DEFAULT NULL,
  user_password varchar(32) DEFAULT NULL,
  PRIMARY KEY (user_id)
);

-- ----------------------------
-- Records of t_user_info
-- ----------------------------
INSERT INTO t_user_info VALUES ('1', 'admin', '管理员', '123456');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
CREATE TABLE t_user_role (
  user_id varchar(11) NOT NULL,
  role_id varchar(11) NOT NULL,
  PRIMARY KEY (user_id,role_id)
);

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO t_user_role VALUES ('1', '1');
commit;
