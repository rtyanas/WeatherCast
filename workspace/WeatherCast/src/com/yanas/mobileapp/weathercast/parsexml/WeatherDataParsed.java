package com.yanas.mobileapp.weathercast.parsexml;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.util.Log;

import com.yanas.mobileapp.weathercast.GlobalSettings;
import com.yanas.mobileapp.weathercast.StationData;
import com.yanas.mobileapp.weathercast.WeatherCondDataValue;
import com.yanas.mobileapp.weathercast.WeatherDataValue;

public class WeatherDataParsed {

	public StationData stationData;
	public Vector<LayoutAndDates> layoutAndDatesV;
	
	public WeatherDataParsed() {
		stationData = new StationData();
		layoutAndDatesV = new Vector<LayoutAndDates>();
	}
	
	
	public void updateWeatherDataPeriod() {
		
		if(stationData.getTemperature().size() > 0) {
			for(LayoutAndDates lad : layoutAndDatesV) {
				if(lad.layout.equals(stationData.getTemperature().get(0).getPeriod())) {
					copyPeriods(lad.startDate, stationData.getTemperature());
				}
			}
		}
		
		if(stationData.getProbOfPrecip12().size() > 0) {
			for(LayoutAndDates lad : layoutAndDatesV) {
				if(lad.layout.equals(stationData.getProbOfPrecip12().get(0).getPeriod())) {
					copyPeriods(lad.startDate, stationData.getProbOfPrecip12());
				}
			}
		}

		if(stationData.getTemperatureMax().size() > 0) {
			for(LayoutAndDates lad : layoutAndDatesV) {
				if(lad.layout.equals(stationData.getTemperatureMax().get(0).getPeriod())) {
					copyPeriods(lad.startDate, stationData.getTemperatureMax());
				}
			}
		}

		if(stationData.getTemperatureMin().size() > 0) {
			for(LayoutAndDates lad : layoutAndDatesV) {
				if(lad.layout.equals(stationData.getTemperatureMin().get(0).getPeriod())) {
					copyPeriods(lad.startDate, stationData.getTemperatureMin());
				}
			}
		}

		if(stationData.getWindGust().size() > 0) {
			for(LayoutAndDates lad : layoutAndDatesV) {
				if(lad.layout.equals(stationData.getWindGust().get(0).getPeriod())) {
					copyPeriods(lad.startDate, stationData.getWindGust());
				}
			}
		}

		if(stationData.getWindSustained().size() > 0) {
			for(LayoutAndDates lad : layoutAndDatesV) {
				if(lad.layout.equals(stationData.getWindSustained().get(0).getPeriod())) {
					copyPeriods(lad.startDate, stationData.getWindSustained());
				}
			}
		}

		if(stationData.getWindSustainedDirection().size() > 0) {
			for(LayoutAndDates lad : layoutAndDatesV) {
				if(lad.layout.equals(stationData.getWindSustainedDirection().get(0).getPeriod())) {
					copyPeriods(lad.startDate, stationData.getWindSustainedDirection());
				}
			}
		}

		if(stationData.getWeather().size() > 0) {
			for(LayoutAndDates lad : layoutAndDatesV) {
				if(lad.layout.equals(stationData.getWeather().get(0).getPeriod())) {
					copyPeriodsWeather(lad.startDate, stationData.getWeather());
				}
			}
		}

	}
	
	public void copyPeriods(Vector<String> startDateV, Vector<WeatherDataValue> weatherV) {
		if(startDateV.size() != weatherV.size()) {
			if(GlobalSettings.weatherDataParsed)
				Log.e("WeatherDataParsed", "Layout Date lists different sizes, weather type: "+ 
															weatherV.getClass().getName());
			return;
		}
		
		int i = 0;
		for(String date : startDateV) {
			weatherV.get(i).setPeriod(date);
			i++;
		}
	}
	
	public void copyPeriodsWeather(Vector<String> startDateV, Vector<WeatherCondDataValue> weatherV) {
		if(startDateV.size() != weatherV.size()) {
			if(GlobalSettings.weatherDataParsed)
				Log.e("WeatherDataParsed", "Layout Date lists different sizes, weather type: "+ 
															weatherV.getClass().getName() +", these will not match, copy date anyway.");
		}
		
		int i = 0;
		for(String date : startDateV) {
			// Go until the end of weatherV is reached since they do not match
			// due to "weather-condition" having a "VALUE" partially. 
			if(i >= weatherV.size())
				break;
			weatherV.get(i).setPeriod(date);
			i++;
		}
	}
	
