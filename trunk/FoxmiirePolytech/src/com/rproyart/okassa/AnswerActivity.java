package com.rproyart.okassa;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v4.content.LocalBroadcastManager;

/**
 * Cette classe permet de gerer l'affichage de 
 * l'image que l'on a trouvé en utilisant la 
 * comparaison d'hisrtogrammes
 * 
 * @author olympe kassa & romain proyart 
 * 
 */


public class AnswerActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    
	String Path = "/data/data/com.rproyart.okassa/app_asset_to_local/";
	LocalBroadcastManager mLocalBroadcastManager;
	BroadcastReceiver mReceiver;
	ImageView imgView ;
	TextView textView ;
	 static final String ACTION_STARTED = "STARTED";
	 static final String ACTION_UPDATE = "UPDATE";
	 static final String ACTION_STOPPED = "STOPPED";
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        Log.i("INFOS ACTIVITY CREATE", "super oncreate");
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.i("INFOS ACTIVITY CREATE", "getting all surfaceholder");
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Log.i("INFOS ACTIVITY CREATE", "frame orientation");
        
        setContentView(R.layout.identifi); 
        
        
		Context context = this.getBaseContext();
        imgView = new ImageView(context);
       
        
        textView = new TextView(context) ;
        textView = (TextView)findViewById(R.id.textView1) ;
        
        // We use this to send broadcasts within our local process.
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this) ;

        // We are going to watch for interesting local broadcasts.
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATE);
        
        
        mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				  if (intent.getAction().equals(ACTION_UPDATE))
				  {
					  /*
					   * On dessine l'image que l'on a trouvé
					   */
					  
					  drawPic(intent.getStringExtra("image_name"),
							  intent.getStringExtra("image_infos")) ;		  
	              } 
			}
        };
        mLocalBroadcastManager.registerReceiver(mReceiver, filter);

    }
    
    /**
     * cette fonction permet de dessiner sur notre vue 
     * finale l'image que l'on a trouvé et ses informations 
     * stockées dans un fichier 
     * 
     * 
     * @param nom
     * @param infos (correspond a l'uri)
     * 
     * @author olympe kassa & romain proyart
     */
    
    public void drawPic(String nom, String infos )
    {
    	StringBuilder strBuild = new StringBuilder();
        File imgFile = new  File(nom);
        String line; 
      
        /*
         * on affiche l'image trouvée dans 
         * le background de la vue et les infos dans 
         * une fenetre dialogue
         * 
         */
        if(imgFile.exists())
        {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgView = (ImageView)findViewById(R.id.imageView1);
            imgView.setImageBitmap(myBitmap);
        }
        
        /*
         * on recupère le text correspondant à l'image
         * 
         */
         
 	       Log.i("DRAW PICTURE", "reading file for images titles");
	
			   // get input stream for text
			   // infos = Path_infos+infos_file stored in database
			   // Path_infos = InfosFolder/ 
			
	 	       try {
	 	    	   
	 	    	  Log.i("DRAW PICTURE", "get the file of infos...");
	 	    	  BufferedReader  br = new BufferedReader(
	 	 	    		   new InputStreamReader(getAssets().open(infos)));
	 	    	  
	 	    	 Log.i("DRAW PICTURE", "read file of infos...");
	 	    	   		while((line = br.readLine()) != null) 
	 	    	   		{
	 	    	   			
	 	    	   			strBuild.append(line);
				       
	 	    	   		}
	 	       		} 
	 	       		catch (IOException e) 
	 	       		{
				       // TODO Auto-generated catch block
	 	       			e.printStackTrace();
	 	       		} 
	            
	            // on affiche les infos contenues dans 
	 	        // le fichier d'infos de l'image
	 	       	Log.i("DRAW PICTURE", "infos to show to UI ="+strBuild);	
		        textView.setText(strBuild) ;
		        
 	      
    }

    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		 //drawing the bmp on the ImageView
        Log.i("INFOS ACTIVITY CREATE", "retrieve the good image");

        Log.i("ACTIVITY START", "set up filter of intents");
	    IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction(InfosImageService.ACTION_UPDATE);
	    
	    Log.i("ACTIVITY START", "we register to receiver");
	    registerReceiver(mReceiver, intentFilter);

	}


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }
    
    
    
    

}
