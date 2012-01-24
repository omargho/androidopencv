package com.pfe.okassa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/**
 *  Creation de l'activité principale, on a ainsi
 *  la base de donnee instanciée via la classe image
 *  à laquelle on peut inserer des images.
 * 
 *  
 * @author Olympe Kassa & Romain Proyart
 * 
 * {code.google.com/p/androidopencv}
 */



public class DataBase_ extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		DatabaseManager.init(this);    
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 Intent intent = new Intent();
		 intent.putExtra("DETECTFEATURES", DatabaseManager.getInstance().GetData().get(0).toString());
		 sendBroadcast(intent);
	}

	

}
