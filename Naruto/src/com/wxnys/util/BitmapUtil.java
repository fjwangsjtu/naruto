package com.wxnys.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.util.Log;

/**
 * The opertion for Bitmap.
 * 
 * @author wangfujian
 * 
 */
public class BitmapUtil {

	private static final String TAG = "BitmapUtil";

	private static final int DEFAULT_JPEG_QUALITY = 80;// 默认保存图片的质量
	private static final int DEFAUL_JPEG_MAX_WIDTH = 512;// 默认图的最大宽度

	/**
	 * 创建实际尺寸大小的图片
	 * 
	 * @param imgPath
	 *            图片在本地的路径
	 * @return 实际尺寸大小的图片
	 */
	public static Bitmap createActualBitmap(String imgPath) {
		return createBitmap(imgPath, -1, -1);
	}

	/**
	 * 创建一个不大于 {@value #DEFAUL_JPEG_MAX_WIDTH} px宽度的Bitmap
	 * 
	 * @param imgPath
	 *            图片在本地的路径
	 * @return 生成的Bitmap
	 */
	public static Bitmap createBitmap(String imgPath) {
		return createBitmap(imgPath, DEFAUL_JPEG_MAX_WIDTH, -1);
	}

	/**
	 * 创建一个不大于maxWidth宽度的Bitmap
	 * 
	 * @param imgPath
	 *            图片在本地的路径
	 * @param maxWidth
	 *            输出图片的最大宽度(px)
	 * @return 生成的Bitmap
	 */
	public static Bitmap createBitmap(String imgPath, int maxWidth) {
		return createBitmap(imgPath, maxWidth, -1);
	}

	/**
	 * 创建一个 宽度不大于maxWidth 并且 高度不大于maxHeight 的Bitmap.
	 * 
	 * @param imgPath
	 *            图片在本地的路径
	 * @param maxWidth
	 *            输出图片的最大宽度(px). 如果为-1,则忽略最大高度限制
	 * @param maxHeight
	 *            最大高度(px). 如果为-1,则忽略最大高度限制
	 * @return 生成的Bitmap
	 */
	public static Bitmap createBitmap(String imgPath, int maxWidth,
			int maxHeight) {
		if (imgPath == null)
			return null;

		File file = new File(imgPath);
		if (!file.exists())
			return null;

		int degree = getImageDegree(imgPath);
		AppLogEx.d(TAG, "degree:" + degree);
		Matrix matrix = new Matrix();
		matrix.setRotate(degree);

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgPath, opts);

		int srcWidth = opts.outWidth;
		int srcHeight = opts.outHeight;
		if ((degree % 360 + 360) % 360 == 90
				|| (degree % 360 + 360) % 360 == 270) {
			srcWidth = opts.outHeight;
			srcHeight = opts.outWidth;
		}

		int widthSampleSize = 1;// x方向的采样点
		if (maxWidth > 0) {
			widthSampleSize = computeSampleSize(srcWidth, maxWidth);
		}

