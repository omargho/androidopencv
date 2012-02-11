package com.rproyart.okassa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class acceuil extends Activity implements OnTouchListener {

	
	Button button;
	
	@Override

	public void onCreate(Bundle savedInstanceState){

	      super.onCreate(savedInstanceState);

	      button.setOnTouchListener(this);
	      
	      View v = findViewById(R.id.RelativeLayout1) ;

	      v.setOnTouchListener(this);
	      
	      setContentView(v);

	}

	 

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

	            Intent myIntent = new Intent(v.getContext(), InfosImage.class);

	            startActivity(myIntent);

		return false;
	}


}
