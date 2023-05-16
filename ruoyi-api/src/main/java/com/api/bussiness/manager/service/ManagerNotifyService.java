package com.api.bussiness.manager.service;


public interface ManagerNotifyService {
	
	boolean existClassName(String className);
	
	String parsePayNotify(String msg,String className) throws Exception;
	
	String parseWithDrawalNotify(String msg,String className) throws Exception;
}
