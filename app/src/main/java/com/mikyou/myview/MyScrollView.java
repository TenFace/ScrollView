package com.mikyou.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView{
	private OnScrollViewListener onScrollViewListener;

	public MyScrollView(Context context) {
		super(context);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * 定义ScrollView滚动时回调的接口
	 * onScroll方法用于返回的myScrollView滑动的Y方向的距离
	 * */
	public interface OnScrollViewListener{
		public void onScroll(int scrollY);
	}
	/**
	 * 设置滚动时候的接口
	 * */
	public void setOnScrollViewListener(OnScrollViewListener onScrollViewListener) {
		this.onScrollViewListener = onScrollViewListener;
	}
	@Override
	protected int computeVerticalScrollRange() {
		return super.computeVerticalScrollRange();
	}
	/**
	 * 由于ScrollView中的方法onScrollchanged没有被暴露出去，所以不能直接调用它
	 * 所以就写了一个监听回调的接口，在该方法中调用，利用接口中的一个带参数的抽象方法，将
	 * onScrollchanged方法中变化的top值，也即是滑动过程中竖直方向上滑动过的距离即ScrollY
	 * 传出去，即暴露出去供我们调用，从而可以动态地监听到滑动过程中ScrollY值的变化。
	 * */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (onScrollViewListener!=null) {
			onScrollViewListener.onScroll(t);
		}
	}
}
