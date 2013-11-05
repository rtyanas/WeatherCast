package com.yanas.mobileapp.weathercast;

import java.io.Serializable;

public class WeatherCondDataValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String coverage;
	private String intensity;
	private String additive;
	private String weather_type;
	private String qualifier;
	private String period;
	
	public WeatherCondDataValue() {
		coverage = "";
		intensity = "";
		additive = "";
		weather_type = "";
		qualifier = "";
		period = "";
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getIntensity() {
		return intensity;
	}

	public void setIntensity(String intensity) {
		this.intensity = intensity;
	}

	public String getAdditive() {
		return additive;
	}

	public void setAdditive(String additive) {
		this.additive = additive;
	}

	public String getWeather_type() {
		return weather_type;
	}

	public void setWeather_type(String weather_type) {
		this.weather_type = weather_type;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

}
