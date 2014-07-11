package com.wxnys.cache.img;

import android.graphics.Bitmap;

import com.wxnys.cache.img.MyImageLoader.ImageCache;

public class NoImageCache implements ImageCache {

	@Override
	public Bitmap getBitmap(String url) {
		return null;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {

	}

}
