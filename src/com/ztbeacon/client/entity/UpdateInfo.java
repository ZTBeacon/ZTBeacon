package com.ztbeacon.client.entity;

public class UpdateInfo {
private String version;  //服务器版本号
public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getApkurl() {
	return apkurl;
}
public void setApkurl(String apkurl) {
	this.apkurl = apkurl;
}
private String description;  //升级提示
private String apkurl;  //下载地址
}
