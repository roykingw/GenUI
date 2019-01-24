package com.genauth.app.controller;

/**
 * Created by roykingw on 2017/12/14 0014.
 */

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import services.DobboTestService;

@Controller
@RequestMapping(value = "dubboTest")
public class DubboTestController {

    @Reference(version="1.0.0")
    private DobboTestService dubboTestService;

    @ResponseBody
    @RequestMapping(value = "StringTest",produces="application/json;charset=UTF-8",method={RequestMethod.GET})
    public Object dubboTestInfo(){
        return dubboTestService.StringTest();
    }
}
