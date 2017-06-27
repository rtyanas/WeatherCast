package com.yanas.mobileapp.weathercast.datastore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yanas.mobileapp.weathercast.GlobalSettings;

public class CityListDbHelper  extends SQLiteOpenHelper {

	  public static final String TABLE = "cities";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_CITY = "city";
	  public static final String COLUMN_STATE = "state";
	  public static final String COLUMN_ZIP_CODE = "zip_code";
	  public static final String COLUMN_LAT = "lat";
	  public static final String COLUMN_LON = "lon";

      public static final String USER_SETTINGS_TABLE = "UserSettings";
      public static final String US_COLUMN_ID = "_id";
      public static final String US_COLUMN_DISPLAY_TYPE = "display_type";
      public static final String US_COLUMN_BACKGROUND_COLOR = "backround_color";
      public static final String US_COLUMN_CITY_LIST_SORT = "city_list_sort";

      // Database creation sql statement
      private static final String US_DATABASE_CREATE = "create table "
          + USER_SETTINGS_TABLE + "(" + US_COLUMN_ID + " integer primary key autoincrement, " + 
          US_COLUMN_DISPLAY_TYPE + " integer, " +
          US_COLUMN_BACKGROUND_COLOR + " integer, " +
          US_COLUMN_CITY_LIST_SORT + " integer " +
          ");";

	  private static final String DATABASE_NAME = "weathercast.db";
	  public static final int DATABASE_VERSION = 14;

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
	      // if(GlobalSettings.city_list_db_data) { Log.d(CityListDbHelper.class.getName(), "Create DB and Table: "+ DATABASE_CREATE); }
	      Log.d(CityListDbHelper.class.getName(), "Create DB and Table: "+ DATABASE_CREATE+ ", "+ US_DATABASE_CREATE);
        database.execSQL(US_DATABASE_CREATE);
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	      if(GlobalSettings.city_list_db_data) Log.w(CityListDbHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_SETTINGS_TABLE);
	    onCreate(db);
	  }

	} 
