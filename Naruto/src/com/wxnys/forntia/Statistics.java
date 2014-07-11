package com.wxnys.forntia;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.wxnys.util.AppLogEx;

public class Statistics {

	public static String BAIDU_MTJ_APPKEY = "a9316e2d60";

	private static String CHANNEL_NAME = "baidu";
	private static String CHANNEL_FILE_NAME = "channel";
	private static int sessionTimeOut = 60; // 1-600;

	public static void initStatistics(Context context) {
//		StatService.setAppKey(BAIDU_MTJ_APPKEY);
		InputStream is = null;
		String channelNum = null;
		try {
			is = context.getAssets().open(CHANNEL_FILE_NAME);
			if (null != is) {
				byte[] buffer = new byte[1024];
				int size = is.read(buffer);
				if (size >= 0) {
					channelNum = new String(buffer, 0, size);
					channelNum = channelNum.trim();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (null != channelNum) {
			AppLogEx.d("channel_test", channelNum);
			StatService.setAppChannel(context, channelNum, true);
		}
		StatService.setSessionTimeOut(sessionTimeOut);
		StatService.setOn(context, StatService.EXCEPTION_LOG);
		StatService.setSendLogStrategy(context, SendStrategyEnum.APP_START, 1,
				false);
		// StatService.setDebugOn(DEBUG);
	}

	public static void onResume(Activity activit) {
		StatService.onResume(activit);
	}

	public static void onPause(Activity activit) {
		StatService.onPause(activit);
	}

	public static void onResume(Fragment fragment) {
		StatService.onResume(fragment);
	}

	public static void onPause(Fragment fragment) {
		StatService.onPause(fragment);
	}

	private static void onEvent(Context context, String event_id, String label) {
		StatService.onEvent(context, event_id, label);
	}

	public static void onMoreActionFromClick(Context context, String label) {
		onEvent(context, "MoreActionFrom", label);
	}
}