package cn.bmob.naruto.widget;

import cn.bmob.naruto.R;

import com.wxnys.util.BitmapUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

	Paint mPaint = new Paint();

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CircleImageView(Context context) {
		super(context);
		init(context);
	}

	Bitmap dst = null;
	Bitmap src = null;

	private void init(Context context) {
		dst = BitmapFactory.decodeResource(getResources(), R.drawable.image1)
				.copy(Config.ARGB_4444, true);
		setImageBitmap(dst);
		src = BitmapFactory.decodeResource(getResources(), R.drawable.image2);
	}

	private int radius = 400;

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
		invalidate();
	}

	private int angle = 360;

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
		invalidate();
	}
	
	public void getBitmap(){
		mPaint.reset();
		Canvas canvas = new Canvas(src);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawBitmap(dst, 0, 0, mPaint);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mPaint.reset();
		mPaint.setFilterBitmap(false);
		canvas.drawColor(Color.WHITE);
		canvas.drawPaint(mPaint);
		int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
				Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
						| Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
						| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
						| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
		Drawable drawble = getDrawable();
		if (drawble == null) {
            return; // couldn't resolve the URI
        }
		int mDrawableWidth = drawble.getIntrinsicWidth();
		int mDrawableHeight = drawble.getIntrinsicHeight();
		
        if (mDrawableWidth == 0 || mDrawableHeight == 0) {
            return;     // nothing to draw (empty bounds)
        }

//        if (getImageMatrix() == null && getPaddingTop() == 0 && getPaddingLeft() == 0) {
        	drawble.draw(canvas);
//        } else {
//            int saveCount = canvas.getSaveCount();
//            canvas.save();
//            
//            if (mCropToPadding) {
//                final int scrollX = mScrollX;
//                final int scrollY = mScrollY;
//                canvas.clipRect(scrollX + mPaddingLeft, scrollY + mPaddingTop,
//                        scrollX + mRight - mLeft - mPaddingRight,
//                        scrollY + mBottom - mTop - mPaddingBottom);
//            }
//            
//            canvas.translate(mPaddingLeft, mPaddingTop);
//
//            if (mDrawMatrix != null) {
//                canvas.concat(mDrawMatrix);
//            }
//            mDrawable.draw(canvas);
//            canvas.restoreToCount(saveCount);
//        }
		// Bitmap s = makeSrc(800, 800);
		int w = getWidth();
		int h = getHeight();
		int x = w > h ? h : w;
		Bitmap d = makeDst(x, x);
//		
		// canvas.translate(150, 150);
//		canvas.drawBitmap(d, 0, 0, mPaint);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// canvas.drawBitmap(s, 0, 0, mPaint);
		canvas.drawBitmap(d, 0, 0, mPaint);
		// d.recycle();
		canvas.restoreToCount(sc);
		// invalidate();
	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	// // super.onDraw(canvas);
	// mPaint.reset();
	// canvas.drawColor(Color.WHITE);
	// Log.e("TTT", radius + " : radius");
	// mPaint.setFilterBitmap(false);
	// // Bitmap s = makeSrc(800, 800);
	// int w = getWidth();
	// int h = getHeight();
	// int x = w > h ? h : w;
	// Bitmap d = makeDst(x, x);
	// int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
	// Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
	// | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
	// | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
	// | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
	// // canvas.translate(150, 150);
	// canvas.drawBitmap(d, 0, 0, mPaint);
	// mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	// // canvas.drawBitmap(s, 0, 0, mPaint);
	// canvas.drawBitmap(src, 0, 0, mPaint);
	// // d.recycle();
	// canvas.restoreToCount(sc);
	// }

	// create a bitmap with a circle, used for the "dst" image
	private Bitmap makeDst(int w, int h) {
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

		p.setColor(0xFFFFCC44);
		// c.drawCircle(w/2, h/2, radius, p);
		// c.drawOval(new RectF(0, 0, w * 3 / 4, h * 3 / 4), p);
		c.drawArc(new RectF(0, 0, w, h), 360 - angle, angle, true, p);
		return bm;
	}

	// create a bitmap with a rect, used for the "src" image
	private Bitmap makeSrc(int w, int h) {
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

		p.setColor(0xFF66AAFF);
		c.drawRect(w / 3, h / 3, w * 19 / 20, h * 19 / 20, p);
		return bm;
	}
}
