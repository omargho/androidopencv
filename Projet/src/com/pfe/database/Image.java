package com.pfe.database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *  Création de la classe image et de la table
 *  en base de donnée avec le DAO (Data Access Object)
 *  un exemple d'utilisation de SQLite sous Android
 *  Elle se résume à un BYTEARRAY qui 
 *  correspond à une matrice de  descripteurs.
 *  
 * @author Olympe Kassa Romain Proyart
 */



@DatabaseTable(tableName="images")

public class Image {
	
	   @DatabaseField(generatedId=true)
       int id;
       
	   @DatabaseField(dataType = DataType.BYTE_ARRAY)
       byte[] blob;
	
	   @DatabaseField(dataType = DataType.BYTE_ARRAY)
       byte[] desc1;   
       		
	   @DatabaseField(dataType = DataType.BYTE_ARRAY)
       byte[] desc2;
       
	   @DatabaseField(dataType = DataType.BYTE_ARRAY)
       byte[] hist1;
       
       @DatabaseField(dataType = DataType.INTEGER)
       int m;
       
       @DatabaseField(dataType = DataType.INTEGER)
       int n;
       
       @DatabaseField(dataType = DataType.LONG_STRING)
       String infos;
       
       
       
    public Image()
    {
               // all persisted classes must define a no-arg constructor with at least package visibility
    
    }


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public byte[] getBlob() {
		return blob;
	}

	public void setBlob(byte[] blob) {
		this.blob = blob;
	}



	public byte[] getDesc1() {
		return desc1;
	}



	public void setDesc1(byte[] desc1) {
		this.desc1 = desc1;
	}



	public byte[] getDesc2() {
		return desc2;
	}



	public void setDesc2(byte[] desc2) {
		this.desc2 = desc2;
	}



	public byte[] getHist1() {
		return hist1;
	}



	public void setHist1(byte[] hist1) {
		this.hist1 = hist1;
	}



	public int getM() {
		return m;
	}



	public void setM(int m) {
		this.m = m;
	}



	public int getN() {
		return n;
	}



	public void setN(int n) {
		this.n = n;
	}



	public String getInfos() {
		return infos;
	}



	public void setInfos(String infos) {
		this.infos = infos;
	}
	
}
