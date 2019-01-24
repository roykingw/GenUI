package com.genauth.sys.entity;

public class SubMenu {

	private String menuId;
	private String name;
	private String statePath;
	private String menuCode;
	private String controllerCode;
	private String pageCode;
	private String parent;
	private String menuLevel;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatePath() {
		return statePath;
	}

	public void setStatePath(String statePath) {
		this.statePath = statePath;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getControllerCode() {
		return controllerCode;
	}

	public void setControllerCode(String controllerCode) {
		this.controllerCode = controllerCode;
	}

	public String getPageCode() {
		return pageCode;
	}

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(String menuLevel) {
		this.menuLevel = menuLevel;
	}
}
