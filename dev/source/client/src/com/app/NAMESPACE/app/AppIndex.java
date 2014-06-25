package com.app.NAMESPACE.app;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.NAMESPACE.R;
import com.app.NAMESPACE.auth.AuthApp;
import com.app.NAMESPACE.base.BaseApp;
import com.app.NAMESPACE.base.BaseHandler;
import com.app.NAMESPACE.base.BaseMessage;
import com.app.NAMESPACE.base.BaseTask;
import com.app.NAMESPACE.base.C;
import com.app.NAMESPACE.list.BlogList;
import com.app.NAMESPACE.model.Blog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.KeyEvent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class AppIndex extends AuthApp {

	private ListView blogListView;
	private BlogList blogListAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_index);
		
		// set handler
		this.setHandler(new IndexHandler(this));
		
		// tab button
		ImageButton ib = (ImageButton) this.findViewById(R.id.main_tab_1);
		ib.setImageResource(R.drawable.tab_blog_2);
	}
	
	public void onStart(){
		super.onStart();
		
		// show all blog list
		HashMap<String, String> blogParams = new HashMap<String, String>();
		blogParams.put("typeId", "0");
		blogParams.put("pageId", "0");
		this.doTaskAsync(C.task.blogList, C.api.blogList, blogParams);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// async task callback methods
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);

		switch (taskId) {
			case C.task.blogList:
				try {
					@SuppressWarnings("unchecked")
					final ArrayList<Blog> blogList = (ArrayList<Blog>) message.getResultList("Blog");
					// load face image
					for (Blog blog : blogList) {
						loadImage(blog.getFace());
					}
					// show text
					blogListView = (ListView) this.findViewById(R.id.app_index_list_view);
					blogListAdapter = new BlogList(this, blogList);
					blogListView.setAdapter(blogListAdapter);
					blogListView.setOnItemClickListener(new OnItemClickListener(){
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
							Bundle params = new Bundle();
							params.putString("blogId", blogList.get(pos).getId());
							overlay(AppBlog.class, params);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					toast(e.getMessage());
				}
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