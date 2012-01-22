package com.pfe.okassa;

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



public class DataBase_ extends OrmLiteBaseService {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionSource getConnectionSource() {
		// TODO Auto-generated method stub
		return super.getConnectionSource();
	}

	@Override
	public OrmLiteSqliteOpenHelper getHelper() {
		// TODO Auto-generated method stub
		return super.getHelper();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	

}
