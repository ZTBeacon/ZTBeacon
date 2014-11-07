package com.ztbeacon.client.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ztbeacon.client.R;
import com.ztbeacon.client.activity.navi.NaviActivity;
import com.ztbeacon.client.activity.store.StoreInfoActivity;
import com.ztbeacon.client.adapter.HomeSearchListAdapter;
import com.ztbeacon.client.adapter.SpinnerButton;
import com.ztbeacon.client.entity.Word;
import com.ztbeacon.client.utils.SearchUtil;

public class HomeResultActivity extends Activity{
	private EditText etSearch;
	private ListView msgList;
	private HomeSearchListAdapter mAdapter;
	private SpinnerButton mSpinnerBtn;
	private List<Map<String,Object>> msgData = new ArrayList<Map<String,Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_search);
		Intent intent = this.getIntent();  
		 //判断intent的action是否等于action-view  
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
		    // Handle the normal search query case
		    String query = intent.getStringExtra(SearchManager.QUERY);
		    System.out.println("ACTION_SEARCH:"+query);
		}else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			Word theWord = SearchUtil.getInstance()
					.getMatches(intent.getDataString().trim().toLowerCase())
					.get(0);
			System.out.println("SearchUtil.getInstance()");
			launchWord(theWord);
			finish();
		} else {
			etSearch = (EditText) findViewById(R.id.search_etSearch);
			etSearch.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					System.out.println("onSearchRequested");
					onSearchRequested();
					return false;
				}
			});
			msgList = (ListView) findViewById(R.id.search_listview);
			bindData();
			initData();
			this.mSpinnerBtn = (SpinnerButton) this
   				.findViewById(R.id.search_spinner_btn);
			Bundle bundle = this.getIntent().getExtras();  
			final String type = bundle.getString("type");
			this.mSpinnerBtn.setText(type);
   		// 设置下拉布局资源文件,布局创建监听器，以便实例化控件对象
   		mSpinnerBtn.setResIdAndViewCreatedListener(R.layout.spinner_choice_items,
   				new SpinnerButton.ViewCreatedListener(){
   					public void onViewCreated(View v) {
   				
   						// TODO Auto-generated method stub
   						v.findViewById(R.id.spinner_food).setOnClickListener(new View.OnClickListener() {
   							public void onClick(View v) {
   								// TODO Auto-generated method stub
   								handleClick(((TextView)v).getText().toString());
   							}
   						});
   						v.findViewById(R.id.spinner_play).setOnClickListener(new View.OnClickListener() {
   							public void onClick(View v) {
   								// TODO Auto-generated method stub
   								handleClick(((TextView)v).getText().toString());
   							}
   						});
   						v.findViewById(R.id.spinner_shop).setOnClickListener(new View.OnClickListener() {
   							public void onClick(View v) {
   								// TODO Auto-generated method stub
   								handleClick(((TextView)v).getText().toString());
   							}
   						});
   					}
   			
   		},0,type);
		}
	}


	private void initData() {
		
		mAdapter = new HomeSearchListAdapter(HomeResultActivity.this,msgData,msgList);
		msgList.setAdapter(mAdapter);
		msgList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Toast.makeText(SearchActivity.this, "你点击了"+arg2,
				// Toast.LENGTH_LONG).show();
				startActivity(new Intent(HomeResultActivity.this,
						StoreInfoActivity.class));

			}
		});
	}


	private void bindData() {
		try {
			// 清空列表
			msgData.removeAll(msgData);
			for (int i = 0; i < 8; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", "美食城" + i);
				map.put("addr", "地址：老城隍庙地址是黄浦区安仁街218号");
				map.put("img_url",
						"http://www.898.travel/img/20100511/20100511162952_xdyjxbkd.jpg");
				map.put("daozhequ", "9936");
				map.put("phone", "18815294585");
				msgData.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("BindData-------错误信息："+e.toString());
		}
	}


	// 存储值，进行界面跳转
	private void launchWord(Word pavilion) {
		Intent next = new Intent();
		next.setClass(this, NaviActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("word", pavilion.getWord());
		next.putExtras(bundle);
		next.putExtras(bundle);
		startActivity(next);
	}
   private void handleClick(String text){
		mSpinnerBtn.dismiss();
		mSpinnerBtn.setText(text);
	}
}
