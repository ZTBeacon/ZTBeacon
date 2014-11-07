package com.ztbeacon.client.activity;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ztbeacon.client.R;
import com.ztbeacon.client.network.HttpClient;
import com.ztbeacon.client.network.Request;
import com.ztbeacon.client.network.mode.LoginoutResponseParam;
import com.ztbeacon.client.network.mode.RequestParam;

public class LoginActivity extends Activity {
	private int screenWidth;
	private RelativeLayout user_box,pass_box,regist_box;
	private Button login_bt,regist_bt,noname_bt;
	private EditText username,password;
	private LoginTask mLoginTask;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_box=(RelativeLayout) findViewById(R.id.user_box);
        pass_box=(RelativeLayout) findViewById(R.id.pass_box);
        login_bt=(Button) findViewById(R.id.login_bt);
        regist_box=(RelativeLayout) findViewById(R.id.regist_box);
        regist_bt=(Button) findViewById(R.id.regist);
        noname_bt=(Button) findViewById(R.id.noname_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
      //获取屏幕的分辨率
      		DisplayMetrics dm = new DisplayMetrics();
      		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
      		screenWidth = dm.widthPixels;
            //Toast.makeText(LoginActivity.this, screenWidth+"进入下一页"+screenHeight, Toast.LENGTH_SHORT).show();
            setViewWidth(user_box,5,4);
            setViewWidth(pass_box,5,4);
            setViewWidth(login_bt,5,4);
            setViewWidth(regist_box,5,4);
            setViewWidth(regist_bt,25,9);
            setViewWidth(noname_bt,25,9);
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
  //设置自适应屏幕输入框等的方法
    public void setViewWidth(View relat,int scale1,int scale2){
        RelativeLayout.LayoutParams tparams =  (RelativeLayout.LayoutParams)relat.getLayoutParams();
        tparams.width=screenWidth/scale1*scale2;
        relat.setLayoutParams(tparams);
    }
    public void BtClick(View view){
    	Intent intent;
    	if(view.getId()==R.id.login_bt){
    		if(TextUtils.isEmpty(username.getText().toString())) {
    			username.setError(getString(R.string.no_empyt_name));
    			return;
    		}
    		
    		if(TextUtils.isEmpty(password.getText().toString())) {
    			password.setError(getString(R.string.no_empty_password));
    			return;
    		}		
    		RequestParam requestParam = new RequestParam();
    		requestParam.setUserName(username.getText().toString());
    		requestParam.setPassword(password.getText().toString());
    		requestParam.setRequestType(requestParam.LOGIN);
    		requestParam.setRandomKey("1234");
    		requestParam.setParams(new String[]{""});
    		
    		mLoginTask = new LoginTask();
    		mLoginTask.execute(requestParam);
    		
    	}else if(view.getId()==R.id.noname_login){
    		intent = new Intent(LoginActivity.this, TabHostActivity.class);
    		startActivity(intent);
    	}else{
    		intent = new Intent(LoginActivity.this, RegistActivity.class);
    		startActivity(intent);
    	}
    }
	private void sendLoginBroadCast() {
		//开启广播
	}	
	/**
	 * 请求登录的Task
	 *
	 */
	public class LoginTask extends AsyncTask<RequestParam, Integer, Boolean> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(LoginActivity.this, "正常登陆~.~",
					getText(R.string.waiting));
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(RequestParam... param) {

			if(!HttpClient.isConnect(LoginActivity.this)) {
				return false;
			}
			
			RequestParam requestParam = param[0];
			String res = Request.request(requestParam.getJSON(),RequestParam.LOGIN);
			System.out.println("RES:"+res);
			//如果请求结果为空字符串，则请求失败
			if ("".equals(res)) {
				return false;
			}
			try {
				LoginoutResponseParam response = new LoginoutResponseParam(res);
				if (response.getResult() != LoginoutResponseParam.RESULT_SUCCESS) {
					return false;
				}
				//System.out.println("--------TOKEN:"+response.getToken());
				/*// 存储用户登录信息
				clientApplication.setLoginUserInfo(username.getText().toString());
				SharedPreferences sharedPreferences = clientApplication.getLoginUserInfo();
				Editor editor = sharedPreferences.edit();
				editor.putString(RequestParam.USER_NAME, username.getText().toString());
				editor.putString(RequestParam.PASSWORD, password.getText().toString());
				editor.putString(RequestParam.ADDR, response.getPersonAddress());
				editor.putString(RequestParam.SEX, response.getPersonSex());
				editor.putString(RequestParam.NAME, response.getPersonName());
				editor.putString(RequestParam.PHOTO, response.getPersonPhoto());
				editor.putInt(RequestParam.STATUS, RequestParam.ONLINE);
				editor.commit();*/
				return true;
			} catch (JSONException e) {
				System.out.println("登陆异常==="+ e.toString());
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {			
			dialog.dismiss();
			if (result) {
				System.out.println("开始登陆！！！！");
				sendLoginBroadCast();
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, TabHostActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("user", username.getText().toString());
				intent.putExtra("user",bundle);
				// 转向登陆后的页面
				startActivity(intent);
				finish();
			} else {
				System.out.println("登陆失败:");
				//Toast.makeText(LoginActivity.this, "登陆失败:", Toast.LENGTH_SHORT).show();
				//Utils.myToast(LoginActivity.this, getText(R.string.login_fail).toString(), R.drawable.toast_error);
			}
			super.onPostExecute(result);
		}
	}
}
