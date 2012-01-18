package com.pfe.okassa;
 
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.Toast;
 
public class Projet_MaBaseSQLite extends SQLiteOpenHelper {
 

	//The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.pfe.okassa/databases/";
	private static final String TABLE_IMAGES = "table_images";
	private static final String COL_ID = "ID";
	private static final String COL_IMAGE = "IMAGE_BLOB";
	private static final String COL_DESC1 = "DESC1_BLOB";
	private static final String COL_DESC2 = "DESC2_BLOB";
	private static final String COL_M = "M";
	private static final String COL_N = "N";
	private static final String COL_INFO = "INFO";
	
    private final Context context ;
 
	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		// TODO Auto-generated method stub
		return super.getReadableDatabase();
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		Toast msg = Toast.makeText(context, "BDD ouverte en écriture...", Toast.LENGTH_LONG);
		msg.show() ;
		return super.getWritableDatabase();
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		Toast msg = Toast.makeText(context, "base de données en ouverture...", Toast.LENGTH_LONG);
		msg.show() ;
		
	}

	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_IMAGES + " ("
	+ COL_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_IMAGE + "BLOB," + COL_DESC1 +
	" BLOB,"+ COL_DESC2 + "BLOB," + COL_M + "INTEGER,"+ COL_N + "INTEGER,"+ COL_INFO + "TEXT); "
	;

	public Projet_MaBaseSQLite(Context context, String name, CursorFactory factory,int version) {
	
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		this.context = context ;
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//on créé la table à partir de la requête écrite dans la variable CREATE_BDD

				db.execSQL(CREATE_BDD);
				Toast msg = Toast.makeText(context, "requete de création executée...", Toast.LENGTH_LONG);
				msg.show() ;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
		//comme ça lorsque je change la version les id repartent de 0
				db.execSQL("DROP TABLE " + TABLE_IMAGES + ";");
				onCreate(db);
		
	}
 
}