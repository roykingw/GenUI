package com.genService;
import com.genService.util.EnvVariable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Properties;

/**
 * Created by roykingw on 2017/12/13 0013.
 */
@SpringBootApplication
@ComponentScan(value = "com.genService")
public class ServerApplication {

    public static void main(String[] args){
        Properties properties = System.getProperties();
        properties.setProperty("spring.profiles.active", EnvVariable.name());
        properties.setProperty("dubbo.qos.port", "20881");
        SpringApplication.run(ServerApplication.class, args);
    }
}
