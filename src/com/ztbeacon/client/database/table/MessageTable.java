package com.ztbeacon.client.database.table;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ztbeacon.client.database.DatabaseHelper;
import com.ztbeacon.client.database.DatabaseHelper.TableCreateInterface;


public class MessageTable implements TableCreateInterface{
	// 定义表名
	public static String tableName = "message";
	// 定义各字段名
	public static String _id = "_id"; // _id是SQLite中自动生成的主键，用语标识唯一的记录，为了方便使用，此处定义对应字段名
	public static String id = "id";  // 商家Id
	public static String name = "name"; // 商家名称
	public static String address = "address"; // 商家地址
	public static String telephone = "telephone"; // 商家电话
	public static String description = "description"; // 简单描述
	public static String category = "category"; // 行业类别
	public static String url = "url"; // 商家主页
	public static String detail = "detail"; // 详细信息
	public static String position = "position";  //所在地址
	public static String photo = "photo"; // 图片 
	public static String zan = "zan" ; //点赞次数
	public static String ready = "ready" ;  //阅读次数
	public static String istop = "istop" ;  //是否置顶
	
	// 返回表的实例进行创建与更新
	private static MessageTable msgtable = new MessageTable();
	
	public static MessageTable getInstance(){
		return MessageTable.msgtable;
	}
	//建立数据表
	
	public void onCreate(SQLiteDatabase db) {
		
		String sql = "CREATE TABLE "
				+ MessageTable.tableName
				+ " (  "
				+ "_id integer primary key autoincrement, "				
				+ MessageTable.id + " TEXT, "
				+ MessageTable.name + " TEXT, "
				+ MessageTable.address + " TEXT, "
				+ MessageTable.telephone + " TEXT, "
				+ MessageTable.description + " TEXT, "
				+ MessageTable.category + " TEXT, "
				+ MessageTable.url + " TEXT, "
				+ MessageTable.detail + " TEXT, "
				+ MessageTable.position + " TEXT, "
				+ MessageTable.zan + " LONG, "
				+ MessageTable.ready + " LONG, "		
				+ MessageTable.istop + " LONG default 0"
				+ ");";
		db.execSQL( sql );
		System.out.println("创建数据库表");
	}

