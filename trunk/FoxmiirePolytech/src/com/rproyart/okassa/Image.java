package com.rproyart.okassa;

import org.opencv.core.Mat;

import android.util.Log;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *  Creation de la classe image et de la table
 *  en base de donnee avec le DAO (Data Access Object)
 *  un exemple d'utilisation de SQLite sous Android
 *    
 * @author Olympe Kassa & Romain Proyart
 * 
 * {code.google.com/p/androidopencv}
 * 
 * |--------------------------------------------------------|
 * |______Id____|___image___|_sift______|___surf____|_...___|					
 * |____________|___________|___________|___________|_______|
 * |____________|___________|___________|___________|_______|
 */


@DatabaseTable(tableName = "images")

public class Image {
	
	   @DatabaseField(generatedId = true)
       int id;
       
	   @DatabaseField(index = true)
	   String string;
	   
	   @DatabaseField(dataType = DataType.BYTE_ARRAY)
       byte[] image;
	
	   @DatabaseField(dataType = DataType.BYTE_ARRAY)
       byte[] sift;   
       		
	   @DatabaseField(dataType = DataType.BYTE_ARRAY)
       byte[] surf;
       
	   @DatabaseField(dataType = DataType.BYTE_ARRAY)
       byte[] hist1;
       
       @DatabaseField
       int m;
       
       @DatabaseField
       int n;
       
       @DatabaseField
       String infos;
       
       
    public Image()
    {
    	Log.i("IMAGE", "constructor");
    }
    
    public Image(byte[] bs, byte[] bs2, byte[] bs3, byte[] bs4, int m, int n, String infos)
    {
    	Log.i("IMAGE", "constructor");
    	this.image = bs ;
    	this.sift = bs2 ;
    	this.surf = bs3 ;
    	this.hist1 = bs4 ;
    	this.m = m ;
    	this.n = n ;
    	this.infos = infos ;
    	
    	
    }
    
  
	/**
	 * toString
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id);
		sb.append(", ").append("id=").append(id);
		sb.append(", ").append("m=").append(m);
		sb.append(", ").append("n=").append(n);
		sb.append(", ").append("infos=").append(infos);
		return sb.toString();
	}
}
