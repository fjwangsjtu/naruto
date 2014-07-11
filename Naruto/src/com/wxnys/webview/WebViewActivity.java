package com.wxnys.webview;


import com.wxnys.forntia.Statistics;

import cn.bmob.naruto.R;
import cn.bmob.naruto.ui.BaseActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class WebViewActivity extends BaseActivity implements
		OnClickListener{

	private ImageView mBtnRefresh;
	private ImageView mBtnForward;
	private ImageView mBtnBackward;
	private CustomWebView mWebView;
	private FrameLayout mFLWebviewcontainer;
	private String mUrl;

	private ActionBar mActionBar;
	private boolean mHasWindowFoucs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		mUrl = intent.getStringExtra("url");
		// boolean redirect = intent.getBooleanExtra("redirect", false);
		setContentView(R.layout.activity_webview);

		mActionBar = getSupportActionBar();
		int flag = mActionBar.getDisplayOptions();
		flag |= ActionBar.DISPLAY_SHOW_CUSTOM;
		mActionBar.setDisplayOptions(flag);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mBtnRefresh = (ImageView) findViewById(R.id.refresh);
		mBtnRefresh.setOnClickListener(this);

		mBtnForward = (ImageView) findViewById(R.id.forward);
		mBtnForward.setOnClickListener(this);

		mBtnBackward = (ImageView) findViewById(R.id.backward);
		mBtnBackward.setOnClickListener(this);
		mBtnBackward.setEnabled(false);
		mFLWebviewcontainer = (FrameLayout) findViewById(R.id.webviewcontainer);
		mWebView = new CustomWebView(this);
		mFLWebviewcontainer.addView(mWebView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSaveFormData(false);
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (mWebView.canGoBack()) {
					mBtnBackward.setEnabled(true);
				} else {
					mBtnBackward.setEnabled(false);
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (mWebView.canGoBack()) {
					mBtnBackward.setEnabled(true);
				} else {
					mBtnBackward.setEnabled(false);
				}
			}

		});
		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				mActionBar.setTitle(title);
				if (mWebView.canGoBack()) {
					mBtnBackward.setEnabled(true);
				} else {
					mBtnBackward.setEnabled(false);
				}
			}

		});
		mWebView.loadUrl(mUrl);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Statistics.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Statistics.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWebView != null) {
			mWebView.destroy();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
//		case R.id.action_settings:
//			SettingUtil.startSettingActivity(this);
//			return true;
//		case R.id.action_feedback:
//			SettingUtil.startFeedBackActivity(this);
//			return true;
//		case R.id.action_about:
//			SettingUtil.startAboutUsActivity(this);
//			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backward:
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			}
			if (!mWebView.canGoBack()) {
				mBtnBackward.setEnabled(false);
			}
			break;
		case R.id.forward:
			if (mWebView.canGoForward()) {
				mWebView.goForward();
			}
			break;
		case R.id.refresh:
			mWebView.reload();
			break;
		default:
			break;
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mHasWindowFoucs = hasFocus;
	}
}
