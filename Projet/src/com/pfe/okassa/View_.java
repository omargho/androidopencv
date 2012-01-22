package com.pfe.okassa;


import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.SurfaceHolder;

class View_ extends View_Base {
    public static Mat mRgba;
    public static Bitmap bmp;
    
    Imgproc imgproc;

    public View_(Context context) {
        super(context);
    }

    @Override
    public void surfaceChanged(SurfaceHolder _holder, int format, int width, int height) {
        super.surfaceChanged(_holder, format, width, height);

        synchronized (this) {
            // initialize Mats before usage
            mRgba = new Mat();
        }
    }

    @Override
    protected Bitmap processFrame(VideoCapture capture) {
        
        capture.retrieve(mRgba, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGBA);
        
        bmp = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
                
        if (Utils.matToBitmap(mRgba, bmp))
            return bmp;
        
        bmp.recycle();
        return null;
      
    	
    }

    public ContentResolver getContentResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public void run() {
        super.run();

        synchronized (this) {
            // Explicitly deallocate Mat
            if (mRgba != null)
                mRgba.release();
            
            mRgba = null;
        }
    }
}
