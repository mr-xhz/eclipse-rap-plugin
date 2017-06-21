package com.idohoo.eclipse.plugin.rap.vo.rap;

import java.util.List;

public class RapParamVO {

	private String dataType;
	private Integer id;
	private String identifier;
	private String name;
	private List<RapParamVO> parameterList;
	private String remark;
	private String validator;
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<RapParamVO> getParameterList() {
		return parameterList;
	}
	public void setParameterList(List<RapParamVO> parameterList) {
		this.parameterList = parameterList;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getValidator() {
		return validator;
	}
	public void setValidator(String validator) {
		this.validator = validator;
	}
	
	
}
