package com.genauth.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pojo.PageBean;

public interface BaseController<T> {

	public Object queryData(HttpServletRequest request, PageBean<T> pageBean) throws Exception;
	
	public Object exportData(HttpServletRequest request,HttpServletResponse response) throws Exception;
	
	public Object deleteData(HttpServletRequest request,HttpServletResponse response);
//	public Object deleteData(HttpServletRequest request,BlackInfo blackInfo) throws Exception;
	
//	public Object updateData(HttpServletRequest request,BlackInfo blackInfo) throws Exception;
}
