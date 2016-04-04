package com.yanas.mobileapp.weathercast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.yanas.mobileapp.weathercast.SettingsWeather.PanelSelectionEnum;
import com.yanas.mobileapp.weathercast.datastore.CityListDbData;
import com.yanas.mobileapp.weathercast.datastore.CityListDbHelper;
import com.yanas.mobileapp.weathercast.datastore.UserSettingsDbData;
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
    public final static String DISPLAY_STYLE_ID = "com.yanas.mobileapp.weathercast.DISPLAY_STYLE";

	public final static String CURRENT_LOCATION = "XX_Current_Location_YY";
	
	public final static String CITY_ARG = "com.yanas.mobileapp.weathercast.CITY";
	public final static String STATE_ARG = "com.yanas.mobileapp.weathercast.STATE";
	public final static String ZIP_ARG = "com.yanas.mobileapp.weathercast.ZIP";
	public final static String LAT_ARG = "com.yanas.mobileapp.weathercast.LAT";
	public final static String LON_ARG = "com.yanas.mobileapp.weathercast.LON";
    public static final String BACKGROUND = "com.yanas.mobileapp.weathercast.LON";

    public static final String ORIENTATION = "com.yanas.mobileapp.weathercast.ORIENTATION";

    static final int new_station_result = 1;

	
	protected Object mActionMode;
	public int selectedItem = -1;
	
	private UserSettingsDbData userSettingsDbData;
	public static SettingsWeather settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_list);

