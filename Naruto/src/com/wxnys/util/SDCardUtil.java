package com.wxnys.util;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class SDCardUtil {

	public static boolean isSDCardReady() {
		boolean ready = false;
		String sdcardState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
			ready = true;
		}
		return ready;
	}

	public static boolean isSDCardHasRoom() {
		boolean enoughRoom = false;
		if (isSDCardReady()) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long availCount = sf.getAvailableBlocks();
			if (availCount > 0) {
				enoughRoom = true;
			}
		}
		return enoughRoom;
	}
}
