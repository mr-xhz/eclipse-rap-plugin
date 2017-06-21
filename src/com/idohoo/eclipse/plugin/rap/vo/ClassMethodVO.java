package com.idohoo.eclipse.plugin.rap.vo;

import java.util.ArrayList;
import java.util.List;

public class ClassMethodVO {

	/**
	 * 说明
	 */
	private String description = "";
	
	/**
	 * 中文名字
	 */
	private String nickname = "";
	
	/**
	 * 方法名
	 */
	private String name;
	
	/**
	 * 返回结果
	 */
	private ClassFieldVO result = new ClassFieldVO();
	
	/**
	 * 作者
	 */
	private String author;
	
	/**
	 * 提交方式
	 */
	private String method = "";
	
	/**
	 * 地址后缀
	 */
	private String url;
	
	/**
	 * 参数
	 */
	private List<ClassFieldVO> params = new ArrayList<ClassFieldVO>();

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public ClassFieldVO getResult() {
		return result;
	}

	public void setResult(ClassFieldVO result) {
		this.result = result;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<ClassFieldVO> getParams() {
		return params;
	}

	public void setParams(List<ClassFieldVO> params) {
		this.params = params;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
	
	
}
