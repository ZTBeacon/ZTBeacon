package com.ztbeacon.client.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ztbeacon.client.entity.Word;
import com.ztbeacon.client.network.Request;
import com.ztbeacon.client.network.mode.RequestParam;
import com.ztbeacon.client.network.mode.message.GetKeyWordResponseParam;
import com.ztbeacon.client.network.mode.message.GetMessageResponseParam;
import com.ztbeacon.client.network.mode.message.KeyWordRequestParam;
import com.ztbeacon.client.network.mode.message.MessageRequestParam;

public class SearchUtil {
	private static final SearchUtil sInstance = new SearchUtil();
	private final Map<String, List<Word>> mDict = new ConcurrentHashMap<String, List<Word>>();
	private List<Word> listWords;
	public static SearchUtil getInstance() {
		return sInstance;
	}

	private SearchUtil() {
	}
	public SearchUtil(List<Word> words){
		this.listWords = words;
	}
	private boolean mLoaded = false;

	public synchronized void ensureLoaded() {
		if (mLoaded)
			return;
		new Thread(new Runnable() {
			public void run() {
				try {
					// 插入数据
					//addWord("a", "aaa");
					System.out.println("载入keyword数据");
					KeyWordRequestParam mRequestParam = new KeyWordRequestParam();
					mRequestParam.setToken("dd7f0c50b3d201a3d7a78635913a151d");
					mRequestParam.setMarketId("1");
					mRequestParam.setMaxnum(100);
					String res = Request.request(mRequestParam.getJSON(), RequestParam.GET_STORE_LIST);
					GetKeyWordResponseParam response = new GetKeyWordResponseParam(res);
					JSONArray keyWordlist = response.getKeyWordJson();
					for(int i = 0;i<keyWordlist.length();i++){
						JSONObject object = keyWordlist.getJSONObject(i);
						addWord(object.get("id").toString(),object.get("name").toString(), object.get("address").toString());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@SuppressWarnings("unchecked")
	public List<Word> getMatches(String query) {
		List<Word> list = mDict.get(query);
		return list == null ? Collections.EMPTY_LIST : list;
	}

	private void addWord(String id,String word, String definition) {
		final Word theWord = new Word(id,word, definition);

		final int len = word.length();
		for (int i = 0; i < len; i++) {
			final String prefix = word.substring(0, len - i);
			addMatch(prefix, theWord);
		}
	}
/*	private void addWord(String word, String definition) {
		final Word theWord = new Word(word, definition);

		final int len = word.length();
		for (int i = 0; i < len; i++) {
			final String prefix = word.substring(0, len - i);
			addMatch(prefix, theWord);
		}
	}*/
	private void addMatch(String query, Word word) {
		List<Word> matches = mDict.get(query);
		if (matches == null) {
			matches = new ArrayList<Word>();
			mDict.put(query, matches);
		}
		matches.add(word);
	}
}
