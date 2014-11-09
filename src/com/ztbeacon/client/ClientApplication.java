package com.ztbeacon.client;
import java.util.ArrayList;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.SensoroBeaconManager;
import com.sensoro.beacon.kit.SensoroBeaconManager.BeaconManagerListener;
import com.ztbeacon.client.activity.store.StoreInfoActivity;
import com.ztbeacon.client.database.DatabaseHelper;
import com.ztbeacon.client.network.mode.RequestParam;
import com.ztbeacon.client.service.LocService;

public class ClientApplication extends Application{
	
	/**
	 * 请求协议
	 */
	public static final String HTTP = "http";
	
	/**
	 * 服务器地址
	 */
	public static final String IP_ADDRESS = "112.11.119.154";
	
	/**
	 * 服务器端口
	 */
	public static final int PORT = 80;
	
	/**
	 * 请求的文件
	 */
	//public static final String FILE = "/ServiceAPI/user/token/token.php";
	
	
	/**
	 * 服务器端XML升级文件
	 */
	public static final String UPDATE_FILE = "/ztbeacon/updateInfo.xml";
	/**
	 * 本地保存图片目录
	 */
	public static final String mLocalSavePath = Environment.getExternalStorageDirectory() + "/SyncImage/";
	
	private DatabaseHelper databaseHelper;
	// 语音合成对象O
	public static SpeechSynthesizer mTts;
	// 引擎类型
	public static String mEngineType = SpeechConstant.TYPE_CLOUD;
	private SharedPreferences loginUserInfo;
	private SensoroBeaconManager beaconManager;
	private NotificationManager notiManeger;
	private static ArrayList<SubBeacon> subBeacons = new ArrayList<ClientApplication.SubBeacon>();
	public static boolean isSearchOpen=false,isSaveSubBeacons,isReady=true,isVoice=false;
	@Override
	public void onCreate() {
		super.onCreate();
		this.getApplicationContext().bindService(new Intent(this.getApplicationContext(),
				LocService.class), ClientApplication.this.serviceConnection,
				Context.BIND_AUTO_CREATE);
		databaseHelper = new DatabaseHelper(this.getApplicationContext(), "client.db", null, 3);
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
		setParam();
		ClientApplication.mTts.startSpeaking("欢迎光临", null);
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "BLE is not supported", Toast.LENGTH_SHORT)
					.show();
		}else{
		beaconManager = SensoroBeaconManager.getInstance(this);
		notiManeger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		beaconManager.setBeaconManagerListener(beaconManagerListener);
		try {
			beaconManager.startService();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	BeaconManagerListener beaconManagerListener = new BeaconManagerListener() {
		@Override
		public void onUpdateBeacon(ArrayList<Beacon> arg0) {
			for (int i = 0; i < arg0.size(); i++) {
				if (arg0.get(i).getRssi() > -65) {
					isSaveSubBeacons = true;
					// 发现一个beacon在0.5米内，且还未存在于subBeacons内时，将其major存入subBeacons
					for (int j = 0; j < subBeacons.size(); j++) {
						if (subBeacons.get(j).getMajor() == arg0.get(i)
								.getMajor()) {
							isSaveSubBeacons = false;
						}
					}
					if (isSaveSubBeacons) {
						SubBeacon beacon = new SubBeacon();
						beacon.setMajor(arg0.get(i).getMajor());
						subBeacons.add(beacon);
						push("欢迎使用", beacon.getMajor());
					}
				} else if (arg0.get(i).getRssi() <= -70) {
					// 发现一个beacon 原先在0.5米内 后来出来0.5米 就移除出subBeacon
					for (int j = 0; j < subBeacons.size(); j++) {
						if (subBeacons.get(j).getMajor() == arg0.get(i)
								.getMajor()) {
							subBeacons.remove(j);
						}
					}
				}
			}
		}

		@Override
		public void onNewBeacon(Beacon arg0) {
			// TODO Auto-generated method stub
			Log.d("sensorof", "---------------start-----------");
			Log.d("sensorof", "Uuid:" + arg0.getProximityUuid());
			Log.d("sensorof", "Major:" + arg0.getMajor());
			Log.d("sensorof", "Minor:" + arg0.getMinor());
			Log.d("sensorof", "Rssi:" + arg0.getRssi());
			Log.d("sensorof", "Distance:" + arg0.getDistance());
			Log.d("sensorof", "----------------over------------");
		}

		@Override
		public void onGoneBeacon(Beacon arg0) {
			// TODO Auto-generated method stub
			Log.d("sensorol", "---------------start-----------");
			Log.d("sensorol", "Uuid:" + arg0.getProximityUuid());
			Log.d("sensorol", "Major:" + arg0.getMajor());
			Log.d("sensorol", "Minor:" + arg0.getMinor());
			Log.d("sensorol", "----------------over------------");
		}
	};
	@SuppressWarnings("deprecation")
	void push(String msg, int id) {
		isVoice = true;
		isReady = false;
		Notification notification = new Notification(R.drawable.ic_launcher,
				msg, System.currentTimeMillis());
		Intent intent = new Intent(getApplicationContext(), StoreInfoActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(
				getApplicationContext(), 100, intent, 0);
		notification.setLatestEventInfo(getApplicationContext(), "通知标题",
				"你靠近了Beacon" + Integer.toHexString(id), contentIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notiManeger.notify(id, notification);
	}

	class SubBeacon {
		int major;

		public int getMajor() {
			return major;
		}

		public void setMajor(int major) {
			this.major = major;
		}
	}
	
	public DatabaseHelper getDatabaseHelper(){		
		return this.databaseHelper;		
	}
	
	public SharedPreferences getLoginUserInfo(){
		SharedPreferences shared = this.getSharedPreferences("lastest_login", Context.MODE_PRIVATE);
		String name = shared.getString(RequestParam.USER_NAME, "");		
		this.loginUserInfo = this.getSharedPreferences(name, Context.MODE_PRIVATE);
		return this.loginUserInfo;		
	}
	
	public void setLoginUserInfo(String name) {
		SharedPreferences shared = this.getSharedPreferences("lastest_login", Context.MODE_PRIVATE);
		shared.edit().putString(RequestParam.USER_NAME, name).commit();
	}
	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			try {
				System.out.println("### Service Connect Success . service = "
						+ service.getInterfaceDescriptor());
			} catch (RemoteException e) {
			}
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			System.out.println("### Service Connect Failure.");
		}
	};
	@Override
	public void onTerminate() {
		this.getApplicationContext().unbindService(ClientApplication.this.serviceConnection);
		if (mTts != null) {
			mTts.stopSpeaking();
			// 退出时释放连接
			mTts.destroy();
		}
		super.onTerminate();
	}
	/**
	 * 初期化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d("InitListener", "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				System.out.println("初始化失败,错误码：" + code);
			}
		}
	};

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	private void setParam() {

		// 设置合成
		if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_CLOUD);
			// 设置发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		} else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_LOCAL);
			// 设置发音人 voicer为空默认通过语音+界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME, "");
		}

		// 设置语速
		mTts.setParameter(SpeechConstant.SPEED, "50");

		// 设置音调
		mTts.setParameter(SpeechConstant.PITCH, "50");

		// 设置音量
		mTts.setParameter(SpeechConstant.VOLUME, "50");

		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
	}
	
}