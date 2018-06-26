package com.lynhaw.g4test.service;

import com.alibaba.fastjson.JSON;
import com.lynhaw.g4test.request.HttpsRquest;
import com.lynhaw.g4test.request.SSLClient;
import com.lynhaw.g4test.response.LogInResponse;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author yyhu3
 * @Date 2018-05-08 11:31
 */
@Service
public class LogIn {
    @Autowired
    HttpsRquest httpsRquest;
    private String client_id = "28b3a9985fe411e7b1295cf3fc96a72c";
    Logger logger = Logger.getLogger(LogIn.class);

    public String logInRequset(String userName, String passWord)
    {
        LogInResponse logInResponse =null;
        try {
            String csrftoken="";
            HttpClient httpsClient = new SSLClient();
//            HttpMethod method = new GetMethod("https://login.netease.com/connect/authorize?response_type=code&scope=openid%20fullname%20email&client_id="+client_id+"&redirect_uri=http%3A%2F%2Fyx.mail.netease.com%2Fopenid%2Fcb");
            HttpGet httpsGet = new HttpGet("https://login.netease.com/connect/authorize?response_type=code&scope=openid%20fullname%20email&client_id="+client_id+"&redirect_uri=http%3A%2F%2Fyx.mail.netease.com%2Fopenid%2Fcb");
            httpsGet.addHeader("Connection", "Keep-Alive");
            logger.info("发起登录的第一次请求:");
            HttpResponse getResponse = httpsClient.execute(httpsGet);
            logger.info("登录的第一次请求url为:https://login.netease.com/connect/authorize?response_type=code");
            logger.info("登录的第一次请求httpstatus为:"+getResponse.getStatusLine().getStatusCode());
            List<Cookie> cookies = ((AbstractHttpClient) httpsClient).getCookieStore().getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                if (cookies.get(i).toString().contains("csrftoken"))
                {
                    csrftoken=cookies.get(i).toString();
                    csrftoken = csrftoken.split("value: ")[1].split("]")[0];
                    logger.info("首次请求的token返回是:"+csrftoken);
                }
            }
            httpsGet.releaseConnection();
            HttpPost httpPost = new HttpPost("https://login.netease.com/connect/authorize?response_type=code&scope=openid%20fullname%20email&client_id="+client_id+"&redirect_uri=http%3A%2F%2Fyx.mail.netease.com%2Fopenid%2Fcb");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
            httpPost.setHeader("Cache-Control","max-age=0");
            httpPost.setHeader("Accept-Encoding","gzip, deflate, br");
            httpPost.setHeader("Accept-Language","zh-CN,zh;q=0.8");
            httpPost.setHeader("Referer","https://login.netease.com/connect/authorize?response_type=code&scope=openid%20fullname%20email&client_id=28b3a9985fe411e7b1295cf3fc96a72c&redirect_uri=http%3A%2F%2Fyx.mail.netease.com%2Fopenid%2Fcb");
            httpPost.setHeader("Cookie","csrftoken="+csrftoken);
            String requestContent="csrfmiddlewaretoken="+csrftoken+"&authm=corp&client_id="+client_id+"&redirect_uri=http%3A%2F%2Fyx.mail.netease.com%2Fopenid%2Fcb&response_type=code&scope=openid+fullname+email&state=&corpid="+userName+"&corppw="+passWord+"&allow=submit";
            StringEntity entity = new StringEntity(requestContent);
            httpPost.setEntity(entity);
            logger.info("发起登录的第二次请求:");
            logger.info("登录的第二次请求的url为:https://login.netease.com/connect/authorize?response_type=code&scope=openid");
            HttpResponse postResponse = httpsClient.execute(httpPost);
            Header locationHeader = postResponse.getFirstHeader("Location");
            String location = "";
            if (locationHeader!=null)
            {
                location = locationHeader.getValue();
                logger.info("重定向获取url成功！！！");
            }
            logger.info("获取到的重定向的url为:"+location);
            logger.info("登录的第二次请求httpstatus为:"+postResponse.getStatusLine().getStatusCode());
            httpPost.releaseConnection();
            logger.info("发起登录的第三次请求:"+location);
            HttpGet httpGet = new HttpGet(location);
            HttpResponse getThirdResponse = httpsClient.execute(httpGet);
            List<Cookie> getThirdCookies = ((AbstractHttpClient) httpsClient).getCookieStore().getCookies();
            String YX_CSRF_TOKEN = getCookie(getThirdCookies,"YX_CSRF_TOKEN");
            String YX_OPENID_SESS = getCookie(getThirdCookies,"YX_OPENID_SESS");
            String csrfToken = getCookie(getThirdCookies,"csrftoken");
            String yx_name = getCookie(getThirdCookies,"yx_name");
            String yx_username = getCookie(getThirdCookies,"yx_username");
            logInResponse = new LogInResponse();
            logInResponse.setCsrftoken(csrfToken);
            logInResponse.setYX_CSRF_TOKEN(YX_CSRF_TOKEN);
            logInResponse.setYx_name(yx_name);
            logInResponse.setYX_OPENID_SESS(YX_OPENID_SESS);
            logInResponse.setYx_username(yx_username);
        } catch (Exception e) {
//            HttpsRquest httpsRquest = new HttpsRquest();
            httpsRquest.OperaException(e);
        }
        finally {
            logger.info("cookie返回包为:"+JSON.toJSONString(logInResponse));
            return JSON.toJSONString(logInResponse);
        }
    }

    public String getCookie(List<Cookie> cookies, String cookieKey)
    {
        String cookieValue="";
        for (int i = 0; i < cookies.size(); i++) {
            if (cookies.get(i).toString().contains(cookieKey))
            {
                cookieValue=cookies.get(i).toString();
                cookieValue = cookieValue.split("value: ")[1].split("]")[0];
                logger.info(String.format("获取到的%s的cookie值为:%s",cookieKey,cookieValue));
            }
        }
        return cookieValue;
    }

    public static void main(String[] args)
    {
        LogIn LogIn = new LogIn();
        LogIn.logInRequset("test","test");
    }
}
