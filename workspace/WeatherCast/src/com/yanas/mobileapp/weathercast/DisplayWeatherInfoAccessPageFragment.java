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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
	WeatherDataControl weatherControl;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static DisplayWeatherInfoAccessPageFragment create(DisplayData dataSelected_in) {
        DisplayWeatherInfoAccessPageFragment fragment = new DisplayWeatherInfoAccessPageFragment();
        Bundle args = new Bundle();
        
        args.putSerializable(STATIONDATA_DATETIME_ARG, dataSelected_in);
        fragment.setArguments(args);

        return fragment;
    }

    public DisplayWeatherInfoAccessPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        byte stationBA[] = null;
        
        if(getArguments() != null) {  // Get station data
            DisplayData displayData = (DisplayData) getArguments().getSerializable(STATIONDATA_DATETIME_ARG);
            weatherControl = new WeatherDataControl(displayData);
        } else {
        	Log.e("DisplayWeatherInfoAccessPageFragment", "getArguments() is null");
        }
    }

    
    /**
     * 
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                                Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.display_weather_layout, container, false);

        
        if(weatherControl.getDateOfData() == null) {
            // Set the title view to show city and date/time
            ((ImageView) rootView.findViewById(
                    R.id.temp_icon)).setImageResource(0);
            ((ImageView) rootView.findViewById(
                    R.id.temp_image)).setImageResource(0);
            
            ((TextView) rootView.findViewById(R.id.temp_hour)).setText( "All Data" );
                    
            ((TextView) rootView.findViewById(R.id.temp_max)).setText("");
                    
            ((TextView) rootView.findViewById(R.id.temp_min)).setText("Unavailable");
            return rootView;
        }
        
        // Set the title view to show city and date/time
        ((TextView) rootView.findViewById(R.id.date_time)).setText(weatherControl.getTodaysDate()); // + " "+ dateTime[0] +" ~ "+ dateTime[1].split("-")[0]);

        // Set the title view to show city and date/time
        ((TextView) rootView.findViewById(R.id.city_zip)).setText(
                StringUtils.createStationRow(weatherControl.getCity(), 
                        weatherControl.getState(), 
                        weatherControl.getZipcode()));

        // Temperature
        int temperatureId = rootView.getResources().getIdentifier(
                "temp_guage"+ weatherControl.getTemperatureIconLevel().toLowerCase(), 
                "drawable", "com.yanas.mobileapp.weathercast");

        if(temperatureId != 0) {
            ((ImageView) rootView.findViewById(
                    R.id.temp_icon)).setImageResource(temperatureId);
            ((ImageView) rootView.findViewById(
                    R.id.temp_image)).setImageResource(temperatureId);
        }
        
        ((TextView) rootView.findViewById(R.id.temp_hour)).setText( weatherControl.getTemperature() );
        		
        ((TextView) rootView.findViewById(R.id.temp_max)).setText(weatherControl.getTemperatureMax());
        		
        ((TextView) rootView.findViewById(R.id.temp_min)).setText(weatherControl.getTemperatureMin());

        // Wind
        ((TextView) rootView.findViewById(R.id.wind_sustained)).setText( weatherControl.getWindSustained() );

        int windId = rootView.getResources().getIdentifier(
                "sailing_wind"+ weatherControl.getWindIconLevel().toLowerCase(), 
                "drawable", "com.yanas.mobileapp.weathercast");

        if(windId != 0) {
            ((ImageView) rootView.findViewById(
                    R.id.wind_image)).setImageResource(windId);
        }
        
        int windDirId = rootView.getResources().getIdentifier(
                "wind_dir_"+ weatherControl.getCompassDir().toLowerCase() +"_50", 
                "drawable", "com.yanas.mobileapp.weathercast");

        if(windDirId != 0) {
            ((ImageView) rootView.findViewById(
                    R.id.compass_dir_image)).setImageResource(windDirId);
        }
        

        ((TextView) rootView.findViewById(R.id.compass_direction)).setText( weatherControl.getCompassDir() );

        ((TextView) rootView.findViewById(R.id.wind_gust)).setText( weatherControl.getWindGust() );

        // ((TextView) rootView.findViewById(R.id.wind_direction)).setText(weatherControl.getWindDir() );
        		
        ((TextView) rootView.findViewById(R.id.pop)).setText(weatherControl.getPop() );
        
        ((TextView) rootView.findViewById(R.id.cloudAmount)).setText(weatherControl.getCloudAmount() );
    
        // Predicted predominant weather
        
        ((TextView) rootView.findViewById(R.id.weather_predominant_amount)).setText(weatherControl.getPredominantWx());
        // .getCoverage() +","+ w.getIntensity() +","+ 
	    // w.getWeather_type() +","+ w.getQualifier()
        		
        ((TextView) rootView.findViewById(R.id.sailingExperience)).setText( weatherControl.estimatedSailingExperience() );

        ((TextView) rootView.findViewById(R.id.day_of_week)).setText( weatherControl.getDayOfWeek() );
        
        // Add - Sun/Moon/clouds/rain
        
        LayerDrawable layerDr = new LayerDrawable(getGraphicWeather(rootView, weatherControl, R.id.weather_predominant)); 
        ((ImageView) rootView.findViewById(
                R.id.weather_predominant)).setImageDrawable(layerDr);

        return rootView;
    }


    static public Drawable[] getGraphicWeather(ViewGroup viewG, WeatherDataControl weatherC, int predResource) {
        // Set icon for sun or moon
        int dayNightIcon; // get Resource
 
        // Find sunrise - sunset times
        if( weatherC.isMoonRising() )
            dayNightIcon = weatherC.getCurrentMoonPhaseResource();
        else
            dayNightIcon = R.drawable.sun;
        
        ((ImageView) viewG.findViewById(predResource)).setBackgroundResource(dayNightIcon);

        // Cloud amount; how many clouds
        int cloudConfigId = viewG.getResources().getIdentifier(
                "cloud_"+ weatherC.getCloudLevel().toLowerCase(), 
                "drawable", "com.yanas.mobileapp.weathercast");

        // Precipitation amount; amount of rain or snow
        
        int rainConfigId = 0;

        if( weatherC.getPredominantWxType().toLowerCase().contains("freezing") ) {
             rainConfigId = viewG.getResources().getIdentifier(
                     "icey_"+ weatherC.getRainSnowIceLevel().toLowerCase(), "drawable", "com.yanas.mobileapp.weathercast"); 
         } else if( weatherC.getPredominantWxType().toLowerCase().contains("snow") ) {
             rainConfigId = viewG.getResources().getIdentifier(
                     "snow_"+ weatherC.getRainSnowIceLevel().toLowerCase(), "drawable", "com.yanas.mobileapp.weathercast");
         } else if( weatherC.getPredominantWxType().toLowerCase().contains("rain") ) {
                 rainConfigId = viewG.getResources().getIdentifier(
                     "rain_"+ weatherC.getRainSnowIceLevel().toLowerCase(), "drawable", "com.yanas.mobileapp.weathercast"); 
         }
            
        
        Drawable[] layers = new Drawable[3];  // Used to combine cloud and rain.

        if(cloudConfigId != 0) {
            layers[0] = viewG.getResources().getDrawable(cloudConfigId);
        }
        else {
            layers[0] = viewG.getResources().getDrawable(R.drawable.cloud_noclouds);
        }
        
        if(rainConfigId != 0) {
            layers[1] = viewG.getResources().getDrawable(rainConfigId);
        }
        else {
            layers[1] = viewG.getResources().getDrawable(R.drawable.cloud_noclouds);
        }
        

        // Default
        layers[2] = viewG.getResources().getDrawable(R.drawable.cloud_noclouds);

        if(weatherC.isThunder()) {
            if( weatherC.getPredominantWxType().toLowerCase().contains("thunderstorms") &&
                    weatherC.getPredominantWxType().toLowerCase().contains("slight chance") ) {
                layers[2] = viewG.getResources().getDrawable(R.drawable.thunderstorm_slight_chance);
            } 
            else if( weatherC.getPredominantWxType().toLowerCase().contains("thunderstorms") ) {
                layers[2] = viewG.getResources().getDrawable(R.drawable.thunderstorm);
            }
        }
  
        
        return layers;

    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }


}
