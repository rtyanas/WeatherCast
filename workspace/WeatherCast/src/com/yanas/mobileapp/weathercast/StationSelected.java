package com.yanas.mobileapp.weathercast;

import com.yanas.utils.GPSTracking;

public class StationSelected {
	
	protected String city;
	protected String state;
	protected String zipCode;
	protected String latitude;
	protected String longitude;
	protected String dateTime;


	public StationSelected(StationListActivity stationLA, 
			String city_in, String state_in, String zipCode_in) {
		
		city = city_in;
		state = state_in;
		zipCode = zipCode_in;
		latitude = "";
		longitude = "";
		
		// If there is a zip code then the get weather will use zip code
		// otherwise get weather using lat/long
		if(zipCode.equals("")) {
			getCurrentLatLonZipCity(stationLA);
		}
		
	}

	private boolean getCurrentLatLonZipCity(StationListActivity stationLA) {
		GPSTracking gpsT = new GPSTracking(stationLA);

		boolean canGetLocation = gpsT.canGetLocation();
		
		if(canGetLocation) {
			latitude = String.valueOf(gpsT.getLatitude()); 
			longitude = String.valueOf(gpsT.getLongitude());  
			city = gpsT.getLocality(stationLA);
			zipCode = gpsT.getPostalCode(stationLA) ;  
			if(city == null) {
				city = "City";
			}
			if(city != null && ( city.equals("")  || city.equals("City") ) ) {
				city += "-"+ zipCode;
			}
			if(zipCode == null) {
				zipCode = "";
			}
		}
		
		return canGetLocation;
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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

	public String getStationData() {
		return city +
			","+ state +
			","+ zipCode +
			","+ latitude + 
			","+ longitude;
	}

	public String toString() {
		return "city "+ 
				",state "+ 
				",zipCode "+ 
				",latitude "+ 
				",longitude "+ "\n"+
		        city +
				","+ state +
				","+ zipCode +
				","+ latitude + 
				","+ longitude;
	}
}
