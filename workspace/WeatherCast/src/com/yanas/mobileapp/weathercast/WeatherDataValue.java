package com.yanas.mobileapp.weathercast;

import java.io.Serializable;

public class WeatherDataValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String value;
	private String units;
	private String period;
	private String iconDesc;
	private String textDesc;
	
	public WeatherDataValue() {
		value = "";
		units = "";
		period = "";
		iconDesc = "";
		textDesc = "";
	}
	
	public String getUnits() {
		if(units.equals("percent"))
			return "%";
		else if(units.equals("knots"))
			return "KTS";
		else if(units.equals("Fahrenheit"))
			return "\u2109";
		else if(units.equals("Celsius"))
			return "\u2103";
		
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getIconDesc() {
		return iconDesc;
	}
	public void setIconDesc(String iconDesc) {
		this.iconDesc = iconDesc;
	}
	public String getTextDesc() {
		return textDesc;
	}
	public void setTextDesc(String textDesc) {
		this.textDesc = textDesc;
	}
	
	public WeatherDataValue copy() {
		WeatherDataValue wdv = new WeatherDataValue();
		wdv.setIconDesc(this.getIconDesc());
		wdv.setPeriod(this.getPeriod());
		wdv.setTextDesc(this.getTextDesc());
		wdv.setUnits(this.getUnits());
		wdv.setValue(this.getValue());	
		
		return wdv;
	}
}
