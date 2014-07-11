package cn.bmob.naruto.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import cn.bmob.naruto.model.PictureData;
import cn.bmob.naruto.model.PictureItem;
import cn.bmob.naruto.model.PictureType;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class PictureDataManager {

	public final int PICTURE_COUNT_EVERYTIME = 10;
	private static PictureDataManager sInstance = null;

	public synchronized static PictureDataManager getInstance() {
		if (null == sInstance) {
			sInstance = new PictureDataManager();
		}
		return sInstance;
	}

	public interface PictureDataChangeCallback {
		public void onSucess();

		public void onDataChange(PictureType type);

		public void onError(String msg);
	}

	public HashMap<PictureType, PictureData> mAllPictures = new HashMap<PictureType, PictureData>();

	private static ArrayList<PictureDataChangeCallback> mCallbacks = new ArrayList<PictureDataManager.PictureDataChangeCallback>();

	public PictureDataManager() {

	}

	public static void registerPictureDataCallback(
			PictureDataChangeCallback callback) {
		if (!mCallbacks.contains(callback)) {
			mCallbacks.add(callback);
		}
	}

	public static void unregisterPictureDataCallback(
			PictureDataChangeCallback callback) {
		mCallbacks.remove(callback);
	}

	public PictureData getPictureData(Context context, PictureType type) {
		if (mAllPictures.containsKey(type)) {
			return mAllPictures.get(type);
		} else {
			mAllPictures.put(type, new PictureData(type));
			getNextPicture(context, type, null);
			return null;
		}
	}

	public void getPictureData(Context context, PictureType type,
			PictureDataChangeCallback callback) {
		if (mAllPictures.containsKey(type)) {
			if (callback != null) {
				callback.onDataChange(type);
			}
		} else {
			mAllPictures.put(type, new PictureData(type));
			getNextPicture(context, type, callback);
		}
	}

	public void getNextPicture(Context context, final PictureType type,
			final PictureDataChangeCallback callback) {
		BmobQuery<PictureItem> query = new BmobQuery<PictureItem>();
		query.addWhereEqualTo("type", type.getName());
		query.setLimit(PICTURE_COUNT_EVERYTIME);
		query.setSkip(PICTURE_COUNT_EVERYTIME
				* mAllPictures.get(type).getLoadPageNumber());
		query.findObjects(context, new FindListener<PictureItem>() {

			@Override
			public void onSuccess(List<PictureItem> data) {
				mAllPictures.get(type).addPictureItems(data);
				if (data != null) {
					mAllPictures.get(type).setLoadPageNumber(
							mAllPictures.get(type).getLoadPageNumber() + 1);
				}
				if (callback != null) {
					callback.onDataChange(type);
				}
			}

			@Override
			public void onError(int errcode, String msg) {
				if (callback != null) {
					callback.onError(msg);
				}
			}
		});
	}

}
