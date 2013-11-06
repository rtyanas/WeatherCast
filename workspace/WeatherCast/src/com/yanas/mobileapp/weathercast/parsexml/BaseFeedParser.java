package com.yanas.mobileapp.weathercast.parsexml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import com.yanas.mobileapp.weathercast.parsexml.WeatherDataParsed.LayoutAndDates;

public abstract class BaseFeedParser /* implements FeedParser */ {

	private final URL feedUrl;
	private FileInputStream fis;
	private InputStream is;
	
	protected BaseFeedParser(String feedUrl){
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected BaseFeedParser(FileInputStream fis_in){
		fis = fis_in;
		feedUrl = null;
	}
	

	protected BaseFeedParser(InputStream is_in){
		fis = null;
		feedUrl = null;
		is = is_in;
	}
	

	protected InputStream getInputStream() {
		if(feedUrl != null) {
				try {
			
				return feedUrl.openConnection().getInputStream();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} 
		else if(fis != null) {
			return fis;
		}
		else {
			return is;
		}
	
	}
	
    abstract WeatherDataParsed parse();
    
    
}