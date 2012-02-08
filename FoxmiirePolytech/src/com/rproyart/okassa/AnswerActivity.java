package com.rproyart.okassa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
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
    
	String Path = "/FoxmiirePolytech/assets/" ;
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
        imgView = (ImageView)findViewById(R.id.imageView1);
        
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
        File imgFile = new  File(Path+nom);
        String line; 
        File yourFile ;
        FileReader filereader;
        
        
        if(imgFile.exists())
        {
           imgView.setImageURI(Uri.fromFile(imgFile));
            
           yourFile = new File(infos);
 	       
 	       Log.i("DRAW PICTURE", "reading file for images titles");
 	       
 	       
		try {
			   filereader = new FileReader(yourFile);
	 	       Log.i("DRAW PICTURE", "get the file of infos...");
	 	       
	 	       BufferedReader br = new BufferedReader(filereader);
	 	       
	 	       Log.i("DRAW PICTURE", "read file of infos...");
	 	       try {
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
		        
		        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	       

        }
    }

    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		 //drawing the bmp on the ImageView
        Log.i("INFOS ACTIVITY CREATE", "retrieve the good image");

		
	}


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }

}
