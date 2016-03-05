package com.yanas.mobileapp.weathercast.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.yanas.mobileapp.weathercast.GlobalSettings;
import com.yanas.mobileapp.weathercast.SettingsWeather;
import com.yanas.mobileapp.weathercast.StationSelected;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserSettingsDbData {
	
	Context context;

	  // Database fields
	  private SQLiteDatabase database;
	  private UserSettingsDbHelper dbHelper;
	  private String[] allColumns = { 
			  UserSettingsDbHelper.COLUMN_ID,
			  UserSettingsDbHelper.COLUMN_DISPLAY_TYPE,
			  UserSettingsDbHelper.COLUMN_BACKGROUND_COLOR,
			  UserSettingsDbHelper.COLUMN_CITY_LIST_SORT
	  };

	  public UserSettingsDbData(Context context_in) {
		  context = context_in;
	    dbHelper = new UserSettingsDbHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	    if(GlobalSettings.city_list_db_data) Log.d("BuildingDbData.open()", "database: open? "+ database.isOpen() + database.toString());
	  }

	  public void close() {
	    dbHelper.close();
	  }

      public long updateSetting(SettingsWeather settingsW) {
          
          ContentValues values = new ContentValues();
          
          SettingsWeather swFirst = getSavedSettings(); // gets first record.  Update this using the ID

          values.put(UserSettingsDbHelper.COLUMN_BACKGROUND_COLOR, settingsW.getBackgroundColor());
          values.put(UserSettingsDbHelper.COLUMN_CITY_LIST_SORT, settingsW.getCityStateListSort());
          values.put(UserSettingsDbHelper.COLUMN_DISPLAY_TYPE, settingsW.getPanelSelectInt());
          long insertId = database.update(UserSettingsDbHelper.TABLE, values, 
                  UserSettingsDbHelper.COLUMN_ID +" = "+ swFirst.getID(), null);

          if(GlobalSettings.city_list_db_data) Log.d(UserSettingsDbData.class.getName() +"updateSetting()", 
                  "insertId: "+ insertId );      
          return insertId;
      }
      
      
	  public long insertSetting(SettingsWeather settingsW) {
	    ContentValues values = new ContentValues();

	    values.put(UserSettingsDbHelper.COLUMN_BACKGROUND_COLOR, settingsW.getBackgroundColor());
	    values.put(UserSettingsDbHelper.COLUMN_CITY_LIST_SORT, settingsW.getCityStateListSort());
	    values.put(UserSettingsDbHelper.COLUMN_DISPLAY_TYPE, settingsW.getPanelSelectInt());
	    long insertId = database.insert(UserSettingsDbHelper.TABLE, null,
	        values);
//	    if(insertId != -1) {
//		    Cursor cursor = database.query(CityListDbHelper.TABLE,
//		        allColumns, CityListDbHelper.COLUMN_ID + " = " + insertId, null,
//		        null, null, null);
//		    cursor.moveToFirst();
//		    newBuilding = cursorToBuilding(cursor);
//		    cursor.close();
//	    }
        if(GlobalSettings.city_list_db_data) Log.d(UserSettingsDbData.class.getName() +"insertSetting()", 
                "insertId: "+ insertId );      
        
	    return insertId;
	  }

	  public void deleteStation(StationSelected comment) {
	    long id = comment.getId();
	    if(GlobalSettings.city_list_db_data) Log.d("CityListDbData.deleteStation()", "delete Station with id: " + id);
	    database.delete(CityListDbHelper.TABLE, CityListDbHelper.COLUMN_ID
	        + " = " + id, null);
	  }
	  
		public int initDB(Vector<StationSelected> stations) {
			int numStationsRet=0;
//			Cursor cursor = database.rawQuery("", null);
			long numRows = DatabaseUtils.longForQuery(
					database, "SELECT COUNT(*) FROM "+ CityListDbHelper.TABLE, null);
//			if(numRows <= 0) {
//				for(StationSelected ss : stations) {
//					this.createStation(ss.getCity(), ss.getState(), 
//							ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
//					numStationsRet++;
//				}
//			}
//			else {
//				if(GlobalSettings.city_list_db_data) Log.d("CityListDbData", 
//						"records not added to database, existing records: "+ numRows);
//			}
			
			return numStationsRet;
		}
		


		/**
		 * 
		 * @return First record
		 */

      public SettingsWeather getSavedSettings() {
            SettingsWeather setting = new SettingsWeather();

            Cursor cursor = database.query(UserSettingsDbHelper.TABLE,
                allColumns, null, null, null, null, UserSettingsDbHelper.COLUMN_ID);
            
            int numRecs=0;
            cursor.moveToFirst();
            if (! cursor.isAfterLast()) {
                // Get first record
                setting = cursorToSettingsWeather(cursor);
                numRecs++;
            }
            setting.setCountDbReturn(numRecs);

            // make sure to close the cursor
            cursor.close();
            return setting;
      }
      

	  private SettingsWeather cursorToSettingsWeather(Cursor cursor) {
		String city, st, zip;  
		SettingsWeather settingW = new SettingsWeather();

        settingW.setID(  cursor.getInt(0) );
		settingW.setPanelSelect(  cursor.getInt(1) );
		
		if(GlobalSettings.city_list_db_data) Log.d(UserSettingsDbData.class.getName() +"cursorToSettingsWeather()", 
                "cursor.getInt(0): "+ cursor.getInt(0) +", cursor.getInt(1): "+ cursor.getInt(1));		
		
		return settingW;
	  }

	public SQLiteDatabase getDatabase() {
		return database;
	}


	  
}
