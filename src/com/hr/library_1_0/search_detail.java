package com.hr.library_1_0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.UserDataHandler;

import com.hr.library_1_0.db.db_Create;
import com.hr.library_1_0.db.db_Useful;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class search_detail extends Activity {
	private Document mDocument;

	private TextView mSuoshu;
	private TextView mISBN;
	private TextView mPush;

	private String mLinks;
	private String myPlace;
	private String myTitle;

	private TextView mPlace;
	private TextView mTitle;
	private TextView mCondition;

	private static int mBorrowed = 0;

	private TextView mDetailSum;

	private ListView mListView;

	private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> mList2 = new ArrayList<Map<String, String>>();

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mTitle.setText(myTitle);
				mSuoshu.setText(myPlace);
				mISBN.setText(mList.get(1).get("title"));
				mPush.setText(mList.get(2).get("title"));
				break;
			case 2:
				mDetailSum.setText(mList2.size() + "  ����¼");
				MyAdapter myAdapter = new MyAdapter(search_detail.this);
				myAdapter.notifyDataSetChanged();
				mListView.setAdapter(myAdapter);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_detail);
		initView();
	}

	private void initView() {
		mSuoshu = (TextView) findViewById(R.id.search_detail_tv1);
		mISBN = (TextView) findViewById(R.id.search_detail_tv2);
		mPush = (TextView) findViewById(R.id.search_detail_tv3);

		mDetailSum = (TextView) findViewById(R.id.search_detail_sum_tv2);

		mPlace = (TextView) findViewById(R.id.search_detail_list_place);
		mTitle = (TextView) findViewById(R.id.search_detail_title);
		mCondition = (TextView) findViewById(R.id.search_detail_list_condition);

		mListView = (ListView) findViewById(R.id.search_detail_listview);

		myTitle = getIntent().getStringExtra("title");
		myPlace = getIntent().getStringExtra("place");
		mLinks = getIntent().getStringExtra("links");
		mLinks = mLinks.replace("zyk_zyk", "kzh=zyk");
		getInfo();
	}

	private void getInfo() {
		new Thread() {
			public void run() {
				try {
					mDocument = Jsoup.connect(mLinks).timeout(5000).get();

					for (Element element : mDocument
							.getElementsByClass("table12").get(0)
							.select("tbody > tr")) {
						// ����һ��HashMapȥ�洢����
						Map<String, String> map = new HashMap<String, String>();
						// ���td��Ŀ�ڵ����ݲ�Ϊ��
						if (!element.select("td").toString().equals("")) {
							String text2 = element.select("td").get(1).text();
							map.put("title", text2);
							mList.add(map);
						}
					}
					// ��ȡListView��Ϣ
					for (Element element : mDocument
							.getElementsByClass("bordermain").get(0)
							.select("tbody > tr")) {
						// ����һ��HashMapȥ�洢����
						Map<String, String> map = new HashMap<String, String>();
						// ���td��Ŀ�ڵ����ݲ�Ϊ��
						if (!element.select("td").toString().equals("")) {
							// ��ص�
							String where = element.select("td").get(4).text();
							// ��������
							String singletype = element.select("td").get(5)
									.text();
							// ״̬
							String ishere = element.select("td").get(7).text();
							map.put("where", where.trim());
							map.put("singletype", singletype.trim());
							map.put("ishere", ishere.trim());
							if (map.containsValue("��ص�") == false) {
								mList2.add(map);
							}
						}
					}
					mHandler.sendEmptyMessage(1);
					mHandler.sendEmptyMessage(2);
				} catch (Exception e) {
					e.printStackTrace();// ����htmlʧ��
				}
			};// ���н���
		}.start();// �߳̽���
	}// getInfo����

	/** ��ʼ��ListView������ */
	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		private MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mList2.size();
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
				convertView = mInflater.inflate(R.layout.search_detail_list,
						null);
				holder.where = (TextView) convertView
						.findViewById(R.id.search_detail_list_place);
				holder.ishere = (TextView) convertView
						.findViewById(R.id.search_detail_list_condition);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.where.setText(mList2.get(position).get("where") + " ("
					+ mList2.get(position).get("singletype") + ")");
			holder.ishere.setText(mList2.get(position).get("ishere"));

			return convertView;
		}

	}

	/** ƥ��ListView��Ŀ */
	static class ViewHolder {
		public ImageView img;
		public TextView where;
		public TextView ishere;
	}

	public void back(View view) {
		search_detail.this.finish();
	}

	public void collection(View view) {
		db_Useful useful = new db_Useful(this);
		if (useful.findData(mTitle.getText().toString())) {
			Toast.makeText(search_detail.this, "�Ѿ��ղع�����", 0).show();
		}else {
			useful.addData(mTitle.getText().toString(), mSuoshu.getText()
					.toString(), mISBN.getText().toString(), mPush.getText()
					.toString());
			Toast.makeText(this, "�ղسɹ���", 0).show();
		}
	}
}
