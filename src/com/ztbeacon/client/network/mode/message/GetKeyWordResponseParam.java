package com.ztbeacon.client.network.mode.message;

import org.json.JSONArray;
import org.json.JSONException;

import com.ztbeacon.client.network.mode.ResponseParam;

public class GetKeyWordResponseParam extends ResponseParam{
	private JSONArray array;
	
	public GetKeyWordResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		// 对于成功返回的json字符串获取其返回内容（content）
		if (super.getResult() == ResponseParam.RESULT_SUCCESS) {
			this.array = super.jsonObject.getJSONArray("list");
		}
	}
	public JSONArray getKeyWordJson(){
		return this.array;
	}
}
