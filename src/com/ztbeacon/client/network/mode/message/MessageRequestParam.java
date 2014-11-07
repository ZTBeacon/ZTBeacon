package com.ztbeacon.client.network.mode.message;

import java.text.DecimalFormat;

import org.json.JSONObject;

import android.util.Log;

import com.ztbeacon.client.entity.Position;

public class MessageRequestParam {
	private String token;
	private String marketId;
	private Position position;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public String getJSON() {
		
		JSONObject object = new JSONObject();
		JSONObject pos = new JSONObject();
		try {
			object.put( "token", this.token );
			object.put( "marketid", this.marketId );
			pos.put("pos_x", new DecimalFormat("#.#").format(this.position.getPos_x()));
			pos.put("pos_y", new DecimalFormat("#.#").format(this.position.getPos_y()));
			pos.put("pos_z", new DecimalFormat("#.#").format(this.position.getPos_z()));
			pos.put("accuracy", this.position.getAccuracy());
			object.put("position", pos);
			System.out.println("获取消息请求参数:-----"+object.toString());
			return object.toString();
		} catch (Exception e) {
			Log.e( "RequestParam", "构建发送请求参数出错", e );
			return "";
		}
	}
}
