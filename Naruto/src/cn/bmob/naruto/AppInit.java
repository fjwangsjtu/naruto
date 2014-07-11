package cn.bmob.naruto;

import cn.bmob.tools.MyBmob;

import com.wxnys.forntia.Statistics;

import android.content.Context;

public class AppInit {

	public static void init(final Context context) {
		MyBmob.init(context);
		Statistics.initStatistics(context);
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				AppInfo.getChannel(context);
//			}
//		}).start();
	}
}
