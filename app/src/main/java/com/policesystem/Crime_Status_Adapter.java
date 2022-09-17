package com.policesystem;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import com.policesystem.webservices.JSONParse;
import com.policesystem.webservices.RestAPI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Crime_Status_Adapter extends ArrayAdapter<String> {

	Context con;
	String[] id,details,status;
	String[] name,street,city,zip,phone,email,pic;
	int pos;
	String user;
	Dialog da;
	int z=0;

	public Crime_Status_Adapter(Context context, String[] id,String[] details, String[] status,String user) 
	{
		super(context, R.layout.crime_status_list_item,id);
		con=context;
		this.id=id;
		this.details=details;
		this.status=status;
		this.user=user;
		
	}
	
	public Crime_Status_Adapter(Context context, String[] id,String[] details, String[] status, String[] name,String[] street, String[] city, String[] zip, String[] phone,String[] email, String[] pic, String user) {
		super(context, R.layout.crime_status_list_item,id);
		con=context;
		this.id=id;
		this.details=details;
		this.status=status;
		this.name=name;
		this.street=street;
		this.city=city;
		this.zip=zip;
		this.phone=phone;
		this.email=email;
		this.pic=pic;
		this.user=user;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater li=LayoutInflater.from(con);
		View v=li.inflate(R.layout.crime_status_list_item, null,true);
		
		TextView detailstxt,statustxt;
		detailstxt=(TextView) v.findViewById(R.id.crime_status_details);
		statustxt=(TextView) v.findViewById(R.id.crime_status_status);
		Button more=(Button) v.findViewById(R.id.crime_status_btn);
		String s="<b>"+"Crime: "+"</b>"+details[position];
		String s2="<b>"+"Status: "+"</b>"+"<font color=\"red\">"+status[position]+"</font>";
		detailstxt.setText(Html.fromHtml(s));
		statustxt.setText(Html.fromHtml(s2));
		
		
		TextView nametxt,citytxt;
		nametxt=(TextView) v.findViewById(R.id.crime_status_name);
		citytxt=(TextView) v.findViewById(R.id.crime_status_city);
		TableRow nametb,citytb;
		nametb=(TableRow) v.findViewById(R.id.crime_status_nametb);
		citytb=(TableRow) v.findViewById(R.id.crime_status_citytb);
		
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
					new getcrimebyid().execute(id[position]);
				}
				
				
			}
		});
		
		return v;
	}
	
	public class getcrimebyid extends AsyncTask<String, JSONObject, String>
	{

		@Override
		protected String doInBackground(String... p) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.CrimeDet(p[0]);
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
			//Toast.makeText(con, result, Toast.LENGTH_SHORT).show();
			String temp[]=result.split("\\*");
			
			Dialog d=new Dialog(con);
			d.requestWindowFeature(Window.FEATURE_NO_TITLE);
			d.setContentView(R.layout.crime_status_dialog);
			
			TextView cid,street,city,cdetails,status;
			ImageView img;
			cid=(TextView) d.findViewById(R.id.crimed_id);
			street=(TextView) d.findViewById(R.id.crimed_street);
			city=(TextView) d.findViewById(R.id.crimed_city);
			cdetails=(TextView) d.findViewById(R.id.crimed_details);
			status=(TextView) d.findViewById(R.id.crimed_status);
			img=(ImageView) d.findViewById(R.id.crimed_img);
			
			String image=temp[4];
			byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
	        img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
			
			String s="<b>"+"Id: "+"</b>"+ id[pos];
			String s1="<b>"+"Crime: "+"</b>"+ temp[3];
			String s3="<b>"+"Street: "+"</b>"+ temp[1];
			String s4="<b>"+"City: "+"</b>"+ temp[2];
			String s6="<b>"+"Status: "+"</b>"+"<font color=\"red\">"+temp[5]+"</font>";
			
			cid.setText(Html.fromHtml(s));
			cdetails.setText(Html.fromHtml(s1));
			street.setText(Html.fromHtml(s3));
			city.setText(Html.fromHtml(s4));
			status.setText(Html.fromHtml(s6));
			
			d.show();
		}
	}
	
	public void dailog()
	{
		da=new Dialog(con);
		da.requestWindowFeature(Window.FEATURE_NO_TITLE);
		da.setContentView(R.layout.crime_status_dialog_admin);
		final TextView idt,deatilst,streett,cityt,zipt,namet,phnt,emailt;
		final Spinner statust;
		Button save,can;
		ImageView img;
		idt=(TextView) da.findViewById(R.id.crimed_id);
		deatilst=(TextView) da.findViewById(R.id.crimed_details);
		streett=(TextView) da.findViewById(R.id.crimed_street);
		cityt=(TextView) da.findViewById(R.id.crimed_city);
		zipt=(TextView) da.findViewById(R.id.crimed_zip);
		namet=(TextView) da.findViewById(R.id.crimed_name);
		phnt=(TextView) da.findViewById(R.id.crimed_phone);
		emailt=(TextView) da.findViewById(R.id.crimed_email);
		statust=(Spinner) da.findViewById(R.id.crimed_status);
		save=(Button) da.findViewById(R.id.crimed_save);
		can=(Button) da.findViewById(R.id.crimed_cancel);
		img=(ImageView) da.findViewById(R.id.crimed_img);
		
		String image=pic[pos];
		byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
		
		String ids="<b>"+"Id: "+"</b>"+ id[pos];
		String deatilss="<b>"+"Deatils: "+"</b>"+ details[pos];
		String streets="<b>"+"Street: "+"</b>"+ street[pos];
		String citys="<b>"+"City"+"</b>"+ city[pos];
		String zips="<b>"+"Pincode: "+"</b>"+ zip[pos];
		String names="<b>"+"Name: "+"</b>"+ name[pos];
		String phns="<b>"+"Phone: "+"</b>"+ phone[pos];
		String emails="<b>"+"Email: "+"</b>"+ email[pos];
		
		
		idt.setText(Html.fromHtml(ids));
		deatilst.setText(Html.fromHtml(deatilss));
		streett.setText(Html.fromHtml(streets));
		cityt.setText(Html.fromHtml(citys));
		zipt.setText(Html.fromHtml(zips));
		namet.setText(Html.fromHtml(names));
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
				JSONObject json=api.updateCrime(p[0], p[1]);
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
