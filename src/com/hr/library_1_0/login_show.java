package com.hr.library_1_0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import com.hr.library_1_0.build.ListInfo;
import com.hr.library_1_0.search_show.MyAdapter;
import com.hr.library_1_0.search_show.ViewHolder;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class login_show extends Activity {
	private TextView mNameTextView;
	private TextView mBookSumtTextView;
	private TextView mFineSumtTextView;

	private ListView mListView;

	private MyAdapter mAdapter;

	private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();

	private String mUsername;
	private String mShowName;
	private String mBooksum;
	private String mFinesum;
	private String mCookie;
	private String mDztm;
	private Button mButton;

	private TextView mishas;
	
	private Document mDocument;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(login_show.this, "�Ѿ������������ʧ�ܡ�", 0).show();
				break;
			case 2:
				Toast.makeText(login_show.this, "����ɹ���", 0).show();
				break;
			case 3:
				Toast.makeText(login_show.this, " ���ڣ��뵽ͼ������ѣ�", 0).show();
				break;
			case 4:
				Toast.makeText(login_show.this, " ��������쳣��", 0).show();
				break;
			}
		};
	};

	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_show);
		initView();
	}

	private void initView() {
		mNameTextView = (TextView) findViewById(R.id.login_show_name_tv);
		mBookSumtTextView = (TextView) findViewById(R.id.login_show_borrownum_tv);
		mFineSumtTextView = (TextView) findViewById(R.id.login_show_finesum_tv);
		mListView = (ListView) findViewById(R.id.login_show_listview);
		
		mButton = (Button) findViewById(R.id.login_show_exit);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
				Editor mEditor = sp.edit();
				mEditor.putBoolean("checked_1",false);
				mEditor.putBoolean("checked_2",false);
				mEditor.commit();
				Intent intent = new Intent();
				intent.setClass(login_show.this, login.class);
				startActivity(intent);
				finish();
				
			}
		});
		
		mishas = (TextView) findViewById(R.id.login_show_ishas);
		// �õ�����

		// �õ�cookie
		mCookie = getIntent().getStringExtra("cookie");
		// �õ�ѧ��
		mUsername = getIntent().getStringExtra("username");
		// �õ��û���
		mShowName = getIntent().getStringExtra("showname");
		// �õ���������
		mBooksum = getIntent().getStringExtra("mbooksum");
		// �õ�����
		mFinesum = getIntent().getStringExtra("mfinesum");
		// �õ�List
		ListInfo mListInfo = (ListInfo) getIntent()
				.getSerializableExtra("List");
		mList = mListInfo.getmList();

		mAdapter = new MyAdapter(this);
		mAdapter.notifyDataSetChanged();
		mListView.setAdapter(mAdapter);

		// ���ñ�ǩ
		mNameTextView.setText(mShowName);
		mBookSumtTextView.setText(mBooksum + " ��");
		mFineSumtTextView.setText(mFinesum + " Ԫ");
		if (mList.size()==0) {
			mishas.setVisibility(mishas.VISIBLE);
			mListView.setVisibility(mListView.GONE);
		}
	}

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
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				// ��ȡListView����
				convertView = mInflater.inflate(R.layout.login_show_list, null);
				// ����
				holder.mBookName = (TextView) convertView
						.findViewById(R.id.login_show_list_bookname);
				// ���ఴť
				holder.mMoreButton = (Button) convertView
						.findViewById(R.id.login_show_more_btn);
				// ���谴ť
				holder.mBorrowButton = (Button) convertView
						.findViewById(R.id.login_show_borrow_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.mBookName.setText(mList.get(position).get("mBookName"));
			// �������ఴť
			holder.mMoreButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showInfo(position);

				}
			});
			// �������谴ť
			holder.mBorrowButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					toBorrow(position);
					//test();
				}
			});

			return convertView;
		}
	}
	
	private void test(){
		new Thread(){
			public void run() {
				try {
					mDocument = Jsoup
							.connect(
									"http://172.16.4.188:8081/opac/index_wdtsg.jsp")
							.cookie("JSESSIONID",
									mCookie)
							.get();
					System.out.println(mDocument);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			};//run����
		}.start();//�߳̽���

	}
	
	private void  toBorrow(final int position){
		new Thread(){
			public void run() {
				try {
					 String mDctm =mList.get(position).get("mDctm");
					 mDctm =mDctm.substring(0, 8);
					// System.out.println(mDctm);
					Response res = Jsoup
							.connect(
									"http://172.16.4.188:8081/opac/dzxj.jsp").cookie("JSESSIONID", mCookie)
							.data("dztm", mUsername, "dctm",
									mDctm)
							.method(Method.POST).execute();
					mDocument =res.parse();
					String isSuccess = mDocument.select("body").text();
					System.out.println(isSuccess);
					if (isSuccess.contains("�������")) {
						mHandler.sendEmptyMessage(1);
					}else if(isSuccess.contains("success")){
						mHandler.sendEmptyMessage(2);
					}else  if(isSuccess.contains("����")){	
						mHandler.sendEmptyMessage(3);
					}else{
						mHandler.sendEmptyMessage(4);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			};//run����
		}.start();//�߳̽���
	}
	

	/** ƥ��ListView��Ŀ */
	static class ViewHolder {
		public TextView mBookName;
		public Button mMoreButton;
		public Button mBorrowButton;
	}

	public void showInfo(int position) {
		new AlertDialog.Builder(this)
				.setTitle("����")
				.setMessage(
						"������" + mList.get(position).get("mBookName") + "\n���ߣ�"
								+ mList.get(position).get("mAuthor") + "\n��صأ�"
								+ mList.get(position).get("mPlace") + "\n���ͣ�"
								+ mList.get(position).get("mStyle") + "\n�״ν��ģ�"
								+ mList.get(position).get("mFirstborrow")
								+ "\nӦ��ʱ�䣺"
								+ mList.get(position).get("mPostTime")
								+ "\n����ʱ�䣺"
								+ mList.get(position).get("mSecondPostTime")
								+ "\n���������"
								+ mList.get(position).get("mSecondTimeCount")
								+ "\n״̬��" + mList.get(position).get("mHow"))
				.setPositiveButton("ȷ��", null).show();
	}

	public void back(View view) {
		finish();
	}

}
