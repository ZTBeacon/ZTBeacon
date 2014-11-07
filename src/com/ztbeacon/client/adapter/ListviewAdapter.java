package com.ztbeacon.client.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ztbeacon.client.R;
import com.ztbeacon.client.database.table.MessageTable;
import com.ztbeacon.client.loader.SyncImageLoader;
import com.ztbeacon.client.utils.StringTools;

public class ListviewAdapter extends BaseAdapter {

	private static final String TAG = "ListviewAdapter";
	private Context mContext = null;
	private LayoutInflater inflater = null;
	private List<HashMap<String, Object>> mData = null;
	SyncImageLoader syncImageLoader;
	private ListView mListView;

	public ListviewAdapter(Context context, List<HashMap<String, Object>> list,
			ListView listview) {
		// super();
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mContext = context;
		this.mData = list;
		this.mListView = listview;
		syncImageLoader = new SyncImageLoader();
	}

	class ViewHolder {
		// 消息标题
		TextView msg_title;
		TextView msg_addr;
		ImageView msg_img;
		TextView msg_ready;
		TextView msg_zan;
		LinearLayout layout;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Map<String, Object> getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			ViewHolder holder = null;
			if (convertView != null) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = inflater.inflate(R.layout.search_result_item,
						null);
				holder = new ViewHolder();
				holder.msg_title = (TextView) convertView
						.findViewById(R.id.search_name);
				holder.msg_addr = (TextView) convertView
						.findViewById(R.id.search_address);
				holder.msg_img = (ImageView) convertView
						.findViewById(R.id.search_img);
				holder.msg_ready = (TextView) convertView
						.findViewById(R.id.search_ready);
				holder.msg_zan = (TextView) convertView
						.findViewById(R.id.search_zan);
				holder.layout = (LinearLayout)convertView.findViewById(R.id.search_ll);
				convertView.setTag(holder);
			}
			Map<String, Object> map = mData.get(position);
			String title = map.get(MessageTable.name).toString();
			String addr = map.get(MessageTable.address).toString();
			String ready = map.get(MessageTable.ready).toString();
			String zan = map.get(MessageTable.zan).toString();
			String url = map.get(MessageTable.url).toString();
			String img = StringTools.getFileNameFromUrl(url);
			Log.d(TAG, "URL:"+url+"-------name:"+img);
			holder.msg_title.setText(title);
			holder.msg_addr.setText(addr);
			holder.msg_img.setBackgroundResource(R.drawable.wait);
			holder.msg_img.setTag(position);
			holder.msg_ready.setText("(" + ready + ")       ");
			holder.msg_zan.setText("(" + zan + ")");
			if(map.get("istop")!=null&&Integer.parseInt(map.get("istop").toString())>0){
				holder.layout.setBackgroundResource(R.drawable.search_item_p);
			}else{
				holder.layout.setBackgroundResource(R.color.white);
			}
			syncImageLoader.loadImage(position, url, imageLoadListener, img);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "ListgetView--------错误信息：" + e.toString());
		}
		return convertView;
	}

	SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {
		public void onImageLoad(Integer t, Drawable drawable) {
			// BookModel model = (BookModel) getItem(t);
			try {
				View view = mListView.findViewWithTag(t);
				if (view != null) {
					ImageView iv = (ImageView) view
							.findViewById(R.id.search_img);
					iv.setBackgroundDrawable(drawable);
					iv.setTag("");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "SyncImageLoader.OnImageLoadListener-------错误信息："
						+ e.toString());
			}
		}
		public void onError(Integer t) {

		}
	};
}
