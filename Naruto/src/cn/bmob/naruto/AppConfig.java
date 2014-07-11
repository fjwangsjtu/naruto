package cn.bmob.naruto;

import android.os.Environment;

public class AppConfig {

	public final static boolean DEBUG = true;// Log.isLoggable("stu",
												// StuLogEx.dEBUG);

	public final static String SDCARD_FILEFOLDER_DOWNLOAD = Environment
			.getExternalStorageDirectory() + "/naruto/";
}
