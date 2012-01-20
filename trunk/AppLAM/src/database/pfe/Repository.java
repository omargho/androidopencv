package database.pfe;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import main.pfe.Image;

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
	 * Permet d'ajouter une Image à la base, et de la mettre à jour si elle est déjà présente
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
	 * Supprime l'image passée en paramètre de la base
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
	 * 
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
