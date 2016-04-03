package com.zsk.androtweet.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper
  extends SQLiteOpenHelper
{
  public DBHelper(Context paramContext)
  {
    super(paramContext, "AndroTweet", null, 7);
  }
  
  public void FreshDB(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS TIMELINE");
    onCreate(paramSQLiteDatabase);
  }
  
  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("CREATE TABLE TIMELINE (_id INTEGER PRIMARY KEY AUTOINCREMENT , tweet_id LONG, reply_id LONG, tweet TEXT, tweettime INTEGER, RT LONG, FAV LONG );");
  }
  
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    FreshDB(paramSQLiteDatabase);
  }
}


/* Location:              /Users/kaloglu/Desktop/androtweet/dex2jar-2.0/classes-dex2jar.jar!/com/zsk/androtweet/Database/DBHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */