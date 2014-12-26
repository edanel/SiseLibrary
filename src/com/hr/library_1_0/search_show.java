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
				//��ֹ�ص�
				mListView.setSelection(mcount);
				mcount = myAdapter.getCount();
				break;
			case 2:
				Loadmore();
				break;
			case 3:
				Toast.makeText(search_show.this, "δ���ҵ����������Ϣ", 0).show();
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
	 * ��ʼ��
	 */
	private void initView(){
		//��ȡ�����ļ�
		mListView = (ListView) findViewById(R.id.search_show_listview);
		mBookSumTextView = (TextView) findViewById(R.id.search_show_sum_tv2);
		moreView = LayoutInflater.from(this).inflate(R.layout.moreview, null);
		//�õ���������
		mSearchType = getIntent().getStringExtra("mSearchType");
		// �õ�ͼ����
		keywords = getIntent().getStringExtra("keywords");
		// �õ�List����
		ListInfo mListInfo = (ListInfo) getIntent()
				.getSerializableExtra("List");
		//��ȡ��������ͼ������Ŀ
		String mBookSum = getIntent().getStringExtra("booksum") + "  ����¼";
		mBookSumTextView.setText(mBookSum);
		//��ȡList��Ŀ
		mList = mListInfo.getmList();
		mListView.addFooterView(moreView);
		// ����item
		myAdapter = new MyAdapter(this);
		myAdapter.notifyDataSetChanged();
		mListView.setAdapter(myAdapter);
		mcount = myAdapter.getCount();
		//�����Ŀ����20��������������
		if (mcount<20) {
			mListView.removeFooterView(moreView);
		}
		/** 
		 * ���ù��������¼� 
		 * */
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (mcount == lastItem && scrollState == this.SCROLL_STATE_IDLE) {
					moreView.setVisibility(moreView.VISIBLE);
					//�����߳���Ϣ����������
					mHandler.sendEmptyMessage(2);
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//������Ϊ21���� -1
				lastItem = visibleItemCount + firstVisibleItem - 1;
			}
		});
		//����ListView����¼�
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int mposition = myAdapter.getCount();
				if (position!=mposition) {
					//�����ϸ��Activity
					Intent intent = new Intent(search_show.this, search_detail.class);			
					Bundle bundle = new Bundle();
					bundle.putString("title", mList.get(position).get("title"));
					bundle.putString("place", mList.get(position).get("place"));
					bundle.putString("links", mList.get(position).get("links"));
					intent.putExtras(bundle);
					//������ղ�Activity
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
		
	}//initView����

	/** ��ʼ��ListView������ */
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
				//��ȡListView����
				convertView = mInflater
						.inflate(R.layout.search_show_list, null);					
				//����
				holder.title = (TextView) convertView
						.findViewById(R.id.search_show_list_bookname);
				//ISBN��
				holder.place = (TextView) convertView
						.findViewById(R.id.search_show_list_place);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(mList.get(position).get("title"));
			// holder.push.setText(mList.get(position).get("push"));
			holder.place.setText("����ţ�"+mList.get(position).get("place"));
			return convertView;
		}
	}
	/** ƥ��ListView��Ŀ */
	static class ViewHolder {
		public TextView push;
		public TextView title;
		public TextView place;
		public TextView isbn;
	}

	public void back(View view) {
		search_show.this.finish();
	}

	/** �������ظ������� */
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

						// ����ͼ��ı���
						for (Element element : mDocument
								.getElementsByClass("xxtable").get(0)
								.select("tbody > tr")) {
							// ����һ��HashMapȥ�洢����
							Map<String, String> map = new HashMap<String, String>();
							// ���td��Ŀ�ڵ����ݲ�Ϊ��
							if (!element.select("td").toString().equals("")) {
								// ���ͼ������
								String text = element.select("td").get(1)
										.select("a").text();
								// ���ͼ������
								String links = element.select("input").get(0)
										.attr("value");
								// �Ƴ�aԪ��
								element.select("td").get(1).select("a")
										.remove();
								// ���ͼ��λ��
								String place = element.select("td").get(1)
										.select("B").text();
								// ѹ������
								map.put("title", text);
								map.put("place", place);
								map.put("links",
										"http://172.16.4.188:8081/opac/ckgc.jsp?"
												+ links);
								mList.add(map);
							}
						}// ѭ������
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
