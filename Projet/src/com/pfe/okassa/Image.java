package com.pfe.okassa;

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



@DatabaseTable(tableName="images")

public class Image {
	
	   @DatabaseField(columnName = "id", id = true, generatedId = true)
       int id;
       
	   @DatabaseField(columnName = "image", dataType = DataType.BYTE_ARRAY)
       byte[] image;
	
	   @DatabaseField(columnName = "sift" , dataType = DataType.BYTE_ARRAY)
       byte[] sift;   
       		
	   @DatabaseField(columnName = "surf" ,dataType = DataType.BYTE_ARRAY)
       byte[] surf;
       
	   @DatabaseField(columnName = "histogramme" ,dataType = DataType.BYTE_ARRAY)
       byte[] hist1;
       
       @DatabaseField(columnName = "m" ,dataType = DataType.INTEGER)
       int m;
       
       @DatabaseField(columnName = "n" ,dataType = DataType.INTEGER)
       int n;
       
       @DatabaseField(dataType = DataType.LONG_STRING)
       String infos;
       
       
       
    public Image()
    {
       // all persisted classes must define 
    	//a no-arg constructor with at least package visibility
    
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
	 * Ligne de la matrice
	 * @return
	 */

	public int getM() {
		return m;
	}



	public void setM(int m) {
		this.m = m;
	}

	/**
	 * Colonne de la matrice
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
	
}
