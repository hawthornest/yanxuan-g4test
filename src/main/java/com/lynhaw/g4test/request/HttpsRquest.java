package com.lynhaw.g4test.request;

import com.lynhaw.g4test.service.PublicMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    PublicMethod publicMethod;
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
        logger.info("开始进入gotest回调通知后,获取测试集执行结果");
        Long timeStamp = System.currentTimeMillis();
        String requestContent = "{\"timestamp\": "+timeStamp+", \"taskId\": \""+taskId+"\"}";
        String requestUrl = "https://gotest.hz.netease.com/open/api/history/detail";
        String callBackPostResponse = httpsGotestRequest(requestUrl,requestContent,timeStamp);
        logger.info("获取到的返回包为:"+callBackPostResponse);
        logger.info("结束gotest回调通知信息获取");
        return callBackPostResponse;
    }

    public String httpsTaskRerun(String taskId)
    {
        logger.info("gotest失败重试--------------start");
        Long timeStamp = System.currentTimeMillis();
        String requestContent = "{\"timestamp\": "+timeStamp+", \"taskId\": \""+taskId+"\"}";
        logger.info("请求参数为:"+requestContent);
        String requestUrl = "https://gotest.hz.netease.com/open/api/task/rerun";
        String reRunResponse = httpsGotestRequest(requestUrl,requestContent,timeStamp);
        logger.info("获取到的返回包为:"+reRunResponse);
        logger.info("gotest失败重试--------------end");
        return reRunResponse;
    }

    public String httpsTaskRunPost(String testId, String environmentId, String callBackUrl)
    {
        logger.info("进入执行测试集");
        Long timeStamp = System.currentTimeMillis();
        String requestUrl = "https://gotest.hz.netease.com/open/api/task/suite/run";
        String requestContent;
        if (callBackUrl==null||callBackUrl.equals(""))
        {
            requestContent = "{\"id\": \""+testId+"\", \"cid\": \""+environmentId+"\",\"replace\":false,\"timestamp\":"+timeStamp+"}";
        }
        else
        {
            requestContent = "{\"id\": \""+testId+"\", \"cid\": \""+environmentId+"\",\"webhook\":\""+callBackUrl+"\",\"replace\":false,\"timestamp\":"+timeStamp+"}";
        }
        String taskRunPostResponse = httpsGotestRequest(requestUrl,requestContent,timeStamp);
        logger.info("结束执行测试集");
        return taskRunPostResponse;
    }

    public String httpsTaskSuiteInfo(String testId)
    {
        logger.info("进入获取测试集接口信息");
        Long timeStamp = System.currentTimeMillis();
        String requestContent ="{\"id\":\""+testId+"\",\"timestamp\": "+timeStamp+"}";
        String requestUrl = "https://gotest.hz.netease.com/open/api/suite/projectapi/coverage/info";
        String taskSuiteInfoResponse = httpsGotestRequest(requestUrl,requestContent,timeStamp);
        logger.info("结束获取测试集接口信息");
        return taskSuiteInfoResponse;
    }

    public String httpsGotestRequest(String requestUrl,String requestContent,Long timeStamp)
    {
        String result = "";
        try {
            HttpClient httpClient = new SSLClient();
            String sign = paramMd5(requestContent+"|"+key);
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("token",token);
            httpPost.setHeader("sign",sign);
            StringEntity entity = new StringEntity(requestContent);
            logger.info("请求参数为:"+requestContent+",签名为:"+sign);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,"utf-8");
                }
            }
            logger.info("返回包为:"+result);
            System.out.println(result);
            return result;
        }catch (Exception e) {
            publicMethod.OperaException(e);
            return null;
        }
    }


    public static void main(String[] args) {
        String taskId = "0ba8f9a3-acc8-11e8-b283-87b43dd2860e";
        HttpsRquest httpsRquest = new HttpsRquest();
//        String responseResult = httpsRquest.httpsCallBackPost(taskId);
//        HandleResponse handleResponse = new HandleResponse();
//        handleResponse.encapsuReportDetail(responseResult);
        httpsRquest.httpsTaskSuiteInfo("1024");
    }
}
