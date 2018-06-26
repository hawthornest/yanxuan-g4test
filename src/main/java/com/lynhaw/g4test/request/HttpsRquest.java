package com.lynhaw.g4test.request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;

/**
 * @Author yyhu3
 * @Date 2018-04-28 15:39
 */
@Service
public class HttpsRquest {
    Logger logger = Logger.getLogger(HttpsRquest.class);
    private String token = "7a906a1b-835c-4b21-ac61-97231a2bb4b0";
    private String key = "88f9af6bd9b268ae";
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public String paramMd5(String SourceString) throws Exception {
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(SourceString.getBytes());
        byte messageDigest[] = digest.digest();
        return toHexString(messageDigest).toLowerCase();
    }



    public String httpsCallBackPost(String taskId)
    {

        String result = "";
        try {
            HttpClient httpClient = new SSLClient();
            Long timeStamp = System.currentTimeMillis();
            String requestContent = "{\"timestamp\": "+timeStamp+", \"taskId\": \""+taskId+"\"}";
            String sign = paramMd5(requestContent+"|"+key);
            HttpPost httpPost = new HttpPost("https://gotest.hz.netease.com/open/api/history/detail");
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("token",token);
            httpPost.setHeader("sign",sign);
            StringEntity entity = new StringEntity(requestContent);
            logger.info("请求参数为:"+requestContent+",签名为:"+sign);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            logger.info("返回包为:"+response);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,"utf-8");
                }
            }
            System.out.println(result);
            return result;
        } catch (Exception e) {
            OperaException(e);
            return null;
        }
    }

    public void OperaException(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        logger.error(sw.toString());
    }

    public String httpsTaskRunPost(String testId, String environmentId, String callBackUrl)
    {
        String result = "";
        try {
            HttpClient httpClient = new SSLClient();
            Long timeStamp = System.currentTimeMillis();
            String requestContent = "{\"id\": \""+testId+"\", \"cid\": \""+environmentId+"\",\"webhook\":\""+callBackUrl+"\",\"replace\":false,\"timestamp\":"+timeStamp+"}";
            String sign = paramMd5(requestContent+"|"+key);
            HttpPost httpPost = new HttpPost("https://gotest.hz.netease.com/open/api/task/suite/run");
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("token",token);
            httpPost.setHeader("sign",sign);
            StringEntity entity = new StringEntity(requestContent);
            logger.info("请求参数为:"+requestContent+",签名为:"+sign);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            logger.info("返回包为:"+response);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,"utf-8");
                }
            }
            System.out.println(result);
            return result;
        }catch (Exception e) {
            OperaException(e);
            return null;
        }
    }

    public static void main(String[] args) {
        String taskId = "d147d0eb-4ac4-11e8-a4d4-6d0a0c2e1ab9";
        HttpsRquest httpsRquest = new HttpsRquest();
//        String responseResult = httpsRquest.httpsCallBackPost(taskId);
//        HandleResponse handleResponse = new HandleResponse();
//        handleResponse.encapsuReportDetail(responseResult);
        httpsRquest.httpsTaskRunPost("704","1i1284","http://smartipc.you.163.com/smart-ipc");
    }
}
