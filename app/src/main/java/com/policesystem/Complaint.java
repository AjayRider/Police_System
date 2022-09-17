package com.policesystem;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.policesystem.webservices.JSONParse;
import com.policesystem.webservices.RestAPI;

public class Complaint extends Fragment {
	
	String id,uemail;
	SharedPreferences sp;
	EditText add,city,pincode,subject,complaint;
	Button btn;
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		
		View v=inflater.inflate(R.layout.complaint, container,false);
		sp=getActivity().getSharedPreferences("crime", Context.MODE_PRIVATE);
		uemail=sp.getString("email", "");
		add=(EditText) v.findViewById(R.id.comp_add);
		city=(EditText) v.findViewById(R.id.comp_city);
		pincode=(EditText) v.findViewById(R.id.comp_pin);
		subject=(EditText) v.findViewById(R.id.comp_subj);
		complaint=(EditText) v.findViewById(R.id.comp_complaint);
		btn=(Button) v.findViewById(R.id.comp_btn);
		
		new complid().execute();
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(add.getText().toString().compareTo("")!=0 || city.getText().toString().compareTo("")!=0 ||pincode.getText().toString().compareTo("")!=0 ||subject.getText().toString().compareTo("")!=0 ||complaint.getText().toString().compareTo("")!=0)
				{
					if(add.getText().toString().compareTo("")!=0)
					{
						if(city.getText().toString().compareTo("")!=0)
						{
							if(pincode.getText().toString().compareTo("")!=0)
							{
								if(subject.getText().toString().compareTo("")!=0)
								{
									if(complaint.getText().toString().compareTo("")!=0)
									{
										new addcompl().execute(id,add.getText().toString(),uemail,city.getText().toString(),pincode.getText().toString(),subject.getText().toString(),complaint.getText().toString());
									}
									else
									{
										complaint.setError("Enter your Complaint");
										complaint.requestFocus();
									}
								}
								else
								{
									subject.setError("Enter Subject");
									subject.requestFocus();
								}
							}
							else
							{
								pincode.setError("Enter Pincode");
								pincode.requestFocus();
							}
						}
						else
						{
							city.setError("Enter City");
							city.requestFocus();
						}
					}
					else
					{
						add.setError("Enter Address");
						add.requestFocus();
					}
				}
				else
				{
					Toast.makeText(getActivity(), "All Fields are Mandatory", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		return v;
		
	}
	
	public class complid extends AsyncTask<String , JSONObject, String>
	{

		@Override
		protected String doInBackground(String... arg0) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.getCompId();
				JSONParse jp=new JSONParse();
				a=jp.mainparse(json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				a=e.getMessage();
			}
			return a;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.compareTo("")==0)
			{
				Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
			}
			else
			{
				id=result;
			}
		}
		
	}
	
	public class addcompl extends AsyncTask<String , JSONObject, String>
	{

		@Override
		protected String doInBackground(String... p) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.AddComp(p[0], p[1], p[2], p[3], p[4], p[5], p[6]);
				JSONParse jp=new JSONParse();
				a=jp.mainparse(json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				a=e.getMessage();
			}
			return a;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.compareTo("true")==0)
			{
				add.setText("");
				city.setText("");
				pincode.setText("");
				subject.setText("");
				complaint.setText("");
				Toast.makeText(getActivity(), "Complaint Registered", Toast.LENGTH_SHORT).show();
				
			}
			else
			{
				Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	
}
