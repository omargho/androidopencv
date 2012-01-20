package database.pfe;

import java.sql.SQLException;

import main.pfe.Image;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Création et gestion de la base de donnée
 * 
 * @author Romain Proyart Olympe Kassa
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "images.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 2;

	public DatabaseHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// the DAO object we use to access the SimpleData table
	private Dao<Image, Integer> imageDao = null;

	@Override
	public void onCreate(final SQLiteDatabase db, final ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Image.class);
		}
		catch (final SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust the various data to
	 * match the new version number.
	 */
	@Override
	public void onUpgrade(final SQLiteDatabase db, final ConnectionSource connectionSource, final int oldVersion, final int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Image.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		}
		catch (final SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	public Dao<Image, Integer> getImageDao() throws SQLException {
		if (this.imageDao == null) {
			this.imageDao = getDao(Image.class);
		}
		return this.imageDao;
	}

	

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		this.imageDao = null;
	}

}