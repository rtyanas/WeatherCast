package com.yanas.mobileapp.weathercast.parsexml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.util.Log;

import com.yanas.mobileapp.weathercast.GlobalSettings;
import com.yanas.mobileapp.weathercast.StationData;
import com.yanas.mobileapp.weathercast.WeatherCondDataValue;
import com.yanas.mobileapp.weathercast.WeatherDataValue;

public class WeatherDataParsed implements Serializable {

	private static final long serialVersionUID = 1L;
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
		
		if(stationData.getprobOfPrecip12().size() > 0) {
			for(LayoutAndDates lad : layoutAndDatesV) {
				if(lad.layout.equals(stationData.getprobOfPrecip12().get(0).getPeriod())) {
					copyPeriods(lad.startDate, stationData.getprobOfPrecip12());
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
	
	
	public List<DisplayData> generateDisplayDataList() {
		DisplayData dd = new DisplayData();
		
		return dd.generateDisplayDataList(this.stationData);
	}
	
	/*
	 * Date list to match weather data.
	 */
	public class LayoutAndDates implements Serializable {
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
	

	public class DisplayData implements Serializable {
		
		private static final long serialVersionUID = 1L;
		String city;
		String state;
		String zipcode;
		WeatherDataValue temperature;
		WeatherDataValue tempMax;
		WeatherDataValue tempMin;
		WeatherDataValue windSustained;
		WeatherDataValue windDirection;
		WeatherDataValue windGust;
		WeatherDataValue propPrecip12;
		WeatherCondDataValue weatherPredominant;
		public List<DisplayData> displayL;
		
		public DisplayData() {
			temperature = null;
			tempMax = null;
			tempMin = null;
			windSustained = null;
			windDirection = null;
			windGust = null;
			propPrecip12 = null;
			weatherPredominant = null;
			displayL = new ArrayList<DisplayData>();
		}
		
		public List<DisplayData> generateDisplayDataList(StationData statnDt) {
			
			DisplayData dd = null;
			
			// Add the first value in each vector since the temp 
			// period can be after the first period of the other weather values
			dd = new DisplayData();

			dd.city = statnDt.getCity();
			dd.state = statnDt.getState();
			dd.zipcode = statnDt.getZipcode();

			// ToDo Set the period on temperature here to
			// the smallest period in this display
			dd.temperature = statnDt.getTemperature(0);
			dd.tempMin = statnDt.getTemperatureMin(0);
			dd.tempMax = statnDt.getTemperatureMax(0);
			dd.windSustained = statnDt.getWindSustained(0);
			dd.windDirection = statnDt.getWindSustainedDirection(0);
			dd.windGust = statnDt.getWindGust(0);
			dd.windDirection = statnDt.getWindSustainedDirection(0);
			dd.propPrecip12 = statnDt.getprobOfPrecip12(0);
			dd.weatherPredominant = statnDt.getWeather(0);
			displayL.add(dd);

			for(WeatherDataValue wtemp : statnDt.getTemperature()) {
				dd = new DisplayData();

				dd.city = statnDt.getCity();
				dd.state = statnDt.getState();
				dd.zipcode = statnDt.getZipcode();

				dd.temperature = wtemp;
				// Compare dates (2013-11-01T02:00:00-04:00)
				// 2013-11-09T19:00:00-05:00
				for(WeatherDataValue w : statnDt.getTemperatureMin()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.tempMin = w;
						break;
					}
				}
				for(WeatherDataValue w : statnDt.getTemperatureMax()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.tempMax = w;
						break;
					}
				}
				for(WeatherDataValue w : statnDt.getWindSustained()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.windSustained = w;
						break;
					}
				}
				for(WeatherDataValue w : statnDt.getWindSustainedDirection()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.windDirection = w;
						break;
					}
				}
				for(WeatherDataValue w : statnDt.getWindGust()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.windGust = w;
						break;
					}
				}
				for(WeatherDataValue w : statnDt.getWindSustainedDirection()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.windDirection = w;
						break;
					}
				}
				for(WeatherDataValue w : statnDt.getprobOfPrecip12()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.propPrecip12 = w;
						break;
					}
				}
				for(WeatherCondDataValue w : statnDt.getWeather()) {
					if(wtemp.getPeriod().substring(0, 18).compareTo(w.getPeriod().substring(0, 18)) <= 0 ) {
						dd.weatherPredominant = w;
						break;
					}
				}
				displayL.add(dd);
			} // for(WeatherDataValue wtemp : statnDt.getTemperature())
			
			return displayL;
		}
		
		
		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getZipcode() {
			return zipcode;
		}

		public void setZipcode(String zipcode) {
			this.zipcode = zipcode;
		}

		public WeatherDataValue getTemperature() {
			return temperature;
		}

		public void setTemperature(WeatherDataValue temperature) {
			this.temperature = temperature;
		}

		public WeatherDataValue getTempMax() {
			return tempMax;
		}

		public void setTempMax(WeatherDataValue tempMax) {
			this.tempMax = tempMax;
		}

		public WeatherDataValue getTempMin() {
			return tempMin;
		}

		public void setTempMin(WeatherDataValue tempMin) {
			this.tempMin = tempMin;
		}

		public WeatherDataValue getWindSustained() {
			return windSustained;
		}

		public void setWindSustained(WeatherDataValue windSustained) {
			this.windSustained = windSustained;
		}

		public WeatherDataValue getWindDirection() {
			return windDirection;
		}

		public void setWindDirection(WeatherDataValue windDirection) {
			this.windDirection = windDirection;
		}

		public WeatherDataValue getWindGust() {
			return windGust;
		}

		public void setWindGust(WeatherDataValue windGust) {
			this.windGust = windGust;
		}

		public WeatherDataValue getPropPrecip12() {
			return propPrecip12;
		}

		public void setPropPrecip12(WeatherDataValue propPrecip12) {
			this.propPrecip12 = propPrecip12;
		}

		public WeatherCondDataValue getWeatherPredominant() {
			return weatherPredominant;
		}

		public void setWeatherPredominant(WeatherCondDataValue weatherPredominant) {
			this.weatherPredominant = weatherPredominant;
		}



	}
	
}
