package com.idohoo.eclipse.plugin.rap.vo;

import java.util.ArrayList;
import java.util.List;

public class ClassFieldVO {

	/**
	 * 名字
	 */
	private String name;
	
	/**
	 * 数据类型
	 */
	private String javaType;
	
	/**
	 * 字段说明
	 */
	private String comment = "";
	
	/**
	 * 是否列表
	 */
	private boolean isList = false;
	
	/**
	 * 如果不是基础类型
	 */
	private List<ClassFieldVO> fields = new ArrayList<ClassFieldVO>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public boolean isList() {
		return isList;
	}

	public void setList(boolean isList) {
		this.isList = isList;
	}

	public List<ClassFieldVO> getFields() {
		return fields;
	}

	public void setFields(List<ClassFieldVO> fields) {
		this.fields = fields;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
