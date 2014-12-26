package com.hr.library_1_0;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources.Theme;

public class Dialog {
	Context context;
	ProgressDialog m_pDialog;

	public Dialog(Context context) {
		this.context = context;
		// 创建ProgressDialog对象
		m_pDialog = new ProgressDialog(context);
		// 设置进度条风格，风格为圆形，旋转的
			//m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		m_pDialog.setMessage("正在搜索...");

		// 设置ProgressDialog 的进度条是否不明确
			//m_pDialog.setIndeterminate(false);
		// 设置ProgressDialog 是否可以按退回按键取消
		m_pDialog.setCancelable(true);
		m_pDialog.setCanceledOnTouchOutside(false);
	}

	public void showDialog() {
		m_pDialog.show();
	}

	public void showDialog(String str) {
		m_pDialog.setMessage(str);
		m_pDialog.show();
	}

	public void cancelDialog() {
		try {
			m_pDialog.cancel();
		} catch (Exception e) {
		}
	}
}

