package com.ztbeacon.client.activity.store;

import com.ztbeacon.client.ClientApplication;
import com.ztbeacon.client.R;
import com.ztbeacon.client.activity.navi.NaviActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StoreInfoActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storeinfo);
		ClientApplication.isReady = true;
	}
	public void clickBack(View v){
		this.finish();
	}
	public void ClickNavi(View v){
		Intent next = new Intent();
		next.setClass(this, NaviActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("word", "巴黎春天");
		next.putExtras(bundle);
		next.putExtras(bundle);
		startActivity(next);
		this.finish();
	}
}
