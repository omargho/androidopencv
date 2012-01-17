package com.pfe.okassa;

import java.util.ArrayList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import com.pfe.okassa.Projet_Service;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Toast;



public class Projet_Main extends Activity {
	
	LayoutInflater controlInflater = null;
	Message message ;
	MyReceiver myReceiver;
	public  Mat img ;
	public ProgressDialog mProgressDialog;
	
  	public void loadImage(Mat img)
   	{
   		Toast.makeText(getBaseContext(),"Chargement de l'image... ", Toast.LENGTH_LONG).show();
   				   		
   		
   		if ( img.empty() == true)
   		{	
   			Toast.makeText(getBaseContext(),"Chargement echec... ", Toast.LENGTH_LONG).show();
   		}
   		else 
   		{
   			Toast.makeText(getBaseContext(),"Chargement de l'image succes... ", Toast.LENGTH_LONG).show();  
   			
   		}	
   	}

	
  	  	
  	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        /*Original Contentview*/
        setContentView(new ProjetView(this));

        getWindow().setFormat(PixelFormat.UNKNOWN);
       
        controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.control, null);
        LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
        this.addContentView(viewControl, layoutParamsControl);
        
      //Création d'une instance de ma classe ImagesBDD
       // Projet_ImagesBDD imageBdd = new Projet_ImagesBDD(this);
 
        //Création d'un image
        //Projet_Image image = new Projet_Image(null, null, null);
 
        //On ouvre la base de données pour écrire dedans
        //imageBdd.open();
        //On insère l'image que l'on vient de créer
       // imageBdd.insertImage(image);
      
    }
      
    
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
				
		//Register BroadcastReceiver
	      //to receive event from our service
	      myReceiver = new MyReceiver();
	      IntentFilter intentFilter = new IntentFilter();
	      intentFilter.addAction(Projet_Service.MY_ACTION);
	      registerReceiver(myReceiver, intentFilter);
	      Bundle b = new Bundle();
	      
	       ((Button)findViewById(R.id.mbuttonAnalyse)).setOnClickListener(new View.OnClickListener(){
	    	   
	   		@Override
	        public void onClick(View v) {
	           	  
	   			//Mat img = ProjetView.mRgba ;
	   			Bitmap bmp = ProjetView.bmp ;
	   			      
	   			Mat img =  Utils.bitmapToMat(bmp) ;
	             	   			
	           	 //Highgui.imwrite("/mnt/sdcard/DCIM/Camera/picture.bmp", img) ;
	           	 loadImage(img) ;     
	           	 Intent i= new Intent(Projet_Main.this, Projet_Service.class);
	           	
	           	// i.putExtra("INIT_DATA", "Data passed from Activity to Service in startService"); 
	            // startService(i) ;      	 
	                }
	          });
	}


    @Override
    protected void onStop() {
     // TODO Auto-generated method stub
     unregisterReceiver(myReceiver);
     super.onStop();
    }
    
	private class MyReceiver extends BroadcastReceiver{
    	 
    	 @Override
    	 public void onReceive(Context arg0, Intent arg1) {
    	  // TODO Auto-generated method stub
    	  
    	  String orgData = arg1.getStringExtra("DETECTFEATURES");
    	  
    	  Toast.makeText(Projet_Main.this,
    	    "Triggered by the Computing Service : !\n"
    	    + orgData,
    	    Toast.LENGTH_LONG).show();
    	   }
    	}
              
}