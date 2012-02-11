package com.rproyart.okassa;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;



/**
 *  Activité d'acceuil de notre application
 *  on touche l'écran pour ouvrir la camera 
 * 
 * @author olympekassa
 *
 */
public class InfosImageAcceuil extends Activity {

	
	private View myView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Log.i("ACCEUIL ACTIVITY CREATE", "getting all surfaceholder");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        
        Log.i("ACCEUIL ACTIVITY CREATE", "frame orientation");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
		
        Log.i("ACCEUIL ACTIVITY CREATE", "set the content of the view acceuil");
		setContentView(R.layout.acceuil);
		
		Log.i("ACCEUIL ACTIVITY CREATE", "get the view user gonna touch");
        myView = findViewById(R.id.imageView1);
        
        Log.i("ACCEUIL ACTIVITY CREATE", "set ontouch listener");
        myView.setOnTouchListener(
        	new View.OnTouchListener() {
			public boolean onTouch(View myView, MotionEvent event) {
				
				 Log.i("ACCEUIL ACTIVITY CREATE", "intent activity for camera");
				Intent myIntent = new Intent(InfosImageAcceuil.this, 
						InfosImageCamera.class);
				
				Log.i("ACCEUIL ACTIVITY CREATE", "start the activity camera");
		        startActivity(myIntent);
		        
		        			
				// TODO Auto-generated method stub
				return true;
			}
		}     
        );    
	}
}