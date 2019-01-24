package com.genauth;

import com.genauth.sys.util.EnvVariable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.util.Properties;

/**
 * Created by roykingw on 2017/10/12 0012.
 */
@ServletComponentScan
@SpringBootApplication
public class GenAuthApplication {

    public static void main(String[] args) {

        Properties properties = System.getProperties();
        //通过注入 -Dboot.env属性来配置多运行环境支持。
        properties.setProperty("spring.profiles.active", EnvVariable.name());
        //这个版本的dubbo内部的netty端口号，默认为22222，不知道为什么会和tomcat冲突。可以修改下端口号
        //也可以通过-D参数指定。注意要与服务端一致，见dubbo-2.5.8.jar中com.alibaba.dubbo.qos.common.Constants
        properties.setProperty("dubbo.qos.port", "20882");
        SpringApplication.run(GenAuthApplication.class, args);
    }
}
