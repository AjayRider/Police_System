package com.policesystem;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.policesystem.webservices.JSONParse;
import com.policesystem.webservices.RestAPI;

public class Complaint_status_adapter extends ArrayAdapter<String>{
	
	Context con;
	String[] id,sub,comp,status;
	String[] name,city,add,pin,phone,email;
	int pos;
	String user;
	Dialog da;
	int z=0;

	public Complaint_status_adapter(Context context, String[] id,String[] sub, String[] comp, String[] status,String user) 
	{
		super(context, R.layout.complaint_status_list_item,id);
		con=context;
		this.id=id;
		this.sub=sub;
		this.comp=comp;
		this.status=status;
		this.user=user;
		
	}
	
	public Complaint_status_adapter(Context context, String[] id,String[] name, String[] city, String[] add, String[] pin, String[] phone, String[] email, String[] sub, String[] comp,String[] status, String user) {
		super(context, R.layout.complaint_status_list_item,id);
		con=context;
		this.id=id;
		this.sub=sub;
		this.comp=comp;
		this.status=status;
		this.name=name;
		this.city=city;
		this.user=user;
		this.add=add;
		this.pin=pin;
		this.phone=phone;
		this.email=email;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater li=LayoutInflater.from(con);
		View v=li.inflate(R.layout.complaint_status_list_item, null,true);
		
		TextView subtxt,comptxt,statustxt;
		subtxt=(TextView) v.findViewById(R.id.comp_user_sub);
		comptxt=(TextView) v.findViewById(R.id.comp_user_complaint);
		statustxt=(TextView) v.findViewById(R.id.comp_user_status);
		Button more=(Button) v.findViewById(R.id.comp_user_btn);
		String s="<b>"+"Subject: "+"</b>"+sub[position];
		String s1="<b>"+"Complaint Details: "+"</b>"+comp[position];
		String s2="<b>"+"Status: "+"</b>"+"<font color=\"red\">"+status[position]+"</font>";
		subtxt.setText(Html.fromHtml(s));
		comptxt.setText(Html.fromHtml(s1));
		statustxt.setText(Html.fromHtml(s2));
		
		TextView nametxt,citytxt;
		nametxt=(TextView) v.findViewById(R.id.comp_user_name);
		citytxt=(TextView) v.findViewById(R.id.comp_user_city);
		TableRow nametb,citytb;
		nametb=(TableRow) v.findViewById(R.id.comp_user_nametb);
		citytb=(TableRow) v.findViewById(R.id.comp_user_citytb);
		
		if(user.compareTo("admin")==0)
		{
			nametxt.setVisibility(View.VISIBLE);
			citytxt.setVisibility(View.VISIBLE);
			String s3="<b>"+"Name: "+"</b>"+name[position];
			String s4="<b>"+"City: "+"</b>"+city[position];
			nametxt.setText(Html.fromHtml(s3));
			citytxt.setText(Html.fromHtml(s4));
		}
		else
		{
			nametxt.setVisibility(View.GONE);
			citytxt.setVisibility(View.GONE);
			nametb.setVisibility(View.GONE);
			citytb.setVisibility(View.GONE);
			
		}
		
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pos=position;
				if(user.compareTo("admin")==0)
				{
					dailog();
				}
				else
				{
				new getcompbyid().execute(id[position]);
				}
				
			}
		});
		
		return v;
	}
	
	public class getcompbyid extends AsyncTask<String, JSONObject, String>
	{

		@Override
		protected String doInBackground(String... p) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.CompDet(p[0]);
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
			String temp[]=result.split("\\*");
			
			Dialog d=new Dialog(con);
			d.requestWindowFeature(Window.FEATURE_NO_TITLE);
			d.setContentView(R.layout.complaint_status_dialog);
			
			TextView cid,sub,city,state,complaint,status;
			cid=(TextView) d.findViewById(R.id.cd_id);
			sub=(TextView) d.findViewById(R.id.cd_sub);
			city=(TextView) d.findViewById(R.id.cd_city);
			state=(TextView) d.findViewById(R.id.cd_state);
			complaint=(TextView) d.findViewById(R.id.cd_comp);
			status=(TextView) d.findViewById(R.id.cd_status);
			
			String s="<b>"+"Id: "+"</b>"+ id[pos];
			String s1="<b>"+"Subject: "+"</b>"+ temp[3];
			String s3="<b>"+"City: "+"</b>"+ temp[1];
			String s4="<b>"+"Pincode: "+"</b>"+ temp[2];
			String s5="<b>"+"Complaint: "+"</b>"+ temp[4];
			String s6="<b>"+"Status: "+"</b>"+"<font color=\"red\">"+temp[5]+"</font>";
			
			cid.setText(Html.fromHtml(s));
			sub.setText(Html.fromHtml(s1));
			city.setText(Html.fromHtml(s3));
			state.setText(Html.fromHtml(s4));
			complaint.setText(Html.fromHtml(s5));
			status.setText(Html.fromHtml(s6));
			
			d.show();
		}
	}
	
	public void dailog()
	{
		da=new Dialog(con);
		da.requestWindowFeature(Window.FEATURE_NO_TITLE);
		da.setContentView(R.layout.complaint_status_dialog_admin);
		final TextView idt,namet,subt,compt,addt,cityt,pint,phnt,emailt;
		final Spinner statust;
		Button save,can;
		idt=(TextView) da.findViewById(R.id.cd_id);
		namet=(TextView) da.findViewById(R.id.cd_name);
		subt=(TextView) da.findViewById(R.id.cd_sub);
		compt=(TextView) da.findViewById(R.id.cd_comp);
		addt=(TextView) da.findViewById(R.id.cd_add);
		cityt=(TextView) da.findViewById(R.id.cd_city);
		pint=(TextView) da.findViewById(R.id.cd_pin);
		phnt=(TextView) da.findViewById(R.id.cd_Phone);
		emailt=(TextView) da.findViewById(R.id.cd_email);
		statust=(Spinner) da.findViewById(R.id.cd_status);
		save=(Button) da.findViewById(R.id.cd_save);
		can=(Button) da.findViewById(R.id.cd_can);
		
		String ids="<b>"+"Id: "+"</b>"+ id[pos];
		String names="<b>"+"Name: "+"</b>"+ name[pos];
		String subs="<b>"+"Subject: "+"</b>"+ sub[pos];
		String comps="<b>"+"Complaint"+"</b>"+ comp[pos];
		String adds="<b>"+"Address: "+"</b>"+ add[pos];
		String citys="<b>"+"City: "+"</b>"+ city[pos];
		String pins="<b>"+"pincode: "+"</b>"+ pin[pos];
		String phns="<b>"+"Phone: "+"</b>"+ phone[pos];
		String emails="<b>"+"Email: "+"</b>"+ email[pos];
		
		idt.setText(Html.fromHtml(ids));
		namet.setText(Html.fromHtml(names));
		subt.setText(Html.fromHtml(subs));
		compt.setText(Html.fromHtml(comps));
		addt.setText(Html.fromHtml(adds));
		cityt.setText(Html.fromHtml(citys));
		pint.setText(Html.fromHtml(pins));
		phnt.setText(Html.fromHtml(phns));
		emailt.setText(Html.fromHtml(emails));
		
		String op = "Submitted,Seen,Processing,Completed";
		List<String> l = Arrays.asList(op.split(","));
		ArrayAdapter<String> adp1=new ArrayAdapter<String>(con,android.R.layout.simple_list_item_1,l); 
		statust.setAdapter(adp1);
		if(status[pos].compareTo("Submitted")==0)
		{
			statust.setSelection(0);
		}
		else if(status[pos].compareTo("Seen")==0)
		{
			statust.setSelection(1);
		}
		else if(status[pos].compareTo("Processing")==0)
		{
			statust.setSelection(2);
		}
		else if(status[pos].compareTo("Completed")==0)
		{
			statust.setSelection(3);
		}
		
		
		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new update().execute(id[pos],statust.getSelectedItem().toString());
				
			}
		});
		
		can.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				da.cancel();
			}
		});
		
		da.show();
		
		
	}
	
	public class update extends AsyncTask<String, JSONObject, String>
	{

		@Override
		protected String doInBackground(String... p) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.updateComp(p[0], p[1]);
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
				z=1;
//				Toast.makeText(con, result, Toast.LENGTH_SHORT).show();
			}
			else if(result.compareTo("false")==0)
			{
				Toast.makeText(con, "problem in Updation", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(con, result, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public int getz()
    {
        return z;
    }
    public void setz(int o)
    {
        z=o;
    }

}
