package com.wxnys.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Cache;
import com.wxnys.disklrucache.DiskLruCache;
import com.wxnys.disklrucache.DiskLruCache.Snapshot;
import com.wxnys.util.AppLogEx;

public class ImageDiskCache implements Cache {

	private final static String TAG = "StuDiskCache";

	private Integer mAppVersion = 1;
	private Integer mValueCount = 1; // 一个entry存在多少张图片；
	private long DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024; // 100M
	private File mDirFile;

	private final int DISK_CACHE_INDEX = 0;

	private DiskLruCache mDiskCache;

	public ImageDiskCache() {

	}

	public ImageDiskCache(File dir) {
		mDirFile = dir;
	}

	public long size() {
		long size = 0;
		if (mDiskCache != null) {
			size = mDiskCache.size();
		}
		return size;
	}

	@Override
	public void clear() {
		if (mDiskCache != null) {
			try {
				// mDiskCache.clear();
				mDiskCache.delete();
				initialize();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Entry get(String url) {
		url = getDiskLruKeyFormUrl(url);
		Snapshot snapshot = null;
		if (mDiskCache != null) {
			try {
				snapshot = mDiskCache.get(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (snapshot != null) {
			return inputStreamToEntry(snapshot);
		}
		return null;
	}

	private Entry inputStreamToEntry(Snapshot snapshot) {
		// StuLogEx.enter();
		InputStream in = snapshot.getInputStream(DISK_CACHE_INDEX);
		ByteArrayOutputStream swapStrem = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		byte[] data = null;
		int rc = 0;
		try {
			if (in != null && swapStrem != null) {
				while ((rc = in.read(buffer, 0, 512)) > 0) {
					swapStrem.write(buffer, 0, rc);
				}
				data = swapStrem.toByteArray();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (in != null) {
					in.close();
				}
				if (swapStrem != null) {
					swapStrem.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		if (data != null && data.length > 0) {
			Entry entry = new Entry();
			entry.data = data;
			entry.ttl = Long.MAX_VALUE;
			entry.softTtl = Long.MAX_VALUE;
			AppLogEx.leave();
			return entry;
		}
		return null;
	}

	@Override
	public void initialize() {
		AppLogEx.v(TAG, "DISK_CACHE_MAX_SIZE " + DISK_CACHE_MAX_SIZE);
		try {
			mDiskCache = DiskLruCache.open(mDirFile, mAppVersion, mValueCount,
					DISK_CACHE_MAX_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void invalidate(String url, boolean fullExpire) {

	}

	@Override
	public void put(String url, Entry entry) {
		saveCache(url, entry);
	}

	@Override
	public void remove(String url) {
		url = getDiskLruKeyFormUrl(url);
		// StuLogEx.v(TAG, "url : " + url);
		if (mDiskCache != null) {
			try {
				mDiskCache.remove(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getDiskLruKeyFormUrl(String url) {
		String result = null;
		MessageDigest digester = null;
		try {
			digester = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return getHashCodeForUrl(url);
		}
		if (digester != null) {
			digester.reset();
			byte[] data = url.getBytes();
			digester.update(data);
			byte[] digest = digester.digest();
			result = HexEncode(digest);
			result = result.toLowerCase();
			return result;
		} else {
			return getHashCodeForUrl(url);
		}
	}

	public static String HexEncode(byte[] toencode) {
		StringBuilder sb = new StringBuilder(toencode.length * 2);
		for (byte b : toencode) {
			sb.append(Integer.toHexString((b & 0xf0) >>> 4));
			sb.append(Integer.toHexString(b & 0x0f));
		}
		return sb.toString().toUpperCase();
	}

	private String getHashCodeForUrl(String url) {
		int firstHalfLength = url.length() / 2;
		String localFilename = String.valueOf(url.substring(0, firstHalfLength)
				.hashCode());
		localFilename += String.valueOf(url.substring(firstHalfLength)
				.hashCode());
		return localFilename;
	}

	private void saveCache(String url, Entry entry) {
		url = getDiskLruKeyFormUrl(url);
		AppLogEx.v(TAG, "saveCache url : " + url);
		Snapshot snapshot = null;
		try {
			snapshot = mDiskCache.get(url);
			if (snapshot != null) {
				snapshot.getInputStream(DISK_CACHE_INDEX).close();
			} else {
				final DiskLruCache.Editor editor = mDiskCache.edit(url);
				if (editor != null) {
					OutputStream out = editor.newOutputStream(DISK_CACHE_INDEX);
					out.write(entry.data);
					editor.commit();
					out.close();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class SaveCacheTask extends AsyncTask<Void, Void, Void> {

		private String mUrl;
		private Entry mEntry;

		public SaveCacheTask(String url, Entry entry) {
			mUrl = url;
			mEntry = entry;
		}

		@Override
		protected Void doInBackground(Void... params) {
			saveCache(mUrl, mEntry);
			return null;
		}

	}
}
