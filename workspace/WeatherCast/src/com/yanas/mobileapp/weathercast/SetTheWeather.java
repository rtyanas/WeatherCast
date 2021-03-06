package com.yanas.mobileapp.weathercast;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yanas.mobileapp.restaccess.RestClient.RequestMethod;

/*
 * Set up the weather data for request.  
 */
public class SetTheWeather {
	
	public RequestMethod method = RequestMethod.GET;
	public String url = "";
	public ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();
	public ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	
	public String observedData;

	//  8465705, "air_temperature", 2013-09-18T00:00, 2013-09-18T00:00
	public SetTheWeather setTheTide(String station, String observeProp, 
			String startDate_Time, String endDate_Time) {
		url = "http://opendap.co-ops.nos.noaa.gov/ioos-dif-sos/SOS";
		method = RequestMethod.GET;
		headers = new ArrayList<NameValuePair>();
		params = new ArrayList<NameValuePair>();
		observedData = observeProp;
		
		headers.add(new BasicNameValuePair("NOAA", "WeatherData") );
		
		params.add(new BasicNameValuePair("service", "SOS") );
		params.add(new BasicNameValuePair("request", "GetObservation") );
		params.add(new BasicNameValuePair("version", "1.0.0") );
		params.add(new BasicNameValuePair("observedProperty", observeProp) );
		// params.add(new BasicNameValuePair("observedProperty", "winds") );
		params.add(new BasicNameValuePair("offering", "urn:ioos:station:NOAA.NOS.CO-OPS:"+ station) );
		params.add(new BasicNameValuePair("responseFormat", "text/csv") );
		params.add(new BasicNameValuePair("eventTime", 
				startDate_Time +":00Z/"+ endDate_Time +":00Z") );
		
		return this;

	}
	
	
	//  8465705, "air_temperature", 2013-09-18T00:00, 2013-09-18T00:00
	public SetTheWeather setTheMeteorological(String endPoint1Lat, String endPoint1Lon, 
									 String beginDate, String endDate) {
		params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("lat", endPoint1Lat) );
		params.add(new BasicNameValuePair("lon", endPoint1Lon) );

		params.add(new BasicNameValuePair("begin", beginDate +":00") );
		params.add(new BasicNameValuePair("end",   endDate +":00") );
		
		return (setTheMeteorological(params));
	}
	
	
	public SetTheWeather setTheMeteorological(String zipCode, 
			 String beginDate, String endDate) {
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("zipCodeList", zipCode) );
		
		params.add(new BasicNameValuePair("begin", beginDate +":00") );
		params.add(new BasicNameValuePair("end",   endDate +":00") );
		
		return (setTheMeteorological(params));
	}
	
	
	// http://graphical.weather.gov/xml/sample_products/browser_interface/ndfdXMLclient.php?zipCodeList=20910+25414&product=time-series&begin=2004-01-01T00:00:00&end=2013-04-21T00:00:00&maxt=maxt&mint=mint

	private SetTheWeather setTheMeteorological(	ArrayList<NameValuePair> params) {
		url = "http://graphical.weather.gov/xml/sample_products/browser_interface/ndfdXMLclient.php";
		method = RequestMethod.GET;
		headers = new ArrayList<NameValuePair>();
		
		headers.add(new BasicNameValuePair("NOAA", "MeteorologicalWeatherData") );
		
		params.add(new BasicNameValuePair("product", "time-series") );
		
		params.add(new BasicNameValuePair("wx",    "wx") );
		params.add(new BasicNameValuePair("wspd",  "wspd") );
		params.add(new BasicNameValuePair("wdir",  "wdir") );
		params.add(new BasicNameValuePair("wgust", "wgust") );
		
		params.add(new BasicNameValuePair("temp",  "temp") );
		params.add(new BasicNameValuePair("maxt",  "maxt") );
		params.add(new BasicNameValuePair("mint",  "mint") );
		// params.add(new BasicNameValuePair("responseFormat", "text/csv") );

		params.add(new BasicNameValuePair("pop12", "pop12") );
		params.add(new BasicNameValuePair("sky", "sky") );
		return this;
	}

}

