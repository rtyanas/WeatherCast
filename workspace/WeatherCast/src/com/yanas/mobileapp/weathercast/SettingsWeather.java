package com.yanas.mobileapp.weathercast;

public class SettingsWeather {
    
    public enum PanelSelectionEnum {
        SINGLE_PANE, QUAD_PANE_ONLY_GRAPHICS, QUAD_PANE_ONLY_NUMBERS
    };

    public PanelSelectionEnum panelSelect;

    public SettingsWeather() {
        
        panelSelect = PanelSelectionEnum.SINGLE_PANE ;
        
    }

    public PanelSelectionEnum getPanelSelect() {
        return panelSelect;
    }

    public void setPanelSelect(PanelSelectionEnum panelSelect) {
        this.panelSelect = panelSelect;
    }
}
