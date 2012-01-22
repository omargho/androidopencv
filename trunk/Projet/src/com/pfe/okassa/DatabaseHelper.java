package com.pfe.okassa;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


/**
 *  Creation de la classe DatabaseHelper elle
 *  permet de gerer avec le DAO (Data Access Object)
 *  l'insertion, la recuperation et le retrait des 
 *  images de la base de données.
 *  
 *  the database is done through a DAO 
 *  (Data Access Object) which exposes 
 *  operations like create, update, delete, 
 *  and more general purpose objects like 
 *  QueryBuilder. Here's an example of 
 *  creating a DAO and using it to create 
 *  an Image in the database.
 *  
 * @author Olympe Kassa & Romain Proyart
 * 
 * {code.google.com/p/androidopencv}
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private Context _context;
	private static final String DATABASE_NAME = "images.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<Image, String> simpleDao = null;
	private RuntimeExceptionDao<Image, String> simpleRuntimeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		_context = context;
	}
	/**
	 *   DAO 
	 *  (Data Access Object) which exposes 
	 *  operations like create, update, delete, 
	 *  and more general purpose objects like 
	 *  QueryBuilder
	 * @return
	 * @throws SQLException
	 */
	public Dao<Image, String> getDao() throws SQLException {
		if (simpleDao == null) {
			try {
				simpleDao = getDao(Image.class);
			} catch (java.sql.SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return simpleDao;
	}

	public RuntimeExceptionDao<Image, String> getSimpleDataDao() {
		if (simpleRuntimeDao == null) {
			simpleRuntimeDao = getRuntimeExceptionDao(Image.class);
		}
		return simpleRuntimeDao;
	}

	/**
	 * method for list of images
	 * @return
	 */
	public List<Image> GetData()
	{
		DatabaseHelper helper = new DatabaseHelper(_context);
		RuntimeExceptionDao<Image, String> simpleDao = helper.getSimpleDataDao();
		List<Image> list = simpleDao.queryForAll();
		return list;
	}

	/**
	 * method for insert image
	 * @param image
	 * @return 0 if not done int
	 */
	public int addData(Image image)
	{
		int i = 0 ;
		RuntimeExceptionDao<Image, String> dao = getSimpleDataDao();
		i = dao.create(image);
		return i;
	}

	/**
	 * method for delete all rows
	 */
	public void deleteAll()
	{
		RuntimeExceptionDao<Image, String> dao = getSimpleDataDao();
		List<Image> list = dao.queryForAll();
		dao.delete(list);
	}

	@Override
	public void close() {
		super.close();
		simpleRuntimeDao = null;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			try {
				TableUtils.createTable(connectionSource, Image.class);
			} catch (java.sql.SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			try {
				TableUtils.dropTable(connectionSource, Image.class, true);
			} catch (java.sql.SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
}
	

