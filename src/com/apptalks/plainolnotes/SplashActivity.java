package com.apptalks.plainolnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity 
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		setContentView(R.layout.splash_activity);
 		
 		Thread logoTimer = new Thread()
		{
			public void run() 
			{
				try 
				{
					sleep(5000);
					Intent i1 = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(i1);
				} catch (InterruptedException e) 
				{
 					e.printStackTrace();
				}

			}

		};
		logoTimer.start();
	}

}
