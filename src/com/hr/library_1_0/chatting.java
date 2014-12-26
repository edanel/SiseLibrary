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
	 * ����XListView���
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
		 * ����XListview���
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
     * ��ȡ"������ʽ"��JSON���ݣ� 
     * ������ʽ��{"total":2,"success":true,"arrayData":[{"id":1,"name":"С��"},{"id":2,"name":"Сè"}]} 
     * @param path  ��ҳ·�� 
     * @return  ����List 
     * @throws Exception 
     */  
    public  List<Map<String, String>> getJSONObject(String path,int pd) throws Exception {  
      
        Map<String, String> map = null;  
        /*
		URL url = new URL(path);  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();// ����HttpURLConnection����,���ǿ��Դ������л�ȡ��ҳ����.  
        conn.setConnectTimeout(10 * 1000);   // ��λ�Ǻ��룬���ó�ʱʱ��Ϊ5��  
        conn.setRequestMethod("GET");       // HttpURLConnection��ͨ��HTTPЭ������path·���ģ�������Ҫ��������ʽ,���Բ����ã���ΪĬ��ΪGET
        if (conn.getResponseCode() == 200) {
        	// �ж��������Ƿ���200�룬����ʧ��  
            InputStream is = conn.getInputStream(); // ��ȡ������  
            byte[] data = readStream(is);   // ��������ת�����ַ�����  
            String json = new String(data); // ���ַ�����ת�����ַ���  
            //������ʽ��{"total":2,"success":true,"arrayData":[{"id":1,"name":"С��"},{"id":2,"name":"Сè"}]}  
         */
        if (pd == 2) {
            pages++;
		}
        Response response =	Jsoup
				.connect("http://172.16.111.87/xiabancao/api/board/list")
				.data("pages",pages+"").method(Method.POST)
				.execute();
        
            JSONObject jsonObject=new JSONObject(response.body());     //���ص�������ʽ��һ��Object���ͣ����Կ���ֱ��ת����һ��Object  
            int code=jsonObject.getInt("code");  
            String success = jsonObject.getString("success");
            Log.i("abc", "code:" + code + " | success:" + success);   //��������  
            JSONArray jsonArray = jsonObject.getJSONArray("data");//������һ���������ݣ�������getJSONArray��ȡ����  
            if (pd == 1) {
               mList.clear();
			}
            for (int i = 0; i < jsonArray.length(); i++) {  

            	JSONObject item = jsonArray.getJSONObject(i); // �õ�ÿ������  
                String uid = item.getString("uid");     // ��ȡ������Ϣ
                String createtime = item.getString("createtime");//ʱ��
                String content = item.getString("content");  
               
                String re_StrTime = null; 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��HHʱmm��"); 
				long lcc_time = Long.valueOf(createtime); 
				re_StrTime = sdf.format(new Date(lcc_time * 1000L)); 
				
                map = new HashMap<String, String>(); // ��ŵ�MAP����  
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
						.inflate(R.layout.feed_list, null);
				//ͷ��
				holder.Image = (ImageView) convertView
						.findViewById(R.id.feed_item_iv_avatar);
				//����
				holder.Name = (TextView) convertView
						.findViewById(R.id.feed_item_htv_name);
				//ʱ��
				holder.Time = (TextView) convertView
						.findViewById(R.id.feed_item_htv_site);
				//����
				holder.Content = (TextView) convertView
						.findViewById(R.id.feed_item_etv_content);
				//������
			//	holder.Comment = (TextView) convertView
			//			.findViewById(R.id.feed_item_htv_commentcount);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			


			//����
			holder.Name.setText(mList.get(position).get("uid"));
			//����
			holder.Content.setText(mList.get(position).get("content"));
			//ʱ��
			holder.Time.setText(mList.get(position).get("createtime"));
			//������
			//holder.Comment.setText(mList.get(position).get("Comment"));
			return convertView;
		}
	}
	/** ƥ��ListView��Ŀ */
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
        mXListView.setRefreshTime("�ո�");
    }
}
