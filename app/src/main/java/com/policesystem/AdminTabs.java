package com.policesystem;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class AdminTabs extends Fragment{

	ActionBar ab;
	ViewPager vp;
	Admintabspageradapt tabsad;

    public TabLayout tabLayout;
    public ViewPager viewPager;
	
	String tabs[]={"Complaint","Crime","Missing Person"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_tabs, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.container);

        /*for(String tab_name : tabs)
        {
            ab.addTab(ab.newTab().setText(tab_name).setTabListener(this));
        }*/
        viewPager.setAdapter(new Admintabspageradapt(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        return v;
    }
/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_tabs);

		vp=(ViewPager) findViewById(R.id.pager);
		ab=getActionBar();
		tabsad=new Admintabspageradapt(getSupportFragmentManager());
		sp=getSharedPreferences("crime", Context.MODE_PRIVATE);
		editor=sp.edit();
		editor.putString("user", "admin");
		editor.commit();

		vp.setAdapter(tabsad);
		ab.setHomeButtonEnabled(false);
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		for(String tab_name : tabs)
		{
			ab.addTab(ab.newTab().setText(tab_name).setTabListener(this));
		}

		vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				ab.setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		vp.setCurrentItem(tab.getPosition());
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
	*/

		
}
