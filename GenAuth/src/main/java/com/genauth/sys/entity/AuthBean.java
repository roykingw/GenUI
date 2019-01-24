package com.genauth.sys.entity;

public class AuthBean {
    private String authCode = "";
    private String menuCode = "";
    private String pageCode = "";
    private String controllerCode = "";
    private String authType = "";

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getControllerCode() {
        return controllerCode;
    }

    public void setControllerCode(String controllerCode) {
        this.controllerCode = controllerCode;
    }

}
