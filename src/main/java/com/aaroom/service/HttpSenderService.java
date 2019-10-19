package com.aaroom.service;


import com.aaroom.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;

@Slf4j
@Component
public class HttpSenderService {
	private static final Logger logger = LoggerFactory.getLogger(HttpSenderService.class);


		/**
		 * @throws UnsupportedEncodingException
		 */

		public String sendMsgByPhone(String sendNum, String sendContent) throws UnsupportedEncodingException {
			try {
				String Timestemp = CommonUtil.getTimestemp();// 时间戳
				String Key = CommonUtil.getKey(UserName, Password, Timestemp);// 加密
				// 短信内容。RLEncoder.encode utf-8编码，500个字或字符以内
				String Content = sendContent;
				// 多个手机号码用“,”隔开。（半角逗号）1000个以内（不可有空号）
				String Mobiles = sendNum;
				// （可选）预约时间（不预约的话填写,时间格式yyyymmddhhmmss 20160617234758）
				//String SchTime = "20160617234758";
				// 1~5的整数 从低到高
				int Priority = 5;
				// （可选）批次小号 字符串32位以内
				String PackID = "";
				// （可选）批次号 字符串32位以内。例：群发短信3万，每一个包1000。为这个3万个分配一个PacksID
				// ，每1000包分配一个PackID
				String PacksID = "";
				// （可选）
				String ExpandNumber = "";
				// 短信唯一标识 长整型数字。短信ID，用来匹配状态报告（必须数字类型）
				Long SMSID = System.currentTimeMillis();
				StringBuffer _StringBuffer = new StringBuffer();
				_StringBuffer.append("UserName=" + "shmskj" + "&");
				_StringBuffer.append("Key=" + Key + "&");
				_StringBuffer.append("Timestemp=" + Timestemp + "&");
				_StringBuffer.append("Content=" + URLEncoder.encode(Content, "utf-8") + "&");
				_StringBuffer.append("CharSet=utf-8&");
				_StringBuffer.append("Mobiles=" + Mobiles + "&");
				//_StringBuffer.append("SchTime=" + SchTime + "&");
				_StringBuffer.append("Priority=" + Priority + "&");
				_StringBuffer.append("PackID=" + PackID + "&");
				_StringBuffer.append("PacksID=" + ExpandNumber + "&");
				_StringBuffer.append("ExpandNumber=" + PacksID + "&");
				_StringBuffer.append("SMSID=" + SMSID);
				Hashtable _Header = new Hashtable();
				_Header.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
				//System.out.println(_StringBuffer.toString());
				// post请求

				InputStream _InputStream = CommonUtil.SendMessage(_StringBuffer.toString().getBytes("utf-8"), _Header, "http://www.youxinyun.com:3070/Platform_Http_Service/servlet/SendSms");
				String response = CommonUtil.GetResponseString(_InputStream, "utf-8");
				// 响应的包体
				//System.out.println(response);
				return response;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}



		private String UserName ;
		private String Password ;
		private String serverAddress;

	
	
}
