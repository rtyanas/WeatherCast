package com.yanas.mobileapp.weathercast.parsexml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;

import com.yanas.mobileapp.weathercast.GlobalSettings;
import com.yanas.mobileapp.weathercast.StationData;
import com.yanas.mobileapp.weathercast.WeatherCondDataValue;
import com.yanas.mobileapp.weathercast.WeatherDataValue;
import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed.LayoutAndDates;

import android.os.Environment;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;

public class WeatherXmlParsing extends BaseFeedParser {

	// names of the XML tags
	static final String DWML = "dwml";
	static final String DATA = "data";
	static final String TIME_LAYOUT = "time-layout";
	static final String PUB_DATE = "pubDate";
	static final String DESCRIPTION = "description";
	static final String LAYOUT_KEY = "layout-key";
	static final String START_VALID_TIME = "start-valid-time";
	static final String END_VALID_TIME = "end-valid-time";
	static final String PARAMETERS = "parameters";
	static final String TEMPERATURE = "temperature";
	static final String WIND_SPEED = "wind-speed";
	static final String DIRECTION = "direction";
	static final String POP = "probability-of-precipitation";
	static final String WEATHER = "weather";
	static final String WEATHER_CONDITION = "weather-conditions";
	static final String VALUE = "value";
	static final String VISIBILITY = "visibility";  // To do get this data from VALUE.getChild()
	
//	StationData stationData;
//	Vector<LayoutAndDates> layoutAndDatesV;
	
	// If this variable is defined in the method a compile error is generated
	WeatherDataParsed wdp;  

