package com.genService.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;

import pojo.Sysdict;
import services.ServerDictService;

@Service(delay = -1 ,retries = 0,timeout = 1000)
public class ServerDictServiceImpl extends BaseServiceImpl<Sysdict> implements ServerDictService{

}
