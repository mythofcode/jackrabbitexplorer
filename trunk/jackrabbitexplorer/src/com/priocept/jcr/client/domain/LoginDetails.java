package com.priocept.jcr.client.domain;

import java.io.Serializable;

public class LoginDetails implements Serializable {
	private static final long serialVersionUID = 9709731178901836L;

	private String rmiUrl;
	private String workSpace;
	private String userName;
	private String password;
	
	public String getRmiUrl() {
		return rmiUrl;
	}
	public void setRmiUrl(String rmiUrl) {
		this.rmiUrl = rmiUrl;
	}
	public String getWorkSpace() {
		return workSpace;
	}
	public void setWorkSpace(String workSpace) {
		this.workSpace = workSpace;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
