package com.yanas.mobileapp.weathercast.datastore;

import com.yanas.mobileapp.weathercast.GlobalSettings;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CityListDbHelper  extends SQLiteOpenHelper {

	  public static final String TABLE = "buildings";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_CITY = "city";
	  public static final String COLUMN_STATE = "state";
	  public static final String COLUMN_ZIP_CODE = "zip_code";
	  public static final String COLUMN_LAT = "lat";
	  public static final String COLUMN_LON = "lon";

	  private static final String DATABASE_NAME = "weathercast.db";
	  public static final int DATABASE_VERSION = 4;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE + "(" + COLUMN_ID + " integer primary key autoincrement, " + 
			              COLUMN_CITY + " text not null, " +
			              COLUMN_STATE + " text not null, " +
			              COLUMN_ZIP_CODE + " text, " +
			              COLUMN_LAT + " text, " +
			              COLUMN_LON + " text " +
	      ");";

	  public CityListDbHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	      if(GlobalSettings.city_list_db_data) Log.d(CityListDbHelper.class.getName(), "Create DB and Table: "+ DATABASE_CREATE);
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	      if(GlobalSettings.city_list_db_data) Log.w(CityListDbHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE);
	    onCreate(db);
	  }

	} 
