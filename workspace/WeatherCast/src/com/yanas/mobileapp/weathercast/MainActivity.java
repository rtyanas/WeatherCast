package com.yanas.mobileapp.weathercast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.yanas.mobileapp.weathercast.datastore.CityListDbData;
import com.yanas.mobileapp.weathercast.datastore.CityListDbHelper;
import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed;
import com.yanas.utils.StringUtils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity         
/* implements LoaderManager.LoaderCallbacks<Cursor> */ {

    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;

    CityListDbData cityZipDbData;
    List<StationSelected> cityZipList;
    HashMap<Integer, String> selecedList = new HashMap<Integer, String>();
    
	public final static String LOCATION_ID = "com.yanas.mobileapp.weathercast.CURRENT_LOCATION";

	public final static String CURRENT_LOCATION = "XX_Current_Location_YY";
	
	public final static String CITY_ARG = "com.yanas.mobileapp.weathercast.CITY";
	public final static String STATE_ARG = "com.yanas.mobileapp.weathercast.STATE";
	public final static String ZIP_ARG = "com.yanas.mobileapp.weathercast.ZIP";
	public final static String LAT_ARG = "com.yanas.mobileapp.weathercast.LAT";
	public final static String LON_ARG = "com.yanas.mobileapp.weathercast.LON";

	static final int new_station_result = 1;
	
	protected Object mActionMode;
	public int selectedItem = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_list);

