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
public class WeatherDataControl  {

    public static final String ARG_TEMP = "temp";
    public static final String STATIONDATA_DATETIME_ARG = "station_datetime_Data";

    DisplayData displayData;
    
    // 2013-10-24T08:00:00-04:00
    SimpleDateFormat sdfInput = new SimpleDateFormat( // input parsing
            "yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
    SimpleDateFormat sdfDisplay = new SimpleDateFormat( // output GUI
            "MMM d", Locale.US);
    SimpleDateFormat sdfSunCheck = new SimpleDateFormat( // output GUI
            "HH", Locale.US);
    SimpleDateFormat sdfMonth = new SimpleDateFormat( // output GUI
            "MM", Locale.US);
    SimpleDateFormat sdfDayOfWeek = new SimpleDateFormat( // output GUI
            "E", Locale.US);
    SimpleDateFormat sdfDayOfWeekAmPm = new SimpleDateFormat( // output GUI
            "E h a", Locale.US);

    String todaysDate = "Today's Date and Time";
    public String getTodaysDate() {
        return todaysDate;
    }

    Integer hour  = 0;
    Integer month = 0;
    String dayOfWeek = "Day of week";
    String dayOfWeekAmPm = "Day of week";
    
    // This date used to calculate the day and date in header and for moon phase
    Date dateOfData = new Date();
    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public WeatherDataControl (DisplayData displayData_in) {
        displayData = displayData_in;
        try {
            dateOfData = sdfInput.parse(displayData.getTemperature().getPeriod());
            Log.d("DisplayWeatherInfoAccessPageFragmnt", "Date: "+ dateOfData);
            todaysDate = sdfDisplay.format(dateOfData);
            hour  = Integer.parseInt(sdfSunCheck.format(dateOfData));
            month = Integer.parseInt(sdfMonth.format(dateOfData));
            dayOfWeek = sdfDayOfWeek.format(dateOfData) +" "+ hour.toString() +":00";
            dayOfWeekAmPm = sdfDayOfWeekAmPm.format(dateOfData) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    Date getDateOfData() {
        Date dateOfD = null;
        try {
            dateOfD = sdfInput.parse(displayData.getTemperature().getPeriod());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateOfD;
    }
    
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getDayOfWeekAmPm() {
        return dayOfWeekAmPm;
    }
    
    public String getCity() {
        return displayData.getCity(); 
    }
    
    public String getState() {
        return displayData.getState();
    }
    
    public String getZipcode() {
        return displayData.getZipcode();
    }
    

    public String getTemperature() {
        String returnStr = displayData.getTemperature().getValue() +" "+ 
        (displayData.getTemperature().getUnits().length() >= 1 ?
                displayData.getTemperature().getUnits() : "");
        return returnStr;
        
    }
    
    public String getTemperatureMin() {
        String tMin = "Min Not Avail";

        if(displayData.getTempMin() != null) {
            tMin = "Min "+displayData.getTempMin().getValue() +" "+ 
                    (displayData.getTempMin().getUnits().length() >= 1 ?
                            displayData.getTempMin().getUnits() : "");
        }

        return tMin;
    }

    
    public String getTemperatureMax() {
        String tMax = "Max Not Avail";
        
        if(displayData.getTempMax() != null ) {
            tMax = "Max "+displayData.getTempMax().getValue() +" "+ 
                    (displayData.getTempMax().getUnits().length() >= 1 ?
                           displayData.getTempMax().getUnits() : "");
        }
        return tMax;
    }

    
    public String getTemperatureIconLevel() {
        String returnStr = "Not Avail";
        if(displayData.getTemperature() != null ) {
            try {
                returnStr = 
                        getTemperatureValue(Integer.parseInt(displayData.getTemperature().getValue()) );      
            } catch(NumberFormatException nfe) {
                if(GlobalSettings.display_weatherInfo_access_pagefrag) 
                    Log.e("DisplayWeatherInfoAccessPageFragment", "Temperature number parse error.");
            }
        }
        
        return returnStr;
    }
    
    public String getWindIconLevel() {
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
        
        return windIconStr;
        
    }
    

    public String getWindSustained() {
        String wSus = "Wind Not Avail";
                
        if(displayData.getWindSustained() != null) {
            wSus = "Wind "+displayData.getWindSustained().getValue() +" "+ 
                    displayData.getWindSustained().getUnits();
        }
        return wSus;
    }


    public String getWindSustainedMph() {
        String wSus = "Wind Not Avail";
        int mph = 0;
                
        if(displayData.getWindSustained() != null) {
            if("kts".equalsIgnoreCase(displayData.getWindSustained().getUnits()) ) {
                mph = (int) Math.ceil(Integer.parseInt(displayData.getWindSustained().getValue()  ) * 0.621371  );
            }
                    
            wSus = "Wind "+ mph +" MPH";
        }
        return wSus;
    }


    public String getWindGust() {
        String wGust = "Gust Not Avail";
        
        if(displayData.getWindGust() != null) {
            wGust = "Gust "+displayData.getWindGust().getValue() +" "+ 
                    displayData.getWindGust().getUnits();
        }
        return wGust;
    }

    public String getWindGustMph() {
        String wGust = "Gst Not Avl";
        int mph = 0;
        
        if(displayData.getWindGust() != null) {
            if("kts".equalsIgnoreCase(displayData.getWindGust().getUnits()) ) {
                mph = (int) Math.ceil(Integer.parseInt(displayData.getWindGust().getValue()  ) * 0.621371  );
            }
            wGust = "Gust "+mph +" MPH";
        }
        return wGust;
    }

    public String getCompassDir() {
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

        return compassDir;
    }

    public String getWindDir() {
        String wDir = "Direction Not Avail";
        
        if(displayData.getWindDirection() != null) {
            wDir = "Direction "+displayData.getWindDirection().getValue() +" "+ 
                    displayData.getWindDirection().getUnits();
        }

        return wDir;
    }

    public String getPop() {
        String pop = "POP Not Available";
        
        if(displayData.getPropPrecip12() != null) {
            pop = displayData.getPropPrecip12().getValue() +
            displayData.getPropPrecip12().getUnits() +
            " Precip"; 
        }
                   ;
        return pop;
    }
    
    public String getCloudAmount() {
        String cloudAmount = "Cloud Coverage Not Available";
        if(displayData.getCloudAmount() != null) {
            cloudAmount = displayData.getCloudAmount().getValue() +
                    displayData.getCloudAmount().getUnits() +
                    " Cloud Cover ";
        }
        return cloudAmount;
    }
    
    public String getCloudAmount(String description) {
        String cloudAmount = "Cloud Coverage Not Available";
        if(displayData.getCloudAmount() != null) {
            cloudAmount = displayData.getCloudAmount().getValue() +
                    displayData.getCloudAmount().getUnits() +
                    description;
        }
        return cloudAmount;
    }
    
    public String getPredominantWx() {
        String wx = "";
        String comma = ", ";
        
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
            } else if(checkIfClear()  ) {
                wx = "Clear";
            }
        } else {
            if(checkIfClear()  ) {
                wx = "Clear";
            }
        }

        return wx;
    }
    
    public String getPredominantWxType() {
        String wxType = "";
        if(displayData.getWeatherPredominant() != null) {
            wxType = displayData.getWeatherPredominant().getWeather_type() == null ? "" : displayData.getWeatherPredominant().getWeather_type();
        }
        return wxType;
    }
    
    
    public boolean isMoonRising() {
        return this.isMoonRise(month, hour);
    }


    public String getCloudLevel() {
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
        
        return cloudConfigStr;
    }
    
    
    public String getRainSnowIceLevel() {
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
        return rainConfigStr;
    }
    

    private boolean checkIfClear() {
        boolean isClear = false;
        if(displayData.getCloudAmount() != null  ) {
            try { 
                int cloudAmountWx = Integer.parseInt(displayData.getCloudAmount().getValue());
                if(cloudAmountWx < 25)
                    isClear = true;
            } catch(NumberFormatException  nfe) {
                Log.e("WeatherDataControl.checkIfClear", "Clound Amount for Wx clear prediction number parse error.");
            }                
        }

        return isClear;
    }
    
    
    /**
     * Return if it is night depending on the month
     * Month     Sunset   Sunrise
     * January   17pm      7am
     * February  18pm      7am 
     * March     19pm      7am
     * April     20pm      6am
     * May       20pm      6am
     * June      21pm      5am
     * July      20pm      6am
     * August    20pm      6am
     * September 19pm      7am
     * October   18pm      7am
     * November  17pm      7am
     * December  17pm      7am
     * 
     * @param hour
     * @return
     */
    private boolean isMoonRise(int month, int hour) {
        boolean isNight = false;

        Log.d("isMoonRise", "Month: "+ month +", hour: "+ hour);
        
        if(month == 1)
            isNight = (hour > 17 || hour <= 7);
        else if(month == 2)
            isNight = (hour > 18 || hour <= 7);
        else if(month == 3)
            isNight = (hour > 19 || hour <= 7);
        else if(month == 4)
            isNight = (hour > 20 || hour <= 6);
        else if(month == 5)
            isNight = (hour > 20 || hour <= 6);
        else if(month == 6)
            isNight = (hour > 21 || hour <= 5);
        else if(month == 7)
            isNight = (hour > 20 || hour <= 6);
        else if(month == 8)
            isNight = (hour > 20 || hour <= 6);
        else if(month == 9)
            isNight = (hour > 19 || hour <= 7);
        else if(month == 10)
            isNight = (hour > 18 || hour <= 7);
        else if(month == 11)
            isNight = (hour > 17 || hour <= 7);
        else if(month == 12)
            isNight = (hour > 17 || hour <= 7);
        else
            isNight = (hour > 19 || hour <= 7);  // Default to March / September

            
        return isNight;
    }
    
    
    private String getTemperatureValue(int temperature_in) {
        String temperatureRet = "";
        
        if(temperature_in > 92) {
            temperatureRet = "8";  // Explosion
        } else if(temperature_in <= 92 && temperature_in > 85 ) {
            temperatureRet = "7";  // Yellow center
        } else if(temperature_in <= 85 && temperature_in > 78 ) {
            temperatureRet = "6";  // Orange center
        } else if(temperature_in <= 78 && temperature_in > 65 ) {
            temperatureRet = "5";  // Red / orange
        } else if(temperature_in <= 65 && temperature_in > 50 ) {
            temperatureRet = "4";  // Orange
        } else if(temperature_in <= 50 && temperature_in > 35 ) {
            temperatureRet = "3";  // Light Orange
        } else if(temperature_in <= 35 && temperature_in > 25 ) {
            temperatureRet = "2";  // Blue
        } else if(temperature_in <= 25 && temperature_in > 15 ) {
            temperatureRet = "1";  // Snow flake
        } else if(temperature_in <= 15 ) {
            temperatureRet = "0";  // Frozen Thermometer
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
    
    
    public String estimatedSailingExperience() {
        return this.estimatedSailingExperience(displayData);
       
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
    
    public int getCurrentMoonPhaseResource() {
        return this.getCurrentMoonPhaseResource(this.dateOfData);
    }
    
    
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
