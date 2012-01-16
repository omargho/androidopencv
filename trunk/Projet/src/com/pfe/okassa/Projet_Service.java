package com.pfe.okassa;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;


public class Projet_Service extends Service {
	
	Messenger messenger; 
	Message message;
	final static String MY_ACTION = "MY_ACTION";
	String initData;	
	Semaphore sem = new Semaphore(1);
	
	
	List<KeyPoint> k = new ArrayList<KeyPoint>() ;
	Mat img ;	
	
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
	
//=================================== ASYNC TASK ============================================================
  	
  	 class LongOperation extends AsyncTask<String, Void, Boolean> {
  		
  		Intent intent = new Intent();
	   // intent.setAction(MY_ACTION);
  		List<KeyPoint> k ;  
  		 
  	  @Override
  	  protected Boolean doInBackground(String... params) {
  	    // perform long running operation operation
  		k =  detectFeatures(Projet_Service.this.img) ;
  	    return true;
  	  }
  	 
  	  /* (non-Javadoc)
  	   * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
  	   */
  	  @Override
  	  protected void onPostExecute(Boolean result) {
  	    // execution of result of Long time consuming operation
  		intent.putExtra("DETECTFEATURES", "onPostExecute... ");
	 	sendBroadcast(intent);
	 	
	   	  }
  	 
  	  /* (non-Javadoc)
  	   * @see android.os.AsyncTask#onPreExecute()
  	   */
  	  @Override
  	  protected void onPreExecute() {
  	  // Things to be done before execution of long running operation. For example showing ProgessDialog
  		intent.putExtra("DETECTFEATURES", "onPreExecute... ");
	 	sendBroadcast(intent);
	 	
  	  }
  	 
  	  /* (non-Javadoc)
  	   * @see android.os.AsyncTask#onProgressUpdate(Progress[])
  	   */
  	  @Override
  	  protected void onProgressUpdate(Void... values) {
  	      // Things to be done while execution of long running operation is in progress. For example updating ProgessDialog
  	   }
  	}
  	
  	//===========================================================================================================

	
	
	public class MyThread extends Thread{
		 
		 @Override
		 public void run() {
		  // TODO Auto-generated method stub
			 
		 try{
				 				 
				 
			  new LongOperation().execute() ;
				 
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
