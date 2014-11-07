package com.ztbeacon.client.network.mode.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

import com.ztbeacon.client.network.mode.ResponseParam;

public class GetMessageResponseParam extends ResponseParam{
	
	private JSONArray array;
	
	public GetMessageResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		// 对于成功返回的json字符串获取其返回内容（content）
		if (super.getResult() == ResponseParam.RESULT_SUCCESS) {
			this.array = super.jsonObject.getJSONArray("shops");
		}
	}
	public List<ContentValues> getMessageEntity(){
		List<ContentValues> values = new ArrayList<ContentValues>();
		ContentValues msg;
		System.out.println("GetMessageResponseParam:------"+this.array);
		for (int i = 0; i < array.length(); i++) {
			msg = new ContentValues();
			try {
				JSONObject object = array.getJSONObject(i);
				//(new Random().nextInt(100))测试使用
				msg.put("id", object.getString("id")+(new Random().nextInt(100)));
				msg.put("name",object.getString("name"));
				msg.put("description",object.getString("description"));
				msg.put("telephone",object.getString("telephone"));
				msg.put("address",object.getString("address"));
				msg.put("category",object.getString("category"));
				msg.put("url",object.getString("url"));
				msg.put("detail",object.getString("detail"));
				String pos = object.getJSONObject("position").getString("pos_x") +","
							+object.getJSONObject("position").getString("pos_y") +","
							+object.getJSONObject("position").getString("pos_z") +",";
				msg.put("position",pos);
				msg.put("ready", (new Random().nextInt(10000)));
				msg.put("zan", (new Random().nextInt(1000)));
				values.add(msg);
			} catch (JSONException e) {
				System.out.println("获得消息出错：===" + e.toString());
				e.printStackTrace();
			}
		}
		return values;
	}
}
