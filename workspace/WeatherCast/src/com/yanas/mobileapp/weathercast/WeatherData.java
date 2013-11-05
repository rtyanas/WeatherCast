package com.yanas.mobileapp.weathercast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.yanas.mobileapp.restaccess.RestClient;
import com.yanas.mobileapp.restaccess.RestClient.RequestMethod;

import android.content.Context.*;
import android.net.Uri;

public class WeatherData {

	/*
	 * Execute the weather data request
	 */
	public class GetTheWeather extends AsyncTask<SetTheWeather, Integer, Long> {
		String station;
		String temperature;
		String response;
		
		GetTheWeather() {
		
		}
		
	   	protected Long doInBackground(SetTheWeather... weatherReq) {
			long sizeL = 0L;
			Log.d("HttpTest", "start doInBackground" );
			
			RestClient rc = new RestClient();
			try {
				rc.Execute(weatherReq[0].method, weatherReq[0].url, 
						weatherReq[0].headers, weatherReq[0].params);
				response = rc.response;
				Log.d("HttpTest", "Response Data:" + response);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return sizeL;
	   	}
	   	
		public String getResponse() {
			
			return response;
		}
	} // GetTheWeather
	
	
	/*
	 * Execute the weather data request
	 */
	/**
	 * 
	 * @param weatherReq
	 * @return
	 */
	public String getObservedPropertyTide(SetTheWeather weatherReq) {
		String observedDataRet = "Not Available";
		GetTheWeather weatherRun = new GetTheWeather();
		
		weatherRun.execute(weatherReq);
		
		// Wait for data return
		for(int i=0; i < 40; i++ ) {
			if(weatherRun.response != null)
				break;
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Log.d("DisplayMessage", "Resonse: "+ weatherRun.response);

		if(weatherRun.response != null)
		{
			if(weatherReq.observedData.equals("air_temperature")) 
			{
				String temp = "Not Available";
				// Java evaluates if statement left to right making the split on "," 
				// valid if split on "\n" is true
				if(weatherRun.response != null && weatherRun.response.split("\n").length > 1  && 
				   weatherRun.response.split("\n")[1].split(",").length > 5)  
				{
					temp = weatherRun.response.split("\n")[1].split(",")[5];
				}
				else
				{
					Log.e("DiaplayMessage", "Temperature value not available");
				}
				observedDataRet = temp;
			}
			else if(weatherReq.observedData.equals("winds")) 
			{
				String temp = "Not Available";
				// Java evaluates if statement left to right making the split on "," 
				// valid if split on "\n" is true
				if(weatherRun.response != null && weatherRun.response.split("\n").length > 1  && 
				   weatherRun.response.split("\n")[1].split(",").length > 7)  
				{
					temp = weatherRun.response.split("\n")[1].split(",")[5] +","+
					       weatherRun.response.split("\n")[1].split(",")[6] +","+
					       weatherRun.response.split("\n")[1].split(",")[7];
				}
				else
				{
					Log.e("DiaplayMessage", "Winds value not available");
				}
				observedDataRet = temp;
			}
			
			
		}
		
		
		return observedDataRet;
	} // getObservedProperty(SetTheWeatherTide weatherReq) {
	
	boolean DEBUG = false;
	String xmlFileOut = "weather.xml";
	
	/**
	 * Get 
	 * @param weatherReq
	 * @return
	 */
	public StationData getObservedPropertyMeteorological(
			DisplayWeatherInfoActivity dwThis, SetTheWeather weatherReq,
			String city, String state, String zipcode, String latitude, String longitude) {
		String observedDataRet = "Not Available";
		GetTheWeather weatherRun = new GetTheWeather();
		StationData stationData = new StationData(city, state, zipcode, latitude, longitude);
		
		weatherRun.execute(weatherReq);
		
		// Wait for data return
		for(int i=0; i < 40; i++ ) {
			if(weatherRun.response != null)
				break;
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Log.d("DisplayMessage", "Resonse: "+ weatherRun.response);
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	    Document dom = null;
		try {
		    /* Parse the xml-data from our URL. */
//		    URL url = new URL("http://xyz.com/.../");
//		    InputStream inputStream = url.openStream();
		    /*Get Document Builder*/
		    DocumentBuilder builder = builderFactory.newDocumentBuilder();
		    // dom = builder.parse(weatherRun.response);
		    
		    ByteArrayInputStream bis = new ByteArrayInputStream(weatherRun.response.getBytes("utf-8"));
		    StringBuilder sb = new StringBuilder();
		    if(DEBUG) {
		    	File path = Environment.getExternalStoragePublicDirectory(
		                Environment.DIRECTORY_DOWNLOADS);
		    	File file = new File(path, xmlFileOut);

		    	Log.d("WeatherData", "Output file: "+ path +"/"+ xmlFileOut );
 				FileOutputStream fos = null;
		    	try {
  	 				fos = new FileOutputStream(file);
  	 				int ch;
			    	while( (ch=bis.read() ) != -1) {
				    	fos.write(ch);		    		
			    	}
			    	
			        // Tell the media scanner about the new file so that it is
			        // immediately available to the user.
//			        MediaScannerConnection.scanFile(dwThis,
//			                new String[] { file.toString() }, null,
//			                new MediaScannerConnection.OnScanCompletedListener() {
//			            public void onScanCompleted(String path, Uri uri) {
//			                Log.i("ExternalStorage", "Scanned " + path + ":");
//			                Log.i("ExternalStorage", "-> uri=" + uri);
//			            }
//			        });
	
			    	
		    	} catch(IOException ioe) {
		    		Log.e("getObservedPropertyMeteorological() : ", ioe.getMessage());
		    	} finally {
			    	fos.flush();
			    	fos.close();
		    	}
		    }
		    // parse wants input stream arg even though is will compile with a string arg
		    bis.reset();
		    InputSource inputSource = new InputSource(bis) ;
		    dom = builder.parse(inputSource) ;
		    
		} catch (Exception e) { 
			Log.e("DisplayMessage", "Exception: "+ e.getMessage());
		}
		
		Element rootElement = dom.getDocumentElement();
		
		stationData.setStartDate(  getXmlDateTimeStartEnd(rootElement, "time-layout") );
		
		WeatherDataValue weatherDV = null;
		String data = getXmlValues(rootElement, "temperature", "hourly");
		if ( ! data.equals("") ) {
			observedDataRet = data +"-hourly, ";
			weatherDV = new WeatherDataValue();
			weatherDV.setValue(data);
			weatherDV.setPeriod("hourly");
		}
		stationData.setTemperature(weatherDV);
				
		data = getXmlValues(rootElement, "temperature", "maximum");
		if ( ! data.equals("") ) {
			observedDataRet += data +"-maximum, ";
			weatherDV = new WeatherDataValue();
			weatherDV.setValue(data);
			weatherDV.setPeriod("maximum");
		}
		stationData.setTemperatureMax(weatherDV);
				
		data = getXmlValues(rootElement, "temperature", "minimum");
		if ( ! data.equals("") ) {
			observedDataRet += data +"-minimum, ";
			weatherDV = new WeatherDataValue();
			weatherDV.setValue(data);
			weatherDV.setPeriod("minimum");
		}
		stationData.setTemperatureMin(weatherDV);
				
		data = getXmlValues(rootElement, "wind-speed", "sustained");
		if ( ! data.equals("") ) {
			observedDataRet += ":"+ data +"-sustained,";
			weatherDV = new WeatherDataValue();
			weatherDV.setValue(data);
			weatherDV.setPeriod("");
		}
		stationData.setWindSustained(weatherDV);
				
		data = getXmlValues(rootElement, "wind-speed", "gust");
		if ( ! data.equals("") ) {
			observedDataRet += ":"+ data +"-gust";
			weatherDV = new WeatherDataValue();
			weatherDV.setValue(data);
			weatherDV.setPeriod("");
		}
		stationData.setWindGust(weatherDV);
				
		data = getXmlValues(rootElement, "direction", "wind");
		if ( ! data.equals("") ) {
			observedDataRet += ":"+ data +"-gust";
			weatherDV = new WeatherDataValue();
			weatherDV.setValue(data);
			weatherDV.setPeriod("");
		}
		stationData.setWindSustainedDirection(weatherDV);
				
		data = getXmlValues(rootElement, "probability-of-precipitation", "12 hour");
		if ( ! data.equals("") ) {
			observedDataRet += ":"+ data +"-gust";
			weatherDV = new WeatherDataValue();
			weatherDV.setValue(data);
			weatherDV.setPeriod("");
		}
		stationData.setprobOfPrecip12(weatherDV);
				
//		data = getXmlValues(rootElement, "weather", "");
//		if ( ! data.equals("") ) {
//			observedDataRet += ":"+ data +"-gust";
//			weatherDV = new WeatherDataValue();
//			weatherDV.setValue(data);
//			weatherDV.setPeriod("");
//		}
//		stationData.setWeather(weatherDV);
				
		
		return stationData;

	} // getObservedProperty(SetTheWeatherMeteorological weatherReq) {

	
	// time-layout

	private String getXmlDateTimeStartEnd(Element rootElement, String propertyIn) {
		
		NodeList items = rootElement.getElementsByTagName(propertyIn);
		String dateTimeStartEnd = "";
		
		for (int i=0;i<items.getLength();i++) {
			  Node item = items.item(i);
			  NodeList properties = item.getChildNodes();
			  for (int j=0;j<properties.getLength();j++) {
			        Node property = properties.item(j);
			        String name = property.getNodeName();
			        
			        if(property.getNodeName() != null && property.getNodeName() != null && 
			        						property.getNodeName().equals("start-valid-time"))  {
			        		dateTimeStartEnd += property.getTextContent() +", ";
			        } else if(property.getNodeName() != null && property.getNodeName() != null && 
			        						property.getNodeName().equals("end-valid-time"))  {
			        		dateTimeStartEnd += property.getTextContent() +", ";
			        }
			        Log.d(propertyIn, " name: "+ name +
			        		", value: "+ property.getNodeValue() +
			        		", text: "+ property.getTextContent() +
			        		", Local Name: "+ property.getLocalName() +
			        		", prefix: "+ property.getPrefix());
			  }
	    }
		
		return dateTimeStartEnd;
	}
	
	
	private String getXmlValues(Element rootElement, String propertyIn, String typeIn) {
		
		NodeList items = rootElement.getElementsByTagName(propertyIn);

		String units = "";
		String value = "";
		String type = "";
		
		for (int i=0;i<items.getLength();i++) {
			  Node item = items.item(i);
			  
              NamedNodeMap nodeMap = item.getAttributes();
  	          if(nodeMap != null && nodeMap.getLength() > 0) {
  	        	  if(nodeMap.getNamedItem("units") != null) 
  	        		units = nodeMap.getNamedItem("units").getNodeValue();
  	        	  
  	        	  if(nodeMap.getNamedItem("type") != null) 
  	        		type = nodeMap.getNamedItem("type").getNodeValue();
		      }
              
			  NodeList properties = item.getChildNodes();
			  for (int j=0;j<properties.getLength();j++){
			        Node property = properties.item(j);
			        String name = property.getNodeName();
			        
			        if(property.getNodeName() != null && property.getNodeName() != null && 
			        		property.getNodeName().equals("value"))  {
			        	if( typeIn.equals("") || type.equals(typeIn)) {
				        	value = property.getTextContent();
			        	}
			        }
			        	
			        Log.d(propertyIn, " name: "+ name +
			        		", value: "+ property.getNodeValue() +
			        		", text: "+ property.getTextContent() +
			        		", Local Name: "+ property.getLocalName() +
			        		", prefix: "+ property.getPrefix());
			        
			  }
		}

		return value +", "+ units;
	}
	
	

}
