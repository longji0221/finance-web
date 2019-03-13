package com.ald.fanbei.unit.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CrawlerTest {
	
	private String baseUrl = "http://localhost:8188";
	
	@Test
    public void render(){
		try {
			List<Header> headers = new ArrayList<>();
	        headers.add(new BasicHeader("content-type", "application/x-www-form-urlencoded; charset=utf-8"));
			
			CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultHeaders(headers).build();
			
	        HttpPost httpPost = new HttpPost( baseUrl+"/third/crawler/render.up" );
	        httpPost.addHeader("User-Agent","Mozilla/5.0");
	        JSONArray jsonArray = new JSONArray();
	        JSONObject object = new JSONObject();
	        
	        object.put("benefitAccount", "lvwlkj205@sina.com");
	        object.put("aliAccount","15057131239");//支付宝账号
	        object.put("orderNo","01110036003110080w125cx");//支付宝交易号
	        object.put("createTime",new Date());//支付宝创建时间
	        object.put("userName","郭帅强");
	        object.put("money","10");//支付宝金额
	        object.put("remark","极速贷还款 15057131239");//备注
	        object.put("remarkAccount","15057131239");
	        
	        jsonArray.add(object);
	        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
	        urlParameters.add(new BasicNameValuePair("alipaylist", jsonArray.toString()));
	        HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
	        httpPost.setEntity(postParams);
	        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
	        System.out.println(EntityUtils.toString(httpResponse.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
