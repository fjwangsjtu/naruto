package cn.bmob.naruto.discuss;

import cn.bmob.naruto.R;

import com.baidu.mobstat.ar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DiscussFragment extends Fragment {
	
	public static DiscussFragment newInstance(){
		DiscussFragment fragment = new DiscussFragment();
		Bundle args = fragment.getArguments();
		if(null == args){
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
		View view = inflater.inflate(R.layout.fragment_discuss, null);
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
