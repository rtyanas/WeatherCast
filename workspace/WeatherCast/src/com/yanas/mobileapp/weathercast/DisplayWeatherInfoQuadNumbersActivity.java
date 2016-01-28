package com.yanas.mobileapp.weathercast;



import java.util.ArrayList;
import java.util.List;

import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed;
import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed.DisplayData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
// 
public class DisplayWeatherInfoQuadNumbersActivity extends FragmentActivity {

    private ViewPager mPager;
    int stringSize;
    List<DisplayData> ddL = null;
    String location;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_weather_info);
		// Show the Up button in the action bar.
		setupActionBar();

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });

		Intent intent = getIntent();
		location = intent.getStringExtra(StationListActivity.LOCATION_ID);
		
		new AssembleWeatherAsync().execute(this);

	}
	
	
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//	    super.onConfigurationChanged(newConfig);
//	    
//        setContentView(R.layout.activity_display_weather_info);
//
//	    // Display the data onPostExecute
//        DisplayWeatherInfoActivity.this.mPagerAdapter.notifyDataSetChanged();
//	}
	
	
	/**
	 * Run thread to get weather data
	 * @author RT
	 *
	 */
	public class AssembleWeatherAsync extends AsyncTask<DisplayWeatherInfoQuadNumbersActivity, 
	                                                   Integer, WeatherDataParsed> {
	    
        private ProgressDialog progressD;
        
	    public AssembleWeatherAsync() {
	        progressD = new ProgressDialog(DisplayWeatherInfoQuadNumbersActivity.this);	        
	    }
	    
	    protected void onPreExecute() {
	        this.progressD.setTitle("Retrieving weather");
            this.progressD.setMessage(
                    (DisplayWeatherInfoQuadNumbersActivity.this.location.split(",").length > 1 ? 
                            DisplayWeatherInfoQuadNumbersActivity.this.location.split(",")[0] : "This land") +
                    " is a pleasant place.");
	        
	        if( ! this.progressD.isShowing())
	            this.progressD.show();
	    }
	    
	    protected WeatherDataParsed doInBackground(DisplayWeatherInfoQuadNumbersActivity... displayW )
	    {
	        AssembleWeatherData assembleWeatherData = new AssembleWeatherData(
	                DisplayWeatherInfoQuadNumbersActivity.this.location);
	        WeatherDataParsed wdp = assembleWeatherData.retrieveWeather();
	        
	        return wdp;
	    }
	    
	    protected void onPostExecute(WeatherDataParsed wdp_in)
	    {
	        if(progressD.isShowing()) {
	            progressD.dismiss();
	        }
	        
	        if(wdp_in == null) {
	            return;
	        }
	        
	        // Update the data for displaying
	        DisplayWeatherInfoQuadNumbersActivity.this.ddL = wdp_in.generateDisplayDataList();   
	        stringSize = ddL.size();
	        
	        // Display the data onPostExecute
	        DisplayWeatherInfoQuadNumbersActivity.this.mPagerAdapter.notifyDataSetChanged();
	    }
	    
	}  // AssembleWeatherAsync
	
	
	
	protected void onResume() {
	    super.onResume();
	    
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.display_message, menu);
//		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
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
	
    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
// java.lang.NoClassDefFoundError: 
// com.yanas.mobileapp.weathercast.DisplayWeatherInfoActivity$ScreenSlidePagerAdapter

    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int endDd = (position + 1) * 4;
            int startDd = endDd >= 4 ? endDd - 4 : 0;
            ArrayList<DisplayData> ddSubL = new ArrayList<DisplayData>();
            for(int i = startDd; i < endDd && i < ddL.size(); i++) {
                ddSubL.add( ddL.get(i) );
            }
            return DisplayWeatherInfoAccessQNPageFragment.create(ddSubL);
        }

        @Override
        public int getCount() {
            return (int) Math.ceil( stringSize / 4);
        }
    }

} // public class DisplayMessageActivity extends Activity
