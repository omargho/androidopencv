package com.rproyart.okassa;

import android.util.Log;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

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
       // all persisted classes must define 
    	//a no-arg constructor with at least package visibility
    	Log.i("IMAGE", "constructor");
    }
    
    public Image(byte[] bs, byte[] bs2, byte[] bs3, byte[] bs4, int m, int n, String infos)
    {
    	this.image = bs ;
    	this.sift = bs2 ;
    	this.surf = bs3 ;
    	this.hist1 = bs4 ;
    	this.m = m ;
    	this.n = n ;
    	this.infos = infos ;
    }
    
    /**
     * Id de l'image
     * @return
     */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Image propre
	 * @return
	 */

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	/**
	 * Descripteur SURF
	 * @return
	 */

	public byte[] getSurf() {
		return surf;
	}



	public void setSurf(byte[] surf) {
		this.surf = surf;
	}

	/**
	 * Descripteur SIFT
	 * @return
	 */

	public byte[] getSift() {
		return sift;
	}



	public void setSift(byte[] sift) {
		this.sift= sift;
	}

	/**
	 * Hstogramme de l'image
	 * @return
	 */

	public byte[] getHist1() {
		return hist1;
	}



	public void setHist1(byte[] hist1) {
		this.hist1 = hist1;
	}

	/**
	 * Ligne de la byte[]rice
	 * @return
	 */

	public int getM() {
		return m;
	}



	public void setM(int m) {
		this.m = m;
	}

	/**
	 * Colonne de la byte[]rice
	 * @return
	 */

	public int getN() {
		return n;
	}



	public void setN(int n) {
		this.n = n;
	}


	/**
	 * Informations sur l'image
	 * @return
	 */
	public String getInfos() {
		return infos;
	}



	public void setInfos(String infos) {
		this.infos = infos;
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
