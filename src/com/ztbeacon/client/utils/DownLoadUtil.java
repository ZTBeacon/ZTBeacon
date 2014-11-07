package com.ztbeacon.client.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;

public class DownLoadUtil {
public static File getFile(String urlPath,String filePath,ProgressDialog pd){
	try{
		URL url = new URL(urlPath);
		File file = new File(filePath);
		System.out.println(filePath);
		FileOutputStream fos = new FileOutputStream(file);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		//下载的请求是GET方式，conn的默认方式也是GET请求
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
		//获取服务端的文件总长度
		int max = conn.getContentLength();
		//设置进度条的最大值
		pd.setMax(max);
		//获取要下载的APK的文件的输入流
		InputStream is = conn.getInputStream();
		//设置一个缓存区
		byte[] buffer = new byte[1024];
		int len = 0;
		int process = 0;
		while((len = is.read(buffer))!=-1){
			fos.write(buffer, 0, len);
			//每读取一次输入流，就刷新一次下载进度
			process+=len;
			pd.setProgress(process);
			Thread.sleep(30);
		}
		//刷新缓存数据到文件中
		fos.flush();
		//关流
		fos.close();
		is.close();
		return file;
	}catch(Exception e){
		e.printStackTrace();
		return null;
	}
}
		/*
		 * 获取一个路径中的文件名。例如：mobilesafe.apk
		 * */	
	public static String getFileName(String urlPath){
		return urlPath.substring(urlPath.lastIndexOf("/")+1,urlPath.length());
	}
}
