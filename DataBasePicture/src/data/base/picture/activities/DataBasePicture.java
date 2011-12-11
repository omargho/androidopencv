package data.base.picture.activities;


import data.base.picture.R;
import data.base.picture.services.serviceFeature;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class DataBasePicture extends Activity {
		

    static boolean mBound = false;
    /** Called when the activity is first created. */
    @Override
    
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
 
        //thisIntent.getAction(); 
    	Button bouton = (Button) findViewById(R.id.buttonToast);
    	bouton.setOnClickListener( new OnClickListener()
    	{
    	    		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				Toast msg = Toast.makeText(DataBasePicture.this, "Chargement en cours...", Toast.LENGTH_LONG);

				msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);

				msg.show();
				
				startService(new Intent(DataBasePicture.this, serviceFeature.class));				
			}
    	});
    }
    
}



						    
			 
			
    