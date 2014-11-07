package com.ztbeacon.client.loader;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import com.ztbeacon.client.ClientApplication;

import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;

public class SyncImageExpandLoader {

private Object lock = new Object();
	
	private boolean mAllowLoad = true;
	
	private boolean firstLoad = true;
	
	private int mStartLoadLimit = 0;
	
	private int mStopLoadLimit = 0;
	
	final Handler handler = new Handler();
	
	private HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();   
	
	public interface OnImageLoadListener {
		public void onImageLoad(Integer t,Integer c, Drawable drawable);
		public void onError(Integer t,Integer c);
	}
	
	public void setLoadLimit(int startLoadLimit,int stopLoadLimit){
		if(startLoadLimit > stopLoadLimit){
			return;
		}
		mStartLoadLimit = startLoadLimit;
		mStopLoadLimit = stopLoadLimit;
	}
	
	public void restore(){
		mAllowLoad = true;
		firstLoad = true;
	}
		
	public void lock(){
		mAllowLoad = false;
		firstLoad = false;
	}
	
	public void unlock(){
		mAllowLoad = true;
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public void loadImage(Integer t,Integer c, String imageUrl,
			OnImageLoadListener listener,String imageName) {
		final OnImageLoadListener mListener = listener;
		final String mImageUrl = imageUrl;
		final Integer mt = t;
		final Integer mc = c;
		final String imgName = imageName;
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				if(!mAllowLoad){
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				if(mAllowLoad && firstLoad){
					loadImage(mImageUrl, mt,mc, mListener,imgName);
				}
				
				if(mAllowLoad && mt <= mStopLoadLimit && mt >= mStartLoadLimit){
					loadImage(mImageUrl, mt,mc, mListener,imgName);
				}
			}

		}).start();
	}
	
	private void loadImage(final String mImageUrl,final Integer mt,final Integer mc,final OnImageLoadListener mListener,final String imageName){
		
		if (imageCache.containsKey(mImageUrl)) {  
			System.out.println("drawable");
            SoftReference<Drawable> softReference = imageCache.get(mImageUrl);  
            final Drawable d = softReference.get();  
            if (d != null) {  
            	handler.post(new Runnable() {
    				@Override
    				public void run() {
    					if(mAllowLoad){
    						mListener.onImageLoad(mt,mc, d);
    					}
    				}
    			});
                return;  
            }  
        }  
		try {
			final Drawable d = loadImageFromUrl(mImageUrl,imageName);
			if(d != null){
                imageCache.put(mImageUrl, new SoftReference<Drawable>(d));
			}
			handler.post(new Runnable() {
				@Override
				public void run() {
					if(mAllowLoad){
						mListener.onImageLoad(mt,mc, d);
					}
				}
			});
		} catch (IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mListener.onError(mt,mc);
				}
			});
			e.printStackTrace();
		}
	}

	public static Drawable loadImageFromUrl(String url,String imageName) throws IOException {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//手机存储卡TXYCciot目录是否存在
			String FileUrl = ClientApplication.mLocalSavePath;
			File folder = new File(FileUrl);
			if(!folder.exists()){
				folder.mkdir();
			}
			File f = new File(FileUrl+imageName);
			//SD
			if(f.exists()){
				FileInputStream fis = new FileInputStream(f);
				Drawable d = Drawable.createFromStream(fis, "src");
				return d;
			}

			URL m = new URL(url);
			InputStream i = (InputStream) m.getContent();
			DataInputStream in = new DataInputStream(i);
			FileOutputStream out = new FileOutputStream(f);
			byte[] buffer = new byte[1024];
			int   byteread=0;
			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
			in.close();
			out.close();
			Drawable d = Drawable.createFromStream(i, "src");
			return loadImageFromUrl(url,imageName);
		}

		else{
			URL m = new URL(url);
			InputStream i = (InputStream) m.getContent();
			Drawable d = Drawable.createFromStream(i, "src");
			return d;
		}
		
	}
}
