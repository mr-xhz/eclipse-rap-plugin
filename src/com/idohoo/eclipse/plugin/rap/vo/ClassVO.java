package com.idohoo.eclipse.plugin.rap.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassVO {

	/**
	 * 本地文件地址
	 */
	private String location;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 包名
	 */
	private String classPackage;

	/**
	 * import
	 */
	private Map<String, String> classImport = new HashMap<String, String>();

	/**
	 * 类名
	 */
	private String className;
	
	/**
	 * 包含名
	 */
	public String fullClassName;
	
	/**
	 * 父类
	 */
	private String parentClass;
	
	/**
	 * 接口
	 */
	private String parentInterface;

	/**
	 * 地址前缀
	 */
	private String url = "";

	/**
	 * 方法
	 */
	private List<ClassMethodVO> methods = new ArrayList<ClassMethodVO>();
	
	/**
	 * 属性
	 */
	private List<ClassFieldVO> fields = new ArrayList<ClassFieldVO>();

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getClassPackage() {
		return classPackage;
	}

	public void setClassPackage(String classPackage) {
		this.classPackage = classPackage;
	}

	public Map<String, String> getClassImport() {
		return classImport;
	}

	public void setClassImport(Map<String, String> classImport) {
		this.classImport = classImport;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<ClassMethodVO> getMethods() {
		return methods;
	}

	public void setMethods(List<ClassMethodVO> methods) {
		this.methods = methods;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentClass() {
		return parentClass;
	}

	public void setParentClass(String parentClass) {
		this.parentClass = parentClass;
	}

	public List<ClassFieldVO> getFields() {
		return fields;
	}

	public void setFields(List<ClassFieldVO> fields) {
		this.fields = fields;
	}

	public String getParentInterface() {
		return parentInterface;
	}

	public void setParentInterface(String parentInterface) {
		this.parentInterface = parentInterface;
	}

	public String getFullClassName() {
		return fullClassName;
	}

	public void setFullClassName(String fullClassName) {
		this.fullClassName = fullClassName;
	}
	
	
	
	
}
