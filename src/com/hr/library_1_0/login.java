package com.hr.library_1_0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.hr.library_1_0.build.ListInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class login extends Activity {
	private EditText mUsernameText;
	private EditText mPasswordText;
	private Button mPostButton;

	private String mUsername;
	private String mPassword;

	private Document mDocument;

	private String mShowName;

	private String mBooksum;
	private String mFinesum;

	private String sessionId;

	private CheckBox mCheckBox_1;
	private CheckBox mCheckBox_2;

	private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(login.this, "登陆成功", 0).show();
				ListInfo mListInfo = new ListInfo();
				mListInfo.setmList(mList);
				Intent intent = new Intent();
				intent.setClass(login.this, login_show.class);

				Bundle bundle = new Bundle();
				bundle.putString("username", mUsername);
				bundle.putString("cookie", sessionId);
				bundle.putString("showname", mShowName);
				bundle.putString("mbooksum", mBooksum);
				bundle.putString("mfinesum", mFinesum);
				bundle.putSerializable("List", mListInfo);

				intent.putExtras(bundle);
				startActivity(intent);
				finish();
				break;
			case 2:
				Toast.makeText(login.this, "学号或密码输入错误", 0).show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		
		if (sp.getBoolean("checked_1", false)&&sp.getBoolean("checked_2", false)) {
			mUsername =sp.getString("username", "");
			mPassword = sp.getString("password", "");
			toLogin();
		}else {
			setContentView(R.layout.login);
			initView();
			toLogin();
		}

	}

	private void initView() {
		mUsernameText = (EditText) findViewById(R.id.login_username);
		mPasswordText = (EditText) findViewById(R.id.login_password);
		mPostButton = (Button) findViewById(R.id.login_post);

		mCheckBox_1 = (CheckBox) findViewById(R.id.login_checked_1);
		mCheckBox_2 = (CheckBox) findViewById(R.id.login_checked_2);
		
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		
		
		mCheckBox_1.setChecked(sp.getBoolean("checked_1", false));
		mCheckBox_2.setChecked(sp.getBoolean("checked_2", false));
		if (mCheckBox_1.isChecked()) {
			mUsernameText.setText(sp.getString("username", ""));
			mPasswordText.setText(sp.getString("password", ""));
			if (mCheckBox_2.isChecked()) {
				mUsername =sp.getString("username", "");
				mPassword = sp.getString("password", "");
				toLogin();
			}
		}


		

		mPostButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mUsername = mUsernameText.getText().toString().trim();
				mPassword = mPasswordText.getText().toString().trim();
				if (TextUtils.isEmpty(mUsername)
						|| TextUtils.isEmpty(mPassword)) {
					Toast.makeText(login.this, "请正确输入学号密码", 0).show();
				} else {
					toCreatChecked();//写入数据
					toLogin();
				}
			}
		});
	}

	private void toCreatChecked() {
		if (mCheckBox_1.isChecked()) {
			toSaveLoginData(login.this,mUsername,mPassword);
			if (mCheckBox_2.isChecked()) {
				SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
				Editor mEditor = sp.edit();
				mEditor.putBoolean("checked_2",true);
				mEditor.commit();
			}
		}else {
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			Editor mEditor = sp.edit();
			mEditor.putBoolean("checked_1", false);
			mEditor.commit();
		}
	}

	
	private static void toSaveLoginData(Context context,String username,String password){
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		Editor mEditor = sp.edit();
		mEditor.putString("username",username);
		mEditor.putString("password", password);
		mEditor.putBoolean("checked_1", true);
		mEditor.commit();
	}

	private void toLogin() {
		new Thread() {
			public void run() {
				try {
					String go = getLoginCookie(mUsername, mPassword);
					if (go.contains("fail")) {
						mHandler.sendEmptyMessage(2);
					} else {
						getData(go);
						mHandler.sendEmptyMessage(1);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			};// run结束
		}.start();// 线程结束
	}

	private String getLoginCookie(String username, String password)
			throws Exception {
		Response res = Jsoup
				.connect("http://172.16.4.188:8081/opac/dzjsjg.jsp")
				.data("dztm", username, "dzmm", password).method(Method.POST)
				.execute();
		Document dcm = res.parse();
		String isSuccess = dcm.select("body").text();
		System.out.println(isSuccess);
		// 判断登陆是否成功
		if (isSuccess.contains("success")) {
			sessionId = res.cookie("JSESSIONID");
			return sessionId;
		} else {
			System.out.println("no");
			return "fail";
		}

	}

	private void getData(String cookie) throws Exception {
		// 首页
		mDocument = Jsoup
				.connect("http://172.16.4.188:8081/opac/index_wdtsg.jsp")
				.cookie("JSESSIONID", cookie).get();

		// 再次抓取数据，获取借阅书名
		// mDocument =
		// Jsoup.connect("http://172.16.4.188:8081/opac/index_wdtsg.jsp").cookie("JSESSIONID",cookie).get();
		mList.clear();
		for (Element element : mDocument.getElementsByClass("bordermain")
				.get(0).select("tbody > tr")) {
			Map<String, String> map = new HashMap<String, String>();
			System.out.println("********");
			if (!element.select("td").toString().equals("")) {
				// 单册条码
				String mDctm = element.select("td").get(1).text();
				// 借阅书名
				String mBookName = element.select("td").get(2).text();
				// 主编
				String mAuthor = element.select("td").get(3).text();
				// 所属书库
				String mPlace = element.select("td").get(6).text();
				// 类型
				String mStyle = element.select("td").get(7).text();
				// 借阅时间
				String mFirstborrow = element.select("td").get(8).text();
				// 应还时间
				String mPostTime = element.select("td").get(9).text();
				// 续借时间
				String mSecondPostTime = element.select("td").get(10).text();
				// 续借次数
				String mSecondTimeCount = element.select("td").get(11).text();
				// 状态
				String mHow = element.select("td").get(12).text();
				// System.out.println(mBookName+mAuthor+mPlace+mStyle+mFirstborrow+mPostTime+mSecondPostTime+mSecondTimeCount+mHow);
				if (mBookName.contains("正题名") == false) {
					map.put("mDctm", mDctm);
					map.put("mBookName", mBookName);
					map.put("mAuthor", mAuthor);
					map.put("mPlace", mPlace);
					map.put("mStyle", mStyle);
					map.put("mFirstborrow", mFirstborrow);
					map.put("mPostTime", mPostTime);
					map.put("mSecondPostTime", mSecondPostTime);
					map.put("mSecondTimeCount", mSecondTimeCount);
					map.put("mHow", mHow);
					mList.add(map);
				}

			}
		}

		mDocument.select("div").get(2).select("a#a12").remove();
		mDocument.select("div").get(2).select("a#a13").remove();
		mDocument.select("div").get(2).select("a#a14").remove();
		mDocument.select("div").get(2).select("a#a15").remove();
		mDocument.select("div").get(2).select("a").get(0).remove();
		// 赋值显示用户名
		mShowName = mDocument.select("div").get(2).text();

		mBooksum = mDocument.select("table").get(0).select("tr").get(1)
				.select("td").get(0).text();
		mBooksum = mBooksum.substring(7, 9);

		// 显示借阅的图书

		// 显示罚金，Note:放在最后
		mDocument.getElementsByClass("bordermain").remove();
		mFinesum = mDocument.select("table").get(0).select("tr").get(3)
				.select("td").get(0).text();
		mFinesum = mFinesum.substring(8, 11);

		// System.out.println(mFinesum);

	}
	public void collection(View view){
		Intent intent = new Intent();
		intent.setClass(login.this, collection.class);
		startActivity(intent);
		finish();
	}
	
	

	public void back(View view) {
		if (mCheckBox_1.isChecked()) {
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			Editor mEditor = sp.edit();
			mEditor.putBoolean("checked_1", true);
			mEditor.commit();
		}else {
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			Editor mEditor = sp.edit();
			mEditor.putBoolean("checked_1", false);
			mEditor.commit();
		}
		login.this.finish();
	}
}
