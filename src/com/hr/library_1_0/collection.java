package com.hr.library_1_0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hr.library_1_0.search_detail.MyAdapter;
import com.hr.library_1_0.search_detail.ViewHolder;
import com.hr.library_1_0.db.db_Info;
import com.hr.library_1_0.db.db_Useful;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class collection extends Activity {

	private int count;
	private List<Map<String, String>> groupList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> childItem = new ArrayList<Map<String,String>>();
	private List<Map<String, String>> condition = new ArrayList<Map<String,String>>();
	private ExpandableListView elv ;
	private ExpandableAdapater mExpandableAdapater ;
	
	private int clickPosition;
	
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				getInfo();
				mExpandableAdapater = new ExpandableAdapater(collection.this);
				mExpandableAdapater.notifyDataSetChanged();
				elv.setAdapter(mExpandableAdapater);
				break;
			}
		}
	};
	//private List<List<Map<String, String>>> childList = new ArrayList<List<Map<String, String>>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection);

		elv = (ExpandableListView) findViewById(R.id.expandablelistview);
		
		/**
		 * 读取收藏数据
		 */
		getInfo();
		mExpandableAdapater = new ExpandableAdapater(collection.this);
		mExpandableAdapater.notifyDataSetChanged();
		elv.setAdapter(mExpandableAdapater);
	}
	
	private void getInfo(){
		childItem.clear();
		db_Useful useful = new db_Useful(this);
		List<db_Info> infos =  useful.findAllData();
		for(db_Info info : infos){
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", info.getTitle());
			map.put("suoshu", info.getSuoshu());
			map.put("isbn", info.getIsbn());
			map.put("push", info.getPush());
			childItem.add(map);
		}
		
	}
	class ExpandableAdapater extends BaseExpandableListAdapter {
		private LayoutInflater mInflater;
		public ExpandableAdapater(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getGroupCount() {
			return childItem.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return childItem.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childItem.get(groupPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupHolder holder = new GroupHolder();
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.collection_group_list,
						null);
				holder.title = (TextView) convertView
						.findViewById(R.id.collection_group_tv);
				convertView.setTag(holder);
			} else {
				holder = (GroupHolder) convertView.getTag();
			}
			holder.title.setText(childItem.get(groupPosition).get("title"));

			return convertView;
		}

		@Override
		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ChildHolder holder = new ChildHolder();
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.collection_child_list,
						null);
				holder.title = (TextView) convertView
						.findViewById(R.id.collection_list_title);
				holder.suoshu = (TextView) convertView
						.findViewById(R.id.collection_list_tv1);
				holder.isbn = (TextView) convertView
						.findViewById(R.id.collection_list_tv2);
				holder.push = (TextView) convertView
						.findViewById(R.id.collection_list_tv3);
				holder.button = (Button) convertView
						.findViewById(R.id.collection_child_btn);

				convertView.setTag(holder);
			} else {
				holder = (ChildHolder) convertView.getTag();
			}
			//监听取消收藏按钮事件
			holder.button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					db_Useful useful = new db_Useful(collection.this);
					useful.deleteData(childItem.get(groupPosition).get("title"));
					mHandler.sendEmptyMessage(1);
					Toast.makeText(collection.this, "取消收藏成功！",0).show();
					
				}
			});
			

			 holder.title.setText(childItem.get(groupPosition).get("title"));
			holder.suoshu.setText(childItem.get(groupPosition).get("suoshu"));
			holder.isbn.setText(childItem.get(groupPosition).get("isbn"));
			 holder.push.setText(childItem.get(groupPosition).get("push"));

			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

	}

	/** 匹配ListView项目 */
	static class GroupHolder {
		public TextView title;
	}

	static class ChildHolder {
		public TextView title;
		public TextView suoshu;
		public TextView isbn;
		public TextView push;
		public Button button;
	}
	
	
	
	
	
	

	public void back(View view) {
		finish();
	}
}
