package com.idohoo.eclipse.plugin.rap.vo.rap;

import java.util.List;

public class RapProjectDataVO {

	private String version;
	
	private String createDateStr;
	
	private Integer id;
	
	private String introduction;
	
	private String name;
	
	private RapUserVO user;
	
	private List<RapModuleVO> moduleList;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RapUserVO getUser() {
		return user;
	}

	public void setUser(RapUserVO user) {
		this.user = user;
	}

	public List<RapModuleVO> getModuleList() {
		return moduleList;
	}

	public void setModuleList(List<RapModuleVO> moduleList) {
		this.moduleList = moduleList;
	}
	
	
	
	
}
