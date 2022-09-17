package com.policesystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.policesystem.utils.MyUtils;
import com.policesystem.utils.PermissionUtils;
import com.policesystem.webservices.JSONParse;
import com.policesystem.webservices.RestAPI;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class Crime extends Fragment {
	
	EditText street,city,zipcode,details;
	Button browse,btn;
	ImageView img;
	String image="",uemail,id;
	SharedPreferences sp;
	private static final int GALLERY_PERMISSIONS_REQUEST = 0;
	private static final int GALLERY_IMAGE_REQUEST = 1;
	public static final int CAMERA_PERMISSIONS_REQUEST = 2;
	public static final int CAMERA_IMAGE_REQUEST = 3;
	private static final int MAX_DIMENSION = 1200;
	private Dialog d;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		
		View v=inflater.inflate(R.layout.crime, container,false);
		street=(EditText) v.findViewById(R.id.crime_street);
		city=(EditText) v.findViewById(R.id.crime_city);
		zipcode=(EditText) v.findViewById(R.id.crime_zipcode);
		details=(EditText) v.findViewById(R.id.crime_details);
		browse=(Button) v.findViewById(R.id.crime_browse);
		btn=(Button) v.findViewById(R.id.crime_btn);
		img=(ImageView) v.findViewById(R.id.crime_img);
		sp=getActivity().getSharedPreferences("crime",Context.MODE_PRIVATE);
		uemail=sp.getString("email", "");
		new crimeid().execute();
		browse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.pic_dialog);
                Button cam, gal;
                cam = (Button) d.findViewById(R.id.camera);
                gal = (Button) d.findViewById(R.id.gallery);

                cam.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.cancel();
                       startCamera();
                    }
                });

                gal.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startGalleryChooser();
                    }
                });
                d.show();
			}
		});
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(street.getText().toString().compareTo("")!=0 || city.getText().toString().compareTo("")!=0 || zipcode.getText().toString().compareTo("")!=0 || details.getText().toString().compareTo("")!=0)
				{
					if(image.compareTo("")!=0)
					{
						if(street.getText().toString().compareTo("")!=0)
						{
							if(city.getText().toString().compareTo("")!=0)
							{
								if(zipcode.getText().toString().compareTo("")!=0)
								{
									if(details.getText().toString().compareTo("")!=0)
									{
										new addcrime().execute(id,uemail,street.getText().toString(),city.getText().toString(),zipcode.getText().toString(),details.getText().toString(),image);
									}
									else
									{
										details.setError("Enter Crime Details");
										details.requestFocus();
									}
								}
								else
								{
									zipcode.setError("Enter Zipcode");
									zipcode.requestFocus();
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
							street.setError("Enter Street");
							street.requestFocus();
						}
					}
					else
					{
						Toast.makeText(getActivity(), "Select a Image from the Crime Scene", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(getActivity(), "All Fields are Mandatory!", Toast.LENGTH_SHORT).show();
				}
	
			}
		
			
		});
		return v;
	}

	public void startCamera() {
		if (PermissionUtils.requestPermission(
				this.getActivity(),
				CAMERA_PERMISSIONS_REQUEST,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.CAMERA)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Uri photoUri = FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".provider", getCameraFile());
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
		}
	}


	public File getCameraFile() {
		File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		return new File(dir, "temp.jpg");
	}

	public void startGalleryChooser() {
		if (PermissionUtils.requestPermission(this.getActivity(), GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Select a photo"),
					GALLERY_IMAGE_REQUEST);
		}
	}

	@Override
	public void onRequestPermissionsResult(
			int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case CAMERA_PERMISSIONS_REQUEST:
				if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
					startCamera();
				}
				break;
			case GALLERY_PERMISSIONS_REQUEST:
				if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
					startGalleryChooser();
				}
				break;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
			if (data.getData() != null) {
				setImgEncoded(data.getData());
			}
		} else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
			Uri photoUri = FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".provider", getCameraFile());
			if (photoUri != null) {
				setImgEncoded(photoUri);
			}
		}
		if (d!= null && d.isShowing()){
			d.cancel();
		}
        /*if (requestCode == RC_CAMERA && resultCode == RESULT_OK &&
				data != null && data.getData() != null) {
        	//Toast.makeText(this, ""+data.getData(), Toast.LENGTH_SHORT).show();
            Uri uri = data.getData();
            try {
                Bitmap bt = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                img.setImageBitmap(bt);
                convertbase64();
            } catch (Exception e) {
            	e.printStackTrace();
            }
        } else if (requestCode == 0 && resultCode ==getActivity().RESULT_OK  && data != null && data.getData() != null)
        {
            Uri uri = data.getData();
            Bitmap bt;	
            try {
            	
                bt = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                img.setImageBitmap(bt);
                convertbase64();
            } catch (Exception e) {
            	
            }
        }*/
	}
	public void setImgEncoded(Uri uri) {
		if (uri != null) {
			try {
				// scale the image to save on bandwidth
				Bitmap bitmap =
						scaleBitmapDown(
								MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri),
								MAX_DIMENSION);
				//resultBitmap = bitmap;
				image = MyUtils.encodeBitmap(bitmap);
				img.setImageBitmap(bitmap);
			} catch (IOException e) {
				Log.d(TAG, "Image picking failed because " + e.getMessage());
				Toast.makeText(getContext(), R.string.image_picker_error, Toast.LENGTH_LONG).show();
			}
		} else {
			Log.d(TAG, "Image picker gave us a null image.");
			Toast.makeText(getContext(), R.string.image_picker_error, Toast.LENGTH_LONG).show();
		}
	}
	private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

		int originalWidth = bitmap.getWidth();
		int originalHeight = bitmap.getHeight();
		int resizedWidth = maxDimension;
		int resizedHeight = maxDimension;

		if (originalHeight > originalWidth) {
			resizedHeight = maxDimension;
			resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
		} else if (originalWidth > originalHeight) {
			resizedWidth = maxDimension;
			resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
		} else if (originalHeight == originalWidth) {
			resizedHeight = maxDimension;
			resizedWidth = maxDimension;
		}
		return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
	}


/*	public void convertbase64()
	{
		BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
		Bitmap bitmap = drawable.getBitmap();
		
		ByteArrayOutputStream stream=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bimg=stream.toByteArray();
		image = Base64.encodeToString(bimg,Base64.DEFAULT);
	}*/
	
	public class crimeid extends AsyncTask<String , JSONObject, String>
	{

		@Override
		protected String doInBackground(String... arg0) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.getCrimeId();
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
	
	public class addcrime extends AsyncTask<String , JSONObject, String>
	{

		@Override
		protected String doInBackground(String... p) {
			String a="back";
			RestAPI api=new RestAPI();
			try {
				JSONObject json=api.AddCrime(p[0], p[1], p[2], p[3], p[4], p[5], p[6]);
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
				street.setText("");
				city.setText("");
				zipcode.setText("");
				details.setText("");
				img.setImageBitmap(null);
				Toast.makeText(getActivity(), "Crime Registered", Toast.LENGTH_SHORT).show();
				
			}
			else
			{
				Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
			}
		}
		
	}
       

}
