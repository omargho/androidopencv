package com.rproyart.okassa;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

/**
 * Cette classe permet d'effectuer l'ouverture de la camera
 * pour effectuer la capture de l'image. Mais aussi le peuplement
 * de la base de données avec les images stockées dans le 
 * fichier assets. 
 * 
 * 
 * 
 * @author olympe kassa  & romain proyart
 *
 */



public class InfosImageCamera extends OrmLiteBaseActivity<DatabaseHelper> {
	
		
	// this var is  used to control the view in a viewgroup
	LayoutInflater controlInflater = null;
	
	// the receiver we us for listening messages from
	//other activities or services
	MyReceiver myReceiver;
	
	// this var is  used to store the main image in OpenCV format
	public static Mat img;
	
	// the progress dialog is used to show to 
	// the user that computation is performing
	public ProgressDialog mProgressDialog;
	
	// var used to catch the picture
	public static boolean click ;
	
	// var used to enable the analyse button
	public static boolean enable ;
	
	// path of images we stored in the database	
	String Path_Image = "ImageFolder/" ;
	
	// path of file used to describe images
	String Path_infos = "InfosFolder/";
	
	
    // we use a local broadcast receiver to manage 
	// messages inside the application
	 LocalBroadcastManager mLocalBroadcastManager;
	 
	// we use a local broadcast receiver to catch only 
	// messages inside the application
     BroadcastReceiver mReceiver;
	
     
     static final String ACTION_BUTTON = "BUTTON ENABLE";
     
 	/**
 	 * 
 	 *  Compute histogramm of an 
 	 *  image to compare with others 
 	 *  
 	 *  @author olympe kassa
 	 *  */ 
 	
 public byte[] computeHist(Mat img)
 	{
	 
	 ArrayList<Mat> images = new ArrayList<Mat>();
	 List<Integer> channels = new ArrayList<Integer>();
	 List<Integer> histSize = new ArrayList<Integer>();
	 List<Float> ranges = new ArrayList<Float>();           
	                
     images.add(img);
     channels.add(0);
	 histSize.add(10);
	 ranges.add(0.0f); ranges.add(256.0f);
	 Mat hist = new Mat();
	 Log.i("SERVICE HISTOGRAMS ACTIVITY", "computing begin...");
	 Imgproc.calcHist(images, channels, new Mat(), hist, histSize, ranges);
	 Log.i("SERVICE HISTOGRAMS ACTIVITY", "histograms size is = " + histSize);
	 
	 Mat m = new Mat() ;
	 
	 hist.convertTo(m,CvType.CV_32SC1) ;
	 Log.i("SERVICE HISTOGRAMS ACTIVITY", "mat converted size ="+ m.size());
	 
	 List<Integer> is = new ArrayList<Integer>() ;
	 Converters.Mat_to_vector_int(m, is) ;
	 Log.i("SERVICE HISTOGRAMS ACTIVITY", "Matrix coveter size ="+is.size());
	 	 
	 byte[] bytes = null;
	  ByteArrayOutputStream bos = new ByteArrayOutputStream();
	  try 
	  {	  
	    ObjectOutputStream oos = new ObjectOutputStream(bos); 
	    oos.writeObject(is);
	    oos.flush(); 
	    oos.close(); 
	    bos.close();
	    bytes = bos.toByteArray ();
	  }
	  catch (IOException ex) {
	    //TODO: Handle the exception
	  }

	 Log.i("SERVICE HISTOGRAMS ACTIVITY", " byte conveter size ="+ bytes.length);
	
 	 return bytes ;
 	}
 
 public String loadDataImageFileFromAsset(String image) 
 {
		// load text
	 	try {
	 	    	// get input stream for text
		    	InputStream is = getAssets().open(image);
		    	// check size
		    	int size = is.available();
		    	// create buffer for IO
		    	byte[] buffer = new byte[size];
		    	// get data to buffer
		    	is.read(buffer);
		    	// close stream
		    	is.close();
		    	
		    	 Log.i("POPULATE IMAGE DATABASE", "reading file for images titles");	
	 	}
	 	catch (IOException ex) {
	 		
	 	}
	 	 
	return image;
	 
 }
 
