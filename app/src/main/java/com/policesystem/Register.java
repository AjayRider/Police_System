package com.policesystem;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.policesystem.webservices.JSONParse;
import com.policesystem.webservices.RestAPI;

public class Register extends Activity {
	
	EditText name,cont,email,pass,cpass;
	Button btn;
	String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		name=(EditText) findViewById(R.id.reg_name);
		cont=(EditText) findViewById(R.id.reg_cont);
		email=(EditText) findViewById(R.id.reg_email);
		pass=(EditText) findViewById(R.id.reg_pass);
		cpass=(EditText) findViewById(R.id.reg_cpass);
		btn=(Button) findViewById(R.id.reg_btn);
		
		new getid().execute();
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(name.getText().toString().compareTo("")!=0 || cont.getText().toString().compareTo("")!=0 || email.getText().toString().compareTo("")!=0 || pass.getText().toString().compareTo("")!=0 || cpass.getText().toString().compareTo("")!=0 )
				{
					if(name.getText().toString().compareTo("")!=0)
					{
						if(cont.getText().toString().compareTo("")!=0)
						{
							if(email.getText().toString().compareTo("")!=0)
							{
								if(pass.getText().toString().compareTo("")!=0)
								{
									if(cpass.getText().toString().compareTo("")!=0)
									{
										if(pass.getText().toString().compareTo(cpass.getText().toString())==0)
										{
											new registertask().execute(id,name.getText().toString(),cont.getText().toString(),email.getText().toString(),pass.getText().toString());
										}
										else
										{
											pass.setText("");
											cpass.setText("");
											pass.requestFocus();
											Toast.makeText(Register.this, "Passwords dont Match!", Toast.LENGTH_SHORT).show();
										}
									}
									else
									{
										cpass.setError("Confirm the Password");
										cpass.requestFocus();
									}
								}
								else
								{
									pass.setError("Enter Password");
									pass.requestFocus();
								}
							}
							else
							{
								email.setError("Enter Email");
								email.requestFocus();
							}
						}
						else
						{
							cont.setError("Enter Contact");
							cont.requestFocus();
						}
					}
					else
					{
						name.setError("Enter Name");
						name.requestFocus();
					}
				}
				else
				{
					Toast.makeText(Register.this, "All Fields are Mandatory", Toast.LENGTH_SHORT).show();
				}
				 
			}
		});
	}
	
	public class getid extends AsyncTask<String, JSONObject, String>
	{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.getUId();
				JSONParse jp=new JSONParse();
				a=jp.mainparse(json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				a=e.getMessage();
			}
			return a;
		}
		
		@Override
		protected void onPostExecute(String s) {
			// TODO Auto-generated method stub
			super.onPostExecute(s);
			if(s.compareTo("")!=0)
			{
				id=s;
			}
			else
			{
				Toast.makeText(Register.this, s, Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	
	public class registertask extends AsyncTask<String, JSONObject, String>
	{

		@Override
		protected String doInBackground(String... p) {
			// TODO Auto-generated method stub
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.register(p[0], p[1], p[2], p[3], p[4]);
				JSONParse jp=new JSONParse();
				a=jp.mainparse(json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				a=e.getMessage();
			}
			return a;
		}
		
		@Override
		protected void onPostExecute(String s) {
			// TODO Auto-generated method stub
			super.onPostExecute(s);
			if(s.compareTo("")!=0)
			{
				if(s.compareTo("true")==0)
				{
					finish();
				}
				else
				{
					Toast.makeText(Register.this, s, Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(Register.this, s, Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}

}

