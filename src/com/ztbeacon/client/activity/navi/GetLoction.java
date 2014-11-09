package com.ztbeacon.client.activity.navi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

import com.ztbeacon.client.entity.UserInfo;
import com.ztbeacon.client.entity.iBeaconClass.iBeacon;


/**
 * @author ��С�� E-mail:493026465@qq.com
 * @version ����ʱ�䣺2014��11��5�� ����9:21:32 ��˵�����õ����ڵط���
 */
public class GetLoction {
	private static final int NEAR = -80;
	private static final int FAR = -90;
	static final char digits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public final static Message getBeacon(ArrayList<iBeacon> beacons) {
		// ����
		int[] results = new int[10];
		for (int i = 0; i < results.length; i++) {
			results[i] = beacons.get(i).rssi;
		}
		Arrays.sort(results);
		int result = (results[3] + results[4] + results[5] + results[6]) / 4;
		Log.e("Error", getHex(beacons.get(0).major)
				+ getHex(beacons.get(0).minor));
		return getDistance(result, getHex(beacons.get(0).major)
				+ getHex(beacons.get(0).minor));
	}

	private static Message getDistance(int result, String name) {
		Message msg = new Message();
		if (result >= NEAR) {
			msg.what = 3;
			try {
				msg.obj = name;
				JSONObject resutl = postLoc(name);

				UserInfo.srcInfo.marketName = resutl.getJSONObject("location")
						.getJSONObject("market").getString("marketname");
				UserInfo.srcInfo.pos_x = resutl.getJSONObject("location")
						.getJSONObject("position").getDouble("pos_x");
				UserInfo.srcInfo.pos_y = resutl.getJSONObject("location")
						.getJSONObject("position").getDouble("pos_y");
				UserInfo.srcInfo.pos_z = resutl.getJSONObject("location")
						.getJSONObject("position").getDouble("pos_z");
				UserInfo.srcInfo.marketId = resutl.getJSONObject("location")
						.getJSONObject("market").getInt("marketid");
				UserInfo.srcInfo.name = name;
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// �߽���
		} else if (result <= FAR) {
			// Զ����
		}
		return msg;
	}

	private final static String getHex(int major) {
		int length = 32;
		char[] result = new char[length];
		char[] finalresult = { '0', '0', '0', '0' };
		do {
			result[--length] = digits[major & 15];
			major >>>= 4;
		} while (major != 0);
		for (int i = length, j = 3; i < result.length; i++, j--) {
			finalresult[j] = result[i];
		}
		return new String(finalresult);
	}
	
	private static JSONObject getJson(String id) throws JSONException {
		JSONObject root = new JSONObject();
		root.put("token", UserInfo.token);
		JSONArray ibeaconInfos = new JSONArray();
		JSONObject ibeaconInfo = new JSONObject();
		ibeaconInfo.put("id", id);
		ibeaconInfo.put("state", "near");
		ibeaconInfos.put(ibeaconInfo);
		root.put("ibeacon", ibeaconInfos);
		return root;
	}

	static final JSONObject postLoc(String id) throws ClientProtocolException, IOException, JSONException {
		HttpPost request = new HttpPost(
				"http://112.11.119.154//ServiceAPI/locator/");//��λ
		// �ȷ�װһ�� JSON ����
		// �󶨵����� Entry
		StringEntity se = new StringEntity(getJson(id).toString());
		request.setEntity(se);
		// ��������
		HttpResponse httpResponse = new DefaultHttpClient().execute(request);
		// �õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ���������
		String retSrc = EntityUtils.toString(httpResponse.getEntity());
		// // ���� JSON ����
		return new JSONObject(retSrc);
	}
	
	
}
