/**
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wxnys.cache.img;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.wxnys.cache.img.MyImageLoader.ImageContainer;
import com.wxnys.cache.img.MyImageLoader.ImageListener;
import com.wxnys.util.AppLogEx;

/**
 * Handles fetching an image from a URL as well as the life-cycle of the
 * associated request.
 */
public class StuNetworkImageViewNeo extends ImageView {
	/** The URL of the network image to load */
	private String mUrl;

	private List<String> mUrls = new ArrayList<String>();
	private boolean mInQueue = false;
	private Bitmap mLastSuccessBitmap = null;

	public static final int PRIORITY_STOP = 0;
	public static final int PRIORITY_DOWNLOAD = 1;

	public int mMode = PRIORITY_DOWNLOAD;

	/**
	 * Resource ID of the image to be used as a placeholder until the network
	 * image is loaded.
	 */
	private int mDefaultImageId;

	/**
	 * Resource ID of the image to be used if the network response fails.
	 */
	private int mErrorImageId;

	/** Local copy of the ImageLoader. */
	private MyImageLoader mImageLoader;

	private OnImageLoaderDone mImageLoaderDoneCallback = null;

	public interface OnImageLoaderDone {
		void onImageLoaderDone();
	}

	/** Current ImageContainer. (either in-flight or finished) */
	private ImageContainer mImageContainer;

	public StuNetworkImageViewNeo(Context context) {
		this(context, null);
	}

