package com.rproyart.okassa;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
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
         static final String ACTION_BUTTON = "BUTTON ENABLE";
         private static final int ALERT_DIALOG = 1;
         String text_to_show ;
         
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
        
        
        
        // aficher les infos a la demande
        
        ((Button) findViewById( R.id.button1 ) )
		.setOnClickListener( new OnClickListener()
		{

			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog( ALERT_DIALOG );
			}

		}
	    );
        
        // retour vers l'activité de base
        
        ((Button) findViewById( R.id.button2 ) )
		.setOnClickListener( new OnClickListener()
		{

			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent i= new Intent(AnswerActivity.this, InfosImage.class);
	   			 Log.i("ANSWER ACTIVITY TO MAIN ACTIVITY", "starting service");
	   			 Log.i("ANSWER ACTIVITY TO MAIN ACTIVITY", "launching service");
	   			 startActivity(i) ;
			}

		}
	    );
        
        
        mReceiver = new BroadcastReceiver() {

                        @Override
                        public void onReceive(Context context, Intent intent) {
                                // TODO Auto-generated method stub
                                  if (intent.getAction().equals(ACTION_UPDATE))
                                  {
                                          /*
                                           * On dessine l'image que l'on a trouvé
                                           */
                                          
                                          drawPic(Path + intent.getStringExtra("image_name"),
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
                Log.i("DRAW PICTURE", "get the layout...");
                View mlayout = findViewById(R.id.RelativeLayout1);

                Log.i("DRAW PICTURE", "decoding the file from path= "+imgFile.getAbsolutePath());
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Log.i("DRAW PICTURE", "bmp size is ="+myBitmap.getWidth()+"*"+myBitmap.getHeight());
            
            Log.i("DRAW PICTURE", "get drawable...");
                Drawable d =new BitmapDrawable(myBitmap);
                Log.i("DRAW PICTURE", "drawable visibility is  ="+ d.isVisible());
                mlayout.setBackgroundDrawable(d) ;
               
        }
        else
        {
                Log.i("DRAW PICTURE", "file doesn't exist...");
                
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
                                                
                                                strBuild.append(line+"\n");
                                            
                                       
                                        }
                                } 
                                catch (IOException e) 
                                {
                                       // TODO Auto-generated catch block
                                        e.printStackTrace();
                                } 
                    
                        // on affiche les infos contenues dans 
                        // le fichier d'infos de l'image
                                
                        text_to_show = strBuild.toString() ;
                        showDialog( ALERT_DIALOG );
                        Log.i("DRAW PICTURE", "infos to show to UI ="+strBuild);        
                        //textView.setText(strBuild) ;
                        
                        // sending data to enable button clickable
         	   			Intent iBis = new Intent(ACTION_BUTTON);
                        iBis.putExtra("button_enable", "false");
                        Log.i("ANSWER BUTTON ", "true enable button");
                        mLocalBroadcastManager.sendBroadcast(iBis);
                         
                        
              
    }

    
    
    /**
     * Dialogue dans laquelle$
     * on affiche les infos
     * 
     */
    
    
    @Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		String text =  text_to_show;
		
		Dialog dialog = null;
		if ( id == ALERT_DIALOG )
		{
			ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.style.MyTheme );
			AlertDialog.Builder builder= new AlertDialog.Builder( ctw );
			builder.setMessage(text)
			    .setTitle( "Informations sur l'image" )
				.setCancelable( false )
				.setPositiveButton( "Close",
						new DialogInterface.OnClickListener()
						{

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
							
						} 
			);
			dialog = builder.create();
		}
		if ( dialog == null )
		{
			dialog = super.onCreateDialog( id );
		}
		return dialog;
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
