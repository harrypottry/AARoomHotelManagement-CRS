package com.aaroom.service;


import com.aaroom.model.ShangmeiRetView;
import com.aaroom.service.impl.RedisCacheService;
import com.aaroom.utils.Constants;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by sosoda on 2019/2/20.
 */
@Service
public class ShangmeiOTAService {


    @Value("${shangmei.ota.clientid}")
    private String clientid;


    @Value("${shangmei.ota.clientsecret}")
    private String clientsecret;

    @Autowired
    private HttpSenderService httpSenderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisCacheService redisCacheService;

    public String getOTAToken() {
        String token = redisCacheService.get(Constants.SHANGMEIOTATOKEN);
       /* if(token!=null && token.length()>0) {
            return token;
        }*/

        String plainCreds = clientid+":"+clientsecret;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "basic " + base64Creds);
        headers.add("Content-Type","application/json");

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<ShangmeiRetView> response = restTemplate.exchange(Constants.GET_TOKEN, HttpMethod.GET, entity, ShangmeiRetView.class);
        ShangmeiRetView<Map<String,Object>> ret = response.getBody();
        if(ret.getError_code()==0) {
            Map<String, Object> entity1 = ret.getEntity();
            redisCacheService.put(Constants.SHANGMEIOTATOKEN, entity1.get("access_token"), 30*60);
            return (String)entity1.get("access_token");
        }else {
            try {
                httpSenderService.sendMsgByPhone("13998420803","【美睡科技】OTAtoken无法获取"+ret.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    public <T,K> ShangmeiRetView<T> sendRequest(String url, HttpMethod method, K params, Class<T> responseType) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + getOTAToken());
        headers.add("Content-Type","application/json");

        HttpEntity<K> entity = new HttpEntity(params,headers);
        ResponseEntity<ShangmeiRetView> response = restTemplate.exchange(url, method, entity, ShangmeiRetView.class);
        ShangmeiRetView body = response.getBody();
        return response.getBody();

    }

    /**
     * 加入访问出错重试机制
     */
    public <T,K> T exchange(String url, HttpMethod method, K params, Class<T> responseType) {

        ShangmeiRetView body = this.sendRequest(url, method, params, responseType);
        if(body.getError_code().equals("1001")) {
            redisCacheService.del(Constants.SHANGMEIOTATOKEN);
            body = this.sendRequest(url, method, params, responseType);
        }
        if (body.getError_code().equals("0")){
            return (T)body.getEntity();
        }
        return (T)body;
    }
}
