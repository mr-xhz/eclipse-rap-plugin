package com.idohoo.eclipse.plugin.rap.http;

public class RapHttpConstant {

	/**
	 * cookie失效时间
	 */
	public static final Long COOKIE_TIME_EXPIRE = 300000L;
	
	/**
	 * 登录地址
	 */
	public static final String LOGIN_URL = "/account/doLogin.do";
	
	/**
	 * 用户更新通知
	 */
	public static final String UNREAD_NOTIFICATION_LIST_URL = "/account/getUnreadNotificationList.do";
	
	
	/**
	 * 用户项目
	 */
	public static final String PROJECTS_URL = "/org/projects.do";
	
	/**
	 * 项目信息
	 */
	public static final String WORKSPACE_URL = "/workspace/loadWorkspace.do";
	
	/**
	 * 锁定地址
	 */
	public static final String LOCK_URL = "/workspace/lock.do";
	
	/**
	 * 保存
	 */
	public static final String CHECK_IN_URL = "/workspace/checkIn.do";
}
