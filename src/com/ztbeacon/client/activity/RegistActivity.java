package com.ztbeacon.client.activity;



import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ztbeacon.client.R;
import com.ztbeacon.client.adapter.SpinnerButton;

public class RegistActivity extends Activity {
	int screenWidth ,screenHeight;
	View username,password,sure_password,phone,sex_box,regist;
	private SpinnerButton mSpinnerBtn;
	TextView man,girl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
       /* LayoutInflater layout = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View view = layout.inflate(R.layout.spinner_dropdown_items, null);
       man = (TextView) view.findViewById(R.id.textView1);
       girl = (TextView) view.findViewById(R.id.textView2);*/
        
        username= findViewById(R.id.username);
        password= findViewById(R.id.password);
    	sure_password= findViewById(R.id.sure_password);
    	phone= findViewById(R.id.phone);
    	sex_box= findViewById(R.id.sex_box);
    	regist= findViewById(R.id.regist);
    		//获取屏幕的分辨率
      		DisplayMetrics dm = new DisplayMetrics();
      		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
      		screenWidth = dm.widthPixels;
      		screenHeight = dm.heightPixels;
         //   Toast.makeText(RegistActivity.this, screenWidth+"进入下一页"+screenHeight, Toast.LENGTH_SHORT).show();
            
            setViewWidth(username,5,4);
            setViewWidth(password,5,4);
            setViewWidth(sure_password,5,4);
            setViewWidth(phone,5,4);
            setViewWidth(sex_box,5,4);
            setViewWidth(regist,5,4);
            
           /* LinearLayout.LayoutParams imagebtn_params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                	imagebtn_params.width = screenWidth /5* 4;
                	man.setLayoutParams(imagebtn_params);
                	girl.setLayoutParams(imagebtn_params);
                	Toast.makeText(RegistActivity.this, man.getWidth()+"进入下一页"+girl.getWidth(), Toast.LENGTH_SHORT).show();*/
               /* LinearLayout.LayoutParams Mparams =  (LinearLayout.LayoutParams)man.getLayoutParams();
                Mparams.width=300;
                man.setLayoutParams(Mparams);
                
                
                LinearLayout.LayoutParams Gparams =  (LinearLayout.LayoutParams)girl.getLayoutParams();
                Gparams.width=300;
                girl.setLayoutParams(Gparams);
                Toast.makeText(RegistActivity.this, Gparams.width+"进入下一页"+Gparams.width, Toast.LENGTH_SHORT).show();*/
                
            this.mSpinnerBtn = (SpinnerButton) this
    				.findViewById(R.id.spinner_btn);
    		
    		// 设置下拉布局资源文件,布局创建监听器，以便实例化控件对象
    		mSpinnerBtn.setResIdAndViewCreatedListener(R.layout.spinner_dropdown_items,
    				new SpinnerButton.ViewCreatedListener(){
    					public void onViewCreated(View v) {
    				
    						// TODO Auto-generated method stub
    						v.findViewById(R.id.spinner_tv1).setOnClickListener(new View.OnClickListener() {
    							public void onClick(View v) {
    								// TODO Auto-generated method stub
    								handleClick(((TextView)v).getText().toString());
    							}
    						});
    						v.findViewById(R.id.spinner_tv2).setOnClickListener(new View.OnClickListener() {
    							public void onClick(View v) {
    								// TODO Auto-generated method stub
    								handleClick(((TextView)v).getText().toString());
    							}
    						});
    					}
    			
    		},screenWidth,"regist");
            
            
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

 
    private void handleClick(String text){
		mSpinnerBtn.dismiss();
		mSpinnerBtn.setText(text);
	}
}
