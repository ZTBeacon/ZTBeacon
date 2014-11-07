package com.ztbeacon.client.entity;

public class ClientInfo {
	public ClientInfo(){}
	public ClientInfo(String ostype,String version){
		this.ostype = ostype;
		this.version = version;
	}
	public String getOstype() {
		return ostype;
	}
	public void setOstype(String ostype) {
		this.ostype = ostype;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	private String ostype;
	private String version;
}
