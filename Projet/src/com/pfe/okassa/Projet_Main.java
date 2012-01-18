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
	
	private class Projet_Service extends Service {
		
		Messenger messenger; 
		Message message;
		final static String MY_ACTION = "MY_ACTION";
		String initData;	
		
		
		
		List<KeyPoint> k = new ArrayList<KeyPoint>() ;
			
		
		@Override
		public void onCreate() {
			// TODO Auto-generated method stub
			super.onCreate();
			 
		}
		
		
		@Override
		public void onStart(Intent intent, int startId) {
			// TODO Auto-generated method stub
			super.onStart(intent, startId);

			
			initData = intent.getStringExtra("INIT_DATA");
			 
			 MyThread myThread = new MyThread();
			 myThread.start();
			
		}
	    
		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		} 

		public void loadImage(Mat img)
	   	{
	   		Toast.makeText(getBaseContext(),"Chargement de l'image Service... ", Toast.LENGTH_LONG).show();
	   				   		
	   		
	   		if ( img.empty() == true)
	   		{	
	   			Toast.makeText(getBaseContext(),"Chargement echec Service... ", Toast.LENGTH_LONG).show();
	   		}
	   		else 
	   		{
	   			Toast.makeText(getBaseContext(),"Chargement de l'image succes Service... ", Toast.LENGTH_LONG).show();  
	   			
	   		}	
	   	}

		
		
		/**
		 * Cette methode permet de detecter les points
		 * d'interets dans notre image et ainsi d'obtenir
		 * une base d'identification.
		 * 
		 * @param img
		 * @return
		 * 
		 * @author Olympe Kassa
		 */
		
		public List<KeyPoint> detectFeatures(Mat img)
			{								
				
				List<KeyPoint> keyPoints = new ArrayList<KeyPoint>() ;
				FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIFT) ;
							
				   Intent intent = new Intent();
			       intent.setAction(MY_ACTION);
			      
			       intent.putExtra("DETECTFEATURES", "detection des points d'interets... ");
			       sendBroadcast(intent);
				
				if(detector.empty() == true)
				{
				       intent.putExtra("DETECTFEATURES", "le descripteur est null... ");
				       sendBroadcast(intent);
				}
				else
				{					
				       intent.putExtra("DETECTFEATURES", "on peut commencer le detection");
				       sendBroadcast(intent);
					  
				       detector.detect(img, keyPoints);	
				      
					
					if(keyPoints.isEmpty() == true)
					{	
						 intent.putExtra("DETECTFEATURES", "les points d'interets n'ont pas été detectés correctement...");				 
					     sendBroadcast(intent);
						
					}
					else
					{	
						 intent.putExtra("DETECTFEATURES", "les points d'interets ont été detectés... et on a : "+ keyPoints.size());
					     sendBroadcast(intent);
					}
				}
				
			 return keyPoints ;		
		}
		
		
		/**
		 * Cette methode permet de calculer les descripteurs
		 * de notre image à partir des points d'interets 
		 * calculés précedements.
		 * 
		 * @param img3
		 * @param kpt
		 * @param descriptors
		 * 
		 * 
		 * @author Olympe Kassa
		 */
		
		public Mat computeDescriptors(Mat img3,List<KeyPoint> kpt)
		{
			Mat descriptors = new Mat()  ;
			DescriptorExtractor extract = DescriptorExtractor.create(DescriptorExtractor.SIFT);
			
			
				Intent intent = new Intent();
				intent.setAction(MY_ACTION);
			 	intent.putExtra("DETECTFEATURES", "on se lance dans le calcul les descripteurs... "+ kpt.size());
			 	sendBroadcast(intent);
			 	
			    extract.compute(img3, kpt, descriptors);
			   
			 				
				if(descriptors.empty() == true)
				{				
					intent.putExtra("DETECTFEATURES", "les descripteurs n'ont pas été detectés correctement... ");
				 	sendBroadcast(intent);
				}
				else
				{				
					intent.putExtra("DETECTFEATURES", "les descripteurs ont  été detectés ... ");
				 	sendBroadcast(intent);
				}
				
				intent.putExtra("DETECTFEATURES","keypoints " + "size is :"+ kpt.size() + " descriptors size is :"+ descriptors.size());
			 	sendBroadcast(intent);
				
				return descriptors;
				
		}
		
	  	 
	  	 
	 	/**
	 	 * Cette methode nous permet de convertir un objet en 
	 	 * en byte dans notre cas il s'agit de récuperer l'
	 	 * élément m(i,j) de notre matrice.
	 	 * 
	 	 * @param obj
	 	 * @return
	 	 * @throws IOException
	 	 * 
	 	 * @author Olympe Kassa
	 	 * 
	 	 * 
	 	 * 
	 	 */
	 	
	 	public byte[] toByteArray (Object obj)
	 	{
	 	  byte[] bytes = null;
	 	  ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 	  try {
	 	    ObjectOutputStream oos = new ObjectOutputStream(bos); 
	 	    oos.writeObject(obj);
	 	    oos.flush(); 
	 	    oos.close(); 
	 	    bos.close();
	 	    bytes = bos.toByteArray ();
	 	  }
	 	  catch (IOException ex) {
	 	    //TODO: Handle the exception
	 	  }
	 	  return bytes;
	 	}
	  	 
	 	/**
	 	 *	Cette methode nous permet de convertir une matrice 
	 	 * tableau d'objet de bytes dans notre cas il 
	 	 * s'agit de récuperer notre matrice et de mettre ses 
	 	 * éléments de type byte dans un tableau.
	 	 * 
	 	 *  @author Olympe Kassa
	 	 * 
	 	 * @param mat
	 	 * @return
	 	 */
	 	
	 	public List<byte[]>  toByteArrayGlob (Mat mat)
	 	{
	 	  List<byte[]> bytes = new ArrayList<byte[]>();
	 	  
	 	  int m=mat.height() ;
	 	  int n = mat.width() ;
	 	  int i,j;
	 	  
	 	  for(i=0;i<m;i++)
	 	  {
	 		  for(j=0;j<n;j++)
	 		  {
	 			 bytes.add(toByteArray(mat.get(i, j))) ;  
	 		  }
	 	  }	 
	 			return bytes;
	 	}
	 	
	 	/**
	 	 * Cette methode nous permet de convertir une liste de  bytearray
	 	 * en Matrice de pouvoir la reutiliser avec le kNN par exemple
	 	 * 
	 	 * @param bytes
	 	 * @return
	 	 * 
	 	 * @author Olympe Kassa
	 	 * 
	 	 */
	 	    
	 	public Mat toMatrice(List<byte[]> bytes,int n) throws IOException, ClassNotFoundException
	 	{
	 	 
	 	  int j=0 ;
	 	  int i=0;
	 	  int k=0;
	 	  Mat mat = new Mat() ;
	 	  Iterator<byte[]> iter = bytes.iterator();
		  	while(iter.hasNext())
		  	{
		  		while(j<n)
		  		{
			  		mat.put(i, j, bytes.get(k)) ;
			  		k++;
			  		j++;
		  		}
		  		  i++ ;

		  	}
	 	  return mat;
	 	} 
	  	 
		
		public class MyThread extends Thread{
			 
			 @Override
			 public void run() {
			  // TODO Auto-generated method stub
				 
			 try{
				 //loadImage(img) ;
				 k =  detectFeatures(Projet_Main.img) ;
					 // appel de la base de données
				 }
				 catch(Exception e)
				 {
					// TODO Auto-generated catch block
					    e.printStackTrace();
				 }
				 stopSelf();
			 }
			 
			}
	}

	
	//============================================ FIN SERVICE =================================================================
	
	
	
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
	   			img = loadImage(path_) ;
	   			
	           	 Intent i= new Intent(Projet_Main.this, Projet_Service.class);
	           	
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
    	  
    	  Toast.makeText(Projet_Main.this,
    	    "Triggered by the Computing Service : !\n"
    	    + orgData,
    	    Toast.LENGTH_LONG).show();
    	   }
    	}
              
}