package com.pfe.okassa;


/**
 * Création d'une classe image toute simple pour
 *  un exemple d'utilisation de SQLite sous Android
 *  Elle se résume à un BYTEARRAY qui 
 *  correspond à une matrice de  descripteurs.
 *  
 * @author Olympe Kassa
 */

public class Projet_Image {
	
	private byte[] blob;
	private byte[] desc1;
	private byte[] desc2;
	
	public Projet_Image(byte[] blob,byte[] desc1, byte[] desc2 ){
		this.blob = blob;
		this.desc1 = desc1 ;
		this.desc2 = desc2 ;
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

	
  /*
   * A modifier (non-Javadoc)
   * @see java.lang.Object#toString()
   */
	@Override
	public String toString(){
		return "BLOB : "+ blob;
	}
	
	

}
