package com.ztbeacon.client.activity.store;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ztbeacon.client.ClientApplication;
import com.ztbeacon.client.R;
import com.ztbeacon.client.activity.navi.NaviActivity;
import com.ztbeacon.client.network.HttpClient;
import com.ztbeacon.client.network.Request;
import com.ztbeacon.client.network.mode.RequestParam;
import com.ztbeacon.client.network.mode.message.GetStoreInfoResponseParam;
import com.ztbeacon.client.network.mode.message.StoreInfoRequestParam;

public class StoreInfoActivity extends Activity{
	private TextView nameTv, descriptionTv, addrTv, phoneTv, netTv, detailTv;
	private ImageView logoIv;
	private GetStoreInfoTask mGetStoreInfoTask;
	private Bitmap bitmap;
	private String shopId,word;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storeinfo);
		ClientApplication.isReady = true;
		//取得bundle对象 
		Bundle bundle = this.getIntent().getExtras();
		word = bundle.getString("word");
		shopId = bundle.getString("id");
		nameTv = (TextView) findViewById(R.id.store_name);
		nameTv.setText(word);
		descriptionTv = (TextView) findViewById(R.id.store_description);
		addrTv = (TextView) findViewById(R.id.store_info_addr);
		phoneTv = (TextView) findViewById(R.id.store_info_phone);
		netTv = (TextView) findViewById(R.id.store_info_net);
		detailTv = (TextView) findViewById(R.id.store_detail);
		logoIv = (ImageView) findViewById(R.id.search_img);
		loadData();
	}
	private void loadData() {
		if(shopId!=null){
		StoreInfoRequestParam mRequestParam = new StoreInfoRequestParam();
		mRequestParam.setToken("dd7f0c50b3d201a3d7a78635913a151d");
		mRequestParam.setShopId(shopId);
		mGetStoreInfoTask = new GetStoreInfoTask();
		mGetStoreInfoTask.execute(mRequestParam);
		}
	}
	public void clickBack(View v){
		this.finish();
	}
	public void ClickNavi(View v){
		Intent next = new Intent();
		next.setClass(this, NaviActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("word", word);
		next.putExtras(bundle);
		startActivity(next);
		this.finish();
	}
	  private class GetStoreInfoTask extends AsyncTask < StoreInfoRequestParam,Integer,JSONObject > {
		    private ProgressDialog dialog;

		    protected void onPreExecute() {
		      dialog = ProgressDialog.show(StoreInfoActivity.this, "等待更新", getText(R.string.waiting));
		      super.onPreExecute();
		    }

		protected JSONObject doInBackground(StoreInfoRequestParam... params) {
			// 如果网络没有连接则更新进度为 网络连接异常
			if (!HttpClient.isConnect(getApplication().getApplicationContext())) {
				return null;
			}

			StoreInfoRequestParam requestParam = (StoreInfoRequestParam) params[0];
			System.out.println("RequestParam.GET_MESSAGE:---"
					+ requestParam.getJSON());

			String res = Request.request(requestParam.getJSON(),
					RequestParam.GET_STORE_INFO);
			System.out.println(res);
			// 如果请求结果为空字符串，则请求失败
			if ("".equals(res)) {
				return null;
			}
			JSONObject storeInfo = null;
			try {
				GetStoreInfoResponseParam response = new GetStoreInfoResponseParam(res);
				storeInfo = response.getStoreInfo();
				System.out.println("商家详情：" + storeInfo);
				bitmap =  getHttpBitmap(storeInfo.getString("url"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return storeInfo;
		}

		    private Bitmap getHttpBitmap(String url) {
		    	URL myFileURL;
		        Bitmap bitmap=null;
		        try{
		            myFileURL = new URL(url);
		            //获得连接
		            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
		            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
		            conn.setConnectTimeout(6000);
		            //连接设置获得数据流
		            conn.setDoInput(true);
		            //不使用缓存
		            conn.setUseCaches(false);
		            //这句可有可无，没有影响
		            //conn.connect();
		            //得到数据流
		            InputStream is = conn.getInputStream();
		            //解析得到图片
		            bitmap = BitmapFactory.decodeStream(is);
		            //关闭数据流
		            is.close();
		        }catch(Exception e){
		            e.printStackTrace();
		        }
		        return bitmap;
			}

			protected void onPostExecute(JSONObject result) {
				try {
				if (result != null) {
					descriptionTv.setText(result.getString("description"));
					addrTv.setText(result.getString("address"));
					phoneTv.setText(result.getString("telephone"));
					netTv.setText(result.getString("logo"));
					detailTv.setText(result.getString("detail"));
					//Bitmap bitmap = getHttpBitmap(result.getString("url"));
					logoIv.setImageBitmap(bitmap);
				} else {
					descriptionTv.setText("等待更新");
					addrTv.setText("等待更新");
					phoneTv.setText("等待更新");
					netTv.setText("等待更新");
					detailTv.setText("等待更新");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      dialog.dismiss();
		      super.onPostExecute(result);
		    }
		  }
}
