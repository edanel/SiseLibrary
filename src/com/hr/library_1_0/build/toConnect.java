package com.hr.library_1_0.build;

import java.net.URLEncoder;

import android.R.integer;


public class toConnect {
	/** ³õÊ¼»¯ËÑË÷http */
	public String Httpstart(String Keywords, int Page,String SearchType) {
		String mUrl = null;
		try {
			mUrl = "http://172.16.4.188:8081/opac/jdjsjg.jsp?page="
					+ Page
					+ "&oper=and&addquery=false&changpage=false&geshi=bgfm&ifface=true&filterfl=&filterdcd=&filtersub=&filterkdm=&viewallsub=false&jstj="+SearchType+"&jsc="
					+ URLEncoder.encode(Keywords, "gbk")
					+ "&mypagecount1=1&sort=kdm+desc%2Cdatestr&orderby=desc&mypagecount1=1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mUrl;
	}
	
	public String getNewsUrl(int Page){
		String mUrl = null;
		mUrl = "http://news.sise.com.cn/list.php?id-5-page-"+Page+".html";
		return mUrl;
	}
	public String getItUrl(int Page){
		String mUrl = null;
		mUrl = "http://news.sise.com.cn/list.php?id-37-page-"+Page+".html";
		return mUrl;
	}
	public String getStudyUrl(int Page){
		String mUrl = null;
		mUrl = "http://news.sise.com.cn/list.php?id-17-page-"+Page+".html";
		return mUrl;
	}
}
