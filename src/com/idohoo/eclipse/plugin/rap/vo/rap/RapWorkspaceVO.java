package com.idohoo.eclipse.plugin.rap.vo.rap;

import java.util.List;

public class RapWorkspaceVO {

	private List<RapCheckVO> checkList;
	
	private RapProjectDataVO projectData;
	
	private String relatedIds;

	public List<RapCheckVO> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<RapCheckVO> checkList) {
		this.checkList = checkList;
	}

	public RapProjectDataVO getProjectData() {
		return projectData;
	}

	public void setProjectData(RapProjectDataVO projectData) {
		this.projectData = projectData;
	}

	public String getRelatedIds() {
		return relatedIds;
	}

	public void setRelatedIds(String relatedIds) {
		this.relatedIds = relatedIds;
	} 
	
	
	
}
