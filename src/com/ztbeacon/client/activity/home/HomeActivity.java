package com.ztbeacon.client.activity.home;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ztbeacon.client.R;
import com.ztbeacon.client.activity.store.StoreInfoActivity;

/**
 * 仿优酷Android客户端图片左右滑动
 * 
 */
public class HomeActivity extends Activity {
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合
	private int[] imageResId; // 图片ID
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem = 0; // 当前图片的索引号
	int screenWidth ,screenHeight;
	ImageButton food,happy,shop,more;
	ImageView markt_img;
	// An ExecutorService that can schedule commands to run after a given delay,
	// or to execute periodically.
	private ScheduledExecutorService scheduledExecutorService;

	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
        //Toast.makeText(HomeActivity.this, screenWidth+"进入下一页"+screenHeight, Toast.LENGTH_SHORT).show();
        
        food=(ImageButton) findViewById(R.id.food);
        happy=(ImageButton) findViewById(R.id.happy);
        shop=(ImageButton) findViewById(R.id.shop);
        more=(ImageButton) findViewById(R.id.more);
        markt_img=(ImageView) findViewById(R.id.markt_img);
        LinearLayout.LayoutParams imagebtn_params = new LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        	imagebtn_params.width = screenWidth / 4;
            imagebtn_params.height = imagebtn_params.width;
            
           food.setLayoutParams(imagebtn_params);
           happy.setLayoutParams(imagebtn_params);
           shop.setLayoutParams(imagebtn_params);
           more.setLayoutParams(imagebtn_params);
           
          // Toast.makeText(HomeActivity.this,imagebtn_params.width+"进入下一页"+imagebtn_params.height, Toast.LENGTH_SHORT).show();
           
           RelativeLayout.LayoutParams imagevie_params = new RelativeLayout.LayoutParams(
                   LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
           		imagevie_params.width = (screenWidth / 3)-20;
           		imagevie_params.height = imagevie_params.width-35;
           		markt_img.setLayoutParams(imagevie_params);
           //Toast.makeText(HomeActivity.this,imagevie_params.width+"进入下一页"+imagevie_params.height, Toast.LENGTH_SHORT).show();
		imageResId = new int[100];
		imageResId[0]=R.drawable.home_a;
		imageResId[1]=R.drawable.home_b;
		imageResId[2]=R.drawable.home_d;
		imageViews = new ArrayList<ImageView>();
		
		// 初始化图片资源
		for (int i = 0; i < imageResId.length; i++) {
			if(i>=3){imageResId[i]=imageResId[i-3];}
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageViews.add(imageView);
		}

		
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));
		dots.add(findViewById(R.id.v_dot2));


		viewPager = (ViewPager) findViewById(R.id.vp);
		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());

	}

	
	@Override
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3, TimeUnit.SECONDS);
		super.onStart();
	}

	@Override
	protected void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				//System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * 
	 */
	 class MyPageChangeListener implements OnPageChangeListener {
		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			if(currentItem%3==0){
				dots.get(1).setBackgroundResource(R.drawable.home_dot_normal);
				dots.get(2).setBackgroundResource(R.drawable.home_dot_normal);
				dots.get(0).setBackgroundResource(R.drawable.home_dot_focused);}
			else if(currentItem%3==1){
				dots.get(0).setBackgroundResource(R.drawable.home_dot_normal);
				dots.get(1).setBackgroundResource(R.drawable.home_dot_focused);
				dots.get(2).setBackgroundResource(R.drawable.home_dot_normal);}
			else if(currentItem%3==2){
				dots.get(0).setBackgroundResource(R.drawable.home_dot_normal);
				dots.get(1).setBackgroundResource(R.drawable.home_dot_normal);
				dots.get(2).setBackgroundResource(R.drawable.home_dot_focused);}
			/*Toast.makeText(MyViewPagerActivity.this, currentItem+"你不好"+position, Toast.LENGTH_SHORT).show();
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;*/
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageResId.length;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			//Toast.makeText(MyViewPagerActivity.this, currentItem+"点击了注册"+arg1, Toast.LENGTH_SHORT).show();
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}
	public void homeClick(View view){
		Intent intent = new Intent();
		intent.setClass(this, HomeResultActivity.class);
		Bundle bundle = new Bundle();
		if(view.getId() == R.id.shop){
			bundle.putString("type", "购物");
		}else if(view.getId() == R.id.happy){
			bundle.putString("type", "娱乐");
		}else if(view.getId() == R.id.food){
			bundle.putString("type", "美食");
		}else{
			bundle.putString("type", "更多");
		}
		intent.putExtras(bundle);
		startActivity(intent);
	}
	public void searchBt(View v){
		Intent intent = new Intent();
		intent.setClass(this, HomeResultActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("type", "搜索");
		intent.putExtras(bundle);
		startActivity(intent);
	}
	public void turnStore(View v){
		startActivity(new Intent(this, StoreInfoActivity.class));
	}
}