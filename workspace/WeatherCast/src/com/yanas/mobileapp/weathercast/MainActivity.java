package com.yanas.mobileapp.weathercast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yanas.mobileapp.restaccess.RestClient;
import com.yanas.mobileapp.restaccess.RestClient.RequestMethod;

import android.os.Bundle;
import android.provider.Contacts.People;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {

	SimpleAdapter simpleAdpt;
	
	public final static String STATION_ID = "com.yanas.mobileapp.weathercast.STATION_ENTERED";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
//        // We'll define a custom screen layout here (the one shown above), but
//        // typically, you could just use the standard ListActivity layout.
//        setContentView(R.layout.custom_list_activity_view);
//
//        // Query for all people contacts using the Contacts.People convenience class.
//        // Put a managed wrapper around the retrieved cursor so we don't have to worry about
//        // requerying or closing it as the activity changes state.
//        Cursor mCursor = this.getContentResolver().query(People.CONTENT_URI, null, null, null, null);
//        startManagingCursor(mCursor);
//
//        // Now create a new list adapter bound to the cursor.
//        // SimpleListAdapter is designed for binding to a Cursor.
//        ListAdapter adapter = new SimpleCursorAdapter(
//                this, // Context.
//                android.R.layout.two_line_list_item,  // Specify the row template to use (here, two columns bound to the two retrieved cursor rows).
//                mCursor,                                              // Pass in the cursor to bind to.
//                new String[] {People.NAME, People.NUMBER},           // Array of cursor columns to bind to.
//                new int[] {android.R.id.text1, android.R.id.text2});  // Parallel array of which template objects to bind to those columns.
//
//        // Bind to our new adapter.
//        setListAdapter(adapter);

        
		 setContentView(R.layout.activity_main);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
	    initList();

	    // We get the ListView component from the layout
	    ListView lv = (ListView) findViewById(R.id.listView);

	    // This is a simple adapter that accepts as parameter
	    // Context
	    // Data list
	    // The row layout that is used during the row creation
	    // The keys used to retrieve the data
	    // The View id used to show the data. The key number and the view id must match
	    simpleAdpt = new SimpleAdapter(this, planetsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});

	    lv.setAdapter(simpleAdpt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}
	
	public void sendMessage(View view) {
		
		Intent intent = new Intent(this, StationListActivity.class);
		
		// Intent intent = new Intent(this, ListViewExampleActivity.class);
		// Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText textMessage = (EditText) findViewById(R.id.station_id);
		String station = textMessage.getText().toString();
		intent.putExtra(STATION_ID, station);
		startActivity(intent);
	}
	
	// The data to show
	List<Map<String, String>> planetsList = new ArrayList<Map<String,String>>();


	   private void initList() {
	    // We populate the planets

	    planetsList.add(createPlanet("planet", "Mercury"));
	    planetsList.add(createPlanet("planet", "Venus"));
	    planetsList.add(createPlanet("planet", "Mars"));
	    planetsList.add(createPlanet("planet", "Jupiter"));
	    planetsList.add(createPlanet("planet", "Saturn"));
	    planetsList.add(createPlanet("planet", "Uranus"));
	    planetsList.add(createPlanet("planet", "Neptune"));
	    planetsList.add(createPlanet("planet", "Neptune"));
	    planetsList.add(createPlanet("planet", "Neptune"));
	    planetsList.add(createPlanet("planet", "Neptune"));
	    planetsList.add(createPlanet("planet", "Neptune"));
	    planetsList.add(createPlanet("planet", "Neptune"));
	    planetsList.add(createPlanet("planet", "Neptune"));
	    planetsList.add(createPlanet("planet", "Neptune"));
	    planetsList.add(createPlanet("planet", "Neptune"));
	    planetsList.add(createPlanet("planet", "Neptune"));
	    planetsList.add(createPlanet("planet", "Neptune"));
	    planetsList.add(createPlanet("planet", "Mars"));
	    planetsList.add(createPlanet("planet", "Jupiter"));
	    planetsList.add(createPlanet("planet", "Earth"));

	}

	private HashMap<String, String> createPlanet(String key, String name) {
	    HashMap<String, String> planet = new HashMap<String, String>();
	    planet.put(key, name);

	    return planet;
	}

}
