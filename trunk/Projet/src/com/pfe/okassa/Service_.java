package com.pfe.okassa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;


public class Service_ extends Service {
	
	Messenger messenger; 
	Message message;
	final static String MY_ACTION = "MY_ACTION";
	String initData;	
	List<KeyPoint> k = new ArrayList<KeyPoint>() ;
	public Mat img ;	
	Image image ;
	boolean isRunning = false ;
	
	/**
	 * Convert a serializable object to byteArray
	 * for the database 
	 * @param obj
	 * @return
	 */
	
	public byte[] ObjectToByteArray (Object obj)
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
	 * Convert a byteArray to an Object
	 *     
	 * @param bytes
	 * @return
	 */
	
	public Object ByteArrayToObject (byte[] bytes)
	{
	  Object obj = null;
	  try {
	    ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
	    ObjectInputStream ois = new ObjectInputStream (bis);
	    obj = ois.readObject();
	  }
	  catch (IOException ex) {
	    //TODO: Handle the exception
	  }
	  catch (ClassNotFoundException ex) {
	    //TODO: Handle the exception
	  }
	  return obj;
	}
	
	
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

	public Mat loadImage(String imgName)
   	{
   		
   		   Intent intent = new Intent();
	       intent.setAction(MY_ACTION);
	      
	       //intent.putExtra("DETECTFEATURES", "Chargement de l'image Service... ");
	       //sendBroadcast(intent);
   		
   		Mat img = Highgui.imread(imgName) ;
   		
   		if ( img.empty() == true)
   		{	
   		   //intent.putExtra("DETECTFEATURES", "Chargement echec service...");
	       //sendBroadcast(intent);
   			
   		}
   		else 
   		{
   		   intent.putExtra("DETECTFEATURES", "Chargement de l'image Service OK... ");
	       sendBroadcast(intent);
   		}
		return img;	
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
	
	public List<KeyPoint> detectFeatures(Mat img, int choix)
		{								
			
			List<KeyPoint> keyPoints = new ArrayList<KeyPoint>() ;
			FeatureDetector detector = null ;
			
			switch(choix)
			{
			case 0 :
					detector = FeatureDetector.create(FeatureDetector.SIFT) ;
			case 1 :
					detector = FeatureDetector.create(FeatureDetector.SURF) ;
			}
					
			   Intent intent = new Intent();
		       intent.setAction(MY_ACTION);
		      
		       //intent.putExtra("DETECTFEATURES", "detection des points d'interets... ");
		       //sendBroadcast(intent);
			
			if(detector.empty() == true )
			{
			      // intent.putExtra("DETECTFEATURES", "le descripteur est null... ");
			      // sendBroadcast(intent);
			}
			else
			{					
			     //  intent.putExtra("DETECTFEATURES", "on peut commencer le detection");
			     //  sendBroadcast(intent);
				   
			       intent.putExtra("SHOWPROGRESSBAR", true);
			       sendBroadcast(intent);
			       detector.detect(img, keyPoints);	
			      
			      
				
				if(keyPoints.isEmpty() == true)
				{	
				//	 intent.putExtra("DETECTFEATURES", "les points d'interets n'ont pas �t� detect�s correctement...");				 
				 //    sendBroadcast(intent);
					
				}
				else
				{	
					 intent.putExtra("DETECTFEATURES", "les points d'interets ont �t� detect�s... et on a : "+ keyPoints.size());
				     sendBroadcast(intent);
				}
			}
			
		 return keyPoints ;		
	}
	
	
	/**
	 * Cette methode permet de calculer les descripteurs
	 * de notre image � partir des points d'interets 
	 * calcul�s pr�cedements.
	 * 
	 * @param img3
	 * @param kpt
	 * @param descriptors
	 * 
	 * 
	 * @author Olympe Kassa
	 */
	
	public Mat computeDescriptors(Mat img3,List<KeyPoint> kpt, int choix)
	{
		Mat descriptors = new Mat()  ;
		DescriptorExtractor extract = null ; 
		
			switch(choix)
			{
			case 0 :
				extract = DescriptorExtractor.create(DescriptorExtractor.SIFT);
			case 1:
				extract = DescriptorExtractor.create(DescriptorExtractor.SURF);
			}
		
			Intent intent = new Intent();
			intent.setAction(MY_ACTION);
		 	//intent.putExtra("DETECTFEATURES", "on se lance dans le calcul les descripteurs... "+ kpt.size());
		 	//sendBroadcast(intent);
		 	
		    extract.compute(img3, kpt, descriptors);
		   
		    intent.putExtra("HIDEPROGRESSBAR", true);
		    sendBroadcast(intent);
		 				
			if(descriptors.empty() == true)
			{				
				//intent.putExtra("DETECTFEATURES", "les descripteurs n'ont pas �t� detect�s correctement... ");
			 	//sendBroadcast(intent);
			}
			else
			{				
				//intent.putExtra("DETECTFEATURES", "les descripteurs ont  �t� detect�s ... ");
			 	//sendBroadcast(intent);
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
 	 * s'agit de r�cuperer notre matrice et de mettre ses 
 	 * �l�ments de type byte dans un tableau.
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
 	  
 	  	Intent intent = new Intent();
		intent.setAction(MY_ACTION);
	 	intent.putExtra("DETECTFEATURES", "toByteArray: "+ bytes.size());
	 	sendBroadcast(intent);
	 	
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
  	 
	
 	/* Compute histogramm */ 
 	
 	public Mat computeHist(Mat img)
 	{
 		Mat hist = null;
		// we compute the histogram from the 0-th and 1-st channels
		List<Integer> channels = new ArrayList<Integer>();
		channels.add(0);
		channels.add(1);
		
		
		List<Integer> histSize = new ArrayList<Integer>();
		 // and the saturation to 32 levels
	    int hbins = 30, sbins = 32;
	    histSize.add(hbins);
	    histSize.add(sbins);
	    
		// ranges of channels
		List<Float> ranges = new ArrayList<Float>();
		ranges.add((float)0);
		ranges.add((float)256);
		
		// Mat to compute histogramms
		List<Mat> mat = new ArrayList<Mat>();
		mat.add(img) ;
		
		Imgproc.calcHist(mat, channels, null, hist, histSize, ranges) ;
		
 		return hist ;
 	}
 	
 	
	public class MyThread extends Thread{
		
		private volatile Thread runner;

		public synchronized void startThread(){
		  if(runner == null){
		    runner = new Thread(this);
		    runner.start();
		  }
		}

		public synchronized void stopThread(){
		  if(runner != null){
		    Thread moribund = runner;
		    runner = null;
		    moribund.interrupt();
		  }
		}
		
		
	@Override
   public void run() {
		  // TODO Auto-generated method stub
		while(Thread.currentThread() == runner) 
		{
			 try{
				 
				 // working stuff of the app
				 isRunning = true ;
				 img = loadImage("/mnt/sdcard/DCIM/Camera/picture.jpg") ;
				 List<KeyPoint> k_sift = detectFeatures(img,0);
				 List<KeyPoint> k_surf = detectFeatures(img,1);
				 Mat sift = computeDescriptors(img,k_sift,0) ;
				 Mat surf = computeDescriptors(img,k_surf,1) ;
				 Mat hist = computeHist(img) ;
				
				 // on essaie d'inserer les images dans la BDD
				 image = new Image() ;
				 image.setId(1);
				 image.setImage(ObjectToByteArray(img));
				 image.setM(img.height());
				 image.setN(img.width()) ;
				 image.setSift(ObjectToByteArray(sift)) ;
				 image.setSurf(ObjectToByteArray(surf)) ;
				 image.setHist1(ObjectToByteArray(hist));
				 
				 if(isRunning ==  true)
				 {
					 isRunning = false ;   
					 stopThread();
				        return; //optional in this case since the loop will exit anyways
				      }
				 
				 // appel de la base de donn�es et au kNN
				 }
				 catch(Exception e)
				 {
					// TODO Auto-generated catch block
					    e.printStackTrace();
				 }	
			}
		 }	 
	}
}
