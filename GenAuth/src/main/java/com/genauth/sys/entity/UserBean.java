package com.genauth.sys.entity;

public class UserBean {
    private String userId;
    private String userName;
    private String userRealname;
    private String userPassword;
    
    private String userRoles; //用户所属角色ID
    private String userControllercode;//用户拥有的ControllerCode 用"|"隔开
    private String userPagecode; //用户拥有的pageCode 用"|"隔开
    private String userMenucode; //用户拥有的menuCode 用"|"隔开

    public String getUserControllercode() {
        return userControllercode;
    }

    public void setUserControllercode(String userControllercode) {
        this.userControllercode = userControllercode;
    }

    public String getUserPagecode() {
        return userPagecode;
    }

    public void setUserPagecode(String userPagecode) {
        this.userPagecode = userPagecode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

	public String getUserMenucode() {
		return userMenucode;
	}

	public void setUserMenucode(String userMenucode) {
		this.userMenucode = userMenucode;
	}

	public String getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(String userRoles) {
		this.userRoles = userRoles;
	}
}
