package com.hr.library_1_0_json;

public class StudentContent {
	private int bid ;
	private String content;
	private String title;
	private String time;
	private String anonymous;
	
	public StudentContent(){
		super();
	}
	public StudentContent(int bid, String content, String title, String time,
			String anonymous) {
		super();
		this.bid = bid;
		this.content = content;
		this.title = title;
		this.time = time;
		this.anonymous = anonymous;
	}
	@Override
	public String toString() {
		return "StudentContent [bid=" + bid + ", content=" + content
				+ ", title=" + title + ", time=" + time + ", anonymous="
				+ anonymous + "]";
	}
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAnonymous() {
		return anonymous;
	}
	public void setAnonymous(String anonymous) {
		this.anonymous = anonymous;
	}
	
}
