package com.idohoo.eclipse.plugin.rap.vo.rap;

/**
 * @author xhz
 *
 */
public class RapProjectVO {
	private Integer id;
	private Boolean isManagable;
	private String accounts;
	private String desc;
	private String status;
	private String name;
	private Boolean isDeletable;
	private Integer teamId;
	private Boolean related;
	private Object creator;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getIsManagable() {
		return isManagable;
	}
	public void setIsManagable(Boolean isManagable) {
		this.isManagable = isManagable;
	}
	public String getAccounts() {
		return accounts;
	}
	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsDeletable() {
		return isDeletable;
	}
	public void setIsDeletable(Boolean isDeletable) {
		this.isDeletable = isDeletable;
	}
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public Boolean getRelated() {
		return related;
	}
	public void setRelated(Boolean related) {
		this.related = related;
	}
	public Object getCreator() {
		return creator;
	}
	public void setCreator(Object creator) {
		this.creator = creator;
	}
	@Override
	public String toString() {
		return "RapProjectVO [id=" + id + ", isManagable=" + isManagable + ", accounts=" + accounts + ", desc=" + desc
				+ ", status=" + status + ", name=" + name + ", isDeletable=" + isDeletable + ", teamId=" + teamId
				+ ", related=" + related + ", creator=" + creator + "]";
	}
	
	
}
