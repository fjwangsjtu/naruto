package cn.bmob.naruto.comic.widget;

import cn.bmob.naruto.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class ComicLayout extends RelativeLayout {

	public ComicLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ComicLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ComicLayout(Context context) {
		super(context);
		init(context);
	}

	private void init(Context ctx) {
		View view = LayoutInflater.from(ctx).inflate(R.layout.view_comic_item,
				this);
	}
}
