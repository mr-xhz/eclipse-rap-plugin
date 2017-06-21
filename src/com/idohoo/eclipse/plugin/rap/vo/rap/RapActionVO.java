package com.idohoo.eclipse.plugin.rap.vo.rap;

import java.util.List;

public class RapActionVO {

	private String description;
	private Integer id;
	private String name;
	private String requestType;
	private String requestUrl;
	private String responseTemplate;
	private List<RapParamVO> requestParameterList;
	private List<RapParamVO> responseParameterList;
	private Integer pageId;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getResponseTemplate() {
		return responseTemplate;
	}
	public void setResponseTemplate(String responseTemplate) {
		this.responseTemplate = responseTemplate;
	}
	public List<RapParamVO> getRequestParameterList() {
		return requestParameterList;
	}
	public void setRequestParameterList(List<RapParamVO> requestParameterList) {
		this.requestParameterList = requestParameterList;
	}
	public List<RapParamVO> getResponseParameterList() {
		return responseParameterList;
	}
	public void setResponseParameterList(List<RapParamVO> responseParameterList) {
		this.responseParameterList = responseParameterList;
	}
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
	
	
}
