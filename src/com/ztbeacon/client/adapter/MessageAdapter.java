package com.ztbeacon.client.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.ztbeacon.client.R;
import com.ztbeacon.client.database.table.MessageTable;

public class MessageAdapter extends ResourceCursorAdapter {

	public MessageAdapter(Context context, int layout, Cursor c,
			boolean autoRequery) {
		super(context, layout, c, autoRequery);
	}
	@Override
	public ContentValues getItem(int position) {
		ContentValues values = new ContentValues();
		Cursor c = (Cursor) super.getItem(position);
		values.put(MessageTable._id, c.getInt(c.getColumnIndex(MessageTable._id)));
		values.put(MessageTable.id, c.getString(c.getColumnIndex(MessageTable.id)));
		values.put(MessageTable.name, c.getString(c.getColumnIndex(MessageTable.name)));
		values.put(MessageTable.address, c.getString(c.getColumnIndex(MessageTable.address)));
		values.put(MessageTable.photo, c.getString(c.getColumnIndex(MessageTable.photo)));
		values.put(MessageTable.category, c.getString(c.getColumnIndex(MessageTable.category)));
		values.put(MessageTable.description, c.getString(c.getColumnIndex(MessageTable.description)));
		values.put(MessageTable.detail, c.getString(c.getColumnIndex(MessageTable.detail)));
		values.put(MessageTable.telephone, c.getString(c.getColumnIndex(MessageTable.telephone)));
		values.put(MessageTable.url, c.getString(c.getColumnIndex(MessageTable.url)));
		values.put(MessageTable.position, c.getString(c.getColumnIndex(MessageTable.position)));
		return values;
	}
	public void refresh() {
		this.getCursor().requery();
	}
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		 final ContactListItemCache cache = (ContactListItemCache) view.getTag();
		 cursor.copyStringToBuffer(cursor.getColumnIndex(MessageTable.name), cache.nameBuffer);
		 cursor.copyStringToBuffer(cursor.getColumnIndex(MessageTable.address), cache.addressBuffer);
		 cursor.copyStringToBuffer(cursor.getColumnIndex(MessageTable.ready), cache.readyBuffer);
		 cursor.copyStringToBuffer(cursor.getColumnIndex(MessageTable.zan), cache.zanBuffer);
		 cache.nameView.setText(cache.nameBuffer.data, 0, cache.nameBuffer.sizeCopied);
		 cache.addressView.setText(cache.addressBuffer.data, 0, cache.addressBuffer.sizeCopied);
		 cache.zanView.setText(cache.zanBuffer.data, 0, cache.zanBuffer.sizeCopied);
		 cache.readyView.setText(cache.readyBuffer.data, 0, cache.readyBuffer.sizeCopied);
		 //cache.imgView.setImageResource(R.drawable.log_incoming);
		 
	}
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = super.newView(context, cursor, parent);
		ContactListItemCache cache = new ContactListItemCache();
		cache.nameView = (TextView) view.findViewById(R.id.search_name);
		cache.addressView = (TextView) view.findViewById(R.id.search_address);
		cache.imgView = (ImageView) view.findViewById(R.id.search_img);
		cache.zanView = (TextView) view.findViewById(R.id.search_zan);
		cache.readyView = (TextView) view.findViewById(R.id.search_ready);
		view.setTag(cache);
        return view;
	}
	final static class ContactListItemCache {
        public TextView nameView;
        public TextView addressView;
        public TextView zanView;
        public ImageView imgView;
        public TextView readyView;
        public CharArrayBuffer nameBuffer = new CharArrayBuffer(128);
        public CharArrayBuffer addressBuffer = new CharArrayBuffer(128);
        public CharArrayBuffer readyBuffer = new CharArrayBuffer(128);
        public CharArrayBuffer zanBuffer = new CharArrayBuffer(128);
	}
}
