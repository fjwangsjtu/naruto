package cn.bmob.naruto.picture;

import java.util.ArrayList;

import com.wxnys.cache.img.MyImageContext;
import com.wxnys.cache.img.MyNetworkImageView;

import cn.bmob.naruto.R;
import cn.bmob.naruto.R.id;
import cn.bmob.naruto.R.layout;
import cn.bmob.naruto.model.PictureItem;
import cn.bmob.naruto.model.PictureType;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;

public class ImageDetailShowAdapter extends PagerAdapter {

	private ArrayList<PictureItem> mPictureData;

	private Context mContext;

	public ImageDetailShowAdapter(Context context, ArrayList<PictureItem> data) {
		mContext = context;
		if (data != null) {
			mPictureData = data;
		} else {
			mPictureData = new ArrayList<PictureItem>();
		}
	}

	@Override
	public int getCount() {
		return mPictureData.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// super.destroyItem(container, position, object);
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// return super.instantiateItem(container, position);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.view_picture_detail_show, null);
		MyNetworkImageView img = (MyNetworkImageView) view
				.findViewById(R.id.img);
		img.setImageUrl(mPictureData.get(position).url, MyImageContext
				.getInstance(mContext).getImageLoader());
		container.addView(view);
		view.setTag(position);
		return view;
	}

}
