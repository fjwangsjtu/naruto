package cn.bmob.naruto.comic;

import java.util.List;

import org.json.JSONArray;

import cn.bmob.naruto.R;
import cn.bmob.naruto.comic.model.AnimationItem;
import cn.bmob.naruto.model.PictureItem;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ComicFragment extends Fragment {

	public static ComicFragment newInstance() {
		ComicFragment fragment = new ComicFragment();
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
		BmobQuery<AnimationItem> query = new BmobQuery<AnimationItem>();
		query.findObjects(getActivity(), new FindListener<AnimationItem>() {
			
			@Override
			public void onSuccess(List<AnimationItem> arg0) {
				// TODO Auto-generated method stub
				Log.e("TTT", arg0.toString());
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.e("TTT", arg0 + arg1);
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_comic, null);
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
