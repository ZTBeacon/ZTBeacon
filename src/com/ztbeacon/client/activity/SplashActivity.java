package com.ztbeacon.client.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ztbeacon.client.ClientApplication;
import com.ztbeacon.client.R;
import com.ztbeacon.client.entity.UpdateInfo;
import com.ztbeacon.client.utils.DownLoadUtil;
import com.ztbeacon.client.utils.UpdateInfoParser;

public class SplashActivity extends Activity {
	private UpdateInfo info;
	private static final int GET_INFO_SUCCESS = 10;
	private static final int SERVER_ERROR = 11;
	private static final int SERVER_URL_ERROR = 12;
	private static final int PROTOCOL_ERROR = 13;
	private static final int IO_ERROR = 14;
	private static final int XML_PARSE_ERROR = 15;
	private static final int DOWNLOAD_SUCCESS = 16;
	private static final int DOWNLOAD_ERROR = 17;
	protected static final String TAG = "SplashActivity";
	private long startTime,endTime;
	private RelativeLayout rl_splash;
	private TextView tv_splash_version;
	private ProgressDialog pd ;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case SERVER_ERROR:
				Toast.makeText(getApplicationContext(), "已是最新版本", Toast.LENGTH_SHORT).show();//服务器内部异常
				loadMainUI();
				break;
			case SERVER_URL_ERROR:
				Toast.makeText(getApplicationContext(), "服务器路径不正确", Toast.LENGTH_SHORT).show();
				loadMainUI();
				break;
			case PROTOCOL_ERROR:
				Toast.makeText(getApplicationContext(), "服务器内部异常", Toast.LENGTH_SHORT).show();
				loadMainUI();
				break;
			case XML_PARSE_ERROR:
				Toast.makeText(getApplicationContext(), "XML解析出错", Toast.LENGTH_SHORT).show();
				loadMainUI();
				break;
			case IO_ERROR:
				Toast.makeText(getApplicationContext(), "请检查网络是否连接", Toast.LENGTH_SHORT).show();
				loadMainUI();
				break;
			case GET_INFO_SUCCESS:
				String serverversion = info.getVersion();
				String currentversion = getVersion();
				if(currentversion.equals(serverversion)){
					Log.i(TAG, "版本号相同进入主界面");
					loadMainUI();
				}else{
					Log.i(TAG, "版本号不相同,升级对话框");
					showUpdateDialog();
				}
				break;
			case DOWNLOAD_SUCCESS:
				Log.i(TAG, "文件下载成功");
				//获得发送过来的消息
				File file = (File) msg.obj;
				//执行文件安装
				installApk(file);
				break;
			case DOWNLOAD_ERROR:
				Toast.makeText(getApplicationContext(), "下载失败!", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置为全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本号:"+getVersion());
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        rl_splash.startAnimation(aa);
        //连接服务器，获取基本信息
        new Thread(new CheckVersionTask()){}.start();
    }
    	/*
    	 * 显示升级对话框
    	 * */
    protected void showUpdateDialog(){
    	//创建对话框的构造器
    	AlertDialog.Builder builder = new Builder(this);
    	//设置对话框提示
    	builder.setIcon(getResources().getDrawable(R.drawable.logo));
    	builder.setTitle("升级提示");
    	builder.setMessage(info.getDescription());
    	pd = new ProgressDialog(SplashActivity.this);
    	pd.setMessage("正在下载");
    	//指定显示下载进度条为水平形状
    	pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	//设置升级按钮
    	builder.setPositiveButton("升级", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG,"升级，下载" + info.getApkurl());
				//判断Sdcard是否存在
				if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
					pd.show();
					new Thread(){
						public void run() {
							String path = info.getApkurl();
							String fileName = DownLoadUtil.getFileName(path);
							//在SD卡上创建一个文件
							File file = new File(Environment.getExternalStorageDirectory(),fileName);
							//得到下载后的APK文件
							file = DownLoadUtil.getFile(path,file.getAbsolutePath(),pd);
							if(file != null){
								//向主线程发送下载成功的消息
								Message msg = Message.obtain();
								msg.what = DOWNLOAD_SUCCESS;
								msg.obj = file;
								SplashActivity.this.handler.sendMessage(msg);
							}else{
								//向主线程发送下载失败的消息
								Message msg = Message.obtain();
								msg.what = DOWNLOAD_ERROR;
								SplashActivity.this.handler.sendMessage(msg);
							}
							pd.dismiss();  //下载结束后，关闭进度条
						}
					}.start();
				}else{
					Toast.makeText(getApplicationContext(), "sd卡不可用", Toast.LENGTH_SHORT).show();
					loadMainUI();//进入程序主界面
				}
			}
		});
    	builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				loadMainUI();//进入程序主界面
			}});
    	builder.create().show();
    	
    }
	//获取当前应用的版本号
	private String getVersion(){
		//得到系统的包管理器，已经得到了apk的面向对象的包装
		PackageManager pm = this.getPackageManager();
		try{
			//参数一:当前应用程序的包名;    参数二:可选的附加消息,这里用不到，可以定义为0
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			//返回当前应用程序的版本号
			return info.versionName;
		}catch (Exception e) { //理论上，该异常不可能发生
			e.printStackTrace();
			return "";
		}
	}
	/*
	 * 连接检查应用的版本号与服务器上的版本号是否相同
	 * */
	public class CheckVersionTask implements Runnable {
		public void run() {
			startTime = System.currentTimeMillis();
			Message msg = Message.obtain();
			try{
			String serverurl = ClientApplication.HTTP+"://"+ClientApplication.IP_ADDRESS+":"+ClientApplication.PORT+ClientApplication.UPDATE_FILE;
			//http://10.21.204.6:8080/SecureSplash/info.xml
			URL url = new URL(serverurl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			int code = conn.getResponseCode();
			if(code == 200){   //200表示与服务器连接成功
				InputStream is = conn.getInputStream();
				info = UpdateInfoParser.getUpdateInfo(is);
				endTime = System.currentTimeMillis();
				resultTime(startTime,endTime);
				msg.what = GET_INFO_SUCCESS;
				SplashActivity.this.handler.sendMessage(msg);
			}else{
				msg.what = SERVER_ERROR;
				endTime = System.currentTimeMillis();
				resultTime(startTime,endTime);
				SplashActivity.this.handler.sendMessage(msg);
			}
		}catch(MalformedURLException e){
			e.printStackTrace();
			msg.what = SERVER_URL_ERROR;
			endTime = System.currentTimeMillis();
			resultTime(startTime,endTime);
			SplashActivity.this.handler.sendMessage(msg);
		}catch(ProtocolException e){
			e.printStackTrace();
			msg.what = PROTOCOL_ERROR;
			endTime = System.currentTimeMillis();
			resultTime(startTime,endTime);
			SplashActivity.this.handler.sendMessage(msg);
		}catch(IOException e){
			e.printStackTrace();
			msg.what = IO_ERROR;
			endTime = System.currentTimeMillis();
			resultTime(startTime,endTime);
			SplashActivity.this.handler.sendMessage(msg);
		}catch(XmlPullParserException e){
			e.printStackTrace();
			msg.what = XML_PARSE_ERROR;
			endTime = System.currentTimeMillis();
			resultTime(startTime,endTime);
			SplashActivity.this.handler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	/*
	 * 安装一个apk文件
	 * */
	protected void installApk(File file){
		//隐式意图
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");//设置意图的动作
		intent.addCategory("android.intent.category.DEFAULT");
		//为意图添加额外的数据
		intent.setDataAndType(Uri.fromFile(file),   //要安装的完整文件名
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
	/*
	 * 跳转至MainActivity
	 * */
	private void loadMainUI(){
		Intent in = new Intent(SplashActivity.this,LoginActivity.class);
		SplashActivity.this.startActivity(in);
		SplashActivity.this.finish();
	}
	private void resultTime(long startTime,long endTime){
		long resultTime = endTime - startTime;
		if(resultTime<2000){
		try {
			Thread.sleep(2000-resultTime);
		} catch (InterruptedException e) {
			Log.i(TAG, "子线程睡眠出错");
		}}
	}
}