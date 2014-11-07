package com.ztbeacon.client.activity.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.iflytek.cloud.SpeechUtility;
import com.ztbeacon.client.ClientApplication;
import com.ztbeacon.client.R;
import com.ztbeacon.client.activity.navi.NaviActivity;
import com.ztbeacon.client.activity.store.StoreInfoActivity;
import com.ztbeacon.client.adapter.ListviewAdapter;
import com.ztbeacon.client.adapter.SpinnerButton;
import com.ztbeacon.client.database.table.MessageTable;
import com.ztbeacon.client.entity.Position;
import com.ztbeacon.client.entity.Word;
import com.ztbeacon.client.network.HttpClient;
import com.ztbeacon.client.network.Request;
import com.ztbeacon.client.network.mode.RequestParam;
import com.ztbeacon.client.network.mode.message.GetMessageResponseParam;
import com.ztbeacon.client.network.mode.message.MessageRequestParam;
import com.ztbeacon.client.utils.SearchUtil;

public class SearchActivity extends Activity {
  private EditText etSearch;
  private ListView msgList;
  private ListviewAdapter mAdapter;
  private SpinnerButton mSpinnerBtn;
  private List < HashMap < String,
  Object >> msgData = new ArrayList < HashMap < String,
  Object >> ();
  private GetMessageTask mGetMessageTask;
  private ClientApplication clientApplication;
  int clickedMsg;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_result);
    clientApplication = (ClientApplication) getApplication();

    Intent intent = this.getIntent();

    // 判断intent的action是否等于action-view
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      // Handle the normal search query case
      String query = intent.getStringExtra(SearchManager.QUERY);
      System.out.println("ACTION_SEARCH:" + query);
    } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
      Word theWord = SearchUtil.getInstance().getMatches(intent.getDataString().trim().toLowerCase()).get(0);
      System.out.println("您点击了第几家店:"+theWord.getId());
      launchWord(theWord);
      finish();
    } else {
      etSearch = (EditText) findViewById(R.id.search_etSearch);
      etSearch.setOnTouchListener(new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
          onSearchRequested();
          return false;
        }
      });
      msgList = (ListView) findViewById(R.id.search_listview);
      bindData();
      initData();
      this.mSpinnerBtn = (SpinnerButton) this.findViewById(R.id.search_spinner_btn);
      SpeechUtility.createUtility(SearchActivity.this, "appid=" + getString(R.string.app_id));
      // 设置下拉布局资源文件,布局创建监听器，以便实例化控件对象
      mSpinnerBtn.setResIdAndViewCreatedListener(R.layout.spinner_choice_items, new SpinnerButton.ViewCreatedListener() {
        public void onViewCreated(View v) {
          // TODO Auto-generated method stub
          v.findViewById(R.id.spinner_food).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              handleClick(((TextView) v).getText().toString());
            }
          });
          v.findViewById(R.id.spinner_play).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              handleClick(((TextView) v).getText().toString());
            }
          });
          v.findViewById(R.id.spinner_shop).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              handleClick(((TextView) v).getText().toString());
            }
          });
        }
      },
      0, "");
      registerForContextMenu(msgList);
    }
  }

  public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = ( AdapterView.AdapterContextMenuInfo ) item.getMenuInfo();
		switch (item.getItemId()) {
		case 0:
			setTopMessage(menuInfo.position);
			break;
		case 1:
			deleteMessage(menuInfo.position);
			break;
		case 2:
			setTopMessage(menuInfo.position);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
  }

  private void setTopMessage(int position) {
	  HashMap<String, Object> map = new HashMap<String, Object>();
	  map = msgData.get(position);
	  msgData.remove(position);
	  int index = 0;
	  if(map.get("istop")!=null&&Integer.parseInt(map.get("istop").toString())>0){  //如果置顶，取消置顶
		  map.put("istop",0);
		  index = MessageTable.getTopCount(clientApplication.getDatabaseHelper());
	  }else{
		  map.put("istop", MessageTable.getTopNum(clientApplication.getDatabaseHelper())+1);
	  }
	  msgData.add(index, map);
	  mAdapter.notifyDataSetChanged();
	  MessageTable.updateMessageTable(clientApplication.getDatabaseHelper(),map);
  }

  private void deleteMessage(int position) {
	  msgData.remove(position);
	  mAdapter.notifyDataSetChanged();
	  MessageTable.deleteMessageTable(clientApplication.getDatabaseHelper(), msgData.get(position).get("id").toString());
  }


