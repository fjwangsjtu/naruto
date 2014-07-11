package com.wxnys.cache.img;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.wxnys.cache.ImageVolley;

public class MyImageContext {

	private RequestQueue mQueue;

	private MyImageLoader mImageLoader;

	private static MyImageContext mInstance = null;

	private MyImageContext(Context context) {
		mQueue = ImageVolley.newRequestQueue(context);

		mImageLoader = new MyImageLoader(mQueue);
	}

	public static synchronized MyImageContext getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MyImageContext(context);
		}
		return mInstance;
	}
	
	public MyImageLoader getImageLoader(){
		return mImageLoader;
	}
}
