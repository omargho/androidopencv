package com.rproyart.okassa;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class acceuil extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	 	super.onCreate(savedInstanceState);
        Log.i("SEND INFOS ACTIVITY CREATE", "super oncreate");
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.i("SEND INFOS ACTIVITY CREATE", "getting all surfaceholder");
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Log.i("SEND INFOS ACTIVITY CREATE", "frame orienation");

        /*Original Contentview*/
        Log.i("INFOS ACTIVITY CREATE", "we had the contentView");
   
        // vue d'affichage
        setContentView(R.layout.acceuil);
        
        getWindow().setFormat(PixelFormat.UNKNOWN);
       
        /**
         * Start activity of database
         */
		 
		 Log.i("ACTIVITY CREATE", "BROADCAST TYPE DATABASE");
		 
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	

}
