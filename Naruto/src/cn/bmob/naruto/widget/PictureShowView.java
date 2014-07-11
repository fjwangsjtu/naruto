package cn.bmob.naruto.widget;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.wxnys.cache.AppImageVolley;
import com.wxnys.cache.ImageVolley;
import com.wxnys.cache.img.MyImageContext;
import com.wxnys.cache.img.MyNetworkImageView;
import com.wxnys.util.AppLogEx;

import cn.bmob.naruto.R;
import cn.bmob.naruto.data.PictureDataManager;
import cn.bmob.naruto.data.PictureDataManager.PictureDataChangeCallback;
import cn.bmob.naruto.model.PictureData;
import cn.bmob.naruto.model.PictureItem;
import cn.bmob.naruto.model.PictureType;
import cn.bmob.naruto.util.BitmapCache;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class PictureShowView extends RelativeLayout implements OnScrollListener {

	private PictureType mType;

	private PictureData mPictureData;
	private ArrayList<PictureItem> mPicItems = new ArrayList<PictureItem>();

	private GridAdapter mAdatper;
	private GridView mGridView;

	private RequestQueue mRequestQueue;

	public interface onThumbnailImageClickListener {
		public void onThumbnailClick(int left, int top, int width, int heigt,
				PictureType type);
	}

	private onThumbnailImageClickListener mThumbnailItemClickListener;
	private PictureDataChangeCallback mCallback = new PictureDataChangeCallback() {

		@Override
		public void onSucess() {

		}

		@Override
		public void onError(String msg) {

		}

		@Override
		public void onDataChange(PictureType type) {
			if (type.getName().equals(mType.getName())) {
				mPictureData = PictureDataManager.getInstance().getPictureData(
						getContext(), type);
				if (mPictureData != null) {
					mPicItems = mPictureData.getPictureItems();
					mAdatper.notifyDataSetChanged();
				}

			}
		}
	};

	public PictureShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public PictureShowView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public PictureShowView(Context context) {
		this(context, null);
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			AppLogEx.e(VIEW_LOG_TAG, PictureShowView.this.getLeft()
					+ " left : top " + PictureShowView.this.getTop());
			AppLogEx.e(VIEW_LOG_TAG,
					view.getLeft() + " left : top " + view.getTop());
			mPictureData.setShowPosition(position);
			mThumbnailItemClickListener.onThumbnailClick(view.getLeft(),
					view.getTop(), view.getWidth(), view.getHeight(), mType);
		}
	};

	public void setThumbnailImageClickListener(
			onThumbnailImageClickListener listener) {
		mThumbnailItemClickListener = listener;
	}

	private void init(Context context) {
		mRequestQueue = AppImageVolley.getImageRequestQueue(getContext());
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_picture_show, this);
		mGridView = (GridView) view.findViewById(R.id.gridview);
		mAdatper = new GridAdapter();
		mGridView.setAdapter(mAdatper);
		mGridView.setOnItemClickListener(mItemClickListener);
		mGridView.setOnScrollListener(this);
	}

	@Override
	protected void onAttachedToWindow() {
		AppLogEx.enter();
		AppLogEx.w(VIEW_LOG_TAG, mType + "");
		PictureDataManager.unregisterPictureDataCallback(mCallback);
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		AppLogEx.enter();
		AppLogEx.w(VIEW_LOG_TAG, mType + "");
		super.onDetachedFromWindow();
	}

	public void setPictureType(PictureType type) {
		mType = type;
		PictureDataManager.getInstance().getPictureData(getContext(), type,
				mCallback);
	}

	private class GridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mPicItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mPicItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView != null) {
				view = convertView;
			} else {
				view = LayoutInflater.from(getContext()).inflate(
						R.layout.view_picture_show_item, null);
			}
			MyNetworkImageView img = (MyNetworkImageView) view
					.findViewById(R.id.img);
			img.setImageUrl(mPicItems.get(position).url, MyImageContext
					.getInstance(getContext()).getImageLoader());
			return view;
		}

	}

	public void getAllPictureItem() {
		BmobQuery<PictureItem> query = new BmobQuery<PictureItem>();
		query.addWhereEqualTo("type", mType.getName());
		query.findObjects(getContext(), new FindListener<PictureItem>() {

			@Override
			public void onSuccess(List<PictureItem> data) {
				if (data != null) {
					mPicItems = (ArrayList<PictureItem>) data;
					mAdatper.notifyDataSetChanged();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (totalItemCount > 0
				&& (firstVisibleItem + visibleItemCount) >= totalItemCount) {
			PictureDataManager.getInstance().getNextPicture(getContext(),
					mType, mCallback);
		}
	}
}
