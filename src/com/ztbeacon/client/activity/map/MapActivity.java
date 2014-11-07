package com.ztbeacon.client.activity.map;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.TextView;

import com.ztbeacon.client.R;
import com.ztbeacon.client.adapter.DragImageView;
import com.ztbeacon.client.adapter.SpinnerButton;
import com.ztbeacon.client.utils.BitmapUtil;

//地图类
public class MapActivity extends Activity {
	private int window_width, window_height;// 控件宽度
	private DragImageView dragImageView;// 自定义控件
	private int state_height;// 状态栏的高度

	private ViewTreeObserver viewTreeObserver;

	private Spinner spinner;
	private Bitmap bmp1;
	private Bitmap bmp2;
	private Bitmap bmp3;
	private Bitmap bmp4;
	private SpinnerButton mSpinnerBtn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.activity_map);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.title_bar);
		/** 获取可見区域高度 **/
		WindowManager manager = getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();
		this.mSpinnerBtn = (SpinnerButton) this
				.findViewById(R.id.floor_spinner_btn);
		// 设置下拉布局资源文件,布局创建监听器，以便实例化控件对象
		mSpinnerBtn.setResIdAndViewCreatedListener(R.layout.spinner_floor_items,
				new SpinnerButton.ViewCreatedListener(){
					public void onViewCreated(View v) {
				
						// TODO Auto-generated method stub
						v.findViewById(R.id.spinner_2f).setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								// TODO Auto-generated method stub
								handleClick(((TextView)v).getText().toString());
							}
						});
						v.findViewById(R.id.spinner_3f).setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								// TODO Auto-generated method stub
								handleClick(((TextView)v).getText().toString());
							}
						});
					}
		},0,"");
		dragImageView = (DragImageView) findViewById(R.id.map);
		bmp1 = BitmapUtil.ReadBitmapById(this, R.drawable.map1, window_width,
				window_height);/*
								 * bmp2 = BitmapUtil.ReadBitmapById(this,
								 * R.drawable.ic_launcher, window_width,
								 * window_height); bmp3 =
								 * BitmapUtil.ReadBitmapById(this,
								 * R.drawable.ic_launcher, window_width,
								 * window_height); bmp4 =
								 * BitmapUtil.ReadBitmapById(this,
								 * R.drawable.ic_launcher, window_width,
								 * window_height);
								 */
		// 设置图片
		dragImageView.setImageBitmap(bmp1);
		dragImageView.setmActivity(this);// 注入Activity.
		/** 测量状态栏高度 **/
		viewTreeObserver = dragImageView.getViewTreeObserver();
		viewTreeObserver
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (state_height == 0) {
							// 获取状况栏高度
							Rect frame = new Rect();
							getWindow().getDecorView()
									.getWindowVisibleDisplayFrame(frame);
							state_height = frame.top;
							dragImageView.setScreen_H(window_height
									- state_height);
							dragImageView.setScreen_W(window_width);
						}

					}
				});
		// 设置下拉菜单代码
		/*
		 * spinner = (Spinner) findViewById(R.id.spinner1);
		 * 
		 * spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		 * 
		 * @Override public void onItemSelected(AdapterView<?> parent, View
		 * view, int position, long id) { // TODO Auto-generated method stub
		 * 
		 * if (position == 2) dragImageView.setImageBitmap(bmp2);
		 * 
		 * }
		 * 
		 * @Override public void onNothingSelected(AdapterView<?> parent) {
		 * //设置下拉菜单代码 TODO Auto-generated method stub
		 * 
		 * } });
		 */

	}

	/**
	 * 读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
    private void handleClick(String text){
		mSpinnerBtn.dismiss();
		mSpinnerBtn.setText(text);
	}
}
