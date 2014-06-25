package com.app.weibo.widget;

import com.app.weibo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class PullListView extends ListView implements OnScrollListener {
	
	private static final int INIT_REFRESH = 1;			// 初始状态
	private static final int RESET_REFRESH = 2;			// 拉动刷新
	private static final int REFRESHING = 3;			// 正在刷新
	
	private int mRefreshState;							// 当前状态
	private int mScrollState;							// 滚动状态
	
	private LayoutInflater mInflater;
	private RelativeLayout mRefreshView;				// 头部视图对象
	private TextView mRefreshViewText;					// 头部提示文字
	private ImageView mRefreshViewImage;				// 头部提示图片
	private ProgressBar mRefreshViewProcess;			// 头部进度条
	private boolean mRefreshViewShow = false;			// 头部是否显示
	private int mRefreshViewScrollTop;					// 头部的 Top Padding 值
	
	private OnRefreshListener mOnRefreshListener;		// 刷新监听器类

	public PullListView(Context context) {
		super(context);
		init(context);
	}

	public PullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PullListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRefreshView = (RelativeLayout) mInflater.inflate(R.layout.widget_pull_list_head, this, false);
		mRefreshViewText = (TextView) mRefreshView.findViewById(R.id.widget_pull_list_text);
		mRefreshViewImage = (ImageView) mRefreshView.findViewById(R.id.widget_pull_list_image);
		mRefreshViewProcess = (ProgressBar) mRefreshView.findViewById(R.id.widget_pull_list_progress);
		mRefreshState = INIT_REFRESH; // 初始状态
		addHeaderView(mRefreshView);
		super.setOnScrollListener(this);
	}

	private void reset() {
		mRefreshState = RESET_REFRESH; // 初始状态
		setSelection(1);
		mRefreshViewShow = false;
		mRefreshViewText.setText(R.string.widget_pull_list_view_down);
		mRefreshViewImage.setVisibility(View.VISIBLE);
		mRefreshViewProcess.setVisibility(View.GONE);
		mRefreshState = INIT_REFRESH; // 初始状态
	}
	
	@Override
	protected void onAttachedToWindow() {
		reset();
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		reset();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				if (mRefreshViewShow == true && mRefreshViewScrollTop != 0) {
					reset();
				}
				break;
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				break;
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mScrollState = scrollState;
		// when stop scroll
		if (mScrollState == SCROLL_STATE_IDLE) {
			if (mRefreshViewShow == true) {
				if (mRefreshViewScrollTop == 0) {
					mRefreshViewImage.setVisibility(View.GONE);
					mRefreshViewProcess.setVisibility(View.VISIBLE);
					onRefresh();
				} else {
					reset();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (mRefreshState != RESET_REFRESH && mRefreshState != REFRESHING) {
			// when touch scroll
			if (mScrollState == SCROLL_STATE_TOUCH_SCROLL) {
				// show list header
				if (firstVisibleItem == 0) {
					mRefreshViewShow = true;
					mRefreshViewScrollTop = mRefreshView.getTop();
				}
			// when auto scroll
			} else if (mScrollState == SCROLL_STATE_FLING) {
				if (firstVisibleItem == 0) {
					reset();
				}
			}
		}
	}
	
	public void onRefresh() {
		mRefreshState = REFRESHING;
		mRefreshViewText.setText(R.string.widget_pull_list_view_refresh);
		if (mOnRefreshListener != null) {
			mOnRefreshListener.onRefresh();
		}
	}
	
	public void onRefreshComplete() {
		invalidateViews();
		reset();
	}
	
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
    	mOnRefreshListener = onRefreshListener;
	}
	
	public interface OnRefreshListener {
		public void onRefresh();
	}
}