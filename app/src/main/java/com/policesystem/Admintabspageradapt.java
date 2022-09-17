package com.policesystem;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Admintabspageradapt extends FragmentPagerAdapter {

	public Admintabspageradapt(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			return new Complaint_Status();
		case 1:
			return new Crime_Status();
		case 2:
			return new Missing_Person_Status();
			
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	@Override
	public CharSequence getPageTitle(int position) {

		switch (position) {
			case 0:
				return "Complaint";
			case 1:
				return "Crime";
			case 2:
				return "Missing Person";
		}
		return null;
	}
	
}
