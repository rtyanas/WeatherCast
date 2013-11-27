package com.yanas.mobileapp.weathercast.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.yanas.mobileapp.weathercast.StationSelected;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CityListDbData {
	
	Context context;

	  // Database fields
	  private SQLiteDatabase database;
	  private CityListDbHelper dbHelper;
	  private String[] allColumns = { 
			  CityListDbHelper.COLUMN_ID,
			  CityListDbHelper.COLUMN_CITY ,
			  CityListDbHelper.COLUMN_STATE ,
			  CityListDbHelper.COLUMN_ZIP_CODE ,
			  CityListDbHelper.COLUMN_LAT ,
			  CityListDbHelper.COLUMN_LON 
	  };

	  public CityListDbData(Context context_in) {
		  context = context_in;
	    dbHelper = new CityListDbHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	    Log.d("BuildingDbData", "database: open? "+ database.isOpen() + database.toString());
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public StationSelected createStation(String city, String state, 
			  String zipCode, String lat, String lon) {
	    ContentValues values = new ContentValues();
	    StationSelected newBuilding = new StationSelected(context, city, state, zipCode);

	    values.put(CityListDbHelper.COLUMN_CITY, city);
	    values.put(CityListDbHelper.COLUMN_STATE, state);
	    values.put(CityListDbHelper.COLUMN_ZIP_CODE, zipCode);
	    values.put(CityListDbHelper.COLUMN_LAT, lat);
	    values.put(CityListDbHelper.COLUMN_LON, lon);
	    long insertId = database.insert(CityListDbHelper.TABLE, null,
	        values);
//	    if(insertId != -1) {
//		    Cursor cursor = database.query(CityListDbHelper.TABLE,
//		        allColumns, CityListDbHelper.COLUMN_ID + " = " + insertId, null,
//		        null, null, null);
//		    cursor.moveToFirst();
//		    newBuilding = cursorToBuilding(cursor);
//		    cursor.close();
//	    }
	    return newBuilding;
	  }

	  public void deleteBuilding(StationSelected comment) {
	    long id = comment.getId();
	    System.out.println("Building deleted with id: " + id);
	    database.delete(CityListDbHelper.TABLE, CityListDbHelper.COLUMN_ID
	        + " = " + id, null);
	  }
	  
		public int initDB(Vector<StationSelected> stations) {
			int numStationsRet=0;
//			Cursor cursor = database.rawQuery("", null);
			long numRows = DatabaseUtils.longForQuery(
					database, "SELECT COUNT(*) FROM "+ CityListDbHelper.TABLE, null);
			if(numRows <= 0) {
				for(StationSelected ss : stations) {
					this.createStation(ss.getCity(), ss.getState(), 
							ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
					numStationsRet++;
				}
			}
			else {
				Log.d("CityListDbData", 
						"records not added to database, existing records: "+ numRows);
			}
			
			return numStationsRet;
		}
		



	  public List<StationSelected> getAllCityZipData() {
	    List<StationSelected> comments = new ArrayList<StationSelected>();

	    Cursor cursor = database.query(CityListDbHelper.TABLE,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	StationSelected comment = cursorToBuilding(cursor);
	      comments.add(comment);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return comments;
	  }

	  public StationSelected getBuilding(int hashCode) {
		    StationSelected building = new StationSelected(context, "", "", "");

		    Cursor cursor = database.query(CityListDbHelper.TABLE,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	building = cursorToBuilding(cursor);
		    	cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    return building;
		  }

	  private StationSelected cursorToBuilding(Cursor cursor) {
		String city, st, zip;  
		StationSelected station = new StationSelected(
				context, cursor.getString(1), cursor.getString(2), cursor.getString(3));

		station.setLatitude(cursor.getString(4));
		station.setLongitude(cursor.getString(5));

		return station;
	  }

	public SQLiteDatabase getDatabase() {
		return database;
	}


	  
}
