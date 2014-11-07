package com.ztbeacon.client.network.mode.message;

import org.json.JSONObject;

import android.util.Log;

public class KeyWordRequestParam {
	private String token;
	private String marketId;
	private int maxnum;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getMarketId() {
		return marketId;
	}
	public int getMaxnum() {
		return maxnum;
	}
	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public String getJSON() {
		
		JSONObject object = new JSONObject();
		try {
			object.put( "token", this.token );
			object.put( "marketid", this.marketId );
			object.put("maxnum", this.maxnum);
			System.out.println("获取消息请求参数:-----"+object.toString());
			return object.toString();
		} catch (Exception e) {
			Log.e( "RequestParam", "构建发送请求参数出错", e );
			return "";
		}
	}
}
