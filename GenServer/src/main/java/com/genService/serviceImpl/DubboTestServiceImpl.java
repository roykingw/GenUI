package com.genService.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import org.apache.log4j.Logger;
import services.DobboTestService;

/**
 * Created by roykingw on 2017/12/13 0013.
 */
@Service(version = "1.0.0",retries = 3,timeout = 1000)
public class DubboTestServiceImpl implements DobboTestService {
    private Logger logger = Logger.getLogger(this.getClass());
    @Override
    public String StringTest() {
        logger.info("dubbo request received");
        return "Dubbo Test";
    }
}
