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
    
    
	/*
	 * Execute the weather data request
	 */
	public class GetTheWeather extends AsyncTask<SetTheWeather, Integer, String> {
		String station;
		String temperature;
		String response;

		Dialog dialog;
		ProgressBar progressBar;
		TextView tvLoading,tvPer;
		Button btnCancel;


		GetTheWeather() {
		
		}
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new Dialog(dwThis);
			dialog.setCancelable(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.progressdialog);

			progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar1);
			tvLoading = (TextView) dialog.findViewById(R.id.tv1);
			tvPer = (TextView) dialog.findViewById(R.id.tvper);
			btnCancel = (Button) dialog.findViewById(R.id.btncancel);

			btnCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					objMyTask.cancel(true);
					dialog.dismiss();
				}
			});

			dialog.show();
		}


		@Override
	   	protected String doInBackground(SetTheWeather... weatherReq) {

	   		if(GlobalSettings.weatherData) Log.d("GetTheWeather", "start doInBackground" );
			
			RestClient rc = new RestClient();
			try {
				rc.Execute(weatherReq[0].method, weatherReq[0].url, 
						weatherReq[0].headers, weatherReq[0].params);
				response = rc.response;
				// if(GlobalSettings.weatherData) Log.d("GetTheWeather", "Response Data:" + response);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return response;
	   	}
	   	
		public String getResponse() {
			
			return response;
		}

		
		protected void onProgressUpdate(Integer vals)  {
			
		}
		
		@Override
 		protected void  onPostExecute (String  result) {
			super.onPostExecute(result);
			if(GlobalSettings.weatherData) Log.d("GetTheWeather", " onPostExecute result: "+ result);
			dialog.dismiss();

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
		String response = "";
		
		weatherRun.execute(weatherReq);

		try {
			response = weatherRun.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
		
		if(GlobalSettings.weatherData) Log.d("DisplayMessage", "Resonse: "+ weatherRun.response);

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
					if(GlobalSettings.weatherData) Log.e("DiaplayMessage", "Temperature value not available");
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
					if(GlobalSettings.weatherData) Log.e("DiaplayMessage", "Winds value not available");
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
	public WeatherDataParsed getObservedPropertyMeteorological(
			SetTheWeather weatherReq,
			String city, String state, String zipcode, String latitude, String longitude) {
		String observedDataRet = "Not Available";
		GetTheWeather theWeather = new GetTheWeather();
		StationData stationData = new StationData(city, state, zipcode, latitude, longitude);
		
		if(GlobalSettings.weatherData) Log.d("WeatherData getObservedPropertyMeteorological", "Execute");
		theWeather.execute(weatherReq);
		try {
			observedDataRet = theWeather.get();
			AsyncTask.Status stat = theWeather.getStatus();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Wait for data return
//		for(int i=0; i < 40; i++ ) {
//			if(theWeather.response != null)
//				break;
//			
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				if(GlobalSettings.weatherData) Log.e("WeatherData", "Error getting response: "+ e.getMessage());
//			}
//		}
		
		if(GlobalSettings.weatherData) Log.d("DisplayMessage", "observedDataRet: "+ observedDataRet); // theWeather.response);
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	    Document dom = null;
		try {
		    /* Parse the xml-data from our URL. */
//		    URL url = new URL("http://xyz.com/.../");
//		    InputStream inputStream = url.openStream();
		    /*Get Document Builder*/
		    DocumentBuilder builder = builderFactory.newDocumentBuilder();
		    // dom = builder.parse(weatherRun.response);
		    
		    ByteArrayInputStream bis = new ByteArrayInputStream(observedDataRet.getBytes("utf-8")); // theWeather.response.getBytes("utf-8"));
		    StringBuilder sb = new StringBuilder();
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
		    		if(GlobalSettings.weatherData) Log.e("getObservedPropertyMeteorological() : ", ioe.getMessage());
		    	} finally {
			    	fos.flush();
			    	fos.close();
		    	}
		    }
		    // parse wants input stream arg even though is will compile with a string arg
		    bis.reset();
		    InputSource inputSource = new InputSource(bis) ;
		    dom = builder.parse(inputSource) ;
		    
		    WeatherXmlParsing wxp = new WeatherXmlParsing ( new ByteArrayInputStream(theWeather.response.getBytes())  );
		    wdp =  wxp.parse();
		    wdp.updateWeatherDataPeriod();
		    wdp.stationData.setCity(city);
		    wdp.stationData.setState(state);
		    wdp.stationData.setZipcode(zipcode);

		} catch (Exception e) { 
			if(GlobalSettings.weatherData) Log.e("DisplayMessage", "Exception: "+ e.getMessage());
		}
		
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
