/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yanas.mobileapp.weathercast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * ScreenSlideActivity} samples.</p>
 */
public class DisplayWeatherInfoAccessPageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    public static final String ARG_TEMP = "temp";
    public static final String STATIONDATA_ARG = "station_Data";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
	StationData stationData;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static DisplayWeatherInfoAccessPageFragment create(StationData stationSelected_in) {
        DisplayWeatherInfoAccessPageFragment fragment = new DisplayWeatherInfoAccessPageFragment();
        Bundle args = new Bundle();
        
     // Serialize data object to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(stationSelected_in);
	        out.close();
	        args.putByteArray(STATIONDATA_ARG, bos.toByteArray());
	        fragment.setArguments(args);
		} catch (IOException e) {
			Log.e("DisplayWeatherInfoAccessPageFragment", "create, "+ e.getMessage());
		}
        
        return fragment;
    }

    public DisplayWeatherInfoAccessPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        byte stationBA[] = null;
        
        if(getArguments() != null) {
        	stationBA = getArguments().getByteArray(STATIONDATA_ARG);
        }
        else {
        	Log.e("DisplayWeatherInfoAccessPageFragment", "getArguments() is null");
        }
        
        if(stationBA != null) {
	        ByteArrayInputStream bin =  new ByteArrayInputStream (stationBA);
	        
	        try {
				ObjectInputStream in = new ObjectInputStream(bin);
				stationData = (StationData)in.readObject();
			} catch (ClassNotFoundException e) {
		        Log.e("DisplayWeatherInfoAccessPageFragment", e.getMessage());
			} catch (StreamCorruptedException e) {
		        Log.e("DisplayWeatherInfoAccessPageFragment", e.getMessage());
			} catch (IOException e) {
		        Log.e("DisplayWeatherInfoAccessPageFragment", e.getMessage());
			}
	        
	        Log.d("DisplayWeatherInfoAccessPageFragment", stationData.toString());
	        // stationData = getArguments().g.getStringArray(ARG_TEMP);
        }
        else {
        	Log.e("DisplayWeatherInfoAccessPageFragment", "onCreate: stationBA is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment2_screen_slide_page, container, false);

        // Set the title view to show the page number.
        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                getString(R.string.title_template_step, mPageNumber + 1));

        // Set the title view to show the page number.
        ((TextView) rootView.findViewById(android.R.id.text2)).setText(
        		stationData.toString()) ;

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
