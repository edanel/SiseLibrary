package com.hr.library_1_0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.hr.library_1_0.build.ListInfo;
import com.hr.library_1_0.build.toConnect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class search_show extends Activity {
	private ListView mListView;
	private TextView mBookSumTextView;
	private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
	private MyAdapter myAdapter;
	private String keywords;
	private static int page = 2;
	private int mcount;
	private View moreView;
	private int lastItem;
	private String mSearchType;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				myAdapter.notifyDataSetChanged();
				mListView.setAdapter(myAdapter);
				//防止回弹
				mListView.setSelection(mcount);
				mcount = myAdapter.getCount();
				break;
			case 2:
				Loadmore();
				break;
			case 3:
				Toast.makeText(search_show.this, "未能找到更多相关信息", 0).show();
				mListView.removeFooterView(moreView);
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_show);
		initView();
	}
	/**
	 * 初始化
	 */
	private void initView(){
		//获取布局文件
		mListView = (ListView) findViewById(R.id.search_show_listview);
		mBookSumTextView = (TextView) findViewById(R.id.search_show_sum_tv2);
		moreView = LayoutInflater.from(this).inflate(R.layout.moreview, null);
		//得到搜索类型
		mSearchType = getIntent().getStringExtra("mSearchType");
		// 得到图书名
		keywords = getIntent().getStringExtra("keywords");
		// 得到List数据
		ListInfo mListInfo = (ListInfo) getIntent()
				.getSerializableExtra("List");
		//获取搜索到的图书总数目
		String mBookSum = getIntent().getStringExtra("booksum") + "  条记录";
		mBookSumTextView.setText(mBookSum);
		//获取List条目
		mList = mListInfo.getmList();
		mListView.addFooterView(moreView);
		// 监听item
		myAdapter = new MyAdapter(this);
		myAdapter.notifyDataSetChanged();
		mListView.setAdapter(myAdapter);
		mcount = myAdapter.getCount();
		//如果条目不足20，表明搜索结束
		if (mcount<20) {
			mListView.removeFooterView(moreView);
		}
		/** 
		 * 设置滚动监听事件 
		 * */
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (mcount == lastItem && scrollState == this.SCROLL_STATE_IDLE) {
					moreView.setVisibility(moreView.VISIBLE);
					//发送线程消息，启动加载
					mHandler.sendEmptyMessage(2);
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//总条数为21，则 -1
				lastItem = visibleItemCount + firstVisibleItem - 1;
			}
		});
		//监听ListView点击事件
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int mposition = myAdapter.getCount();
				if (position!=mposition) {
					//传入给细节Activity
					Intent intent = new Intent(search_show.this, search_detail.class);			
					Bundle bundle = new Bundle();
					bundle.putString("title", mList.get(position).get("title"));
					bundle.putString("place", mList.get(position).get("place"));
					bundle.putString("links", mList.get(position).get("links"));
					intent.putExtras(bundle);
					//传入给收藏Activity
					Intent collectionIntent = new Intent(search_show.this, collection.class);			
					Bundle collectionBundle = new Bundle();
					collectionBundle.putString("title", mList.get(position).get("title"));
					collectionBundle.putString("place", mList.get(position).get("place"));
					collectionBundle.putString("links", mList.get(position).get("links"));
					collectionIntent.putExtras(collectionBundle);
					
					startActivity(intent);
					
				}
			}
		});
		
	}//initView结束

	/** 初始化ListView适配器 */
	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				//获取ListView布局
				convertView = mInflater
						.inflate(R.layout.search_show_list, null);					
				//书名
				holder.title = (TextView) convertView
						.findViewById(R.id.search_show_list_bookname);
				//ISBN号
				holder.place = (TextView) convertView
						.findViewById(R.id.search_show_list_place);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(mList.get(position).get("title"));
			// holder.push.setText(mList.get(position).get("push"));
			holder.place.setText("索书号："+mList.get(position).get("place"));
			return convertView;
		}
	}
	/** 匹配ListView项目 */
	static class ViewHolder {
		public TextView push;
		public TextView title;
		public TextView place;
		public TextView isbn;
	}

	public void back(View view) {
		search_show.this.finish();
	}

	/** 下拉加载更多数据 */
	private void Loadmore() {
		new Thread() {

			public void run() {

				try {
					Document mDocument;
					toConnect mConnect = new toConnect();
					mDocument = Jsoup
							.connect(mConnect.Httpstart(keywords, page,mSearchType))
							.timeout(10000).get();

					if (mDocument.getElementsByClass("xxtable").get(0)
							.select("tbody > tr").isEmpty() == false) {

						// 遍历图书的标题
						for (Element element : mDocument
								.getElementsByClass("xxtable").get(0)
								.select("tbody > tr")) {
							// 定义一个HashMap去存储数据
							Map<String, String> map = new HashMap<String, String>();
							// 如果td项目内的内容不为空
							if (!element.select("td").toString().equals("")) {
								// 获得图书名字
								String text = element.select("td").get(1)
										.select("a").text();
								// 获得图书链接
								String links = element.select("input").get(0)
										.attr("value");
								// 移除a元素
								element.select("td").get(1).select("a")
										.remove();
								// 获得图书位置
								String place = element.select("td").get(1)
										.select("B").text();
								// 压入数据
								map.put("title", text);
								map.put("place", place);
								map.put("links",
										"http://172.16.4.188:8081/opac/ckgc.jsp?"
												+ links);
								mList.add(map);
							}
						}// 循环结束
						mHandler.sendEmptyMessage(1);
					} else {
						mHandler.sendEmptyMessage(3);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		page++;
		mcount = myAdapter.getCount();
	}
}
