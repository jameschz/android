package com.app.weibo.list;

import java.util.LinkedList;

import com.app.weibo.R;
import com.app.weibo.base.BaseApp;
import com.app.weibo.base.BaseList;
import com.app.weibo.model.Blog;
import com.app.weibo.util.AppCache;
import com.app.weibo.util.AppFilter;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BlogList extends BaseList {

	private BaseApp app;
	private LayoutInflater inflater;
	private LinkedList<Blog> blogList;
	
	public final class BlogListItem {
		public ImageView face;
		public TextView content;
		public TextView uptime;
		public TextView comment;
	}
	
	public BlogList (BaseApp app, LinkedList<Blog> blogList) {
		this.app = app;
		this.inflater = LayoutInflater.from(this.app);
		this.blogList = blogList;
	}
	
	@Override
	public int getCount() {
		return blogList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int p, View v, ViewGroup parent) {
		// init tpl
		BlogListItem  blogItem = null;
		// if cached expired
		if (v == null) {
			v = inflater.inflate(R.layout.tpl_list_blog, null);
			blogItem = new BlogListItem();
			blogItem.face = (ImageView) v.findViewById(R.id.tpl_list_blog_image_face);
			blogItem.content = (TextView) v.findViewById(R.id.tpl_list_blog_text_content);
			blogItem.uptime = (TextView) v.findViewById(R.id.tpl_list_blog_text_uptime);
			blogItem.comment = (TextView) v.findViewById(R.id.tpl_list_blog_text_comment);
			v.setTag(blogItem);
		} else {
			blogItem = (BlogListItem) v.getTag();
		}
		// fill data
		blogItem.uptime.setText(blogList.get(p).getUptime());
		// fill html data
		blogItem.content.setText(AppFilter.getHtml(blogList.get(p).getContent()));
		blogItem.comment.setText(AppFilter.getHtml(blogList.get(p).getComment()));
		// load face image
		String faceUrl = blogList.get(p).getFace();
		Bitmap faceImage = AppCache.getImage(faceUrl);
		if (faceImage != null) {
			blogItem.face.setImageBitmap(faceImage);
		}
		return v;
	}
}