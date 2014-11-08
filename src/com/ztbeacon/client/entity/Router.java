package com.ztbeacon.client.entity;
/**
 * @author  王小红  E-mail:493026465@qq.com
 * @version 创建时间：2014年11月5日 下午4:18:44
 * 类说明
 */
public class Router {
	private String ibeaconid;
	private double pos_x;
	private double pos_y;
	private double pos_z;
	public String getIbeaconid() {
		return ibeaconid;
	}
	public void setIbeaconid(String ibeaconid) {
		this.ibeaconid = ibeaconid;
	}
	public double getPos_x() {
		return pos_x;
	}
	public void setPos_x(double pos_x) {
		this.pos_x = pos_x;
	}
	public double getPos_y() {
		return pos_y;
	}
	public void setPos_y(double pos_y) {
		this.pos_y = pos_y;
	}
	public double getPos_z() {
		return pos_z;
	}
	public void setPos_z(double pos_z) {
		this.pos_z = pos_z;
	}
}
