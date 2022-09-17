package com.policesystem;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

import com.policesystem.webservices.JSONParse;
import com.policesystem.webservices.RestAPI;

public class Missing_Pincode extends Activity {


    ListView list;
    TableRow prog;
    String[] id, pname, status, img;
    String[] name, email, age, gender, lastseen, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing_pincode);

        list = (ListView) findViewById(R.id.missing_pincode_list);
        prog = (TableRow) findViewById(R.id.tbprog);

        new getmissadmin().execute();


    }


    public class getmissadmin extends AsyncTask<String, JSONObject, String> {

        @Override
        protected String doInBackground(String... p) {
            String a = "back";
            RestAPI api = new RestAPI();
            try {
                JSONObject json = api.viewMiss();
                JSONParse jp = new JSONParse();
                a = jp.mparse(json);
            } catch (Exception e) {
                a = "back" + e.getMessage();
            }
            return a;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.i("reply", "onPostExecute: " + result);
            if (result.compareTo("false") != 0) {
                result = result.substring(5, result.length());
                String temp[] = result.split("\\#");
                id = new String[temp.length];
                pname = new String[temp.length];
                age = new String[temp.length];
                gender = new String[temp.length];
                lastseen = new String[temp.length];
                detail = new String[temp.length];
                name = new String[temp.length];
                email = new String[temp.length];
                img = new String[temp.length];
                status = new String[temp.length];
                for (int i = 0; i < temp.length; i++) {
                    String temp1[] = temp[i].split("\\*");
                    if (temp1[0] != null)
                        id[i] = temp1[0];
                    if (temp1[1] != null)
                        email[i] = temp1[1];
                    if (temp1[2] != null)
                        name[i] = temp1[2];
                    if (temp1[3] != null)
                        pname[i] = temp1[3];
                    if (temp1[4] != null)
                        age[i] = temp1[4];
                    if (temp1[5] != null)
                        gender[i] = temp1[5];
                    if (temp1[6] != null)
                        lastseen[i] = temp1[6];
                    if (temp1[7] != null)
                        detail[i] = temp1[7];
                    if (temp1[8] != null)
                        img[i] = temp1[8];
                    if (temp1[9] != null)
                        status[i] = temp1[9];
                }

                Missing_Person_Status_Adapter mpsa = new Missing_Person_Status_Adapter(Missing_Pincode.this, id, pname, age, gender, lastseen, detail, name, email, img, status, "pincode");
                list.setAdapter(mpsa);
                prog.setVisibility(View.GONE);

            } else {
                Toast.makeText(Missing_Pincode.this, "NO Missing Person to Show", Toast.LENGTH_SHORT).show();
            }

        }


    }

}
