package com.hr.library_1_0_json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {
	  public HttpUtils() { 
	    }
	    public static String getJsonContent(final String path){ 
	    	new Thread(){
				public void run() {
					  try { 
				            URL url = new URL(path); 
				            HttpURLConnection connection = (HttpURLConnection)url.openConnection(); 
				            connection.setConnectTimeout(3000); 
				            connection.setRequestMethod("GET"); 
				            connection.setDoInput(true); 
				            int code = connection.getResponseCode(); 
				            if(code == 200){ 
				               // return changeInputStream(connection.getInputStream()); 
				            } 
				        } catch (MalformedURLException e) { 
				            e.printStackTrace(); 
				        } catch (IOException e) { 
				            e.printStackTrace(); 
				        } 
				       
				};//run结束
			}.start();//结束线程
			 return null; 
	    } 
	 
	    /** 
	     * 将一个输入流转换成指定编码的字符串   
	     * @param inputStream 
	     * @return 
	     */ 
	    private static String changeInputStream(InputStream inputStream) { 
	        String jsonString = ""; 
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
	        int len = 0; 
	        byte[] data = new byte[1024]; 
	        try { 
	            while((len=inputStream.read(data))!=-1){ 
	                outputStream.write(data,0,len); 
	            } 
	            jsonString = new String(outputStream.toByteArray()); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } 
	        return jsonString; 
	    } 
	} 
