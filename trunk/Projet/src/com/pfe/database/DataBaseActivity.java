package com.pfe.database;

import android.os.Bundle;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

public class DataBaseActivity extends OrmLiteBaseActivity {

	@Override
	public ConnectionSource getConnectionSource() {
		// TODO Auto-generated method stub
		return super.getConnectionSource();
	}

	@Override
	public OrmLiteSqliteOpenHelper getHelper() {
		// TODO Auto-generated method stub
		return super.getHelper();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
