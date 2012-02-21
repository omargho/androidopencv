package com.rproyart.okassa;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.view.ViewGroup;
import android.graphics.Matrix;
import java.lang.Math.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.support.v4.content.LocalBroadcastManager;

/**
 * Cette classe permet de gerer l'affichage de 
 * l'image que l'on a trouvé en utilisant la 
 * comparaison des descripteurs
 * 
 * 
 * @author olympe kassa & romain proyart
 * 			IMA5SC Polytech'lille 2012
 * 
 * @site https://code.google.com/p/androidopencv 
 * 
 */


/*
 * 
 * A faire :
 * 	dialog de choix de l'image retrouv�e
 *  afficher l'image et demander � l'utilisateur si
 *  c'est la bonne du genre OK ou NEXT.
 *  A l'utilisateur de choisir !
 *  
 *  Au lieu d'afficher les nom des images
 *  afficher plutot chaque image et demander 
 *  a l'utilisateur de choisir
 *  
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
         private static final int CHOOSE_IMAGE_ID = 0 ;
         private static final int PICK_IMAGE = 2 ;
         
        String text_to_show ;
		private String i_1;
		private String i_2;
		private String i_3;
		
		String i_1_infos ;
		String i_2_infos ;
		String i_3_infos ;
		
		int pic_ch ;
		Drawable d ;
		int cpt = 0 ;
		
  		Bitmap bp ;
		Bitmap bp_new ;
         
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
                                showDialog(ALERT_DIALOG );
                        }

                }
            );
        
        // retour vers l'activité de base
        
        ((Button) findViewById( R.id.button2 ) )
                .setOnClickListener( new OnClickListener()
                {

                        public void onClick(View v) {
                                // TODO Auto-generated method stub
                                 Intent i= new Intent(AnswerActivity.this, InfosImageAcceuil.class);
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
                                          i_1 = intent.getStringExtra("image_first_name") ;
                                          i_1_infos = intent.getStringExtra("image_first_infos") ;
                                          Log.i("ANSWER ACTIVITY TO MAIN ACTIVITY", "image 1 = "+ i_1 +
                                        		  "image 1 infos ="+ i_1_infos);
                                        
                                          i_2 = intent.getStringExtra("image_second_name") ;
                                          i_2_infos = intent.getStringExtra("image_second_infos") ;
                                          Log.i("ANSWER ACTIVITY TO MAIN ACTIVITY", "image 2 = "+ i_2 +
                                        		  "image 2 infos =" + i_2_infos);
                                        
                                          i_3 = intent.getStringExtra("image_third_name") ;
                                          i_3_infos = intent.getStringExtra("image_third_infos") ;
                                          Log.i("ANSWER ACTIVITY TO MAIN ACTIVITY", "image 3 = "+ i_3 +
                                        		  "image 3 infos = " +  i_3_infos);
                                          
                                                                                    
                                          showDialog(PICK_IMAGE);

                                          
//                                                           
                                    } 
                        }
        };
        mLocalBroadcastManager.registerReceiver(mReceiver, filter);

    }

    
    /**
     * On resize un bitmap pour l'afficher 
     * � l'utilisateur
     * 
     * @param srcBitmap
     * @param newWidth
     * @param newHeight
     * @return
     */

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

    int width = bm.getWidth();
    Log.i("DRAW PICTURE", "bmp width = "+ width);
    int height = bm.getHeight();
    Log.i("DRAW PICTURE", "bmp height = "+ height);
    
    float scaleWidth = ((float) newWidth) / width;
    Log.i("DRAW PICTURE", "scale width = "+ scaleWidth);
    float scaleHeight = ((float) newHeight) / height;
    Log.i("DRAW PICTURE", "scale height = "+ scaleHeight);

    // create a matrix for the manipulation

    Matrix matrix = new Matrix();

    // resize the bit map

    matrix.postScale(scaleWidth, scaleHeight);

    // recreate the new Bitmap

    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    Log.i("DRAW ALL PICTURE", "resized bmp size is ="+resizedBitmap.getWidth()+"*"+resizedBitmap.getHeight());
    
    return resizedBitmap;

    }





    
    public Bitmap drawPict(String nom)
    {
    		Bitmap bmp = null ;
    		File imgFile = new  File(nom);
            if(imgFile.exists())
            {
                Log.i("DRAW ALL PICTURE", "get the layout...");
                 //   View mlayout = findViewById(R.id.RelativeLayout1);

                Log.i("DRAW ALL PICTURE", "decoding the file from path= "+imgFile.getAbsolutePath());
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Log.i("DRAW ALL PICTURE", "bmp size is ="+myBitmap.getWidth()+"*"+myBitmap.getHeight());
                
                Log.i("DRAW ALL PICTURE", "get drawable...");
                bmp = myBitmap ;
               //     mlayout.setBackgroundDrawable(d) ;
                   
            }
            else
            {
                    Log.i("DRAW PICTURE", "file doesn't exist...");
                    
            }
    	
    	return bmp ;
    }
  
  
    public String infosImage (String infos)
    {
        /*
         * on recupère le text correspondant à l'image
         * 
         */
         
    	StringBuilder strBuild = new StringBuilder();
       
        String line; 
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
                                
                       
                        
                        Log.i("DRAW PICTURE", "infos to show to UI ="+strBuild);        
                                                
                        return strBuild.toString() ;
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
                d =new BitmapDrawable(myBitmap);
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
                        
                        Log.i("DRAW PICTURE", "infos to show to UI ="+strBuild);        
                        textView.setText(strBuild) ;
                        
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
                String img1 = i_1;
                String img2 = i_2;
                String img3 = i_3;
                Dialog dialog = null;
                Bitmap curr_bmp ;
                
                switch(id) 
                {
                	
                case ALERT_DIALOG:
                    // do the work to define the alert Dialog
                	
                	final Dialog d_ = new Dialog(AnswerActivity.this);

                	d_.setContentView(R.layout.infosedit);
                	d_.setTitle("Informations sur l'image");

                	TextView infos = (TextView) d_.findViewById(R.id.text);
                	infos.setText(text);
                	ImageView im = (ImageView) d_.findViewById(R.id.mini_image);
                	
                	im.setBackgroundDrawable(new BitmapDrawable(getResizedBitmap(bp,72,72))) ;
                	
                	dialog = d_ ;
                    break ;	
                case PICK_IMAGE :
                	
                	final Dialog d = new Dialog(AnswerActivity.this);
                	
            		d.setContentView(R.layout.choose);
            		d.setTitle( "       Is it the right image ?" ) ;
            		
            		final ImageView image = (ImageView) d.findViewById(R.id.image);
            		
            		bp = drawPict(Path + i_1) ;
            		Log.i("DRAW PICTURE", "old bmp size is ="+bp.getWidth()+"*"+bp.getHeight());
                    
            		final int newHeight = 208 ;
            		final int newWidth = 308 ;
            		
            		Log.i("DRAW PICTURE", "new bmp size is ="+newHeight+"*"+newWidth);
            		
            		bp_new = getResizedBitmap(bp,newHeight, newWidth);
            		//Log.i("DRAW PICTURE", "new bmp size is ="+bp_new.getWidth()+"*"+bp_new.getHeight());
                    
            		text_to_show = infosImage(i_1_infos) ;
            		
            		Button yes = (Button) d.findViewById(R.id.yes);
                    yes.setOnClickListener(new OnClickListener() {
                    @Override
                        public void onClick(View v) {
                    	                   	
                    	View mlayout = findViewById(R.id.RelativeLayout1);
                    	                    	
             	        mlayout.setBackgroundDrawable(new BitmapDrawable(bp)) ;
                    	d.dismiss();
                        }
                    });
            		
                    Button one = (Button) d.findViewById(R.id.one);
                    one.setOnClickListener(new OnClickListener() {
                    @Override
                        public void onClick(View v) {
                    				
                    				text_to_show = infosImage(i_1_infos) ;
                    				bp = drawPict(Path + i_1) ;
    	                    		bp_new = getResizedBitmap(bp,newHeight, newWidth);
    	                    		image.setImageBitmap(bp_new);
    	                    		
                        }
                    });
            		
                    Button two = (Button) d.findViewById(R.id.two);
                    two.setOnClickListener(new OnClickListener() {
                    @Override
                        public void onClick(View v) {
                    				
                    				text_to_show = infosImage(i_2_infos) ;
                    				bp = drawPict(Path + i_2) ;
    	                    		bp_new = getResizedBitmap(bp,newHeight, newWidth);
    	                    		image.setImageBitmap(bp_new);
    	                    		
                        }
                    });
                    
                   
                    Button three = (Button) d.findViewById(R.id.three);
                    three.setOnClickListener(new OnClickListener() {
                    @Override
                        public void onClick(View v) {
                    				
                    				text_to_show = infosImage(i_3_infos) ;
                    				bp = drawPict(Path + i_3) ;
    	                    		bp_new = getResizedBitmap(bp,newHeight, newWidth);
    	                    		image.setImageBitmap(bp_new);
    	                    		
                        }
                    });
                    
            		image.setImageBitmap(bp_new);
            		                	
            		dialog = d ;
            		
            		//dialog.show();
            		break ;
            		
                default:
                    dialog = null;
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
