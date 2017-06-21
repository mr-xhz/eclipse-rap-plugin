package com.idohoo.eclipse.plugin.rap.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.idohoo.eclipse.plugin.rap.exception.RapException;
import com.idohoo.eclipse.plugin.rap.main.Activator;
import com.idohoo.eclipse.plugin.rap.util.JsonUtil;
import com.idohoo.eclipse.plugin.rap.util.PreferenceUtil;
import com.idohoo.eclipse.plugin.rap.util.StringUtil;
import com.idohoo.eclipse.plugin.rap.vo.RapPreferenceVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapModuleVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapProjectDataVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapProjectVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapResultVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapWorkspaceVO;

public class RapHttpClient {

	private String domain;
	
	private String username;
	
	private String password;
	
	private String cookie;
	
	private Long cookieTime;
	
	private static RapHttpClient instance;
	
	private RapHttpClient(String domain,String username,String password){
		this.domain = domain;
		this.username = username;
		this.password = password;
	}
	
	public static RapHttpClient getInstance(){
		if(instance == null){
			RapPreferenceVO rapPreferenceVO = PreferenceUtil.getVO(Activator.getDefault(), RapPreferenceVO.class);
			instance = new RapHttpClient(rapPreferenceVO.getUrl(),rapPreferenceVO.getUsername(),rapPreferenceVO.getPassword());
		}
		return instance;
				
	}

	public String getCookie() {
		if(this.cookieTime != null && this.cookieTime < new Date().getTime()){
			return "";
		}
		return cookie == null?"":cookie;
	}

	public void setCookie(String cookie) {
		this.cookieTime = new Date().getTime() + RapHttpConstant.COOKIE_TIME_EXPIRE;
		this.cookie = cookie;
	}
	
	/**
	 * 检查并且登录
	 */
	private void checkAndLogin() throws RapException{
		if(StringUtil.isEmpty(this.getCookie())){
			this.login();
		}
	}
	
	/**
	 * cookie 过期的时候可以用来重新登录
	 */
	private void login() throws RapException{
		Map<String,String> params = new HashMap<String,String>();
		params.put("account", this.username);
		params.put("password", this.password);
		doPost(RapHttpConstant.LOGIN_URL,params);
	}
	
	public List<RapProjectVO> listProject() throws RapException{
		checkAndLogin();
		String result = doGet(RapHttpConstant.PROJECTS_URL,null);
		Object object = JsonUtil.parseAndGet(result,new String[]{"groups","","projects"});
		List<RapProjectVO> liRapProjectVO =  JsonUtil.parseToList(object, RapProjectVO.class);
		if(liRapProjectVO == null){
			liRapProjectVO = new ArrayList<RapProjectVO>();
		}
		return liRapProjectVO;
	}
	
	public RapWorkspaceVO loadWorkspae(int projectId) throws RapException{
		checkAndLogin();
		Map<String,String> params = new HashMap<String,String>();
		params.put("projectId", projectId+"");
		String result = doPost(RapHttpConstant.WORKSPACE_URL,params);
		return JsonUtil.parseToObject(result, RapWorkspaceVO.class);
	}
	
	public RapProjectDataVO lock(int projectId) throws RapException{
		checkAndLogin();
		Map<String,String> params = new HashMap<String,String>();
		params.put("projectId", projectId+"");
		String result = doPost(RapHttpConstant.WORKSPACE_URL,params);
		Object object = JsonUtil.parseAndGet(result,"projectData");
		return JsonUtil.parseToObject(object, RapProjectDataVO.class);
	}
	
	public void checkIn(int projectId,String projectData) throws RapException{
		checkAndLogin();
		Map<String,String> params = new HashMap<String,String>();
		params.put("id", projectId+"");
		params.put("projectData", projectData);
		params.put("deletedObjectListData", "[]");
		params.put("versionPosition", "4");
		params.put("description", "rap-plugin save");
		doPost(RapHttpConstant.CHECK_IN_URL,params);
	}
	
	private String doPost(String url,Map<String,String> params) throws RapException{
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(this.domain+url);
		postMethod.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");  
		if(params != null && params.size() > 0){
			NameValuePair[] parametersBody = new NameValuePair[params.size()];
			int index = 0;
			for(String key : params.keySet()){
				parametersBody[index++] = new NameValuePair(key,params.get(key));
			}
			postMethod.setRequestBody(parametersBody);
		}
		if(StringUtil.isNotEmpty(this.getCookie())){
			postMethod.setRequestHeader("Cookie", this.getCookie());
		}
		try {
			httpClient.executeMethod(postMethod);
			String sessionCookie = postMethod.getResponseHeader("Set-Cookie") == null?"":postMethod.getResponseHeader("Set-Cookie").getValue();
			sessionCookie = (sessionCookie == null?"":sessionCookie);
			Matcher matcher = Pattern.compile("(JSESSIONID=[a-z0-9]*?);",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(sessionCookie);
			if(matcher.find()){
				this.setCookie(matcher.group(1));
			}
			String result = postMethod.getResponseBodyAsString();
			RapResultVO rapResultVO = JsonUtil.parseToObject(result, RapResultVO.class);
			if(rapResultVO != null && StringUtil.isNotEmpty(rapResultVO.getErrMsg()) && !rapResultVO.getIsOk()){
				//出错了
				throw new RapException(rapResultVO.getErrMsg());
			}
			return result;
		} catch(RapException re){
			throw re;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private String doGet(String url,Map<String,String> params) throws RapException{
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(this.domain+url);
		if(params != null && params.size() > 0){
			NameValuePair[] queryString = new NameValuePair[params.size()];
			int index = 0;
			for(String key : params.keySet()){
				queryString[index++] = new NameValuePair(key,params.get(key));
			}
			getMethod.setQueryString(queryString);
		}
		if(StringUtil.isNotEmpty(this.getCookie())){
			getMethod.setRequestHeader("Cookie", this.getCookie());
		}
		try {
			httpClient.executeMethod(getMethod);
			String result = getMethod.getResponseBodyAsString();
			RapResultVO rapResultVO = JsonUtil.parseToObject(result, RapResultVO.class);
			if(rapResultVO != null && StringUtil.isNotEmpty(rapResultVO.getErrMsg()) && !rapResultVO.getIsOk()){
				//出错了
				throw new RapException(rapResultVO.getErrMsg());
			}
			return result;
		}catch(RapException re){
			throw re;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
}
