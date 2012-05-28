package com.emilsjolander.components.StickyScrollViewItems;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

/**
 * 
 * @author Emil Sjšlander - sjolander.emil@gmail.com
 *
 */
public class StickyScrollView extends ScrollView {

	/**
	 * Tag for views that should stick and have constant drawing. e.g. TextViews, ImageViews etc
	 */
	public static final String STICKY_TAG = "sticky";

	/**
	 * Tag for views that should stick and have non-constant drawing. e.g. Buttons, ProgressBars etc
	 */
	public static final String STICKY_TAG_NONCONSTANT = "sticky-nonconstant";

	private ArrayList<View> stickyViews;
	private View currentlyStickingView;
	private FrameLayout wrapper;
	private float stickyViewTopOffset;
	private boolean redirectTouchesToStickyView;
	private boolean clippingToPadding;
	private boolean clipToPaddingHasBeenSet;

	private Runnable invalidateRunnable = new Runnable() {

		@Override
		public void run() {
			if(currentlyStickingView!=null){
				int l = currentlyStickingView.getLeft();
				int t  = currentlyStickingView.getBottom();
				int r = currentlyStickingView.getRight();
				int b = (int) (getScrollY() + (currentlyStickingView.getHeight() + stickyViewTopOffset));
				invalidate(l,t,r,b);
			}
			postDelayed(this, 16);
		}
	};

	public StickyScrollView(Context context) {
		this(context, null);
	}

	public StickyScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.scrollViewStyle);
	}

	public StickyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}

	public void setup(){
		wrapper = new FrameLayout(getContext());
		stickyViews = new ArrayList<View>();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(!clipToPaddingHasBeenSet){
			clippingToPadding = true;
		}
	}

	@Override
	public void setClipToPadding(boolean clipToPadding) {
		super.setClipToPadding(clipToPadding);
		clippingToPadding  = clipToPadding;
		clipToPaddingHasBeenSet = true;
	}

	private void addViewToWrapper(View child){
		if(wrapper.getChildCount()>0) return;
		if (getChildCount() > 0) {
			throw new IllegalStateException("ScrollView can host only one direct child");
		}
		FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		child.setLayoutParams(params);
		wrapper.addView(child);
		findStickyViews(wrapper);
	}

	@Override
	public void addView(View child) {
		addViewToWrapper(child);
		super.addView(wrapper);
	}

	@Override
	public void addView(View child, int index) {
		addViewToWrapper(child);
		super.addView(wrapper, index);
	}

	@Override
	public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
		addViewToWrapper(child);
		super.addView(wrapper, index, params);
	}

	@Override
	public void addView(View child, int width, int height) {
		addViewToWrapper(child);
		super.addView(wrapper, width, height);
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		addViewToWrapper(child);
		super.addView(wrapper, params);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if(currentlyStickingView != null){
			canvas.save();
			canvas.translate(getPaddingLeft(), getScrollY() + stickyViewTopOffset + (clippingToPadding ? getPaddingTop() : 0));
			canvas.clipRect(0, (clippingToPadding ? -stickyViewTopOffset : 0), getWidth(), currentlyStickingView.getHeight());
			currentlyStickingView.draw(canvas);
			canvas.restore();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(ev.getAction()==MotionEvent.ACTION_DOWN){
			redirectTouchesToStickyView = true;
		}

		if(redirectTouchesToStickyView){
			redirectTouchesToStickyView = currentlyStickingView != null;
			if(redirectTouchesToStickyView){
				redirectTouchesToStickyView = 
					ev.getY()<=(currentlyStickingView.getHeight()+stickyViewTopOffset) && 
					ev.getX() >= currentlyStickingView.getLeft() && 
					ev.getX() <= currentlyStickingView.getRight();
			}
		}else if(currentlyStickingView == null){
			redirectTouchesToStickyView = false;
		}
		if(redirectTouchesToStickyView){
			ev.offsetLocation(0, -1*((getScrollY() + stickyViewTopOffset) - currentlyStickingView.getTop()));
		}
		return super.dispatchTouchEvent(ev);
	}

	private boolean hasNotDoneActionDown = true;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(redirectTouchesToStickyView){
			ev.offsetLocation(0, ((getScrollY() + stickyViewTopOffset) - currentlyStickingView.getTop()));
		} 
		
		if(ev.getAction()==MotionEvent.ACTION_DOWN){
			hasNotDoneActionDown = false;
		}
		
		if(hasNotDoneActionDown){
			MotionEvent down = MotionEvent.obtain(ev);
			down.setAction(MotionEvent.ACTION_DOWN);
			super.onTouchEvent(down);
			hasNotDoneActionDown = false;
		}
		
		if(ev.getAction()==MotionEvent.ACTION_UP || ev.getAction()==MotionEvent.ACTION_CANCEL){
			hasNotDoneActionDown = true;
		}
		
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		doTheStickyThing();
	}

	private void doTheStickyThing() {
		View viewThatShouldStick = null;
		View approachingView = null;
		for(View v : stickyViews){
			int viewTop = v.getTop() - getScrollY() + (clippingToPadding ? 0 : getPaddingTop());
			if(viewTop<=0){
				if(viewThatShouldStick==null || viewTop>(viewThatShouldStick.getTop() - getScrollY() + (clippingToPadding ? 0 : getPaddingTop()))){
					viewThatShouldStick = v;
				}
			}else{
				if(approachingView == null || viewTop<(approachingView.getTop() - getScrollY() + (clippingToPadding ? 0 : getPaddingTop()))){
					approachingView = v;
				}
			}
		}
		if(viewThatShouldStick!=null){
			stickyViewTopOffset = approachingView == null ? 0 : Math.min(0, approachingView.getTop() - getScrollY()  + (clippingToPadding ? 0 : getPaddingTop()) - viewThatShouldStick.getHeight());
			if(viewThatShouldStick != currentlyStickingView){
				if(currentlyStickingView!=null){
					stopStickingCurrentlyStickingView();
				}
				startStickingView(viewThatShouldStick);
			}
		}else if(currentlyStickingView!=null){
			stopStickingCurrentlyStickingView();
		}
	}

	private void startStickingView(View viewThatShouldStick) {
		currentlyStickingView = viewThatShouldStick;
		if(currentlyStickingView.getTag().equals(STICKY_TAG_NONCONSTANT)){
			post(invalidateRunnable);
		}
	}

	private void stopStickingCurrentlyStickingView() {
		currentlyStickingView = null;
		removeCallbacks(invalidateRunnable);
	}

	/**
	 * Notify that the sticky attribute has been added or removed from one or more views in the View hierarchy
	 * Also all this after you are done adding/removing any kind of View from the hierarchy within the ScrollView
	 */
	public void notifyHierarchyChanged(){
		if(currentlyStickingView!=null){
			stopStickingCurrentlyStickingView();
		}
		stickyViews.clear();
		findStickyViews(wrapper);
		doTheStickyThing();
		invalidate();
	}

	private void findStickyViews(ViewGroup vg) {
		for(int i = 0 ; i<vg.getChildCount() ; i++){
			String tag = (String) vg.getChildAt(i).getTag();
			if(tag!=null && tag.contains(STICKY_TAG)){
				stickyViews.add(vg.getChildAt(i));
			}else if(vg.getChildAt(i) instanceof ViewGroup){
				findStickyViews((ViewGroup) vg.getChildAt(i));
			}
		}
	}

}
