package com.wxnys.cache;

import android.content.Context;

import com.android.volley.RequestQueue;

public class AppImageVolley {

	public static RequestQueue mRequestQueue;
	
	public static synchronized RequestQueue getImageRequestQueue(Context context){
		if(null == mRequestQueue ){
			mRequestQueue = ImageVolley.newRequestQueue(context);
		}
		return mRequestQueue;
	}
}
