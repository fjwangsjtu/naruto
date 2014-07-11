package cn.bmob.naruto.picture;

import com.wxnys.webview.WebViewActivity;

import cn.bmob.naruto.R;
import cn.bmob.naruto.picture.AllPictureTypeFragment.onPictureStateChange;
import cn.bmob.naruto.ui.BaseActivity;
import cn.bmob.naruto.util.PictureUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AllPictureShowActivity extends BaseActivity implements onPictureStateChange {

	private FrameLayout mFragmentContainer;

	private int BACK_PRESSED_DELAYT_TIME = 3000;
	private boolean isCanExit = false;

	// private final int MSG_DISMISS_WELCOME = 1;
	private final int MSG_BACK_PRESSED = 2;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			// case MSG_DISMISS_WELCOME:
			// removeWelcomeFragment();
			// break;
			case MSG_BACK_PRESSED:
				isCanExit = false;
				break;
			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_picture_type);

		mFragmentContainer = (FrameLayout) findViewById(R.id.container);
		addPictureFragment();
		// addWelcomeFragment();

	}

	private AllPictureTypeFragment mPicturefragment;

	public void addPictureFragment() {
		mPicturefragment = new AllPictureTypeFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, mPicturefragment, "picture")
				.commitAllowingStateLoss();
	}

	// public void addWelcomeFragment() {
	// WelcomeFragment fragment = new WelcomeFragment();
	// getSupportFragmentManager().beginTransaction()
	// .add(R.id.container, fragment, "welcome")
	// .commitAllowingStateLoss();
	// mHandler.sendEmptyMessageDelayed(MSG_DISMISS_WELCOME, 3000);
	// }
	//
	// public void removeWelcomeFragment() {
	// Fragment fragment = getSupportFragmentManager().findFragmentByTag(
	// "welcome");
	// if (fragment != null) {
	// getSupportFragmentManager().beginTransaction().remove(fragment)
	// .commitAllowingStateLoss();
	// }
	// }

	@Override
	public void onBackPressed() {
		if (mPicturefragment.isShowDetail()) {
			mPicturefragment.showThumbnail();
			return;
		}
		if (isCanExit) {
			super.onBackPressed();
		} else {
			isCanExit = true;
			Toast.makeText(this, "再按一次退出应用.", Toast.LENGTH_LONG).show();
			mHandler.sendEmptyMessageDelayed(MSG_BACK_PRESSED,
					BACK_PRESSED_DELAYT_TIME);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.picture_show, menu);
		// getMenuInflater().inflate(R.menu.main, menu);
		// SearchView searchView = (SearchView)
		// menu.findItem(R.id.action_menu_search)
		// .getActionView();
		// if (searchView == null) {
		// AppLogEx.e("SearchView", "Fail to get Search View.");
		// return true;
		// }
		// searchView.setIconifiedByDefault(true); //
		// 缺省值就是true，可能不专门进行设置，false和true的效果图如下，true的输入框更大
		//
		// // 获取搜索服务管理器
		// SearchManager searchManager = (SearchManager)
		// getSystemService(Context.SEARCH_SERVICE);
		// // searchable activity的component name，由此系统可通过intent进行唤起
		// ComponentName cn = new ComponentName(this,
		// SearchResultActivity.class);
		// // 通过搜索管理器，从searchable
		// //
		// activity中获取相关搜索信息，就是searchable的xml设置。如果返回null，表示该activity不存在，或者不是searchable
		// SearchableInfo info = searchManager.getSearchableInfo(cn);
		// if (info == null) {
		// AppLogEx.e("SearchableInfo", "Fail to get search info.");
		// }
		// // 将searchable activity的搜索信息与search view关联
		// searchView.setSearchableInfo(info);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_settings:
			// SettingUtil.startSettingActivity(this);
			return true;
		case R.id.action_feedback:
			// SettingUtil.startFeedBackActivity(this);
			return true;
		case R.id.action_about:
			// SettingUtil.startAboutUsActivity(this);
			return true;
		case R.id.action_save:
//			 saveFile();
			PictureUtil.savePicture(this, mPicturefragment.getCurrentBitmap());
			return true;
		case R.id.action_share:
			Intent intent = new Intent(this, WebViewActivity.class);
//			intent.putExtra("url", "http://ac.qq.com/naruto/v/cid/613");
			intent.putExtra("url", "http://www.tudou.com/albumplay/Lqfme5hSolM/pr4gzVxRlCM.html");
			startActivity(intent);
			break;
		case R.id.action_walpaper:
			PictureUtil.setWallpager(this, mPicturefragment.getCurrentBitmap());
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onThumbnailShow() {

	}

	@Override
	public void onDetailShow() {

	}

}
