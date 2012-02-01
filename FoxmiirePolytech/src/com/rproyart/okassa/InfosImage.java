package com.rproyart.okassa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.opencv.core.Mat;
import com.rproyart.okassa.InfosImageService;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;



/**
 *  OrmLiteBaseActivity<DatabaseHelper> est une sous 
 *  classe de Activity mais qui nous permet de recuperer
 *  une instance du DAO. 
 * 
 * @author olympekassa
 *
 */
public class InfosImage extends OrmLiteBaseActivity<DatabaseHelper> {
	
	String MAIN  ;
	LayoutInflater controlInflater = null;
	Message message ;
	MyReceiver myReceiver;
	public static Mat img;
	public ProgressDialog mProgressDialog;
	public static boolean click ;
	public static boolean store ;
	InfosImageView view ;
	
	
    private MenuItem            mItemAddPicture;
    private MenuItem            TakeAPicture;
	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
	  	  	
  	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        Log.i("INFOS ACTIVITY CREATE", "super oncreate");
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.i("INFOS ACTIVITY CREATE", "getting all surfaceholder");
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Log.i("INFOS ACTIVITY CREATE", "frame orienation");
        //setContentView(R.layout.acceuil);
        
        
        click = false ;
        store = false ;
        /*Original Contentview*/
        Log.i("INFOS ACTIVITY CREATE", "we had the contentView");
        view = new InfosImageView(this) ;
        setContentView(view);
        getWindow().setFormat(PixelFormat.UNKNOWN);
       
        
        Log.i("ACTIVITY CREATE", "reglage de l'image");
        controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.control, null);
        LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
        this.addContentView(viewControl, layoutParamsControl);
        Log.i("ACTIVITY CREATE", "reglage OK"); 
        
        /**
         * Start activity of database
         */
		 
		 Log.i("ACTIVITY CREATE", "BROADCAST TYPE DATABASE");
		 
		 RuntimeExceptionDao<Image, Integer> simpleDao = getHelper().getSimpleDataDao();
		       
    }
      
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("MenuOptions", "onCreateOptionsMenu");
        mItemAddPicture = menu.add("Ajouter image");
        mItemAddPicture.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				store = true ;
				
				return true;
			}
		});
        TakeAPicture = menu.add("Prendre image") ;
        return true;
    }
    
    /**
	 * Do our sample database stuff.
	 * grab image from surface holder
	 * and store it into the database
	 */
    
    /**
	 * Convert a serializable object to byteArray
	 * for the database 
	 * @param obj
	 * @return
	 */
	
    public static byte[] ObjectToByteArray(Object obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (bos != null) {
                bos.close();
            }
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
	
   /**
    * cette methode permet de gerer les
    * boutons options
    * 
    */
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("Menu options", "Menu Item selected " + item);
        if (item == mItemAddPicture)
        {
        	store = true ;
        }
        
        if (item == TakeAPicture)
        {
        	
        }
        
		return false;
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
	    	      
	    ((Button)findViewById(R.id.mbuttonAnalyse)).setOnClickListener(new View.OnClickListener(){
	      
	   		@Override
	        public void onClick(View v) {
	   			 
	   			 Log.i("ACTIVITY START", "button clicked");  			
	   			 click = true ;
	   			 Log.i("ACTIVITY START", "we wait a little bit to be sure the picture is saved");
	   			 SystemClock.sleep(1000) ;   			
	   			 Log.i("ACTIVITY START", "picture saved");
	   			 Intent i= new Intent(InfosImage.this, InfosImageService.class);
	   			 Log.i("ACTIVITY START", "starting service");
	   			 Log.i("ACTIVITY START", "launching service");
	   			 startService(i) ;      	 
	               }
	          });
	     
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
