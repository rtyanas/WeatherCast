package com.yanas.mobileapp.weathercast;

import android.database.DatabaseUtils;
import android.util.Log;

import com.yanas.mobileapp.weathercast.datastore.CityListDbHelper;
import com.yanas.mobileapp.weathercast.datastore.UserSettingsDbData;
import com.yanas.mobileapp.weathercast.datastore.UserSettingsDbHelper;

public class SettingsWeather {
    
    public enum PanelSelectionEnum  {
        SINGLE_PANE(0), QUAD_PANE_ONLY_MIN_GRAPHICS(1), QUAD_PANE_ONLY_GRAPHICS(2), 
        QUAD_PANE_ONLY_NUMBERS(3);
        private int value;
        private PanelSelectionEnum(int val) {
            value = val;
        }
        public int getValue() {
            return value;
        }
    };

    private int id;
    private PanelSelectionEnum panelSelect;
    private int backgroundColor;
    private int cityStateListSort;
    UserSettingsDbData userSettingsDbData;
    private long countDbReturn;

    public SettingsWeather(UserSettingsDbData userSettingsDbData_in) {
        userSettingsDbData = userSettingsDbData_in;
        init();
    }
    
    public SettingsWeather() {
        userSettingsDbData = null;
        init();
    }
    
    private boolean init() {
        
        // Default values
        panelSelect = PanelSelectionEnum.QUAD_PANE_ONLY_NUMBERS;
        backgroundColor = 0;
        cityStateListSort = 0;
        return true;
    }
    
    public boolean getSettingsWeather() {

        SettingsWeather sw = userSettingsDbData.getSavedSettings();
        if(sw.getCountDbReturn() > 0) {
            panelSelect = sw.getPanelSelect();
            backgroundColor = sw.getBackgroundColor();
            cityStateListSort = sw.getCityStateListSort();
            if(GlobalSettings.main_activity) Log.d(SettingsWeather.class.getName(), "Found DB values found and loaded");
        }
        else {
            if(GlobalSettings.main_activity) Log.w(SettingsWeather.class.getName(), "DB values not found");
            init();
            userSettingsDbData.insertSetting(this);
        }

        return true;
    }
    
    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public long saveSettingsWeather() {
        return userSettingsDbData.updateSetting(this);
        
    }

    public PanelSelectionEnum getPanelSelect() {
        return panelSelect;
    }

    public int getPanelSelectInt() {
        return panelSelect.getValue();
    }

    public void setPanelSelect(PanelSelectionEnum panelSelect) {
        this.panelSelect = panelSelect;
    }
    
    public void setPanelSelect(int panelSelect) {
        this.panelSelect = PanelSelectionEnum.QUAD_PANE_ONLY_NUMBERS;
        
        if(panelSelect == PanelSelectionEnum.QUAD_PANE_ONLY_NUMBERS.getValue() ) {
            this.panelSelect = PanelSelectionEnum.QUAD_PANE_ONLY_NUMBERS;
        } 
        else if(panelSelect == PanelSelectionEnum.QUAD_PANE_ONLY_GRAPHICS.getValue() ) {
            this.panelSelect = PanelSelectionEnum.QUAD_PANE_ONLY_GRAPHICS;
        }
        else if(panelSelect == PanelSelectionEnum.QUAD_PANE_ONLY_MIN_GRAPHICS.getValue() ) {
            this.panelSelect = PanelSelectionEnum.QUAD_PANE_ONLY_MIN_GRAPHICS;
        }
        else if(panelSelect == PanelSelectionEnum.SINGLE_PANE.getValue() ) {
            this.panelSelect = PanelSelectionEnum.SINGLE_PANE;
        }
    }
    
    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getCityStateListSort() {
        return cityStateListSort;
    }

    public void setCityStateListSort(int cityStateListSort) {
        this.cityStateListSort = cityStateListSort;
    }

    public long getCountDbReturn() {
        return countDbReturn;
    }

    public void setCountDbReturn(long countDbReturn) {
        this.countDbReturn = countDbReturn;
    }

    
}
