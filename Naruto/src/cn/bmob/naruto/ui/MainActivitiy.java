package cn.bmob.naruto.ui;

import com.viewpagerindicator.TabPageIndicator;

import cn.bmob.naruto.R;
import cn.bmob.naruto.comic.ComicFragment;
import cn.bmob.naruto.discuss.DiscussFragment;
import cn.bmob.naruto.picture.AllPictureTypeFragment;
import cn.bmob.naruto.picture.PictureFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivitiy extends BaseActivity {

	private TabPageIndicator mTabPage;
	private ViewPager mViewPager;
	private MainFragementPagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTabPage = (TabPageIndicator) findViewById(R.id.indicator);
		mViewPager = (ViewPager) findViewById(R.id.content);
		mPagerAdapter = new MainFragementPagerAdapter(
				getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mTabPage.setViewPager(mViewPager);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private class MainFragementPagerAdapter extends FragmentPagerAdapter {

		public MainFragementPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = PictureFragment.newInstance();
//				fragment = new AllPictureTypeFragment();
				break;
			case 1:
				fragment = DiscussFragment.newInstance();
				break;
			case 2:
				fragment = ComicFragment.newInstance();
				break;
			default:
				fragment = ComicFragment.newInstance();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String title = "";
			switch (position) {
			case 0:
				title = "图片";
				break;
			case 1:
				title = "资讯";
				break;
			case 2:
				title = "动漫";
				break;
			default:
				break;
			}
			return title;
		}

	}
}
