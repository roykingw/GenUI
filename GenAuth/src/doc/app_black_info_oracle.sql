--appDatasource 测试报表 黑名单表 初始化
create table BLACK_INFO
(
  id          VARCHAR2(64) not null,
  black_type  VARCHAR2(10) not null,
  key_id      VARCHAR2(50) not null,
  tx_typ      VARCHAR2(5) not null,
  bus_typ     VARCHAR2(5) not null,
  exp_dt      VARCHAR2(20) not null,
  reason      VARCHAR2(100) not null,
  create_by   VARCHAR2(64),
  create_date TIMESTAMP(6),
  update_by   VARCHAR2(64),
  update_date TIMESTAMP(6),
  remarks     VARCHAR2(255)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column BLACK_INFO.id
  is 'ID';
comment on column BLACK_INFO.black_type
  is '黑名单类型';
comment on column BLACK_INFO.key_id
  is '关键字';
comment on column BLACK_INFO.tx_typ
  is '交易类型';
comment on column BLACK_INFO.bus_typ
  is '业务类型';
comment on column BLACK_INFO.exp_dt
  is '过期时间';
comment on column BLACK_INFO.reason
  is '理由';
comment on column BLACK_INFO.create_by
  is '创建用户';
comment on column BLACK_INFO.create_date
  is '创建日期';
comment on column BLACK_INFO.update_by
  is '更新用户';
comment on column BLACK_INFO.update_date
  is '更新日期';
comment on column BLACK_INFO.remarks
  is '备注';
create unique index BLACK_INFO_INX1 on BLACK_INFO (BLACK_TYPE, KEY_ID, TX_TYP, BUS_TYP, EXP_DT)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table BLACK_INFO
  add constraint BLACK_INFO_PK primary key (ID);

prompt Disabling triggers for BLACK_INFO...
alter table BLACK_INFO disable all triggers;
prompt Deleting BLACK_INFO...
delete from BLACK_INFO;
commit;
prompt Loading BLACK_INFO...
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('9553f350d2d944c8bee64afb0fa4157c', 'WIFI', '14:75:90:86:65:80JKZX20', '*', '*', '20480810', 'FW1001-hour_black_wifi', '1', to_timestamp('06-09-2017 16:27:06.173000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('06-09-2017 16:27:06.173000', 'dd-mm-yyyy hh24:mi:ss.ff'), null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('5dab7430d59749089344fe54117c9d5c', 'MBLNO', '18711191654', '*', '*', '20170930', '11', '1', to_timestamp('01-09-2017 15:15:19.913000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('01-09-2017 15:15:29.787000', 'dd-mm-yyyy hh24:mi:ss.ff'), null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('6440434cefe54a7c857ae643b65fc9c3', 'MBLNO', '18711191654', 'B1', '*', '20891020', '333', '1', to_timestamp('10-10-2017 17:00:30.216000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('10-10-2017 17:00:30.216000', 'dd-mm-yyyy hh24:mi:ss.ff'), null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('a46413fce4564ff29a331a6df9eda62f', 'WIFI', 'EE:26:CA:DA:83:09TESTER_2.4G', '15', '1500', '20150508', '1', '5232440cde264f348cc16d4a5084ee57', to_timestamp('25-08-2017 10:06:48.692000', 'dd-mm-yyyy hh24:mi:ss.ff'), '5232440cde264f348cc16d4a5084ee57', to_timestamp('25-08-2017 10:11:49.217000', 'dd-mm-yyyy hh24:mi:ss.ff'), null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('8d5c7588ae914fe8979d69de8121115c', 'PC', '5328EE1B82C5B9F3F4270D6A6DC4A029', '15', '1500', '20170825', '1', '5232440cde264f348cc16d4a5084ee57', to_timestamp('25-08-2017 09:55:42.375000', 'dd-mm-yyyy hh24:mi:ss.ff'), '5232440cde264f348cc16d4a5084ee57', to_timestamp('25-08-2017 10:07:25.436000', 'dd-mm-yyyy hh24:mi:ss.ff'), null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('6741663988144fbbaeb617ba75f6dc8f', 'WIFI', '44:94:FC:5E:50:2DRCS5.0', '*', '*', '20480812', 'FW1001-hour_black_wifi', '1', to_timestamp('07-09-2017 09:51:05.665000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('07-09-2017 09:51:05.665000', 'dd-mm-yyyy hh24:mi:ss.ff'), null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('d2727f0df1994c02b7ae5b05119f762f', 'PHONE', 'B0:E0:3C:A2:72:AA', 'A3', '*', '20480811', 'HZ1002-minute_black_terml_resetpwd', '1', to_timestamp('05-09-2017 11:40:08.414000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('05-09-2017 11:40:08.414000', 'dd-mm-yyyy hh24:mi:ss.ff'), null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('fac35abf4308472faf1b24862dc024f1', 'WIFI', 'EE:26:CA:DA:83:09TESTER_2.4G', '*', '*', '20480811', 'FW1001-hour_black_wifi', '1', to_timestamp('04-09-2017 19:56:05.788000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('04-09-2017 19:56:05.788000', 'dd-mm-yyyy hh24:mi:ss.ff'), null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('47d6b8bf662c49ab806d189f8ffa3560', 'MBLNO', 'i', '*', '*', '20480827', 'test_asfds', '1', to_timestamp('19-10-2017 14:53:41.250000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('19-10-2017 14:53:42.314000', 'dd-mm-yyyy hh24:mi:ss.ff'), null);
insert into BLACK_INFO (id, black_type, key_id, tx_typ, bus_typ, exp_dt, reason, create_by, create_date, update_by, update_date, remarks)
values ('1813bd7005bd48339659a6cc82f44148', 'CREDITCRAD', 'Hx6wlOLY+CjY1vyBN4BlHFOMvDvZgUT3', '*', '*', '20480804', 'TX_LOG:YHK1002-day_hkcrd_usr_cnt', '1', to_timestamp('28-08-2017 14:31:51.153000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('28-08-2017 14:31:51.153000', 'dd-mm-yyyy hh24:mi:ss.ff'), null);
commit;
prompt 10 records loaded
prompt Enabling triggers for BLACK_INFO...
alter table BLACK_INFO enable all triggers;
set feedback on
set define on
prompt Done.