	// 更新数据表
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if ( oldVersion < newVersion ) {
			String sql = "DROP TABLE IF EXISTS " + MessageTable.tableName;
			db.execSQL( sql );
			this.onCreate( db );
		}
	}
	// 插入消息
	public synchronized static void insertMessageTable( DatabaseHelper dbHelper, ContentValues userValues ) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.insert( MessageTable.tableName, null, userValues );
		db.close();
	}
	// 更新istop最大值
	public synchronized static void updateMessageTable( DatabaseHelper dbHelper, HashMap<String, Object> newMap) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues newValues = new ContentValues();
		newValues.put("istop", newMap.get("istop").toString());
		db.update(MessageTable.tableName, newValues, MessageTable.id+"=?", new String[] { newMap.get("id").toString() });
		db.close();
	}	
	// 删除一条消息
	public synchronized static void deleteMessageTable( DatabaseHelper dbHelper, String id ) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(  MessageTable.tableName, MessageTable.id + "=?",new String[] { id }  );
		db.close();
	}
	// 删除所有消息
	public synchronized static void deleteAllMessageTable( DatabaseHelper dbHelper ) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		db.delete(  MessageTable.tableName, null, null  );
		db.close();
	}
	// 以HashMap<String, Object>键值对的形式获取一条消息
    public synchronized static HashMap<String, Object> getMessageTable( DatabaseHelper dbHelper, int _id ){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		HashMap<String, Object> msgMap = new HashMap<String, Object>();
		// 此处要求查询MessageTable._id为传入参数_id的对应记录，使游标指向此记录
		Cursor cursor = db.query( MessageTable.tableName, null, MessageTable._id + " =? ", new String[]{ _id + "" }, null, null, null);
		cursor.moveToFirst();
		msgMap.put(MessageTable._id, cursor.getLong(cursor.getColumnIndex(MessageTable._id)));
		msgMap.put(MessageTable.id, cursor.getString(cursor.getColumnIndex(MessageTable.id)));
		msgMap.put(MessageTable.address, cursor.getInt(cursor.getColumnIndex(MessageTable.address)));
		msgMap.put(MessageTable.name, cursor.getString(cursor.getColumnIndex(MessageTable.name)));
		msgMap.put(MessageTable.photo, cursor.getString(cursor.getColumnIndex(MessageTable.photo)));
		msgMap.put(MessageTable.telephone, cursor.getInt(cursor.getColumnIndex(MessageTable.telephone)));
		msgMap.put(MessageTable.description, cursor.getString(cursor.getColumnIndex(MessageTable.description)));
		msgMap.put(MessageTable.detail, cursor.getString(cursor.getColumnIndex(MessageTable.detail)));
		msgMap.put(MessageTable.url, cursor.getString(cursor.getColumnIndex(MessageTable.url)));
		msgMap.put(MessageTable.position, cursor.getString(cursor.getColumnIndex(MessageTable.position)));
		msgMap.put(MessageTable.istop, cursor.getInt(cursor.getColumnIndex(MessageTable.istop)));
		cursor.close();
		db.close();
		return msgMap;
	}
    // 获得查询指向消息表的游标，要求以时间倒序排列
    public synchronized static Cursor getAllMessageTables(DatabaseHelper dbHelper) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		// 此处MessageTable.time + " DESC "作用是要求游标以时间倒序进行查询
    	Cursor cursor = db.query(MessageTable.tableName, null, null, null, null, null, MessageTable._id + " DESC ");
    	cursor.moveToFirst();
    	db.close();
		return cursor;
    }
    
    // 返回消息总数
    public synchronized static int getCount(DatabaseHelper dbHelper) {
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	Cursor cursor = db.query(MessageTable.tableName, null, null, null, null, null, MessageTable._id + " DESC ");
    	int count = cursor.getCount();
    	cursor.close();
    	db.close();
		return count;
    }
    
    // 获得置顶消息的总数
	public synchronized static int getTopCount(DatabaseHelper dbHelper) {
		int count = 0;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(MessageTable.tableName, null, "istop>?",
				new String[] { "0" }, null, null, null);
		count = cursor.getCount();
		cursor.close();
		db.close();
		System.out.println("置顶消息总数为:"+count);
		return count;
	}
    // 返回最大置顶的值
    public synchronized static int getTopNum(DatabaseHelper dbHelper) {
    	int topNum = 0;
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	String strSql = "select max(istop) AS topNum from message";
    	Cursor cursor = db.rawQuery(strSql, null);
    	cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			topNum = cursor.getInt(cursor.getColumnIndex("topNum"));
		}
		System.out.println("最大置顶数值:"+topNum);
    	cursor.close();
    	db.close();
		return topNum;
    }
   // 以ArrayList<HashMap<String, Object>>队列的形式获得所有消息的信息
   public synchronized static ArrayList<HashMap<String, Object>> getListMessageTable( DatabaseHelper dbHelper ){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		ArrayList<HashMap<String, Object>> msgList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> msg = null;
		Cursor cursor = db.rawQuery("select * from (select * from message order by _id desc) order by istop desc", null);
		//Cursor cursor = db.query( MessageTable.tableName, null, null, null, null, null, MessageTable._id+","+ MessageTable.istop + " DESC ");
		for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() ){
			msg = new HashMap<String, Object>();
			msg.put(MessageTable._id, cursor.getInt(cursor.getColumnIndex(MessageTable._id)));
			msg.put(MessageTable.id, cursor.getString(cursor.getColumnIndex(MessageTable.id)));
			msg.put(MessageTable.address, cursor.getString(cursor.getColumnIndex(MessageTable.address)));
			msg.put(MessageTable.name, cursor.getString(cursor.getColumnIndex(MessageTable.name)));
			msg.put(MessageTable.telephone, cursor.getString(cursor.getColumnIndex(MessageTable.telephone)));
			msg.put(MessageTable.description, cursor.getString(cursor.getColumnIndex(MessageTable.description)));
			msg.put(MessageTable.detail, cursor.getString(cursor.getColumnIndex(MessageTable.detail)));
			msg.put(MessageTable.url, cursor.getString(cursor.getColumnIndex(MessageTable.url)));
			msg.put(MessageTable.position, cursor.getString(cursor.getColumnIndex(MessageTable.position)));
			msg.put(MessageTable.ready, cursor.getString(cursor.getColumnIndex(MessageTable.ready)));
			msg.put(MessageTable.zan, cursor.getString(cursor.getColumnIndex(MessageTable.zan)));
			msg.put(MessageTable.istop, cursor.getInt(cursor.getColumnIndex(MessageTable.istop)));
			msgList.add(msg);
		}
		cursor.close();
		db.close();
		return msgList;
	}
   
}