	/*
	 * Date list to match weather data.
	 */
	public class LayoutAndDates {
		private String layout;
		Vector<String> startDate;
		Vector<String> endDate;
		
		LayoutAndDates() {
			layout = "";
			startDate = new Vector<String>();
			endDate = new Vector<String>();			
		}
		
		public void clear() {
			layout = "";
			startDate.clear();
			endDate.clear();
		}
		
		public LayoutAndDates copy() {
			LayoutAndDates copy = new LayoutAndDates();
			
			copy.layout = new String(this.layout);

			for(String s : this.startDate) {
				copy.startDate.add(new String(s));				
			}
			
			for(String s : this.endDate) {
				copy.endDate.add(new String(s));				
			}
			
			return copy;
		}

		public String getLayout() {
			return layout;
		}

		public void setLayout(String layout) {
			this.layout = layout;
		}
	} // LayoutAndDates
	

	public class DisplayData {
		
		String dateTime;
		String temperature;
		String tempMax;
		String tempMin;
		String windSustained;
		String windDirection;
		String windGust;
		String propPrecip12;
		String weatherPredominant;
		public List<DisplayData> displayL;
		
		public DisplayData() {
			dateTime = "";
			temperature = "";
			tempMax = "";
			tempMin = "";
			windSustained = "";
			windDirection = "";
			windGust = "";
			weatherPredominant = "";
			displayL = new ArrayList<DisplayData>();
		}
		
		public List<DisplayData> generateDisplayDataList(StationData statnDt) {
			
			DisplayData dd = null;
			for(WeatherDataValue wtemp : statnDt.getTemperature()) {
				dd = new DisplayData();
				dd.dateTime = wtemp.getPeriod();
				dd.temperature = wtemp.getValue();
				// Compare dates (2013-11-01T02:00:00-04:00)
				for(WeatherDataValue w : statnDt.getTemperatureMin()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.tempMin = w.getValue();
						break;
					}
				}
				for(WeatherDataValue w : statnDt.getTemperatureMax()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.tempMax = w.getValue();
						break;
					}
				}
				for(WeatherDataValue w : statnDt.getWindSustained()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.windSustained = w.getValue();
						break;
					}
				}
				for(WeatherDataValue w : statnDt.getWindSustainedDirection()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.windDirection = w.getValue();
						break;
					}
				}
				for(WeatherDataValue w : statnDt.getWindGust()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.windGust = w.getValue();
						break;
					}
				}
				for(WeatherDataValue w : statnDt.getWindSustainedDirection()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.windDirection = w.getValue();
						break;
					}
				}
				for(WeatherDataValue w : statnDt.getProbOfPrecip12()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.windDirection = w.getValue();
						break;
					}
				}
				for(WeatherCondDataValue w : statnDt.getWeather()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.weatherPredominant = w.getCoverage() +","+ w.getIntensity() +","+ 
											    w.getWeather_type() +","+ w.getQualifier();
						break;
					}
				}
				displayL.add(dd);
			} // for(WeatherDataValue wtemp : statnDt.getTemperature())
			
			return displayL;
		}
		
		
		public String getDateTime() {
			return dateTime;
		}

		public void setDateTime(String dateTime) {
			this.dateTime = dateTime;
		}

		public String getTemperature() {
			return temperature;
		}

		public void setTemperature(String temperature) {
			this.temperature = temperature;
		}

		public String getTempMax() {
			return tempMax;
		}

		public void setTempMax(String tempMax) {
			this.tempMax = tempMax;
		}

		public String getTempMin() {
			return tempMin;
		}

		public void setTempMin(String tempMin) {
			this.tempMin = tempMin;
		}

		public String getWindSustained() {
			return windSustained;
		}

		public void setWindSustained(String windSustained) {
			this.windSustained = windSustained;
		}

		public String getWindDirection() {
			return windDirection;
		}

		public void setWindDirection(String windDirection) {
			this.windDirection = windDirection;
		}

		public String getWindGust() {
			return windGust;
		}

		public void setWindGust(String windGust) {
			this.windGust = windGust;
		}

		public String getPropPrecip12() {
			return propPrecip12;
		}

		public void setPropPrecip12(String propPrecip12) {
			this.propPrecip12 = propPrecip12;
		}

		public String getWeatherPredominant() {
			return weatherPredominant;
		}

		public void setWeatherPredominant(String weatherPredominant) {
			this.weatherPredominant = weatherPredominant;
		}


	}
	
}