 /*
  * idée utiliser le storage interne pour stocker les images 
  * 
  */
 public String loadDataImageFromAsset(String image) 
 {
	// utiliser storage  
	 
	 String _path = "/data/data/com.rproyart.okassa/app_asset_to_local/";

 	// load image
 	try {
 			
	    	// get input stream of image
 		    
 			InputStream ims = getAssets().open(Path_Image + image) ;
	    	
 			File mydir = getBaseContext().getDir("asset_to_local", Context.MODE_PRIVATE); //Creating an internal dir;
 			File fileWithinMyDir = new File(mydir, image); //Getting a file within the dir.
 			FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file

 			Log.i("LOAD DATA FROM ASSETS", "name of image ="+"ImageFolder/"+image);
	    	
	    	Log.i("LOAD DATA FROM ASSETS", "get image byteArray");
	    	byte[] data = new byte[ims.available()];
	    	
	    	Log.i("LOAD DATA FROM ASSETS", "bmp is decoded");       	 	
            
                    
            try 
            {
                ims.read(data);
                out.write(data);
                ims.close();
                out.close() ;    
            } 
            catch (FileNotFoundException e) {
                Log.e("convert image", "FileNotFoundException", e);
            } 
            catch (IOException e) {
                Log.e("convert image", "IOEception", e);
            } 	
 	}
 	catch(IOException ex) {
 		
 	}
 	
 	return _path+image ;
 }
 
	
	/**
	 * Cette fonction est utilisée pour peupler
	 * la base de données de notre application
	 * au démarrage.
	 * 
	 * 
	 * @param image_path
	 * @author olympe kassa
	 */
 
 public void PopulateDataBase()
 	{
 		Log.i("POPULATE DATABASE", "loading images into database");
            	// lecture du fichier des titres des images
            	try { 
            			
            		Log.i("POPULATE DATABASE", "reading titles of " +
            	       		"images ");
            		            		            	       
            	       BufferedReader  br = new BufferedReader(
            	    		   new InputStreamReader(getAssets().open("titresImages.txt"))); 
            	       
            	       String line; 
            	       
            	       Log.i("POPULATE DATABASE", "start reading images...");
            	       while((line = br.readLine()) != null) 
            	       {
            	    	   Log.i("POPULATE DATABASE", "nom image="+line);
            	    	   
            	    	   // on récupère le chemin de l'image et 
            	    	   // le titre de l'image et on on ajoute 
            	    	   // l'image a la base de données
            	    	   
            	    	   // on récupère le nom de l'image pour avoir son fichier
            	    	   // d'infos
            	    	               	    	   
            	    	   int mid= line.lastIndexOf(".");
            	    	   String fname=line.substring(0,mid);
            	    	   String ext ="_file.txt" ;
            	    	    
            	    	   Log.i("POPULATE DATABASE", "nom image sans extension="+fname);
            	    	   String infos_file = fname + ext ;
            	    	   Log.i("POPULATE DATABASE", "nom fichier infos ="+infos_file);
            	    	    
            	    	   PopulateDataBaseImage(loadDataImageFromAsset(line), line,Path_infos+infos_file);
            	        } 
            	           br.close();
            	       }
            	      catch (IOException e) 
            	     { 
            	        e.printStackTrace(); 
            	     }
            	               
 	}
 	
 	/**
 	 *  Cette fonction est utilisée pour inserer dans la
 	 *  base de données une image contenue dans le 
 	 *  dossier assets de notre application
 	 * 
 	 * @param image_path
 	 * @param image_name
 	 */
 
public void PopulateDataBaseImage(String image_path, String image_name,String infos_Image_Path)
{
		  
		 Log.i("POPULATE DATABASE IMAGE", "loading image");
		 Mat img = Highgui.imread(image_path) ;
	   		
	   		if ( img.empty() == true)
	   		{	
	   			Log.i("POPULATE DATABASE IMAGE", "image is empty");
	   		}
	   		else
	   		{
	   			Log.i("POPULATE DATABASE IMAGE", "image loaded");
	   		}
	   		
		 
		 
		 Log.i("POPULATE DATABASE IMAGE", "type de la matrice ="+ img.type());
		

	     Log.i("POPULATE DATABASE IMAGE", "computing histogramms");
	     byte[] hist = computeHist(img) ;
	    
	     Log.i("POPULATE DATABASE IMAGE", "histogramms");
	                                     
	 		 
	    // get the dao
	    RuntimeExceptionDao<Image, Integer> simpleDao = getHelper().getSimpleDataDao();                                 
	            
	    Log.i("POPULATE DATABASE IMAGE", "creating new image into database ");
	    
	    // add the current image to the database
		Image image = new Image(hist,image_name,infos_Image_Path);

	    // create some entries in the onCreate
	    Log.i("POPULATE DATABASE IMAGE", "inserting the new image");
	    simpleDao.create(image);

}
	
  	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        Log.i("INFOS ACTIVITY CREATE", "super oncreate");
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.i("INFOS ACTIVITY CREATE", "getting all surfaceholder");
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Log.i("INFOS ACTIVITY CREATE", "frame orientation");
               
        click = false ;
        
        /*Original Contentview*/
        Log.i("INFOS ACTIVITY CREATE", "we had the contentView");
        
                              
        setContentView(new InfosImageView(this));
        getWindow().setFormat(PixelFormat.JPEG);
       
        
        // Receuver of answer activity
        
