package com.hr.library_1_0.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class db_Useful {
	private db_Create helper;

	public db_Useful(Context context) {
		helper = new db_Create(context);
	}

	public Long addData(String title, String suoshu, String isbn, String push) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("suoshu", suoshu);
		values.put("isbn", isbn);
		values.put("push", push);
		Long addId = db.insert("collection", null, values);
		db.close();
		return addId;
	}

	public int deleteData(String title) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int number = db.delete("collection", "title=?", new String[] { title });
		db.close();
		return number;
	}

	public List<db_Info> findAllData() {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("collection", new String[] {"id" ,"title",
				"suoshu", "isbn", "push" }, null, null, null, null, null);
		List<db_Info> infos = new ArrayList<db_Info>();
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String suoshu = cursor.getString(cursor.getColumnIndex("suoshu"));
			String isbn = cursor.getString(cursor.getColumnIndex("isbn"));
			String push = cursor.getString(cursor.getColumnIndex("push"));
			db_Info info = new db_Info(id, title, suoshu, isbn, push);
			infos.add(info);
		}
		cursor.close();
		db.close();
		return infos;
	}
	
	public boolean findData(String title){
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("collection", null, "title=?", new String[]{title}, null, null, null);
		boolean result = cursor.moveToNext();
		cursor.close();
		db.close();
		return result;
	}

}
