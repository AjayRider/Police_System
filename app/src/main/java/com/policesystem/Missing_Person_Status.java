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

public class Missing_Person_Status extends Fragment {
	
	ListView list;
	String[] id,pname,status,img;
	String[] name,email,age,gender,lastseen,detail;
	SharedPreferences sp;
	String uemail;
	TableRow prog;
	String user;
	Missing_Person_Status_Adapter mpsa;
	
	Timer timer;
	TimerTask timerTask;
	Handler handler = new Handler();
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		
		View v=inflater.inflate(R.layout.missing_person_status, container,false);
		list=(ListView) v.findViewById(R.id.missing_list);
		prog=(TableRow) v.findViewById(R.id.tbprog);
		sp=getActivity().getSharedPreferences("crime", Context.MODE_PRIVATE);
		uemail=sp.getString("email", "");
		user=sp.getString("user", "");
		timer = new Timer();
		
		if(user.compareTo("user")==0)
		{
			new getmissing().execute(uemail);
		}
		else if(user.compareTo("admin")==0)
		{
			new getmissadmin().execute();
		}
		return v;
		
	}
	
	
	public class getmissing extends AsyncTask<String, JSONObject, String>
	{

		@Override
		protected String doInBackground(String... p) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.viewMissE(p[0]);
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
				pname=new String[temp.length];
				img=new String[temp.length];
				status=new String[temp.length];
				for(int i=0;i<temp.length;i++)
				{
					String temp1[]=temp[i].split("\\*");
					id[i]=temp1[0];
					pname[i]=temp1[1];
					img[i]=temp1[6];
					status[i]=temp1[7];
				}
				
				mpsa=new Missing_Person_Status_Adapter(getActivity(),id,pname,img,status,user);
				list.setAdapter(mpsa);
				//prog.setVisibility(View.GONE);
			}
			else
			{
				//prog.setVisibility(View.GONE);
				Toast.makeText(getActivity(), "NO Crimes to Show", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public class getmissadmin extends AsyncTask<String, JSONObject, String>
	{

		@Override
		protected String doInBackground(String... p) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.viewMiss();
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
				pname=new String[temp.length];
				age=new String[temp.length];
				gender=new String[temp.length];
				lastseen=new String[temp.length];
				detail=new String[temp.length];
				name=new String[temp.length];
				email=new String[temp.length];
				img=new String[temp.length];
				status=new String[temp.length];
				for(int i=0;i<temp.length;i++)
				{
					String temp1[]=temp[i].split("\\*");
					id[i]=temp1[0];
					pname[i]=temp1[3];
					age[i]=temp1[4];
					gender[i]=temp1[5];
					lastseen[i]=temp1[6];
					detail[i]=temp1[7];
					name[i]=temp1[2];					
					email[i]=temp1[1];
					img[i]=temp1[8];
					status[i]=temp1[9];
				}
				
				mpsa=new Missing_Person_Status_Adapter(getActivity(),id,pname,age,gender,lastseen,detail,name,email,img,status,user);
				list.setAdapter(mpsa);
//
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
						int i=mpsa.getz();
						if (i==1)
						{
							new getmissadmin().execute();
							mpsa.setz(0);
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
