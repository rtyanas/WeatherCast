package com.yanas.mobileapp.weathercast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity         
/* implements LoaderManager.LoaderCallbacks<Cursor> */ {

    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;

	public final static String LOCATION_ID = "com.yanas.mobileapp.weathercast.CURRENT_LOCATION";

	public final static String CURRENT_LOCATION = "XX_Current_Location_YY";
	
    Vector<StationSelected> stations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_list);

		setupActionBar();
		initStationData(this);

		ArrayList<String> stationsAL = new ArrayList<String>();
		
		for(StationSelected sd : stations) {
			stationsAL.add(sd.getCity() +", "+ sd.getState() +", "+ sd.getZipCode() );
		}
		
        setListAdapter(new StableArrayAdapter(this, 
        		android.R.layout.simple_list_item_1, stationsAL) );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.station_list, menu);
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}
	
    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        final String item = (String) l.getItemAtPosition(position);
        Log.d("OnClick", "item: "+ item);
        
		Intent intent = new Intent(this, DisplayWeatherInfoActivity.class);
		intent.putExtra(LOCATION_ID, stations.get(position).getStationData());
		startActivity(intent);
    }

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	private void initStationData(MainActivity this_in) {
		stations = new Vector<StationSelected>();
		stations.add(new StationSelected(this_in, CURRENT_LOCATION, "", ""));
		stations.add(new StationSelected(this_in, "Bedminster", "NJ", "07921"));
		stations.add(new StationSelected(this_in, "Detroit", "MI", "48201"));
		stations.add(new StationSelected(this_in, "Flemington", "NJ", "08822"));
		stations.add(new StationSelected(this_in, "Fair Haven", "NJ", "07704"));
		stations.add(new StationSelected(this_in, "Houston", "TX", "77001"));
		stations.add(new StationSelected(this_in, "Mantoloking", "NJ", "08738"));
		stations.add(new StationSelected(this_in, "Miami", "FL", "33101"));
		stations.add(new StationSelected(this_in, "Netcong", "NJ", "07857"));
		stations.add(new StationSelected(this_in, "New York", "NY", "10018"));
		stations.add(new StationSelected(this_in, "Perth Amboy", "NJ", "08861"));
		stations.add(new StationSelected(this_in, "Pittsburgh", "PA", "15122"));
		stations.add(new StationSelected(this_in, "Portland", "OR", "97086"));
		stations.add(new StationSelected(this_in, "Rock Hall", "MD", "21661"));
		stations.add(new StationSelected(this_in, "Rockaway", "NJ", "07866"));
		stations.add(new StationSelected(this_in, "San Diego", "CA", "92101"));
		stations.add(new StationSelected(this_in, "Sandy Hook", "NJ", "07732"));
		stations.add(new StationSelected(this_in, "Shrewsbury", "NJ", "07702"));
		stations.add(new StationSelected(this_in, "Toms River", "NJ", "08722"));
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
    

}
