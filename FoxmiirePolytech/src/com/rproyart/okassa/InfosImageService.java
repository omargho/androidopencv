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
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class InfosImageService extends OrmLiteBaseService<DatabaseHelper>  {
	
	LocalBroadcastManager mLocalBroadcastManager;
	Messenger messenger; 
	Message message;
	final static String MY_ACTION = "MY_ACTION";
	String initData;	
	String infos_image = "Pas d'infos sur cette image" ;
	List<KeyPoint> k = new ArrayList<KeyPoint>() ;
	List<KeyPoint> sift_2 = new ArrayList<KeyPoint>() ;
	public Mat img ;	
	Image image ;
	public boolean flag ;
	
	// on a lancé les calculs
	public static boolean isRunning = true ;
	static final String ACTION_STARTED = "STARTED";
	static final String ACTION_UPDATE = "UPDATE";
	static final String ACTION_STOPPED = "STOPPED";

	
	
	/**
	 * Ce handler est utilisé pour envoyer 
	 * le nom de l'image trouvée par la 
	 * comparaison de l'hostogramme, des descripteurs
	 * ...
	 * 
	 * @author olympe kassa
	 * 
	 */
	
	static final int MSG_UPDATE = 1;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

              super.handleMessage(msg);
            
        }
    };
	
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
		 mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		 
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
				// nothing
				
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
				// nothing
				
		}		
		 	
		    intent.putExtra("HIDEPROGRESSBAR", true);		
		    sendBroadcast(intent);
		    
		    Log.i("COMPUTEDESCRIPTORS","HIDEPROGRESSBAR") ;
			return descriptors;
			
	}
	 	 
	
 	/**
 	 * 
 	 *  Compute histogramm of an 
 	 *  image to compare with others 
 	 *  
 	 *  @author olympe kassa
 	 *  */ 
 	
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
	 Mat mask = new Mat() ;
	 
	 Log.i("SERVICE HISTOGRAMS", "computing begin...");
	 Imgproc.calcHist(images, channels, mask, hist, histSize, ranges);
	 Log.i("SERVICE HISTOGRAMS", "histograms size is = " + histSize);
	 
 	 return hist ;
 	}
 	
 
 /**
  * Cette fonction permet de convertir une matrice
  * en Arraylist de byte
  * 
  * @param mat
  * @return
  * 
  * @author olympe kassa
  */
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
 
 /**
  * Cette fonction permet de convertir une
  * arraylist de byte en Matrice
  * 
  * @author olympe kassa
  * 
  */
 
 
 public Mat ConvertVector( byte[] byte_array)
 {
	 Mat m = new Mat() ;
	 
	 Object test = ByteArrayToObject (byte_array) ;
	 
	 List<Integer> is  = (List<Integer>) test ;
	// Log.i("SERVICE CONVERT VECTOR", "list to convert size ="+ is.size());
	// Log.i("SERVICE CONVERT VECTOR ", "  element of vector =" + is.toString()) ;
	
	 m = Converters.vector_int_to_Mat(is) ;
	 
	 Mat mat = new Mat() ;
	 m.convertTo(mat, CvType.CV_32F) ;
	// Log.i("SERVICE CONVERT VECTOR", "Vector coveter size ="+is.size());
	 
	 //Log.i("SERVICE CONVERT VECTOR", " Mat coveter size ="+ m.size());
	 
	 return mat ;
 }
 
 
 
@Override
public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	// Tell any local interested parties about the stop.
    mLocalBroadcastManager.sendBroadcast(new Intent(ACTION_STOPPED));

    // Stop doing updates.
    mHandler.removeMessages(MSG_UPDATE);
}



public class MyThread extends Thread{
		
	@Override
   public void run() {
		  // TODO Auto-generated method stub	
	
		//while(flag == true)
		{
			 try{
					
				 Image im_query = null ;
					
				// File f = new File("/mnt/sdcard/DCIM/Camera/picture.jpg") ;
				 File f = new File("/data/data/com.rproyart.okassa/app_pictureDir/picture.jpg") ;
				 byte[] im = convertImage(f) ;
				 
				 if(im.length == 0)
				 {
					 Log.i("SERVICE IMAGE CONVERSION", "WTF it's not converted !!!"); 
				 }
				 else
				 {
					 Log.i("SERVICE IMAGE CONVERSION", "OMG it's converted... it's great !!!"); 
				 }
				  
				 Log.i("SERVICE", "loading image");
				 
				 // on récupère l'image prise en photo
				// img = loadImage("/data/data/com.rproyart.okassa/app_pictureDir/picture.jpg") ;
				
				 // on teste une image temporaire
				  img = loadImage("/data/data/com.rproyart.okassa/app_asset_to_local/4cantons.jpg") ;
				 
				 
				 //img = loadImage("/mnt/sdcard/DCIM/Camera/picture.jpg") ;
				 Log.i("SERVICE", "image loaded");
				 
				 Log.i("SERVICE", "type de la matrice ="+ img.type());
				 				
                 Log.i("SERVICE", "computing histogramms");
                 Mat hist = computeHist(img) ;
                 Log.i("SERVICE", "type de la matrice ="+ hist.type());
                 Log.i("SERVICE", "histogramms");
                                                 
                 byte[] test = ConvertMatrix(hist);
//                 byte[] test_sift = ConvertMatrix(sift);
//                 byte[] test_surf = ConvertMatrix(surf);
//                 
                 
                // get the dao
                RuntimeExceptionDao<Image, Integer> simpleDao = getHelper().getSimpleDataDao();                                 
                        
                // trying to retrieve data
                Log.i("IMAGE DATABASE ", "we get the data we inserted into the database");
                                                                
                List<Image> result = simpleDao.queryForAll() ;
                
                
                if (result.isEmpty() == false)
                {
                        Log.i("IMAGE RETRIEVED ", "we retrieved the " +
                                        "column and his size is =" + result.size() );
                                                 
                        
                        for(Image image : result)
                        {
                                Log.i("IMAGE RETRIEVED ", " sift size =" + image.hist.length) ;
                                Mat m_comp = ConvertVector(image.hist) ;
                                Log.i("IMAGE RETRIEVED ", " native object =" + m_comp.nativeObj);
                                
                                // compare their histograms using the Bhattacharyya coefficient
                                
                                // 0.2 = good similarity
                                // d=0.0 = perfect similarity
                                double resu = Imgproc.compareHist(ConvertVector(test),
                                                m_comp , Imgproc.CV_COMP_BHATTACHARYYA);         
                                
                                if(resu < 0.1)
                                {
                                	im_query = image ;
                                }
                                
                                Log.i("IMAGE RETRIEVED ", " distance between picture " +
                                		"histogram and "+ image.nom +" histogram equals" + resu);
                        }
                        
                        // on affiche l'image trouvée et on envoie son nom
                        //  à l'activité de reponse
                        
                        
                    
                        Log.i("IMAGE RETRIEVED ", " using histogramms the " +
                                        "nearest image is =" + im_query.toString());
                        
                        
                      // sending data of the query image
       	   			  Intent iBis = new Intent(ACTION_UPDATE);
                      iBis.putExtra("image_name", im_query.nom);
                      iBis.putExtra("image_infos", im_query.infos);
                      Log.i("IMAGE RETRIEVED ", " image_name = " + im_query.nom +
                                "image location is =" + im_query.infos);
                      mLocalBroadcastManager.sendBroadcast(iBis);
                       
                }
                
               // flag = false ;
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
}
