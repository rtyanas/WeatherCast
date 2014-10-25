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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed;
import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed.DisplayData;
import com.yanas.utils.StringUtils;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    
    /**
     * 
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	// bDisplayData dd = createTestDisplayData();
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.display_weather_layout, container, false);

        String comma = ", ";
        
        // 2013-10-24T08:00:00-04:00
        SimpleDateFormat sdfInput = new SimpleDateFormat( // input parsing
        		"yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        SimpleDateFormat sdfDisplay = new SimpleDateFormat( // output GUI
        		"MMM d", Locale.US);
        SimpleDateFormat sdfSunCheck = new SimpleDateFormat( // output GUI
        		"HH", Locale.US);
        SimpleDateFormat sdfDayOfWeek = new SimpleDateFormat( // output GUI
        		"E", Locale.US);
        String todaysDate = "Today's Date and Time";
        Integer hour = 0;
        String dayOfWeek = "Day of week";
        
        // This date used to calculate the day and date in header and for moon phase
        Date dateOfData = new Date();
        try {
            dateOfData = sdfInput.parse(displayData.getTemperature().getPeriod());
			Log.d("DisplayWeatherInfoAccessPageFragmnt", "Date: "+ dateOfData);
			todaysDate = sdfDisplay.format(dateOfData);
			hour = Integer.parseInt(sdfSunCheck.format(dateOfData));
			dayOfWeek = sdfDayOfWeek.format(dateOfData) +" "+ hour.toString() +":00";
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        // Set the title view to show city and date/time
        ((TextView) rootView.findViewById(R.id.date_time)).setText(todaysDate); // + " "+ dateTime[0] +" ~ "+ dateTime[1].split("-")[0]);

        // Set the title view to show city and date/time
        ((TextView) rootView.findViewById(R.id.city_zip)).setText(
                StringUtils.createStationRow(displayData.getCity(), 
                        displayData.getState(), 
                        displayData.getZipcode()));

        // Temperature
        String temperatureIconStr = "Not Avail";
        if(displayData.getTemperature() != null ) {
            try {
                temperatureIconStr = 
                        getTemperatureValue(Integer.parseInt(displayData.getTemperature().getValue()) );      
            } catch(NumberFormatException nfe) {
                if(GlobalSettings.display_weatherInfo_access_pagefrag) 
                    Log.e("DisplayWeatherInfoAccessPageFragment", "Temperature number parse error.");
            }
        }

        int temperatureId = rootView.getResources().getIdentifier(
                "temp_guage"+ temperatureIconStr.toLowerCase(), "drawable", "com.yanas.mobileapp.weathercast");

        if(temperatureId != 0) {
            ((ImageView) rootView.findViewById(
                    R.id.temp_icon)).setImageResource(temperatureId);
            ((ImageView) rootView.findViewById(
                    R.id.temp_image)).setImageResource(temperatureId);
        }
        
        ((TextView) rootView.findViewById(R.id.temp_hour)).setText(
        		displayData.getTemperature().getValue() +" "+ 
        		(displayData.getTemperature().getUnits().length() >= 1 ?
        				displayData.getTemperature().getUnits() : "") );
        		
        String tMax = displayData.getTempMax() == null ? "Max Not Avail" :
        	"Max "+displayData.getTempMax().getValue() +" "+ 
         		   (displayData.getTempMax().getUnits().length() >= 1 ?
         				  displayData.getTempMax().getUnits() : "");
        ((TextView) rootView.findViewById(R.id.temp_max)).setText(tMax);
        		
		String tMin = displayData.getTempMin() == null ? "Min Not Avail" :
			"Min "+displayData.getTempMin().getValue() +" "+ 
			(displayData.getTempMin().getUnits().length() >= 1 ?
					displayData.getTempMin().getUnits() : "");
        ((TextView) rootView.findViewById(R.id.temp_min)).setText(tMin);
        		
        // Wind
        String windIconStr = "Not Avail";
        if(displayData.getWindSustained() != null ) {
            try {
                windIconStr = 
                        getWindValue(Integer.parseInt(displayData.getWindSustained().getValue()) );      
            } catch(NumberFormatException nfe) {
                if(GlobalSettings.display_weatherInfo_access_pagefrag) 
                    Log.e("DisplayWeatherInfoAccessPageFragment", "Wind number parse error.");
            }
        }

        int windId = rootView.getResources().getIdentifier(
                "sailing_wind"+ windIconStr.toLowerCase(), "drawable", "com.yanas.mobileapp.weathercast");

        if(windId != 0) {
            ((ImageView) rootView.findViewById(
                    R.id.wind_image)).setImageResource(windId);
        }
        
        String wSus = displayData.getWindSustained() == null ? "Wind Not Avail" :
        		"Wind "+displayData.getWindSustained().getValue() +" "+ 
                  	    displayData.getWindSustained().getUnits();
        ((TextView) rootView.findViewById(R.id.wind_sustained)).setText( wSus );

        String compassDir = "Not Avail";
        if(displayData.getWindDirection() != null ) {
        	try {
        		compassDir = 
        				getRoseValue(Integer.parseInt(displayData.getWindDirection().getValue()));
        		
        	} catch(NumberFormatException nfe) {
        		if(GlobalSettings.display_weatherInfo_access_pagefrag) 
        			Log.e("DisplayWeatherInfoAccessPageFragment", "Wind direction number parse error.");
        	}
        }

        String wDir = displayData.getWindDirection() == null ? "Direction Not Avail" :
        		"Direction "+displayData.getWindDirection().getValue() +" "+ 
                   	         displayData.getWindDirection().getUnits();
        // ((TextView) rootView.findViewById(R.id.wind_direction)).setText(wDir );
        		
        String wGust = displayData.getWindGust() == null ? "Gust Not Avail" :
        		"Gust "+displayData.getWindGust().getValue() +" "+ 
                   	    displayData.getWindGust().getUnits();
        ((TextView) rootView.findViewById(R.id.wind_gust)).setText( wGust );
        		
        String pop = displayData.getPropPrecip12() == null ? "POP Not Available" :
        		displayData.getPropPrecip12().getValue() +
        		displayData.getPropPrecip12().getUnits() +
        		" Precip" 
                   	   ;
        ((TextView) rootView.findViewById(R.id.pop)).setText(pop );
        
        String cloudAmount = displayData.getCloudAmount() == null ? "Cloud Coverage Not Available" :
    		displayData.getCloudAmount().getValue() +
    		displayData.getCloudAmount().getUnits() +
    		" Cloud Cover ";
        ((TextView) rootView.findViewById(R.id.cloudAmount)).setText(cloudAmount );
    
        // ((ImageView) rootView.findViewById(R.id.weather_predominant_icon)).setImageResource(dayNightIcon);        
        		
        // Predicted predominant weather
        
        String wx = "";
        
        if(displayData.getWeatherPredominant() != null) {
            String qual = displayData.getWeatherPredominant() == null ? "" : displayData.getWeatherPredominant().getQualifier();
            String intensity = displayData.getWeatherPredominant().getIntensity() == null ? "" : displayData.getWeatherPredominant().getIntensity();
            String weatherType = displayData.getWeatherPredominant().getWeather_type() == null ? "" : displayData.getWeatherPredominant().getWeather_type();
            String coverage = displayData.getWeatherPredominant().getCoverage() == null ? "" : displayData.getWeatherPredominant().getCoverage();
            
            qual = qual.equals("none") ? "" : qual;

            if(intensity.equals("none")) {
                wx = "Slight chance of "+ weatherType;
            }            
            else if( ( ! "".equals(coverage) ) && ( ! "".equals(intensity) ) && 
                    ( ! "".equals(weatherType) ) ) {
                wx =  coverage +", "+ intensity +", "+
                        weatherType  + (qual.equals("") ? "" : comma) + 
                        qual;
            } else if(displayData.getCloudAmount() != null  ) {
                try { 
                    int cloudAmountWx = Integer.parseInt(displayData.getCloudAmount().getValue());
                    if(cloudAmountWx < 25)
                        wx = "Clear";
                } catch(NumberFormatException  nfe) {
                    Log.e("DisplayWeatherInfoAccessPageFragment", "Clound Amount for Wx clear prediction number parse error.");
                }                
            }
        } else {
            if(displayData.getCloudAmount() != null  ) {
                try { 
                    int cloudAmountWx = Integer.parseInt(displayData.getCloudAmount().getValue());
                    if(cloudAmountWx < 25)
                        wx = "Clear";
                } catch(NumberFormatException  nfe) {
                    Log.e("DisplayWeatherInfoAccessPageFragment", "Clound Amount for Wx clear prediction number parse error.");
                }                
            }

        }

        ((TextView) rootView.findViewById(R.id.weather_predominant_amount)).setText(wx);
        // .getCoverage() +","+ w.getIntensity() +","+ 
	    // w.getWeather_type() +","+ w.getQualifier()
        		
        ((TextView) rootView.findViewById(R.id.sailingExperience)).setText( estimatedSailingExperience(displayData) );

        ((TextView) rootView.findViewById(R.id.day_of_week)).setText( dayOfWeek );
        
        ((TextView) rootView.findViewById(R.id.compass_direction)).setText( compassDir );

        int windDirId = rootView.getResources().getIdentifier(
        		"wind_dir_"+ compassDir.toLowerCase() +"_50", "drawable", "com.yanas.mobileapp.weathercast");

        if(windDirId != 0) {
	        ((ImageView) rootView.findViewById(
	        		R.id.compass_dir_image)).setImageResource(windDirId);
        }
		
        // Add - Sun/Moon/clouds/rain

        // Set icon for sun or moon

        int dayNightIcon; // get Resource
 
        if(hour > 19  ||  hour <= 06 )
            dayNightIcon = getCurrentMoonPhaseResource(dateOfData);
        else
            dayNightIcon = R.drawable.sun;
        
        ((ImageView) rootView.findViewById(R.id.weather_predominant)).setBackgroundResource(dayNightIcon);


        String cloudConfigStr = "Not Avail";
        if(displayData.getCloudAmount() != null ) {
            try {
                cloudConfigStr = 
                        getCloudConfigValue(Integer.parseInt(displayData.getCloudAmount().getValue()));
                
            } catch(NumberFormatException nfe) {
                if(GlobalSettings.display_weatherInfo_access_pagefrag) 
                    Log.e("DisplayWeatherInfoAccessPageFragment", "Wind direction number parse error.");
            }
        }
        
        String rainConfigStr = "Not Avail";
        if(displayData.getPropPrecip12() != null ) {
            try {
                rainConfigStr = 
                        getRainConfigValue(Integer.parseInt(displayData.getPropPrecip12().getValue()));
                
            } catch(NumberFormatException nfe) {
                if(GlobalSettings.display_weatherInfo_access_pagefrag) 
                    Log.e("DisplayWeatherInfoAccessPageFragment", "Wind direction number parse error.");
            }
        }
        
        int cloudConfigId = rootView.getResources().getIdentifier(
                "cloud_"+ cloudConfigStr.toLowerCase(), "drawable", "com.yanas.mobileapp.weathercast");
        
        int rainConfigId = rootView.getResources().getIdentifier(
                "rain_"+ rainConfigStr.toLowerCase(), "drawable", "com.yanas.mobileapp.weathercast");
        
        Drawable[] layers = new Drawable[2];  // Used to combine cloud and rain.

        if(cloudConfigId != 0) {
            layers[0] = rootView.getResources().getDrawable(cloudConfigId);
        }
        else {
            layers[0] = rootView.getResources().getDrawable(R.drawable.cloud_noclouds);
        }
        
        if(rainConfigId != 0) {
            layers[1] = rootView.getResources().getDrawable(rainConfigId);
        }
        else {
            layers[1] = rootView.getResources().getDrawable(R.drawable.cloud_noclouds);
        }
        
        LayerDrawable layerDr = new LayerDrawable(layers); 
        ((ImageView) rootView.findViewById(
                R.id.weather_predominant)).setImageDrawable(layerDr);

        return rootView;
    }

    
    private String getTemperatureValue(int temperature_in) {
        String temperatureRet = "";
        
        if(temperature_in > 90) {
            temperatureRet = "8";
        } else if(temperature_in <= 90 && temperature_in > 80 ) {
            temperatureRet = "7";
        } else if(temperature_in <= 80 && temperature_in > 65 ) {
            temperatureRet = "6";
        } else if(temperature_in <= 65 && temperature_in > 55 ) {
            temperatureRet = "5";
        } else if(temperature_in <= 55 && temperature_in > 45 ) {
            temperatureRet = "4";
        } else if(temperature_in <= 45 && temperature_in > 35 ) {
            temperatureRet = "3";
        } else if(temperature_in <= 35 && temperature_in > 25 ) {
            temperatureRet = "2";
        } else if(temperature_in <= 25 ) {
            temperatureRet = "1";
        }

        return temperatureRet;
    }
    
    
    private String getWindValue(int windKts_in ) {
        String windLevel = "";
        
        if(windKts_in < 3) {
            windLevel = "1";
        } else if(windKts_in >= 3 && windKts_in < 6 ) {
            windLevel = "2";
        } else if(windKts_in >= 6 && windKts_in < 15 ) {
            windLevel = "3";
        } else if(windKts_in >= 15 ) {
            windLevel = "4";
        }
        
        return windLevel;
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
    
    
    public String getRoseValue(int direction) {
    	String roseVal = "Not Avail";
    	int N = 360, NNE = 22,  NE = 45,  ENE = 67;
    	int E = 90,  ESE = 111, SE = 135, SSE = 157;
    	int S = 180, SSW = 202, SW = 225, WSW = 247;
    	int W = 270, WNW = 292, NW = 315, NNW = 337;
    	int variance = 11;

    	
    	if( direction <= variance )
    		roseVal = "N";
    	else if(inRange(direction, N, variance) )
    		roseVal = "N";
    	else if(inRange(direction, NNE, variance) )
    		roseVal = "NNE";
    	else if(inRange(direction, NE, variance) )
    		roseVal = "NE";
    	else if(inRange(direction, ENE, variance) )
    		roseVal = "ENE";
    	else if(inRange(direction, E, variance) )
    		roseVal = "E";
    	else if(inRange(direction, ESE, variance) )
    		roseVal = "ESE";
    	else if(inRange(direction, SE, variance) )
    		roseVal = "SE";
    	else if(inRange(direction, SSE, variance) )
    		roseVal = "SSE";
    	else if(inRange(direction, S, variance) )
    		roseVal = "S";
    	else if(inRange(direction, SSW, variance) )
    		roseVal = "SSW";
    	else if(inRange(direction, SW, variance) )
    		roseVal = "SW";
    	else if(inRange(direction, WSW, variance) )
    		roseVal = "WSW";
    	else if(inRange(direction, W, variance) )
    		roseVal = "W";
    	else if(inRange(direction, WNW, variance) )
    		roseVal = "WNW";
    	else if(inRange(direction, NW, variance) )
    		roseVal = "NW";
    	else if(inRange(direction, NNW, variance) )
    		roseVal = "NNW";
    	else
    		roseVal = "NESW";
    	
    	return roseVal;
    }
    
    
    public boolean inRange(int direction_in, int compassDir, int variance) {
    	if( direction_in >= (compassDir - variance)  &&   direction_in <= (compassDir + variance))
    		return true;
    		
    	return false;	
    }
    
    final long dayInMilli = (60000 * 60 * 24);
    final double moonPhase = 29.5305888610;
    
    private int getCurrentMoonPhase(Date dateOfData_in) {
        int phaseValue = 0; // new = 0 ... full = 8
        // SimpleDateFormat sdfJan2014 = new SimpleDateFormat("yyyy MMM dd HH:mm:s.S"); 
        Date Jan2014Date = new Date();

        try {
            SimpleDateFormat sdfJan2014 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); // 2014-10-10T08:00:00-04:00
            Jan2014Date = sdfJan2014.parse("2014-01-01T11:14:00-04:00"); 
            long daysDiff = (dateOfData_in.getTime() - Jan2014Date.getTime()) / dayInMilli;
            Log.d("DisplayWeatherAccessPageFragment:getCurrentMoonPhase()",
                    "days Diff: "+ daysDiff 
                    +", phase pre number: "+ daysDiff % moonPhase );

            switch( (int) (daysDiff % moonPhase)) {
            case  0: phaseValue = 0; break; 
            case  1: phaseValue = 0; break; 
            case  2: phaseValue = 1; break; 
            case  3: phaseValue = 1; break; 
            case  4: phaseValue = 2; break; 
            case  5: phaseValue = 2; break; 
            case  6: phaseValue = 3; break; 
            case  7: phaseValue = 3; break; 
            case  8: phaseValue = 4; break; 
            case  9: phaseValue = 4; break; 
            case 10: phaseValue = 5; break; 
            case 11: phaseValue = 5; break; 
            case 12: phaseValue = 6; break; 
            case 13: phaseValue = 7; break; 
            case 14: phaseValue = 8; break; 
            case 15: phaseValue = 8; break;
            case 16: phaseValue = 8; break;
            case 17: phaseValue = 9; break;
            case 18: phaseValue = 9; break;
            case 19: phaseValue = 10; break;
            case 20: phaseValue = 10; break;
            case 21: phaseValue = 11; break;
            case 22: phaseValue = 11; break;
            case 23: phaseValue = 12; break;
            case 24: phaseValue = 13; break;
            case 25: phaseValue = 13; break;
            case 26: phaseValue = 14; break;
            case 27: phaseValue = 14; break;
            case 28: phaseValue = 15; break;
            case 29: phaseValue = 15; break;
            case 30: phaseValue = 15; break;
            }
        } catch (IllegalArgumentException e) {
            Log.e("DisplayWeatherAccessPageFragment:getCurrentMoonPhase()", e.getMessage());
        } catch (ParseException e) {
            Log.e("DisplayWeatherAccessPageFragment:getCurrentMoonPhase()", e.getMessage());
        }
        
        return phaseValue;
    }
    
    
    private int getCurrentMoonPhaseResource(Date date) {
        int moonResource = R.drawable.moon08_full;

        int phaseValue = getCurrentMoonPhase(date);
        
        switch(phaseValue) {
        case 0:  moonResource = R.drawable.moon00_new; break;
        case 1:  moonResource = R.drawable.moon01_1_8waxing; break;
        case 2:  moonResource = R.drawable.moon02_1_4waxing; break;
        case 3:  moonResource = R.drawable.moon03_3_8waxing; break;
        case 4:  moonResource = R.drawable.moon04_1_2waxing; break;
        case 5:  moonResource = R.drawable.moon05_5_8waxing; break;
        case 6:  moonResource = R.drawable.moon06_3_4waxing; break;
        case 7:  moonResource = R.drawable.moon07_7_8waxing; break;
        case 8:  moonResource = R.drawable.moon08_full; break;
        case 9:  moonResource = R.drawable.moon09_7_8; break;
        case 10: moonResource = R.drawable.moon10_3_4; break;
        case 11: moonResource = R.drawable.moon11_5_8; break;
        case 12: moonResource = R.drawable.moon12_1_2; break;
        case 13: moonResource = R.drawable.moon13_3_8; break;
        case 14: moonResource = R.drawable.moon14_1_4; break;
        case 15: moonResource = R.drawable.moon15_1_8; break;
        }
        
        return moonResource;
    }

    /**
     * Pass percent of clouds, return the cloud configuration from no clouds to 
     * partly to mostly to 100%
     * @param cloudPerc
     * @return
     */
    public String getCloudConfigValue(int cloudPerc) {
        String cloudConfig = null;
        int light = 10, med_light = 25, med = 40;
        int med_heavy = 60, heavy_light = 72, heavy = 90; 
                
        if(cloudPerc < light) {
            cloudConfig = "noclouds";
        }
        else if(cloudPerc >= light && cloudPerc < med_light ) {
            cloudConfig = "light";
        }
        else if(cloudPerc >= med_light && cloudPerc < med ) {
            cloudConfig = "med_light";
        }
        else if(cloudPerc >= med && cloudPerc < med_heavy ) {
            cloudConfig = "med";
        }
        else if(cloudPerc >= med_heavy && cloudPerc < heavy_light ) {
            cloudConfig = "med_heavy";
        }
        else if(cloudPerc >= heavy_light && cloudPerc < heavy ) {
            cloudConfig = "heavy";
        }
        else if(cloudPerc >= heavy_light ) {
            cloudConfig = "obscure";
        }
        
        return cloudConfig;
    }
    
    public String getRainConfigValue(int probPrecip) {
        String rainConfigRet = "";
        int light = 15, med = 30, med_heavy = 60, heavy = 75;
        
        if(probPrecip > light && probPrecip < med)
            rainConfigRet = "light";
        else if(probPrecip >= med && probPrecip < med_heavy)
            rainConfigRet = "med_light";
        else if(probPrecip >= med_heavy && probPrecip < heavy)
            rainConfigRet = "med_heavy";
        else if(probPrecip >= heavy)
            rainConfigRet = "heavy";
        
        return rainConfigRet;
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
