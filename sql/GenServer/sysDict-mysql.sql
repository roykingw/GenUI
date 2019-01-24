CREATE TABLE `sysDict`  (
  `dict_id` varchar(32) NOT NULL COMMENT '主键',
  `dict_value` varchar(32) COMMENT '字典值',
  `dict_label` varchar(64) COMMENT '字典描述',
  `dict_type` varchar(32) COMMENT '字典类型',
  `dict_type_desc` varchar(128) COMMENT '字典类型描述',
  `update_by` varchar(32) COMMENT '最后修改人',
  `update_time` datetime(0) COMMENT '最后修改时间',
  PRIMARY KEY (`dict_id`)
);

