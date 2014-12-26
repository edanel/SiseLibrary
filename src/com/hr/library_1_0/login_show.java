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
				Toast.makeText(login_show.this, "已经续借过，续借失败。", 0).show();
				break;
			case 2:
				Toast.makeText(login_show.this, "续借成功！", 0).show();
				break;
			case 3:
				Toast.makeText(login_show.this, " 过期，请到图书馆续费！", 0).show();
				break;
			case 4:
				Toast.makeText(login_show.this, " 网络错误，异常！", 0).show();
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
		// 得到条码

		// 得到cookie
		mCookie = getIntent().getStringExtra("cookie");
		// 得到学号
		mUsername = getIntent().getStringExtra("username");
		// 得到用户名
		mShowName = getIntent().getStringExtra("showname");
		// 得到借阅数量
		mBooksum = getIntent().getStringExtra("mbooksum");
		// 得到罚金
		mFinesum = getIntent().getStringExtra("mfinesum");
		// 得到List
		ListInfo mListInfo = (ListInfo) getIntent()
				.getSerializableExtra("List");
		mList = mListInfo.getmList();

		mAdapter = new MyAdapter(this);
		mAdapter.notifyDataSetChanged();
		mListView.setAdapter(mAdapter);

		// 设置标签
		mNameTextView.setText(mShowName);
		mBookSumtTextView.setText(mBooksum + " 本");
		mFineSumtTextView.setText(mFinesum + " 元");
		if (mList.size()==0) {
			mishas.setVisibility(mishas.VISIBLE);
			mListView.setVisibility(mListView.GONE);
		}
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				// 获取ListView布局
				convertView = mInflater.inflate(R.layout.login_show_list, null);
				// 书名
				holder.mBookName = (TextView) convertView
						.findViewById(R.id.login_show_list_bookname);
				// 更多按钮
				holder.mMoreButton = (Button) convertView
						.findViewById(R.id.login_show_more_btn);
				// 续借按钮
				holder.mBorrowButton = (Button) convertView
						.findViewById(R.id.login_show_borrow_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.mBookName.setText(mList.get(position).get("mBookName"));
			// 监听更多按钮
			holder.mMoreButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showInfo(position);

				}
			});
			// 监听续借按钮
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
				
			};//run结束
		}.start();//线程结束

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
					if (isSuccess.contains("最大续借")) {
						mHandler.sendEmptyMessage(1);
					}else if(isSuccess.contains("success")){
						mHandler.sendEmptyMessage(2);
					}else  if(isSuccess.contains("过期")){	
						mHandler.sendEmptyMessage(3);
					}else{
						mHandler.sendEmptyMessage(4);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			};//run结束
		}.start();//线程结束
	}
	

	/** 匹配ListView项目 */
	static class ViewHolder {
		public TextView mBookName;
		public Button mMoreButton;
		public Button mBorrowButton;
	}

	public void showInfo(int position) {
		new AlertDialog.Builder(this)
				.setTitle("详情")
				.setMessage(
						"书名：" + mList.get(position).get("mBookName") + "\n作者："
								+ mList.get(position).get("mAuthor") + "\n典藏地："
								+ mList.get(position).get("mPlace") + "\n类型："
								+ mList.get(position).get("mStyle") + "\n首次借阅："
								+ mList.get(position).get("mFirstborrow")
								+ "\n应还时间："
								+ mList.get(position).get("mPostTime")
								+ "\n续借时间："
								+ mList.get(position).get("mSecondPostTime")
								+ "\n续借次数："
								+ mList.get(position).get("mSecondTimeCount")
								+ "\n状态：" + mList.get(position).get("mHow"))
				.setPositiveButton("确定", null).show();
	}

	public void back(View view) {
		finish();
	}

}
