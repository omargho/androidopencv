package com.pfe.okassa;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
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
	
	
	private static final String DATABASE_NAME = "images.db";
	private static final int DATABASE_VERSION = 1;

	
	private RuntimeExceptionDao<Image, String> simpleRuntimeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	/**
	 * @return
	 * @throws SQLException
	 */

	public RuntimeExceptionDao<Image, String> getSimpleDataDao() {
		if (simpleRuntimeDao == null) {
			simpleRuntimeDao = getRuntimeExceptionDao(Image.class);
		}
		return simpleRuntimeDao;
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
				Log.i(DatabaseHelper.class.getName(), "database created");
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
	

