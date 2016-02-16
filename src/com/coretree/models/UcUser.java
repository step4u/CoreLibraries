package com.coretree.models;

public class UcUser {
	private String username;
	private String ext;
	private String role;
	private UcUserState state;
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return this.username;
	}
	
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getExt() {
		return this.ext;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	public String getRole() {
		return this.role;
	}
	
	public void setState(UcUserState state) {
		this.state = state;
	}
	public UcUserState getState() {
		return this.state;
	}
}
