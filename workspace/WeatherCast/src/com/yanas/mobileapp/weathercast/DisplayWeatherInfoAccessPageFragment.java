/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yanas.mobileapp.weathercast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed;
import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed.DisplayData;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * ScreenSlideActivity} samples.</p>
 */
public class DisplayWeatherInfoAccessPageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    public static final String ARG_TEMP = "temp";
    public static final String STATIONDATA_DATETIME_ARG = "station_datetime_Data";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
	DisplayData displayData;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static DisplayWeatherInfoAccessPageFragment create(DisplayData dataSelected_in) {
        DisplayWeatherInfoAccessPageFragment fragment = new DisplayWeatherInfoAccessPageFragment();
        Bundle args = new Bundle();
        
     // Serialize data object to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(dataSelected_in);
	        out.close();
	        args.putByteArray(STATIONDATA_DATETIME_ARG, bos.toByteArray());
	        fragment.setArguments(args);
		} catch (IOException e) {
			Log.e("DisplayWeatherInfoAccessPageFragment", "create, "+ e.getMessage());
			e.printStackTrace();
		}
        
        return fragment;
    }

    public DisplayWeatherInfoAccessPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        byte stationBA[] = null;
        
        if(getArguments() != null) {  // Get station data
        	stationBA = getArguments().getByteArray(STATIONDATA_DATETIME_ARG);
        }
        else {
        	Log.e("DisplayWeatherInfoAccessPageFragment", "getArguments() is null");
        }
        
        if(stationBA != null) { // Translate from Serial back to StationData
	        ByteArrayInputStream bin =  new ByteArrayInputStream (stationBA);
	        
	        try {
				ObjectInputStream in = new ObjectInputStream(bin);
				displayData = (DisplayData)in.readObject();
			} catch (ClassNotFoundException e) {
		        Log.e("DisplayWeatherInfoAccessPageFragment", e.getMessage());
			} catch (StreamCorruptedException e) {
		        Log.e("DisplayWeatherInfoAccessPageFragment", e.getMessage());
			} catch (IOException e) {
		        Log.e("DisplayWeatherInfoAccessPageFragment", e.getMessage());
			}
	        
	        Log.d("DisplayWeatherInfoAccessPageFragment", displayData.toString());
	        // stationData = getArguments().g.getStringArray(ARG_TEMP);
        }
        else {
        	Log.e("DisplayWeatherInfoAccessPageFragment", "onCreate: stationBA is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	// bDisplayData dd = createTestDisplayData();
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.display_weather_layout, container, false);

        String dateTime[] = {"",""};
        
        if(displayData.getTemperature().getPeriod().split("T").length >= 2) {
        	dateTime = displayData.getTemperature().getPeriod().split("T");
        }
        
        // Set the title view to show the page number.
        ((TextView) rootView.findViewById(R.id.date_time)).setText(
        		displayData.getCity() +", "+ displayData.getState() +", "+ 
        		displayData.getZipcode() +"\n"+ dateTime[0] +" ~ "+ dateTime[1].split("-")[0]);

        ((TextView) rootView.findViewById(R.id.temp_hour)).setText(
        		displayData.getTemperature().getValue() +" "+ 
        		displayData.getTemperature().getUnits().substring(0, 1) );
        		
        String tMax = displayData.getTempMax() == null ? "Max Not Avail" :
        	"Max "+displayData.getTempMax().getValue() +" "+ 
         		   displayData.getTempMax().getUnits().substring(0, 1);
        ((TextView) rootView.findViewById(R.id.temp_max)).setText(tMax);
        		
		String tMin = displayData.getTempMin() == null ? "Min Not Avail" :
			"Min "+displayData.getTempMin().getValue() +" "+ 
			displayData.getTempMin().getUnits().substring(0, 1);
        ((TextView) rootView.findViewById(R.id.temp_min)).setText(tMin);
        		
        String wSus = displayData.getWindSustained() == null ? "Wind Not Avail" :
        		"Wind "+displayData.getWindSustained().getValue() +" "+ 
                  	    displayData.getWindSustained().getUnits();
        ((TextView) rootView.findViewById(R.id.wind_sustained)).setText( wSus );
        		
        String wDir = displayData.getWindDirection() == null ? "Direction Not Avail" :
        		"Direction "+displayData.getWindDirection().getValue() +" "+ 
                   	         displayData.getWindDirection().getUnits();
        ((TextView) rootView.findViewById(R.id.wind_direction)).setText(wDir );
        		
        String wGust = displayData.getWindGust() == null ? "Gust Not Avail" :
        		"Gust "+displayData.getWindGust().getValue() +" "+ 
                   	    displayData.getWindGust().getUnits();
        ((TextView) rootView.findViewById(R.id.wind_gust)).setText( wGust );
        		
        String pop = displayData.getPropPrecip12() == null ? "POP Not Available" :
        		"Probability of Precip "+displayData.getPropPrecip12().getValue() +" "+ 
                   	   displayData.getPropPrecip12().getUnits();
        ((TextView) rootView.findViewById(R.id.pop)).setText(pop );
        		
		String wx =  displayData.getWeatherPredominant() == null ? "Wx Not Available" : 
			"Wx "+displayData.getWeatherPredominant().getCoverage() +", "+ 
					displayData.getWeatherPredominant().getIntensity() +", "+
					displayData.getWeatherPredominant().getWeather_type()  +", "+ 
					displayData.getWeatherPredominant().getQualifier();
		
        ((TextView) rootView.findViewById(R.id.weather_predominant_amount)).setText(wx);
        // .getCoverage() +","+ w.getIntensity() +","+ 
	    // w.getWeather_type() +","+ w.getQualifier()
        		
        ((TextView) rootView.findViewById(R.id.sailingExperience)).setText( estimatedSailingExperience(displayData) );

        
        		//stationData.toString().replaceFirst("Rockaway", "Yanas House") ); //  +"data source www.weather.gov") ;

        return rootView;
    }

    
    private String estimatedSailingExperience(DisplayData displayData) {
    	StringBuffer sailingEx = new StringBuffer();
    	int wSus = -1;
    	int wGust = -1;
    	int temp = -100;
    	int pop = -1;
    	
    	try {wSus = displayData.getWindSustained() != null ? Integer.parseInt(displayData.getWindSustained().getValue() ) : -1; }
    	catch (NumberFormatException nfe) {};
    	try {wGust = displayData.getWindGust() != null ? Integer.parseInt(displayData.getWindGust().getValue() ) : -1 ; }
    	catch (NumberFormatException nfe) {};
    	try {temp = displayData.getTemperature() != null ? Integer.parseInt(displayData.getTemperature().getValue() ) : -100; }
    	catch (NumberFormatException nfe) {};
    	try {pop = displayData.getPropPrecip12() != null ? Integer.parseInt(displayData.getPropPrecip12().getValue() ) : -1; }
    	catch (NumberFormatException nfe) {};
    	
    	if(wSus < 0 )
    		sailingEx.append("No wind info ");
    	else if(wSus >= 0 && wSus < 8 )
    		sailingEx.append("Light winds ");
    	else if(wSus >= 8 && wSus < 18 )
    		sailingEx.append("Good winds ");
    	else if(wSus >= 18 && wSus < 20 )
    		sailingEx.append("Strong winds ");
    	else if(wSus >= 20 && wSus < 25 )
    		sailingEx.append("Very strong winds - ** Reef ** ");
    	else if(wSus >= 25)
    		sailingEx.append("Big storm! - ** motor home ** ");
    	
    	if(wGust < 0)
    		sailingEx.append("No gust info ");
    	if(wGust >= 0 && wGust < 8 )
    		sailingEx.append("Light gusts ");
    	else if(wGust >= 8 && wGust < 18)
    		sailingEx.append("Medium gusts ");
    	else if(wGust >= 18 && wGust < 20)
    		sailingEx.append("Strong gusts ");
    	else if(wGust >= 20 && wGust < 25)
    		sailingEx.append("Very strong gusts ");
    	else if(wGust >= 25 )
    		sailingEx.append("Definetely reef - gusts ");
    	
    	if(temp < -99)
    		sailingEx.append("Temps not available ");
    	else if(temp < 50 )
    		sailingEx.append("Get a parka ");
    	else if(temp >= 50 && temp < 65 )
    		sailingEx.append("Temperature is cold ");
    	else if(temp >= 65 && temp < 72 )
    		sailingEx.append("Temperature is chilly ");
    	else if(temp >= 72  && temp < 85 )
    		sailingEx.append("Temperature is nice ");
    	else if(temp >= 85  && temp < 90 )
    		sailingEx.append("Temperature is warm ");
    	else if(temp >= 90  )
    		sailingEx.append("Temperature is hot, look for wind ");
    	
    	if(pop < 0)
    		sailingEx.append("Prop precip not available ");
    	else if(pop < 10 )
    		sailingEx.append("No rain ");
    	else if(pop >= 10 && pop < 30 )
    		sailingEx.append("Maybe some rain ");
    	else if(pop >= 30 && pop < 50 )
    		sailingEx.append("Might rain ");
    	else if(pop >= 50  && pop < 75 )
    		sailingEx.append("Good chance of rain ");
    	else if(pop >= 75 )
    		sailingEx.append("Looks like rain! ");
    	
    	return sailingEx.toString();
    }
    
    
    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    /**
    public DisplayData createTestDisplayData() {
    	
    	WeatherDataParsed wdp = new WeatherDataParsed();
    	DisplayData dd = wdp.new DisplayData();
    	
    	dd.setDateTime("20121104:T2200");
    	dd.setPropPrecip12("10");
    	dd.setTemperature("50");
    	dd.setTempMax("60");
    	dd.setTempMin("30");
    	dd.setWeatherPredominant("light rain showers");
    	dd.setWindDirection("270");
    	dd.setWindGust("12");
    	dd.setWindSustained("9");
    	
    	return dd;
    }
    **/
}
