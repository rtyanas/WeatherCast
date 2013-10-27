package com.yanas.mobileapp.weathercast;

import java.io.Serializable;

public class StationData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String city;
	private String state;
	private String zipcode;
	private String latitude;
	private String longitude;
	
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	
	private WeatherDataValue temperature;
	private WeatherDataValue temperatureMin;
	private WeatherDataValue temperatureMax;
	private WeatherDataValue predominant;
	private WeatherDataValue percentPercip;
	private WeatherDataValue windSustained;
	private WeatherDataValue windSustainedDirection;
	private WeatherDataValue windGust;
	private WeatherDataValue tideLevel;
	private WeatherDataValue tideDirection;
	private WeatherDataValue weather;
	private WeatherDataValue propOfPrecip12;
	
	
	
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

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public WeatherDataValue getWeather() {
		return weather;
	}

	public void setWeather(WeatherDataValue weather) {
		this.weather = weather;
	}

	public WeatherDataValue getPropOfPrecip12() {
		return propOfPrecip12;
	}

	public void setPropOfPrecip12(WeatherDataValue propOfPrecip12) {
		this.propOfPrecip12 = propOfPrecip12;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public StationData() {
		city = "";
		state = "";
		zipcode = "";
		latitude = "";
		longitude = "";
		startDate = "";
		startTime = "";
		endDate = "";
		endTime = "";
	}
	
	public StationData(	String city_in,
			String state_in, String zipcode_in,
			String latitude_in, String longitude_in) {
		city = city_in;
		state = state_in;
		zipcode = zipcode_in;
		latitude = latitude_in;
		longitude = longitude_in;		
		startDate = "";
		endDate = "";
	}
	
	public WeatherDataValue getTemperature() {
		return temperature;
	}
	public void setTemperature(WeatherDataValue temperature) {
		this.temperature = temperature;
	}
	public WeatherDataValue getTemperatureMin() {
		return temperatureMin;
	}
	public void setTemperatureMin(WeatherDataValue temperatureMin) {
		this.temperatureMin = temperatureMin;
	}
	public WeatherDataValue getTemperatureMax() {
		return temperatureMax;
	}
	public void setTemperatureMax(WeatherDataValue temperatureMax) {
		this.temperatureMax = temperatureMax;
	}
	public WeatherDataValue getPredominant() {
		return predominant;
	}
	public void setPredominant(WeatherDataValue predominant) {
		this.predominant = predominant;
	}
	public WeatherDataValue getPercentPercip() {
		return percentPercip;
	}
	public void setPercentPercip(WeatherDataValue percentPercip) {
		this.percentPercip = percentPercip;
	}
	public WeatherDataValue getWindSustained() {
		return windSustained;
	}
	public void setWindSustained(WeatherDataValue windSustained) {
		this.windSustained = windSustained;
	}
	public WeatherDataValue getWindSustainedDirection() {
		return windSustainedDirection;
	}
	public void setWindSustainedDirection(WeatherDataValue windSustainedDirection) {
		this.windSustainedDirection = windSustainedDirection;
	}
	public WeatherDataValue getWindGust() {
		return windGust;
	}
	public void setWindGust(WeatherDataValue windGust) {
		this.windGust = windGust;
	}
	public WeatherDataValue getTideLevel() {
		return tideLevel;
	}
	public void setTideLevel(WeatherDataValue tideLevel) {
		this.tideLevel = tideLevel;
	}
	public WeatherDataValue getTideDirection() {
		return tideDirection;
	}
	public void setTideDirection(WeatherDataValue tideDirection) {
		this.tideDirection = tideDirection;
	}

//	String s = "New Haven, Conn\nTemperature: "+ temperature +"F"+
//			"\nWinds direction: "+ windDir +"\n        speed: "+  windSpeed +"knt\n        Gust: "+ windGust +"knt"+
//			"\n\n+=+="+ city +", "+ state +
//			"\nTemp: "+ stationData.getTemperature().getValue() +
//			"\nT Min: "+ stationData.getTemperatureMin().getValue() +
//			"\nT Max: "+ stationData.getTemperatureMax().getValue() +
//			"\nWind: "+ stationData.getWindSustained().getValue() +
//			"\nWind Dir: "+ stationData.getWindSustainedDirection().getValue() +
//			"\nGust: "+ stationData.getWindGust().getValue() +"\n"
//			
//			;

	public String toString() {
		return 	"City: "+ city +"- state: "+ state +"- zip: "+ zipcode + "\n"+
				"Date: Start"+ getStartDate() +" End: "+ getEndDate() +"\n"+
				"Temp: "+ getTemperature().getValue() +
				", T Min: "+ getTemperatureMin().getValue() +
				", T Max: "+ getTemperatureMax().getValue() +
				"\nWind: "+ getWindSustained().getValue() +
				", Wind Dir: "+ getWindSustainedDirection().getValue() +
				" Gust: "+ getWindGust().getValue() +"\n"+
				"Weather: "+ getWeather().getValue() +" POP: "+ getPropOfPrecip12().getValue() +"\n"
				
				;

	}
}
