package com.rproyart.okassa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;



import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class InfosImageViewBase extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	 private static final String TAG = "Sample::SurfaceView";

	    private SurfaceHolder       mHolder;
	    private VideoCapture        mCamera;
	    public ProgressDialog progressDialog;
	    public static Bitmap bmp_ ;
	    int w,h ;
	  
	    
	    public InfosImageViewBase(Context context) {
	        super(context);
	      
	        mHolder = getHolder();
	        mHolder.addCallback(this);
	        
	        Log.i(TAG, "Instantiated new " + this.getClass());
	    }

	    @Override
		public void surfaceChanged(SurfaceHolder _holder, int format, int width, int height) {
	        Log.i(TAG, "surfaceCreated");
	        synchronized (this) {
	            if (mCamera != null && mCamera.isOpened()) {
	                Log.i(TAG, "before mCamera.getSupportedPreviewSizes()");
	                List<Size> sizes = mCamera.getSupportedPreviewSizes();
	                Log.i(TAG, "after mCamera.getSupportedPreviewSizes()");
	                int mFrameWidth = width;
	                int mFrameHeight = height;

	                // selecting optimal camera preview size
	                {
	                    double minDiff = Double.MAX_VALUE;
	                    for (Size size : sizes) {
	                        if (Math.abs(size.height - height) < minDiff) {
	                            mFrameWidth = (int) size.width;
	                            mFrameHeight = (int) size.height;
	                            minDiff = Math.abs(size.height - height);
	                        }
	                    }
	                }

	                mCamera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, mFrameWidth);
	                mCamera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, mFrameHeight);
	            }
	        }
	    }

	    @Override
		public void surfaceCreated(SurfaceHolder holder) {
	        Log.i(TAG, "surfaceCreated");
	        mCamera = new VideoCapture(Highgui.CV_CAP_ANDROID);
	        //mCamera.set(Highgui.CV_CVTIMG_FLIP, 1);
	        if (mCamera.isOpened()) {
	        	
	            (new Thread(this)).start();
	        } else {
	            mCamera.release();
	            mCamera = null;
	            Log.e(TAG, "Failed to open native camera");
	        }
	    }

	    @Override
		public void surfaceDestroyed(SurfaceHolder holder) {
	        Log.i(TAG, "surfaceDestroyed");
	        if (mCamera != null) {
	            synchronized (this) {
	                mCamera.release();
	                mCamera = null;
	            }
	        }
	    }

	    abstract Bitmap processFrame(VideoCapture capture);

	    
	    
	    @Override
		public void run() {
	        Log.i(TAG, "Starting processing thread");
	        while (true) {
	            Bitmap bmp = null;

	            synchronized (this) {
	                if (mCamera == null)
	                    break;

	                if (!mCamera.grab()) {
	                    Log.e(TAG, "mCamera.grab() failed");
	                    break;
	                }

	                bmp = processFrame(mCamera);
	            }

	            if (bmp != null) {
	            	
	            	/**
	            	 * On capture la photo que l'on enregistre dans la 
	            	 * carte mémoire il a fallut dans un premier temps 
	            	 * rajouter le droit de lire et d'ecrire dans la 
	            	 * SD au manifest.xml
	            	 */
	            	
	                if(InfosImage.click == true)
	                {
	                	Canvas canvas = new Canvas(bmp);
	                	canvas.drawBitmap(bmp,w,h, null) ;
	                	String _path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
	                    File file = new File(_path + "/"+ "picture.jpg");
	                    
	                    FileOutputStream fos;
	                    
	                    /*
	                     * on utilise le storage interne pour preserver 
	                     * les données 
	                     * 
	                     */
	                    
	                    //Creating an internal dir;
	                    File mydir = getContext().getDir("pictureDir", Context.MODE_PRIVATE); 
	                    //Getting a file within the dir.
	                    File fileWithinMyDir = new File(mydir, "picture.jpg"); 
	                    FileOutputStream out ;
	                    
	                    
	                    try {
	                        fos = new FileOutputStream(file);
	                        out= new FileOutputStream(fileWithinMyDir); //Use the stream as usual
	                        											//to write into the file
	                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
	                        out.close() ;
	                        fos.close();
	                        
	                        InfosImage.click = false ;
	                    } catch (FileNotFoundException e) {
	                        Log.e("Panel", "FileNotFoundException", e);
	                    } catch (IOException e) {
	                        Log.e("Panel", "IOEception", e);
	                    }
	                    	
	                }
	            	
	                Canvas canvas = mHolder.lockCanvas();
	                                
	                if (canvas != null) {
	                	w = (canvas.getWidth() - bmp.getWidth()) / 2 ;
	                	h =  (canvas.getHeight() - bmp.getHeight()) / 2 ;
	                	
	                    canvas.drawBitmap(bmp, (canvas.getWidth() - bmp.getWidth()) / 2, (canvas.getHeight() - bmp.getHeight()) / 2, null);

	                    mHolder.unlockCanvasAndPost(canvas);
	                }
	                bmp.recycle();
	            }
	        }

	        Log.i(TAG, "Finishing processing thread");
	    }    
}
