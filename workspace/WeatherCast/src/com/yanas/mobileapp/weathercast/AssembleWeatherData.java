package com.yanas.mobileapp.weathercast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Pattern;

import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed;

import android.util.Log;

public class AssembleWeatherData {

	private String city;
	private String state;
	private String zipcode;
	private String latitude;
	private String longitude;
	
	boolean useZipcode = false;
	final int CITY = 0;
	final int STATE = 1;
	final int ZIPCODE = 2;
	final int LATITUDE = 3;
	final int LONGITUDE = 4;

	public AssembleWeatherData(String location_in) {
		city = "";
		state = "";
		zipcode = "";
		latitude = "0.0";
		longitude = "0.0";
		
		String stationData[] = location_in.split(",");
		Log.d("DisplayMessage", "location: "+ stationData);
		if(stationData.length >= 3) {
			city = stationData[CITY].trim();
			state = stationData[STATE].trim();
			zipcode = stationData[ZIPCODE].trim();
			if( (stationData[ZIPCODE] != null) && 
					((! stationData[ZIPCODE].equals("")) && (! stationData[ZIPCODE].equals("null"))) ) {
				useZipcode = true;
				Log.i("DisplayWeatherInfoActivity", "Using Zipcode");
			}
			else {
				Log.i("DisplayWeatherInfoActivity", "Zipcode not found, use lat/long");
			}
			
			if( ( ! useZipcode ) && stationData.length >= 5) {
				latitude = location_in.split(",")[3];
				longitude = location_in.split(",")[4];
			}
		}
	}
		
	
	/**
	 * Get the data 
	 * @return
	 */
	public WeatherDataParsed retrieveWeather() {
		int sizeRandom = 10;
		
		/* hard code station for Testing */
		String station = "8465705"; // New Haven, Conn - air temp, wind
		// station = "8531680"; // Sandy Hook - Tide info 
		// station = "8531833"; // Navesink tide info
		// station = "8531223";  // Cheesequake tide info
		// station = "8531545";  // Keyport tide info

		Date nowDate = new Date();
		// Use Locale.US since the HTTP Get request expects that format.
		SimpleDateFormat ft = new SimpleDateFormat("yyy-MM-dd'T'HH:", Locale.US);
		SimpleDateFormat ftGmt = new SimpleDateFormat("yyy-MM-dd'T'HH:", Locale.US); 
		ftGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		Log.d("DisplayMessage", "Date: "+ft.format(nowDate) );
		// DisplayMessage(17332): Date: 2013-10-01T10:44
		
		Pattern pNums = Pattern.compile("^[0-9.]*$");

		SetTheWeather weatherReq = new SetTheWeather();
		weatherReq.setTheTide(
				station, "air_temperature", 
				ftGmt.format(nowDate)+"00", ftGmt.format(nowDate)+"00");

		WeatherData weatherData = new WeatherData( );
//		String temperature = weatherData.getObservedPropertyTide(weatherReq);
//				
//		if(pNums.matcher(temperature).find() )
//		{
//			double round = 0.5;
//			Double tempD =  Double.valueOf(temperature);
//			tempD = (tempD * 9.0/5.0 + 32) + round;
//			temperature = String.valueOf(tempD.intValue());
//		}
//		
//		weatherReq.setTheTide(
//				station, "winds", 
//				ftGmt.format(nowDate)+"00", ftGmt.format(nowDate)+"00");

//		String winds = weatherData.getObservedPropertyTide(weatherReq);
//		String windDir="Not Available";
//		String windSpeed = "Not Available";
//		String windGust = "Not Available";
//		if(winds.split(",").length >= 3) {
//			double ms_knotsConversionFactor = 1.94384449244;
//			int i=0;
//			for(String w : winds.split(",")) {
//				if(pNums.matcher(w).find() && i == 0 ) // Found wind direction
//				{
//					windDir = w;
//				} 
//				else if(pNums.matcher(w).find() && i == 1 ) // Found wind speed
//				{
//					windSpeed = String.format("%.2f", Double.valueOf((Double.valueOf(w) * ms_knotsConversionFactor)));
//				}
//				else if(pNums.matcher(w).find() && i == 2 ) // Found wind Gust speed
//				{
//					windGust = String.format("%.2f", Double.valueOf((Double.valueOf(w) * ms_knotsConversionFactor)));
//				}
//				i++;
//			}
//		}
		
		final int ONE_HOUR = 60 * 60 * 1000; // in milliseconds.
		final long ONE_DAY = ONE_HOUR * 24;
        long NUMBER_OF_DAYS = ONE_DAY * 5;
		
		if(useZipcode) {
			weatherReq.setTheMeteorological(
					// South Amboy 08879 //  Long Branch
					zipcode,
					ft.format(nowDate)+"00", 
					ft.format(nowDate.getTime() + NUMBER_OF_DAYS  /* add one hour */)+"00");			
		}
		else {  // use lat/lon
			weatherReq.setTheMeteorological(
					// "40.4867", "-74.2790", // South Amboy  // "40.2954", "-73.9899",  Long Branch
					latitude, longitude,
					ft.format(nowDate)+"00", 
					ft.format(nowDate.getTime() + NUMBER_OF_DAYS  /* add one hour */)+"00");
		}
		WeatherDataParsed wdp = weatherData.getObservedPropertyMeteorological(
				weatherReq, 
				city, state, zipcode, latitude, longitude);
				
//		// Test data
//		for(int i=0; i < sizeRandom; i++ ) {
//			stationDataV.add(stationData);
//		}
		
		return wdp;
		
	}

	
}