//		setupActionBar();
		
		cityZipDbData = new CityListDbData(this);
		cityZipDbData.open();
		// long numRecsAdded = cityZipDbData.initDB(stations);
		// Log.d("MainActivity", "numRecsAdded: "+ numRecsAdded);

		initStationData();
		
		populateStationList();

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
        	@Override
        	public boolean onItemLongClick(AdapterView<?> parent, View view, 
        			int position, long id) {
        		
        		if(mActionMode != null) {
        			return false;
        		}        
        		
        		selectedItem = position;
        	
        		mActionMode = MainActivity.this.startActionMode(mActionModeCallback);
        		view.setSelected(true);
        		return true;
        	}


        });
        
	}
	
	
	private boolean populateStationList() {
		
		ArrayList<String> stationsAL = new ArrayList<String>();
		
		cityZipList = cityZipDbData.getAllCityZipData();
		
		boolean first = true;
		
		String station = "";
		Integer Int = Integer.valueOf(0);
		for(StationSelected sd : cityZipList) {
			if (first) {
				// Reset to get current location
				sd = new StationSelected(this, CURRENT_LOCATION, "", "");
				first = false;
			}
			
			station = StringUtils.createStationRow(
			        sd.getCity(), sd.getState(), sd.getZipCode());  
			stationsAL.add(station);

			station = sd.getCity() +", "+ sd.getState() +", "+ sd.getZipCode();
			selecedList.put(Int, station);

			Int++;
		}
		
        setListAdapter(new StableArrayAdapter(this, 
        		android.R.layout.simple_list_item_1, stationsAL) );
        

        return true;
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.station_list, menu);
//		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			if(GlobalSettings.main_activity) Log.d("MainActivity", "onOptionsItemSelected, settings menu option selected");
			Intent intent = new Intent(this, NewStationActivity.class);
			startActivityForResult(intent, new_station_result);

			break;

		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode) {
		case new_station_result:
			if(resultCode == RESULT_OK) {
				if(GlobalSettings.main_activity) Log.d("MainActivity", "onActivityResult, "+ 
						data.getStringExtra(CITY_ARG) +","+
						data.getStringExtra(STATE_ARG) +","+
						data.getStringExtra(ZIP_ARG) +","+
						data.getStringExtra(LAT_ARG) +","+
						data.getStringExtra(LON_ARG)
						);
				StationSelected ss = new StationSelected(this, 
						data.getStringExtra(CITY_ARG), data.getStringExtra(STATE_ARG), data.getStringExtra(ZIP_ARG));
				cityZipDbData.createStation(ss.getCity(), ss.getState(), 
						ss.getZipCode(), ss.getLatitude(), ss.getLongitude());

				populateStationList();
				
			}
			else
				if(GlobalSettings.main_activity) Log.d("MainActivity", "onActivityResult, result code - bad ");
			break;
		}
		
	}
	
	String titleError = "Apologies, cannot get weather";
	
    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        final String item = (String) this.selecedList.get(Integer.valueOf(position));
        Log.d("OnClick", "item: "+ item);
        
        String error = "Error detected.";
        boolean isZipCodeOK = true;
        String stationData[] = item.split(",");
        int ZIPCODE = 2;
        if(stationData.length >= 3) {
            boolean isZip = true, isInternet = true;
            if( (isZip = "".equals(stationData[ZIPCODE]) ) ) {
                isZipCodeOK = false;
                
                if( ! isZip)
                    error = "Sorry, zip code is required.";
                
                Log.e("MainActivity", "Zipcode invalid");
                
            }
        }
        else if(stationData.length < 3) {
            isZipCodeOK = false;
            error = "Please enter correct city, state and zip code.  Zip Code is currently required";
        }
        
        if(isZipCodeOK) {
            new ValidateInternetRunDisplayAsync().execute(item);
        }
        else {
            showValidateAlert(titleError, error);
        }
    }

    
    /**
     * Show Alert dialog passing in Title and message.  This alert has one 
     * OK button for dismissing.
     * @param title
     * @param error
     */
    private void showValidateAlert(String title, String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error) .setTitle(title);
        builder
            .setCancelable(true)
            .setNeutralButton(R.string.ok,  new DialogInterface.OnClickListener() {          
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
        });
        AlertDialog alDialog = builder.create();
        alDialog.show();
    }
    
    
    /**
     * Run thread to get weather data
     * @author RT
     *
     */
    public class ValidateInternetRunDisplayAsync extends AsyncTask<String, Integer, String> {
        
//        private ProgressDialog progressD;
        
        public ValidateInternetRunDisplayAsync() {
//            progressD = new ProgressDialog(MainActivity.this);            
        }
        
        protected void onPreExecute() {
//            this.progressD.setTitle("Retrieving weather");
//            this.progressD.setMessage(
//                    (DisplayWeatherInfoActivity.this.location.split(",").length > 1 ? 
//                            DisplayWeatherInfoActivity.this.location.split(",")[0] : "This land") +
//                    " is a pleasant place.");
//            
//            if( ! this.progressD.isShowing())
//                this.progressD.show();
        }
        
        protected String doInBackground(String... station_in )
        {
            String station=null;
            
            if(station_in.length > 0)
                station = station_in[0];
            
            ConnectivityManager cm =
                      (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
              
            
            if (netInfo == null ) {
                return "";
            }

//            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
//                  return station + "["+ netInfo.toString() +"]";
//            }

            try {
              InetAddress ipAddr = InetAddress.getByName("yahoo.com"); 
            
              if ("".equals(ipAddr.toString()) ) {
                  return "";
              } else {
                  return station +",<"+ ipAddr.toString() +">"+ ipAddr.isReachable(500);
              }
            } catch (Exception e) {
                return "";
            } 
        }
        
        protected void onPostExecute(String station_in)
        {
//            if(progressD.isShowing()) {
//                progressD.dismiss();
//            }
            if("".equalsIgnoreCase(station_in) ) {
                showValidateAlert(titleError, "Internet is not available.");
            }
            else {
                // showValidateAlert("Weather Debug", "Internet is available. " + station_in);
                Intent intent = new Intent(MainActivity.this, DisplayWeatherInfoActivity.class);
                intent.putExtra(LOCATION_ID, station_in);
                startActivity(intent);                            
            }
        }
        
    }
    

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	
	private void initStationData() {
		

		long numRows = DatabaseUtils.longForQuery(
				cityZipDbData.getDatabase(), "SELECT COUNT(*) FROM "+ CityListDbHelper.TABLE, null);
		
		Log.d("MainActivity", "initStationData num Recs in DB: "+ numRows);

		if(numRows <= 0) {
			StationSelected ss = new StationSelected(this, CURRENT_LOCATION, "", "");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Bedminster", "NJ", "07921");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Detroit", "MI", "48201");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Flemington", "NJ", "08822");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Fair Haven", "NJ", "07704");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Houston", "TX", "77001");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Mantoloking", "NJ", "08738");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Miami", "FL", "33101");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Netcong", "NJ", "07857");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "New York", "NY", "10018");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Perth Amboy", "NJ", "08861");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Pittsburgh", "PA", "15122");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Portland", "OR", "97086");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Rock Hall", "MD", "21661");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Rockaway", "NJ", "07866");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "San Diego", "CA", "92101");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Sandy Hook", "NJ", "07732");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Shrewsbury", "NJ", "07702");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());
			ss = new StationSelected(this, "Toms River", "NJ", "08722");
			cityZipDbData.createStation(ss.getCity(), ss.getState(), 
					ss.getZipCode(), ss.getLatitude(), ss.getLongitude());

			Log.d("MainActivity", "initStationData num New Recs in DB: "+ numRows);
		}


		
	}

	
	/**
	 * Create the Array
	 * @author RT
	 *
	 */
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
            List<String> objects) {
          super(context, textViewResourceId, objects);
          for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
          }
        }

        @Override
        public long getItemId(int position) {
          String item = getItem(position);
          return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
          return true;
        }
    }
    
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // called when the action mode is created; startActionMode() was called
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
          // Inflate a menu resource providing context menu items
          MenuInflater inflater = mode.getMenuInflater();
          // assumes that you have "contexual.xml" menu resources
          inflater.inflate(R.menu.rowselection, menu);
          return true;
        }

        // the following method is called each time 
        // the action mode is shown. Always called after
        // onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
          return false; // Return false if nothing is done
        }

        // called when the user selects a contextual menu item
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
          switch (item.getItemId()) {
          case R.id.delete:
        	  removeStation();
            // the Action was executed, close the CAB
            mode.finish();
            return true;
          default:
            return false;
          }
        }

        // called when the user exits the action mode
        public void onDestroyActionMode(ActionMode mode) {
          mActionMode = null;
          selectedItem = -1;
        }
      };

      private void removeStation() {
    	  if(GlobalSettings.main_activity) Log.d("MainActivity", "show toast");
    	  
    	  Toast.makeText(MainActivity.this,
    	        String.valueOf(selectedItem) + 
    	        cityZipList.get(selectedItem).getId() +","+ 
    	        cityZipList.get(selectedItem).getCity() +","+ 
    	        cityZipList.get(selectedItem).getState() +","+
    	        cityZipList.get(selectedItem).getZipCode(),
    	        Toast.LENGTH_LONG).show();
    	  
    	  cityZipDbData.deleteStation(cityZipList.get(selectedItem));
    	  
    	  populateStationList();

      }

      
}
