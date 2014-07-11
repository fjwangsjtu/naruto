package cn.bmob.naruto.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class PictureDetailLayout extends RelativeLayout {

	private int nleft;
	private int nwidth;
	private int ntop;
	private int nheight;

	public PictureDetailLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PictureDetailLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PictureDetailLayout(Context context) {
		super(context);
	}

	public int getNleft() {
		return nleft;
	}

	public void setNleft(int nleft) {
		this.nleft = nleft;
		LayoutParams params = (LayoutParams) getLayoutParams();
		params.leftMargin = nleft;
		requestLayout();
	}

	public int getNtop() {
		return ntop;
	}

	public void setNtop(int ntop) {
		this.ntop = ntop;
		LayoutParams params = (LayoutParams) getLayoutParams();
		params.topMargin = ntop;
		requestLayout();
	}

	public int getNwidth() {
		return nwidth;
	}

	public void setNwidth(int nwidth) {
		this.nwidth = nwidth;
		LayoutParams params = (LayoutParams) getLayoutParams();
		params.width = nwidth;
		requestLayout();
	}

	public int getNheight() {
		return nheight;
	}

	public void setNheight(int nheight) {
		this.nheight = nheight;
		LayoutParams params = (LayoutParams) getLayoutParams();
		params.height = nheight;
		requestLayout();
	}

}
