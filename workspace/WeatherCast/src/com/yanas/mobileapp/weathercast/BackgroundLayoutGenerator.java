package com.yanas.mobileapp.weathercast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;



public class BackgroundLayoutGenerator {

    
    private int rows;
    private int columns;
    private int cellType;
    
    TableLayout tableLO;
    
    Context thisBuildAct;
    
    public BackgroundLayoutGenerator(int rows_in, int columns_in, int cellType_in,
            Context thisBuildAct_in ) {
        rows = rows_in;
        columns = columns_in;
        cellType = cellType_in;
        
        thisBuildAct = thisBuildAct_in;
    }
    
    public TableLayout generateTableLayout() {
        tableLO = new TableLayout(thisBuildAct);
        LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams trparams = 
                new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 
                                          TableRow.LayoutParams.WRAP_CONTENT);

        View iv = null;
        TableRow tr = null;
        for(int row=1; row <= rows; row++) {
            tr = new TableRow(thisBuildAct);
            tr.setLayoutParams(params);

            for(int col=1; col <= columns; col++) {
                iv = new Button(thisBuildAct);
                iv.setLayoutParams(trparams);
                if( row % 2 == 0) {
                    if((col % 2 != 0) ) {
                        ((Button)iv).setBackgroundColor(Color.BLACK); 
                    }
                    else {
                        ((Button)iv).setBackgroundColor(Color.BLACK); //  -16711936); // green
                    }
                }
                else {
                    if( col % 2 == 0 ) { 
                        ((Button)iv).setBackgroundColor(Color.BLACK); // .setBackgroundResource(R.drawable.road_section);
                    }
                    else {
                        ((Button)iv).setBackgroundColor(Color.BLACK); //  -16711936); // green
                    }
                }
                
                tr.addView(iv);
            }
            tableLO.addView(tr);
        }
        
        return tableLO;
    }

    public TableLayout getTableLO() {
        return tableLO;
    }

    public void setTableLO(TableLayout tableLO) {
        this.tableLO = tableLO;
    }
    

}
