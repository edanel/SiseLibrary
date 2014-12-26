package com.hr.library_1_0;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources.Theme;

public class Dialog {
	Context context;
	ProgressDialog m_pDialog;

	public Dialog(Context context) {
		this.context = context;
		// ����ProgressDialog����
		m_pDialog = new ProgressDialog(context);
		// ���ý�������񣬷��ΪԲ�Σ���ת��
			//m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		m_pDialog.setMessage("��������...");

		// ����ProgressDialog �Ľ������Ƿ���ȷ
			//m_pDialog.setIndeterminate(false);
		// ����ProgressDialog �Ƿ���԰��˻ذ���ȡ��
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

