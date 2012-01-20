package database.pfe;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import main.pfe.Image;


/**
 * Classe qui permet d'effectuer des op�rations sur la table qui correspond � la classe Image :
 * 
 * getImages(), saveOrUpdateImage(), ...
 * 
 * @author Olympe Kassa Romain Proyart
 */
public class Repository {
	private final static String LOG_TAG = "Repository";

	private Dao<Image, Integer> imageDao;

	public Repository(final DatabaseHelper databaseHelper) {
		this.imageDao = getImageDao(databaseHelper);
	}

	public void clearData() {
		final List<Image> images = getImages();
		for (final Image image : images) {
			deleteImage(image);
		}
	}
	
	/**
	 * Retourne une liste avec toutes les instances de l'objet Image
	 * 
	 * @return
	 */

	public List<Image> getImages() {
		try {
			return this.imageDao.queryForAll();
		}
		catch (final SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<Image>();
	}
	
	
	
	/**
	 * Permet d'ajouter une Image � la base, et de la mettre � jour si elle est d�j� pr�sente
	 * 
	 * @param image
	 */
	public void saveOrUpdateImage(final Image image) {
		try {
			this.imageDao.createOrUpdate(image);
		}
		catch (final SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Supprime l'image pass�e en param�tre de la base
	 * 
	 * @param image
	 */
	public void deleteImage(final Image image) {
		try {
			this.imageDao.delete(image);
		}
		catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * R�cup�re le lien entre la table et la bdd
	 * 
	 * @param databaseHelper
	 * @return
	 */
	private Dao<Image, Integer> getImageDao(final DatabaseHelper databaseHelper) {
		if (null == this.imageDao) {
			try {
				this.imageDao = databaseHelper.getImageDao();
			}
			catch (final SQLException e) {
				Log.e(LOG_TAG, "Unable to load DAO: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return this.imageDao;
	}
}
