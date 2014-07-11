package cn.bmob.naruto.ui;

import com.wxnys.forntia.Statistics;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public abstract class BaseActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Statistics.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Statistics.onResume(this);
	}

	
}
