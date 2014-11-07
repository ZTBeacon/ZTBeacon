package com.ztbeacon.client.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.iflytek.cloud.ErrorCode;
import com.ztbeacon.client.ClientApplication;
import com.ztbeacon.client.R;
import com.ztbeacon.client.activity.car.CarActivity;
import com.ztbeacon.client.activity.home.HomeActivity;
import com.ztbeacon.client.activity.map.MapActivity;
import com.ztbeacon.client.activity.search.SearchActivity;

//tabHost类
@SuppressWarnings("deprecation")
public class TabHostActivity extends TabActivity {
	private TabHost tabHost;
	private LinearLayout[] tab_ll;
	private Runnable reciveMsg;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==0)
				tab_ll[1].setBackgroundColor(Color.rgb(53, 4,25));
			else
				tab_ll[1].setBackgroundColor(Color.rgb(143, 12,66));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_host);
		tabHost = getTabHost();
		initTab();
		tab_ll = new LinearLayout[4];
		tab_ll[0] = (LinearLayout) findViewById(R.id.tab_ll_home);
		tab_ll[1] = (LinearLayout) findViewById(R.id.tab_ll_message);
		tab_ll[2] = (LinearLayout) findViewById(R.id.tab_ll_map);
		tab_ll[3] = (LinearLayout) findViewById(R.id.tab_ll_car);
		setBackgroundColor(0);
		for (int i = 0; i < tab_ll.length; i++) {
			tab_ll[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v.getId() == R.id.tab_ll_home) {
						tabHost.setCurrentTab(0);
						setBackgroundColor(0);
						ClientApplication.isSearchOpen = false;
					} else if (v.getId() == R.id.tab_ll_message) {
						tabHost.setCurrentTab(1);
						ClientApplication.isSearchOpen = true;
						handler.removeCallbacks(reciveMsg);
						setBackgroundColor(1);
					} else if (v.getId() == R.id.tab_ll_map) {
						tabHost.setCurrentTab(2);
						setBackgroundColor(2);
						ClientApplication.isSearchOpen = false;
					} else if (v.getId() == R.id.tab_ll_car) {
						tabHost.setCurrentTab(3);
						setBackgroundColor(3);
						ClientApplication.isSearchOpen = false;
					}
				}
			});
		}
		
		reciveMsg = new Runnable() {
			@Override
			public void run() {
				boolean flag = false;
				while (true) {
					try {
						if (!ClientApplication.isSearchOpen&&!ClientApplication.isReady) {
							if (flag) {
								handler.sendEmptyMessage(1);
							} else {
								handler.sendEmptyMessage(0);
							}
							flag = !flag;
							Thread.sleep(200);
						} else {
							Thread.sleep(200);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(reciveMsg).start();
		//语音播报
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						if (ClientApplication.isVoice) {
							int code = ClientApplication.mTts.startSpeaking(
									"您有一条优惠消息,请查收", null);
							Log.e("TabHostThread--", code+"");
							if (code != ErrorCode.SUCCESS) {
									System.out.println("语音合成失败,错误码: " + code);
							}
							Thread.sleep(1500);
							ClientApplication.isVoice = false;
						} else {
							Thread.sleep(200);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	private void initTab() {
		tabHost.addTab(tabHost.newTabSpec("a")
				.setIndicator("HOME")
				.setContent(new Intent(TabHostActivity.this, HomeActivity.class)));// 写入需要跳转的activity1
		tabHost.addTab(tabHost.newTabSpec("b")
				.setIndicator("MSG")
				.setContent(
				new Intent(TabHostActivity.this, SearchActivity.class)));// 写入需要跳转的activity2
		tabHost.addTab(tabHost.newTabSpec("c")
				.setIndicator("MAP")
				.setContent(
				new Intent(TabHostActivity.this, MapActivity.class)));// 写入需要跳转的activity3
		tabHost.addTab(tabHost.newTabSpec("d")
				.setIndicator("CAR")
				.setContent(
				new Intent(TabHostActivity.this, CarActivity.class)));// 写入需要跳转的activity4

	}


	private void setBackgroundColor(int clickNum) {
		for (int i = 0; i < tab_ll.length; i++) {
			if (i == clickNum)
				tab_ll[i].setBackgroundColor(Color.rgb(53, 4, 25)); // 选中
			else
				tab_ll[i].setBackgroundColor(Color.rgb(143, 12, 66));// 未选中
		}
	}

/*	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(beaconManager!=null){
			beaconManager.stopService();
			beaconManager = null;
		}
	}*/
}
