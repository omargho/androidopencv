package com.pfe.okassa;


import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 *	Cette classe va nous permettre dbe g�rer
 *	l'insertion, la suppression, la modification
 * 	des images dans la BDD (Base De Donn�es) ainsi
 *  que de faire des requ�tes pour r�cup�rer
 *  une image contenue dans la base de donn�es. 
 *
 *
 * @author olympe kassa
 *
 */



public class Projet_ImagesBDD {
	
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "base_img";
 
	private static final String TABLE_IMAGES = "pictures";
	private static final String COL_ID = "ID";
	private static final String COL_IMAGE = "IMAGE_BLOB";
	private static final String COL_DESC1 = "DESC1_BLOB";
	private static final String COL_DESC2 = "DESC2_BLOB";
	private static final String COL_M = "M";
	private static final String COL_N = "N";
	private static final String COL_INFO = "INFO";
	
	
	private SQLiteDatabase bdd;
	 
	private Projet_MaBaseSQLite projet_MaBaseSQLite;
	
	private Context context ;
	
	public Projet_ImagesBDD(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context ;
		projet_MaBaseSQLite = new Projet_MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
	}

	
	public SQLiteDatabase getBDD(){
		return bdd;
	}
	
 
 
	public int removeImageWithID(int id){
		//Suppression d'un livre de la BDD gr�ce � l'ID
		return bdd.delete(TABLE_IMAGES, COL_ID + " = " +id, null);
	}
 
	
	//on ouvre la BDD en �criture
	public void open(){
		
//		Toast msg = Toast.makeText(context, "la base de donn�es est ouverte...", Toast.LENGTH_LONG);
//		msg.show() ;
	
//		Projet_DataBaseHelper.close();


		bdd = projet_MaBaseSQLite.getWritableDatabase();
		
		//projet_MaBaseSQLite.onOpen(bdd) ;
		
;	}
 
	//on ferme l'acc�s � la BDD
	public void close(){
		
//		Toast msg = Toast.makeText(context, "la base de donn�es est ferm�e...", Toast.LENGTH_LONG);
//		msg.show() ;
		bdd.close();
	}
	
	
	/*
	 * 
	 * Cette methode permet d'inserer une image dans 
	 * la base de donn�es
	 * 
	 */
	
	
	public long insertImage(Projet_Image projet_Image) {
		// TODO Auto-generated method stub
		//Cr�ation d'un ContentValues 
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associ� � une cl� 
		//(qui est le nom de la colonne dans laquelle
		//on veut mettre la valeur)
		values.put(COL_IMAGE, projet_Image.getBlob());
		values.put(COL_DESC1, projet_Image.getDesc1());
		values.put(COL_DESC2, projet_Image.getDesc2());
		//on ins�re l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_IMAGES, null, values);
	}


	/*
	 * Cette methode nous permet de chercher dans la BDD un blob
	 * 
	 *  IDEE: Mettre tous les  bb dans une liste de byte en 
	 *  global public qui soit accessible par d'autre classes
	 *  Appeler d'abord byte to Objet pour avoir des descripteurs
	 *  dans une liste puis appeler 
	 *  
	 * 
	 */
	public List<byte[]> getImage(int choix)
	{	
		List<byte[]> list_bb = null ;
		byte[] bb = null ;
		
		switch(choix)
		{
		case 0 :
			
			/**
			 * On renvoie la liste des images
			 */
			Cursor result= bdd.rawQuery("SELECT"+COL_IMAGE +"FROM TABLE_IMAGES", null);
			result.moveToFirst();
			while (!result.isAfterLast())
			{
				bb = result.getBlob(result.getPosition()) ;
				list_bb.add(bb) ;
				result.moveToNext();
			}
			result.close();
		case 1:
			/**
			 * On renvoie la liste des decripteurs SIFT
			 */
			result= bdd.rawQuery("SELECT"+COL_DESC1 +"FROM TABLE_IMAGES", null);
			result.moveToFirst();
			while (!result.isAfterLast())
			{
				bb = result.getBlob(result.getPosition()) ;
				list_bb.add(bb) ;
				result.moveToNext();
			}
			result.close();
		case 2:
			/**
			 * On renvoie la liste des descripteurs SURF
			 */
			result= bdd.rawQuery("SELECT"+COL_DESC2 +"FROM TABLE_IMAGES", null);
			result.moveToFirst();
			while (!result.isAfterLast())
			{
				bb = result.getBlob(result.getPosition()) ;
				list_bb.add(bb) ;
				result.moveToNext();
			}
			result.close();
		case 3:
			/**
			 * On doit rajouter les requetes pour recuperer m et n 
			 */

		default :
			list_bb = null ;
		}
							
		return list_bb;
	}
	
	
}
