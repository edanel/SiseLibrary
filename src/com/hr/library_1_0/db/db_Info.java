package com.hr.library_1_0.db;

import android.R.integer;

public class db_Info {
	private int id;
	private String title;
	private String suoshu;
	private String isbn;
	private String push;

	public db_Info() {

	}

	public db_Info(int id, String title, String suoshu, String isbn, String push) {
		this.id = id;
		this.title = title;
		this.suoshu = suoshu;
		this.isbn = isbn;
		this.push = push;
	}

	@Override
	public String toString() {
		return id + title + suoshu + isbn + push;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSuoshu() {
		return suoshu;
	}

	public void setSuoshu(String suoshu) {
		this.suoshu = suoshu;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPush() {
		return push;
	}

	public void setPush(String push) {
		this.push = push;
	}

}
