package com.ztbeacon.client.network.mode.message;

import org.json.JSONException;
import org.json.JSONObject;

import com.ztbeacon.client.network.mode.ResponseParam;

public class GetStoreInfoResponseParam extends ResponseParam{
	private JSONObject object;
	
	public GetStoreInfoResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		// 对于成功返回的json字符串获取其返回内容（content）
		if (super.getResult() == ResponseParam.RESULT_SUCCESS) {
			this.object = super.jsonObject.getJSONObject("info");
			System.out.println("商家详情:"+jsonObject.toString());
		}
	}
	public JSONObject getStoreInfo(){
		return this.object;
	}
}
