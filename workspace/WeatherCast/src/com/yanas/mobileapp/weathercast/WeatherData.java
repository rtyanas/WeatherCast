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
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanas.mobileapp.restaccess.RestClient;
import com.yanas.mobileapp.restaccess.RestClient.RequestMethod;
import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed;
import com.yanas.mobileapp.weathercast.parsexml.WeatherXmlParsing;

import android.content.Context.*;
import android.net.Uri;

public class WeatherData {
	
	
    WeatherDataParsed wdp = null;
    DisplayWeatherInfoActivity dwThis;

    public WeatherData(DisplayWeatherInfoActivity dwThis_in) {
    	dwThis = dwThis_in;
    }
    
	/**
	 * 
	 * @param weatherReq
	 * @return
	 */
	public String getObservedPropertyTide(SetTheWeather weatherReq) {
		String observedDataRet = "Not Available";
		String response = "";
		
        RestClient rc = new RestClient();
        try {
            rc.Execute(weatherReq.method, weatherReq.url, 
                    weatherReq.headers, weatherReq.params);
            response = rc.response;
            // if(GlobalSettings.weatherData) Log.d("GetTheWeather", "Response Data:" + response);
            
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // *** 

        

		return observedDataRet;
	} // getObservedProperty(SetTheWeatherTide weatherReq) {
	
	boolean DEBUG = false;
	String xmlFileOut = "weather.xml";
	
	/**
	 * Get 
	 * @param weatherReq
	 * @return
	 */
	public WeatherDataParsed getObservedPropertyMeteorological(
			SetTheWeather weatherReq,
			String city, String state, String zipcode, String latitude, String longitude) {
	
	    String observedDataRet = "Not Available";
		
		if(GlobalSettings.weatherData) Log.d("WeatherData getObservedPropertyMeteorological", "Execute");

		// theWeather.execute(weatherReq);
		
	    // Copied from AsynTask
        RestClient rc = new RestClient();
        try {
            rc.Execute(weatherReq.method, weatherReq.url, 
                    weatherReq.headers, weatherReq.params);
            // response = rc.response;
            // if(GlobalSettings.weatherData) Log.d("GetTheWeather", "Response Data:" + response);
            
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // *** 

		
		if(GlobalSettings.weatherData) Log.d("DisplayMessage", "observedDataRet: "+ observedDataRet); // theWeather.response);
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	    Document dom = null;
		try {
		    ByteArrayInputStream bis = new ByteArrayInputStream(observedDataRet.getBytes("utf-8")); // theWeather.response.getBytes("utf-8"));
		    StringBuilder sb = new StringBuilder();
		    
		    
		    /***  Test for xml ReST input, when DEBUG is true  ***/
		    if(DEBUG) {
		    	File path = Environment.getExternalStoragePublicDirectory(
		                Environment.DIRECTORY_DOWNLOADS);
		    	File file = new File(path, xmlFileOut);

		    	if(GlobalSettings.weatherData) Log.d("WeatherData", "Output file: "+ path +"/"+ xmlFileOut );
 				FileOutputStream fos = null;
		    	try {
  	 				fos = new FileOutputStream(file);
  	 				int ch;
			    	while( (ch=bis.read() ) != -1) {
				    	fos.write(ch);		    		
			    	}
			    	
			    	if(GlobalSettings.weatherData) 
			    		Log.e("getObservedPropertyMeteorological() : ", "Did write "+ path +" "+ xmlFileOut);
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
		    		if(GlobalSettings.weatherData) Log.e("getObservedPropertyMeteorological() : ", ioe.getMessage() +" Error: "+ observedDataRet);
		    	} finally {
			    	fos.flush();
			    	fos.close();
		    	}
		    }
		    /****  Test for xml ReST file input  ****/
		    
		    WeatherXmlParsing wxp = new WeatherXmlParsing ( new ByteArrayInputStream(rc.response.getBytes())  );
		    wdp =  wxp.parse();
		    wdp.updateWeatherDataPeriod();
		    wdp.stationData.setCity(city);
		    wdp.stationData.setState(state);
		    wdp.stationData.setZipcode(zipcode);

		} catch (Exception e) { 
			if(GlobalSettings.weatherData) Log.e("DisplayMessage", "Exception: "+ e.getMessage());
		}
		
		if(wdp == null)
			wdp = new WeatherDataParsed();
		
		return wdp;

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
			        if(GlobalSettings.weatherData) Log.d(propertyIn, " name: "+ name +
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
			        	
			        if(GlobalSettings.weatherData) Log.d(propertyIn, " name: "+ name +
			        		", value: "+ property.getNodeValue() +
			        		", text: "+ property.getTextContent() +
			        		", Local Name: "+ property.getLocalName() +
			        		", prefix: "+ property.getPrefix());
			        
			  }
		}

		return value +", "+ units;
	}
	
	

}
