package com.wxnys.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.wxnys.cache.img.NoImageCache;
import com.wxnys.cache.img.MyImageLoader;

public class ImageVolley extends Volley {

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it.
	 * 
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context) {
		return newStuRequestQueue(context, null);
	}

	public static RequestQueue newStuRequestQueue(Context context,
			HttpStack stack) {

		// 1. init Cache
		// File cacheDir = new File(dir, y);
		ImageCache cache = ImageCache.getInstance(context);
		// 2. create HttpStack
		String userAgent = "volley/0";
		try {
			String packageName = context.getPackageName();
			PackageInfo info = context.getPackageManager().getPackageInfo(
					packageName, 0);
			userAgent = packageName + "/" + info.versionCode;
		} catch (NameNotFoundException e) {
		}

		if (stack == null) {
			if (Build.VERSION.SDK_INT >= 9) {
				stack = new HurlStack();
			} else {
				// Prior to Gingerbread, HttpUrlConnection was unreliable.
				// See:
				// http://android-developers.blogspot.com/2011/09/androids-http-clients.html
				stack = new HttpClientStack(
						AndroidHttpClient.newInstance(userAgent));
			}
		}

		Network network = new BasicNetwork(stack);

		// 3. create RequestQueue
		RequestQueue queue = new RequestQueue(cache, network);
		queue.start();

		return queue;
	}
	
	public static void clear(Context context){
		ImageCache cache = ImageCache.getInstance(context);
		cache.clear();
	}
	
	public static void ImageGet(Context context){
		RequestQueue queue = newRequestQueue(context);
		MyImageLoader imageLoad = new MyImageLoader(queue);
		
		
	}
}
