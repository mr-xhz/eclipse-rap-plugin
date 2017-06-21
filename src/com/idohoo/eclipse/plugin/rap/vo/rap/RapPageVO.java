package com.idohoo.eclipse.plugin.rap.vo.rap;

import java.util.List;

public class RapPageVO {

	private Integer id;
	
	private String name;
	
	private List<RapActionVO> actionList;
	
	private String introduction;

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

	

	public List<RapActionVO> getActionList() {
		return actionList;
	}

	public void setActionList(List<RapActionVO> actionList) {
		this.actionList = actionList;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	
	
	
}
