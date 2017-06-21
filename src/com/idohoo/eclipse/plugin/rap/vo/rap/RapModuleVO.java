package com.idohoo.eclipse.plugin.rap.vo.rap;

import java.util.List;

public class RapModuleVO {

	private Integer id;
	
	private String name;
	
	private String introduction;
	
	private List<RapPageVO> pageList;

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

	public List<RapPageVO> getPageList() {
		return pageList;
	}

	public void setPageList(List<RapPageVO> pageList) {
		this.pageList = pageList;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	} 
	
	
	
	
	
}
