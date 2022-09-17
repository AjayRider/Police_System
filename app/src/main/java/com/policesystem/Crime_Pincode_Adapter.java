package com.policesystem;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Crime_Pincode_Adapter extends ArrayAdapter<String>{

	String[] id,street,details,img,status;
	Context con;
	public Crime_Pincode_Adapter(Context context, String[] id,String[] street, String[] details, String[] img, String[] status) {
		super(context, R.layout.crime_pincode_listitem,id);
		
		this.id=id;
		this.street=street;
		this.details=details;
		this.img=img;
		this.status=status;
		con=context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater li=LayoutInflater.from(con);
		View v=li.inflate(R.layout.crime_pincode_listitem, null,true);
		
		TextView streett,detailst,statust;
		ImageView pic;
		streett=(TextView) v.findViewById(R.id.crime_pin_street);
		detailst=(TextView) v.findViewById(R.id.crime_pin_details);
		statust=(TextView) v.findViewById(R.id.crime_pin_status);
		pic=(ImageView) v.findViewById(R.id.crime_pin_img);
		
		String image=img[position];
		byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        pic.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
		
		String s1="<b>"+"Street: "+"</b>"+ street[position];
		String s2="<b>"+"Details: "+"</b>"+ details[position];
		String s3="<b>"+"Status: "+"</b>"+"<font color=\"red\">"+status[position]+"</font>";
		
		streett.setText(Html.fromHtml(s1));
		detailst.setText(Html.fromHtml(s2));
		statust.setText(Html.fromHtml(s3));
		
		
		return v;
	}

}
