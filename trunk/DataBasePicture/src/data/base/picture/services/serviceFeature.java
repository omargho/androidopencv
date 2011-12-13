package data.base.picture.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

import data.base.picture.*;
import data.base.picture.activities.DataBasePicture;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;



public class serviceFeature extends Service {
	
	
	 	Mat image ;
	 	List<Mat> images =new ArrayList<Mat>();
	 	List<List<KeyPoint>> list_keypoints =new ArrayList<List<KeyPoint>>();
		List<KeyPoint> keypoints =new ArrayList<KeyPoint>();
		FeatureDetector detector ;
		TextView t ;
		Mat outImage ;
		Mat descriptors = new Mat() ;
		DescriptorExtractor surf  ;
		
		
	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	 @Override
	public void onCreate()
	 {
		
		 	super.onCreate();
		 	/*
		 	 * On cr�e un detecteur de points d'inter�ts pour r�cuperer 
		 	 * les points d'interets sur notre image.
		 	 * 
		 	 */
			detector = FeatureDetector.create(FeatureDetector.SIFT) ;
			
			/*
			 * On utilise un extracteur de descripteur pour les points d'interets 
			 * que l'on aura trouv�
			 * 
			 * 
			 */
			surf = DescriptorExtractor.create(DescriptorExtractor.SIFT);
		 	
			Toast msg = Toast.makeText(serviceFeature.this, "My Service is Created", Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
			msg.show();		 
			
			Toast.makeText(getBaseContext(),"chargement de l'image... ", Toast.LENGTH_LONG).show();
			
			/*
			 * On charge l'image qui a �t� prise par la cam�ra OpenCV
			 * 
			 */
			
			image = Highgui.imread("/mnt/sdcard/DCIM/Camera/ardrone.jpg");
			
			if ( image.empty() == true)
			{	
				Toast.makeText(getBaseContext(),"l'image n'a pas �t� charg�e ", Toast.LENGTH_LONG).show();
			}
			else 
			{
				Toast.makeText(getBaseContext(),"l'image a bien �t� charg�e ", Toast.LENGTH_LONG).show();
				Toast.makeText(getBaseContext(),"detection des points d'interets... ", Toast.LENGTH_LONG).show();
				//-- Step 1: Detect the keypoints using SURF Detector in the picture
				if(detector.empty() == true)
				{
					Toast.makeText(getBaseContext(),"le descripteur est null ", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getBaseContext(),"on peut commencer le detection", Toast.LENGTH_LONG).show();
					images.add(image);
					list_keypoints.add(keypoints);
				}
				
			}
	 }
	 
	 
		 
	 @Override
	public void onStart(Intent intent, int startId)
	 {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		//GenericDescriptorMatcher gd = GenericDescriptorMatcher.create(BIND_AUTO_CREATE);
		//gd.add(images, list_keypoints);

		
		/*
		 * On d�tecte les points d'int�rets contenus dans l'image et on
		 * stocke ces dernier dans un vecteur 
		 * 
		 * 
		 */
		detector.detect( image, keypoints);	 
		
			if(keypoints.isEmpty() == true)
			{
				Toast.makeText(getBaseContext(),"les points d'interets n'ont pas �t� detect�s correctement... ", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(getBaseContext(),"les points d'interets ont �t� detect�s... ", Toast.LENGTH_LONG).show();
			}
		
		Toast.makeText(getBaseContext(),"on se lance dans le calcul les descripteurs... ", Toast.LENGTH_LONG).show();
		/*
		 * On calcule les descripteurs des points d'interets que 
		 * l'on a trouv� et les stocke dans une matrice de descripteurs
		 * 
		 */
		
		surf.compute(image, keypoints, descriptors);
		
			if(keypoints.isEmpty() == true)
			{
				Toast.makeText(getBaseContext(),"les descripteurs n'ont pas �t� detect�s correctement... ", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(getBaseContext(),"les descripteurs ont �t� detect�s... ", Toast.LENGTH_LONG).show();
			}
			Toast.makeText(getBaseContext(),"keypoints size is :"+ keypoints.size() + " descriptors size is :"+ descriptors.size()
					, Toast.LENGTH_LONG).show();	
			/*
			 * On ouvre la base de donn�es
			 * 
			 */
			
				
			
			//store the data as a ByteArray
			//db is a SQLiteDatabase object
			ContentValues dataToInsert = new ContentValues();                          
	//		dataToInsert.put("tbl_descriptors.rien",baf.toByteArray());

			DataBasePicture.db.insert("tbl_descriptors", null, dataToInsert);
		//	DataBasePicture.db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy) ;
			
			
			
		
	}


	 @Override
	 public void onDestroy()
	 {
		 super.onDestroy();
	 }
	 
}


