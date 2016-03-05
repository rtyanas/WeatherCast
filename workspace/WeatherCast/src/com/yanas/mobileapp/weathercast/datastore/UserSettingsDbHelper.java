package com.yanas.mobileapp.weathercast.datastore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserSettingsDbHelper  extends SQLiteOpenHelper {

	  public static final String TABLE = "UserSettings";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_DISPLAY_TYPE = "display_type";
      public static final String COLUMN_BACKGROUND_COLOR = "backround_color";
      public static final String COLUMN_CITY_LIST_SORT = "city_list_sort";

	  private static final String DATABASE_NAME = "weathercast.db";

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE + "(" + COLUMN_ID + " integer primary key autoincrement, " + 
	                      COLUMN_DISPLAY_TYPE + " integer, " +
	                      COLUMN_BACKGROUND_COLOR + " integer, " +
	                      COLUMN_CITY_LIST_SORT + " integer " +
	      ");";

	  public UserSettingsDbHelper(Context context) {
	    super(context, DATABASE_NAME, null, CityListDbHelper.DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
        Log.d(UserSettingsDbHelper.class.getName(), "Create DB and Table: "+ DATABASE_CREATE);
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(UserSettingsDbHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE);
	    onCreate(db);
	  }

	} 
