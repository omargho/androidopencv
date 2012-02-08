package com.rproyart.okassa;

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
	
	// INTEGER AUTOINCREMEMENT PRIMARY KEY
    @DatabaseField(generatedId = true)
    int id;
    // HISTOGRAM OF IMAGE
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    byte[] hist;
     // INFORMATIONS OF IMAGE LIKE AUTHOR, HEIGHT WIDTH....      
    @DatabaseField
    String infos;

    @DatabaseField
    String nom;

    
 public Image()
 {
     Log.i("IMAGE", "hist constructor");

 }   
    
 public Image(byte[] hist, String infos, String nom)
 {
     Log.i("IMAGE", "hist constructor");
     this.hist = hist ;
     this.infos = infos ;
     this.nom = nom ;
 }
 

     /**
      * toString
      */
     @Override
     public String toString() {
             StringBuilder sb = new StringBuilder();
             sb.append("id=").append(id);
             sb.append(", ").append("id=").append(id);
             sb.append(", ").append("infos=").append(infos);
             return sb.toString();
     }
}