public void onCreateContextMenu(ContextMenu menu, View v,
		ContextMenuInfo menuInfo) {
	if(msgData.get(clickedMsg).get("istop")!=null&&Integer.parseInt(msgData.get(clickedMsg).get("istop").toString())>0)
	  menu.add(1, 2, 0, "取消置顶");
	else
	  menu.add(1, 0, 0, "置顶");
	  menu.add(1, 1, 1, "删除");
	  super.onCreateContextMenu(menu, v, menuInfo);
  }
  private void initData() {
    mAdapter = new ListviewAdapter(SearchActivity.this, msgData, msgList);
    msgList.setAdapter(mAdapter);
    msgList.setOnItemClickListener(new OnItemClickListener() {
    	@Override 
    	public void onItemClick(AdapterView < ?>arg0, View arg1, int arg2, long arg3) {
        startActivity(new Intent(SearchActivity.this, StoreInfoActivity.class));
      }
    });
    msgList.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			clickedMsg = arg2;
			System.out.println("被点击的setOnItemLongClickListener:"+arg2);
			return false;
		}
	});
  }

  private void bindData() {
    try {
      Position pos = new Position(48.9f, 19.2f, 4f, 5);
      MessageRequestParam mRequestParam = new MessageRequestParam();
      mRequestParam.setToken("dd7f0c50b3d201a3d7a78635913a151d");
      mRequestParam.setMarketId("1");
      mRequestParam.setPosition(pos);
      mGetMessageTask = new GetMessageTask();
      mGetMessageTask.execute(mRequestParam);
      // 清空列表
      msgData.removeAll(msgData);
      //显示本地数据
      msgData = MessageTable.getListMessageTable(clientApplication.getDatabaseHelper());
    } catch(Exception e) {
      e.printStackTrace();
      System.out.println("BindData-------错误信息：" + e.toString());
    }
  }

  // 存储值，进行界面跳转
  private void launchWord(Word pavilion) {
    Intent next = new Intent();
    next.setClass(this, StoreInfoActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString("word", pavilion.getWord());
    next.putExtras(bundle);
    next.putExtras(bundle);
    startActivity(next);
  }

  private void handleClick(String text) {
    mSpinnerBtn.dismiss();
    mSpinnerBtn.setText(text);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }

  private class GetMessageTask extends AsyncTask < MessageRequestParam,Integer,Boolean > {
    private ProgressDialog dialog;
    List < ContentValues > responseValues;

    protected void onPreExecute() {
      dialog = ProgressDialog.show(SearchActivity.this, "", getText(R.string.waiting));
      super.onPreExecute();
    }

    protected Boolean doInBackground(MessageRequestParam...params) {
      //如果网络没有连接则更新进度为 网络连接异常
      if (!HttpClient.isConnect(getApplication().getApplicationContext())) {
        return false;
      }

      MessageRequestParam requestParam = (MessageRequestParam) params[0];
      System.out.println("RequestParam.GET_MESSAGE:---" + requestParam.getJSON());

      String res = Request.request(requestParam.getJSON(), RequestParam.GET_MESSAGE);
      System.out.println(res);
      // 如果请求结果为空字符串，则请求失败
      if ("".equals(res)) {
        return false;
      }
      try {
        GetMessageResponseParam response = new GetMessageResponseParam(res);

        if (response.getResult() != GetMessageResponseParam.RESULT_SUCCESS) {
          return false;
        }

        responseValues = response.getMessageEntity();

        for (ContentValues values: responseValues) {
          MessageTable.insertMessageTable(clientApplication.getDatabaseHelper(), values);
        }
      } catch(JSONException e) {
        e.printStackTrace();
        return false;
      }
      return true;
    }

    protected void onPostExecute(Boolean result) {
      if (result) {
        for (ContentValues values: responseValues) {
          HashMap < String,Object > map = new HashMap < String,Object > ();
          map.put("id", values.get("id"));
          map.put("name", values.get("name"));
          map.put("address", values.get("address"));
          map.put("url", values.get("url"));
          map.put("ready", values.get("ready"));
          map.put("zan", values.get("zan"));
          msgData.add(MessageTable.getTopCount(clientApplication.getDatabaseHelper()), map);
        }
        mAdapter.notifyDataSetChanged();
      }
      dialog.dismiss();
      super.onPostExecute(result);
    }
  }
}