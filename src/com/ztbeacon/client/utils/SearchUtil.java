package com.ztbeacon.client.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ztbeacon.client.entity.Word;

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
				// 插入数据
				addWord("a", "aaa");
				/*for(int i=0;i<listWords.size();i++){
					addWord(listWords.get(i).getWord(), listWords.get(i).getDefinition());
				}*/
			}
		}).start();
	}

	@SuppressWarnings("unchecked")
	public List<Word> getMatches(String query) {
		List<Word> list = mDict.get(query);
		return list == null ? Collections.EMPTY_LIST : list;
	}

	private void addWord(String word, String definition) {
		final Word theWord = new Word(word, definition);

		final int len = word.length();
		for (int i = 0; i < len; i++) {
			final String prefix = word.substring(0, len - i);
			addMatch(prefix, theWord);
		}
	}

	private void addMatch(String query, Word word) {
		List<Word> matches = mDict.get(query);
		if (matches == null) {
			matches = new ArrayList<Word>();
			mDict.put(query, matches);
		}
		matches.add(word);
	}
}
