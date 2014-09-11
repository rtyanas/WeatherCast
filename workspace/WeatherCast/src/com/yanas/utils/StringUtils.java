package com.yanas.utils;

public class StringUtils {
    
    static public String createStationRow(String city, String state, String zip) {
        StringBuffer c = new StringBuffer(city); 
        
        if( city.trim().equals("") && zip.trim().equals("") ) {
            c.append(state);
        } else if(  ! city.trim().equals("") ) {
            c.append(state.trim().equals("") ? "" : ", "+ state);
            c.append(zip.trim().equals("") ? "" : ", "+ zip);
        } else if(  city.trim().equals("") ) {
            c.append(state);
            if(state.toString().trim().equals(""))
                c.append(zip);
            else {
                c.append(zip.trim().equals("") ? "" : ", "+ zip);                
            }
        }

        return c.toString();
    }

}
