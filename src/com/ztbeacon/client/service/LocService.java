package com.ztbeacon.client.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.ztbeacon.client.activity.navi.GetLoction;
import com.ztbeacon.client.entity.iBeaconClass;
import com.ztbeacon.client.entity.iBeaconClass.iBeacon;

/**
 * @author 王小红 E-mail:493026465@qq.com
 * @version 创建时间：2014年11月8日 下午4:34:07 类说明
 */
public class LocService extends Service {

	private BluetoothAdapter mBluetoothAdapter;
	private boolean flag = true;
	private HashMap<String, ArrayList<iBeacon>> beacons = new HashMap<String, ArrayList<iBeacon>>();
	long time1;
	long time2;
	List<ArrayList<iBeacon>> afb = new ArrayList<ArrayList<iBeacon>>();
	List<String> afs = new ArrayList<String>();

	Thread mainThread;

	private IBinder myBinder = new Binder() {

		@Override
		public String getInterfaceDescriptor() {
			return "LocService Class ..";
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("*** Service onBind()");
		return this.myBinder; // 姝ゅ鏆傛椂涓嶅仛浠讳綍鐨勫鐞�
	}

	@Override
	public void onRebind(Intent intent) {
		System.out.println("*** Service onRebind()");
		super.onRebind(intent);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		System.out.println("*** Service onUnbind()");
		return super.onUnbind(intent);
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();



		startServer();

		System.out.println("*** Service onCreate()");
	}

	void startServer() {
		mainThread = new Thread(thread);
		mainThread.start();
		new Thread(killouttime).start();
	}

	Runnable killouttime = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				// do kill
				try {
					Thread.sleep(30 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					mainThread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					List<ArrayList<iBeacon>> afbs = new ArrayList<ArrayList<iBeacon>>(
							beacons.values());
					for (int i = 0; i < afb.size(); i++) {
						if (afb.get(i).size() == (afbs.get(i).size())) {
							// 说明没更新
							beacons.remove(afs.get(i));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				afb = new ArrayList<ArrayList<iBeacon>>(beacons.values());
				afs = new ArrayList<String>(beacons.keySet());
			}
		}
	};
	Runnable thread = new Runnable() {

		@SuppressLint("NewApi")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			flag = true;
			while (true) {
				mBluetoothAdapter.startLeScan(mLeScanCallback);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				mBluetoothAdapter.stopLeScan(mLeScanCallback);
			}

		}
	};

	@Override
	public void onDestroy() {
		System.out.println("*** Service onDestroy()");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("*** Service onStartCommand");
		return Service.START_CONTINUATION_MASK; // 缁х画鎵ц
	}

	@SuppressLint("NewApi")
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			// TODO Auto-generated method stub
			final iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi,
					scanRecord);
			Thread con = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (beacons.size() == 0) {
						// init
						ArrayList<iBeacon> rssis = new ArrayList<iBeacon>();
						rssis.add(ibeacon);
						beacons.put(Integer.toHexString(ibeacon.major)
								+ Integer.toHexString(ibeacon.minor), rssis);

					} else if (beacons.size() > 0) {
						flag = true;
						List<String> s = new ArrayList<String>(beacons.keySet());

						try {
							for (int i = 0; i < s.size(); i++) {
								if (s.get(i)
										.equals((Integer
												.toHexString(ibeacon.major) + Integer
												.toHexString(ibeacon.minor)))) {
									// 说明已经发现这个beacon。存入即可
									ArrayList<iBeacon> in = beacons.get(s
											.get(i));
									in.add(ibeacon);
									beacons.put(
											(Integer.toHexString(ibeacon.major) + Integer
													.toHexString(ibeacon.minor)),
											in);
									flag = false;
									break;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (flag)// 说明还未发现过这个beacon
						{
							ArrayList<iBeacon> rssis = new ArrayList<iBeacon>();
							rssis.add(ibeacon);
							beacons.put(Integer.toHexString(ibeacon.major)
									+ Integer.toHexString(ibeacon.minor), rssis);
						}
					}

					List<ArrayList<iBeacon>> lists = new ArrayList<ArrayList<iBeacon>>(
							beacons.values());
					try {
						for (int i = 0; i < lists.size(); i++) {
							if (lists.get(i).size() == 10) {
								// // 拿去算

								GetLoction.getBeacon(lists.get(i));

								// 清空
								lists.get(i).clear();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			con.start();

			// 思路:发现一个不同的Beacon就以major+minor为KEY存入他的rssi，形成arrylist

		}
	};

}
