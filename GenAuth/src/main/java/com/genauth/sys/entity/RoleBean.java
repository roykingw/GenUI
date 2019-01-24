package com.genauth.sys.entity;

public class RoleBean {

	private String roleId;
	private String roleCode;
	private String roleName;
	
	private String controllercode;//拥有的ControllerCode 用"|"隔开
    private String pagecode; //拥有的pageCode 用"|"隔开
    private String menucode; //拥有的menuCode 用"|"隔开
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getControllercode() {
		return controllercode;
	}
	public void setControllercode(String controllercode) {
		this.controllercode = controllercode;
	}
	public String getPagecode() {
		return pagecode;
	}
	public void setPagecode(String pagecode) {
		this.pagecode = pagecode;
	}
	public String getMenucode() {
		return menucode;
	}
	public void setMenucode(String menucode) {
		this.menucode = menucode;
	}
}
