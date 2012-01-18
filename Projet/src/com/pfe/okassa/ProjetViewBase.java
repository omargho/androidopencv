package com.pfe.okassa;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.highgui.Highgui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public abstract class ProjetViewBase extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = "Sample::SurfaceView";

    private SurfaceHolder       mHolder;
    private VideoCapture        mCamera;
    public ProgressDialog progressDialog;
    
    String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    String fileName = "myPicture.jpg";

    
    public ProjetViewBase(Context context) {
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

    protected abstract Bitmap processFrame(VideoCapture capture);

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
                Canvas canvas = mHolder.lockCanvas();
                               
                if (canvas != null) {
                    canvas.drawBitmap(bmp, (canvas.getWidth() - bmp.getWidth()) / 2, (canvas.getHeight() - bmp.getHeight()) / 2, null);
                    if(Projet_Main.click == true)
                    {
                    	 
                    	try {
                        	// Not sure if the / is on the path or not
                            File f = new File(baseDir + File.separator + fileName);

                            // Create file if it does not exist
                            boolean success = f.createNewFile();
                            if (success) {
                                // File did not exist and was created
                            	FileOutputStream out = new FileOutputStream(f);
                            	boolean t = bmp.compress(Bitmap.CompressFormat.JPEG, 100, out) ;
                            		if(t=true)
                            		{
                            			// File compression was succesfull
                            		}
                            } else {
                                // File already exists
                            }
                        } catch (IOException e) {
                        }
                    }
                    mHolder.unlockCanvasAndPost(canvas);
                }
                bmp.recycle();
            }
        }

        Log.i(TAG, "Finishing processing thread");
    }    
}