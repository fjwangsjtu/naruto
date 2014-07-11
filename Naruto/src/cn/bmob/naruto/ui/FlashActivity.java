package cn.bmob.naruto.ui;

import java.util.List;

import com.wxnys.forntia.Statistics;
import com.wxnys.util.AppLogEx;

import cn.bmob.naruto.AppInit;
import cn.bmob.naruto.R;
import cn.bmob.naruto.data.PictureTypeManager;
import cn.bmob.naruto.data.PictureTypeManager.PictureTypeCallback;
import cn.bmob.naruto.model.PictureType;
import cn.bmob.naruto.picture.AllPictureShowActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

public class FlashActivity extends Activity {

	private String TAG = "FlashActivity";
	private final int MSG_DISMISS_WELCOME = 1;
	private final int TIME_WELCOME_SHOW = 3000;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = new Intent(FlashActivity.this, MainActivitiy.class);
			FlashActivity.this.startActivity(intent);
		}

	};

	private PictureTypeCallback mCallback = new PictureTypeCallback() {

		@Override
		public void onSuccess(List<PictureType> object) {
			if (object != null) {
			}
		}

		@Override
		public void onError(int code, String msg) {
			AppLogEx.w(TAG, msg + code);
		};

	};
	private ImageView mIvWelcome;
	private TextView mTvWisdom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppInit.init(this);
		setContentView(R.layout.activity_flash);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Statistics.onResume(this);
		mHandler.sendEmptyMessageDelayed(MSG_DISMISS_WELCOME, TIME_WELCOME_SHOW);

		PictureTypeManager.getInstance(this).fetch(null);
	}

	@Override
	protected void onPause() {
		Statistics.onPause(this);
		super.onPause();
	}

}
