package com.policesystem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class tabspageradapt extends FragmentPagerAdapter {
	
	
	

	public tabspageradapt(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			return new Complaint();
		case 1:
			return new Crime();
		case 2:
			return new Missing_Person();
			
		case 3:			
			return new Complaint_Status();
		case 4:
			return new Missing_Person_Status();
		case 5:
			return new Crime_Status();
			
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 6;
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
				case 3:
				return "Complaint Status";
			case 4:
				return "Missing person status";
			case 5:
				return "View crime";
		}
		return null;
	}

	
}