        // We use this to send broadcasts within our local process.
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this) ;

        // We are going to watch for interesting local broadcasts.
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_BUTTON);
        
        
        mReceiver = new BroadcastReceiver() {

                        @Override
                        public void onReceive(Context context, Intent intent) {
                                // TODO Auto-generated method stub
                                  if (intent.getAction().equals(ACTION_BUTTON))
                                  {
                                          /*
                                           * On dessine l'image que l'on a trouvé
                                           */
                                	  String orgData = intent.getStringExtra("button_enable");
                                	  if(orgData.equals("false"))
                                	  {
                                		  enable = false ;
                                	  }
                                	  
                            		     Log.i("MyReceiver from infosImage", orgData);     
                                                           
                                    } 
                        }
        };
        mLocalBroadcastManager.registerReceiver(mReceiver, filter);
        
        
     
        Log.i("ACTIVITY CREATE", "reglage de l'image");
        controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.control, null);
        LayoutParams layoutParamsControl = new 
        LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
        this.addContentView(viewControl, layoutParamsControl);
        Log.i("ACTIVITY CREATE", "reglage OK"); 
        
		 Log.i("ACTIVITY CREATE", "POPULATING THE DATABASE");
		 this.PopulateDataBase() ;
		
    }
    
   
    
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
    	Log.i("ACTIVITY START", "start");
    	super.onStart();
		
    	Log.i("ACTIVITY START", "receiver set up");
		myReceiver = new MyReceiver();
		   
		Log.i("ACTIVITY START", "set up filter of intents");
	    IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction(InfosImageService.MY_ACTION);
	    
	    Log.i("ACTIVITY START", "we register to receiver");
	    registerReceiver(myReceiver, intentFilter);
	    	 
	    // var for button : enable or disable
	    enable = true ;
	    Button bt = (Button)findViewById(R.id.mbuttonAnalyse) ;
	    bt.setVisibility(View.VISIBLE);
	    (bt).setOnClickListener(new View.OnClickListener(){
	      
	   		@Override
	        public void onClick(View v) {
	   			 
	   			 Log.i("ACTIVITY START", "button clicked");  			
	   			 click = true ;
	   			 Log.i("ACTIVITY START", "we wait a little bit to be sure the picture is saved");
	   			 SystemClock.sleep(1000) ;   			
	   			 Log.i("ACTIVITY START", "picture saved");
	   			 
	   			 //Launching a service to compute image recognition
	   			 Intent i= new Intent(InfosImageCamera.this, InfosImageService.class);
	   			 Log.i("ACTIVITY START", "starting service");
	   			 Log.i("ACTIVITY START", "launching service");
	   			 startService(i) ;
	   			 
	   			 // on desactive le bouton
	   			 
	   			 Intent iBis = new Intent(InfosImageCamera.this, AnswerActivity.class);
   	   			 Log.i("ACTIVITY START", "starting AnswerActivity");
   	   			 Log.i("ACTIVITY START", "launching AnswerActivity");
   	   			 startActivity(iBis);
	   			  			 
	             } 
	          });
	    
	    Log.i("ACCEUIL ACTIVITY CREATE", "finish the activity camera");
	    
	    
	    // si le button est activé et cliqué 
	    // alors on le desactive
	    
	    if((enable == true) && (click == true))
	    {
	    	// disable click click event
	    	bt.setClickable(false) ;
	    }
	    
	    if(enable == false)
	    {
	    	// enable click event
	    	bt.setClickable(true) ;
	    }
    }


    @Override
    protected void onStop() {
     // TODO Auto-generated method stub
     unregisterReceiver(myReceiver);
     super.onStop();
     Log.i("ACTIVITY STOP", "stop activity");
    }
    
	private class MyReceiver extends BroadcastReceiver{
    	 
    	 @Override
    	 public void onReceive(Context arg0, Intent arg1) {
    		 
    		 
    	  // TODO Auto-generated method stub    		 
    		 
    		 if(arg1.hasExtra("DETECTFEATURES")){
    			 String orgData = arg1.getStringExtra("DETECTFEATURES");
       		     Log.i("MyReceiver", orgData);
    		 }
    		 
    		 if(arg1.hasExtra("SHOWPROGRESSBAR")){
    			 ProgressBar progressbar = (ProgressBar)findViewById(R.id.progressBar1);
        		 progressbar.setVisibility(View.VISIBLE);
       		     Log.i("MyReceiver", "BROADCAST TYPE SHOWPROGRESSBAR");
    		 }
    		 
    		 if(arg1.hasExtra("HIDEPROGRESSBAR")){
    			 ProgressBar progressbar = (ProgressBar)findViewById(R.id.progressBar1);
        		 progressbar.setVisibility(View.INVISIBLE);
       		     Log.i("MyReceiver", "BROADCAST TYPE HIDEPROGRESSBAR");
    		 }
    		     	  
    	   }
    	}
    
}