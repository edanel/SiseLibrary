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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class search_start extends Activity {
	private Button mSearchButton;
	private EditText mSearchEditText;
	private String mUrl;
	private Document mDocument;
	private String mBookSum;
	private String keywords;

	private RadioGroup mRadioGroup;
	private RadioButton mTitlebButton;
	private RadioButton mCreatorButton;
	private RadioButton mPublisherButton;
	private RadioButton mSshButton;

	private String mSearchType = "title";
	
	private Dialog mDialog;

	private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			
			case 1:
				mDialog.cancelDialog();
				ListInfo mListInfo = new ListInfo();
				mListInfo.setmList(mList);
				Intent intent = new Intent(search_start.this, search_show.class);
				Bundle bundle = new Bundle();
				bundle.putString("booksum", mBookSum);
				bundle.putString("keywords", keywords);
				bundle.putString("mSearchType", mSearchType);
				bundle.putSerializable("List", mListInfo);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case 2:
				Toast.makeText(search_start.this, "未找到相关信息", 0).show();
				break;
			case 3:
				mDialog.showDialog();
				break;
			case 4:
				mDialog.cancelDialog();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_start);
		
		mDialog = new Dialog(search_start.this);
		
		// 初始化控件
		mSearchButton = (Button) findViewById(R.id.search_bt);
		mSearchEditText = (EditText) findViewById(R.id.search_et);

		// 初始化筛选组件
		mRadioGroup = (RadioGroup) findViewById(R.id.search_rgroup);
		mTitlebButton = (RadioButton) findViewById(R.id.search_rgroup_1);
		mCreatorButton = (RadioButton) findViewById(R.id.search_rgroup_2);
		mPublisherButton = (RadioButton) findViewById(R.id.search_rgroup_3);
		mSshButton = (RadioButton) findViewById(R.id.search_rgroup_4);

		// 监听搜索筛选
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.search_rgroup_1) {
					mSearchType = "title";
				} else if (checkedId == R.id.search_rgroup_2) {
					mSearchType = "creator";
				} else if (checkedId == R.id.search_rgroup_3) {
					mSearchType = "Publisher";
				} else if (checkedId == R.id.search_rgroup_4) {
					mSearchType = "ssh";
				}
			}
		});

		// 监听点击搜索事件
		mSearchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 定义关键字 keywords
				keywords = mSearchEditText.getText().toString();

				if (TextUtils.isEmpty(keywords)) {
					Toast.makeText(search_start.this, "请输入关键字", 0).show();
				} else {
					new Thread() {
						public void run() {
							try {
								mHandler.sendEmptyMessage(3);
								toConnect mConnect = new toConnect();
								mDocument = Jsoup
										.connect(
												mConnect.Httpstart(keywords, 1,
														mSearchType))
										.timeout(20000).get();
								// 清空List
								mList.clear();
								if (mDocument.getElementsByClass("xxtable")
										.get(0).select("tbody > tr").isEmpty() == false) {
									// 得到图书总数量
									mBookSum = mDocument.select("table").get(2)
											.select("font").get(0).text();
									// 遍历元素
									for (Element element : mDocument
											.getElementsByClass("xxtable")
											.get(0).select("tbody > tr")) {
										// 定义一个HashMap去存储数据
										Map<String, String> map = new HashMap<String, String>();
										// 如果td项目内的内容不为空
										if (!element.select("td").toString()
												.equals("")) {
											// 获得图书名字
											String text = element.select("td")
													.get(1).select("a").text();
											// 获得图书链接
											String links = element
													.select("input").get(0)
													.attr("value");
											// 移除a元素
											element.select("td").get(1)
													.select("a").remove();
											// 获得图书位置
											String place = element.select("td")
													.get(1).select("B").text();
											// 压入数据
											map.put("title", text);
											map.put("place", place.trim());
											map.put("links",
													"http://172.16.4.188:8081/opac/ckgc.jsp?"
															+ links);
											mList.add(map);
										}
									}// 循环结束
									mHandler.sendEmptyMessage(1);

								}

							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("解析html文档失败");
								mHandler.sendEmptyMessage(2);
								mHandler.sendEmptyMessage(4);
							}
						};
					}.start();
				}
			}
		});
	}

	public void back(View view) {
		search_start.this.finish();
	}
}
