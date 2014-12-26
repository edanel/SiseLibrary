package com.hr.library_1_0;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONArray;

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class chatting extends Activity  implements IXListViewListener {
	private ListView mListView;
	private MyAdapter mAdapter;
	private List<Map<String, String>> mList = new ArrayList<Map<String,String>>();
	
	  List<Map<String, String>> list = new ArrayList<Map<String, String>>();  
	
	private TextView mFeedTextView ;
	/***
	 * 套用XListView框架
	 */
	private XListView mXListView;
	private int pages = 1;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mAdapter.notifyDataSetChanged();
				mXListView.setAdapter(mAdapter);
				break;
			case 2:
				  getFirstData(1);
				break;
			case 3:
				getFirstData(2);
				break;
			case 4:
				mAdapter.notifyDataSetChanged();
				onLoad();
				break;
			case 5:
				onLoad();
				
				break;
			}
		}
	};
	@Override
	protected void onRestart() {
		super.onRestart();
		pages = 1;
		getFirstData(1);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed);
		init();
		getFirstData(1);
	}
	private void init(){
		
		/***
		 * 套用XListview框架
		 */
		mXListView = (XListView) findViewById(R.id.chatting_xlistview);
		mFeedTextView = (TextView) findViewById(R.id.feed_post_tv);
		//mListView = (ListView) findViewById(R.id.chatting_listview);
		//mLoadingdata.setVisibility(mLoadingdata.VISIBLE);
		mAdapter = new MyAdapter(chatting.this);
		mXListView.setPullLoadEnable(true);
		mXListView.setXListViewListener(this);
		mFeedTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(chatting.this, feed_post.class);
				startActivity(intent);
			}
		});
	}

	private void getFirstData(final int pd) {
		new Thread(){
			public void run() {
				try {if (pd == 1) {
					mList = getJSONObject("http://172.16.111.87/xiabancao/api/board/list",1);
					mHandler.sendEmptyMessage(1);
					mHandler.sendEmptyMessage(5);
					}else if (pd ==2) {
						mList = getJSONObject("http://172.16.111.87/xiabancao/api/board/list",2);
						mHandler.sendEmptyMessage(4);
						mHandler.sendEmptyMessage(5);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
    /** 
     * 获取"对象形式"的JSON数据， 
     * 数据形式：{"total":2,"success":true,"arrayData":[{"id":1,"name":"小猪"},{"id":2,"name":"小猫"}]} 
     * @param path  网页路径 
     * @return  返回List 
     * @throws Exception 
     */  
    public  List<Map<String, String>> getJSONObject(String path,int pd) throws Exception {  
      
        Map<String, String> map = null;  
        /*
		URL url = new URL(path);  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 利用HttpURLConnection对象,我们可以从网络中获取网页数据.  
        conn.setConnectTimeout(10 * 1000);   // 单位是毫秒，设置超时时间为5秒  
        conn.setRequestMethod("GET");       // HttpURLConnection是通过HTTP协议请求path路径的，所以需要设置请求方式,可以不设置，因为默认为GET
        if (conn.getResponseCode() == 200) {
        	// 判断请求码是否是200码，否则失败  
            InputStream is = conn.getInputStream(); // 获取输入流  
            byte[] data = readStream(is);   // 把输入流转换成字符数组  
            String json = new String(data); // 把字符数组转换成字符串  
            //数据形式：{"total":2,"success":true,"arrayData":[{"id":1,"name":"小猪"},{"id":2,"name":"小猫"}]}  
         */
        if (pd == 2) {
            pages++;
		}
        Response response =	Jsoup
				.connect("http://172.16.111.87/xiabancao/api/board/list")
				.data("pages",pages+"").method(Method.POST)
				.execute();
        
            JSONObject jsonObject=new JSONObject(response.body());     //返回的数据形式是一个Object类型，所以可以直接转换成一个Object  
            int code=jsonObject.getInt("code");  
            String success = jsonObject.getString("success");
            Log.i("abc", "code:" + code + " | success:" + success);   //测试数据  
            JSONArray jsonArray = jsonObject.getJSONArray("data");//里面有一个数组数据，可以用getJSONArray获取数组  
            if (pd == 1) {
               mList.clear();
			}
            for (int i = 0; i < jsonArray.length(); i++) {  

            	JSONObject item = jsonArray.getJSONObject(i); // 得到每个对象  
                String uid = item.getString("uid");     // 获取需求信息
                String createtime = item.getString("createtime");//时间
                String content = item.getString("content");  
               
                String re_StrTime = null; 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分"); 
				long lcc_time = Long.valueOf(createtime); 
				re_StrTime = sdf.format(new Date(lcc_time * 1000L)); 
				
                map = new HashMap<String, String>(); // 存放到MAP里面  
                map.put("uid", uid+"");  
                map.put("createtime", re_StrTime);  
                map.put("content", content);  
                list.add(map);  
            }  
       // } 
        return list;  
    }  
    public static byte[] readStream(InputStream inputStream) throws Exception {  
    	        ByteArrayOutputStream bout = new ByteArrayOutputStream();  
    	        byte[] buffer = new byte[1024];  
    	        int len = 0;  
    	        while ((len = inputStream.read(buffer)) != -1) {  
    	            bout.write(buffer, 0, len);  
    	        }  
    	        inputStream.close();  
    	        return bout.toByteArray();  
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
						.inflate(R.layout.feed_list, null);
				//头像
				holder.Image = (ImageView) convertView
						.findViewById(R.id.feed_item_iv_avatar);
				//姓名
				holder.Name = (TextView) convertView
						.findViewById(R.id.feed_item_htv_name);
				//时间
				holder.Time = (TextView) convertView
						.findViewById(R.id.feed_item_htv_site);
				//内容
				holder.Content = (TextView) convertView
						.findViewById(R.id.feed_item_etv_content);
				//评论数
			//	holder.Comment = (TextView) convertView
			//			.findViewById(R.id.feed_item_htv_commentcount);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			


			//姓名
			holder.Name.setText(mList.get(position).get("uid"));
			//内容
			holder.Content.setText(mList.get(position).get("content"));
			//时间
			holder.Time.setText(mList.get(position).get("createtime"));
			//评论数
			//holder.Comment.setText(mList.get(position).get("Comment"));
			return convertView;
		}
	}
	/** 匹配ListView项目 */
	static class ViewHolder {
		public ImageView Image;
		public TextView Name;
		public TextView Time;
		public TextView Content;
		public TextView Comment;
	}
	public void back(View view) {
		finish();
	}
	@Override
	public void onRefresh() {
		pages = 1;
       mHandler.sendEmptyMessage(2);
	}
	@Override
	public void onLoadMore() {
		 mHandler.sendEmptyMessage(3);
	}
    private void onLoad() {
        mXListView.stopRefresh();
        mXListView.stopLoadMore();
        mXListView.setRefreshTime("刚刚");
    }
}
