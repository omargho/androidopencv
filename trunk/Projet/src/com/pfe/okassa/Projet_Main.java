package com.pfe.okassa;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import com.pfe.okassa.Projet_Service;
import com.pfe.okassa.Projet_Service.MyThread;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;



public class Projet_Main extends Activity {
	
	LayoutInflater controlInflater = null;
	Message message ;
	MyReceiver myReceiver;
	public static Mat img;
	public ProgressDialog mProgressDialog;
	public static boolean click ;
	
	ProjetView view ;
	
	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
	String fileName = "myPicture.jpg";
	
	

	//=========================================================SERVICE ================================================================
	
	
	
	//============================================== SUITE ACTIVITE ==========================================================
  	
	public Mat loadImage(String imgName)
   	{
   		   		
   		Mat img = Highgui.imread(imgName) ;
   		
   		if ( img.empty() == true)
   		{	
   			Toast.makeText(getBaseContext(),"Chargement echec activité... ", Toast.LENGTH_LONG).show();
   		}
   		else 
   		{
   			Toast.makeText(getBaseContext(),"Chargement de l'image OK... ", Toast.LENGTH_LONG).show();
   		}
		return img;	
   	}

	
  	  	
  	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        click = false ;
        /*Original Contentview*/
        view = new ProjetView(this) ;
        setContentView(view);
        
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
	      final String path_ = Environment.getExternalStorageDirectory() + "/DCIM/Camera/picture.jpg";
	      
	       ((Button)findViewById(R.id.mbuttonAnalyse)).setOnClickListener(new View.OnClickListener(){
	    	   
	   		@Override
	        public void onClick(View v) {
	   				   			
	   			click = true ;
	   			
	   			Toast.makeText(getBaseContext(),"Picture saved... ", Toast.LENGTH_LONG).show();
	   			//img = loadImage(path_) ;
	   			
	           	 Intent i= new Intent(Projet_Main.this, Projet_Service.class);
	           	 Toast.makeText(getBaseContext(),"Starting service... ", Toast.LENGTH_LONG).show();
	           	 i.putExtra("INIT_DATA", "Picture passed from Activity to Service in startService"); 
	             startService(i) ;      	 
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
    	  
    	  Toast.makeText(Projet_Main.this, orgData,Toast.LENGTH_LONG).show();
    	   }
    	}
              
}