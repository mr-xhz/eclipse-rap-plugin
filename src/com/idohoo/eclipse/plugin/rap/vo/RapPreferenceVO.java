package com.idohoo.eclipse.plugin.rap.vo;

import com.idohoo.eclipse.plugin.rap.preferences.PreferenceConstants;
import com.idohoo.eclipse.plugin.rap.util.PreferenceField;

/**
 * 首选项
 * @author xhz
 *
 */
public class RapPreferenceVO {

	@PreferenceField(PreferenceConstants.P_RAP_URL)
	private String url;
	
	@PreferenceField(PreferenceConstants.P_RAP_USERNAME)
	private String username;
	
	@PreferenceField(PreferenceConstants.P_RAP_PASSWORD)
	private String password;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
