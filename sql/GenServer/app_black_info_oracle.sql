create table BLACK_INFO
(
  id          VARCHAR(64) not null comment 'ID',
  black_type  VARCHAR(10) not null COMMENT '黑名单类型',
  key_id      VARCHAR(50) not null COMMENT '关键字',
  tx_typ      VARCHAR(5) not null COMMENT '交易类型',
  bus_typ     VARCHAR(5) not null COMMENT '业务类型',
  exp_dt      VARCHAR(20) not null COMMENT '过期时间',
  reason      VARCHAR(100) not null COMMENT '原因',
  create_by   VARCHAR(64) COMMENT '添加人',
  create_date VARCHAR(32) COMMENT '添加时间',
  update_by   VARCHAR(64) COMMENT '更新人',
  update_date VARCHAR(32) COMMENT '更新时间',
  remarks     VARCHAR(255) COMMENT '备注'
);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('9553f350d2d944c8bee64afb0fa4157c', 'WIFI', '14:75:90:86:65:80JKZX20', '*', '*', '20480810', 'FW1001-hour_black_wifi', '1', '06-09-2017 16:27:06.173000', '1', '06-09-2017 16:27:06.173000', null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('5dab7430d59749089344fe54117c9d5c', 'MBLNO', '18711191654', '*', '*', '20170930', '11', '1', '01-09-2017 15:15:19.913000', '1', '01-09-2017 15:15:29.787000', null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('6440434cefe54a7c857ae643b65fc9c3', 'MBLNO', '18711191654', 'B1', '*', '20891020', '333', '1', '10-10-2017 17:00:30.216000', '1', '10-10-2017 17:00:30.216000', null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('a46413fce4564ff29a331a6df9eda62f', 'WIFI', 'EE:26:CA:DA:83:09TESTER_2.4G', '15', '1500', '20150508', '1', '5232440cde264f348cc16d4a5084ee57', '25-08-2017 10:06:48.692000', '5232440cde264f348cc16d4a5084ee57', '25-08-2017 10:11:49.217000', null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('8d5c7588ae914fe8979d69de8121115c', 'PC', '5328EE1B82C5B9F3F4270D6A6DC4A029', '15', '1500', '20170825', '1', '5232440cde264f348cc16d4a5084ee57', '25-08-2017 09:55:42.375000', '5232440cde264f348cc16d4a5084ee57', '25-08-2017 10:07:25.436000', null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('6741663988144fbbaeb617ba75f6dc8f', 'WIFI', '44:94:FC:5E:50:2DRCS5.0', '*', '*', '20480812', 'FW1001-hour_black_wifi', '1', '07-09-2017 09:51:05.665000', '1', '07-09-2017 09:51:05.665000', null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('d2727f0df1994c02b7ae5b05119f762f', 'PHONE', 'B0:E0:3C:A2:72:AA', 'A3', '*', '20480811', 'HZ1002-minute_black_terml_resetpwd', '1', '05-09-2017 11:40:08.414000', '1', '05-09-2017 11:40:08.414000', null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('fac35abf4308472faf1b24862dc024f1', 'WIFI', 'EE:26:CA:DA:83:09TESTER_2.4G', '*', '*', '20480811', 'FW1001-hour_black_wifi', '1', '04-09-2017 19:56:05.788000', '1', '04-09-2017 19:56:05.788000', null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('47d6b8bf662c49ab806d189f8ffa3560', 'MBLNO', 'i', '*', '*', '20480827', 'test_asfds', '1', '19-10-2017 14:53:41.250000', '1', '19-10-2017 14:53:42.314000', null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('1813bd7005bd48339659a6cc82f44148', 'CREDITCRAD', 'Hx6wlOLY+CjY1vyBN4BlHFOMvDvZgUT3', '*', '*', '20480804', 'TX_LOG:YHK1002-day_hkcrd_usr_cnt', '1', '28-08-2017 14:31:51.153000', '1', '28-08-2017 14:31:51.153000', null);
commit;