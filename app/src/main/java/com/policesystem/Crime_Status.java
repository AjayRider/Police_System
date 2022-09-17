package com.policesystem;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.policesystem.webservices.JSONParse;
import com.policesystem.webservices.RestAPI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class Crime_Status extends Fragment {
	
	ListView list;
	String[] id,details,status;
	String[] name,street,city,zip,phone,email,pic;
	SharedPreferences sp;
	String uemail;
	TableRow prog;
	Crime_Status_Adapter csa;
	String user;
	
	Timer timer;
	TimerTask timerTask;
	Handler handler = new Handler();
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		
		View v=inflater.inflate(R.layout.crime_status, container,false);
		list=(ListView) v.findViewById(R.id.crime_list);
		prog=(TableRow) v.findViewById(R.id.tbprog);
		sp=getActivity().getSharedPreferences("crime", Context.MODE_PRIVATE);
		uemail=sp.getString("email", "");
		user=sp.getString("user", "");
		timer = new Timer();
		
		if(user.compareTo("user")==0)
		{
			new getcomplaint().execute(uemail);
		}
		else if(user.compareTo("admin")==0)
		{
			new getcompadmin().execute();
		}
		return v;
		
	}
	
	public class getcomplaint extends AsyncTask<String, JSONObject, String>
	{

		@Override
		protected String doInBackground(String... p) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.viewCrimeE(p[0]);
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
			prog.setVisibility(View.GONE);
			if(result.compareTo("")!=0)
			{
				String temp[]=result.split("\\#");
				id=new String[temp.length];
				details=new String[temp.length];
				status=new String[temp.length];
				for(int i=0;i<temp.length;i++)
				{
					String temp1[]=temp[i].split("\\*");
					id[i]=temp1[0];
					details[i]=temp1[3];
					status[i]=temp1[5];
				}
				
				csa=new Crime_Status_Adapter(getActivity(),id,details,status,user);
				list.setAdapter(csa);
				//prog.setVisibility(View.GONE);
			}
			else
			{
				//prog.setVisibility(View.GONE);
				Toast.makeText(getActivity(), "NO Crimes to Show", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public class getcompadmin extends AsyncTask<String, JSONObject, String>
	{

		@Override
		protected String doInBackground(String... p) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.viewCrimeAll();
				JSONParse jp=new JSONParse();
				a=jp.mparse(json);
			} catch (Exception e) {
				a="back"+e.getMessage();
			}
			return a;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			prog.setVisibility(View.GONE);

			if(result.compareTo("false")!=0)
			{
				result=result.substring(5,result.length());
				String temp[]=result.split("\\#");
				id=new String[temp.length];
				name=new String[temp.length];
				street=new String[temp.length];
				city=new String[temp.length];
				zip=new String[temp.length];
				phone=new String[temp.length];
				email=new String[temp.length];
				pic=new String[temp.length];
				details=new String[temp.length];
				status=new String[temp.length];
				for(int i=0;i<temp.length;i++)
				{
					String temp1[]=temp[i].split("\\*");
					id[i]=temp1[0];
					name[i]=temp1[1];
					street[i]=temp1[2];
					city[i]=temp1[3];
					zip[i]=temp1[4];
					phone[i]=temp1[5];
					email[i]=temp1[6];					
					pic[i]=temp1[8];
					details[i]=temp1[7];
					status[i]=temp1[9];
				}
				
				csa=new Crime_Status_Adapter(getActivity(),id,details,status,name,street,city,zip,phone,email,pic,user);
				list.setAdapter(csa);

				initializeTimerTask();
				timer.schedule(timerTask, 0, 500);
			}
			else
			{
				Toast.makeText(getActivity(), "NO Complaints to Show", Toast.LENGTH_SHORT).show();
			}
			
		}
		
		
		
	}
	
	public void initializeTimerTask() {

		timerTask = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						int i=csa.getz();
						if (i==1)
						{
							new getcompadmin().execute();
							csa.setz(0);
						}
					}
				});
			}
		};
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		timer.cancel();
	}
	
	

}
