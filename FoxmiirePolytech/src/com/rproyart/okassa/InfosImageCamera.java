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
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;


/**
 * 
 * Cette classe permet de gerer l'ouverture
 * de la camera OpenCV, et de capturer a l'image 
 * que l'on capture pour effectuer le traiatement 
 * d'image avec les fonctions OpenCV 
 * 
 * 
 * @author olympe kassa & romain proyart
 * 			IMA5SC Polytech'lille 2012
 * 
 * @site https://code.google.com/p/androidopencv
 *
 */

public class InfosImageCamera extends OrmLiteBaseActivity<DatabaseHelper> {
        
        String MAIN  ;
        LayoutInflater controlInflater = null;
        Message message ;
        public static Mat img;
        public ProgressDialog mProgressDialog;
        public static boolean click ;
        public static boolean enable ;
        InfosImageView view ;
        SurfaceView v1  ;
        SurfaceView v2 ;
        
        String Path_Image = "ImageFolder/" ;
        String Path_infos = "InfosFolder/";
        
        
         LocalBroadcastManager mLocalBroadcastManager;
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
         List<Float> range = new ArrayList<Float>();           
         
         images.add(img);         
         channels.add(0);
         histSize.add(50);
         range.add(0.0f);
         range.add(256.0f);
         
         Mat hist = new Mat();
         Log.i("SERVICE HISTOGRAMS ACTIVITY", "computing begin...");
         Imgproc.calcHist(images, channels, new Mat(), hist, histSize, range);
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
                        // set result to TextView
                         Log.i("POPULATE IMAGE DATABASE", "reading file for images titles");    
                }
                catch (IOException ex) {
                        
                }
                 
        return image;
         
 }
 
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
                           
                           // beug a voir : eviter de rajouter des lignes
                           // a titresImages.txt car la ligne ligne vide entraine une 
                           // JavaNullPointerException
                           
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
                
                // on recupere l'lélément que l'on charge en db 
                
        }
        
        /**
         *  Cette fonction est utilisée pour inserer dans la
         *  base de données une image contenue dans le 
         *  dossier assets de notre application
         * 
         * @param image_path
         * @param image_name
         * 
         * 
		 * @author olympe kassa & romain proyart
		 * 			IMA5SC Polytech'lille 2012
		 * 
		 * @site https://code.google.com/p/androidopencv
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
                
    }
    
   
    
    @Override
        protected void onStart() {
                // TODO Auto-generated method stub
	        Log.i("ACTIVITY START", "start");
	        super.onStart();
	                
	        Log.i("ACTIVITY START", "receiver set up");
                   
            Log.i("ACTIVITY START", "set up filter of intents");
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(InfosImageService.MY_ACTION);
            
            Log.i("ACTIVITY START", "we register to receiver");
                 
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

    
    public boolean onCreateOptionsMenu(Menu menu) {
    	 
        //Création d'un MenuInflater qui va permettre d'instancier un Menu XML en un objet Menu
        MenuInflater inflater = getMenuInflater();
        //Instanciation du menu XML spécifier en un objet Menu
        inflater.inflate(R.layout.menu, menu);
        return true;
     }
 
       //Méthode qui se déclenchera au clic sur un item
      public boolean onOptionsItemSelected(MenuItem item) {
         //On regarde quel item a été cliqué grâce à son id et on déclenche une action
    	  RuntimeExceptionDao<Image, Integer> Dao = getHelper().getSimpleDataDao(); 
         switch (item.getItemId()) {
            case R.id.database:
               
               return true;
            case R.id.add:
            	 
            	Button bt = (Button)findViewById(R.id.mbuttonAnalyse) ;
            	bt.setClickable(false) ;
            	 
            	click = true ;
                Log.i("ITEM START", "we wait a little bit to be sure the picture is saved");
                SystemClock.sleep(1000) ;                      
                Log.i("ITEM START", "picture saved");
            	
//                PopulateDataBaseImage("/data/data/com.rproyart.okassa/app_pictureDir/picture.jpg"
//                		, String image_name,String infos_Image_Path)
            	
               // Dao.create(null) ;
                
                return true;
            case R.id.remove:
            	                         
               // Dao.delete(null) ;
                          	
                return true;
           case R.id.quitter:
               //Pour fermer l'application il suffit de faire finish()
               finish();
               return true;
           case R.id.delete:
        	   // vider la base de données
        	   Dao.delete(Dao.queryForAll());
        	   return true;
        	   
           case R.id.reset:
        	   // repeupler la base de données
        	   this.PopulateDataBase() ;
        	   return true ;
         }
         return false;
  }
 

    @Override
    protected void onStop() {
     // TODO Auto-generated method stub
     //unregisterReceiver(myReceiver);
     super.onStop();
     Log.i("ACTIVITY STOP", "stop activity");
    }
   
}