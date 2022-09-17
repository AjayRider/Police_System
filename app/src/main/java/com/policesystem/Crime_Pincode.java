package com.policesystem;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.policesystem.webservices.JSONParse;
import com.policesystem.webservices.RestAPI;

public class Crime_Pincode extends AppCompatActivity {
	
	EditText pincode;
	ImageButton search;
	TableRow prog,listtb;
	ListView list;
	String[] id,street,details,img,status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crime_pincode);
		Toast.makeText(Crime_Pincode.this, "Enter Pincode to find the Crimes in your Area", Toast.LENGTH_LONG).show();
		actionbar();
		prog=(TableRow) findViewById(R.id.tbprog);
		listtb=(TableRow) findViewById(R.id.tblist);
		list=(ListView) findViewById(R.id.crime_pincode_list);
		prog.setVisibility(View.GONE);
		listtb.setVisibility(View.GONE);

	}
	
	public class getcrime extends AsyncTask<String, JSONObject, String>
	
	{

		@Override
		protected String doInBackground(String... p) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.viewCrime(p[0]);
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
			if(result.compareTo("")!=0)
			{
				String temp[]=result.split("\\#");
				id=new String[temp.length];
				street=new String[temp.length];
				details=new String[temp.length];
				img=new String[temp.length];
				status=new String[temp.length];
				for(int i=0;i<temp.length;i++)
				{
					String temp1[]=temp[i].split("\\*");
					id[i]=temp1[0];
					street[i]=temp1[2];
					details[i]=temp1[3];
					img[i]=temp1[4];
					status[i]=temp1[5];
				}
				
				Crime_Pincode_Adapter cpa=new Crime_Pincode_Adapter(Crime_Pincode.this,id,street,details,img,status);
				list.setAdapter(cpa);
				prog.setVisibility(View.GONE);
				listtb.setVisibility(View.VISIBLE);
			}
			else
			{
				prog.setVisibility(View.GONE);
				listtb.setVisibility(View.GONE);
				Toast.makeText(Crime_Pincode.this, "NO Crimes to Show in the given Pincode", Toast.LENGTH_SHORT).show();
			}
		}
		}
	
	
	public void actionbar()
    {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View v = mInflater.inflate(R.layout.actionbar, null);
        pincode=(EditText) v.findViewById(R.id.crime_pincode_txt);
		search=(ImageButton) v.findViewById(R.id.crime_pincode_btn);

		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pincode.getText().toString().compareTo("")!=0)
				{
					new getcrime().execute(pincode.getText().toString());
					prog.setVisibility(View.VISIBLE);
					listtb.setVisibility(View.GONE);
				}
				else
				{
					prog.setVisibility(View.GONE);
					listtb.setVisibility(View.GONE);
					Toast.makeText(Crime_Pincode.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        mActionBar.setCustomView(v);
        mActionBar.setDisplayShowCustomEnabled(true);
    }
		
	

}
