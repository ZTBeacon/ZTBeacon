package com.ztbeacon.client.network.mode;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 解析返回参数
 *
 */
public class ResponseParam {

	//返回json中的标识
	private static final String RESULT = "ResponseHead";
	private static final String RESPONSE_TYPE = "requsetType";
	protected static final String CONTENT = "content";
	private static final String TOKEN = "token";
	
	/**
	 * 开始向服务器发送请求
	 */
	public static final int START_REQUEST = -1;
	
	/**
	 * 网络异常
	 */
	public static final int NET_WORK_ERROR = -2;
	
	/**
	 * 访问服务器失败
	 */
	public static final int REQUEST_FAIL = -3;
	
	/**
	 * 请求成功
	 */
	public static final int RESULT_SUCCESS = 0;
	
	/**
	 * 用户名错误
	 */
	public static final int RESULT_USER_LOGIN_NAME_ERROR = 1;
	
	/**
	 * 密码错误
	 */
	public static final int RESULT_PASSWORD_ERROR = 2;	
	
	/**
	 * 服务器错误
	 */
	public static final int RESULT_SERVER_ERROR = 3;
	
	
	protected JSONObject jsonObject;
		
	
	public ResponseParam( String responseJson ) throws JSONException {
		
		try {
			this.jsonObject = new JSONObject( responseJson );
			System.out.println("TOKEN:"+this.jsonObject.getJSONObject(RESULT).getInt("status")+"--"+
					this.jsonObject.getJSONObject(RESULT).getString("msg"));
		} catch ( JSONException e ) {
			throw e;
		}
	}
	
	

	public int getResult() {
		try {
			return this.jsonObject.getJSONObject(RESULT).getInt("status");
		} catch (JSONException e) {
			e.printStackTrace();
			return ResponseParam.RESULT_SERVER_ERROR;
		}
	}

	public String getRequestType() {
		try {
			return this.jsonObject.getString( RESPONSE_TYPE );
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getContent() {
		try {
			return this.jsonObject.getString( CONTENT );
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getToken() {
		try {
			return this.jsonObject.getString( TOKEN );
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}
}
