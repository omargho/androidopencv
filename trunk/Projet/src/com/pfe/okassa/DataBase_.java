package com.pfe.okassa;

import java.util.List;

import android.os.Bundle;
import android.widget.Toast;


import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
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



public class DataBase_ extends OrmLiteBaseActivity {

	public List<Image> list ;
	
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//DatabaseHelper helper = (DatabaseHelper) OpenHelperManager.getHelper(getBaseContext(), DatabaseHelper.class);
		
		//Image image = new Image() ;
		//helper.addData(image) ;
		//helper.GetData() ;
		Toast.makeText(DataBase_.this, "database activity is running...",Toast.LENGTH_LONG).show();
	}
	
	
	public void management(int choix, DatabaseHelper helper, Image image)
	{
		switch(choix)
		{
		case 0:
			helper.addData(image) ;
		case 1:
			list = helper.GetData();
		case 2: 
			helper.deleteAll() ;
		default:
						
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
