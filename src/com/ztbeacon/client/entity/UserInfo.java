package com.ztbeacon.client.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ��С�� E-mail:493026465@qq.com
 * @version ����ʱ�䣺2014��11��5�� ����4:32:47 ��˵��
 */
public class UserInfo {
	public static class srcInfo {
		public static int marketId;
		public static String marketName;
		public static double pos_x;
		public static double pos_y;
		public static double pos_z;
		public static String name;
	}

	public static class dstInfo {
		public static double pos_x;
		public static double pos_y;
		public static double pos_z;
	}

	public static List<Router> routers = new ArrayList<Router>();
	public static String token = "dd7f0c50b3d201a3d7a78635913a151d";

	public static int addAngle =0;
	public static int angle = 0 + addAngle;
	public static String distance = "正在导航，青稍等";

}
