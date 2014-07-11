package cn.bmob.naruto.picture;

import java.util.ArrayList;
import java.util.List;

import com.viewpagerindicator.TabPageIndicator;
import com.wxnys.cache.img.MyNetworkImageView;
import com.wxnys.forntia.Statistics;
import com.wxnys.util.AppLogEx;

import cn.bmob.naruto.R;
import cn.bmob.naruto.R.id;
import cn.bmob.naruto.R.layout;
import cn.bmob.naruto.data.PictureDataManager;
import cn.bmob.naruto.data.PictureTypeManager;
import cn.bmob.naruto.data.PictureTypeManager.PictureTypeCallback;
import cn.bmob.naruto.model.PictureData;
import cn.bmob.naruto.model.PictureItem;
import cn.bmob.naruto.model.PictureType;
import cn.bmob.naruto.widget.PictureDetailLayout;
import cn.bmob.naruto.widget.PictureShowView;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.StaticLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class AllPictureTypeFragment extends Fragment {

	private boolean isShowDetail = false;;
	private RelativeLayout mRlRoot;
	private PictureDetailLayout mRlDetainContainer;
	private ViewPager mViewPagerThumbnail;
	private ViewPager mViewPagerDetail;
	private ImageDetailShowAdapter mDetailAdapter;
	private TabPageIndicator mTabPageIndicator;

	private ThumbnailPagerAdapter mAdapter;
	private ArrayList<PictureType> mPictureData = new ArrayList<PictureType>();

	private onPictureStateChange mPictureStateCallback;

	public interface onPictureStateChange {
		public void onThumbnailShow();

		public void onDetailShow();
	}

	private PictureTypeCallback mCallback = new PictureTypeCallback() {

		@Override
		public void onSuccess(List<PictureType> object) {
			if (object != null) {
				mPictureData = (ArrayList<PictureType>) object;
				mAdapter.notifyDataSetChanged();
				mTabPageIndicator.notifyDataSetChanged();
			}
		}

		@Override
		public void onError(int code, String msg) {
			Log.e("TTT", msg + code);
		};

	};

	private int mDetailShowPosition = 0;

	protected OnPageChangeListener mDetailPageListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			mDetailShowPosition = position;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	public Bitmap getCurrentBitmap() {
//		View view = mViewPagerDetail.getChildAt(mViewPagerDetail
//				.getCurrentItem());
		View view = mViewPagerDetail.findViewWithTag(mViewPagerDetail
				.getCurrentItem());
		MyNetworkImageView niv = (MyNetworkImageView) view
				.findViewById(R.id.img);
		BitmapDrawable bd = (BitmapDrawable) niv.getDrawable();
		if (bd != null) {
			return bd.getBitmap();
		}
		return null;
	}

	private PictureShowView.onThumbnailImageClickListener mThumbnailImageClickListener = new PictureShowView.onThumbnailImageClickListener() {

		@Override
		public void onThumbnailClick(int left, int top, int width, int heigt,
				PictureType type) {
			PictureData data = PictureDataManager.getInstance().mAllPictures
					.get(type);
			mDetailAdapter = new ImageDetailShowAdapter(getActivity(),
					data.getPictureItems());
			mViewPagerDetail.setAdapter(mDetailAdapter);
			mViewPagerDetail.setCurrentItem(data.getShowPosition());
			mViewPagerDetail.setOnPageChangeListener(mDetailPageListener);
			AppLogEx.w("TTT", data.getShowPosition() + " : getShowPosition");
			mRlDetainContainer.setVisibility(View.VISIBLE);
			ObjectAnimator l = ObjectAnimator.ofInt(mRlDetainContainer,
					"nleft", left, 0);
			ObjectAnimator t = ObjectAnimator.ofInt(mRlDetainContainer, "ntop",
					top + mViewPagerThumbnail.getTop(), 0);
			ObjectAnimator w = ObjectAnimator.ofInt(mRlDetainContainer,
					"nwidth", width, mRlRoot.getWidth());
			ObjectAnimator h = ObjectAnimator.ofInt(mRlDetainContainer,
					"nheight", heigt, mRlRoot.getHeight());

			AnimatorSet set = new AnimatorSet();
			set.playTogether(l, t, w, h);
			set.setDuration(2000);
			set.start();

			isShowDetail = true;
			if (mPictureStateCallback != null) {
				mPictureStateCallback.onDetailShow();
			}
		}
	};

	public AllPictureTypeFragment() {

	}

	@Override
	public void onPause() {
		Statistics.onPause(this);
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		Statistics.onResume(this);
	}

	public boolean isShowDetail() {
		return isShowDetail;
	}

	public void showThumbnail() {
		isShowDetail = false;
		if (mPictureStateCallback != null) {
			mPictureStateCallback.onThumbnailShow();
		}
		mRlDetainContainer.setVisibility(View.GONE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_all_picture, null);
		mRlRoot = (RelativeLayout) view.findViewById(R.id.root);
		mRlDetainContainer = (PictureDetailLayout) view
				.findViewById(R.id.detailcontainer);
		mRlDetainContainer.setVisibility(View.GONE);
		mViewPagerThumbnail = (ViewPager) view.findViewById(R.id.thumbnail);
		mAdapter = new ThumbnailPagerAdapter();
		mViewPagerThumbnail.setAdapter(mAdapter);

		mViewPagerDetail = (ViewPager) view.findViewById(R.id.detail);
		mTabPageIndicator = (TabPageIndicator) view
				.findViewById(R.id.indicator);
		mTabPageIndicator.setViewPager(mViewPagerThumbnail);
		PictureTypeManager.getInstance(getActivity()).fetch(mCallback);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	private class ThumbnailPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPictureData.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mPictureData.get(position).getName();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			PictureShowView view = new PictureShowView(getActivity());
			view.setPictureType(mPictureData.get(position));
			view.setThumbnailImageClickListener(mThumbnailImageClickListener);
			container.addView(view);
			return view;
		}

	}

}
