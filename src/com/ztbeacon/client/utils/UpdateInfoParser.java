package com.ztbeacon.client.utils;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.ztbeacon.client.entity.UpdateInfo;

public class UpdateInfoParser {
	public static UpdateInfo getUpdateInfo(InputStream is) throws XmlPullParserException, IOException{
		//获得一个Pull解析的实例
		XmlPullParser parser = Xml.newPullParser();
		//将要解析的文件传入
		parser.setInput(is,"UTF-8");
		UpdateInfo info = new UpdateInfo();
		//获取当前触发事件的类型
		int type = parser.getEventType();
		//使用事件码来判断循环是否结束
		while(type != XmlPullParser.END_DOCUMENT){
			if(type == XmlPullParser.START_TAG){ //开始元素
				if("version".equals(parser.getName())){
					String version = parser.nextText();
					info.setVersion(version);
				}else if("description".equals(parser.getName())){
					String description = parser.nextText();
					info.setDescription(description);
				}else if("apkurl".equals(parser.getName())){
					String apkurl = parser.nextText();
					info.setApkurl(apkurl);
				}
			}
			type = parser.next();
		}
		return info;
	}
}
