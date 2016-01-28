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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class DisplayWeatherInfoAccessQNPageFragment extends Fragment {
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
	ArrayList<WeatherDataControl> weatherControl;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static DisplayWeatherInfoAccessQNPageFragment create(ArrayList<DisplayData> dataSelected_in) {
        DisplayWeatherInfoAccessQNPageFragment fragment = new DisplayWeatherInfoAccessQNPageFragment();
        Bundle args = new Bundle();
        
        args.putSerializable(STATIONDATA_DATETIME_ARG, dataSelected_in);
        fragment.setArguments(args);

        return fragment;
    }

    public DisplayWeatherInfoAccessQNPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        byte stationBA[] = null;
        
        if(getArguments() != null) {  // Get station data
            ArrayList<DisplayData> displayDataL = (ArrayList<DisplayData>) getArguments().getSerializable(STATIONDATA_DATETIME_ARG);
            weatherControl = new ArrayList<WeatherDataControl>();
            for(DisplayData dd : displayDataL) {
                weatherControl.add(new WeatherDataControl(dd) );
            }
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
                .inflate(R.layout.display_weather_qn_layout, container, false);

        // Set quad position one
        
        ((TextView) rootView.findViewById(R.id.cityZip)).setText(StringUtils.createStationRow(weatherControl.get(0).getCity(), 
                weatherControl.get(0).getState(), 
                weatherControl.get(0).getZipcode()));
        
        int qdLoc = 0;
        
        if(weatherControl.size() >= 1) {
            int dayOfWeek1 = getResources().getIdentifier(
                    "dayOfWeek"+ 1, "id", rootView.getContext().getPackageName());
            ((TextView) rootView.findViewById(dayOfWeek1)).setText(
                this.weatherControl.get(qdLoc).getDayOfWeek() );  
//            ((TextView) rootView.findViewById(R.id.dayOfWeek1)).setText(
//                    this.weatherControl.get(qdLoc).getDayOfWeek() );  
            ((TextView) rootView.findViewById(R.id.popAndTemp1)).setText(
                this.weatherControl.get(qdLoc).getTemperature() +" : "+
                weatherControl.get(qdLoc).getPop() );
            ((TextView) rootView.findViewById(R.id.wind1)).setText(
                weatherControl.get(qdLoc).getWindSustained()
                );
            int windDirId = rootView.getResources().getIdentifier(
                    "wind_dir_"+ weatherControl.get(qdLoc).getCompassDir().toLowerCase() +"_50", 
                    "drawable", "com.yanas.mobileapp.weathercast");

            if(windDirId != 0) {
                ((ImageView) rootView.findViewById(
                        R.id.compass_dir_image1)).setImageResource(windDirId);
            }
            ((TextView) rootView.findViewById(R.id.gust1)).setText(
                       weatherControl.get(qdLoc).getWindGust()
                    );
            ((TextView) rootView.findViewById(R.id.sailingExperience1)).
                setText( weatherControl.get(qdLoc).getPredominantWx() );
            
        }
        
        // Set quad position two
        qdLoc = 1;
        if(weatherControl.size() >= 2) {
            ((TextView) rootView.findViewById(R.id.dayOfWeek2)).setText(
                    this.weatherControl.get(qdLoc).getDayOfWeek() );  
                ((TextView) rootView.findViewById(R.id.popAndTemp2)).setText(
                    this.weatherControl.get(qdLoc).getTemperature() +" : "+
                    weatherControl.get(qdLoc).getPop() );
                ((TextView) rootView.findViewById(R.id.wind2)).setText(
                        weatherControl.get(qdLoc).getWindSustained()
                        ); 
                int windDirId = rootView.getResources().getIdentifier(
                        "wind_dir_"+ weatherControl.get(qdLoc).getCompassDir().toLowerCase() +"_50", 
                        "drawable", "com.yanas.mobileapp.weathercast");

                if(windDirId != 0) {
                    ((ImageView) rootView.findViewById(
                            R.id.compass_dir_image2)).setImageResource(windDirId);
                }
                ((TextView) rootView.findViewById(R.id.gust2)).setText(
                           weatherControl.get(qdLoc).getWindGust()
                        );
                ((TextView) rootView.findViewById(R.id.sailingExperience2)).
                setText( weatherControl.get(qdLoc).getPredominantWx() );
        }
        
        // Set quad position three
        qdLoc = 2;
        if(weatherControl.size() >= 3) {
            ((TextView) rootView.findViewById(R.id.dayOfWeek3)).setText(
                    this.weatherControl.get(qdLoc).getDayOfWeek() );  
                ((TextView) rootView.findViewById(R.id.popAndTemp3)).setText(
                    this.weatherControl.get(qdLoc).getTemperature() +" : "+
                    weatherControl.get(qdLoc).getPop() );
                ((TextView) rootView.findViewById(R.id.wind3)).setText(
                        weatherControl.get(qdLoc).getWindSustained()
                        ); // + " "+ dateTime[0] +" ~ "+ dateTime[1].split("-")[0]);
                int windDirId = rootView.getResources().getIdentifier(
                        "wind_dir_"+ weatherControl.get(qdLoc).getCompassDir().toLowerCase() +"_50", 
                        "drawable", "com.yanas.mobileapp.weathercast");

                if(windDirId != 0) {
                    ((ImageView) rootView.findViewById(
                            R.id.compass_dir_image3)).setImageResource(windDirId);
                }
                ((TextView) rootView.findViewById(R.id.gust3)).setText(
                           weatherControl.get(qdLoc).getWindGust()
                        );
                ((TextView) rootView.findViewById(R.id.sailingExperience3)).
                setText( weatherControl.get(qdLoc).getPredominantWx() );
        }
        
        // Set quad position four
        qdLoc = 3;
        if(weatherControl.size() >= 4) {
            ((TextView) rootView.findViewById(R.id.dayOfWeek4)).setText(
                    this.weatherControl.get(qdLoc).getDayOfWeek() );  
                ((TextView) rootView.findViewById(R.id.popAndTemp4)).setText(
                    this.weatherControl.get(qdLoc).getTemperature() +" : "+
                    weatherControl.get(qdLoc).getPop() );
                ((TextView) rootView.findViewById(R.id.wind4)).setText(
                        weatherControl.get(qdLoc).getWindSustained()
                        );
                int windDirId = rootView.getResources().getIdentifier(
                        "wind_dir_"+ weatherControl.get(qdLoc).getCompassDir().toLowerCase() +"_50", 
                        "drawable", "com.yanas.mobileapp.weathercast");

                if(windDirId != 0) {
                    ((ImageView) rootView.findViewById(
                            R.id.compass_dir_image4)).setImageResource(windDirId);
                }
                ((TextView) rootView.findViewById(R.id.gust4)).setText(
                           weatherControl.get(qdLoc).getWindGust()
                        );
                ((TextView) rootView.findViewById(R.id.sailingExperience4)).
                setText( weatherControl.get(qdLoc).getPredominantWx() );
        }
        

        return rootView;
    }



    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }


}
