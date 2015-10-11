package com.zsk.androtweet.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.zsk.androtweet.Database.DB.*;

public class DBHelper extends SQLiteOpenHelper {
	public DBHelper(Context context) {
		super(context, DBNAME,null, DBVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase DB) {
		DB.execSQL(TIMELINE_CREATE);
//		DB.execSQL(USERS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
		FreshDB(DB);
	}

	public void FreshDB(SQLiteDatabase DB) {
		DB.execSQL(TIMELINE_DROP);
//		DB.execSQL(USERS_DROP);

		onCreate(DB);
	}

}
