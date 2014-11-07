package com.ztbeacon.client.entity;


public class Position {
	private float pos_x;
	private float pos_y;
	private float pos_z;
	private int accuracy;
	public Position(){}
	public Position(float x,float y,float z,int accuracy){
		this.pos_x = x;
		this.pos_y = y;
		this.pos_z = z;
		this.accuracy = accuracy;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	public float getPos_x() {
		return pos_x;
	}
	public void setPos_x(float pos_x) {
		this.pos_x = pos_x;
	}
	public float getPos_y() {
		return pos_y;
	}
	public void setPos_y(float pos_y) {
		this.pos_y = pos_y;
	}
	public float getPos_z() {
		return pos_z;
	}
	public void setPos_z(float pos_z) {
		this.pos_z = pos_z;
	}
}
