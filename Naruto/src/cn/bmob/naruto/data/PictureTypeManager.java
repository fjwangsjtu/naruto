package cn.bmob.naruto.data;

import java.util.List;

import com.wxnys.util.AppLogEx;

import android.content.Context;
import cn.bmob.naruto.model.PictureType;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class PictureTypeManager {

	private Context mContext;
	private static PictureTypeManager sInstance = null;

	public interface PictureTypeCallback {

		public void onSuccess(List<PictureType> object);

		public void onError(int code, String msg);
	}

	private PictureTypeCallback mCallback;

	public synchronized static PictureTypeManager getInstance(Context context) {
		if (null == sInstance) {
			sInstance = new PictureTypeManager(context);
		}
		return sInstance;
	}

	public List<PictureType> mAllPictureTypes;

	public PictureTypeManager(Context context) {
		mContext = context;
	}

	public synchronized List<PictureType> getAllPictureType() {
		if (null == mAllPictureTypes) {
			getPictureTypeData();
		}
		return mAllPictureTypes;
	}

	public void fetch() {
		if (mAllPictureTypes != null && mAllPictureTypes.size() > 0) {

		} else {
			getPictureTypeData();
		}
	}

	public void fetch(PictureTypeCallback callback) {
		mCallback = callback;
		if (mAllPictureTypes != null && mAllPictureTypes.size() > 0) {
			if (callback != null) {
				callback.onSuccess(mAllPictureTypes);
			}
		} else {
			getPictureTypeData();
		}
	}

	private void getPictureTypeData() {
		AppLogEx.enter();
		BmobQuery<PictureType> bombQuery = new BmobQuery<PictureType>();
		bombQuery.findObjects(mContext, new FindListener<PictureType>() {

			// 获取成功了,但是没有数据,没有数据也是成功;
			@Override
			public void onSuccess(List<PictureType> object) {
				mAllPictureTypes = object;
				if (mCallback != null) {
					mCallback.onSuccess(object);
				}
			}

			@Override
			public void onError(int code, String msg) {
				if (mCallback != null) {
					mCallback.onError(code, msg);
				}
			}
		});

	}

}
