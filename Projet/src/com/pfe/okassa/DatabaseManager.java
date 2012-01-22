package com.pfe.okassa;

import java.util.List;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.content.Context;
import android.content.Intent;

/**
 *   DAO 
  (Data Access Object) which exposes 
  operations like create, update, delete, 
 and more general purpose objects like 
  QueryBuilder
 
 */

public class DatabaseManager {
	
	static private DatabaseManager instance;

    static public void init(Context ctx) {
        if (null==instance) {
            instance = new DatabaseManager(ctx);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private DatabaseHelper helper;
    
    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }
    
	/**
	 * method for list of images
	 * @return
	 */
	public List<Image> GetData()
	{
		
		RuntimeExceptionDao<Image, String> simpleDao = getHelper().getSimpleDataDao();
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
		RuntimeExceptionDao<Image, String> dao = getHelper().getSimpleDataDao();
		i = dao.create(image);
		return i;
	}

	/**
	 * method for delete all rows
	 */
	public void deleteAll()
	{
		RuntimeExceptionDao<Image, String> dao = getHelper().getSimpleDataDao();
		List<Image> list = dao.queryForAll();
		dao.delete(list);
	}
    

}
