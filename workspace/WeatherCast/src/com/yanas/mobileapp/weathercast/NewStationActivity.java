package com.yanas.mobileapp.weathercast;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

public class NewStationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_station);
		
		((Button)findViewById(R.id.done_button)).setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					Intent intent = new Intent();
					intent.putExtra(MainActivity.CITY_ARG, 
							((TextView)findViewById(R.id.city)).getText().toString() );
					intent.putExtra(MainActivity.STATE_ARG, 
							((TextView)findViewById(R.id.state)).getText().toString() );
					intent.putExtra(MainActivity.ZIP_ARG, 
							((TextView)findViewById(R.id.zip_code)).getText().toString() );
					intent.putExtra(MainActivity.LAT_ARG, 
							((TextView)findViewById(R.id.latitude)).getText().toString() );
					intent.putExtra(MainActivity.LON_ARG, 
							((TextView)findViewById(R.id.longitude)).getText().toString() );
					setResult(Activity.RESULT_OK, intent);
					
					if(GlobalSettings.new_station_activity) 
						Log.d("NewStationActivity", "onClick: "+ ((TextView)findViewById(R.id.zip_code)).getText().toString());
					finish();
				}
					
				} );	

        ((Button)findViewById(R.id.cancel_button)).setOnClickListener( new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                
                if(GlobalSettings.new_station_activity) 
                    Log.d("NewStationActivity", "onClick: "+ ((TextView)findViewById(R.id.zip_code)).getText().toString());
                finish();
            }
                
            } );    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_station, menu);
		return true;
	}
	
	

}
