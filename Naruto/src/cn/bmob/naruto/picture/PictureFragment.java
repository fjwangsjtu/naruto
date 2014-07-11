package cn.bmob.naruto.picture;

import cn.bmob.naruto.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PictureFragment extends Fragment {

	public static PictureFragment newInstance() {
		PictureFragment fragment = new PictureFragment();
		Bundle args = fragment.getArguments();
		if (null == args) {
			args = new Bundle();
		}
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_picture, null);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
	}
}
