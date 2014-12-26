package com.hr.library_1_0.build;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.R.integer;

public class ListInfo implements Serializable {
	private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
	
	public void ListInfo(){
		
	}

	public List<Map<String, String>> getmList() {
		return mList;
	}

	public void setmList(List<Map<String, String>> mList) {
		this.mList = mList;
	}

}
