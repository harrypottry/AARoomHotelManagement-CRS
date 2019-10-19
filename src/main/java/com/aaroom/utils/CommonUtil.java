package com.aaroom.utils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class CommonUtil {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	

    
    


	/************
	 * 获取时间戳
	 *
	 * @return
	 */
	public static String getTimestemp() {
		return new SimpleDateFormat("MMddHHmmss").format(new Date());
	}

	/************
	 * 加密处理
	 *
	 * @param userName
	 * @param password
	 * @param timestemp
	 * @return
	 */
	public static String getKey(String userName, String password, String timestemp) {
		String key = "";
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update("shmskj".getBytes());
			mdTemp.update("033314".getBytes());
			mdTemp.update(timestemp.getBytes());
			key = bytesToHexString(mdTemp.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}

	/**********
	 * 字节数组转16进制字符串
	 *
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte src[]) {
		String resultString = "";
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0)
			return null;
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 255;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2)
				stringBuilder.append(0);
			stringBuilder.append(hv);
		}

		resultString = stringBuilder.toString();
		stringBuilder = null;
		return resultString;
	}
	/**
	 * POST发送消息
	 *
	 * @param Content
	 *            发送的内容
	 * @param _Header
	 *            HTTP头信息
	 * @return HTTPResponse应答
	 */
	public static InputStream SendMessage(byte[] Content, Hashtable _Header, String serverAddress) {
		return SendMessage(Content, _Header, serverAddress, null, 0, null, null);
	}

	public static InputStream SendMessage(byte[] Content, Hashtable _Header, String serverAddress, String proxyHost, int proxyPort, String userName, String password) {
		InputStream _InputStream = null;

		try {
			if (serverAddress == null)
				return null;

			HttpPost _HttpPost = new HttpPost(serverAddress);
			if (_Header != null) {
				Enumeration _List = _Header.keys();
				String keyString = "";
				try {
					while (_List.hasMoreElements()) {
						keyString = (String)_List.nextElement();
						_HttpPost.addHeader(keyString, (String)_Header.get(keyString));
					}
				} catch (NoSuchElementException e) {
					e.printStackTrace();
				}
			}
			_HttpPost.addHeader("Connection", "Keep-Alive");
			_HttpPost.setEntity(new ByteArrayEntity(Content));
			HttpClient _HttpClient = new DefaultHttpClient();
			HttpResponse _HttpResponse = _HttpClient.execute(_HttpPost);

			if (_Header != null) {
				_Header.put("ResponseContentLength", String.valueOf(_HttpResponse.getEntity().getContentLength()));
				Header[] _RespHeader = _HttpResponse.getAllHeaders();
				if (_RespHeader != null && _RespHeader.length > 0) {
					for (Header header : _RespHeader) {
						_Header.put(header.getName(), header.getValue());
					}
				}
			}
			_InputStream = _HttpResponse.getEntity().getContent();
		} catch (Exception e) {
			System.out.println("请求失败，原因：" + e.getMessage() + "请求地址：" + serverAddress);
		}
		return _InputStream;
	}


	/**
	 * Get发送消息
	 *
	 *            发送的内容
	 * @param _Header
	 *            HTTP头信息
	 * @return HTTPResponse应答
	 */
	public static InputStream SendMessage(Hashtable _Header, String serverAddress) {
		return SendMessage(_Header, serverAddress, null, 0, null, null);
	}

	public static InputStream SendMessage(Hashtable _Header, String serverAddress, String proxyHost, int proxyPort, String userName, String password) {
		InputStream _InputStream = null;
		try {
			if (serverAddress == null)
				return null;

			int indexOf = serverAddress.indexOf("http://");
			if (indexOf != 0)
				indexOf = serverAddress.indexOf("https://");

			HttpGet _HttpGet = new HttpGet(indexOf == 0 ? serverAddress : "http://" + serverAddress);
			if (_Header != null) {
				Enumeration _List = _Header.keys();
				String keyString = "";
				try {
					while (_List.hasMoreElements()) {
						keyString = (String)_List.nextElement();
						_HttpGet.addHeader(keyString,(String) _Header.get(keyString));
					}
				} catch (NoSuchElementException e) {
					e.printStackTrace();
				}
			}
			_HttpGet.addHeader("Connection", "Keep-Alive");
			HttpClient _HttpClient = new DefaultHttpClient();
			HttpResponse _HttpResponse = _HttpClient.execute(_HttpGet);
			if (_Header != null) {
				_Header.put("ResponseContentLength", String.valueOf(_HttpResponse.getEntity().getContentLength()));
				Header[] _RespHeader = _HttpResponse.getAllHeaders();
				if (_RespHeader != null && _RespHeader.length > 0) {
					for (Header header : _RespHeader) {
						_Header.put(header.getName(), header.getValue());
					}
				}
			}
			_InputStream = _HttpResponse.getEntity().getContent();
		} catch (Exception e) {
			System.out.println("请求失败，原因：" + e.getMessage());
		}
		return _InputStream;
	}
	/**
	 * 解析返回的流
	 *
	 * @param _InputStream
	 * @return
	 */
	public static String GetResponseString(InputStream _InputStream, String Charset) {
		String response = "error:";
		try {
			if (_InputStream != null) {
				StringBuffer buffer = new StringBuffer();
				InputStreamReader isr = new InputStreamReader(_InputStream, Charset);
				Reader in = new BufferedReader(isr);
				int ch;
				while ((ch = in.read()) > -1) {
					buffer.append((char) ch);
				}
				response = buffer.toString();
				buffer = null;
			} else {
				response = response + "timeout";
			}
		} catch (Exception e) {
			System.out.println("获取响应错误，原因：" + e.getMessage());
			response = response + e.getMessage();
		}
		return response;
	}

	/**
	 * 生成32位UUID(无横线)
	 * @return
	 */
	public static final String createUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 将数据保留两位小数
	 */
	public static double getTwoDecimal(double dnum) {
		DecimalFormat dFormat = new DecimalFormat("#.00");
		String str = dFormat.format(dnum);
		Double tnum = Double.valueOf(str);
		return tnum;
	}
	/**
	 * 获取本月的当前时间和上个月的开始时间结束时间
	 */
	public static Map<String, String> getSimpleDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		String curMonth = sdf.format(c.getTime()); //本月
		c.add(Calendar.MONTH, -1);
		int lastMonthMaxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), lastMonthMaxDay, 23, 59, 59);

		//按格式输出
		String endDate = sdf.format(c.getTime()); //上月最后一天
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-01  00:00:00");
		String startDate = sdf2.format(c.getTime()); //上月第一天
		Map<String, String> map = new HashMap<>();
		map.put("curMonth",curMonth);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

	//字符串转日期格式
	public static Date strToDate(String str, String pattarn) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattarn);
		Date date = null;
		try {
			date =  simpleDateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	//日期格式转字符串
	public static String dateToStr(Date date, String pattarn) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattarn);
		String str = simpleDateFormat.format(date);
		return str;
	}
	//实体类转换Map
	public static Map<String,Object> convertBeanToMap(Object bean) throws IntrospectionException,IllegalAccessException, InvocationTargetException{
		Class type = bean.getClass();
		Map<String,Object> returnMap = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for(int i = 0; i < propertyDescriptors.length; i++){
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if(!propertyName.equals("class")){
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if(result != null){
					returnMap.put(propertyName, result);
				}else{
					returnMap.put(propertyName, null);
				}
			}
		}
			return returnMap;
	}
}
