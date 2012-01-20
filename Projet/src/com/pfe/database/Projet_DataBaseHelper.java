package com.pfe.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Projet_DataBaseHelper extends OrmLiteSqliteOpenHelper {

	private Context _context;
	private static final String DATABASE_NAME = "images.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<Projet_Image, String> simpleDao = null;
	private RuntimeExceptionDao<Projet_Image, String> simpleRuntimeDao = null;

	public Projet_DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		_context = context;
	}

	public Dao<Projet_Image, String> getDao() throws SQLException {
		if (simpleDao == null) {
			try {
				simpleDao = getDao(Projet_Image.class);
			} catch (java.sql.SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return simpleDao;
	}

	public RuntimeExceptionDao<Projet_Image, String> getSimpleDataDao() {
		if (simpleRuntimeDao == null) {
			simpleRuntimeDao = getRuntimeExceptionDao(Projet_Image.class);
		}
		return simpleRuntimeDao;
	}

	//method for list of image
	public List<Projet_Image> GetData()
	{
		Projet_DataBaseHelper helper = new Projet_DataBaseHelper(_context);
		RuntimeExceptionDao<Projet_Image, String> simpleDao = helper.getSimpleDataDao();
		List<Projet_Image> list = simpleDao.queryForAll();
		return list;
	}

	//method for insert data
	public int addData(Projet_Image person)
	{
		RuntimeExceptionDao<Projet_Image, String> dao = getSimpleDataDao();
		int i = dao.create(person);
		return i;
	}

	//method for delete all rows
	public void deleteAll()
	{
		RuntimeExceptionDao<Projet_Image, String> dao = getSimpleDataDao();
		List<Projet_Image> list = dao.queryForAll();
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
			Log.i(Projet_DataBaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Projet_Image.class);
		} catch (SQLException e) {
			Log.e(Projet_DataBaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(Projet_DataBaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Projet_Image.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(Projet_DataBaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}    
 
