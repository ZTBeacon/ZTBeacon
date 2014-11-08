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
 * @author ��С�� E-mail:493026465@qq.com
 * @version ����ʱ�䣺2014��11��6�� ����9:38:19 ��˵�������ķ���
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
		// �ȷ�װһ�� JSON ����
		// �󶨵����� Entry
		StringEntity se = new StringEntity(root.toString());
		request.setEntity(se);
		// ��������
		HttpResponse httpResponse = new DefaultHttpClient().execute(request);
		// �õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ���������
		String retSrc = EntityUtils.toString(httpResponse.getEntity());
		// // ���� JSON ����
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

	public static final Message doNav(List<Router> routers)
			throws ClientProtocolException, JSONException, IOException {
		Message message = new Message();
		if (UserInfo.srcInfo.name == null) {
			// ��δ��λ�õ��û�λ��
			message.obj = "��δ��λ����������λ�ã����Ե�";
		}
		// �ߴ�·��������
		// �õ���·���б�0 - finally Ϊ��㵽�յ� ��������
		else if (routers.get(0).getIbeaconid().equals(UserInfo.srcInfo.name)) {
			if (routers.size() == 1) {
				routers.remove(0);
				return message;
			}
			message.what = 4;// ���µ�����Ϣ
			// �����ˡ����ͷ���
			double dis = Math.sqrt(Math.pow(routers.get(1).getPos_x()
					- UserInfo.srcInfo.pos_x, 2)
					+ Math.pow(UserInfo.srcInfo.pos_y
							- routers.get(1).getPos_y(), 2));
			double angle = 0;
			if (UserInfo.srcInfo.pos_x == routers.get(1).getPos_x()) {
				if (UserInfo.srcInfo.pos_y < routers.get(1).getPos_y())// ������
				{
					angle = 0;
				} else if (UserInfo.srcInfo.pos_y > routers.get(1).getPos_y()) {
					angle = 180;
				}
			} else if (UserInfo.srcInfo.pos_x < routers.get(1).getPos_x())// ����������
			{
				angle = 90.0 - (Math.atan((UserInfo.srcInfo.pos_y - routers
						.get(1).getPos_y())
						/ (UserInfo.srcInfo.pos_x - routers.get(1).getPos_x())) * 180 / Math.PI);
			} else if (UserInfo.srcInfo.pos_x > routers.get(1).getPos_x())// ��������
			{
				angle = 270.0 - ((UserInfo.srcInfo.pos_y - routers.get(1)
						.getPos_y())
						/ Math.atan((UserInfo.srcInfo.pos_x - routers.get(1)
								.getPos_x())) * 180 / Math.PI);
			}
			double[] result = { dis, angle };

			message.obj = result;
			routers.remove(0);
		} else {
			// �����ߴ�·�� 1.�������ߵ�·�����б��� 2.�����ڵ�
			for (int i = 1; i < routers.size(); i++) {
				if (routers.get(i).getIbeaconid().equals(UserInfo.srcInfo.name)) {
					// ��2��� �ı�·��;
					for (int j = 0; j < i; j++) {
						routers.remove(0);
					}
					break;
				}
				// ����·���б��� ���� ���¶�λ
				try {
					UserInfo.routers = Navigation.getRouters();
				} catch (Exception e) {
					// �б��ȡʧ��
					message.what = 1;
					message.obj = "������,��ȡ�Ա�ʧ��-----Message:" + e.toString();
				}
			}
		}
		return message;

	}
}
