package com.ztbeacon.client.adapter;

import com.ztbeacon.client.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GView extends LinearLayout {
	
	public ImageView bgimg;
	public TextView txt;
	public ImageView img;
	public ImageView arrarr;
	
	public boolean useable = true;
	
	private Context context;
	
	private float m_pos_x = 1,m_pos_y = 1;
	
	private int state = 2;
	/*0 ->hide
	 *1 ->halfgone
	 *2 ->showup
	 * */
	
	//public RelativeLayout dis_text_box = null;
	//public TextView dis_text = null;
	
	
	
	public void setM_Pos(float x,float y){
		//计算出移动轨迹
		//存储到 m_pos_x m_pos_y
		this.m_pos_x = (x-5) ;
		this.m_pos_y = (y-5) ;
		//计算出显示的距离的框框的方向
		
		
	
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public GView(Context context) {
		super(context);
		this.context = context;
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		this.img = new ImageView(context);
		this.txt = new TextView(context);
		this.bgimg = new ImageView(context);
		this.addView(img,lp);
		this.addView(txt,lp);
		this.addView(bgimg,lp);
		this.bgimg.setImageResource(R.drawable.navi_black_circle);
	}
	
	public GView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // TODO Auto-generated constructor stub
        LayoutInflater.from(context).inflate(R.layout.g_view, this, true);  
        img = (ImageView)findViewById(R.id.img);
        txt = (TextView)findViewById(R.id.txt);
        bgimg = (ImageView)findViewById(R.id.bgimg);
        arrarr = (ImageView)findViewById(R.id.arrarr);
        txt.setVisibility(View.INVISIBLE);
        
      
        rotation = 0;
    }
	
	
	
	////////////////////////////////基本组件动画测试//////////////////////////////////////////
	 
	private int rotation = 0;
	public void rotateTo(int r){
	//0~360
		
		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation ro1 = new RotateAnimation(this.rotation, r*5/4,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		ro1.setDuration(400);
		RotateAnimation ro2 = new RotateAnimation(0, -r/4,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		ro2.setDuration(70);
		ro2.setStartOffset(400);
		animationSet.addAnimation(ro1);
		animationSet.addAnimation(ro2);
		animationSet.setFillAfter(true);
		this.startAnimation(animationSet);
		this.rotation = r;
		
	}
	
	private float scale = 1f;
	public void scaleTo(float d,int time){
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation sc = new ScaleAnimation(this.scale,d,this.scale,d,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		sc.setDuration(time);
		animationSet.addAnimation(sc);
		animationSet.setFillAfter(true);
		this.startAnimation(animationSet);
		this.scale = d;
	}
	
	private float scale_bg = 1f;
	public void scaleBgTo(float d,int time){
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation sc = new ScaleAnimation(this.scale,d,this.scale,d,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		sc.setDuration(time);
		animationSet.addAnimation(sc);
		animationSet.setFillAfter(true);
		this.img.startAnimation(animationSet);
		this.scale_bg = d;
	}
	
	private float alpha = 1f;
	public void alphaTo(float d,int time){
		AnimationSet animationSet = new AnimationSet(true);
		AlphaAnimation al = new AlphaAnimation(this.alpha, d);
		animationSet.addAnimation(al);
		al.setDuration(time);
		animationSet.setFillAfter(true);
		this.startAnimation(animationSet);
		this.alpha = d;
	}
	
	public void moveTo(float x,float y,int time){
		AnimationSet anmationSet = new AnimationSet(true);
		TranslateAnimation tr = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF,0f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0f,
				Animation.RELATIVE_TO_SELF,0.5f
				);
		tr.setDuration(time);
		anmationSet.setFillAfter(true);
		anmationSet.addAnimation(tr);
		this.startAnimation(anmationSet);
	}
	
	///////////////////////////////////////////////////////////////具体动作//////////////////////////
	
	public void showUp(){
		if(useable){
			txt.setVisibility(View.VISIBLE);
			img.setVisibility(View.VISIBLE);
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////
		
		AnimationSet anmationSet = new AnimationSet(true);
		
		if(this.getState() == 0){
			/*
			 * 
			 * */
			float star_sc_num = 0;
			float last_sc_num = 0.6f;
			float star_al_num = 0;
			float last_al_num = 1f;
			int time = 700;
			if(!this.useable){
				last_sc_num = 2.5f;
				last_al_num = 0.05f;
				time=time*3;
			}
			
			
		
			ScaleAnimation sc = new ScaleAnimation(star_sc_num,last_sc_num,star_sc_num,last_sc_num,
					Animation.RELATIVE_TO_SELF,0.5f,
					Animation.RELATIVE_TO_SELF,0.5f);
			sc.setDuration(time);
			anmationSet.addAnimation(sc);
			
			AlphaAnimation al = new AlphaAnimation(star_al_num, last_al_num);
			al.setDuration(time);
			anmationSet.addAnimation(al);
			
			TranslateAnimation tr = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF,this.m_pos_x,
					Animation.RELATIVE_TO_SELF,0f,
					Animation.RELATIVE_TO_SELF,this.m_pos_y,
					Animation.RELATIVE_TO_SELF,0f
					);
			tr.setDuration(time);
			anmationSet.addAnimation(tr);
			anmationSet.setFillAfter(true);
			this.startAnimation(anmationSet);
			
		}else if(this.getState() == 1){
			/*
			 * 
			 * */
			float star_sc_num = 0.4f;
			float last_sc_num = 0.6f;
			float star_al_num = 0.4f;
			float last_al_num = 1f;
			int time= 200;
			if(!this.useable){
				time=time*3;
				star_sc_num = 1.2f;
				last_sc_num = 2.5f;
				star_al_num = 0.01f;
				last_al_num = 0.05f;
			}
			
			
			ScaleAnimation sc = new ScaleAnimation(star_sc_num,last_sc_num,star_sc_num,last_sc_num,
					Animation.RELATIVE_TO_SELF,0.5f,
					Animation.RELATIVE_TO_SELF,0.5f);
			sc.setDuration(time);
			anmationSet.addAnimation(sc);
			
			AlphaAnimation al = new AlphaAnimation(star_al_num, last_al_num);
			al.setDuration(time);
			anmationSet.addAnimation(al);
			
			TranslateAnimation tr = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF,this.m_pos_x/8,
					Animation.RELATIVE_TO_SELF,0f,
					Animation.RELATIVE_TO_SELF,this.m_pos_y/8,
					Animation.RELATIVE_TO_SELF,0f
					);
			tr.setDuration(time);
			anmationSet.addAnimation(tr);
			anmationSet.setFillAfter(true);
			this.startAnimation(anmationSet);
		}
		
		
		
		this.state = 2;
	}
	
	public void halfGone(){
		txt.setVisibility(View.INVISIBLE);
		img.setVisibility(View.INVISIBLE);
		AnimationSet anmationSet = new AnimationSet(true);
		if(this.getState() == 0){
			/*从无到直接显示
			 *
			 * */
			float star_sc_num = 0;
			float last_sc_num = 0.5f;
			float star_al_num = 0;
			float last_al_num = 0.4f;
			int time = 500;
			if(!this.useable){
				time=time*3;
				last_sc_num = 1.2f;
				last_al_num = 0.01f;
			}
			
			ScaleAnimation sc = new ScaleAnimation(star_sc_num,last_sc_num,star_sc_num,last_sc_num,
					Animation.RELATIVE_TO_SELF,0.5f,
					Animation.RELATIVE_TO_SELF,0.5f);
			sc.setDuration(time);
			anmationSet.addAnimation(sc);
			
			AlphaAnimation al = new AlphaAnimation(star_al_num, last_al_num);
			al.setDuration(time);
			anmationSet.addAnimation(al);
			
			TranslateAnimation tr = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF,this.m_pos_x,
					Animation.RELATIVE_TO_SELF,this.m_pos_x/8,
					Animation.RELATIVE_TO_SELF,this.m_pos_y,
					Animation.RELATIVE_TO_SELF,this.m_pos_y/8
					);
			tr.setDuration(time);
			anmationSet.addAnimation(tr);
			anmationSet.setFillAfter(true);
			this.startAnimation(anmationSet);
			
		}else if(this.getState() == 2){
			/*从全显示到半显示
			 * 
			 * */
			float star_sc_num = 1f;
			float last_sc_num = 0.5f;
			float star_al_num = 1f;
			float last_al_num = 0.4f;
			int time = 500;

			if(!this.useable){
				time=time*3;
				star_sc_num = 2.5f;
				last_sc_num = 1.2f;
				star_al_num = 0.05f;
				last_al_num = 0.01f;
			}
			
			
			ScaleAnimation sc = new ScaleAnimation(star_sc_num,last_sc_num,star_sc_num,last_sc_num,
					Animation.RELATIVE_TO_SELF,0.5f,
					Animation.RELATIVE_TO_SELF,0.5f);
			sc.setDuration(time);
			anmationSet.addAnimation(sc);
			
			AlphaAnimation al = new AlphaAnimation(star_al_num, last_al_num);
			al.setDuration(time);
			anmationSet.addAnimation(al);
			
			TranslateAnimation tr = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF,0f,
					Animation.RELATIVE_TO_SELF,this.m_pos_x/8,
					Animation.RELATIVE_TO_SELF,0f,
					Animation.RELATIVE_TO_SELF,this.m_pos_y/8
					);
			tr.setDuration(time);
			anmationSet.addAnimation(tr);
			anmationSet.setFillAfter(true);
			this.startAnimation(anmationSet);
			
		}
		
		
		this.state = 1;
		
	}
	
	public void gone(){
		txt.setVisibility(View.INVISIBLE);
		img.setVisibility(View.INVISIBLE);
		
		AnimationSet anmationSet = new AnimationSet(true);
		
		if(this.getState() == 1){
			/*
			 * 
			 * */
			float star_sc_num = 0.6f;
			float last_sc_num = 0f;
			float star_al_num = 0.4f;
			float last_al_num = 0f;
			int time = 800;
			if(!useable){
				time=time*3;
				star_sc_num = 1.2f; 
				star_al_num = 0.01f;
			}
			ScaleAnimation sc = new ScaleAnimation(star_sc_num,last_sc_num,star_sc_num,last_sc_num,
					Animation.RELATIVE_TO_SELF,0.5f,
					Animation.RELATIVE_TO_SELF,0.5f);
			sc.setDuration(time);
			anmationSet.addAnimation(sc);
			
			AlphaAnimation al = new AlphaAnimation(star_al_num, last_al_num);
			al.setDuration(time);
			anmationSet.addAnimation(al);
			
			TranslateAnimation tr = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF,0f,
					Animation.RELATIVE_TO_SELF,this.m_pos_x/8,
					Animation.RELATIVE_TO_SELF,0f,
					Animation.RELATIVE_TO_SELF,this.m_pos_y/8
					);
			tr.setDuration(time);
			anmationSet.addAnimation(tr);
			anmationSet.setFillAfter(true);
			this.startAnimation(anmationSet);
		}else if(this.getState() == 2){
			/*
			 *动作流：
			 * 透明度变低、变小。移动到边缘
			 *  
			 * */
			float star_sc_num = 1f;
			float last_sc_num = 0f;
			float star_al_num = 1f;
			float last_al_num = 0f;
			int time = 800;
			
			if(!useable){
				time=time*3;
				star_sc_num = 2.5f; 
				star_al_num = 0.05f;
			}
			ScaleAnimation sc = new ScaleAnimation(star_sc_num,last_sc_num,star_sc_num,last_sc_num,
					Animation.RELATIVE_TO_SELF,0.5f,
					Animation.RELATIVE_TO_SELF,0.5f);
			sc.setDuration(time);
			anmationSet.addAnimation(sc);
			
			AlphaAnimation al = new AlphaAnimation(star_al_num, last_al_num);
			al.setDuration(time);
			anmationSet.addAnimation(al);
			
			TranslateAnimation tr = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF,0f,
					Animation.RELATIVE_TO_SELF,this.m_pos_x,
					Animation.RELATIVE_TO_SELF,0f,
					Animation.RELATIVE_TO_SELF,this.m_pos_y
					);
			tr.setDuration(time);
			anmationSet.addAnimation(tr);
			anmationSet.setFillAfter(true);
			this.startAnimation(anmationSet);
		}
		this.setState(0);
		
	}
	
	public void rotateArr(int r){
		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation ro1 = new RotateAnimation(this.rotation, r*5/4,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		ro1.setDuration(500);
		RotateAnimation ro2 = new RotateAnimation(0, -r/4,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		ro2.setDuration(70);
		ro2.setStartOffset(300);
		animationSet.addAnimation(ro1);
		animationSet.addAnimation(ro2);
		
		ScaleAnimation sc = new ScaleAnimation(this.scale,this.scale,this.scale,this.scale,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		sc.setDuration(0);
		animationSet.addAnimation(sc);
		
//		ScaleAnimation sc1 = new ScaleAnimation(0.8f,this.scale,0.8f,this.scale,
//				Animation.RELATIVE_TO_SELF,0.5f,
//				Animation.RELATIVE_TO_SELF,0.5f);
//		sc1.setDuration(80);
//		sc1.setStartOffset(300);
//		animationSet.addAnimation(sc1);
		
	
		
		animationSet.setFillAfter(true);
		this.startAnimation(animationSet);
		this.rotation = r;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int onClick(){
		
		if(this.useable && this.getState() == 2){
			//显示时候的点按事件
			Log.i("" +"============", "U CLICK X ="+(this.m_pos_x+5)+" | Y = "+(this.m_pos_y+5));
		}
		
		return 0;
	}
	
}