//		setupActionBar();
		
		userSettingsDbData = new UserSettingsDbData(this);
		userSettingsDbData.open();
		settings = new SettingsWeather(userSettingsDbData);
		settings.getSettingsWeather();
		
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
		boolean citySort = true;
		if(citySort)
    		Collections.sort(cityZipList, new Comparator<StationSelected>() {
    		    @Override
    		    public int compare(StationSelected citySt1, StationSelected cityState2) {
    		        return 0;
    		    }
            } );
		else // State sort
            Collections.sort(cityZipList, new Comparator<StationSelected>() {
                @Override
                public int compare(StationSelected citySt1, StationSelected cityState2) {
                    return 0;
                }
            } );
		    
		boolean first = true;
		
		String station = "";
		Integer Int = Integer.valueOf(0);
		for(StationSelected sd : cityZipList) {
			if (first) {
				// Reset to get current location
				sd = new StationSelected(this, CURRENT_LOCATION, "", "");
				first = false;
				
				// If the city is ot found then 
				// do not update the first entry to the current city
				if("".equals( sd.getCity() ) )
				    continue;
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

	/**
	 * Menu item selected
	 */
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

		case R.id.action_days_per_screen_1:
            if(GlobalSettings.main_activity) Log.d("MainActivity", "onOptionsItemSelected, display one day per screen");
            settings.setPanelSelect(PanelSelectionEnum.SINGLE_PANE);
            break;
			
        case R.id.action_days_per_screen_number_4:
            if(GlobalSettings.main_activity) Log.d("MainActivity", "onOptionsItemSelected, display four day per screen");
            settings.setPanelSelect(PanelSelectionEnum.QUAD_PANE_ONLY_NUMBERS);
            break;
            
        case R.id.action_days_per_screen_graphic_4:
            if(GlobalSettings.main_activity) Log.d("MainActivity", "onOptionsItemSelected, display graphic four day per screen");
            settings.setPanelSelect(PanelSelectionEnum.QUAD_PANE_ONLY_GRAPHICS);
            break;
            
        case R.id.action_days_per_screen_min_graphic_4:
            if(GlobalSettings.main_activity) Log.d("MainActivity", "onOptionsItemSelected, display minimum graphic four day per screen");
            settings.setPanelSelect(PanelSelectionEnum.QUAD_PANE_ONLY_MIN_GRAPHICS);
            break;
            
		}
		if(settings.saveSettingsWeather() <= 0) {
		    Log.e("onOptionsItemSelected", "saveSettingsWeather unsuccessful");
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
            if( ("".equals(stationData[ZIPCODE].trim()) ) ) {
                isZipCodeOK = false;                
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
            
            if(GlobalSettings.main_activity) Log.d(MainActivity.class.getName() +":ValidateInternetRunDisplayAsync", 
                                                    "Station: "+ station);
            
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
            
              if (ipAddr == null || "".equals(ipAddr.toString()) ) {
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
            if(MainActivity.settings.getPanelSelect() == null || station_in == null ||  "".equalsIgnoreCase(station_in) ) {
                showValidateAlert(titleError, "Internet is not available.");
            }
            else {
                Intent intent = null;

                switch (MainActivity.settings.getPanelSelect() ) {
                    case SINGLE_PANE :
                        // showValidateAlert("Weather Debug", "Internet is available. Case Single Pane" + station_in);
                        intent = new Intent(MainActivity.this, DisplayWeatherInfoActivity.class);
                        
                        intent.putExtra(LOCATION_ID, station_in);
                        startActivity(intent);                            
                        break;
                        
                    case QUAD_PANE_ONLY_GRAPHICS:
                        // showValidateAlert("Quad Days (Graphics) Feature Coming", "Please be patient.");
                        intent = new Intent(MainActivity.this, DisplayWeatherInfoQuadGraphicsActivity.class);
                        
                        intent.putExtra(LOCATION_ID, station_in);
                        intent.putExtra(DISPLAY_STYLE_ID, MainActivity.settings.getPanelSelect());
                        startActivity(intent);                            
                        
                        break;
                    
                    case QUAD_PANE_ONLY_MIN_GRAPHICS:
                        // showValidateAlert("Quad Days (Graphics) Feature Coming", "Please be patient.");
                        intent = new Intent(MainActivity.this, DisplayWeatherInfoQuadGraphicsActivity.class);
                        
                        intent.putExtra(LOCATION_ID, station_in);
                        intent.putExtra(DISPLAY_STYLE_ID, MainActivity.settings.getPanelSelect());
                        startActivity(intent);                            
                        
                        break;
                    
                    case QUAD_PANE_ONLY_NUMBERS:
                        intent = new Intent(MainActivity.this, DisplayWeatherInfoQuadNumbersActivity.class);
                        
                        intent.putExtra(LOCATION_ID, station_in);
                        startActivity(intent);                            

                        break;

                    default:
                        showValidateAlert(titleError, "Data not available.");
                }
                
            }
        }
        
    } // ValidateInternetRunDisplayAsync
    

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
	        class StationSingle {
	            String city, state, zip;
	            StationSingle(String c, String s, String z) { city = c; state = s; zip = z; }
	        };
	        
	        List<StationSingle> stationList = new ArrayList<StationSingle>();
	        stationList.add(new StationSingle(CURRENT_LOCATION, "", ""));
	        stationList.add(new StationSingle("Akron", "OH", "44305"));
            stationList.add(new StationSingle("Albuquerque", "NM", "87105"));
            stationList.add(new StationSingle("Aspen", "CO", "81611"));
            stationList.add(new StationSingle("Atlanta", "GA", "30310"));
            stationList.add(new StationSingle("Austin", "TX", "78748"));
            stationList.add(new StationSingle("Bakersfield", "CA", "93304"));
            stationList.add(new StationSingle("Bangor", "ME", "04401"));
            stationList.add(new StationSingle("Baton Rouge", "LA", "70808"));
            stationList.add(new StationSingle("Bedminster", "NJ", "07921"));
            stationList.add(new StationSingle("Belleville", "IL", "62221"));
            stationList.add(new StationSingle("Billings", "MT", "58503"));
            stationList.add(new StationSingle("Birmingham", "AL", "35223"));
            stationList.add(new StationSingle("Bismarck", "ND", "58503"));
            stationList.add(new StationSingle("Blacksburg", "VA", "24060"));
            stationList.add(new StationSingle("Boca Raton", "FL", "33486"));
            stationList.add(new StationSingle("Bossier City", "LA", "71111"));
            stationList.add(new StationSingle("Boston", "MA", "02119"));
            stationList.add(new StationSingle("Bozeman", "MT", "59718"));
            stationList.add(new StationSingle("Buffalo", "NY", "14214"));
            stationList.add(new StationSingle("Carson City", "NV", "89703"));
            stationList.add(new StationSingle("Cape Coral", "FL", "33993"));
            stationList.add(new StationSingle("Cleveland", "OH", "44113"));
            stationList.add(new StationSingle("Charleston", "SC", "29412"));
            stationList.add(new StationSingle("Chattanooga", "TN", "37408"));
            stationList.add(new StationSingle("Chicago", "IL", "43209"));
            stationList.add(new StationSingle("Cincinnati", "OH", "45202"));
            stationList.add(new StationSingle("Columbia", "SC", "29205"));
            stationList.add(new StationSingle("Columbus", "OH", "43209"));
            stationList.add(new StationSingle("Concord", "NH", "03301"));
            stationList.add(new StationSingle("Crested Butte", "CO", "81434"));
            stationList.add(new StationSingle("Dallas", "TX", "75241"));
            stationList.add(new StationSingle("Denver", "CO", "80239"));
            stationList.add(new StationSingle("Detroit", "MI", "48201"));
            stationList.add(new StationSingle("Duluth", "MN", "55806"));
            stationList.add(new StationSingle("Flemington", "NJ", "08822"));
            stationList.add(new StationSingle("Fair Haven", "NJ", "07704"));
            stationList.add(new StationSingle("Fort Myers", "FL", "33901"));
            stationList.add(new StationSingle("Fort Wayne", "IN", "46809"));
            stationList.add(new StationSingle("Grand Rapids", "MI", "49503"));
            stationList.add(new StationSingle("Harrisburg", "PA", "17103"));
            stationList.add(new StationSingle("Houston", "TX", "77001"));
            stationList.add(new StationSingle("Huntington", "WV", "77001"));
            stationList.add(new StationSingle("Idaho Falls", "ID", "83401"));
            stationList.add(new StationSingle("Indianapolis", "IN", "46239"));
            stationList.add(new StationSingle("Jackson", "MS", "39212"));
            stationList.add(new StationSingle("Lincoln", "NE", "68516"));
            stationList.add(new StationSingle("Little Rock", "AR", "72202"));
            stationList.add(new StationSingle("Las Vegas", "NV", "89104"));
            stationList.add(new StationSingle("Los Angeles", "CA", "90021"));
            stationList.add(new StationSingle("Lousville", "KY", "40203"));
            stationList.add(new StationSingle("Manchester", "NH", "03103"));
            stationList.add(new StationSingle("Mantoloking", "NJ", "08738"));
            stationList.add(new StationSingle("Memphis", "TN", "38106"));
            stationList.add(new StationSingle("Miami", "FL", "33101"));
            stationList.add(new StationSingle("Milwaukee", "WI", "53221"));
            stationList.add(new StationSingle("Minneapolis", "MN", "55437"));
            stationList.add(new StationSingle("Montgomery", "AL", "36116"));
            stationList.add(new StationSingle("Nashville", "TN", "37218"));
            stationList.add(new StationSingle("Netcong", "NJ", "07857"));
            stationList.add(new StationSingle("New Haven", "CT", "06513"));
            stationList.add(new StationSingle("New Orleans", "LA", "70115"));
            stationList.add(new StationSingle("Newport", "RI", "02840"));
            stationList.add(new StationSingle("New York", "NY", "10018"));
            stationList.add(new StationSingle("Oklahoma City", "OK", "73160"));
            stationList.add(new StationSingle("Orlando", "FL", "32806"));
            stationList.add(new StationSingle("Park City", "UT", "84060"));
            stationList.add(new StationSingle("Pensacola", "FL", "32502"));
            stationList.add(new StationSingle("Perth Amboy", "NJ", "08861"));
            stationList.add(new StationSingle("Philadelphia", "PA", "19107"));
            stationList.add(new StationSingle("Phoenix", "AZ", "85040"));
            stationList.add(new StationSingle("Pittsburgh", "PA", "15122"));
            stationList.add(new StationSingle("Portland", "ME", "04102"));
            stationList.add(new StationSingle("Port Saint Lucie", "FL", "34983"));
            stationList.add(new StationSingle("Portland", "OR", "97229"));
            stationList.add(new StationSingle("Reno", "NV", "89506"));
            stationList.add(new StationSingle("Roanoke", "VA", "21661"));
            stationList.add(new StationSingle("Rock Hall", "MD", "21661"));
            stationList.add(new StationSingle("Rochester", "NY", "14623"));
            stationList.add(new StationSingle("Rockaway", "NJ", "07866"));
            stationList.add(new StationSingle("Rutland", "VT", "05701"));
            stationList.add(new StationSingle("Sacramento", "CA", "95814"));
            stationList.add(new StationSingle("Salt Lake City", "UT", "84112"));
            stationList.add(new StationSingle("San Diego", "CA", "92101"));
            stationList.add(new StationSingle("San Francisco", "CA", "94112"));
            stationList.add(new StationSingle("San Jose", "CA", "95125"));
            stationList.add(new StationSingle("Sandy Hook", "NJ", "07732"));
            stationList.add(new StationSingle("Seattle", "WA", "98148"));
            stationList.add(new StationSingle("Shrewsbury", "NJ", "07702"));
            stationList.add(new StationSingle("Simi", "CA", "93065"));
            stationList.add(new StationSingle("Sioux City", "IA", "51106"));
            stationList.add(new StationSingle("Sioux Falls", "SD", "57104"));
            stationList.add(new StationSingle("Spokane", "WA", "99217"));
            stationList.add(new StationSingle("St Louis", "MO", "63106"));
            stationList.add(new StationSingle("Surprise", "AZ", "85379"));
            stationList.add(new StationSingle("Stowe", "VT", "05672"));
            stationList.add(new StationSingle("Tallahassee", "FL", "32303"));
            stationList.add(new StationSingle("Tampa", "FL", "33602"));
            stationList.add(new StationSingle("Texarkana", "TX", "75501"));
            stationList.add(new StationSingle("Toms River", "NJ", "08722"));
            stationList.add(new StationSingle("Tucson", "AZ", "85713"));
            stationList.add(new StationSingle("Virginia Beach", "VA", "23462"));
            stationList.add(new StationSingle("Whitefish", "MT", "59937"));
            stationList.add(new StationSingle("Wilmington", "DE", "19803"));
            stationList.add(new StationSingle("Washington", "DC", "20001"));
            stationList.add(new StationSingle("Watertown", "SD", "57201"));
            stationList.add(new StationSingle("Wichita", "KS", "67217"));
            stationList.add(new StationSingle("Yellowstone National Park", "WY", "82190"));
	        
	        for(StationSingle sist : stationList) 
	        {
	            cityZipDbData.createStation(sist.city, sist.state, sist.zip, "", "");            
	        }
	        
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
