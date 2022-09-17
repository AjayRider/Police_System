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
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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

public class Missing_Person extends Fragment {

    EditText name, age, lastseen, details;
    Button browse, btn;
    ImageView img;
    RadioGroup rg;
    String id, uemail, image = "", gender;
    RadioButton male, female;
    SharedPreferences sp;
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    private static final int MAX_DIMENSION = 1200;
    private Dialog d;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.missing_person, container, false);
        name = (EditText) v.findViewById(R.id.missing_name);
        age = (EditText) v.findViewById(R.id.missing_age);
        lastseen = (EditText) v.findViewById(R.id.missing_lastseen);
        details = (EditText) v.findViewById(R.id.missing_details);
        browse = (Button) v.findViewById(R.id.missing_browse);
        btn = (Button) v.findViewById(R.id.missing_btn);
        img = (ImageView) v.findViewById(R.id.missing_img);
        rg = (RadioGroup) v.findViewById(R.id.missing_rg);
        male = (RadioButton) v.findViewById(R.id.missing_male);
        female = (RadioButton) v.findViewById(R.id.missing_female);
        sp = getActivity().getSharedPreferences("crime", Context.MODE_PRIVATE);
        uemail = sp.getString("email", "");
        new missingid().execute();

        browse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Dialog d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.pic_dialog);
                Button cam, gal;
                cam = (Button) d.findViewById(R.id.camera);
                gal = (Button) d.findViewById(R.id.gallery);

                cam.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
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


        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                if (id == R.id.missing_male) {
                    gender = "Male";
                } else if (id == R.id.missing_female) {
                    gender = "Female";
                }

            }
        });

        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (name.getText().toString().compareTo("") != 0 || age.getText().toString().compareTo("") != 0 || lastseen.getText().toString().compareTo("") != 0 || details.getText().toString().compareTo("") != 0) {
                    if (image.compareTo("") != 0) {
                        if (gender.compareTo("") != 0) {
                            if (name.getText().toString().compareTo("") != 0) {
                                if (age.getText().toString().compareTo("") != 0) {
                                    if (lastseen.getText().toString().compareTo("") != 0) {
                                        if (details.getText().toString().compareTo("") != 0) {
                                            new addMissing().execute(id, uemail, name.getText().toString(), age.getText().toString(), gender, lastseen.getText().toString(), details.getText().toString(), image);
                                        } else {
                                            details.setError("Enter Missing or the Person's Details");
                                            details.requestFocus();
                                        }
                                    } else {
                                        lastseen.setError("Enter LastSeen");
                                        lastseen.requestFocus();
                                    }
                                } else {
                                    age.setError("Enter Person's Age");
                                    age.requestFocus();
                                }
                            } else {
                                name.setError("Enter Person's Name");
                                name.requestFocus();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Choose a Persons Gender", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Select a Picture of the Missing Person", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "All Fields are Mandatory", Toast.LENGTH_SHORT).show();
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
        if (d != null && d.isShowing()) {
            d.cancel();
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
        if (d != null && d.isShowing()) {
            d.cancel();
        }
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

    public class missingid extends AsyncTask<String, JSONObject, String> {

        @Override
        protected String doInBackground(String... arg0) {
            String a = "back";
            RestAPI api = new RestAPI();
            try {
                JSONObject json = api.getMissId();
                JSONParse jp = new JSONParse();
                a = jp.mainparse(json);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                a = e.getMessage();
            }
            return a;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result.compareTo("") == 0) {
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            } else {
                id = result;
                //Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public class addMissing extends AsyncTask<String, JSONObject, String> {

        @Override
        protected String doInBackground(String... p) {
            String a = "back";
            RestAPI api = new RestAPI();
            try {
                JSONObject json = api.AddMiss(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7]);
                JSONParse jp = new JSONParse();
                a = jp.mainparse(json);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                a = e.getMessage();
            }
            return a;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result.compareTo("") != 0) {
                name.setText("");
                age.setText("");
                details.setText("");
                lastseen.setText("");
                male.setChecked(false);
                female.setChecked(false);
                img.setImageBitmap(null);
                Toast.makeText(getActivity(), "Missing Complaint Registered", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            }
        }

    }

}
