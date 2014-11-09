package com.ztbeacon.client.activity.navi;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ztbeacon.client.R;
import com.ztbeacon.client.adapter.GView;
import com.ztbeacon.client.entity.UserInfo;

public class NaviActivity extends Activity {

	private final float MAX_ROATE_DEGREE = 1.0f;// 最多旋转一周，即360°
	private SensorManager mSensorManager;// 传感器管理对象
	private Sensor mOrientationSensor;// 传感器对象
	private float mDirection;// 当前浮点方向
	private float mTargetDirection;// 目标浮点方向
	private AccelerateInterpolator mInterpolator;// 动画从开始到结束，变化率是一个加速的过程,就是一个动画速率
	protected final Handler mHandler = new Handler();
	Handler doShow = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 2) {
			} else if (msg.what == 3) {
				showDistance.setText(UserInfo.distance);
			} else if (msg.what == 4) {
			} else if (msg.what == 5) {
				// debug
			}
			return false;
		}
	});
	private boolean mStopDrawing;// 是否停止指南针旋转的标志位
	private TextView showDistance;
	protected Runnable mCompassViewUpdater = new Runnable() {
		@Override
		public void run() {
			if (!mStopDrawing) {
				if (mDirection != mTargetDirection) {
					// calculate the short routine
					float to = mTargetDirection;
					if (to - mDirection > 180) {
						to -= 360;
					} else if (to - mDirection < -180) {
						to += 360;
					}

					// limit the max speed to MAX_ROTATE_DEGREE
					float distance = to - mDirection;
					if (Math.abs(distance) > MAX_ROATE_DEGREE) {
						distance = distance > 0 ? MAX_ROATE_DEGREE
								: (-1.0f * MAX_ROATE_DEGREE);
					}
					// need to slow down if the distance is short
					mDirection = normalizeDegree(mDirection
							+ ((to - mDirection) * mInterpolator
									.getInterpolation(Math.abs(distance) > MAX_ROATE_DEGREE ? 0.4f
											: 0.3f)));// 用了一个加速动画去旋转图片，很细致
					// mPointer.updateDirection(mDirection);//更新指南针旋转
					arr.rotateArr((int) mDirection - 180 + UserInfo.angle);// 閺冨娴�//指向北
																			// 需要改成指向目标
					showDistance.setText(UserInfo.distance);

				}
				mHandler.postDelayed(mCompassViewUpdater, 20);// 20毫秒后重新执行自己，比定时器好
			}
		}
	};

	private float normalizeDegree(float degree) {
		return (degree + 720) % 360;
	}

	private ImageView radar;
	private ArrayList<GView> pushnodes;
	private int positions[][] = { { 1, 1 }, { 1, 3 }, { 1, 5 }, { 1, 7 },
			{ 1, 9 }, { 3, 1 }, { 3, 3 }, { 3, 7 }, { 3, 9 }, { 5, 1 },
			{ 5, 9 }, { 7, 1 }, { 7, 3 }, { 7, 7 }, { 7, 9 }, { 9, 1 },
			{ 9, 3 }, { 9, 5 }, { 9, 7 }, { 9, 9 }, };
	private ArrayList<GView> unuseable_pushnodes;
	private int unuseable_positions[][] = { { 1, 2 }, { 2, 8 }, { 4, 1 },
			{ 6, 9 }, { 8, 2 }, { 9, 8 } };
	private int cenDis = 48;
	private int winHei = 854;
	private int winWid = 480;

	Handler handler = new Handler();
	Runnable runable = null;

	public static NaviActivity _this;

	public GView arr = null;
	public GView end_p = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navi);
		// 取得bundle对象
		Navigation.startNavi();
		Bundle bundle = this.getIntent().getExtras();
		String word = bundle.getString("word");
		TextView textView = (TextView) findViewById(R.id.gua_endname);
		textView.setText("目的地:\n" + word);
		showDistance = (TextView) findViewById(R.id.gua_compass_dis);
		_this = this;
		this.radar = (ImageView) findViewById(R.id.gua_radar);

		this.getScaleSeed();
		this.initGView();

		this.runable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				_this.randomPushNode();
				// _this.randomArr();
				_this.handler.postDelayed(runable, 1500);
			}
		};

		initResources();// 初始化view
		initServices();// 初始化传感器和位置服务
	}

	private void initResources() {
		mDirection = 0.0f;// 初始化起始方向
		mTargetDirection = 0.0f;// 初始化目标方向
		mInterpolator = new AccelerateInterpolator();// 实例化加速动画对象
		mStopDrawing = true;
	}

	private void initServices() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mOrientationSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	}

	// //////////////////////////////////数据更新////////////////////////////////////////////////////

	public boolean initGView() {
		this.arr = (GView) findViewById(R.id.gua_dic_point);
		Log.d("==========", (arr == null) + "");

		arr.bgimg.setImageResource(R.drawable.navi_deepred_circle);
		arr.bgimg.setVisibility(View.INVISIBLE);
		arr.arrarr.setImageResource(R.drawable.navi_deepred_arr);
		arr.arrarr.setVisibility(View.VISIBLE);
		arr.scaleTo(1.5f, 0);

		this.pushnodes = new ArrayList<GView>();

		int ids[] = { R.id.p0, R.id.p1, R.id.p2, R.id.p3, R.id.p4, R.id.p5,
				R.id.p6, R.id.p7, R.id.p8, R.id.p9, R.id.p10, R.id.p11,
				R.id.p12, R.id.p13, R.id.p14, R.id.p15, R.id.p16, R.id.p17,
				R.id.p18, R.id.p19, };

		for (int i = 0; i < this.positions.length; i++) {
			GView gv = (GView) findViewById(ids[i]);
			gv.arrarr.setVisibility(View.GONE);
			gv.scaleTo(0.6f, 0);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.leftMargin = this.cenDis * (this.positions[i][0] - 1)
					- (this.cenDis - 48)
					+ new Random().nextInt(this.cenDis / 2) - this.cenDis / 4;
			lp.topMargin = this.cenDis * (this.positions[i][1] - 1)
					+ (winHei / 2 - 48 * 5)
					+ new Random().nextInt(this.cenDis / 2) - this.cenDis / 4;
			gv.setM_Pos(this.positions[i][0], this.positions[i][1]);
			gv.setLayoutParams(lp);
			int ran = new Random().nextInt(5);
			switch (ran) {
			case 0:
				gv.gone();
				break;
			case 1:
				gv.halfGone();
				break;
			case 2:
				gv.showUp();
				break;
			case 3:
				gv.gone();
				break;
			case 4:
				gv.halfGone();
				break;
			}
			this.pushnodes.add(gv);
		}
		this.initUnuseableGview();
		return true;
	}

	public boolean initUnuseableGview() {
		unuseable_pushnodes = new ArrayList<GView>();

		int ids[] = { R.id.una_p0, R.id.una_p1, R.id.una_p2, R.id.una_p3,
				R.id.una_p4, R.id.una_p5, R.id.una_p6

		};

		for (int i = 0; i < unuseable_positions.length; i++) {
			GView gv = (GView) findViewById(ids[i]);
			gv.useable = false;

			gv.arrarr.setVisibility(View.GONE);
			gv.setVisibility(View.VISIBLE);
			gv.img.setVisibility(View.GONE);
			gv.txt.setVisibility(View.GONE);

			// Log.e("----------------", "unuseable view init id:"+i);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.leftMargin = this.cenDis * (this.unuseable_positions[i][0] - 1)
					- (this.cenDis - 48)
					+ new Random().nextInt(this.cenDis / 2) - this.cenDis / 4;
			lp.topMargin = this.cenDis * (this.unuseable_positions[i][1] - 1)
					+ (winHei / 2 - 48 * 5)
					+ new Random().nextInt(this.cenDis / 2) - this.cenDis / 4;
			gv.setM_Pos(this.unuseable_positions[i][0],
					this.unuseable_positions[i][1]);
			gv.setLayoutParams(lp);

			this.unuseable_pushnodes.add(gv);

			gv.gone();
		}

		return true;
	}

	// public boolean updateDestination(String name,int size,int des,int ro,int
	// imgid){
	// /*更改目的地名字
	// * 更改距离
	// * 更改方向
	// * 更改图片
	// * */
	//
	//
	// return true;
	// }
	//
	// public boolean updatePushNode(ArrayList<PushNodeData> pnds){
	// /*获取临近推送点的列表
	// * 扫描次级临近推送表的列表，如果有推送列表里面有相同节点，变为全显示
	// * 列表中不存在的原来的节点 变成次级节点
	// * */
	//
	//
	//
	// return true;
	// }
	//
	// public boolean updateSecondPushNode(ArrayList<PushNodeData> pnds){
	// /*获取次级节点列表
	// * 去除多余的次级节点
	// * */
	// return true;
	// }

	// ///////////////////////////////////屏幕适配方案/////////////////////////////////////////////////////////
	float scale_seed_x = 1;
	float scale_seed_y = 1;

	public void getScaleSeed() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int widthPixels = metrics.widthPixels;
		int heightPixels = metrics.heightPixels;
		this.winHei = metrics.heightPixels;
		this.winWid = metrics.widthPixels;
		// 480 854
		Log.e("XXXXXXXXXXXXXXXXXXXXXXXX", widthPixels + "");
		Log.e("YYYYYYYYYYYYYYYYYYYYYYYY", heightPixels + "");

		if (winHei > winWid) {
			this.cenDis = (winWid - 40) / 10;
		} else {
			this.cenDis = (winWid - 40) / 10;
		}

		this.resetViewSize();
	}

	public void resetViewSize() {

	}

	public void onClick(View view) {
		GView gv = (GView) view;
		gv.onClick();
		// gv.gone();
	}

	public void onClickView(View view) {
		Log.d("=============", "U CILCK " + view.getId());

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		if (mOrientationSensor != null) {
			mSensorManager.registerListener(mOrientationSensorEventListener,
					mOrientationSensor, SensorManager.SENSOR_DELAY_GAME);
		} else {
			Toast.makeText(this, "没有方向传感器", Toast.LENGTH_SHORT).show();
		}
		mStopDrawing = false;
		mHandler.postDelayed(mCompassViewUpdater, 20);// 20毫秒执行一次更新指南针图片旋转
		super.onResume();
	}

	@Override
	protected void onPause() {// 在暂停的生命周期里注销传感器服务和位置更新服务
		super.onPause();
		mStopDrawing = true;
		if (mOrientationSensor != null) {
			mSensorManager.unregisterListener(mOrientationSensorEventListener);
		}
	}// 方向传感器变化监听

	private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			float direction = event.values[0] * -1.0f;
			mTargetDirection = normalizeDegree(direction);// 赋值给全局变量，让指南针旋转
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	// ///////////////////////////////////////////////menu/////////////////////////////////////////////////////////
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// TODO Auto-generated method stub
		menu.add(Menu.NONE, 1, 1, "随机变幻");
		menu.add(Menu.NONE, 2, 2, "箭头乱转");
		menu.add(Menu.NONE, 3, 3, "旋转雷达");
		menu.add(Menu.NONE, 4, 4, "持续变幻");
		menu.add(Menu.NONE, 5, 5, "停止持续变化");
		menu.add(Menu.NONE, 6, 6, "停止雷达");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			this.randomPushNode();

		} else if (item.getItemId() == 2) {
			arr.rotateArr(new Random().nextInt(360));// 绠ご

		} else if (item.getItemId() == 3) {
			this.runRadar();
		} else if (item.getItemId() == 4) {
			this.randomPushNode();
			handler.postDelayed(runable, 1500);
			this.runRadar();

		} else if (item.getItemId() == 5) {
			handler.removeCallbacks(runable);
			handler.removeCallbacks(runable);

		} else if (item.getItemId() == 6) {
			this.stopRadar();
		}

		return true;
	}

	// //////////////////////////////////////////////
	public void randomPushNode() {
		for (int i = 0; i < this.positions.length; i++) {
			GView gv = this.pushnodes.get(i);
			int ran = new Random().nextInt(5);
			switch (ran) {
			case 0:
				gv.gone();
				break;
			case 1:
				gv.halfGone();
				break;
			case 2:
				gv.showUp();
				break;
			case 3:
				gv.gone();
				break;
			case 4:
				gv.halfGone();
				break;
			}
		}
	}

	public void randomArr() {
		arr.rotateArr(new Random().nextInt(360));
	}

	public void runRadar() {
		this.radar.setVisibility(View.VISIBLE);
		AnimationSet set = new AnimationSet(true);
		RotateAnimation ro = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ro.setDuration(4500);
		ro.setRepeatMode(Animation.RESTART);
		ro.setRepeatCount(-1);
		ro.setInterpolator(new LinearInterpolator());
		set.addAnimation(ro);

		this.radar.startAnimation(set);

	}

	public void stopRadar() {
		this.radar.setVisibility(View.INVISIBLE);
		this.radar.clearAnimation();
	}

	// ///////////////////////////////////////////////////////
	public void runBGView() {
		for (int i = 0; i < this.unuseable_pushnodes.size(); i++) {
			GView gv = this.unuseable_pushnodes.get(i);
			int ran = new Random().nextInt(5);
			switch (ran) {
			case 0:
				gv.gone();
				break;
			case 1:
				gv.halfGone();
				break;
			case 2:
				gv.showUp();
				break;
			case 3:
				gv.gone();
				break;
			case 4:
				gv.halfGone();
				break;
			}
		}
	}
}
