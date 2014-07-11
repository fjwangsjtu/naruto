package com.wxnys.cache;

import java.io.File;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.wxnys.util.AppLogEx;

public class ImageCache implements Cache {

	private final static String TAG = "StuCache";
	private String CACHE_DIR_NAME = "/cache";;
	private ImageDiskCache mDiskCache;
	private String mDiskFileDir;

	private int mMemMaxSize = 0; // LruCache Kb

	private LruCache<String, Cache.Entry> mMemCache;

	private static ImageCache mInstance = null;

	public static synchronized ImageCache getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new ImageCache(context);
		}
		return mInstance;
	}

	private ImageCache(Context context) {
		initDiskCache(context);
		initMemCache(context);
	}

	private void initMemCache(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		mMemMaxSize = am.getMemoryClass() / 8 * 1024 * 1024;
		mMemCache = new LruCache<String, Cache.Entry>(mMemMaxSize) {
			@Override
			protected int sizeOf(String key, Entry value) {
				return value.data.length;
			}
		};

	}

	private void initDiskCache(Context context) {
		String sdcardState = Environment.getExternalStorageState();
		if (sdcardState.equals(Environment.MEDIA_MOUNTED)) {
			mDiskFileDir = Environment.getExternalStorageDirectory()
					+ "/百度识图"  + CACHE_DIR_NAME;
		} else {
			mDiskFileDir = context.getCacheDir() + CACHE_DIR_NAME;
		}
		AppLogEx.d(TAG, "mDiskFileDir : " + mDiskFileDir);
		File diskFile = new File(mDiskFileDir);
		if (diskFile != null && !diskFile.exists()) {
			diskFile.mkdirs();
		}
		mDiskCache = new ImageDiskCache(diskFile);
	}

	/**
	 * 
	 * @return the cache size of DiskCache and dataPartitionCache
	 */
	public long getCacheSize() {
		long size = 0;
		if (mDiskCache != null) {
			size = (long) (mDiskCache.size() / 1024 / (float)1024  + 0.5);
		}
		AppLogEx.d(TAG, "mem size: " + mMemCache.size());
		AppLogEx.d(TAG, "disk size: " + mDiskCache.size());
		return size;
	}

	@Override
	public void clear() {
		mDiskCache.clear();
	}

	@Override
	public Entry get(String url) {
		Entry entry = null;
		if (mMemCache != null) {
			entry = mMemCache.get(url);
		}
		if (entry != null) {
			return entry;
		}

		if (mDiskCache != null) {
			entry = mDiskCache.get(url);
		}
		if (entry != null) {
			mMemCache.put(url, entry);
		}
		return entry;
	}

	@Override
	public void initialize() {
		if (mDiskCache != null) {
			mDiskCache.initialize();
		}

	}

	@Override
	public void invalidate(String url, boolean fullExpire) {
		// TODO Auto-generated method stub

	}

	@Override
	public void put(String url, Entry entry) {
		if (mDiskCache != null) {
			mDiskCache.put(url, entry);
		}
		if (mMemCache != null) {
			mMemCache.put(url, entry);
		}

	}

	@Override
	public void remove(String url) {
		if (mDiskCache != null) {
			mDiskCache.remove(url);
		}

		if (mMemCache != null) {
			mMemCache.remove(url);
		}
	}

}
