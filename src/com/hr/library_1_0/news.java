package com.hr.library_1_0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hr.library_1_0.build.toConnect;
import com.hr.library_1_0.search_show.ViewHolder;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class news extends Activity implements IXListViewListener{
	private RadioGroup mRadioGroup;
	private RadioButton mCollegeButton;
	private RadioButton mItButton;
	private RadioButton mKnowButton;
	
	private XListView mXListView;
	
	private Document mDocument;
	
	private Dialog mDialog;
	
	private int CollegeNews =1;
	private int ITNews =1;
	private int StudyNews =1;
	private int which = 1;
	
	
	private MyAdapter mAdapter;
	
	private List<Map<String, String>> mList = new ArrayList<Map<String,String>>();
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);	
			toConnect mConnect = new toConnect();
			switch (msg.what) {
			case 1:
				mAdapter.notifyDataSetChanged();
				mXListView.setAdapter(mAdapter);
				break;
			case 2:
				mAdapter.notifyDataSetChanged();
				break;
			case 3:
				//mDialog.showDialog("加载中...");
				break;
			case 4:
				//mDialog.cancelDialog();
				break;
			case 5:
				getNewsList(mConnect.getNewsUrl(CollegeNews++),2);
				break;
			case 6:
				getNewsList(mConnect.getItUrl(ITNews++),2);
				break;
			case 7:
				getNewsList(mConnect.getStudyUrl(StudyNews++),2);
				break;
			case 8:
				onLoad();
				break;
			case 9:
				getNewsList(mConnect.getNewsUrl(1),1);
				break;
			case 10:
				getNewsList(mConnect.getItUrl(1),1);
				break;
			case 11:
				getNewsList(mConnect.getStudyUrl(1),1);
				break;
			}
		}
	};
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		initView();
		toConnect mConnect = new toConnect();
		getNewsList(mConnect.getNewsUrl(1),1);
		
	}
	
	private void initView(){
		mRadioGroup = (RadioGroup) findViewById(R.id.news_rgroup);
		mCollegeButton = (RadioButton) findViewById(R.id.news_rbutton_1);
		mItButton = (RadioButton) findViewById(R.id.news_rbutton_2);
		mKnowButton = (RadioButton) findViewById(R.id.news_rbutton_3);
		
		mDialog = new Dialog(news.this);
		
		mXListView = (XListView) findViewById(R.id.news_xlistview);

		
		mAdapter = new MyAdapter(news.this);
		mXListView.setPullLoadEnable(true);
		mXListView.setXListViewListener(this);
		
		
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				toConnect mConnect = new toConnect();
				if (checkedId==R.id.news_rbutton_1) {
					mHandler.sendEmptyMessage(3);
					mList.clear();
					getNewsList(mConnect.getNewsUrl(CollegeNews),2);
					which =1;
					mHandler.sendEmptyMessage(8);
					mHandler.sendEmptyMessage(2);
					mHandler.sendEmptyMessage(4);
				}
				else if (checkedId==R.id.news_rbutton_2) {
					mHandler.sendEmptyMessage(3);
					mList.clear();
					getNewsList(mConnect.getItUrl(ITNews),2);
					which =2;
					mHandler.sendEmptyMessage(8);
					mHandler.sendEmptyMessage(2);
					mHandler.sendEmptyMessage(4);
				}
				else if (checkedId==R.id.news_rbutton_3) {
					mHandler.sendEmptyMessage(3);
					mList.clear();
					getNewsList(mConnect.getStudyUrl(StudyNews),2);
					which =3;
					mHandler.sendEmptyMessage(8);
					mHandler.sendEmptyMessage(2);
					mHandler.sendEmptyMessage(4);
				}
			}
		});
		
		mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int mposition = mAdapter.getCount();
				if (position!=mposition) {
					//传入给细节Activity
					Intent intent = new Intent(news.this, news_detial.class);			
					Bundle bundle = new Bundle();
					bundle.putString("title", mList.get(position-1).get("newstitle"));
					bundle.putString("links", mList.get(position-1).get("newslinks"));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
		
	}
	/**
	 * 得到新闻列表
	 */
	private void getNewsList(final String Url,final int isfirst){
		new Thread(){
			public void run() {

				try {
					mDocument = Jsoup.connect(Url).timeout(50000).get();					
						for (Element element : mDocument.select("div#RB > h2")) {
							String mNewsTitle = element.select("a").get(0).text();
							String mLinks = element.select("a").attr("href");
							element.select("h2").select("a").remove();
							String mNewsTime = element.select("h2").text();
							Map<String, String> map = new HashMap<String, String>();
							map.put("newstitle", mNewsTitle);
							map.put("newslinks", mLinks);
							map.put("newstime", mNewsTime);
							mList.add(map);
					}
						if (isfirst == 1) {
							mHandler.sendEmptyMessage(1);
							mHandler.sendEmptyMessage(8);
						}else if (isfirst == 2) {
							mHandler.sendEmptyMessage(2);
							mHandler.sendEmptyMessage(8);
						}
				} catch (Exception e) {
					e.printStackTrace();//解析html失败
				}
				
			};//run结束
		}.start();//结束线程
		
	}
	
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
						.inflate(R.layout.news_list, null);
				//新闻标题
				holder.newsTitle = (TextView) convertView
						.findViewById(R.id.news_list_title);
				//新闻时间
				holder.newsTime = (TextView) convertView
						.findViewById(R.id.news_list_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.newsTitle.setText(mList.get(position).get("newstitle"));
			holder.newsTime.setText(mList.get(position).get("newstime"));
			return convertView;
		}
	}
	/** 匹配ListView项目 */
	static class ViewHolder {
		public TextView newsTitle;
		public TextView newsTime;
	}
	public void back(View view) {
		finish();
	}
	@Override
	public void onRefresh() {
		if (which ==1) {
			mHandler.sendEmptyMessage(9);
		}else if (which == 2) {
			mHandler.sendEmptyMessage(10);
		}else if (which ==3) {
			mHandler.sendEmptyMessage(11);
		}
	}

	@Override
	public void onLoadMore() {
		if (which ==1) {
			mHandler.sendEmptyMessage(5);
		}else if (which == 2) {
			mHandler.sendEmptyMessage(6);
		}else if (which ==3) {
			mHandler.sendEmptyMessage(7);
		}
		
	}
    private void onLoad() {
        mXListView.stopRefresh();
        mXListView.stopLoadMore();
        mXListView.setRefreshTime("刚刚");
    }
}
