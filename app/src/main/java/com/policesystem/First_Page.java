package com.policesystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class First_Page extends AppCompatActivity {
	
	
	ImageView crime,missing,login;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstpage);
		
		crime=(ImageView) findViewById(R.id.crimebtn);
		missing=(ImageView) findViewById(R.id.missingbtn);
		login=(ImageView) findViewById(R.id.loginbtn);
		
		crime.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent maction) {
				int action=maction.getAction();
				
				switch (action & MotionEvent.ACTION_MASK) 
				{
				case MotionEvent.ACTION_DOWN:
					crime.setImageResource(R.drawable.crimebtnpress);
					 
					break;
				case MotionEvent.ACTION_UP:
					crime.setImageResource(R.drawable.crimebtn);
					Intent i=new Intent(First_Page.this,Crime_Pincode.class);
					startActivity(i);
					break;
					
				}
				return true;
			}
		});
		
		missing.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent maction) {
				int action=maction.getAction();
				
				switch (action & MotionEvent.ACTION_MASK) 
				{
				case MotionEvent.ACTION_DOWN:
					missing.setImageResource(R.drawable.missingpress);
					 
					break;
				case MotionEvent.ACTION_UP:
					missing.setImageResource(R.drawable.missing);
					Intent i=new Intent(First_Page.this,Missing_Pincode.class);
					startActivity(i);
					break;
					
				}
				return true;
			}
		});

		login.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent maction) {
				int action=maction.getAction();
				
				switch (action & MotionEvent.ACTION_MASK) 
				{
				case MotionEvent.ACTION_DOWN:
					login.setImageResource(R.drawable.loginpress);
					 
					break;
				case MotionEvent.ACTION_UP:
					login.setImageResource(R.drawable.login);
					Intent i=new Intent(First_Page.this,Login.class);
					startActivity(i);
					break;
					
				}
				return true;
			}
		});
		
		
	}

}
