package com.ztbeacon.client.network.mode.message;

import org.json.JSONObject;

import android.util.Log;

public class StoreInfoRequestParam {
	private String token;
	private String shopId;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getJSON() {
		
		JSONObject object = new JSONObject();
		try {
			object.put( "token", this.token );
			object.put( "shopid", this.shopId );
			System.out.println("获取消息请求参数:-----"+object.toString());
			return object.toString();
		} catch (Exception e) {
			Log.e( "RequestParam", "构建发送请求参数出错", e );
			return "";
		}
	}
}
