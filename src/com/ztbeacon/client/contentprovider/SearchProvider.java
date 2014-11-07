package com.ztbeacon.client.contentprovider;

import java.util.List;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.ztbeacon.client.entity.Word;
import com.ztbeacon.client.utils.SearchUtil;

public class SearchProvider extends ContentProvider {
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		System.out.println("delete");
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		System.out.println("getType");
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		System.out.println("insert");
		return null;
	}

	@Override
	public boolean onCreate() {
		SearchUtil.getInstance().ensureLoaded();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String query = null;
		if (uri.getPathSegments().size() > 1) {
			query = uri.getLastPathSegment().toLowerCase();
		}
		return getSuggestions(query);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private Cursor getSuggestions(String query) {
		String processedQuery = query == null ? "" : query.toLowerCase();
		List<Word> words = SearchUtil.getInstance().getMatches(
				processedQuery);
		MatrixCursor cursor = new MatrixCursor(COLUMNS);
		long id = 0;
		for (Word word : words) {
			cursor.addRow(columnValuesOfWord(id++, word));
		}
		return cursor;
	}

	private Object[] columnValuesOfWord(long id, Word word) {
		return new Object[] { 
				id, // _id
				word.getWord(), // text1
				word.getDefinition(), // text2
				word.getWord(), // intent_data (included when clicking on item)
		};
	}

	private static final String[] COLUMNS = { "_id",
			SearchManager.SUGGEST_COLUMN_TEXT_1,
			SearchManager.SUGGEST_COLUMN_TEXT_2,
			SearchManager.SUGGEST_COLUMN_INTENT_DATA,// 数据传递到intenter中
	};

}
