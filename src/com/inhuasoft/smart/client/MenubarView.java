package com.inhuasoft.smart.client;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenubarView extends FrameLayout {

	private LinearLayout mLayout;
	private ImageView mImageView;
	private TextView mTextView;

	public MenubarView(Context context) {
		super(context, null);
	}

	public MenubarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.MenubarView);
		// int background = getResourceId(ta, R.styleable.MenubarView_background);
		int src = getResourceId(ta, R.styleable.MenubarView_src);
		CharSequence text = ta.getText(R.styleable.MenubarView_text);
		// int textSize = ta.getDimensionPixelSize(R.styleable.MenubarView_text_size, 15);
		ta.recycle();

		LayoutInflater.from(context).inflate(R.layout.menubar, this);
		mLayout = (LinearLayout) findViewById(R.id.menubar_layout);
		mImageView = (ImageView) findViewById(R.id.menubar_image);
		mTextView = (TextView) findViewById(R.id.menubar_text);
		
		mImageView.setImageResource(src);
		mTextView.setText(text);
		mTextView.setTextColor(getColor(R.color.black));
		// mTextView.setTextSize(textSize);
		

	}
	
	public void setSelected(boolean selected) {
		
		mLayout.setSelected(selected);
		mImageView.setSelected(selected);
		if(selected) {
			mLayout.setBackgroundResource(R.drawable.ic_home_bottom_bar_bg);
			mTextView.setTextColor(getColor(R.color.orange));
		}
		else {
			mLayout.setBackgroundDrawable(null);
			mTextView.setTextColor(getColor(R.color.black));
		}
	}
	
//	public void setOnClickListener(OnClickListener l) {
//		mLayout.setOnClickListener(l);
//	}

	public void setText(CharSequence text) {
		mTextView.setText(text);
	}

	public void setText(int resId) {
		mTextView.setText(resId);
	}

	public void setTextColor(int color) {
		mTextView.setTextColor(color);
	}

	public void setTextColor(ColorStateList colors) {
		mTextView.setTextColor(colors);
	}

	private int getResourceId(TypedArray a, int id) {
		TypedValue tv = a.peekValue(id);
		return tv == null ? 0 : tv.resourceId;
	}
	
	private int getColor(int resId) {
		return resId > 0 ? getResources().getColor(resId) : 0;
	}

}