	   public static void main(String args[])
	   {
	       	System.out.println("Start SAX Parse");
	   		String xmlFileOut = "weather.xml";
	    	File path = Environment.getExternalStoragePublicDirectory(
	                Environment.DIRECTORY_DOWNLOADS);
	    	File file = new File(path, xmlFileOut);
	    	if(GlobalSettings.weatherXmlParsing)
	    		Log.d("WeatherData", "Output file: "+ path +"/"+ xmlFileOut );

			FileInputStream fis = null;
		    try {
				fis = new FileInputStream(file); // "C:/Users/RT/Weather/messasgeExample.xml");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    WeatherXmlParsing sx = new WeatherXmlParsing(fis);
		     WeatherDataParsed wdp = sx.parse();
		    System.out.println("End SAX Parse"+ wdp);
	   }

	protected WeatherXmlParsing(String feedUrl){
		super(feedUrl);
		initializeData();
	}
	
	public WeatherXmlParsing(FileInputStream fis){
		super(fis);
		initializeData();
	}

	private void initializeData() {
//		layoutAndDatesV = new Vector<LayoutAndDates>() ;
//		stationData = new StationData();
		wdp = new WeatherDataParsed();
	}
	

	/**
	 * Parse through the XML data and put into StationData Structure and LayoutAndDates list.
	 * These structures are in WeatherDataParsed class this class will merge 
	 * the data and the layout dates into the StationData structures. 
	 */
	public WeatherDataParsed parse() {
		
		final LayoutAndDates layoutAndDates = wdp.new LayoutAndDates();
		RootElement root = new RootElement(DWML);
		Element data = root.getChild(DATA);
		Element time_layout = data.getChild(TIME_LAYOUT);
		Element parameters = data.getChild(PARAMETERS);
		Element temp = parameters.getChild(TEMPERATURE);
		Element windSpeed = parameters.getChild(WIND_SPEED);
		Element direction = parameters.getChild(DIRECTION);
		Element pop = parameters.getChild(POP);
		Element weather = parameters.getChild(WEATHER);
		Element weatherCond = weather.getChild(WEATHER_CONDITION);
		
		final StringBuffer weather_TimeLayout = new StringBuffer();

		final Atts atts = new Atts();
		
		// Time Layouts for weather values
		time_layout.setEndElementListener(new EndElementListener(){
			public void end() {
				wdp.layoutAndDatesV.add(layoutAndDates.copy());
				layoutAndDates.clear() ;
			}
		});
		time_layout.getChild(START_VALID_TIME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				layoutAndDates.startDate.add(body);
			}
		});
		time_layout.getChild(END_VALID_TIME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				layoutAndDates.endDate.add(body);
			}
		});
		time_layout.getChild(LAYOUT_KEY).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				layoutAndDates.setLayout(body);
			}
		});

		// Temperature
		parameters.getChild(TEMPERATURE).setStartElementListener(new StartElementListener(){
			public void start(Attributes atts_in) {
				for(int i=0 ; i < atts_in.getLength(); i++) {
			    	if(GlobalSettings.weatherXmlParsing)
			    		Log.i("WeatherXmlParsing:parse: ", atts_in.getQName(i) +", "+ atts_in.getValue(i) );
					atts.addAtt(atts_in.getQName(i), atts_in.getValue(i));
				}
			}
		});
		parameters.getChild(TEMPERATURE).setEndElementListener(new EndElementListener(){
			public void end() {
				atts.clear();
			}
		});
		temp.getChild(VALUE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				WeatherDataValue wData = new WeatherDataValue();
				wData.setValue(body);
				wData.setPeriod(atts.getAtt("time-layout"));
				wData.setUnits(atts.getAtt("units"));
				if(atts.getAtt("type").equals("maximum")) {
					wdp.stationData.setTemperatureMax(wData);
				}
				else if(atts.getAtt("type").equals("minimum")) {
					wdp.stationData.setTemperatureMin(wData);
				}
				else if(atts.getAtt("type").equals("hourly")) {
					wdp.stationData.setTemperature(wData);
				}
			}
		});

		// Wind Speed
		parameters.getChild(WIND_SPEED).setStartElementListener(new StartElementListener(){
			public void start(Attributes atts_in) {
				for(int i=0 ; i < atts_in.getLength(); i++) {
			    	if(GlobalSettings.weatherXmlParsing)
			    		Log.i("WeatherXmlParsing:parse: ", atts_in.getQName(i) +", "+ atts_in.getValue(i) );
					atts.addAtt(atts_in.getQName(i), atts_in.getValue(i));
				}
			}
		});
		parameters.getChild(WIND_SPEED).setEndElementListener(new EndElementListener(){
			public void end() {
				atts.clear();
			}
		});
		windSpeed.getChild(VALUE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				WeatherDataValue wData = new WeatherDataValue();
				wData.setValue(body);
				wData.setPeriod(atts.getAtt("time-layout"));
				wData.setUnits(atts.getAtt("units"));
				if(atts.getAtt("type").equals("sustained")) {
					wdp.stationData.setWindSustained(wData);
				}
				else if(atts.getAtt("type").equals("gust")) {
					wdp.stationData.setWindGust(wData);
				}
			}
		});


		// Wind Direction
		parameters.getChild(DIRECTION).setStartElementListener(new StartElementListener(){
			public void start(Attributes atts_in) {
				for(int i=0 ; i < atts_in.getLength(); i++) {
			    	if(GlobalSettings.weatherXmlParsing)
			    		Log.i("WeatherXmlParsing:parse: ", atts_in.getQName(i) +", "+ atts_in.getValue(i) );
					atts.addAtt(atts_in.getQName(i), atts_in.getValue(i));
				}
			}
		});
		parameters.getChild(DIRECTION).setEndElementListener(new EndElementListener(){
			public void end() {
				atts.clear();
			}
		});
		direction.getChild(VALUE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				WeatherDataValue wData = new WeatherDataValue();
				wData.setValue(body);
				wData.setPeriod(atts.getAtt("time-layout"));
				wData.setUnits(atts.getAtt("units"));
				if(atts.getAtt("type").equals("wind")) {
					wdp.stationData.setWindSustainedDirection(wData);
				}
			}
		});


		// Probability of Precipitation
		parameters.getChild(POP).setStartElementListener(new StartElementListener(){
			public void start(Attributes atts_in) {
				for(int i=0 ; i < atts_in.getLength(); i++) {
			    	if(GlobalSettings.weatherXmlParsing)
			    		Log.i("WeatherXmlParsing:parse: ", atts_in.getQName(i) +", "+ atts_in.getValue(i) );
					atts.addAtt(atts_in.getQName(i), atts_in.getValue(i));
				}
			}
		});
		parameters.getChild(POP).setEndElementListener(new EndElementListener(){
			public void end() {
				atts.clear();
			}
		});
		pop.getChild(VALUE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				WeatherDataValue wData = new WeatherDataValue();
				wData.setValue(body);
				wData.setPeriod(atts.getAtt("time-layout"));
				wData.setUnits(atts.getAtt("units"));
				if(atts.getAtt("type").equals("12 hour")) {
					wdp.stationData.setprobOfPrecip12(wData);
				}
			}
		});


		// Weather Condition
		parameters.getChild(WEATHER).setStartElementListener(new StartElementListener(){
			public void start(Attributes atts_in) {
				if(weather_TimeLayout.length() > 0)
					weather_TimeLayout.delete(0, weather_TimeLayout.length() - 1);
				weather_TimeLayout.append(atts_in.getValue("time-layout"));
			}
		});
		parameters.getChild(WEATHER).setEndElementListener(new EndElementListener(){
			public void end() {
				atts.clear();
			}
		});
		weatherCond.getChild(VALUE).setStartElementListener(new StartElementListener(){
			public void start(Attributes atts_in) {
				WeatherCondDataValue wData = new WeatherCondDataValue();
				wData.setAdditive(atts_in.getValue("additive"));
				wData.setCoverage(atts_in.getValue("coverage"));
				wData.setIntensity(atts_in.getValue("intensity"));
				wData.setQualifier(atts_in.getValue("qualifier"));
				wData.setWeather_type(atts_in.getValue("weather-type"));
				wData.setPeriod(weather_TimeLayout.toString());
				wdp.stationData.setWeather(wData);
			}
		});


		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return wdp;
	}

	
	public List<Message> parse(String disAbleThisMethod) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			RssHandler handler = new RssHandler();
			parser.parse(this.getInputStream(), handler);
			return handler.getMessages();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	
	public class Atts {
		HashMap<String, String> atts;
		
		public Atts () {
			atts = new HashMap<String, String>();
		}

		public String getAtt(String key) {
			return atts.get(key);
		}

		public void addAtt(String key, String value) {
			this.atts.put(key, value);
		}
		
		public void clear() {
			atts.clear();
		}
	}

}