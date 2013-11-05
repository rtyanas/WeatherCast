package com.yanas.mobileapp.weathercast;


//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
// import org.w3c.dom.Document;

//import com.example.android.animationsdemo.R;
//import com.yanas.mobileapp.weathercast.DisplayWeatherInfoActivity.ScreenSlidePagerAdapter;


import java.util.List;
import java.util.Vector;

import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed;
import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed.DisplayData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
// 
public class DisplayWeatherInfoActivity extends FragmentActivity {

    private ViewPager mPager;
    private String tempsTest[] = { "60F", "61F", "62F", "63F", "64F", "65F" };
    int stringSize;
    List<DisplayData> ddL = null;

    private static final int NUM_PAGES = 5;

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

//		
		Intent intent = getIntent();
		String location = intent.getStringExtra(StationListActivity.LOCATION_ID);
//		
//		TextView textView = new TextView(this);
//		textView.setTextSize(18);
//		
		AssembleWeatherData assembleWeatherData = new AssembleWeatherData(location);
		WeatherDataParsed wdp = assembleWeatherData.retrieveWeather(this);
		ddL = wdp.generateDisplayDataList();
		
		stringSize = ddL.size();
		
//		textView.setText(assmebleWeatherData.retrieveWeather());
//		
//		setContentView(textView);

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
		getMenuInflater().inflate(R.menu.display_message, menu);
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
        	DisplayData dd = ddL.get(position);
            return DisplayWeatherInfoAccessPageFragment.create(dd);
        }

        @Override
        public int getCount() {
            return stringSize;
        }
    }

} // public class DisplayMessageActivity extends Activity
