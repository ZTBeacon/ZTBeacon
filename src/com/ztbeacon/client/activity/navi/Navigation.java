package com.ztbeacon.client.activity.navi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

import com.ztbeacon.client.entity.Router;
import com.ztbeacon.client.entity.UserInfo;

/**
 * @author 王小红 E-mail:493026465@qq.com
 * @version 创建时间：2014年11月6日 上午9:38:19 类说明导航的方法
 */
public class Navigation {
	public static List<Router> getRouters() throws JSONException,
			ClientProtocolException, IOException {
		List<Router> fresult = new ArrayList<Router>();
		JSONObject root = new JSONObject();
		root.put("token", UserInfo.token);
		root.put("marketid", UserInfo.srcInfo.marketId);
		JSONObject src = new JSONObject();
		src.put("pos_x", UserInfo.srcInfo.pos_x);
		src.put("pos_y", UserInfo.srcInfo.pos_y);
		src.put("pos_z", UserInfo.srcInfo.pos_z);
		root.put("src", src);
		JSONObject dst = new JSONObject();
		dst.put("pos_x", UserInfo.dstInfo.pos_x);
		dst.put("pos_y", UserInfo.dstInfo.pos_y);
		dst.put("pos_z", UserInfo.dstInfo.pos_z);
		root.put("dst", dst);

		HttpPost request = new HttpPost(
				"http://112.11.119.154//ServiceAPI/router/");
		// 先封装一个 JSON 对象
		// 绑定到请求 Entry
		StringEntity se = new StringEntity(root.toString());
		request.setEntity(se);
		// 发送请求
		HttpResponse httpResponse = new DefaultHttpClient().execute(request);
		// 得到应答的字符串，这也是一个 JSON 格式保存的数据
		String retSrc = EntityUtils.toString(httpResponse.getEntity());
		// // 生成 JSON 对象
		Log.e("Error", retSrc);
		JSONObject result = new JSONObject(retSrc);

		JSONArray s = result.getJSONObject("route").getJSONArray("router");
		for (int i = 0; i < s.length(); i++) {
			Router router = new Router();
			router.setIbeaconid(s.getJSONObject(i).getString("ibeaconid"));
			router.setPos_x(s.getJSONObject(i).getDouble("pos_x"));
			router.setPos_y(s.getJSONObject(i).getDouble("pos_y"));
			router.setPos_z(s.getJSONObject(i).getDouble("pos_z"));

			fresult.add(router);
		}

		for (int i = 0; i < fresult.size(); i++) {
			Log.e("Error", i + "beacon" + fresult.get(i).getPos_y());
		}

		return fresult;

	}

	public static void startNavi() {
		UserInfo.dstInfo.pos_x = 48.9;
		UserInfo.dstInfo.pos_y = 19.2;
		UserInfo.dstInfo.pos_z = 4;
		Thread navThread = new Thread(startNav);
		navThread.start();
	}

	static Runnable startNav = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {
				UserInfo.routers = Navigation.getRouters();
				while (true) {
					if (UserInfo.routers.size() == 0) {
						Message msg = new Message();
						msg.what = 2;
						msg.obj = "到达目的地";
						UserInfo.distance = "到达目的地";
						break;//
					}
					Navigation.doNav(UserInfo.routers);
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	public static final Message doNav(List<Router> routers)
			throws ClientProtocolException, JSONException, IOException {
		Message message = new Message();
		if (UserInfo.srcInfo.name == null) {
			// 还未定位得到用户位置
			message.obj = "还未定位到您的所在位置，请稍等";
			while (true) {// 卡住 不断去等
				if (UserInfo.srcInfo.name != null
						&& UserInfo.srcInfo.name.charAt(0) == '0') {
					routers = getRouters();
					break;
				}
			}
		}
		// 走错路啦！！！
		// 得到了路径列表0 - finally 为起点到终点 正常导航
		else if (routers.get(0).getIbeaconid().equals(UserInfo.srcInfo.name)) {
			if (routers.size() == 1) {
				routers.remove(0);
				return message;
			}
			message.what = 4;// 更新导航信息
			// 到达了。发送方向
			double dis = Math.sqrt(Math.pow(routers.get(1).getPos_x()
					- UserInfo.srcInfo.pos_x, 2)
					+ Math.pow(UserInfo.srcInfo.pos_y
							- routers.get(1).getPos_y(), 2));
			double angle = 0;
			if (UserInfo.srcInfo.pos_x == routers.get(1).getPos_x()) {
				if (UserInfo.srcInfo.pos_y < routers.get(1).getPos_y())// 往北走
				{
					angle = 0;
				} else if (UserInfo.srcInfo.pos_y > routers.get(1).getPos_y()) {
					angle = 180;
				}
			} else if (UserInfo.srcInfo.pos_x < routers.get(1).getPos_x())// 在往东边走
			{
				angle = 90.0 - (Math.atan((UserInfo.srcInfo.pos_y - routers
						.get(1).getPos_y())
						/ (UserInfo.srcInfo.pos_x - routers.get(1).getPos_x())) * 180 / Math.PI);
			} else if (UserInfo.srcInfo.pos_x > routers.get(1).getPos_x())// 往西边走
			{
				angle = 270.0 - ((UserInfo.srcInfo.pos_y - routers.get(1)
						.getPos_y())
						/ Math.atan((UserInfo.srcInfo.pos_x - routers.get(1)
								.getPos_x())) * 180 / Math.PI);
			}
			// double[] result = { dis, angle };
			UserInfo.angle = (int) angle;
			UserInfo.distance = String.valueOf((int) dis) + "M";
			// message.obj = result;
			routers.remove(0);
		} else {
			// 可能走错路了 1.发现所走的路不再列表中 2.跳过节点
			for (int i = 1; i < routers.size(); i++) {
				if (routers.get(i).getIbeaconid().equals(UserInfo.srcInfo.name)) {
					// 第2情况 改变路径;
					for (int j = 0; j < i; j++) {
						routers.remove(0);
					}
					break;
				}
				// 不再路劲列表中 处理 重新定位
				try {
					UserInfo.routers = Navigation.getRouters();
				} catch (Exception e) {
					// 列表获取失败
					message.what = 1;
					message.obj = "出错了,获取猎豹失败-----Message:" + e.toString();
				}
			}
		}
		return message;

	}
}
