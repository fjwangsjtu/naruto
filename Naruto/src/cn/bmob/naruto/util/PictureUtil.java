package cn.bmob.naruto.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.naruto.AppConfig;

import com.wxnys.util.AppLogEx;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

public class PictureUtil {

	private static final String TAG = "PictureUtil";

	public static void savePicture(Context context, Bitmap bmp) {
		String file_path = AppConfig.SDCARD_FILEFOLDER_DOWNLOAD;
		File dir = new File(file_path);
		if (!dir.exists())
			dir.mkdirs();

		SimpleDateFormat s = new SimpleDateFormat("yyyyMMddhhmmss");
		String format = s.format(new Date()) + ".png";
		String filename = file_path + format;
		File file = new File(filename);
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
			fOut.flush();
			fOut.close();
			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			File f = new File(filename);
			Uri contentUri = Uri.fromFile(f);
			mediaScanIntent.setData(contentUri);
			context.sendBroadcast(mediaScanIntent);
			// Toast.makeText(
			// SlideShowActivity.this,
			// SlideShowActivity.this
			// .getString(R.string.slideshow_save_file)
			// + AppConfig.SDCARD_FILEFOLDER_DOWNLOAD_PROMPT
			// + format, Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sharePicture(Context context, Bitmap bmp) {

	}

	public static void setWallpager(Context context, Bitmap bmp) {
		try {
			WallpaperManager wallpaperManager = WallpaperManager
					.getInstance(context);
			wallpaperManager.setBitmap(bmp);
		} catch (IOException e) {
			AppLogEx.e(TAG, "when set wallpaper", e);
		}
	}
}
