package cn.bmob.naruto.push;

import android.content.Context;
import android.util.Log;
import cn.bmob.push.BmobPushMessageReceiver;


public class NarutoBmobPushMessageReceiver extends BmobPushMessageReceiver {

	@Override
	public void onMessage(Context ctx, String message) {
		Log.e("TTT", message + " :message");
	}

}
