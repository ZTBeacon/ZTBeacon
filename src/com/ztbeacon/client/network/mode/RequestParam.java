package com.ztbeacon.client.network.mode;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ztbeacon.client.entity.ClientInfo;
import com.ztbeacon.client.entity.UserEnyity;

import android.util.Log;

/**
 * 请求服务器的参数
 * 
 *
 */
public class RequestParam {
	
	public static final String USER_NAME = "username";//其实是手机号
	public static final String PASSWORD = "password";
	public static final String RANDOM_KEY = "randomKey";
	public static final String REQUEST_TYPE = "requestType";
	public static final String PARAMS = "params";
	public static final String SEX = "sex";
	public static final String ADDR = "address";
	public static final String NAME = "name";
	public static final String PHOTO = "photo";

	public static final String STATUS = "loginStatus";
	
	public static final int ONLINE = 0;
	public static final int OFFLINE = 1;
	
	public static final int SEND_TOPIC = 111;
	
	/**
	 * 登录
	 */
	public static final String LOGIN = "/ServiceAPI/user/token/token.php";
	
	/**
	 * 注销
	 */
	public static final String LOGOUT = "Logout";
	
	/**
	 * 定时访问服务器
	 */
	public static final String UPDATE_INFO = "update_info";

	/**
	 * 获取Message
	 */
	public static final String GET_MESSAGE = "/ServiceAPI/shop/";
	
	/**
	 * 获取商家列表
	 */
	public static final String GET_STORE_LIST = "/ServiceAPI/shop/list/";

	/**
	 * 注册
	 */
	public static final String SIGNIN = "Signin";
	
	/**
	 * 检查更新
	 */

	/**
	 * 登录名
	 */
	private String userName;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 随机字符串
	 */
	private String randomKey;
	
	/**
	 * 请求类型
	 */
	private String requestType;	
	
	/**
	 * 请求参数
	 */
	private Object params[];
	
	/**
	 * 所在
	 */
	private String address;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRandomKey(String randomKey) {
		this.randomKey = randomKey;
	}	
	
	public void setParams(Object[] params) {
		this.params = params;
	}
	
	public void setRequestType( String requestType ) {
		this.requestType = requestType;
	}
	
	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getRandomKey() {
		return randomKey;
	}

	public String getRequestType() {
		return requestType;
	}	
	
	public String getJSON() {
		
		JSONObject object = new JSONObject();
		try {
/*			object.put( RequestParam.USER_NAME, this.userName );
			object.put( RequestParam.PASSWORD, this.password );
			object.put( RequestParam.RANDOM_KEY, this.randomKey );
			object.put( RequestParam.REQUEST_TYPE,this.requestType );
			object.put( RequestParam.ADDR,this.address);
			JSONArray jsonArray = new JSONArray();
			
			for(Object param : params) {
				jsonArray.put(param);
			}
			object.put(RequestParam.PARAMS, jsonArray);*/
			
			//UserEnyity user = new UserEnyity(this.userName,this.password);
			JSONObject user = new JSONObject();
			user.put("username", "test");
			user.put("password", "cc03e747a6afbbcbf8be7668acfebee5");
			//ClientInfo client = new ClientInfo("Android","1.0");
			JSONObject client = new JSONObject();
			client.put("ostype", "Android");
			client.put("version", "1.0");
			object.put("user", user);
			object.put("client", client);
			System.out.println("请求参数1"+object.toString());
			return object.toString();
			
		} catch (Exception e) {
			Log.e( "RequestParam", "构建发送请求参数出错", e );
			return "";
		}
	}
}