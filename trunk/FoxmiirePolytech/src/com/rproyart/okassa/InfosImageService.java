package com.rproyart.okassa;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;


public class InfosImageService extends OrmLiteBaseService<DatabaseHelper>  {
	
	
	Messenger messenger; 
	Message message;
	final static String MY_ACTION = "MY_ACTION";
	String initData;	
	List<KeyPoint> k = new ArrayList<KeyPoint>() ;
	List<KeyPoint> sift_2 = new ArrayList<KeyPoint>() ;
	public Mat img ;	
	Image image ;
	int isRunning = 0 ;
	
	
	
	
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
	       
   		Mat img = Highgui.imread(imgName) ;
   		
   		if ( img.empty() == false)
   		{	
   		   intent.putExtra("DETECTFEATURES", "image bien chargée... ");
	       sendBroadcast(intent);
   		}
		return img;	
	}
	
	/**
	 * This function is used to convert a file into byte array
	 * to store it into a  database.
	 * 
	 * @author olympe kassa
	 * @param image_url
	 * @return
	 */
	
	
	public byte[] convertImage(File image_url)
	{
		//buffer the download
		InputStream is = null ;
		ByteArrayBuffer baf = null ;
		try {
			is = new FileInputStream (image_url);
			BufferedInputStream bis = new BufferedInputStream(is,128);
			baf = new ByteArrayBuffer(128);
			//get the bytes one by one
			int current = 0;
			try {
				while ((current = bis.read()) != -1) {
				        baf.append((byte) current);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				                        
		return baf.toByteArray();
		
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
						
		Intent intent = new Intent();
		intent.setAction(MY_ACTION);
		intent.putExtra("SHOWPROGRESSBAR", true);
		sendBroadcast(intent);
		FeatureDetector detector = null ;	
		int cpt = 0 ;
		switch(choix)
		{
			case 0 :
				detector = FeatureDetector.create(FeatureDetector.SIFT) ;
				detector.detect(img, keyPoints);	
				Log.i("DETECTFEATURES inside", "SIFT keypoints "+ keyPoints.size());
				
				if(cpt == 0)
				{
					sift_2 = keyPoints ;
					Log.i("DETECTFEATURES inside", "SIFT keypoints global var "+ sift_2.size());
					cpt ++ ;
				}
				
			case 1 :
				detector = FeatureDetector.create(FeatureDetector.SURF) ;
				detector.detect(img, keyPoints);	
				Log.i("DETECTFEATURES inside", "SURF keypoints "+ keyPoints.size());
			default:
				
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
	
	public Mat computeDescriptors(Mat img3,List<KeyPoint> kpt, int choix)
	{
		Mat descriptors = new Mat()  ;
		DescriptorExtractor extract = null ;
		Intent intent = new Intent();
		intent.setAction(MY_ACTION);

		
		switch(choix)
		{
			case 0 :
				
				extract = DescriptorExtractor.create(DescriptorExtractor.SIFT);
				extract.compute(img3, kpt, descriptors);
				
				Log.i("COMPUTEDESCRIPTORS inside"," SIFT descriptors size is :"+ descriptors.size());
				
				
			case 1:
			    extract = DescriptorExtractor.create(DescriptorExtractor.SURF);
				extract.compute(img3, kpt, descriptors);
				
				Log.i("COMPUTEDESCRIPTORS inside"," SURF descriptors size is :"+ descriptors.size());
				
			default:
				
		}		
		 	
		    intent.putExtra("HIDEPROGRESSBAR", true);		
		    sendBroadcast(intent);
		    
		    Log.i("COMPUTEDESCRIPTORS","HIDEPROGRESSBAR") ;
			return descriptors;
			
	}
	 	 
	
 	/* Compute histogramm */ 
 	
 public Mat computeHist(Mat img)
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
	 Log.i("SERVICE HISTOGRAMS", "computing begin...");
	 Imgproc.calcHist(images, channels, new Mat(), hist, histSize, ranges);
	 Log.i("SERVICE HISTOGRAMS", "histograms size is = " + histSize);
	 
 	 return hist ;
 	}
 	
 
 
 public byte[] ConvertMatrix(Mat mat)
 {
	 Mat m = new Mat() ;
	 mat.convertTo(m,CvType.CV_32SC1) ;
	 Log.i("SERVICE", "mat converted size ="+ m.size());
	 
	 List<Integer> is = new ArrayList<Integer>() ;
	 Converters.Mat_to_vector_int(m, is) ;
	 Log.i("SERVICE", "Matrix coveter size ="+is.size());
	 
	 byte[] test = ObjectToByteArray(is);
	 Log.i("SERVICE", " byte coveter size ="+ test.length);
	 
	 
	 return test ;
 }
 
 public Mat ConvertVector( byte[] byte_array)
 {
	 Mat m = new Mat() ;
	 
	 Object test = ByteArrayToObject (byte_array) ;
	 
	 List<Integer> is  = (List<Integer>) test ;
	 // mat.convertTo(m,CvType.CV_32SC1) ;
	 Log.i("SERVICE CONVERT VECTOR", "list to convert size ="+ is.size());
	 Log.i("SERVICE CONVERT VECTOR ", "  element of vector =" + is.toString()) ;
	
	 m = Converters.vector_int_to_Mat(is) ;
	 Log.i("SERVICE CONVERT VECTOR", "Vector coveter size ="+is.size());
	 
	 Log.i("SERVICE CONVERT VECTOR", " Mat coveter size ="+ m.size());
	 
	 return m ;
 }
 
 
 
public class MyThread extends Thread{
	
	private volatile boolean stop = false;
	
	public synchronized void requestStop() {
        stop = true;
     }
	
	@Override
   public void run() {
		  // TODO Auto-generated method stub	
		
			 try{
					
				 File f = new File("/mnt/sdcard/DCIM/Camera/picture.jpg") ;
				 byte[] im = convertImage(f) ;
				  
				 Log.i("SERVICE", "loading image");
				 img = loadImage("/mnt/sdcard/DCIM/Camera/picture.jpg") ;
				 Log.i("SERVICE", "image loaded");
				 
				 Log.i("SERVICE", "type de la matrice ="+ img.type());
				 
				 //Log.i("SERVICE", "type de la matrice ="+ img.convertTo( m, ));
				 
				 
				 
				 Log.i("SERVICE", "computing histogramms");
				 Mat hist = computeHist(img) ;
				 Log.i("SERVICE", "histogramms");
				 				 
				 byte[] test = ConvertMatrix(hist);
				 
				// get the dao
				RuntimeExceptionDao<Image, Integer> simpleDao = getHelper().getSimpleDataDao();					
					
				Log.i("SERVICE", "creating new histograms");
				image = new Image(test, "l'image que l'on utilise") ;
			
				// create some entries in the onCreate
				Log.i("SERVICE", "inserting the new image");
				simpleDao.create(image);

				// trying to retrieve data
				Log.i("IMAGE DATABASE ", "we get the data we inserted into the database");
					
			 	// query for all sift elements in the database
				QueryBuilder<Image, Integer> q = simpleDao.queryBuilder().selectColumns("hist") ;
				// prepare our sql statement
				PreparedQuery<Image> preparedQuery = q.prepare();
				
				List<Image> result = simpleDao.query(preparedQuery);				
						
				
				if (result.isEmpty() == false)
				{
					Log.i("IMAGE RETRIEVED ", "we retrieved the column and his size is =" + result.size() );
					
					for(Image image : result)
					{
						Log.i("IMAGE RETRIEVED ", " sift size =" + image.hist.length) ;
						ConvertVector( image.hist) ;
					}
				}
				
				 this.interrupt() ;
				 stopSelf() ;
				  //appel de la base de données et au kNN
				 }
				 catch(Exception e)
				 {
					// TODO Auto-generated catch block
					    e.printStackTrace();
				 }     
		}	 
	}

}
