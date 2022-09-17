package com.policesystem.webservices;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONParse {



	public String mainparse(JSONObject json) {
		// TODO Auto-generated method stub
		String a="parse";
		try {
			a=json.getString("Value");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			a=e.getMessage();
		}
		return a;
	}

		public String mparse(JSONObject json) {
			String a="parse";
			try {
				a=json.getString("Value");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				a="parse"+e.getMessage();
			}
			return a;
	}

}
