package cn.bmob.naruto.ui;

import cn.bmob.naruto.R;
import cn.bmob.naruto.R.layout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeFragment extends Fragment {

	private ImageView mIvWelcome;
	private TextView mTvWisdom;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_welcome, null);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

}
