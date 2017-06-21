package com.idohoo.eclipse.plugin.rap.util;

import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpUtil {

	public static String doPost(String url,Map<String,String> params){
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");  
		if(params != null && params.size() > 0){
			NameValuePair[] parametersBody = new NameValuePair[params.size()];
			int index = 0;
			for(String key : params.keySet()){
				parametersBody[index++] = new NameValuePair(key,params.get(key));
			}
			postMethod.setRequestBody(parametersBody);
		}
		try {
			httpClient.executeMethod(postMethod);
			return postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
		
	}
	
	public static String doGet(String url,Map<String,String> params){
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		if(params != null && params.size() > 0){
			NameValuePair[] queryString = new NameValuePair[params.size()];
			int index = 0;
			for(String key : params.keySet()){
				queryString[index++] = new NameValuePair(key,params.get(key));
			}
			getMethod.setQueryString(queryString);
		}
		try {
			httpClient.executeMethod(getMethod);
			return getMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
