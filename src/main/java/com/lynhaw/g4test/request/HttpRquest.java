package com.lynhaw.g4test.request;

import com.lynhaw.g4test.service.PublicMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author yyhu3
 * @Date 2018-04-28 15:39
 */
@Service
public class HttpRquest {
    @Autowired
    PublicMethod publicMethod;
    Logger logger = Logger.getLogger(HttpRquest.class);
    public String httpGet(String requestUrl)
    {
        System.out.println("响应结果为:");
        String responseResult = "";
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(requestUrl);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            responseResult = EntityUtils.toString(response.getEntity());
            System.out.println("响应结果为:"+responseResult);
            logger.info("响应结果为:"+responseResult);
        } catch (IOException e) {
            publicMethod.OperaException(e);
        }
        finally {
            return responseResult;
        }
    }

    public String httpPost(String requestUrl, String requestBody, Map<String, String> requestHeader)
    {
        CloseableHttpClient httpClient =  HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(requestUrl);
        for (String key:requestHeader.keySet())
        {
            httpPost.addHeader(key,requestHeader.get(key));
        }
        String responseContent = "";
        try {
            httpPost.setEntity(new StringEntity(requestBody));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            logger.info("响应返回包:"+responseContent);
        } catch (IOException e) {
            publicMethod.OperaException(e);
        }finally {
            return responseContent;
        }
    }

    public static void main(String[] args) {
        HttpRquest httpRquest = new HttpRquest();
//        System.out.println(httpRquest.httpGet("http://smartipc.you.163.com/v2/api-docs"));
        Map<String,String> requestHeaders = new HashMap<String,String>();
        requestHeaders.put("Content-type","application/json");
        String requestUrl = "http://yanxuan-smartipc-wave.test.mail.netease.com/smartipc/wave/getWavesByPushTime.json?source=test&policy=3";
        String requestBody = "{\"channelName\":\"yyhu3test123\",\"pushTime\":1534940700000,\"recvAddress\":{\"city\":\"杭州市\",\"district\":\"滨江区\",\"province\":\"浙江省\"}}";
        System.out.println(httpRquest.httpPost(requestUrl,requestBody,requestHeaders));
    }
}
