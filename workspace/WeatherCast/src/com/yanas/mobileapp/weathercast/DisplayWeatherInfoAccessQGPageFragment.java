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
public class DisplayWeatherInfoAccessQGPageFragment extends Fragment {
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
    public static DisplayWeatherInfoAccessQGPageFragment create(ArrayList<DisplayData> dataSelected_in) {
        DisplayWeatherInfoAccessQGPageFragment fragment = new DisplayWeatherInfoAccessQGPageFragment();
        Bundle args = new Bundle();
        
        args.putSerializable(STATIONDATA_DATETIME_ARG, dataSelected_in);
        fragment.setArguments(args);

        return fragment;
    }

    public DisplayWeatherInfoAccessQGPageFragment() {
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
        	Log.e("DisplayWeatherInfoAccessQGPageFragment", "getArguments() is null");
        }
    }

    
    /**
     * 
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                                Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.display_weather_qg_layout, container, false);

        // Set Header: city and zip
        if(weatherControl != null && weatherControl.get(0) != null && 
                weatherControl.get(0).getDateOfData() != null)
       
            ((TextView) rootView.findViewById(R.id.cityZip)).setText(StringUtils.createStationRow(weatherControl.get(0).getCity(), 
                weatherControl.get(0).getState(), 
                weatherControl.get(0).getZipcode())
                +" - "+ weatherControl.get(0).getTodaysDate());
        else
            ((TextView) rootView.findViewById(R.id.cityZip)).setText("Data Not Available");
            
        
        int qdLoc = 0;
        
        // Set quad position one
        int position = 1;
        if(weatherControl.size() >= position) {
            setFieldsBySection(rootView, position, position - 1);
        }
        
        // Set quad position two
        position = 2;
        if(weatherControl.size() >= position) {
            setFieldsBySection(rootView, position, position - 1);
        }
        
        
        // Set quad position three
        position = 3;
        if(weatherControl.size() >= position) {
            setFieldsBySection(rootView, position, position - 1);
        }
        
        
        // Set quad position four
        position = 4;
        if(weatherControl.size() >= position) {
            setFieldsBySection(rootView, position, position - 1);
        }
        
        return rootView;
    }



    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }


    private boolean setFieldsBySection(ViewGroup viewG, int fldSect, int index) {
        
        if(weatherControl == null || weatherControl.get(index) == null) {
            return false;
        }
        
        int dayOfWeek = getResources().getIdentifier(
                "dayOfWeek"+ fldSect, "id", viewG.getContext().getPackageName());
        int tempIcon = getResources().getIdentifier(
                "temp_icon"+ fldSect, "id", viewG.getContext().getPackageName());
        int tempNum = getResources().getIdentifier(
                "temp_num"+ fldSect, "id", viewG.getContext().getPackageName());
        int windImg = getResources().getIdentifier(
                "wind_image"+ fldSect, "id", viewG.getContext().getPackageName());
        int windDir = getResources().getIdentifier(
                "compass_dir_image"+ fldSect, "id", viewG.getContext().getPackageName());
        int predominant = getResources().getIdentifier(
                "weather_predominant"+ fldSect, "id", viewG.getContext().getPackageName());
        int temperatureId = viewG.getResources().getIdentifier(
                "temp_guage"+ weatherControl.get(index).getTemperatureIconLevel().toLowerCase(), 
                "drawable", "com.yanas.mobileapp.weathercast");

        // Temperature
        ((TextView) viewG.findViewById(dayOfWeek)).setText(
                this.weatherControl.get(index).getDayOfWeekAmPm() );  

        if(temperatureId != 0) {
            ((ImageView) viewG.findViewById(tempIcon)).
                        setImageResource(temperatureId);
        }
        
        ((TextView) viewG.findViewById(tempNum)).setText( weatherControl.get(index).getTemperature() );
        
        // Wind
        int windId = viewG.getResources().getIdentifier(
                "sailing_wind"+ weatherControl.get(index).getWindIconLevel().toLowerCase(), 
                "drawable", "com.yanas.mobileapp.weathercast");

        if(windId != 0) {
            ((ImageView) viewG.findViewById(
                    windImg)).setImageResource(windId);
        }
        
        int windDirId = viewG.getResources().getIdentifier(
                "wind_dir_"+ weatherControl.get(index).getCompassDir().toLowerCase() +"_50", 
                "drawable", "com.yanas.mobileapp.weathercast");

        if(windDirId != 0) {
            ((ImageView) viewG.findViewById(windDir)).
                    setImageResource(windDirId);
        }

        LayerDrawable layerDr = new LayerDrawable(
                DisplayWeatherInfoAccessPageFragment.getGraphicWeather(viewG, weatherControl.get(index), predominant)); 
        ((ImageView) viewG.findViewById(predominant)).setImageDrawable(layerDr);

        return true;

    }
}
