package com.genauth.sys.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by roykingw on 2017/10/12 0012.
 * springBoot权限相关配置
 */
@Component
@ConfigurationProperties(prefix = "authConfig")
public class AuthConfig {
    private String ignoreControllers;
    
    public String getIgnoreControllers() {
        return ignoreControllers;
    }

    public void setIgnoreControllers(String ignoreControllers) {
        this.ignoreControllers = ignoreControllers;
    }
}
