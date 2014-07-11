package cn.bmob.naruto;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class AppInfo {

	private static String mChannel = null;

	public static String getAppid() {
		return "10000";
	}

	public static String getChannel(Context context) {
		if (null == mChannel) {
			try {
				InputStream input = context.getAssets().open("channel");
				byte[] data = new byte[256];
				int length = input.read(data);
				mChannel = new String(data, 0, length);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mChannel;
	}
}
