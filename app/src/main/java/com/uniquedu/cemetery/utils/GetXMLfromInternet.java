package com.uniquedu.cemetery.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GetXMLfromInternet {
	/**
     * 得到数据
     * @param path
     * @return InputStream
     */
    public final static InputStream getStream(String path) {
        InputStream stream = null;
    	//确定请求服务器的地址
        //注意在Android模拟器中访问本机服务器时不可以使用localhost与127字段   
        //因为模拟器本身是使用localhost绑定   
        //建立一个URL对象
        URL url;
		try {
			url = new URL(path);
			//得到打开的链接对象
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setReadTimeout(3*1000);
	        conn.setConnectTimeout(6*1000);
	        //设置请求超时与请求方式
	        stream = conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
            return stream;
    }
	}