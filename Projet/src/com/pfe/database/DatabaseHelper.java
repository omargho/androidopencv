package com.pfe.database;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


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

	//method for list of images
	public List<Image> GetData()
	{
		DatabaseHelper helper = new DatabaseHelper(_context);
		RuntimeExceptionDao<Image, String> simpleDao = helper.getSimpleDataDao();
		List<Image> list = simpleDao.queryForAll();
		return list;
	}

	//method for insert data
	public int addData(Image person)
	{
		RuntimeExceptionDao<Image, String> dao = getSimpleDataDao();
		int i = dao.create(person);
		return i;
	}

	//method for delete all rows
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
	

