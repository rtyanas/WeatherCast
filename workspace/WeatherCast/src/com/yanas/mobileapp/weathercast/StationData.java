package com.yanas.mobileapp.weathercast;

import java.io.Serializable;
import java.util.Vector;

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
	
	private Vector<WeatherDataValue> temperature;
	private Vector<WeatherDataValue> temperatureMin;
	private Vector<WeatherDataValue> temperatureMax;
	private Vector<WeatherDataValue> predominant;
	private Vector<WeatherDataValue> windSustained;
	private Vector<WeatherDataValue> windSustainedDirection;
	private Vector<WeatherDataValue> windGust;
	private Vector<WeatherDataValue> tideLevel;
	private Vector<WeatherDataValue> tideDirection;
	private Vector<WeatherCondDataValue> weather;
	private Vector<WeatherDataValue> probOfPrecip12;
	private Vector<WeatherDataValue> cloudAmount;
	
	
	
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
		initValues();
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
		initValues();
	}
	
	private void initValues() {
		temperature = new Vector<WeatherDataValue>();
		temperatureMin = new Vector<WeatherDataValue>();
		temperatureMax = new Vector<WeatherDataValue>();
		predominant = new Vector<WeatherDataValue>();
		windSustained = new Vector<WeatherDataValue>();
		windSustainedDirection = new Vector<WeatherDataValue>();
		windGust = new Vector<WeatherDataValue>();
		tideLevel = new Vector<WeatherDataValue>();
		tideDirection = new Vector<WeatherDataValue>();
		weather = new Vector<WeatherCondDataValue>();
		probOfPrecip12 = new Vector<WeatherDataValue>();
		cloudAmount = new Vector<WeatherDataValue>();

	}
	
	public Vector<WeatherDataValue> getTemperature() {
		return temperature;
	}

	public Vector<WeatherDataValue> getTemperatureMin() {
		return temperatureMin;
	}

	public Vector<WeatherDataValue> getTemperatureMax() {
		return temperatureMax;
	}

	public Vector<WeatherDataValue> getPredominant() {
		return predominant;
	}

	public Vector<WeatherDataValue> getWindSustained() {
		return windSustained;
	}

	public Vector<WeatherDataValue> getWindSustainedDirection() {
		return windSustainedDirection;
	}

	public Vector<WeatherDataValue> getWindGust() {
		return windGust;
	}

	public Vector<WeatherDataValue> getprobOfPrecip12() {
		return probOfPrecip12;
	}

	public Vector<WeatherDataValue> getCloudAmount() {
		return cloudAmount;
	}

	public Vector<WeatherCondDataValue> getWeather() {
		return weather;
	}

	public Vector<WeatherDataValue> getTideLevel() {
		return tideLevel;
	}

	public Vector<WeatherDataValue> getTideDirection() {
		return tideDirection;
	}

	public WeatherDataValue getTemperature(int i) {
		if(temperature.size() < 1) {
			return new WeatherDataValue();
		}
		return temperature.get(i);
	}
	public void setTemperature(WeatherDataValue temperature) {
		this.temperature.add(temperature);
	}
	public WeatherDataValue getTemperatureMin(int i) {
		if(temperatureMin.size() < 1) {
			return new WeatherDataValue();
		}
		return temperatureMin.get(i);
	}
	public void setTemperatureMin(WeatherDataValue temperatureMin) {
		this.temperatureMin.add(temperatureMin);
	}
	public WeatherDataValue getTemperatureMax(int i) {
		if(temperatureMax.size() < 1) {
			return new WeatherDataValue();
		}
		return temperatureMax.get(i);
	}
	public void setTemperatureMax(WeatherDataValue temperatureMax) {
		this.temperatureMax.add(temperatureMax);
	}
	public WeatherDataValue getPredominant(int i) {
		if(predominant.size() < 1) {
			return new WeatherDataValue();
		}
		return predominant.get(i);
	}
	public void setPredominant(WeatherDataValue predominant) {
		this.predominant.add(predominant);
	}
	public WeatherDataValue getprobOfPrecip12(int i) {
		if(probOfPrecip12.size() < 1) {
			return new WeatherDataValue();
		}
		return probOfPrecip12.get(i);
	}
	public WeatherDataValue getCloudAmount(int i) {
		if(cloudAmount.size() < 1) {
			return new WeatherDataValue();
		}
		return cloudAmount.get(i);
	}

	public void setprobOfPrecip12(WeatherDataValue probOfPrecip12) {
		this.probOfPrecip12.add(probOfPrecip12);
	}

	public void setCloudAmount(WeatherDataValue cloudAmount) {
		this.cloudAmount.add(cloudAmount);
	}

	public WeatherDataValue getWindSustained(int i) {
		if(windSustained.size() < 1) {
			return new WeatherDataValue();
		}
		return windSustained.get(i);
	}
	public void setWindSustained(WeatherDataValue windSustained) {
		this.windSustained.add(windSustained);
	}
	public WeatherDataValue getWindSustainedDirection(int i) {
		if(windSustainedDirection.size() < 1) {
			return new WeatherDataValue();
		}
		return windSustainedDirection.get(i);
	}
	public void setWindSustainedDirection(WeatherDataValue windSustainedDirection) {
		this.windSustainedDirection.add(windSustainedDirection);
	}
	public WeatherDataValue getWindGust(int i) {
		if(windGust.size() < 1) {
			return new WeatherDataValue();
		}
		return windGust.get(i);
	}
	public void setWindGust(WeatherDataValue windGust) {
		this.windGust.add(windGust);
	}

	public WeatherCondDataValue getWeather(int i) {
		if(weather.size() < 1) {
			return new WeatherCondDataValue();
		}
		return weather.get(i);
	}

	public void setWeather(WeatherCondDataValue weather) {
		this.weather.add(weather);
	}

	public WeatherDataValue getTideLevel(int i) {
		if(tideLevel.size() < 1) {
			return new WeatherDataValue();
		}
		return tideLevel.get(i);
	}
	public void setTideLevel(WeatherDataValue tideLevel) {
		this.tideLevel.add(tideLevel);
	}
	public WeatherDataValue getTideDirection(int i) {
		if(tideDirection.size() < 1) {
			return new WeatherDataValue();
		}
		return tideDirection.get(i);
	}
	public void setTideDirection(WeatherDataValue tideDirection) {
		this.tideDirection.add(tideDirection);
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
				"Temp: "+ getTemperature(0).getValue() +
				", T Min: "+ getTemperatureMin(0).getValue() +
				", T Max: "+ getTemperatureMax(0).getValue() +
				"\nWind: "+ getWindSustained(0).getValue() +
				", Wind Dir: "+ getWindSustainedDirection(0).getValue() +
				" Gust: "+ getWindGust(0).getValue() +"\n"
				// "Weather: "+ getWeather(0).getWeather_type() +" POP: "+ getprobOfPrecip12(0).getValue() +"\n"
				
				;

	}
}
