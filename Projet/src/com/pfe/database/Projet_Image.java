package com.pfe.database;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;


/**
 *  Création de la classe image et de la table en base
 *  de donnée avec le DAO (Data Access Object)
 *  un exemple d'utilisation de SQLite sous Android
 *   
 *    
 * @author Olympe Kassa Romain Proyart
 */

@DatabaseTable(tableName = "images")
public class Projet_Image {
	
	@DatabaseField(generatedId=true)
	private int id;
	
	@DatabaseField
	private byte[] blob;

	@DatabaseField
	private byte[] desc1;	
	
	@DatabaseField
	private byte[] desc2;
	
	@DatabaseField
	private byte[] hist;
	
	@DatabaseField
	private int nbr_col;
	
	@DatabaseField
	private int nbr_lign;
	
	@DatabaseField(canBeNull=true)
	private String infos;
	
	
	
	public Projet_Image(){
		// all persisted classes must define a no-arg constructor with at least package visibility
	}
 
 /**
  *  Getteur et Setteur de l'image
  * @return
  */
	
	public byte[] getBlob() {
		return blob;
	}
 
	public void setBlob(byte[] bs) {
		this.blob = bs;
	}
 
	/**
	 * Getteur et Setteur du descripteurs SIFT
	 * @return
	 */
	
	public byte[] getDesc1() {
		return desc1;
	}
 
	public void setDesc1(byte[] desc1) {
		this.desc1 = desc1;
	}

	/**Getteur et Setteur des descripteurs SURF
	 * 
	 * @return
	 */
	
	public byte[] getDesc2() {
		return desc2;
	}
 
	public void setDesc2(byte[] desc2) {
		this.desc2 = desc2;
	}

	
	/**Getteur et Setteur de l'histogramme
	 * 
	 * @return
	 */
	
	public byte[] getHist() {
		return hist;
	}
 
	public void setHist(byte[] hist) {
		this.hist = hist;
	}
	
	
	/**Getteur et Setteur de ligne
	 * 
	 * @return
	 */
	
	public int getLign() {
		return nbr_lign ;
	}
 
	public void setLign(int lign) {
		this.nbr_lign = lign ;
	}
	
	
	/**Getteur et Setteur de colonne
	 * 
	 * @return
	 */
	
	public int getCol() {
		return nbr_col ;
	}
 
	public void setCol(int col) {
		this.nbr_col = col;
	}
	
	/**Getteur et Setteur de l'info
	 * 
	 * @return
	 */
	
	public String getInfo() {
		return infos ;
	}
 
	public void setInfos(String infos) {
		this.infos = infos;
	}
  /*
   * A modifier (non-Javadoc)
   * @see java.lang.Object#toString()
   */
	@Override
	public String toString(){
		return "BLOB : "+ blob;
	}
	
	

}
