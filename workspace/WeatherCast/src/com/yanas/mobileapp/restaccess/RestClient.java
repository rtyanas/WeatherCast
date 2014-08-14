package com.yanas.mobileapp.restaccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;


/**
 * This class communicates with the noaa server as a ReST for an observation
 * on the current weather predictions.  Use the call to GetOpservation with the
 * passed parameters i.e. air temperature, wind direction, 
 * percent precipitation,  predicted weather
 * Representational State Transfer - ReST
 * @author RT
 *
 */
public class RestClient {

	public enum RequestMethod
	{
	    GET,
	    POST
	}
	public int responseCode=0;
	public String message;
	public String response;
	
	/* The following is a sample request
	 * http://opendap.co-ops.nos.noaa.gov/ioos-dif-sos/SOS?service=SOS&request=GetObservation&
	 * version=1.0.0&observedProperty=air_temperature&
	 * offering=urn:ioos:station:NOAA.NOS.CO-OPS:8465705&
	 * responseFormat=text%2Fcsv&
	 * eventTime=2013-09-18T00:00:00Z/2013-09-18T23:59:00Z
	 * 
	 */
	public void Execute(RequestMethod method,String url,ArrayList<NameValuePair> headers,ArrayList<NameValuePair> params) throws Exception
	{
	    switch (method)
	    {
	        case GET:
	        {
	            // add parameters
	            String combinedParams = "";
	            if (params!=null)
	            {
	                combinedParams += "?";
	                for (NameValuePair p : params)
	                {
	                    String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
	                    if (combinedParams.length() > 1)
	                        combinedParams += "&" + paramString;
	                    else
	                        combinedParams += paramString;
	                }
	            }
	            HttpGet request = new HttpGet(url + combinedParams);
	            Log.d("HttpTestGet", "Request: "+ request.getRequestLine());
	            // add headers
	            if (headers!=null)
	            {
	                headers=addCommonHeaderField(headers);
	                for (NameValuePair h : headers)
	                    request.addHeader(h.getName(), h.getValue());
	            }
	            executeRequest(request, url);
	            break;
	        }
	        case POST:
	        {
	            HttpPost request = new HttpPost(url);
	            // add headers
	            if (headers!=null)
	            {
	                headers=addCommonHeaderField(headers);
	                for (NameValuePair h : headers)
	                    request.addHeader(h.getName(), h.getValue());
	            }
	            if (params!=null)
	                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	            
	            Log.d("HttpTestPost", "Request: "+ request);
	            executeRequest(request, url);
	            break;
	        }
	    }
	}
	

	/**
	 * Add Content-Type urlencoded
	 * @param header_in
	 * @return
	 */
	private ArrayList<NameValuePair> addCommonHeaderField(ArrayList<NameValuePair> header_in)
	{
	    header_in.add(new BasicNameValuePair("Content-Type","application/x-www-form-urlencoded"));
	    return header_in;
	}
	
	/**
	 * Execute Get/Post request created by execute method 
	 * @param request
	 * @param url
	 */
	private void executeRequest(HttpUriRequest request, String url)
	{
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse httpResponse;
	    try
	    {
	        httpResponse = client.execute(request);
	        responseCode = httpResponse.getStatusLine().getStatusCode();
	        message = httpResponse.getStatusLine().getReasonPhrase();
	        HttpEntity entity = httpResponse.getEntity();
	
	        if (entity != null)
	        {
	            InputStream instream = entity.getContent();
	            response = convertStreamToString(instream);
	            instream.close();
	        }
	    }
	    catch (IOException ioe) {
	    	Log.e("RestClient.executeRequest() Http response/client", "IOException: "+ ioe.getMessage());
	    }
	    catch(IllegalStateException ise) {
	    	Log.e("RestClient.getContent()", "IllegalStateException: "+ ise.getMessage());    	
	    }
	}
	
	
	/**
	 * convertStreamToString
	 * @param is
	 * @return String
	 */
	private static String convertStreamToString(InputStream is)
	{
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    try
	    {
	        while ((line = reader.readLine()) != null)
	        {
	            sb.append(line + "\n");
	        }
	        is.close();
	    }
	    catch (IOException ioe) { 
	    	Log.e("RestClient.convertStreamToString()", "IOException: "+ ioe.getMessage());    	    	
	    }
	
	    return sb.toString();
	}
}