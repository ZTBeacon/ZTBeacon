package com.ztbeacon.client.activity.store;

import com.ztbeacon.client.ClientApplication;
import com.ztbeacon.client.R;
import com.ztbeacon.client.activity.navi.NaviActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StoreInfoActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storeinfo);
		ClientApplication.isReady = true;
		//取得bundle对象 
		Bundle bundle = this.getIntent().getExtras();
		String word = bundle.getString("word");
		TextView textView = (TextView) findViewById(R.id.store_name);
		textView.setText(word);
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
