package com.aaroom;

import com.aaroom.model.ShangmeiRetView;
import com.aaroom.service.ShangmeiOTAService;
import com.aaroom.utils.CommonUtil;
import com.aaroom.wechat.service.MemberCommentService;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HotelmanagementCrsApplicationTests {
	@Autowired
	private  ShangmeiOTAService shangmeiOTAService;


	@Test
	public void contextLoads() {
		Map<String, String> paramMap = new HashMap<>();
		//paramMap.put("hotel_code","10001");

		ShangmeiRetView exchange =shangmeiOTAService.exchange("http://120.92.148.25:8099/rest/static/hotel/roomtype/00650", HttpMethod.GET, paramMap, ShangmeiRetView.class);
		Map<String,Object> entity = (Map)exchange.getEntity();
		System.out.println(entity);
		System.out.println(entity.getClass());
		System.out.println(exchange.getClass());
		/*String plainCreds = "aahotel:aahotel849$";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		*//*HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "basic " + base64Creds);
		headers.add("Content-Type","application/json");*//*
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet("http://120.92.148.25:8099/rest/static/hotel/list"+"?page=4&per_page=20");
		get.setHeader("Authorization", "token " + shangmeiOTAService.getOTAToken());
		get.setHeader("Content-Type","application/json");
		HttpResponse response = null;
		Object result = null;
		try {
			response = httpClient.execute(get);
			result= EntityUtils.toString(response.getEntity(),"utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		/*MemberCommentService mc = new MemberCommentService();

		String commentByUtility = mc.getCommentByUtility("1");
		System.out.println(commentByUtility);*/
	}
	public static void main(String[] args) {
		Map<String, String> paramMap = new HashMap<>();
		//paramMap.put("hotel_code","10001");
		ShangmeiOTAService shangmeiOTAService = new ShangmeiOTAService();
		ShangmeiRetView exchange =shangmeiOTAService.exchange("http://120.92.148.25:8099/rest/static/hotel/roomtype/00650", HttpMethod.GET, null, ShangmeiRetView.class);
		Map<String,Object> entity = (Map)exchange.getEntity();
		System.out.println(entity);
		System.out.println(entity.getClass());
		System.out.println(exchange.getClass());
	}

}
