package com.pfe.database;


import android.provider.MediaStore.Images;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class Projet_DatabaseActivity extends OrmLiteBaseActivity<Projet_DataBaseHelper> 
{
	
	Projet_Image image = new Projet_Image();
		
	Projet_DataBaseHelper helper = new Projet_DataBaseHelper(getApplicationContext());
	int i = helper.addData(image) ;
			
}
