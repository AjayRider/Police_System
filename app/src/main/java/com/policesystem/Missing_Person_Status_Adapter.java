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

public class Missing_Person_Status_Adapter extends ArrayAdapter<String> {
	
	Context con;
	String[] id,pname,status,img;
	String[] name,email,age,gender,lastseen,detail;
	int pos;
	String user;
	Dialog da;
	int z=0;

	public Missing_Person_Status_Adapter(Context context, String[] id,String[] pname, String[] img, String[] status,String user) 
	{
		super(context, R.layout.missing_person_status_list_item,id);
		con=context;
		this.id=id;
		this.pname=pname;
		this.status=status;
		this.img=img;
		this.user=user;
		
	}
	
	public Missing_Person_Status_Adapter(Context context,String[] id, String[] pname, String[] age, String[] gender,String[] lastseen, String[] detail, String[] name, String[] email,String[] img, String[] status, String user) {
		super(context, R.layout.missing_person_status_list_item,id);
		con=context;
		this.id=id;
		this.pname=pname;
		this.status=status;
		this.img=img;
		this.user=user;
		this.name=name;
		this.email=email;
		this.age=age;
		this.gender=gender;
		this.lastseen=lastseen;
		this.detail=detail;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater li=LayoutInflater.from(con);
		View v=li.inflate(R.layout.missing_person_status_list_item, null,true);
		
		TextView pnametxt,statustxt;
		ImageView pic;
		pnametxt=(TextView) v.findViewById(R.id.missing_status_name);
		statustxt=(TextView) v.findViewById(R.id.missing_status_status);
		pic=(ImageView) v.findViewById(R.id.missing_status_img);
		Button more=(Button) v.findViewById(R.id.missing_status_btn);
		
		String image=img[position];
		byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        pic.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        
		String s="<b>"+"Person's Name: "+"</b>"+pname[position];
		String s2="<b>"+"Status: "+"</b>"+"<font color=\"red\">"+status[position]+"</font>";
		pnametxt.setText(Html.fromHtml(s));
		statustxt.setText(Html.fromHtml(s2));
		
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				pos=position;
				if(user.compareTo("admin")==0 || user.compareTo("pincode")==0)
				{
					dailog();
				}
				else
				{
					new getmissingbyid().execute(id[position]);
				}
			}
		});
		
		return v;
	}
	
	
	public class getmissingbyid extends AsyncTask<String, JSONObject, String>
	{

		@Override
		protected String doInBackground(String... p) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.MissDet(p[0]);
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
			d.setContentView(R.layout.missing_person_status_dialog);
			
			TextView cid,name,age,gender,lastssen,pdetails,status;
			ImageView img;
			cid=(TextView) d.findViewById(R.id.missingd_id);
			name=(TextView) d.findViewById(R.id.missingd_name);
			age=(TextView) d.findViewById(R.id.missingd_age);
			gender=(TextView) d.findViewById(R.id.missingd_gender);
			lastssen=(TextView) d.findViewById(R.id.missingd_lastseen);
			pdetails=(TextView) d.findViewById(R.id.missingd_pdetails);
			status=(TextView) d.findViewById(R.id.missingd_status);
			img=(ImageView) d.findViewById(R.id.missingd_img);
			
			String image=temp[7];
			byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
	        img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
			
			String s="<b>"+"Id: "+"</b>"+ id[pos];
			String s1="<b>"+"Name: "+"</b>"+ temp[2];
			String s2="<b>"+"Age: "+"</b>"+ temp[3];
			String s3="<b>"+"Gender: "+"</b>"+ temp[4];
			String s4="<b>"+"Last Seen: "+"</b>"+ temp[5];
			String s5="<b>"+"Person's Details: "+"</b>"+ temp[6];
			String s6="<b>"+"Status: "+"</b>"+"<font color=\"red\">"+temp[8]+"</font>";
			
			cid.setText(Html.fromHtml(s));
			name.setText(Html.fromHtml(s1));
			age.setText(Html.fromHtml(s2));
			gender.setText(Html.fromHtml(s3));
			lastssen.setText(Html.fromHtml(s4));
			pdetails.setText(Html.fromHtml(s5));
			status.setText(Html.fromHtml(s6));
			
			d.show();
		}
	}
	
	public void dailog()
	{
		da=new Dialog(con);
		da.requestWindowFeature(Window.FEATURE_NO_TITLE);
		da.setContentView(R.layout.missing_person_status_dialog_admin);
		final TextView idt,pnamet,aget,gendert,lastseent,detailst,namet,emailt,statustxt;
		final Spinner statust;
		Button save,can;
		ImageView pic;
		idt=(TextView) da.findViewById(R.id.missingd_id);
		pnamet=(TextView) da.findViewById(R.id.missingd_pname);
		aget=(TextView) da.findViewById(R.id.missingd_age);
		gendert=(TextView) da.findViewById(R.id.missingd_gender);
		lastseent=(TextView) da.findViewById(R.id.missingd_lastseen);
		detailst=(TextView) da.findViewById(R.id.missingd_pdetails);
		namet=(TextView) da.findViewById(R.id.missingd_name);
		emailt=(TextView) da.findViewById(R.id.missingd_email);
		statust=(Spinner) da.findViewById(R.id.missingd_status);
		save=(Button) da.findViewById(R.id.missingd_save);
		can=(Button) da.findViewById(R.id.missingd_cancel);
		pic=(ImageView) da.findViewById(R.id.missingd_img);
		statustxt=(TextView) da.findViewById(R.id.missingd_statuspin);
		
		TableRow tb1,tb2,tb3;
		tb1=(TableRow) da.findViewById(R.id.tb1);
		tb2=(TableRow) da.findViewById(R.id.tb2);
		tb3=(TableRow) da.findViewById(R.id.tb3);
		
		String image=img[pos];
		byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        pic.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        
        if(user.compareTo("pincode")==0)
        {
        	tb1.setVisibility(View.VISIBLE);
        	tb2.setVisibility(View.GONE);
        	tb3.setVisibility(View.GONE);
        	
        }
		
		String ids="<b>"+"Id: "+"</b>"+ id[pos];
		String pnames="<b>"+"Person's Name: "+"</b>"+ pname[pos];
		String ages="<b>"+"Age: "+"</b>"+ age[pos];
		String genders="<b>"+"Gender"+"</b>"+ gender[pos];
		String lastseens="<b>"+"Last Seen: "+"</b>"+ lastseen[pos];
		String detailss="<b>"+"Details: "+"</b>"+ detail[pos];
		String names="<b>"+"Name: "+"</b>"+ name[pos];
		String emails="<b>"+"Email: "+"</b>"+ email[pos];
		String s6="<b>"+"Status: "+"</b>"+"<font color=\"red\">"+status[pos]+"</font>";
		
		
		idt.setText(Html.fromHtml(ids));
		pnamet.setText(Html.fromHtml(pnames));
		aget.setText(Html.fromHtml(ages));
		gendert.setText(Html.fromHtml(genders));
		lastseent.setText(Html.fromHtml(lastseens));
		detailst.setText(Html.fromHtml(detailss));
		namet.setText(Html.fromHtml(names));
		emailt.setText(Html.fromHtml(emails));
		statustxt.setText(Html.fromHtml(s6));
		
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
				JSONObject json=api.updateMiss(p[0], p[1]);
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