	public StuNetworkImageViewNeo(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StuNetworkImageViewNeo(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Sets URL of the image that should be loaded into this view. Note that
	 * calling this will immediately either set the cached image (if available)
	 * or the default image specified by
	 * {@link StuNetworkImageViewNeo#setDefaultImageResId(int)} on the view.
	 * 
	 * NOTE: If applicable,
	 * {@link StuNetworkImageViewNeo#setDefaultImageResId(int)} and
	 * {@link StuNetworkImageViewNeo#setErrorImageResId(int)} should be called
	 * prior to calling this function.
	 * 
	 * @param url
	 *            The URL that should be loaded into this ImageView.
	 * @param imageLoader
	 *            ImageLoader that will be used to make the request.
	 */
	/*
	 * public void setImageUrl(String url, StuImageLoader imageLoader) { mUrl =
	 * url; mImageLoader = imageLoader; // The URL has potentially changed. See
	 * if we need to load it. loadImageIfNecessary(false); }
	 */

	public void setImageUrls(List<String> urls, MyImageLoader imageLoader) {
		this.mUrls = urls;
		this.mUrl = urls.get(0);
		mImageLoader = imageLoader;
		mInQueue = false;
		mLastSuccessBitmap = null;
		loadImageIfNecessary(false);
	}

	/**
	 * Sets the default image resource ID to be used for this view until the
	 * attempt to load it completes.
	 */
	public void setDefaultImageResId(int defaultImage) {
		mDefaultImageId = defaultImage;
	}

	/**
	 * Sets the error image resource ID to be used for this view in the event
	 * that the image requested fails to load.
	 */
	public void setErrorImageResId(int errorImage) {
		mErrorImageId = errorImage;
	}

	public void setOnImageLoaderDone(OnImageLoaderDone callback){
		mImageLoaderDoneCallback = callback;
	}
	/**
	 * Loads the image for the view if it isn't already loaded.
	 * 
	 * @param isInLayoutPass
	 *            True if this was invoked from a layout pass, false otherwise.
	 */
	private void loadImageIfNecessary(final boolean isInLayoutPass) {
		int width = getWidth();
		int height = getHeight();

		boolean isFullyWrapContent = getLayoutParams() != null
				&& getLayoutParams().height == LayoutParams.WRAP_CONTENT
				&& getLayoutParams().width == LayoutParams.WRAP_CONTENT;
		// if the view's bounds aren't known yet, and this is not a
		// wrap-content/wrap-content
		// view, hold off on loading the image.
		if (width == 0 && height == 0 && !isFullyWrapContent) {
			return;
		}

		// if the URL to be loaded in this view is empty, cancel any old
		// requests and clear the
		// currently loaded image.

		if (TextUtils.isEmpty(mUrl)) {
			if (mImageContainer != null) {
				mImageContainer.cancelRequest();
				mImageContainer = null;
			}
			setImageBitmap(null);
			return;
		}

		// if there was an old request in this view, check if it needs to be
		// canceled.
		if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
			if (mImageContainer.getRequestUrl().equals(mUrl)) {
				// if the request is from the same URL, return.
				return;
			} else {
				// if there is a pre-existing request, cancel it if it's
				// fetching a different URL.
				mImageContainer.cancelRequest();
				if (!mInQueue)
					setImageBitmap(null);
			}
		}

		// The pre-existing content of this view didn't match the current URL.
		// Load the new image
		// from the network.
		ImageContainer newContainer = mImageLoader.get(mUrl,
				new ImageListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (mMode == StuNetworkImageViewNeo.PRIORITY_DOWNLOAD)
							checkNext();
						if (mMode == StuNetworkImageViewNeo.PRIORITY_STOP
								&& mLastSuccessBitmap == null)
							checkNext();
						AppLogEx.d("foo",
								"get Error Response "
										+ error.getLocalizedMessage());
						return;

						/*
						 * if (mErrorImageId != 0) {
						 * 
						 * //if (!mInQueue) // setImageResource(mErrorImageId);
						 * }
						 */
					}

					@Override
					public void onResponse(final ImageContainer response,
							boolean isImmediate) {
						// If this was an immediate response that was delivered
						// inside of a layout
						// pass do not set the image immediately as it will
						// trigger a requestLayout
						// inside of a layout. Instead, defer setting the image
						// by posting back to
						// the main thread.
						if (isImmediate && isInLayoutPass) {
							post(new Runnable() {
								@Override
								public void run() {
									onResponse(response, false);
								}
							});
							return;
						}

						if (response.getBitmap() != null) {
							// StuLogEx.d("foo",
							// "call foo,set Bitmap with url: "+mUrl);

							// StuLogEx.d("foo", "onResponse,bitmap not null "+mUrl);

							setImageBitmap(response.getBitmap());
							if (mImageLoaderDoneCallback != null) {
								mImageLoaderDoneCallback.onImageLoaderDone();
							}
							mLastSuccessBitmap = response.getBitmap();
							// fixme:resend here
							if (mMode == StuNetworkImageViewNeo.PRIORITY_DOWNLOAD) {
								// StuLogEx.d("foo",
								// "in Download Priority,call checkNext");
								checkNext();
							}
						} else {
							if (!isImmediate
									&& mLastSuccessBitmap == null
									&& mMode == StuNetworkImageViewNeo.PRIORITY_STOP) {

								// StuLogEx.d("foo",
								// "in Stop Priority,call checkNext");
								// checkNext ();
							}

							if (mDefaultImageId != 0) {
								if (!mInQueue)
									setImageResource(mDefaultImageId);
							}
						}
					}

					public void checkNext() {
						// location
						int currentLocation = -1;
						for (int i = 0; i < mUrls.size(); i++) {
							if (mUrl.equals(mUrls.get(i))) {
								currentLocation = i;
								break;
							}
						}

						if (currentLocation >= 0
								&& currentLocation < mUrls.size() - 1) {
							mUrl = mUrls.get(currentLocation + 1);
							AppLogEx.d("foo", "re-fetch url " + mUrl);
							mInQueue = true;
							post(new Runnable() {
								@Override
								public void run() {

									loadImageIfNecessary(false);
								}
							});

						}

					}

				}, 1000, 1000);

		// update the ImageContainer to be the new bitmap container.
		mImageContainer = newContainer;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		loadImageIfNecessary(true);
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mImageContainer != null) {
			// If the view was bound to an image request, cancel it and clear
			// out the image from the view.
			mImageContainer.cancelRequest();
			setImageBitmap(null);
			// also clear out the container so we can reload the image if
			// necessary.
			mImageContainer = null;
		}
		super.onDetachedFromWindow();
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}
}
