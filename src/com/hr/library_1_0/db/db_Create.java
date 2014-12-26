package com.hr.library_1_0.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class db_Create extends SQLiteOpenHelper {

	public db_Create(Context context) {
		super(context, "collcetion.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table collection (id integer primary key autoincrement,title varchar(255),suoshu varcha(64),isbn varcha(64),push varcha(255))");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	
	public void addData(){
		
	}

}
