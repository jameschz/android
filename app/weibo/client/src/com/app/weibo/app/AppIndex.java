package com.app.weibo.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.app.weibo.R;
import com.app.weibo.auth.AuthApp;
import com.app.weibo.base.BaseApp;
import com.app.weibo.base.BaseHandler;
import com.app.weibo.base.BaseMessage;
import com.app.weibo.base.BaseTask;
import com.app.weibo.base.C;
import com.app.weibo.list.BlogList;
import com.app.weibo.model.Blog;
import com.app.weibo.widget.PullListView;
import com.app.weibo.widget.PullListView.OnRefreshListener;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.KeyEvent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

public class AppIndex extends AuthApp {

	private LinkedList<Blog> blogItemList;
	private PullListView blogListView;
	private BlogList blogListAdapter;
	private String lastBlogId = "0";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_index);
		
		// set handler
		this.setHandler(new IndexHandler(this));
		
		// tab button
		ImageButton ib = (ImageButton) this.findViewById(R.id.main_tab_1);
		ib.setImageResource(R.drawable.tab_blog_2);
		
		// init app page
		blogItemList = new LinkedList<Blog>();
		blogListView = (PullListView) this.findViewById(R.id.app_index_list_view);
		
		// show init blog list
		HashMap<String, String> blogParams = new HashMap<String, String>();
		blogParams.put("typeId", "0");
		blogParams.put("startId", "0");
		this.doTaskAsync(C.task.blogList, C.api.blogList, blogParams);
	}
	
	public void refreshBlogList () {
		// show all blog list
		HashMap<String, String> blogParams = new HashMap<String, String>();
		blogParams.put("typeId", "0");
		blogParams.put("startId", lastBlogId);
		this.doTaskAsync(C.task.refreshBlogList, C.api.blogList, blogParams);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// async task callback methods
	
	@Override
	@SuppressWarnings("unchecked")
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		switch (taskId) {
			case C.task.blogList:
				try {
					ArrayList<Blog> blogList = (ArrayList<Blog>) message.getResultList("Blog");
					// append to tail
					int i = 0;
					while (i < blogList.size()) {
						Blog blog = blogList.get(i);
						blogItemList.add(blog);
						if (i == 0) {
							lastBlogId = blog.getId();
						}
						i++;
					}
					// load face images
					for (Blog blog : blogItemList) {
						loadImage(blog.getFace());
					}
					// init adapter
					blogListAdapter = new BlogList(this, blogItemList);
					// init list view
					blogListView.setAdapter(blogListAdapter);
					blogListView.setOnRefreshListener(new OnRefreshListener(){
						@Override
						public void onRefresh() {
							refreshBlogList();
						}
					});
					blogListView.setOnItemClickListener(new OnItemClickListener(){
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
							Bundle params = new Bundle();
							if (pos > 0) pos--; // to right position
							params.putString("blogId", blogItemList.get(pos).getId());
							overlay(AppBlog.class, params);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					toast(e.getMessage());
				}
				break;
			case C.task.refreshBlogList:
				try {
					ArrayList<Blog> blogList = (ArrayList<Blog>) message.getResultList("Blog");
					// append to front
					int i = blogList.size() - 1;
					while (i >= 0) {
						Blog blog = blogList.get(i);
						blogItemList.addFirst(blog);
						lastBlogId = blog.getId();
						i--;
					}
				} catch (Exception e) {
					e.printStackTrace();
					toast(e.getMessage());
				}
				// notify list refresh
				blogListView.onRefreshComplete();
				break;
		}
	}
	
	@Override
	public void onNetworkError (int taskId) {
		super.onNetworkError(taskId);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// other methods
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			doFinish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// inner classes
	
	private class IndexHandler extends BaseHandler {
		public IndexHandler(BaseApp app) {
			super(app);
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				switch (msg.what) {
					case BaseTask.LOAD_IMAGE:
						blogListAdapter.notifyDataSetChanged();
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				app.toast(e.getMessage());
			}
		}
	}
}