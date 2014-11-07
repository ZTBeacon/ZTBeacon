package com.ztbeacon.client.activity.car;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ztbeacon.client.R;

public class CarActivity extends Activity{

	private RelativeLayout relCarNavi,relCarRe;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car);
		relCarNavi = (RelativeLayout) findViewById(R.id.car_rl_navi);
		relCarRe = (RelativeLayout) findViewById(R.id.car_rl_re);
		relCarNavi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(CarActivity.this, "车位导航", Toast.LENGTH_SHORT).show();
			}
		});
		relCarRe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(CarActivity.this, "车位记录", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
