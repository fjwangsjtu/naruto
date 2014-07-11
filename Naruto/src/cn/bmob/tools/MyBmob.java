package cn.bmob.tools;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import android.content.Context;

public class MyBmob {

	public static String AppID = "1450503f5533cc87c18fecc2cc57bd42";

	public static void init(Context context) {
		Bmob.initialize(context, AppID);
		BmobInstallation.getCurrentInstallation(context).save();
		BmobPush.startWork(context, AppID);
		

	}
}
