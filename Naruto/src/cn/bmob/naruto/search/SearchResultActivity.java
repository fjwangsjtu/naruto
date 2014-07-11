package cn.bmob.naruto.search;

import cn.bmob.naruto.ui.BaseActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SearchResultActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setText("Dadsfasdfasdfasdf");
		setContentView(tv);
	}

	
}
