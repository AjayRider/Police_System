package com.policesystem;

import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.policesystem.utils.PermissionUtils;
import com.policesystem.webservices.JSONParse;
import com.policesystem.webservices.RestAPI;

public class Login extends AppCompatActivity {

    private static final int RCODE_PERM = 19;
    EditText email, pass;
    Button login, register;
    TableRow tbprog;
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        SharedPreferences sprefLogin = getSharedPreferences("crime", Context.MODE_PRIVATE);
        String user = sprefLogin.getString("user", "");
        if (user != null && !user.isEmpty()) {
            //already loggedin
            Intent regIntent = null;
            if (user.equalsIgnoreCase("user")) {
                regIntent = new Intent(Login.this, TabsActivity.class);
            } else {
                regIntent = new Intent(Login.this, AdminTabs.class);
            }
            startActivity(regIntent);
            finish();
        }
        setContentView(R.layout.login);

        email = (EditText) findViewById(R.id.login_email);
        pass = (EditText) findViewById(R.id.login_pass);
        login = (Button) findViewById(R.id.login_signin);
        register = (Button) findViewById(R.id.login_signup);
        tbprog = (TableRow) findViewById(R.id.tbprog);
        sp = getSharedPreferences("crime", Context.MODE_PRIVATE);

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //permission
                permissionCheck();
                // TODO Auto-generated method stub
            }
        });

        register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RCODE_PERM:
                if (PermissionUtils.permissionGranted(requestCode, RCODE_PERM, grantResults)) {
                   validationAndLogin();
                }
                break;
        }
    }

    private void requestPerm() {
        ActivityCompat.requestPermissions(Login.this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, RCODE_PERM);
    }

    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //dynamic location permission
            if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(Login.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //ask for permission since not granted
                requestPerm();
                Log.d("TAG", "onCreate: shouldShowReq else");
            } else {
                Log.d("TAG", "onCreate> persmissions granted");
                validationAndLogin();
            }
        } else {
            Log.d("TAG", "onCreate> persmissions granted, <M");
            validationAndLogin();
        }
    }

    private void validationAndLogin() {
        if (email.getText().toString().compareTo("") != 0 || pass.getText().toString().compareTo("") != 0) {
            if (email.getText().toString().compareTo("") != 0) {
                if (pass.getText().toString().compareTo("") != 0) {
                    new userlogin().execute(email.getText().toString(), pass.getText().toString());
                    tbprog.setVisibility(View.VISIBLE);
                } else {
                    pass.setError("Enter Password");
                    pass.requestFocus();
                }
            } else {
                email.setError("Enter Email Address");
                email.requestFocus();
            }
        } else {
            email.setError("Cannot Leave Blank");
            pass.setError("Cannot Leave Blank");
            email.requestFocus();
        }
    }

    public class userlogin extends AsyncTask<String, JSONObject, String> {

        @Override
        protected String doInBackground(String... p) {
            // TODO Auto-generated method stub
            String a = "back";
            RestAPI api = new RestAPI();
            try {
                JSONObject json = api.login(p[0], p[1]);
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
            if (result.compareTo("false") == 0) {
                new adminlogin().execute(email.getText().toString(), pass.getText().toString());
            } else if (result.compareTo("Unable to resolve host") == 0) {
                tbprog.setVisibility(View.GONE);
                Toast.makeText(Login.this, "Coudnt connect to the Server", Toast.LENGTH_SHORT).show();
            } else {
                editor = sp.edit();
                editor.putString("email", email.getText().toString());
                editor.commit();
                tbprog.setVisibility(View.GONE);
                Intent i = new Intent(Login.this, TabsActivity.class);
                i.putExtra("name", result);
                startActivity(i);
            }
        }

    }

    public class adminlogin extends AsyncTask<String, JSONObject, String> {

        @Override
        protected String doInBackground(String... p) {
            // TODO Auto-generated method stub
            String a = "back";
            RestAPI api = new RestAPI();
            try {
                JSONObject json = api.alogin(p[0], p[1]);
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
            if (result.compareTo("true") == 0) {
                //ADMIN DASHBOARD
                tbprog.setVisibility(View.GONE);
                Intent i = new Intent(Login.this, AdminTabsActivity.class);
                startActivity(i);
            } else if (result.compareTo("false") == 0) {
                tbprog.setVisibility(View.GONE);
                email.setText("");
                pass.setText("");
                email.requestFocus();
                Toast.makeText(Login.this, "Invalid Credentails", Toast.LENGTH_SHORT).show();
            } else {
                tbprog.setVisibility(View.GONE);
                Toast.makeText(Login.this, result, Toast.LENGTH_SHORT).show();
            }
        }

    }

}