		int heightSampleSize = 1;// y方向的采样点
		if (maxHeight > 0) {
			heightSampleSize = computeSampleSize(srcHeight, maxHeight);
		}
		opts.inSampleSize = Math.max(widthSampleSize, heightSampleSize);// 取两个采样点中较大的值
		opts.inJustDecodeBounds = false;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath, opts);
		return rotateBitmap(bitmap, degree, true);
	}

	/**
	 * 读取照片exif信息中的旋转角度
	 * 
	 * @param path
	 *            照片路径
	 * @return 角度
	 */
	public static int getImageDegree(String path) {
		if (TextUtils.isEmpty(path))
			return 0;

		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 计算采样点, 最大为128, 最小为1
	 * 
	 * @param srcBitmapWidthPixels
	 *            原图宽度
	 * @param maxWidthPixels
	 *            最小宽度
	 * @return 采样点
	 */
	public static int computeSampleSize(int srcBitmapWidthPixels,
			int maxWidthPixels) {

		if (maxWidthPixels <= 0 || srcBitmapWidthPixels <= 0)
			return 1;

		int initialSize = srcBitmapWidthPixels % maxWidthPixels == 0 ? srcBitmapWidthPixels
				/ maxWidthPixels
				: srcBitmapWidthPixels / maxWidthPixels + 1;

		if (initialSize > 128) {
			initialSize = 128;
		}

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;

	}

	/**
	 * 将bitmap转换为int[]形式的RGBA值
	 * 
	 * @param bm
	 * @return
	 */
	public static int[] getPixels(Bitmap bm) {
		return getPixels(bm, false);
	}

	/**
	 * 将bitmap转换为int[]形式的RGBA值
	 * 
	 * @param bm
	 * @param recycleAfterFinished
	 * @return
	 */
	public static int[] getPixels(Bitmap bm, boolean recycleAfterFinished) {
		if (bm == null)
			return null;

		int width = bm.getWidth();
		int height = bm.getHeight();
		int[] pixels = new int[width * height];
		bm.getPixels(pixels, 0, width, 0, 0, width, height);

		if (recycleAfterFinished) {
			bm.recycle();
			bm = null;
		}

		return pixels;
	}

	/**
	 * 旋转照片
	 * 
	 * @param srcBitmap
	 *            原图
	 * @param degree
	 *            旋转角度
	 * @return 旋转之后的照片
	 */
	public static Bitmap rotateBitmap(Bitmap srcBitmap, int degree) {
		return rotateBitmap(srcBitmap, degree, false);
	}

	/**
	 * 旋转照片
	 * 
	 * @param srcBitmap
	 *            原图
	 * @param degree
	 *            旋转角度
	 * @param recycleRotated
	 *            旋转完图片以后, 是否自动释放原图
	 * @return 旋转之后的照片
	 */
	public static Bitmap rotateBitmap(Bitmap srcBitmap, int degree,
			boolean recycleRotated) {
		if (srcBitmap == null || srcBitmap.isRecycled())
			return null;

		if (degree == 0)
			return srcBitmap;

		Matrix matrix = new Matrix();
		matrix.postRotate(degree);

		Bitmap rotatedBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
				srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);

		if (recycleRotated && rotatedBitmap != srcBitmap) {
			srcBitmap.recycle();
			srcBitmap = null;
		}
		return rotatedBitmap;
	}

	/**
	 * 对图片进行缩放
	 * 
	 * @param srcBitmap
	 *            原图
	 * @param scale
	 *            缩放比例. 如果为1.0或小于等于0, 则不缩放; 如果大于1.0, 则放大; 如果小于1, 则缩小
	 * @return 缩放之后的图片
	 */
	public static Bitmap scaleBitmap(Bitmap srcBitmap, float scale) {
		return scaleBitmap(srcBitmap, scale, false);
	}

	/**
	 * 对图片进行缩放
	 * 
	 * @param srcBitmap
	 *            原图
	 * @param scale
	 *            缩放比例. 如果为1.0或小于等于0, 则不缩放; 如果大于1.0, 则放大; 如果小于1, 则缩小
	 * @param recycleScaled
	 *            缩放完成后, 是否自动释放原图
	 * @return 缩放之后的图片
	 */
	public static Bitmap scaleBitmap(Bitmap srcBitmap, float scale,
			boolean recycleScaled) {

		return scaleBitmap(srcBitmap, scale, scale, recycleScaled);
	}

	/**
	 * 对图片进行等比例缩放, 保证缩放后的图片的宽不大于maxWidth, 并且高度不大于maxHeight
	 * 
	 * @param srcBitmap
	 *            原图
	 * @param maxWidth
	 *            缩放后的最大宽度. 如果小于等于0, 则不缩放
	 * @param maxHeight
	 *            缩放后的最大高度. 如果小于等于0, 则不缩放
	 * @return 缩放之后的图片
	 */
	public static Bitmap scaleBitmap(Bitmap srcBitmap, int maxWidth,
			int maxHeight) {
		return scaleBitmap(srcBitmap, maxWidth, maxHeight, false);
	}

	/**
	 * 对图片进行等比例缩放, 保证缩放后的图片的宽不大于maxWidth, 并且高度不大于maxHeight
	 * 
	 * @param srcBitmap
	 *            原图
	 * @param maxWidth
	 *            缩放后的最大宽度. 如果小于等于0, 则不缩放
	 * @param maxHeight
	 *            缩放后的最大高度. 如果小于等于0, 则不缩放
	 * @param recycleScaled
	 *            缩放完成后, 是否自动释放原图
	 * @return 缩放之后的图片
	 */
	public static Bitmap scaleBitmap(Bitmap srcBitmap, int maxWidth,
			int maxHeight, boolean recycleScaled) {
		if (srcBitmap == null || srcBitmap.isRecycled())
			return null;

		if (maxWidth <= 0 || maxHeight <= 0)
			return srcBitmap;

		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();

		float scaleX = maxWidth * 1.0f / width;
		float scaleY = maxHeight * 1.0f / height;

		if (scaleX > scaleY) {
			scaleX = scaleY;
		}
		scaleY = scaleX;

		return scaleBitmap(srcBitmap, scaleX, scaleX, recycleScaled);
	}

	/**
	 * 对图片进行缩放
	 * 
	 * @param srcBitmap
	 *            原图
	 * @param scaleX
	 *            X缩放比例. 如果为1.0或小于等于0, 则不缩放; 如果大于1.0, 则放大; 如果小于1, 则缩小
	 * @param scaleY
	 *            Y缩放比例. 如果为1.0或小于等于0, 则不缩放; 如果大于1.0, 则放大; 如果小于1, 则缩小
	 * @param recycleScaled
	 *            缩放完成后, 是否自动释放原图
	 * @return 缩放之后的图片
	 */
	public static Bitmap scaleBitmap(Bitmap srcBitmap, float scaleX,
			float scaleY, boolean recycleScaled) {
		if (srcBitmap == null || srcBitmap.isRecycled())
			return null;

		if (scaleX <= 0 || scaleY <= 0)
			return srcBitmap;

		int dstWidth = (int) (srcBitmap.getWidth() * scaleX);
		int dstHeight = (int) (srcBitmap.getHeight() * scaleY);
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(srcBitmap, dstWidth,
				dstHeight, true);

		if (recycleScaled && scaledBitmap != srcBitmap) {
			srcBitmap.recycle();
			srcBitmap = null;
		}

		return scaledBitmap;
	}

	/**
	 * drawable转向bitmap
	 * 
	 * @param drawable
	 * @return drawable转向bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * bitmap转向drawable
	 * 
	 * @param bitmap
	 *            需要转换的Bitmap
	 * @return 转换完成后的drawable
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		return bd;
	}

	/**
	 * 设置透明度
	 * 
	 * @param sourceImg
	 * @param number
	 * @return 设置透明度
	 */
	public static Bitmap setAlpha(Bitmap sourceImg, int number) {
		int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
		sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,
				sourceImg.getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);// 修改最高2位的值
		}
		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(),
				sourceImg.getHeight(), Config.ARGB_8888);

		return sourceImg;
	}

	/**
	 * 创建圆角图片
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return 创建圆角图片
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 创建倒影
	 * 
	 * @param bitmap
	 * @return 创建倒影
	 */
	public static Bitmap createDaoying(Bitmap bitmap) {
		if (bitmap == null)
			return null;
		final int reflectionGap = 0;
		Bitmap originalImage = bitmap;
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 3), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(originalImage, 0, 0, null);
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 3, width, height / 3, matrix, false);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
						+ reflectionGap, 0x70ffffff, 0x00ffffff,
				TileMode.MIRROR);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(
				android.graphics.PorterDuff.Mode.DST_IN));
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		BitmapDrawable bd = new BitmapDrawable(bitmapWithReflection);

		return bd.getBitmap();
	}

	/**
	 * 获得图片的缩略图;从Android 2.2开始系统新增了一个缩略图ThumbnailUtils
	 * 
	 * @param bitmap
	 * @param widht
	 *            ：生成目标的宽度
	 * @param height
	 *            ：生成目标的高度
	 * @return 图片的缩略图
	 */
	public static Bitmap getThumbnail(Bitmap bitmap, int widht, int height) {
		if (bitmap == null)
			return null;
		return ThumbnailUtils.extractThumbnail(bitmap, widht, height);
	}

	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap getGrayBitmap(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * 获取平铺的Bitmap
	 * 
	 * @param context
	 * @param resId
	 * @return 平铺的Bitmap
	 */
	public static BitmapDrawable getRepeatBitmap(Context context, int resId) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				resId);
		BitmapDrawable drawable = new BitmapDrawable(context.getResources(),
				bitmap);
		drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		drawable.setDither(true);
		return drawable;
	}

	/**
	 * 将bitmap保存为jpg格式的图片
	 * 
	 * @param bmp
	 *            需要保存的bmp
	 * @param desFilename
	 *            目的文件路径
	 * @return 保存成功, 则返回true; 否则返回false. 保存完成后, 不会回收 bmp, 需要手动回收bmp.
	 */
	public static boolean saveBitmap(Bitmap bmp, String desFilename) {
		return saveBitmap(bmp, desFilename, Bitmap.CompressFormat.JPEG, false);
	}

	/**
	 * 将bitmap保存为jpg格式的图片
	 * 
	 * @param bmp
	 *            需要保存的bmp
	 * @param desFilename
	 *            目的文件路径
	 * @param recycleBitmapSavedFile
	 *            是否在保存图片后回收bitmap
	 * @return 保存成功, 则返回true; 否则返回false.
	 */
	public static boolean saveBitmap(Bitmap bmp, String desFilename,
			boolean recycleBitmapSavedFile) {
		return saveBitmap(bmp, desFilename, Bitmap.CompressFormat.JPEG,
				recycleBitmapSavedFile);
	}

	/**
	 * 将bitmap保存为png格式的图片
	 * 
	 * @param bmp
	 *            需要保存的bmp
	 * @param desFilename
	 *            目的文件路径
	 * @return 保存成功, 则返回true; 否则返回false. 保存完成后, 不会回收 bmp, 需要手动回收bmp.
	 */
	public static boolean saveBitmapAsPng(Bitmap bmp, String desFilename) {
		return saveBitmap(bmp, desFilename, Bitmap.CompressFormat.PNG, false);
	}

	/**
	 * 将bitmap保存为png格式的图片
	 * 
	 * @param bmp
	 *            需要保存的bmp
	 * @param desFilename
	 *            目的文件路径
	 * @param recycleBitmapSavedFile
	 *            是否在保存图片后回收bitmap
	 * @return 保存成功, 则返回true; 否则返回false.
	 */
	public static boolean saveBitmapAsPng(Bitmap bmp, String desFilename,
			boolean recycleBitmapSavedFile) {
		return saveBitmap(bmp, desFilename, Bitmap.CompressFormat.PNG,
				recycleBitmapSavedFile);
	}

	/**
	 * 将bitmap保存为特定格式的图片
	 * 
	 * @param bmp
	 *            需要保存的bmp
	 * @param desFilename
	 *            目的文件路径
	 * @param format
	 *            保存的图片格式, 如果为空则默认保存为jpg格式
	 * @param recycleBitmapSavedFile
	 *            是否在保存图片后回收bitmap
	 * @return 保存成功, 则返回true; 否则返回false
	 */
	public static boolean saveBitmap(Bitmap bmp, String desFilename,
			Bitmap.CompressFormat format, boolean recycleBitmapSavedFile) {
		try {
			if (bmp == null || bmp.isRecycled()
					|| TextUtils.isEmpty(desFilename)) {
				return false;
			}
			File file = new File(desFilename);
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();

			if (file.exists())
				file.delete();
			file.createNewFile();

			FileOutputStream outStream = new FileOutputStream(file);
			int quality = DEFAULT_JPEG_QUALITY;

			if (format == null) {
				format = Bitmap.CompressFormat.JPEG;
			}

			boolean result = bmp.compress(format, quality, outStream);
			outStream.flush();
			outStream.close();

			if (recycleBitmapSavedFile) {
				bmp.recycle();
				bmp = null;
			}

			// 操作成功, 直接返回
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

		return false;
	}

}
