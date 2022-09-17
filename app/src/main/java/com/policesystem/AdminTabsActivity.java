package com.policesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AdminTabsActivity extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tabs);
        sp=getSharedPreferences("crime", Context.MODE_PRIVATE);
        editor=sp.edit();
        editor.putString("user", "admin");
        editor.commit();
        Fragment fragment = new AdminTabs();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_nav, fragment).commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.logout)
        {
            finish();
            editor=sp.edit();
            editor.putString("user", "");
            editor.commit();
        }
        return true;
    }
}
