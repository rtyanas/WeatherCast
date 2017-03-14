package com.yanas.mobileapp.weathercast;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class BackgroundActivity extends Activity {
	
	int rows = 10;
	int cols = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_background);
		
	    LinearLayout mainLayout = new LinearLayout(this);
	    mainLayout.setOrientation(LinearLayout.VERTICAL);

		if(GlobalSettings.backgroundActivity) Log.d("BackgroundActivity", "onCreate ");
	    TextView backgroundText = new TextView(this);
	    backgroundText.setText("Select Background" );
	    
	    LinearLayout buildingNameLay = new LinearLayout(this);
	    buildingNameLay.setBackgroundColor(Color.CYAN);
	    buildingNameLay.setHorizontalGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
	    buildingNameLay.addView(backgroundText);

	    mainLayout.addView(buildingNameLay, new ViewGroup.LayoutParams (
        		ViewGroup.LayoutParams.MATCH_PARENT, 
        		ViewGroup.LayoutParams.WRAP_CONTENT));
	    	    
	    BackgroundLayoutGenerator backgroundLO = new BackgroundLayoutGenerator(rows,cols,0,this);
	    backgroundLO.generateTableLayout();
	    initBackgroundButtonTable(backgroundLO);
	    
	    LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,  //.FILL_PARENT,
	            LayoutParams.WRAP_CONTENT);
		mainLayout.addView(backgroundLO.getTableLO(), params);

		addContentView(mainLayout, new ViewGroup.LayoutParams (
        		ViewGroup.LayoutParams.WRAP_CONTENT, 
        		ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	
	// Black, grays, light blue, dark blue, dark purple, light purple, light pink, dark pink, 
	// dark red, light red, dark orange light orange, light yellow, dark yellow, 
	// light green dark green 
	// Add grey, blue, orange
	int colors[] = {
			Color.BLACK,
			Color.GRAY,
			0xff767689, 0xff9C9CB5, // gray
			Color.DKGRAY,
			Color.WHITE,
			Color.BLUE,
			0xff2B55FA, 0xff1F45DC, 0xff1439CF, // light blue
			0xff777EF5, 0xff5F66E5, 0xff2C34D0, // light blue 
			0xff8452FA, 0xff5C28D6, 0xff4319A4, // purple
			0xffA4197D, 0xffD729A6, 0xffAF1C85, // dark purple
			Color.MAGENTA,
			0xffFA7BE7, 0xffF460DE, 0xffE827CB, // light pink
			0xffFC2529, 0xffC92023, 0xffAF1C1F, // red
			0xffFF772D, 0xffED671E, 0xffCB510F, // dark orange
			0xffFB8F2A, 0xffEE7E16, 0xffDB7210, // light orange 
			Color.YELLOW,
			0xffEAF42B, 0xffDFDF0F, 0xffC7C709, // light yellow
			Color.GREEN,
			0xff2BF46E, 0xff28D562, 0xff1C9D47, // light green
			0xff278145, 0xff608467, 0xff056380,  // dark green
			0xff1C9D95, 0xff2CC9BE, 0xff52FAEE, // light blue green
			// Color.RED,
			// Color.CYAN,
			Color.TRANSPARENT,
					
	};
    public void initBackgroundButtonTable(BackgroundLayoutGenerator backgroundLO) {

    	int tableChildCnt = backgroundLO.getTableLO().getChildCount();
    	View rowChild ;
    	View cellChild ;
    	int cellCount = 0;
    	int numColors = colors.length;
    	int colorCount = 0;
    	
    	for(int i = 0; i < tableChildCnt; i++) {
    		rowChild = backgroundLO.getTableLO().getChildAt(i);
    		if(rowChild instanceof TableRow) {
    			cellCount = ((TableRow) rowChild).getChildCount();
    			for(int j = 0; j < cellCount; j++) {
    				cellChild = ((TableRow) rowChild).getChildAt(j);
    				if(cellChild instanceof Button) {
    					if(colorCount < numColors) {
	    					cellChild.setOnClickListener(new ButtonClickListener(colors[colorCount]));
	    					cellChild.setBackgroundColor(colors[colorCount]);
	    					colorCount++;
    					}
    				}
    			}
    			
    		} // if(rowChild instanceof TableRow)
    	} // for - tableChildCnt
	
    }


    public class ButtonClickListener implements OnClickListener {
    	int color;
    	public ButtonClickListener(int color_in)  {
    		color = color_in;
    	}
    	
    	@Override
    	public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra(MainActivity.BACKGROUND, color );
			setResult(Activity.RESULT_OK, intent);
			finish();				
    		
    	}
    }



}
